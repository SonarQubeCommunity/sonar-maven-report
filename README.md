Maven Report Plugin
===================
[![Build Status](https://api.travis-ci.org/SonarQubeCommunity/sonar-maven-report.svg)](https://travis-ci.org/SonarQubeCommunity/sonar-maven-report)

## Description
Add a report link to the Maven site that redirects to the project dashboard in SonarQube.

## Usage
* Add the plugin to the reporting section in the POM:
```
<reporting>
  <plugins>
    <plugin>
      <groupId>org.codehaus.sonar-plugins</groupId>
      <artifactId>maven-report</artifactId>
    </plugin>
  </plugins>
</reporting>
```

* Optionally, you can add the following properties to override default values:
```
<properties>
  <!-- default value is http://localhost:9000 -->
  <sonar.host.url>http://my_server:9000</sonar.host.url>
 
  <!-- no branch by default -->
  <branch>BRANCH-1.x</branch>
</properties>
```

* Generate the Maven site:

> mvn site
