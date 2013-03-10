import sbt._

import Keys._
import AndroidKeys._

object Build extends sbt.Build {
  import Dependencies._

  val baseSettings =
    Defaults.defaultSettings ++ Seq (
      name                    := "android-experiments",
      organization  := "my.experiments",
      version                 := "1.0",
      versionCode             := 0,
      scalaVersion            := "2.10.0",
      platformName in Android := "android-17",
      resolvers               ++= Dependencies.resolutionRepos,
      libraryDependencies     ++= compile(scalaz),
      scalacOptions           := Seq("-deprecation", "-unchecked", "-encoding", "utf8")
    )

  lazy val fullAndroidSettings =
    baseSettings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    AndroidManifestGenerator.settings ++
    AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "change-me"
    )

  lazy val main = Project(
    "android-experiments",
    file("."),
    settings = fullAndroidSettings
  )
}
