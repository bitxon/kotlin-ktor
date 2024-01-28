# Ktor Microservice

## Run application

Run application in development mode (embedded h2 database)
```shell
./gradlew run
```

Run application in production mode (postgres database)
```shell
docker-compose up -d
```
```shell
DEVELOPMENT=false ./gradlew run
```

## Test application

<details>
    <summary>Create Account</summary>

```shell
curl --location 'http://localhost:8080/accounts' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "mike@mail.com",
    "firstName": "Mike",
    "lastName": "Brown",
    "dateOfBirth": "2000-03-17",
    "currency": "EUR",
    "moneyAmount": 78
}'
```
</details>

<details>
    <summary>Get All Accounts</summary>

```shell
curl --location 'http://localhost:8080/accounts'
```
</details>

<details>
    <summary>Get Account By Id</summary>

```shell
curl --location 'http://localhost:8080/accounts/1'
```
</details>
