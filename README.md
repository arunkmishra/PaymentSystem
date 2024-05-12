# Payment Processing System
This project implements a simple Payment Service Provider (PSP) system using Scala and Akka HTTP. It handles payment requests, validates them, and simulates interactions with a payment acquirer.
## Features
- **API Endpoint**: Accepts payment details and processes them.
- **Validation**: Validations on fields for payment like credit card number is validated by luhn's algorithm.
- **Mock Acquirer**: Simulates transaction approval or denial based on card details.
- **In-Memory Storage**: Records transactions and their status changes.

## Getting Started
### Prerequisites
- Scala 2.13.13
- sbt (Scala Build Tool) 1.10.0
### Installing
Clone the repository to your local machine:
```bash
git clone https://github.com/arunkmishra/PaymentSystem.git
cd paymentsystem
```
Compile the project and run tests:
`sbt clean compile test`

### Running
- To start the server, use: `sbt run`.
- Run using docker: `docker run -p 8080:8080 payment-system:latest`
- Application will start on http://localhost:8080.

## Usage
## Check Status
To check application running status:
```bash
curl -X GET http://localhost:8080/status
```
### Processing a Payment
To process a payment, send a POST request to `/v1/api/payment` with the following JSON payload:
```json
{
"cardNumber": "4242424242424242",
"expiryDate": "12/27",
"cvv": 123,
"amount": 100.00,
"currency": "USD",
"merchantId": "merchant_001"
}
```
Curl  to send the request:
```bash
curl -X POST http://localhost:8080/v1/api/payment \
-H "Content-Type: application/json" \
-d '{"cardNumber": "4242424242424242", "expiryDate": "12/27", "cvv": 123, "amount": 100.00, "currency": "USD", "merchantId": "merchant_001"}'
```
### Viewing Logs
Check the logs for application at `./application.log` file.
