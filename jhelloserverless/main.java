package com.scylladb.cluster_connection;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;


public class ConnectionExample {
    public void connect_and_query() {
        File bundleFile = new File("/file/downloaded/from/cloud/connect-bundle-test.yaml");

        final ScyllaCloudConnectionConfig cloudConfig = ScyllaCloudConnectionConfig.fromInputStream(new FileInputStream(bundleFile));

        Cluster cluster = Cluster.builder()
        .withLoadBalancingPolicy(DCAwareRoundRobinPolicy.builder().withLocalDc("AWS_US_EAST_1").build()) // your local data center
        .withScyllaCloudConnectionConfig(cloudConfig)
        .build();

        System.out.println("Connected to cluster " + cluster.getMetadata().getClusterName());

        for (Host host: cluster.getMetadata().getAllHosts()) {
          System.out.printf("Datacenter: %s, Host: %s, Rack: %s\n", host.getDatacenter(), host.getEndPoint(), host.getRack());
        }

        Session session = cluster.connect("YOUR_KEYSPACE_NAME");
        System.out.println("Connected to cluster " + metadata.getClusterName())
        ResultSet resultSet = session.execute("SELECT * FROM system.clients LIMIT 10"); // some query

        session.close();
        cluster.close();
    }
}
