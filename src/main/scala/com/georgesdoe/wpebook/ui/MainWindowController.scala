package com.georgesdoe.wpebook.ui

import java.util.concurrent.{ExecutorService, Executors}

import com.georgesdoe.wpebook.wp.{Category, Client}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, ListView, TextField}
import scalafxml.core.macros.sfxml

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}

@sfxml
class MainWindowController(categoryList: ListView[Category],
                            fetchButton: Button,
                           urlField: TextField) {

  val categories = new ObservableBuffer[Category]()

  val singleThreadedPool: ExecutorService = Executors.newSingleThreadExecutor()

  def handleFetch(event: ActionEvent): Unit ={
    val client = new Client(urlField.text.value)

    implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(singleThreadedPool)
    val fetch = Future{
       client.fetchCategories(70)
    }

    fetch.onComplete(list => {
      categories.clear()
      categories.addAll(list.get)
      categoryList.items = categories;
    })

  }

}
