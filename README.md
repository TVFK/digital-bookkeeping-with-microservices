### PostgreSQL

**Used as a database in the bookkeeping-service module** 

```shell
docker run --name bookkeeping-db -p 5433:5432 -e POSTGRES_DB=bookkeeping-d b -e POSTGRES_USER=librarian -e POSTGRES_PASSWORD=librarian postgres:16
```

### Keycloak

**Used as an OAuth 2.0/OIDC server**

```shell
docker exec -it bookkeeping-keycloak /opt/keycloak/bin/kc.sh export --realm digital-bookkeeping --file /opt/keycloak/bin/config/keycloak/import/digital-bookkeeping.json
```