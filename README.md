## Banking API Coding Challenge
Your assignment is to build an internal API for a fake financial institution using Java and
Spring.

### Brief
While modern banks have evolved to serve a plethora of functions, at their core, banks
must provide certain basic features. Today, your task is to build the basic HTTP API for
one of those banks! Imagine you are designing a backend API for bank employees. It
could ultimately be consumed by multiple frontends (web, iOS, Android etc).

### Tasks
- Implement assignment using:
  - Language: Java 
  - Framework: Spring
- There should be API routes that allow them to:
  - Create a new bank account for a customer, with an initial deposit amount. A single customer may have multiple bank accounts.
  - Transfer amounts between any two accounts, including those owned by different customers. 
  - Retrieve balances for a given account. 
  - Retrieve transfer history for a given account. 
  - Write tests for your business logic

### Assumptions
The system take into consideration following assumptions -
- By default, the accounts are created with a USD currency, other currencies can be added.
- All accounts are pre-populated with 1000.0 USD as balance.
- Transfers are based on accounts and not customer ids (So a user may be transfer between his/her accounts)

### Configurations
The application uses `application.yaml` for its configurations.

### Build
This application use **gradle** as its build plugin, please refer to HELP.md for any addition gradle references.

### Running the application

The application can be run as follows -
> ./gradlew booRun

### Testing the application

This application test suite can be run using the gradle task as follows
> ./gradlew cleanTest test

### API Documentation
Open API documentation can be accessed after running the application
> ./gradlew booRun

Then navigate to http://localhost:8080/v3/api-docs

Swagger UI can be found at http://localhost:8080/swagger-ui/index.html