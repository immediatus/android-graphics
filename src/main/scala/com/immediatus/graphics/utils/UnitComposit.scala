package com.immediatus.graphics.utils

import scala.collection.mutable.ListBuffer

trait UnitComposit {

  private var _parent : UnitComposit = null
  private var _children = ListBuffer[UnitComposit]()

  private var _onAttached: UnitComposit => Unit = null
  private var _onDetached: UnitComposit => Unit = null

  def hasParent = _parent != null

  def parent = _parent

  protected def parent_=(unit: UnitComposit) = _parent = unit

  def childCount = _children.size

  def child(index: Int) = _children(index)

  def firstChild = _children.head

  def lastChild = _children.last

  def findChild(f: UnitComposit => Boolean) = _children.find(f)

  def detachSelf() = if(_parent != null) _parent.detachChild(this)

  def detachChild(f: UnitComposit => Boolean): Unit = _children.find(f).map(detachChild)

  def detachChildren() = _children.foreach(detachChild)

  def detachChild(item: UnitComposit): Unit = {
    _children -= item
    item._parent = null
    onDetached(item)
  }

  def attachChild(item: UnitComposit) {
    _children += item
    item.parent = this
    onAttached(item)
  }

  def sortChildren(f: (UnitComposit, UnitComposit) => Boolean) = _children.sortWith(f)

  def onAttached(f: UnitComposit => Unit) = _onAttached = f

  def onDetached(f: UnitComposit => Unit) = _onDetached = f

  protected def onAttached(item: UnitComposit) = if(_onAttached != null) _onAttached(item)

  protected def onDetached(item: UnitComposit) = if(_onDetached != null) _onDetached(item)
}
