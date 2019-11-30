package com.georgesdoe.wpebook.wp

import org.apache.commons.lang.StringEscapeUtils

case class HtmlContent(rendered: String) {
  def unescaped: String = StringEscapeUtils.unescapeHtml(rendered)
}
