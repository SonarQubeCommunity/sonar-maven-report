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

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Add a report link to the Maven site that redirects to the project dashboard in SonarQube/SonarCloud.
 *
 * @since 0.1
 */
@Mojo( name = "report")
public class SonarReportMojo extends AbstractMavenReport {

  @Parameter( property = "sonar.host.url", defaultValue = "http://localhost:9000", alias = "sonar.host.url", required = true )
  private String sonarHostURL;

  @Parameter( property = "branch", alias = "branch", required = false )
  private String branch;

  /**
   * The output directory for the report. Note that this parameter is only evaluated if the goal is run directly from
   * the command line. If the goal is run indirectly as part of a site generation, the output directory configured in
   * the Maven Site Plugin is used instead.
   *
   * @since 0.1
   */
  @Parameter( defaultValue = "${project.reporting.outputDirectory}", readonly = true, required = true )
  private File outputDirectory;

  /**
   * Doxia Site Renderer component.
   */
  @Component
  protected Renderer siteRenderer;

  /**
   * The Maven Project.
   *
   * @since 0.1
   */
  @Parameter( defaultValue = "${project}", readonly = true, required = true )
  protected MavenProject project;

  /**
   * SonarQube/SonarCloud host url; property = "sonar.host.url", alias = "sonar.host.url" (default: http://localhost:9000).
   *
   * @since 0.1
   */
  protected String getSonarHostURL() {
    return sonarHostURL;
  }

  protected void setSonarHostURL(String sonarHostURL) {
    this.sonarHostURL = sonarHostURL;
  }

  /**
   * Branch name; property = "branch", alias = "branch" (default: no branch).
   *
   * @since 0.1
   */
  protected String getBranch() {
    return branch;
  }

  protected void setBranch(String branch) {
    this.branch = branch;
  }

  protected Renderer getSiteRenderer() {
    return siteRenderer;
  }

  protected String getOutputDirectory() {
    return outputDirectory.getAbsolutePath();
  }

  protected void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  protected MavenProject getProject() {
    return project;
  }

  protected void executeReport(Locale locale) throws MavenReportException {
    Sink sink = getSink();
    sink.head();
    sink.title();
    sink.text(getHeader(locale));
    sink.title_();
    sink.head_();

    sink.body();
    sink.sectionTitle1();
    sink.text(getHeader(locale));
    sink.sectionTitle1_();

    String url = getProjectUrl();
    sink.text("Redirecting to ");
    sink.link(url);
    sink.text(url);
    sink.link_();
    sink.rawText("<script type='text/javascript'> window.location='" + url + "'</script>");

    sink.body_();
    sink.flush();
    sink.close();
  }

  public String getOutputName() {
    if (hasTriggerAlternative(Locale.getDefault())) {
      return getBundle(Locale.getDefault()).getString("report.sonar.html.alternative");
    }
    else {
      return getBundle(Locale.getDefault()).getString("report.sonar.html");
    }
  }

  public String getName(Locale locale) {
    if (hasTriggerAlternative(locale)) {
      return getBundle(locale).getString("report.sonar.name.alternative");
    }
    else {
      return getBundle(locale).getString("report.sonar.name");
    }
  }

  public String getDescription(Locale locale) {
    return getBundle(locale).getString("report.sonar.description");
  }

  public String getHeader(Locale locale) {
    if (hasTriggerAlternative(locale)) {
      return getBundle(locale).getString("report.sonar.header.alternative");
    }
    else {
      return getBundle(locale).getString("report.sonar.header");
    }
  }

  public boolean hasTriggerAlternative(Locale locale) {
    return getProjectUrl().contains(getBundle(locale).getString("report.sonar.alternative.trigger"));
  }

  private ResourceBundle getBundle(Locale locale) {
    return ResourceBundle.getBundle("sonar-report", locale, this.getClass().getClassLoader());
  }

  private String getProjectUrl() {
    StringBuilder sb = new StringBuilder(getSonarUrl())
        .append("/dashboard/index/")
        .append(project.getGroupId())
        .append(":")
        .append(project.getArtifactId());
    if (branch != null) {
      sb.append(":").append(branch);
    }
    return sb.toString();
  }

  private String getSonarUrl() {
    if (sonarHostURL.endsWith("/")) {
      return sonarHostURL.substring(0, sonarHostURL.length() - 1);
    }
    return sonarHostURL;
  }
}