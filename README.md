# MargarineV2

## Quick Start with Example

This example has a "`Hello, World`" RESTful web service with Spring.

The service accepts HTTP GET requests at `http://localhost:8080/greeting`.

It will respond with a JSON representation of a greeting, as the following listing shows:

```
{"id":1,"content":"Hello, World!"}
```
## Running
The MongoDB password needs to be passed as an environment variable in order to connect to the database:
```
export $JASYPT_ENCRYPTOR_PASSWORD=ENTER_PASSWORD_HERE
```
Spring Boot backend server
```
./gradlew bootRun
```

React Frontend server
```
cd src/react-frontend/
yarn install

# Option 1: to create a production build of the app and serve files on NodeJS server
yarn start

# Option 2: to start frontend client only
yarn start-client
```

## Frontend Routing
The frontend server sends the `index.html` file in response for the `/` route, and Express.js serves all the files from the build folder generated by React.

Any requests to `http://localhost:3000` with a slug, i.e. `http://localhost:3000/goo123` would redirect to `http://google.com`.
