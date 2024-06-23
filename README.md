 ### PostgreSQL

**Used as a database in the bookkeeping-service module** 

```shell
docker run --name bookkeeping-db -p 5433:5432 -e POSTGRES_DB=bookkeeping-d b -e POSTGRES_USER=librarian -e POSTGRES_PASSWORD=librarian postgres:16
```

### Keycloak

**Used as an OAuth 2.0/OIDC server**

```shell
docker run --name bookkeeping-keycloak -p 8082:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v ./config/keycloak/import:/opt/keycloak/data/import quay.io/keycloak/keycloak:23.0.7 start-dev --import-realm
```

### Victoria Metrics

**Used to collect service metrics**

```shell
docker run --name bookkkeeping-metrics -p 8428:8428 -v ./config/victoria-metrics/promscrape.yaml:/promscrape.yaml victoriametrics/victoria-metrics:v1.93.12 -promscrape.config=/promscrape.yaml
```

### Grafana

**Used to visualize metrics, logs and traces**

```shell
docker run --name bookkeeping-grafana -p 3000:3000 -v ./data/grafana:/var/lib/grafana -u "$(id -u)" grafana/grafana:10.2.4
```

### Grafana Loki

**Used to collect logs**

```shell
docker run --name bookkeeping-loki -p 3100:3100 grafana/loki:2.9.4```
```

### Grafana Tempo

**used to collect traces**

```shell
docker run --name bookkeeping-tracing -p 3200:3200 -p 9095:9095 -p 4317:4317 -p 4318:4318 -p 9411:9411 -p 14268:14268 -v ./config/tempo/tempo.yaml:/etc/tempo.yaml grafana/tempo:2.3.1 -config.file=/etc/tempo.yaml
```