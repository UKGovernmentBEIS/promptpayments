// coursier to speedup downloading dependencies
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M15")
// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.1")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.2")
// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
// addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.1.0")

// Play Ebean support, to enable, uncomment this line, and enable in your build.sbt using
// enablePlugins(PlayEbean).
// addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "1.0.0")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.0.3")
resolvers += "Flyway" at "https://flywaydb.org/repo"
