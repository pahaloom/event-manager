
# A tiny event register

## Requirements for backend
This project requires java 21.
Bundled gradle can be used with following command:
`./gradlew bootRun`
This will result in the backend listening on port 8080.

## Requirements for frontend (in frontend-react directory)
Frontend has been built using node v18.17.0.
Older versions might not quite work.
Quick way to compile and run using pnpm:
`pnpm install
pnpm run start`
Last command will make frontend to listen on port 3000 by default.
In case it is needed to change backend URL, the constant EVENTS_URL can be found in src/constants.js file.

