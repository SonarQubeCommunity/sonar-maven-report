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
   * Report output directory. Note that this parameter is only relevant if the goal is run from the command line or
   * from the default build lifecycle. If the goal is run indirectly as part of a site generation, the output
   * directory configured in the Maven Site Plugin is used instead.
   *
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

  protected Renderer getSiteRenderer() {
    return siteRenderer;
  }

  protected String getOutputDirectory() {
    return outputDirectory.getAbsolutePath();
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
}