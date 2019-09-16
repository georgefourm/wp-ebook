package com.georgesdoe.wpebook.ui

import com.georgesdoe.wpebook.ebook.BookWriterService
import com.georgesdoe.wpebook.wp.{Category, Client, Post}
import javafx.concurrent.{Service, Task}
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{ListView, TableColumn, TableView, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class MainWindowController(categoryList: ListView[Category], urlField: TextField, postsTable: TableView[Post],
                           titleColumn: TableColumn[Post, String], urlColumn: TableColumn[Post, String], dateColumn: TableColumn[Post, String]) {

  private val categories = new ObservableBuffer[Category]()
  categoryList.items = categories

  private val posts = new ObservableBuffer[Post]()
  postsTable.items = posts

  titleColumn.cellValueFactory = cell => {
    StringProperty(cell.value.title.rendered)
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
      client.fetchCategories(70)
    }
  }

  private val fetchPostsService = new Service[List[Post]]() {
    override def createTask(): Task[List[Post]] = () => {
      val client = new Client(urlField.text.value)
      client.fetchPosts(categoryList.selectionModel.value.getSelectedItem.id)
    }
  }

  private val writeBookService = new Service[Unit] {
    override def createTask(): Task[Unit] = () => {
      BookWriterService.write(posts.toList)
    }
  }

  fetchPostsService.setOnSucceeded(_ => {
    posts.clear()
    posts.addAll(fetchPostsService.getValue)
    fetchPostsService.reset()
  })

  fetchCategoriesService.setOnSucceeded(_ => {
    categories.clear()
    categories.addAll(fetchCategoriesService.getValue)
    fetchCategoriesService.reset()
  })

  writeBookService.setOnSucceeded(_ => {
    writeBookService.reset()
  })

  def fetchCategories(event: ActionEvent): Unit = {
    fetchCategoriesService.start()
  }

  def fetchPosts(event: ActionEvent): Unit = {
    fetchPostsService.start()
  }

  def writeBook(event: ActionEvent): Unit = {
    writeBookService.start()
  }

}
