module github.com/rjeczalik/serverless-examples/gohelloserverless

go 1.19

replace github.com/gocql/gocql => github.com/scylladb/gocql v1.7.3

require github.com/gocql/gocql v0.0.0-00010101000000-000000000000

require (
	github.com/golang/snappy v0.0.3 // indirect
	github.com/hailocab/go-hostpool v0.0.0-20160125115350-e80d13ce29ed // indirect
	golang.org/x/net v0.17.0 // indirect
	gopkg.in/inf.v0 v0.9.1 // indirect
	gopkg.in/yaml.v2 v2.4.0 // indirect
	sigs.k8s.io/yaml v1.3.0 // indirect
)
