# serverless-examples
Code examples for ScyllaDB Cloud Serverless platform.

### gohelloserverless

Once a connection bundle file has been saved on a local disk, run the following:

```bash
cd gohelloserverless
go run main.go -bundle connection-bundle-cluster-name.yaml
```

### rusthelloserverless

To run the `cloud` example, run the following:

```bash
cd rusthelloserverless
cargo run --example cloud
```

To locally create a single-DC serverless cluster use [`scylla-ccm`](https://github.com/scylladb/scylla-ccm):

```bash
ccm create ccm_12 -i 127.0.1. -n 1 --scylla -v release:4.6.9
ccm start  --sni-proxy --sni-port 7777
```

Then replace the `config_data.yaml` file with the one ccm generated:

```bash
cd rusthelloserverless
cp ~/.ccm/ccm_12/config_data.yaml .
```
