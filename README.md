# JavaJDBC
Demo for native database connection

## Postgres set up
Creation docker container
```shell
docker pull postgres
docker run --name postgres -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres
docker exec -it $(docker ps -q --filter name=postgres) bash
psql -U postgres
```
Switch over database
```shell
\c star
```
View tables
```shell
\l
select table_name, column_name from information_schema.columns where table_schema='public';
\dt
```