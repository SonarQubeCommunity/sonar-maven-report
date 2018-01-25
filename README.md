# SonarQube/SonarCloud Maven Report Plugin

[![Build Status](https://api.travis-ci.org/SonarQubeCommunity/sonar-maven-report.svg)](https://travis-ci.org/SonarQubeCommunity/sonar-maven-report)

## Description

Add a report link to the Maven site that redirects to the project dashboard in SonarQube/SonarCloud.

## Repository

Version 0.1 was hosted at [codehaus.org](https://twitter.com/codehaus) that was [terminated around May 17th 2015](https://web.archive.org/web/20161017000236/http:/www.codehaus.org).

![The Wayback Machine](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/codehaus.png)

In [![issue 9](https://img.shields.io/github/issues/detail/s/SonarQubeCommunity/sonar-maven-report/9.svg "issue 9")](https://github.com/SonarQubeCommunity/sonar-maven-report/issues/9) was decided to switch to [Sonatype Open Source Software Repository Hosting](http://central.sonatype.org/pages/ossrh-guide.html) (OSSRH).

![Issue 9](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/ossrh.png)

## Releases

The code itself is released in the `master` branch as `maven-report-x.y.z`. The changes are then merged to `ossrh-releases` branch and released as `sonarqube-maven-report-x.y.z` under groupId `nl.demon.shadowland.maven.plugins`, which was already configured for OSSRH access.

<!--- ### The gory details -->

## Usage version 0.2.x

Add the plugin to the reporting section in the POM:

```xml
<project>
  …

  <reporting>
    <plugins>
      <plugin>
        <groupId>nl.demon.shadowland.maven.plugins</groupId>
        <artifactId>sonarqube-maven-report</artifactId>
        <version>0.2.1</version>
      </plugin>
    </plugins>
  </reporting>
</project>
```

## Usage version 0.1

Add the plugin to the reporting section in the POM:

```xml
<project>
  …

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.sonar-plugins</groupId>
        <artifactId>maven-report</artifactId>
        <version>0.1</version>
      </plugin>
    </plugins>
  </reporting>
</project>
```

## Usage properties

Optionally, you can add the following properties to override default values:

```xml
<project>
  …

  <properties>
    <!-- default value is http://localhost:9000 -->
    <sonar.host.url>https://sonarqube.dev.lan/</sonar.host.url>
    <!-- no branch by default -->
    <branch>master</branch>
  </properties>

  …

  <reporting>
    …
  </reporting>
</project>
```

## Usage Maven

Generate the Maven site with: `mvn site`.
<!--- Generate only the report with `` or `mvn nl.demon.shadowland.maven.plugins:sonarqube-maven-report:report`. -->

# Comment test

[comment]: <> ( * [sonarqube-maven-report](https://mvnrepository.com/artifact/nl.demon.shadowland.maven.plugins/sonarqube-maven-report).)
[//]: <> ( * [sonarqube-maven-report](https://mvnrepository.com/artifact/nl.demon.shadowland.maven.plugins/sonarqube-maven-report).)
[//]: # ( * [sonarqube-maven-report](https://mvnrepository.com/artifact/nl.demon.shadowland.maven.plugins/sonarqube-maven-report).)
<!--- * [sonarqube-maven-report](https://mvnrepository.com/artifact/nl.demon.shadowland.maven.plugins/sonarqube-maven-report). -->
