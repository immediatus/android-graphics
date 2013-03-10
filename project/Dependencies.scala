import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    ScalaToolsSnapshots,
    "Typesafe repo"    at "http://repo.typesafe.com/typesafe/releases/",
    "scala-tools repo" at "https://oss.sonatype.org/content/groups/scala-tools/"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  object V {
    val scala    = "2.10"
    val akka     = "2.0.2"
    val scalaz   = "7.0-SNAPSHOT"
    val specs2   = "1.9"
  }

  val akkaActor   = "com.typesafe.akka"   %   "akka-actor"      % V.akka
  val akkaTestKit = "com.typesafe.akka"   %   "akka-testkit"    % V.akka
  val akkaSlf4j   = "com.typesafe.akka"   %   "akka-slf4j"      % V.akka
  val scalaz      = "org.scalaz"          %%  "scalaz-core"     % V.scalaz
  val slf4j       = "org.slf4j"           %   "slf4j-api"       % "1.6.4"
  val specs2      = "org.specs2"          %%  "specs2"          % V.specs2
}
