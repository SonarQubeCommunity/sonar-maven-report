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

The code itself is released in the `master` branch as `maven-report-x.y.z`. The changes are then merged to the `ossrh-releases` branch and released as `sonarqube-maven-report-x.y.z` under the groupId `nl.demon.shadowland.maven.plugins`, which was already configured for OSSRH access.

#### The gory details

The [OSSRH release and deployment stuff](https://freedumbytes.gitlab.io/dpl/html/complete-manual.html#prj.maven.deploy.artifact.open.source) is activated with the `openSource` profile:

```xml
    <profile>
      <id>openSource</id>

      <distributionManagement>
        <repository>
          <id>ossrh</id>
          <name>Open Source Releases</name>
          <url>${ossrhHost}/content/repositories/releases</url>
        </repository>
        <snapshotRepository>
          <id>ossrh</id>
          <name>Open Source Snapshots</name>
          <url>${ossrhHost}/content/repositories/snapshots</url>
        </snapshotRepository>
      </distributionManagement>

      <properties>
        <tagNameFormat>@{project.artifactId}-@{project.version}</tagNameFormat>
      </properties>

      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-gpg-plugin</artifactId>
            <executions>
              <execution>
                <id>sign-artifacts</id>
                <phase>verify</phase>
                <goals>
                  <goal>sign</goal>
                </goals>
                <configuration>
                  <keyname>${gpg.keyname}</keyname>
                  <passphraseServerId>${gpg.keyname}</passphraseServerId>
                  <gpgArguments>
                    <arg>--pinentry-mode</arg>
                    <arg>loopback</arg>
                  </gpgArguments>
                </configuration>
              </execution>
            </executions>
          </plugin>

          <plugin>
            <groupId>org.sonatype.plugins</groupId>
            <artifactId>nexus-staging-maven-plugin</artifactId>
          </plugin>
        </plugins>
      </build>
    </profile>
```

For OSSRH deployment the artifacts must be signed with a [PGP Signature](https://freedumbytes.gitlab.io/dpl/html/complete-manual.html#prj.maven.deploy.pgp), which is stored in the `settings.xml` and referenced by `gpg.keyname`:

```xml
  <servers>
    <server>
      <id>DD605CC8A9582C0D</id>
      <passphrase>{…}</passphrase>
    </server>
  </servers>

  …

  <profiles>
    <profile>
      <id>gnupg</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>…/gpg</gpg.executable>
        <gpg.keyname>DD605CC8A9582C0D</gpg.keyname>
        <gpg.skip>false</gpg.skip>
      </properties>
    </profile>
  </profiles>
```

The [OSSRH account](https://freedumbytes.gitlab.io/dpl/html/complete-manual.html#repo.nexus.maven.distribution.management) is also stored in the `settings.xml` and is referenced by the `id` from the `distributionManagement`:

```xml
  <servers>
    <server>
      <id>ossrh</id>
      <username>username</username>
      <password>{…}</password>
    </server>
  </servers>
```

In case of Java code the [Sources](https://freedumbytes.gitlab.io/dpl/html/complete-manual.html#prj.maven.setup.java.source) and the [Javadoc](https://freedumbytes.gitlab.io/dpl/html/complete-manual.html#prj.maven.setup.java.javadoc) must also be included:

```xml
  <profiles>
    <profile>
      <id>documents</id>

      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
          </plugin>

          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-javadoc-plugin</artifactId>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
```

**Note**: When sources and javadoc are generated in the same phase `verify` as the PGP Signing make sure the `documents` profile is placed before the `openSource` profile, otherwise they won't get signed and thus the deployment will fail.

## Usage version 0.2.x

[![SonarQube/SonarCloud Maven Report Plugin pipeline](https://gitlab.com/freedumbytes/sonar-maven-report/badges/ossrh-releases/build.svg "SonarQube/SonarCloud Maven Report Plugin pipeline")](https://gitlab.com/freedumbytes/sonar-maven-report)
[![SonarQube/SonarCloud Maven Report Plugin build master](https://img.shields.io/travis/SonarQubeCommunity/sonar-maven-report/master.svg?label=build:master "SonarQube/SonarCloud Maven Report Plugin build master")](https://github.com/SonarQubeCommunity/sonar-maven-report/tree/master)
[![SonarQube/SonarCloud Maven Report Plugin build ossrh](https://img.shields.io/travis/SonarQubeCommunity/sonar-maven-report/ossrh-releases.svg?label=build:ossrh-releases "SonarQube/SonarCloud Maven Report Plugin build ossrh")](https://github.com/SonarQubeCommunity/sonar-maven-report/tree/ossrh-releases)

[![Maven Site](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/maven.png "Maven Site")](https://freedumbytes.gitlab.io/sonar-maven-report/)
[![Maven Site](https://javadoc.io/badge/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?color=blue&label=SonarQube/SonarCloud%20Maven%20Report%20Plugin "Maven Site")](https://freedumbytes.gitlab.io/sonar-maven-report/)

[![Central](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/central.png "Central")](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22nl.demon.shadowland.maven.plugins%22%20AND%20a%3A%22sonarqube-maven-report%22)
[![Maven Central](https://img.shields.io/maven-central/v/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?label=Maven%20Central "Maven Central")](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22nl.demon.shadowland.maven.plugins%22%20AND%20a%3A%22sonarqube-maven-report%22)
[![SonarQube/SonarCloud Maven Report Plugin Javadoc](https://javadoc.io/badge/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?color=blue&label=Javadoc "SonarQube/SonarCloud Maven Report Plugin Javadoc")](http://javadoc.io/doc/nl.demon.shadowland.maven.plugins/sonarqube-maven-report)
[![SonarQube/SonarCloud Maven Report Plugin License](https://img.shields.io/badge/GNU_Lesser_General_Public_License-3.0-red.svg "SonarQube/SonarCloud Maven Report Plugin License")](https://www.gnu.org/licenses/lgpl-3.0.html)

[![Nexus](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/nexus.png "Nexus")](https://oss.sonatype.org/index.html#nexus-search;gav~nl.demon.shadowland.maven.plugins~sonarqube-maven-report~~~)
[![Nexus](https://img.shields.io/nexus/r/https/oss.sonatype.org/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?label=Nexus "Nexus")](https://oss.sonatype.org/index.html#nexus-search;gav~nl.demon.shadowland.maven.plugins~sonarqube-maven-report~~~)

[![MvnRepository](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/mvnrepository.png "MvnRepository")](https://mvnrepository.com/artifact/nl.demon.shadowland.maven.plugins/sonarqube-maven-report)
[![MvnRepository](https://img.shields.io/nexus/r/https/oss.sonatype.org/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?label=MvnRepository "MvnRepository")](https://mvnrepository.com/artifact/nl.demon.shadowland.maven.plugins/sonarqube-maven-report)

[![SonarCloud](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/sonarcloud.png "SonarCloud")](https://sonarcloud.io/dashboard?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report)
[![SonarQube/SonarCloud Maven Report Plugin Quality Gate](https://sonarcloud.io/api/badges/gate?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report "SonarQube/SonarCloud Maven Report Plugin Quality Gate")](https://sonarcloud.io/dashboard?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report)
[![SonarQube/SonarCloud Maven Report Plugin vulnerabilities](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=vulnerabilities "SonarQube/SonarCloud Maven Report Plugin vulnerabilities")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=vulnerabilities)
[![SonarQube/SonarCloud Maven Report Plugin bugs](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=bugs "SonarQube/SonarCloud Maven Report Plugin bugs")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=bugs)

[![SonarQube/SonarCloud Maven Report Plugin coverage](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=coverage "SonarQube/SonarCloud Maven Report Plugin coverage")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=coverage)
[![SonarQube/SonarCloud Maven Report Plugin lines of code](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=ncloc "SonarQube/SonarCloud Maven Report Plugin lines of code")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=ncloc)
[![SonarQube/SonarCloud Maven Report Plugin duplication](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=duplicated_lines_density "SonarQube/SonarCloud Maven Report Plugin duplication")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=duplicated_lines)
[![SonarQube/SonarCloud Maven Report Plugin technical debt](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=sqale_debt_ratio "SonarQube/SonarCloud Maven Report Plugin technical debt")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=sqale_index)

[![Dependency Check Report](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/dependency-check.png "Dependency Check Report")](https://freedumbytes.gitlab.io/sonar-maven-report/dependency-check-report.html)
[![Dependency Check Report](https://img.shields.io/nexus/r/https/oss.sonatype.org/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?label=Dependency%20Check "Dependency Check Report")](https://freedumbytes.gitlab.io/sonar-maven-report/dependency-check-report.html)


Add the plugin to the reporting section in the POM:

```xml
<project>
  …

  <reporting>
    <plugins>
      <plugin>
        <groupId>nl.demon.shadowland.maven.plugins</groupId>
        <artifactId>sonarqube-maven-report</artifactId>
        <version>0.2.2</version>
      </plugin>
    </plugins>
  </reporting>
</project>
```

## Usage version 0.1


[![Central](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/central.png "Central")](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.codehaus.sonar-plugins%22%20AND%20a%3A%22maven-report%22)
[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.sonar-plugins/maven-report.svg?label=Maven%20Central "Maven Central")](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.codehaus.sonar-plugins%22%20AND%20a%3A%22maven-report%22)
[![SonarQube/SonarCloud Maven Report Plugin License](https://img.shields.io/badge/GNU_Lesser_General_Public_License-3.0-red.svg "SonarQube/SonarCloud Maven Report Plugin License")](https://www.gnu.org/licenses/lgpl-3.0.html)

[![Nexus](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/nexus.png "Nexus")](https://oss.sonatype.org/index.html#nexus-search;gav~org.codehaus.sonar-plugins~maven-report~~~)
[![Nexus](https://img.shields.io/nexus/r/https/oss.sonatype.org/org.codehaus.sonar-plugins/maven-report.svg?label=Nexus "Nexus")](https://oss.sonatype.org/index.html#nexus-search;gav~org.codehaus.sonar-plugins~maven-report~~~)

[![MvnRepository](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/mvnrepository.png "MvnRepository")](https://mvnrepository.com/artifact/org.codehaus.sonar-plugins/maven-report)
[![MvnRepository](https://img.shields.io/nexus/r/https/oss.sonatype.org/org.codehaus.sonar-plugins/maven-report.svg?label=MvnRepository "MvnRepository")](https://mvnrepository.com/artifact/org.codehaus.sonar-plugins/maven-report)

[![SonarCloud](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/images/icons/sonarcloud.png "SonarCloud")](https://sonarcloud.io/dashboard?id=org.codehaus.sonar-plugins:maven-report)
[![SonarQube/SonarCloud Maven Report Plugin Quality Gate](https://sonarcloud.io/api/badges/gate?key=org.codehaus.sonar-plugins:maven-report "SonarQube/SonarCloud Maven Report Plugin Quality Gate")](https://sonarcloud.io/dashboard?id=org.codehaus.sonar-plugins:maven-report)
[![SonarQube/SonarCloud Maven Report Plugin vulnerabilities](https://sonarcloud.io/api/badges/measure?key=org.codehaus.sonar-plugins:maven-report&metric=vulnerabilities "SonarQube/SonarCloud Maven Report Plugin vulnerabilities")](https://sonarcloud.io/component_measures?id=org.codehaus.sonar-plugins:maven-report&metric=vulnerabilities)
[![SonarQube/SonarCloud Maven Report Plugin bugs](https://sonarcloud.io/api/badges/measure?key=org.codehaus.sonar-plugins:maven-report&metric=bugs "SonarQube/SonarCloud Maven Report Plugin bugs")](https://sonarcloud.io/component_measures?id=org.codehaus.sonar-plugins:maven-report&metric=bugs)

[![SonarQube/SonarCloud Maven Report Plugin coverage](https://sonarcloud.io/api/badges/measure?key=org.codehaus.sonar-plugins:maven-report&metric=coverage "SonarQube/SonarCloud Maven Report Plugin coverage")](https://sonarcloud.io/component_measures?id=org.codehaus.sonar-plugins:maven-report&metric=coverage)
[![SonarQube/SonarCloud Maven Report Plugin lines of code](https://sonarcloud.io/api/badges/measure?key=org.codehaus.sonar-plugins:maven-report&metric=ncloc "SonarQube/SonarCloud Maven Report Plugin lines of code")](https://sonarcloud.io/component_measures?id=org.codehaus.sonar-plugins:maven-report&metric=ncloc)
[![SonarQube/SonarCloud Maven Report Plugin duplication](https://sonarcloud.io/api/badges/measure?key=org.codehaus.sonar-plugins:maven-report&metric=duplicated_lines_density "SonarQube/SonarCloud Maven Report Plugin duplication")](https://sonarcloud.io/component_measures?id=org.codehaus.sonar-plugins:maven-report&metric=duplicated_lines)
[![SonarQube/SonarCloud Maven Report Plugin technical debt](https://sonarcloud.io/api/badges/measure?key=org.codehaus.sonar-plugins:maven-report&metric=sqale_debt_ratio "SonarQube/SonarCloud Maven Report Plugin technical debt")](https://sonarcloud.io/component_measures?id=org.codehaus.sonar-plugins:maven-report&metric=sqale_index)

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
    <sonar.host.url>https://sonarcloud.io/</sonar.host.url>
    <!-- no branch by default -->
    <branch>osssrh-releases</branch>
  </properties>

  …

  <reporting>
    …
  </reporting>
</project>
```

**Note**: The Maven report uses as default `SonarQube` for title, header and html filename. But in case of host `sonarcloude.io` the report switches to `SonarCloud`.

To see [the Maven report in action](https://freedumbytes.gitlab.io/sonar-maven-report/project-reports.html) this project is [mirrored on GitLab](https://gitlab.com/freedumbytes/sonar-maven-report), where the branch [ossrh-releases](https://gitlab.com/freedumbytes/sonar-maven-report/tree/ossrh-releases) is used to generate the site with a [.gitlab-ci.yml](https://freedumbytes.gitlab.io/dpl/html/complete-manual.html#continuous.integration.pipeline.gitlab).

## Usage Maven

Generate the Maven site with: `mvn site`.

Generate only the report with `mvn nl.demon.shadowland.maven.plugins:sonarqube-maven-report:0.2.2:report [-Dsonar.host.url=https://sonarcloud.io/]`.

**Note**: To make sure both commands result in the same content, take a look under the hood of Maven to understand the alternative `pluginManagement` usage shown below.

#### Maven under the hood

First consider the way the `SonarReportMojo` defined the url parameter:

```java
  @Parameter( property = "sonar.host.url", defaultValue = "http://localhost:9000", alias = "sonar.host.url", required = true )
  private String sonarHostURL;
```

Thus there are 3 ways to set this parameter in the POM, which are of course all overruled by the command line option `-Dsonar.host.url`:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <properties>
    <sonar.host.url>https://sonar.property.com/</sonar.host.url>
  </properties>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>nl.demon.shadowland.maven.plugins</groupId>
          <artifactId>sonarqube-maven-report</artifactId>
          <version>0.2.2</version>
          <configuration>
            <sonarHostURL>https://sonar.plugin.management.com/</sonarHostURL>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>

  <reporting>
      <plugin>
        <groupId>nl.demon.shadowland.maven.plugins</groupId>
        <artifactId>sonarqube-maven-report</artifactId>
        <version>0.2.2</version>
        <configuration>
          <sonarHostURL>https://sonar.reporting.com/</sonarHostURL>
        </configuration>
      </plugin>
    </plugins>
  </reporting>
</project>
```

Testing these three possibilities with the above mentioned two Maven Usage commands will not result in the same content for the last reporting configuration.

## Usage alternative pluginManagement

Alternatively, you can add the following plugin management to override default values:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>nl.demon.shadowland.maven.plugins</groupId>
          <artifactId>sonarqube-maven-report</artifactId>
          <version>0.2.2</version>
          <configuration>
            <!-- default value is http://localhost:9000 -->
            <sonarHostURL>https://sonarcloud.io/</sonarHostURL>
            <!-- no branch by default -->
            <branch>osssrh-releases</branch>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>

  <reporting>
      <plugin>
        <groupId>nl.demon.shadowland.maven.plugins</groupId>
        <artifactId>sonarqube-maven-report</artifactId>
        <version>0.2.2</version>
      </plugin>
    </plugins>
  </reporting>
</project>
```
