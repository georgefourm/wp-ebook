package com.georgesdoe.wpebook.wp

import scala.jdk.CollectionConverters._
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

case class HtmlContent(rendered: String) {
  def text(): String = {
    val elements = Jsoup.parse(rendered).select("p")
    elements.eachText().asScala.reduce((current, other) => current + "\n" + Parser.unescapeEntities(other,true))
  }
}
