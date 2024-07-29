CQL="CREATE KEYSPACE IF NOT EXISTS filesHashesKeyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /usr/local/bin/docker-entrypoint.sh "$@"