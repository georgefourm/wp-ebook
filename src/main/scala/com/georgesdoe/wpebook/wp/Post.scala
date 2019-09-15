package com.georgesdoe.wpebook.wp

import java.net.URL
import java.time.LocalDateTime

case class Post(id: Int, link: URL, date: LocalDateTime, title: HtmlContent, content: HtmlContent)
