# Order Service

## Description
The **Order Service** is responsible for handling customer orders. It interacts with the **Product Service** via gRPC to fetch product details. The service publishes events to Kafka and listens for product availability responses.

## Features
- Receives orders from clients.
- Calls **Product Service** via gRPC to get product details.
- Publishes order events to **Kafka** for product availability checks.
- Listens for **Product Service** responses and updates order status accordingly.

## Technologies Used
- **Spring Boot**
- **Spring Web**(Rest API)
- **Spring gRPC**
- **Spring Cloud Stream** (Kafka integration)
- **Spring Data JPA**
- **In memory H2 database**

## Installation

1. **Clone the repository**
   ```sh
   git clone https://github.com/elmerjani/order-service.git
   cd order-service
   ```

2. **Build the application**  
   We’ll want to define the stubs for a Java service based on the product_service.proto file:
   ```sh
   ./mvnw clean package
   ```
   You’ll get two new folders in the target directory : target/target/generated-sources/protobuf/grpc-java and target/target/generated-sources/protobuf/java.
   You may need to instruct your IDE to mark them as source roots. In IntelliJ IDEA, you’d right-click the folder, choose Mark Directory As → Generated Source Root. Eclipse or VSCode will add them automatically for you.

3. **Run Kafka with docker**
   ```sh
   docker run -d -p 9092:9092 --name kafka apche/kafka:latest
   ```

4. **Run the service**

   make sure you have the product service up and running ([Link to product repository](https://github.com/elmerjani/product-service.git))
   ```sh
   ./mvnw spring-boot:run
   ```



## Kafka Topics
| Topic Name      | Purpose  |
|----------------|---------|
| `orders-topic`  | Sends order requests to Product Service |
| `products-topic` | Receives product availability status |

## API Endpoints
| Method | Endpoint | Description        |
|--------|---------|--------------------|
| `POST` | `/orders` | Create a new order |
| `GET` | `/orders` | Get all order      |
| `GET` | `/orders/{id}` | Get order details  |

## Example Request
```json
{
  "productId": 1,
  "quantity": 3
}
```
