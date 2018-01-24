# SonarQube/SonarCloud Maven Report Plugin

[![Build Status](https://api.travis-ci.org/SonarQubeCommunity/sonar-maven-report.svg)](https://travis-ci.org/SonarQubeCommunity/sonar-maven-report)

## Description

Add a report link to the Maven site that redirects to the project dashboard in SonarQube/SonarCloud.

## Usage

Add the plugin to the reporting section in the POM:

```xml
<project>
  …

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.sonar-plugins</groupId>
        <artifactId>maven-report</artifactId>
        <version>${mavenSonarReportPluginVersion}</version>
        <configuration>
          <sonarHostURL>${sonarHost}</sonarHostURL>
        </configuration>
      </plugin>
    </plugins>
  </reporting>
</project>
```

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

Generate the Maven site with: `mvn site`.
