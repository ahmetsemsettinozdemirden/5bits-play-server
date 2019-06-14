name := """5bits-backend"""
organization := "com.5bits"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.8"

resolvers += Resolver.sbtPluginRepo("releases")

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.1"
libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += jdbc
libraryDependencies += "com.auth0" % "java-jwt" % "3.3.0"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"