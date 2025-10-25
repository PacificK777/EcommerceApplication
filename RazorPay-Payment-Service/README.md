# Payment Integration with Spring Boot (RazorPay & Stripe)

This project demonstrates the integration of Razorpay payment gateway with a Spring Boot application. The application provides an API endpoint to initiate a payment using Razorpay.

## Technologies Used

- Java
- Spring Boot
- Maven

## Project Structure

The project follows a standard Maven project structure. The main classes are:

- `PaymentController`: This is the REST controller that handles the payment initiation request.
- `PaymentService`: This is the service class that contains the business logic for payment initiation.
- `RazorpayPaymentGateway`: This class implements the `PaymentGateway` interface and contains the logic for generating a payment link using the Razorpay API.

## Setup and Run

1. Clone the repository.
2. Open the project in your favorite IDE (IntelliJ IDEA recommended).
3. Update the `application.properties` file with your Razorpay key ID and key secret.
4. Run the application.

## API Endpoints

- `POST /payment`: Initiates a payment. The request body should be a JSON object with the following properties:
  - `orderID`: The order ID.
  - `amount`: The payment amount.
  - `name`: The payer's name.
  - `email`: The payer's email.
  - `contactNo`: The payer's contact number.

## Upcoming Features

- Integration with Stripe payment gateway.
