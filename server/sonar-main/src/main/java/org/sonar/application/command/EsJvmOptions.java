/*
 * SonarQube
 * Copyright (C) 2009-2021 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.application.command;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.sonar.process.Props;

public class EsJvmOptions extends JvmOptions<EsJvmOptions> {
  private static final String ELASTICSEARCH_JVM_OPTIONS_HEADER = "# This file has been automatically generated by SonarQube during startup.\n" +
    "# Please use sonar.search.javaOpts and/or sonar.search.javaAdditionalOpts in sonar.properties to specify jvm options for Elasticsearch\n" +
    "\n" +
    "# DO NOT EDIT THIS FILE\n" +
    "\n";

  public EsJvmOptions(Props props, File tmpDir) {
    super(mandatoryOptions(tmpDir, props));
  }

  // this basically writes down the content of jvm.options file distributed in vanilla Elasticsearch package
  // with some changes to fit running bundled in SQ
  private static Map<String, String> mandatoryOptions(File tmpDir, Props props) {
    Map<String, String> res = new LinkedHashMap<>(30);
    fromJvmDotOptionsFile(tmpDir, res);
    fromSystemJvmOptionsClass(res);

    if (!props.value("sonar.jdbc.url", "").contains("jdbc:h2") && !props.valueAsBoolean("sonar.es.bootstrap.checks.disable")) {
      res.put("-Des.enforce.bootstrap.checks=", "true");
    }

    return res;
  }

  private static void fromJvmDotOptionsFile(File tmpDir, Map<String, String> res) {
    // GC configuration
    res.put("-XX:+UseG1GC", "");

    // (by default ES 6.6.1 uses variable ${ES_TMPDIR} which is replaced by start scripts. Since we start JAR file
    // directly on windows, we specify absolute file as URL (to support space in path) instead
    res.put("-Djava.io.tmpdir=", tmpDir.getAbsolutePath());

    // heap dumps (enable by default in ES 6.6.1, we don't enable them, no one will analyze them anyway)
    // generate a heap dump when an allocation from the Java heap fails
    // heap dumps are created in the working directory of the JVM
    // res.put("-XX:+HeapDumpOnOutOfMemoryError", "");
    // specify an alternative path for heap dumps; ensure the directory exists and
    // has sufficient space
    // res.put("-XX:HeapDumpPath", "data");
    // specify an alternative path for JVM fatal error logs (ES 6.6.1 default is "logs/hs_err_pid%p.log")
    res.put("-XX:ErrorFile=", "../logs/es_hs_err_pid%p.log");

    // JDK 8 GC logging (by default ES 6.6.1 enables them, we don't want to do that in SQ, no one will analyze them anyway)
    // res.put("8:-XX:+PrintGCDetails", "");
    // res.put("8:-XX:+PrintGCDateStamps", "");
    // res.put("8:-XX:+PrintTenuringDistribution", "");
    // res.put("8:-XX:+PrintGCApplicationStoppedTime", "");
    // res.put("8:-Xloggc:logs/gc.log", "");
    // res.put("8:-XX:+UseGCLogFileRotation", "");
    // res.put("8:-XX:NumberOfGCLogFiles", "32");
    // res.put("8:-XX:GCLogFileSize", "64m");
    // JDK 9+ GC logging
    // res.put("9-:-Xlog:gc*,gc+age=trace,safepoint:file=logs/gc.log:utctime,pid,tags:filecount=32,filesize=64m", "");
  }

  /**
   * JVM options from class "org.elasticsearch.tools.launchers.SystemJvmOptions"
   */
  private static void fromSystemJvmOptionsClass(Map<String, String> res) {
    /*
     * Cache ttl in seconds for positive DNS lookups noting that this overrides the JDK security property networkaddress.cache.ttl;
     * can be set to -1 to cache forever.
     */
    res.put("-Des.networkaddress.cache.ttl=", "60");
    /*
     * Cache ttl in seconds for negative DNS lookups noting that this overrides the JDK security property
     * networkaddress.cache.negative ttl; set to -1 to cache forever.
     */
    res.put("-Des.networkaddress.cache.negative.ttl=", "10");
    // pre-touch JVM emory pages during initialization
    res.put("-XX:+AlwaysPreTouch", "");
    // explicitly set the stack size
    res.put("-Xss1m", "");
    // set to headless, just in case,
    res.put("-Djava.awt.headless=", "true");
    // ensure UTF-8 encoding by default (e.g., filenames)
    res.put("-Dfile.encoding=", "UTF-8");
    // use our provided JNA always versus the system one
    res.put("-Djna.nosys=", "true");
    /*
     * Turn off a JDK optimization that throws away stack traces for common exceptions because stack traces are important for
     * debugging.
     */
    res.put("-XX:-OmitStackTraceInFastThrow", "");
    // flags to configure Netty
    res.put("-Dio.netty.noUnsafe=", "true");
    res.put("-Dio.netty.noKeySetOptimization=", "true");
    res.put("-Dio.netty.recycler.maxCapacityPerThread=", "0");
    res.put("-Dio.netty.allocator.numDirectArenas=", "0");
    // log4j 2
    res.put("-Dlog4j.shutdownHookEnabled=", "false");
    res.put("-Dlog4j2.disable.jmx=", "true");
    /*
     * Due to internationalization enhancements in JDK 9 Elasticsearch need to set the provider to COMPAT otherwise time/date
     * parsing will break in an incompatible way for some date patterns and locales.
     */
    res.put("-Djava.locale.providers=", "COMPAT");
  }

  public void writeToJvmOptionFile(File file) {
    String jvmOptions = getAll().stream().collect(Collectors.joining("\n"));
    String jvmOptionsContent = ELASTICSEARCH_JVM_OPTIONS_HEADER + jvmOptions;
    try {
      Files.write(file.toPath(), jvmOptionsContent.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new IllegalStateException("Cannot write Elasticsearch jvm options file", e);
    }
  }
}
