"""A simple example of connecting to a cluster.

To install the driver run pip install scylla-driver.
"""

from cassandra.cluster import Cluster, ExecutionProfile, EXEC_PROFILE_DEFAULT
from cassandra.policies import DCAwareRoundRobinPolicy, TokenAwarePolicy

PATH_TO_BUNDLE_YAML = '/file/downloaded/from/cloud/connect-bundle-test.yaml'


def get_cluster():
    profile = ExecutionProfile(
        load_balancing_policy=TokenAwarePolicy(
            DCAwareRoundRobinPolicy(local_dc='AWS_US_EAST_1')
        )
    )

    return Cluster(
        execution_profiles={EXEC_PROFILE_DEFAULT: profile},
        scylla_cloud=PATH_TO_BUNDLE_YAML,
        )


print('Connecting to cluster')
cluster = get_cluster()
session = cluster.connect()

print('Connected to cluster', cluster.metadata.cluster_name)

print('Getting metadata')
for host in cluster.metadata.all_hosts():
    print('Datacenter: {}; Host: {}; Rack: {}'.format(
        host.datacenter, host.address, host.rack)
    )

cluster.shutdown()
