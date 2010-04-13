/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
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
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * @goal report
 */
public class SonarReportMojo extends AbstractMavenReport {

  /**
   * @parameter expression="${sonar.host.url}" default-value="http://localhost:9000" alias="sonar.host.url"
   */
  private String sonarHostURL;

  /**
   * @parameter default-value="${project.reporting.outputDirectory}"
   */
  private File outputDirectory;

  /**
   * Doxia Site Renderer.
   *
   * @component
   */
  protected Renderer siteRenderer;

  /**
   * The Maven Project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  protected MavenProject project;

  /**
   * @parameter expression="${branch}" alias="branch"
   */
  private String branch;

  protected String getSonarHostURL() {
    return sonarHostURL;
  }

  protected void setSonarHostURL(String sonarHostURL) {
    this.sonarHostURL = sonarHostURL;
  }

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
    sink.text(getBundle(locale).getString("report.sonar.header"));
    sink.title_();
    sink.head_();

    sink.body();
    sink.sectionTitle1();
    sink.text(getBundle(locale).getString("report.sonar.header"));
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
    return "sonar";
  }

  public String getName(Locale locale) {
    return getBundle(locale).getString("report.sonar.name");
  }

  public String getDescription(Locale locale) {
    return getBundle(locale).getString("report.sonar.description");
  }

  private ResourceBundle getBundle(Locale locale) {
    return ResourceBundle.getBundle("sonar-report", locale, this.getClass().getClassLoader());
  }

  private String getProjectUrl() {
    StringBuilder sb = new StringBuilder(getSonarUrl())
        .append("/project/index/")
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