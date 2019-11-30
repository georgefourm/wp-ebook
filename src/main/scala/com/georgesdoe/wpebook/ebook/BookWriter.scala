package com.georgesdoe.wpebook.ebook

import java.io.{File, FileOutputStream}

import com.georgesdoe.wpebook.wp.Post
import nl.siegmann.epublib.domain.{Book, Resource}
import nl.siegmann.epublib.epub.EpubWriter
import nl.siegmann.epublib.service.MediatypeService
import scalatags.Text.all._
import scalatags.Text.tags2.title

object BookWriter {

  def write(posts: List[Post], file: File): Unit = {
    val book = new Book;
    val metadata = book.getMetadata
    metadata.addTitle("WordPress Ebook")
    posts.foreach(post => {
      val xhtml = postToHtml(post)
      book.addSection(post.title.unescaped, new Resource(xhtml.getBytes, MediatypeService.XHTML));
    })
    val writer = new EpubWriter()
    writer.write(book, new FileOutputStream(file.getPath))
  }

  def postToHtml(post: Post): String = {
    val tree = html(
      xmlns:= "http://www.w3.org/1999/xhtml",
      head(
        title(post.title.unescaped)
      ),
      body(
        h1(post.title.unescaped),
        raw(post.content.unescaped)
      )
    )

    val doctype =
      """
        |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        |"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        |
        |""".stripMargin
    doctype + "\n" + tree.toString()
  }
}
