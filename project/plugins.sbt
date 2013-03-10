addSbtPlugin("org.scala-sbt" % "sbt-android-plugin" % "0.6.3-SNAPSHOT")

resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers ++= Seq("Web plugin repo" at "http://siasia.github.com/maven2")

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v + "-0.2.11.1"))
