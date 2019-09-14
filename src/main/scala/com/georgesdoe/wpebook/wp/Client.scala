package com.georgesdoe.wpebook.wp

import spray.json._
import com.georgesdoe.wpebook.wp.WordPressProtocol._

class Client(baseUrl: String) {

  def fetchPost(id: Int): Post = {
    val response = requests.get(s"$baseUrl/wp-json/wp/v2/posts/$id")
    val json = response.text.parseJson

    json.convertTo[Post]
  }

  def fetchPosts(categoryId: Int): List[Post] = {
    val response = requests.get(
      s"$baseUrl/wp-json/wp/v2/posts",
      params = Map("categories" -> categoryId.toString)
    )

    val json = response.text.parseJson

    json.convertTo[List[Post]]
  }

  def fetchCategories(per_page: Int): List[Category] = {
    val response = requests.get(
      s"$baseUrl/wp-json/wp/v2/categories",
      params = Map(
        "orderby" -> "name",
        "order" -> "asc",
        "per_page" -> per_page.toString
      )
    )

    val json = response.text.parseJson

    json.convertTo[List[Category]]
  }

}
