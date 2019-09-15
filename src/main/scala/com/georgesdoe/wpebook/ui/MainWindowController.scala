package com.georgesdoe.wpebook.ui

import com.georgesdoe.wpebook.wp.{Category, Client}
import javafx.concurrent.{Service, Task}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, ListView, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class MainWindowController(categoryList: ListView[Category],
                           fetchButton: Button,
                           urlField: TextField) {

  private val categories = new ObservableBuffer[Category]()
  categoryList.items = categories

  private val fetchCategoriesService = new Service[List[Category]]() {
    override def createTask(): Task[List[Category]] = () => {
      val client = new Client(urlField.text.value)
      client.fetchCategories(70)
    }
  }

  fetchCategoriesService.setOnSucceeded(_ => {
    categories.clear()
    categories.addAll(fetchCategoriesService.getValue)
    fetchCategoriesService.reset()
  })

  def handleFetch(event: ActionEvent): Unit = {
    fetchCategoriesService.start()
  }

}
