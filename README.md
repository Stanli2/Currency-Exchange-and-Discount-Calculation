A Spring Boot application that integrates with a third-party currency exchange API to retrieve real-time exchange rates and calculate discounted bill amounts in different currencies.
Features

Currency conversion between different currencies using real-time exchange rates
Discount application based on user type, customer tenure, and bill amount
Caching of exchange rates to minimize API calls
Authentication for secure API access
Comprehensive test coverage

API Integration
This application integrates with the Open Exchange Rates API:

Endpoint: https://open.er-api.com/v6/latest/{base_currency}

Prerequisites

Java 17 
Maven 3.9.4
An API key from Open Exchange Rates

Configuration

Clone the repository:
git clone https://github.com/yourusername/currency-exchange-app.git
cd currency-exchange-app

Update the application.properties file with your API key:
exchange.api.url=https://open.er-api.com/v6/latest/{base_currency}
exchange.api.key=your-api-key
server.port=8080


Building the Application
Build the project
mvn clean install

# Run the application
mvn spring-boot:run

Using Maven
bash# Run tests
mvn test



Code Quality Analysis
Using Maven
bash# Run static code analysis
mvn spotbugs:check pmd:check checkstyle:check
SonarQube Analysis
Prerequisites

SonarQube server running locally or accessible remotely

Using Maven
bash# Run SonarQube analysis
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=your-sonar-token

API Usage
Calculate Bill Amount
Endpoint: POST /api/calculate
Authentication: Basic Authentication (username: user, password: password)
Request Body:
json{
  "items": [
    {
      "name": "Electronics",
      "price": 100.00,
      "category": "ELECTRONICS"
    },
    {
      "name": "Clothing",
      "price": 200.00,
      "category": "CLOTHING"
    }
  ],
  "user": {
    "id": "1",
    "name": "John Doe",
    "type": "EMPLOYEE",
    "tenureYears": 3
  },
  "baseCurrency": "USD",
  "targetCurrency": "EUR"
}
Response:
json{
  "originalAmount": 300.00,
  "baseCurrency": "USD",
  "discountedAmount": 200.00,
  "convertedAmount": 170.00,
  "targetCurrency": "EUR",
  "exchangeRate": 0.85
}
Discount Rules

Employee discount: 30% (not applicable to groceries)
Affiliate discount: 10% (not applicable to groceries)
Loyal customer discount (>2 years): 5% (not applicable to groceries)
$5 discount for every $100 on the bill (applicable to all items)
Only one percentage-based discount can be applied per bill (best one is chosen)

Architecture
The application follows a layered architecture:

API Layer: REST controllers to expose endpoints
Service Layer: Business logic for discount calculation and currency conversion
Integration Layer: Client to interact with the external currency exchange API
Model Layer: Domain objects representing bills, items, users, etc.

Design patterns used:

Strategy Pattern for discount calculation
Builder Pattern for object creation
Dependency Injection for loose coupling
Caching for improved performance
