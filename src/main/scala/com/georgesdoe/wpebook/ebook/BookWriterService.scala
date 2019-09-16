package com.georgesdoe.wpebook.ebook

import java.io.FileOutputStream

import com.georgesdoe.wpebook.wp.Post
import nl.siegmann.epublib.domain.{Book, Resource}
import nl.siegmann.epublib.epub.EpubWriter
import nl.siegmann.epublib.service.MediatypeService

import scalatags.Text.all._
import scalatags.Text.tags2.title

object BookWriterService {

  def write(posts: List[Post]): Unit = {
    val book = new Book;
    val metadata = book.getMetadata
    metadata.addTitle("Creepypasta Collection")
    posts.foreach(post => {
      val xhtml = postToHtml(post)
      book.addSection(post.title.rendered, new Resource(xhtml.getBytes, MediatypeService.XHTML));
    })
    val writer = new EpubWriter()
    writer.write(book, new FileOutputStream("output/collection.epub"))
  }

  def postToHtml(post: Post): String ={
    val tree = html(
      head(
        title(post.title.rendered)
      ),
      body(
        post.content.rendered
      )
    )

    val doctype =
      """
        |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        |"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        |
        |<html xmlns="http://www.w3.org/1999/xhtml">
        |""".stripMargin
    doctype + "\n" + tree.toString()
  }
}
