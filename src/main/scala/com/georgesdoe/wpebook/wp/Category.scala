package com.georgesdoe.wpebook.wp

case class Category(id: Int,name: String,slug: String){
  override def toString: String = s"$name ($slug)"
}
