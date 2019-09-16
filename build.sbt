name := "wp-ebook"

version := "0.1"

scalaVersion := "2.13.0"

scalacOptions += "-Ymacro-annotations"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.11.3",
  "com.lihaoyi" %% "requests" % "0.2.0",
  "io.spray" %%  "spray-json" % "1.3.5",
  "org.scalafx" %% "scalafx" % "12.0.2-R18",
  "org.scalafx" % "scalafxml-core-sfx8_2.13" % "0.5",
  "com.positiondev.epublib" % "epublib-core" % "3.1" excludeAll(
    ExclusionRule(organization = "xmlpull")
  ),
  "com.lihaoyi" %% "scalatags" % "0.7.0"
)

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m =>
  "org.openjfx" % s"javafx-$m" % "12.0.2" classifier osName
)

resourceDirectory := baseDirectory.value / "src/main/resources"