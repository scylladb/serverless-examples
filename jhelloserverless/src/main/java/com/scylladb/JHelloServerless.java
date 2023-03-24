package com.scylladb;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;


public class JHelloServerless {
  private static String BUNDLE_PATH;

  public static void main(String[] args) throws IOException, ParseException {
    parseOptions(args);
    connect_and_query();
  }

  public static void connect_and_query() throws IOException {
    File bundleFile = new File(BUNDLE_PATH);

    Cluster cluster = Cluster.builder()
        .withLoadBalancingPolicy(DCAwareRoundRobinPolicy.builder().withLocalDc("AWS_US_EAST_1").build()) // your local data center
        .withScyllaCloudConnectionConfig(bundleFile)
        .build();

    System.out.println("Connected to cluster " + cluster.getMetadata().getClusterName());

    for (Host host : cluster.getMetadata().getAllHosts()) {
      System.out.printf("Datacenter: %s, Host: %s, Rack: %s\n", host.getDatacenter(), host.getEndPoint(), host.getRack());
    }

    Session session = cluster.connect("YOUR_KEYSPACE_NAME");
    System.out.println("Connected to cluster " + cluster.getMetadata().getClusterName());
    ResultSet resultSet = session.execute("SELECT * FROM system.clients LIMIT 10"); // some query

    session.close();
    cluster.close();
  }

  static void parseOptions(String[] args) throws ParseException {
    Options options = new Options();
    Option bundlePath = Option.builder()
        .argName("bundle")
        .longOpt("bundle")
        .hasArg()
        .desc("Path to the serverless connection bundle.")
        .required()
        .build();
    options.addOption(bundlePath);

    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine line = parser.parse(options, args);
      BUNDLE_PATH = line.getOptionValue(bundlePath);
    }
    catch (ParseException exp) {
      System.err.println("Parsing failed.  Reason: " + exp.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("java Main", options);
      throw exp;
    }
  }
}