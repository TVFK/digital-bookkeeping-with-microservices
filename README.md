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