package com.georgesdoe.wpebook.wp

import java.net.URL
import java.time.LocalDateTime

import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, RootJsonFormat}

object WordPressProtocol extends DefaultJsonProtocol {

  implicit object urlFormat extends JsonFormat[URL] {
    override def read(json: JsValue): URL = json match {
      case JsString(value) => new URL(value)
      case _ => throw new RuntimeException(s"Could not parse JSON string: ${json.toString()}")
    }

    override def write(url: URL): JsValue = JsString(url.toString)
  }

  implicit object dateTimeFormat extends JsonFormat[LocalDateTime] {
    override def read(json: JsValue): LocalDateTime = json match {
      case JsString(value) => LocalDateTime.parse(value)
      case _ => throw new RuntimeException(s"Could not parse JSON string: ${json.toString()}")
    }

    override def write(dateTime: LocalDateTime): JsValue = new JsString(dateTime.toString)
  }

  implicit val htmlFormat: RootJsonFormat[HtmlContent] = jsonFormat1(HtmlContent)
  implicit val postFormat: RootJsonFormat[Post] = jsonFormat5(Post)
  implicit val categoryFormat: RootJsonFormat[Category] = jsonFormat3(Category)
}
