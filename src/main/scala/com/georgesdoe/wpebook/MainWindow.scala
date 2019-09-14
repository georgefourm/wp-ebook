package com.georgesdoe.wpebook

import java.net.URL

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}

object MainWindow extends JFXApp {
  val resource: URL = getClass.getClassLoader.getResource("main_window.fxml")
  val view = FXMLView(resource, NoDependencyResolver)

  stage = new PrimaryStage{
    title = "WordPress - EBook"
    scene =  new Scene(view)
  }
}
