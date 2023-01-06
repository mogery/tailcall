ThisBuild / scalaVersion := "2.13.10"
val zioJson   = "0.4.2"
val zioSchema = "0.4.1"
ThisBuild / scalacOptions       := Seq("-language:postfixOps")
ThisBuild / libraryDependencies := Seq(
  "dev.zio"               %% "zio-schema"            % zioSchema,
  "dev.zio"               %% "zio-schema-derivation" % zioSchema,
  "com.lihaoyi"           %% "pprint"                % "0.8.1",
  "dev.zio"               %% "zio"                   % "2.0.5",
  "com.github.ghostdogpr" %% "caliban"               % "2.0.1",
  "dev.zio"               %% "zio-json"              % zioJson,
  "dev.zio"               %% "zio-json-macros"       % zioJson,
  "dev.zio"               %% "zio-json-yaml"         % zioJson,
  "dev.zio"               %% "zio-parser"            % "0.1.7",

  // Testing
  "dev.zio" %% "zio-test"     % "2.0.5" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.0.5" % Test,
)

ThisBuild / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

Global / semanticdbEnabled      := true
Global / onChangedBuildSource   := ReloadOnSourceChanges
