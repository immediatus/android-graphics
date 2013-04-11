package com.immediatus.utils

import scala.collection.mutable.ListBuffer

trait UnitComposit[T] {
   _self: T =>

   private var _parent : UnitComposit[T] = null
   private var _children = ListBuffer[UnitComposit[T]]()

   private var _onAttached: UnitComposit[T] => Unit = null
   private var _onDetached: UnitComposit[T] => Unit = null

   def self: T = _self

   def hasParent = _parent != null

   def parent: UnitComposit[T] = _parent

   protected def parent_=(unit: UnitComposit[T]) = _parent = unit

   def childCount = _children.size

   def child(index: Int) = _children(index)

   def firstChild = _children.head

   def lastChild = _children.last

   def findChild(f: UnitComposit[T] => Boolean) = _children.find(f)

   def detachSelf() = if(_parent != null) _parent.detachChild(this)

   def detachChild(f: UnitComposit[T] => Boolean): Unit = _children.find(f).map(detachChild)

   def detachChildren() = _children.foreach(detachChild)

   def detachChild(item: UnitComposit[T]): Unit = {
     _children -= item
     item._parent = null
     onDetached(item)
   }

   def attachChild(item: UnitComposit[T]) {
     _children += item
     item.parent = this
     onAttached(item)
   }

   def sortChildren(f: (UnitComposit[T], UnitComposit[T]) => Boolean) = _children.sortWith(f)

   def onAttached(f: UnitComposit[T] => Unit) = _onAttached = f

   def onDetached(f: UnitComposit[T] => Unit) = _onDetached = f

   protected def onAttached(item: UnitComposit[T]) = if(_onAttached != null) _onAttached(item)

   protected def onDetached(item: UnitComposit[T]) = if(_onDetached != null) _onDetached(item)
}
