package main

import (
	"flag"
	"fmt"
	"log"

	"github.com/gocql/gocql"
	"github.com/gocql/gocql/scyllacloud"
)

var connectionBundlePath = flag.String("bundle", "connect-bundle-cluster-name.yaml", "Path to the connection bundle file")

func main() {
	flag.Parse()

	cluster, err := scyllacloud.NewCloudCluster(*connectionBundlePath)
	if err != nil {
		log.Fatalf("Failed to create cloud cluster config: %s", err)
	}
	cluster.PoolConfig.HostSelectionPolicy = gocql.DCAwareRoundRobinPolicy("us-east-1")

	session, err := cluster.CreateSession()
	if err != nil {
		log.Fatalf("Failed to connect to cluster: %s", err)
	}

	defer session.Close()

	var query = session.Query("SELECT * FROM system.clients")

	if rows, err := query.Iter().SliceMap(); err == nil {
		for _, row := range rows {
			fmt.Printf("%v\n", row)
		}
	} else {
		log.Fatalf("Query error: %s", err)
	}
}
