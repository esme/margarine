# MargarineV2

## Quick Start with Example

This example has a "`Hello, World`" RESTful web service with Spring.

The service accepts HTTP GET requests at `http://localhost:8080/greeting`.

It will respond with a JSON representation of a greeting, as the following listing shows:

```
{"id":1,"content":"Hello, World!"}
```
## Running

Spring Boot backend server
```
./gradlew bootRun
```

React Frontend server
```
cd src/react-frontend/
yarn install
yarn start
```
