package com.georgesdoe.wpebook.wp

import java.net.URL
import java.time.LocalDateTime
import scala.jdk.CollectionConverters._
import org.jsoup.Jsoup

case class HtmlContent(rendered: String) {
  def text(): String = {
    val elements = Jsoup.parse(rendered).select("p")
    elements.eachText().asScala.reduce((current, other) => current + "\n" + other)
  }
}

case class Post(id: Int, link: URL, date: LocalDateTime, title: HtmlContent, content: HtmlContent)
