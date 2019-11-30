package com.georgesdoe.wpebook.ui.service

import java.io.File

import com.georgesdoe.wpebook.ebook.BookWriter
import com.georgesdoe.wpebook.wp.Post
import javafx.concurrent.Task
import scalafx.concurrent.Service

class BookWriterService(posts: List[Post], output: File) extends Service(new javafx.concurrent.Service[Unit]() {
  override def createTask(): Task[Unit] = () => {
    BookWriter.write(posts, output)
  }
})
