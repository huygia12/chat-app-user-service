# About project

this is a chat app's service that manage users

## ⇁ Prerequisites

jdk's required version: 21<br>
database of your choice<br>

## ⇁ Setup

first, clone this project<br>
next, config git hooks<br>

```shell
git config core.hooksPath '.git-hooks'
```

verify right hook directory:

```shell
git rev-parse --git-path hooks
```

you need to have `.env` file in `environments` folder (can
be `environments/dev` or `environment/prod` folder), in the file you
need `key=value` each line. See list of required environment
variables [here](#-list-of-available-environment-variables):<br>

## ⇁ List of available environment variables

# Environment variables

| Variable               | Required | Purpose                                                                                                                   |
| ---------------------- | -------- | ------------------------------------------------------------------------------------------------------------------------- |
| SPRING_PROFILES_ACTIVE | YES      | your environment, currently it can be `dev` or `prod`                                                                     |
| PORT                   | YES      | server's port. Note that it must match container port if you want to use docker compose to run your app                   |
| DOMAIN                 | YES      | your domain name                                                                                                          |
| PROTOCOL               | YES      | can be `http` or `https`. In production, you need to set `http` if your port is `80`, `https` if your port is `443`       |
| API_VERSION            | YES      | API version. For example: `v1`                                                                                            |
| DATABASE               | YES      | your chosen database. For example: `postgresql`                                                                           |
| DRIVER_CLASS_NAME      | YES      | example for postgresql: `org.postgresql.Driver`                                                                           |
| DB_DRIVER              | YES      | for example, postgresql driver will be `jdbc:postgresql:/`                                                                |
| DB_HOST                | YES      | host name                                                                                                                 |
| DB_NAME                | YES      | database schema name                                                                                                      |
| DB_PORT                | YES      | for example, postgresql will be `5432`                                                                                    |
| DB_USER                | YES      | database user                                                                                                             |
| DB_PASSWORD            | YES      | password of database user                                                                                                 |
| BCRYPT_STRENGTH        | NO       | this service will en/decrypt token using `bcrypt`, you can define how strong you want this algorithm to be. Default: `12` |
| USERNAME               | YES      | admin username (for whole system control)                                                                                 |
| PASSWORD               | YES      | admin password. Notice that the password must be encrypted by bcrypt with correct configuration, not raw password         |
| ID_SERVICE_PORT        | YES      | port to the id generator service gRPC                                                                                     |
| ID_SERVICE_ADDRESS     | YES      | for example, run id generator service in local host will be `localhost`                                                   |
| CERT_DIR               | YES      | path to folder that contains your certs. For example: `certs`, this must be set to `/etc/certs` when running in container |

For the full .env file example, check
out [this template](./templates/.env.template)

## ⇁ Getting Started

if you change the code, then run this first to format the code:

```shell
make clean
```

first, you need to have `.env` file inside root folder. See more
in [here](#-list-of-available-environment-variables)<br>
from now on, you can add `TLS` option along with the command to enable the mutual TLS handshake, it takes the boolean type value (true | false)<br>
you can run the development server by this command:

```shell
make run
```

NOTE: if your `.env` file has different name, add `ENV_FILE` option in the command (this can work to all `make` command). For example:

```shell
make run ENV_FILE=.env.dev
```

if you want to build docker image, use:

```shell
make build
```

then you can run docker with:

```shell
make server
```

after you run the app, you can go to `/swagger-ui/index.html#/` endpoint to see
swagger (this can only work if `SPRING_PROFILES_ACTIVE=dev`)

## ⇁ Run tests

first, you need to have `.env` file inside `environments/dev` folder. See more
in [here](#-list-of-available-environment-variables)<br>
You must enable tls handshake in id-generator-server then specify `TLS` param along with the command to pass all the test-cases

### ⇁ Run all tests

```shell
make test
```

### ⇁ Run test of 1 method in the class

you can do this by running `./mvnw_wrapper.sh test -Dtest=class#method`. For
example:

```shell
./mvnw_wrapper.sh test -Dtest=ChatAppUserServiceApplicationTests#updateUser_200ValidRequest_success
```

## ⇁ Deploy
