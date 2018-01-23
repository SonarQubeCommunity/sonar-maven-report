/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.mavenreport;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.MavenReportException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SonarReportMojoTest extends AbstractMojoTestCase
{
  private static final String SONARQUBE_REDIRECT_HTML = "<h2><a name=\"SonarQube\"></a>SonarQube</h2>" // Title
      + "Redirecting to <a class=\"externalLink\" href=\"https://sonarqube.com/dashboard/index/grp:art\">https://sonarqube.com/dashboard/index/grp:art</a>" // Link
      + "<script type='text/javascript'> window.location='https://sonarqube.com/dashboard/index/grp:art'</script>"; // Script
  private static final String SONARCLOUD_REDIRECT_HTML = SONARQUBE_REDIRECT_HTML.replaceAll("sonarqube.com", "sonarcloud.io").replaceAll("SonarQube", "SonarCloud").replaceAll(":art", ":art:master");

  @Mock
  private MavenProject project;

  private SonarReportMojo mojo;


  @Before
  public void init() throws IllegalAccessException
  {
    when(project.getGroupId()).thenReturn("grp");
    when(project.getArtifactId()).thenReturn("art");

    mojo = new SonarReportMojo();

    setVariableValueToObject(mojo, "project", project);
  }


  @Test()
  public void hasTriggerAlternative()
  {
    mojo.setSonarHostURL("https://sonarqube.com/");

    assertThat(mojo.hasTriggerAlternative(Locale.getDefault())).isFalse();

    mojo.setSonarHostURL("https://sonarcloud.io/");

    assertThat(mojo.hasTriggerAlternative(Locale.getDefault())).isTrue();
  }


  @Test()
  public void header()
  {
    mojo.setSonarHostURL("https://sonarqube.com/");

    assertThat(mojo.getHeader(Locale.getDefault())).isEqualTo("SonarQube");

    mojo.setSonarHostURL("https://sonarcloud.io/");

    assertThat(mojo.getHeader(Locale.getDefault())).isEqualTo("SonarCloud");
  }


  @Test()
  public void description()
  {
    mojo.setSonarHostURL("https://sonarqube.com/");

    assertThat(mojo.getDescription(Locale.getDefault())).isEqualTo("Dashboard for code quality management");

    mojo.setSonarHostURL("https://sonarcloud.io/");

    assertThat(mojo.getDescription(Locale.getDefault())).isEqualTo("Dashboard for code quality management");
  }


  @Test()
  public void name()
  {
    mojo.setSonarHostURL("https://sonarqube.com/");

    assertThat(mojo.getName(Locale.getDefault())).isEqualTo("SonarQube");

    mojo.setSonarHostURL("https://sonarcloud.io/");

    assertThat(mojo.getName(Locale.getDefault())).isEqualTo("SonarCloud");
  }


  @Test()
  public void outputName()
  {
    mojo.setSonarHostURL("https://sonarqube.com/");

    assertThat(mojo.getOutputName()).isEqualTo("sonarqube");

    mojo.setSonarHostURL("https://sonarcloud.io/");

    assertThat(mojo.getOutputName()).isEqualTo("sonarcloud");
  }


  @Test()
  public void testExecuteReport() throws MavenReportException
  {
    mojo.setSonarHostURL("https://sonarqube.com/");
    mojo.setBranch(null);

    SiteRendererSink sink = generateReport(mojo);

    assertThat(mojo.getBranch()).isNull();
    assertThat(mojo.getSonarHostURL()).isEqualTo("https://sonarqube.com/");
    assertThat(sink.getTitle()).isEqualTo("SonarQube");
    assertThat(sink.getBody()).isEqualTo(SONARQUBE_REDIRECT_HTML);

    mojo.setSonarHostURL("https://sonarcloud.io");
    mojo.setBranch("master");

    sink = generateReport(mojo);

    mojo.generate(sink, null, Locale.getDefault());

    assertThat(mojo.getBranch()).isEqualTo("master");
    assertThat(mojo.getSonarHostURL()).isEqualTo("https://sonarcloud.io");
    assertThat(sink.getTitle()).isEqualTo("SonarCloud");
    assertThat(sink.getBody()).isEqualTo(SONARCLOUD_REDIRECT_HTML);
  }


  private SiteRendererSink generateReport(SonarReportMojo mojo) throws MavenReportException
  {
    SiteRendererSink sink = new SiteRendererSink(null);

    mojo.generate(sink, null, Locale.getDefault());

    return sink;
  }
}
