package com.immediatus.utils

import scala.util.Random
import scala.collection.mutable.Stack

abstract class GenericPool[T](initialSize: Int, private val _growth: Int) {

   private val _availableItems = Stack[T]()
   private var _unrecycledCount = 0

   private val _lock : AnyRef = new Object()

   if(_growth < 0) throw new IllegalArgumentException("growth must be at least 0!")
   if(initialSize > 0) batchAllocatePoolItems(initialSize)

   def this(size: Int) = this(size, 1)
   def this() = this(0)

   def unrecycledCount: Int = _lock.synchronized { _unrecycledCount }

   protected def onAllocatePoolItem(): T

   protected def onHandleRecycleItem(item: T): Unit = { }

   protected def onHandleAllocatePoolItem(): T = onAllocatePoolItem()

   protected def onHandleObtainItem(item: T) : Unit = { }

   def batchAllocatePoolItems(count: Int) = _lock.synchronized {
      (1 to count).foreach(_availableItems.push(onHandleAllocatePoolItem()))
   }

   def obtainPoolItem(): T = _lock.synchronized {
      val item =
         if(_availableItems.size > 0) _availableItems.pop()
         else {
            if(_growth == 1) onHandleAllocatePoolItem()
            else {
               batchAllocatePoolItems(_growth)
               _availableItems.pop()
            }
         }

      onHandleObtainItem(item)
      _unrecycledCount += 1
      item
   }

   def recyclePoolItem(item: T) = _lock.synchronized {
      if (item == null) throw new IllegalArgumentException("Cannot recycle null item!")
      onHandleRecycleItem(item)
      _availableItems.push(item)
      _unrecycledCount -= 1
    }
}
