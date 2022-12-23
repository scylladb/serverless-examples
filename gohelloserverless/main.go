package main

import (
	"fmt"
	"log"

	"github.com/gocql/gocql"
	"github.com/gocql/gocql/scyllacloud"
)

const (
	connectionBundlePath = "/file/downloaded/from/cloud/connect-bundle-test.yaml"
)

func main() {
	cluster, err := scyllacloud.NewCloudCluster(connectionBundlePath)
	if err != nil {
		log.Fatalf("Failed to create cloud cluster config: %s", err)
	}
	cluster.PoolConfig.HostSelectionPolicy = gocql.DCAwareRoundRobinPolicy("AWS_US_EAST_1")

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
