name := """Demo5"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)
libraryDependencies += "redis.clients" % "jedis" % "2.9.0"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.3.0"

