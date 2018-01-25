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

[![Maven Site](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/maven.png "Maven Site") SonarQube/SonarCloud Maven Report Plugin](https://freedumbytes.gitlab.io/sonar-maven-report/).
     * [![Maven Central](https://img.shields.io/maven-central/v/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?label=Maven%20Central "Maven Central")](https://search.maven.org/#search%7cga%7c1%7cnl.demon.shadowland.maven.plugins)
       [![SonarQube/SonarCloud Maven Report Plugin Javadoc](https://javadoc.io/badge/nl.demon.shadowland.maven.plugins/sonarqube-maven-report.svg?color=blue&label=Javadoc "SonarQube/SonarCloud Maven Report Plugin Javadoc")](http://javadoc.io/doc/nl.demon.shadowland.maven.plugins/sonarqube-maven-report)
       [![SonarQube/SonarCloud Maven Report Plugin License](https://img.shields.io/badge/GNU_Lesser_General_Public_License-3.0-red.svg "SonarQube/SonarCloud Maven Report Plugin License")](https://www.gnu.org/licenses/lgpl-3.0.html)
       [![SonarQube/SonarCloud Maven Report Plugin pipeline](https://gitlab.com/freedumbytes/sonar-maven-report/badges/ossrh-releases/build.svg "SonarQube/SonarCloud Maven Report Plugin pipeline")](https://gitlab.com/freedumbytes/sonar-maven-report)
       [![SonarQube/SonarCloud Maven Report Plugin build master](https://img.shields.io/travis/SonarQubeCommunity/sonar-maven-report/master.svg?label=build:master "SonarQube/SonarCloud Maven Report Plugin build master")](https://github.com/SonarQubeCommunity/sonar-maven-report/tree/master)
       [![SonarQube/SonarCloud Maven Report Plugin build ossrh](https://img.shields.io/travis/SonarQubeCommunity/sonar-maven-report/ossrh-releases.svg?label=build:ossrh-releases "SonarQube/SonarCloud Maven Report Plugin build ossrh")](https://github.com/SonarQubeCommunity/sonar-maven-report/tree/ossrh-releases)
       [![SonarQube/SonarCloud Maven Report Plugin Quality Gate](https://sonarcloud.io/api/badges/gate?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report "SonarQube/SonarCloud Maven Report Plugin Quality Gate")](https://sonarcloud.io/dashboard?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report)
     * [![SonarQube/SonarCloud Maven Report Plugin vulnerabilities](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=vulnerabilities "SonarQube/SonarCloud Maven Report Plugin vulnerabilities")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=vulnerabilities)
       [![SonarQube/SonarCloud Maven Report Plugin bugs](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=bugs "SonarQube/SonarCloud Maven Report Plugin bugs")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=bugs)
       [![SonarQube/SonarCloud Maven Report Plugin coverage](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=coverage "SonarQube/SonarCloud Maven Report Plugin coverage")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=coverage)
       [![SonarQube/SonarCloud Maven Report Plugin lines of code](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=ncloc "SonarQube/SonarCloud Maven Report Plugin lines of code")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=ncloc)
       [![SonarQube/SonarCloud Maven Report Plugin duplication](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=duplicated_lines_density "SonarQube/SonarCloud Maven Report Plugin duplication")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=duplicated_lines)
       [![SonarQube/SonarCloud Maven Report Plugin technical debt](https://sonarcloud.io/api/badges/measure?key=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=sqale_debt_ratio "SonarQube/SonarCloud Maven Report Plugin technical debt")](https://sonarcloud.io/component_measures?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report&metric=sqale_index)

[![Nexus](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/nexus.png "Nexus") Nexus Repository Manager](https://oss.sonatype.org/index.html#nexus-search;gav~nl.demon.shadowland.maven.plugins~sonarqube-maven-report~~~)

[![Central](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/central.png "Central") The Central Repository](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22nl.demon.shadowland.maven.plugins%22%20AND%20a%3A%22sonarqube-maven-report%22)

[![MvnRepository](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/mvnrepository.png "MvnRepository") MvnRepository](https://mvnrepository.com/artifact/nl.demon.shadowland.maven.plugins/sonarqube-maven-report)

[![SonarCloud](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/sonarcloud.png "SonarCloud") SonarCloud](https://sonarcloud.io/dashboard?id=nl.demon.shadowland.maven.plugins:sonarqube-maven-report)

[![Dependency Check Report](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/dependency-check.png "Dependency Check Report") Dependency Check Report](https://freedumbytes.gitlab.io/sonar-maven-report/dependency-check-report.html)


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

[![Nexus](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/nexus.png "Nexus") Nexus Repository Manager](https://oss.sonatype.org/index.html#nexus-search;gav~org.codehaus.sonar-plugins~maven-report~~~)
     * [![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.sonar-plugins/maven-report.svg?label=Maven%20Central "Maven Central")](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.codehaus.sonar-plugins%22%20AND%20a%3A%22maven-report%22)
       [![SonarQube/SonarCloud Maven Report Plugin License](https://img.shields.io/badge/GNU_Lesser_General_Public_License-3.0-red.svg "SonarQube/SonarCloud Maven Report Plugin License")](https://www.gnu.org/licenses/lgpl-3.0.html)

[![Central](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/central.png "Central") The Central Repository](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.codehaus.sonar-plugins%22%20AND%20a%3A%22maven-report%22)

[![MvnRepository](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/mvnrepository.png "MvnRepository") MvnRepository](https://mvnrepository.com/artifact/org.codehaus.sonar-plugins/maven-report)

[![SonarCloud](https://raw.githubusercontent.com/SonarQubeCommunity/sonar-maven-report/master/src/site/resources/icons/sonarcloud.png "SonarCloud") SonarCloud](https://sonarcloud.io/dashboard?id=org.codehaus.sonar-plugins:maven-report)

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

ToDo: Generate only the report with `` or `mvn nl.demon.shadowland.maven.plugins:sonarqube-maven-report:report`.
