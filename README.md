# Loan Decision Engine

This is a small web application with Java and Spring Boot to make a decision on a loan application.
I made this service as a home task for one of the banks in Estonia for the position of backend developer.

## Business logic

For simplicity, this service uses an in memory user registry with a hard coded set of users:

```text
49002010965 - debt
49002010976 - segment 1 (credit_modifier = 100)
49002010987 - segment 2 (credit_modifier = 300)
49002010998 - segment 3 (credit_modifier = 1000)
```

If a user has a debt, then the service should not approve the loan. Otherwise, the service will make a decision
based on the given amount, period and credit modifier of the user. If the credit score of the user is positive, then
the service will find the higher amount than in the application it can approve.
If the service cannot approve the requested amount and period for a given user, then the service will find the amount 
which is lower than the requested it can approve. The service will also consider a longer period if the amount to 
approved is below the global limit.

## Run the project

Java 17 is required to run the project.

Clone the project and run the following command in the root of the project.

On Mac and Linux:
```shell
./mvnw spring-boot:run
```

On Windows:
```shell
mvnw.cmd spring-boot:run
```

The service will start on `http://localhost:8080`.
To test, send the POST request to the `/loans` endpoint:

```shell
curl --request POST \
  --url http://localhost:8080/loans \
  --header 'Content-Type: application/json' \
  --data '{
	"personalCode": "49002010987",
	"loanAmount": 5000.00,
	"loanPeriod": 55
}'
```

The above endpoint accepts an object with the following three fields:
```java
String personalCode
Double loanAmount
Integer loanPeriod
```
