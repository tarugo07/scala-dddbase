import sbt._
import Keys._

object DDDBaseBuild extends Build {
  val specs2 = "org.specs2" %% "specs2" % "1.14" % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  val junit = "junit" % "junit" % "4.8.1" % "test"
  val mockito = "org.mockito" % "mockito-core" % "1.9.5" % "test"

  lazy val commonSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.sisioh",
    version := "0.1.2",
    scalaVersion := "2.10.0",
    libraryDependencies ++= Seq(junit, scalaTest, mockito, scalaTest, specs2),
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation"),
    shellPrompt := {
      "sbt (%s)> " format projectId(_)
    },
    publish
  )

  lazy val root: Project = Project("scala-dddbase",
    file("."),
    settings = commonSettings,
    aggregate = Seq(core, spec))

  val spec: Project = Project("scala-dddbase-spec",
    file("scala-dddbase-spec"),
    settings = commonSettings)

  val core: Project = Project("scala-dddbase-core",
    file("scala-dddbase-core"),
    settings = commonSettings) dependsOn(spec)


  def projectId(state: State) = extracted(state).currentProject.id

  def extracted(state: State) = Project extract state

  def publish = publishTo <<= (version) {
    version: String =>
      if (version.trim.endsWith("SNAPSHOT")) {
        Some(Resolver.file("snaphost", new File("./repos/snapshot")))
      } else {
        Some(Resolver.file("release", new File("./repos/release")))
      }
  }

}
