package com.georgesdoe.wpebook.ui

import java.io.File

import com.georgesdoe.wpebook.ui.service.BookWriterService
import com.georgesdoe.wpebook.wp.{Category, Client, Post}
import javafx.concurrent.{Service, Task}
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, ButtonType, ListView, TableColumn, TableView, TextField}
import scalafx.stage.DirectoryChooser
import scalafxml.core.macros.sfxml

@sfxml
class MainWindowController(categoryList: ListView[Category], urlField: TextField, postsTable: TableView[Post], postsCount: TextField,
                           titleColumn: TableColumn[Post, String], urlColumn: TableColumn[Post, String], dateColumn: TableColumn[Post, String]) {

  private val categories = new ObservableBuffer[Category]()
  categoryList.items = categories

  private val posts = new ObservableBuffer[Post]()
  postsTable.items = posts

  titleColumn.cellValueFactory = cell => {
    StringProperty(cell.value.title.unescaped)
  }

  urlColumn.cellValueFactory = cell => {
    StringProperty(cell.value.link.toString)
  }

  dateColumn.cellValueFactory = cell => {
    StringProperty(cell.value.date.toString)
  }

  private val fetchCategoriesService = new Service[List[Category]]() {
    override def createTask(): Task[List[Category]] = () => {
      val client = new Client(urlField.text.value)
      client.fetchCategories(100)
    }
  }

  private val fetchPostsService = new Service[List[Post]]() {
    override def createTask(): Task[List[Post]] = () => {
      val client = new Client(urlField.text.value)
      val number = postsCount.text.value.toIntOption
      val categoryId = categoryList.selectionModel.value.getSelectedItem.id
      client.fetchPosts(categoryId, number.get)
    }
  }

  fetchPostsService.setOnSucceeded(_ => {
    posts.clear()
    posts.addAll(fetchPostsService.getValue)
    fetchPostsService.reset()
  })

  fetchPostsService.setOnFailed(_ => {
    showError("Error Fetching Posts", fetchPostsService.getException)
    fetchPostsService.reset()
  })

  fetchCategoriesService.setOnSucceeded(_ => {
    categories.clear()
    categories.addAll(fetchCategoriesService.getValue)
    fetchCategoriesService.reset()
  })

  fetchCategoriesService.setOnFailed(_ => {
    showError("Error Fetching Categories", fetchCategoriesService.getException)
    fetchCategoriesService.reset()
  })

  private def showError(message: String, exception: Throwable): Option[ButtonType] = {
    new Alert(AlertType.Error) {
      title = "Error"
      headerText = message
      contentText = exception.getMessage
    }.showAndWait()
  }

  private def showMessage(message: String,title: String): Option[ButtonType] ={
    new Alert(AlertType.Information) {
      title
      contentText = message
    }.showAndWait()
  }

  def fetchCategories(event: ActionEvent): Unit = {
    fetchCategoriesService.start()
  }

  def fetchPosts(event: ActionEvent): Unit = {
    fetchPostsService.start()
  }

  def writeBook(event: ActionEvent): Unit = {
    val scene = categoryList.getScene.getWindow
    val chooser = new DirectoryChooser()
    val output = chooser.showDialog(scene)

    if(output == null) return

    val service = new BookWriterService(posts.toList, new File(
      output, "output.epub"
    ))

    service.setOnFailed(_ => {
      showError("Failed Writing Ebook",service.getException)
    })
    service.setOnSucceeded(_ => {
      showMessage(s"Wrote ebook to ${output.getPath}","Success")
    })
    service.start()
  }

}
