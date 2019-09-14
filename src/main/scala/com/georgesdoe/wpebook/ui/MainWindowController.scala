package com.georgesdoe.wpebook.ui

import com.georgesdoe.wpebook.wp.{Category, Client}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, ListView, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class MainWindowController(categoryList: ListView[Category],
                            fetchButton: Button,
                           urlField: TextField) {

  val categories = new ObservableBuffer[Category]()

  def handleFetch(event: ActionEvent): Unit ={
    val client = new Client(urlField.text.value)
    val allCategories = client.fetchCategories(70)
    categories.clear()
    categories.addAll(allCategories)
    categoryList.items = categories;
  }

}
