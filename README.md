# Charity Fundraising Platform

This project is a full-stack, microservices-based web application for creating and managing online fundraising campaigns for various charitable causes. Users can register, create their own charity campaigns, and donate to existing campaigns via PayPal.

## Architecture

The application is built using a microservices architecture, with a React frontend and a set of Spring Boot services for the backend. All services are containerized using Docker and designed to be deployed on Kubernetes.

-   **`Frontend`**: A modern single-page application built with React and Vite, responsible for the user interface and user experience.
-   **`API Gateway`**: Built with Spring Cloud Gateway, this service acts as the single entry point for all client requests. It handles routing, security (JWT validation), and rate limiting.
-   **`Discovery Service`**: Utilizes Eureka for service registration and discovery, allowing backend services to locate and communicate with each other dynamically.
-   **`User Service`**: Manages user-related operations, including registration, authentication (JWT generation), and profile management. It communicates user deletions asynchronously via RabbitMQ.
-   **`Charity Service`**: Handles the creation, retrieval, and management of charity campaigns. It listens for user deletion events to deactivate associated charities.
-   **`Donation Service`**: Manages the donation process, including integration with the PayPal API for payment processing and tracking donation statistics for each campaign.
-   **`Messaging Queue`**: RabbitMQ is used for asynchronous inter-service communication to ensure loose coupling and resilience.
-   **`Database`**: Each core backend service uses its own PostgreSQL database to maintain data isolation.

## Features

-   **User Authentication**: Secure registration and login system with JWT-based authentication.
-   **Profile Management**: Users can view and update their profiles, including name, email, and profile picture.
-   **Charity Campaign Management**: Authenticated users can create multi-step charity campaigns, providing details like title, description, fundraising goal, category, and photos.
-   **Donation System**: Seamless donation process powered by the PayPal REST API.
-   **Dynamic Content**: View a gallery of all charity campaigns and detailed pages for each.
-   **Donation Tracking**: Charity pages display real-time fundraising progress, including the total amount raised and the number of donations.
-   **Admin Functionality**: A dedicated admin role can view a list of all registered users and their status.
-   **Asynchronous Operations**: User deletions trigger asynchronous events via RabbitMQ to update related services.
-   **Containerized & Cloud-Ready**: All services are containerized with Docker and configured for Kubernetes deployment on Azure (AKS).

## Technology Stack

| Category      | Technology                                                                                                  |
| ------------- | ----------------------------------------------------------------------------------------------------------- |
| **Frontend**  | React, Vite, Redux, Redux-Persist, Axios, Material-UI, PayPal React SDK                                     |
| **Backend**   | Java 17, Spring Boot, Spring Cloud (Gateway, Eureka), Spring Security, Spring Data JPA, RabbitMQ, Lombok, JWT |
| **Database**  | PostgreSQL, Flyway                                                                                          |
| **Testing**   | Cypress (E2E), JUnit 5, Mockito                                                                             |
| **DevOps**    | Docker, Kubernetes (Azure AKS), GitLab CI/CD, Azure Container Registry (ACR), Trivy                           |

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

-   Java 17 or later
-   Node.js v18 or later
-   Docker and Docker Compose
-   A running PostgreSQL instance
-   A running RabbitMQ instance

### Backend Setup

1.  **Database Configuration**:
    -   Create a PostgreSQL database named `charity_fundraising_platform`.
    -   Update the database connection details (URL, username, password) in the `application.properties` file for `user-service`, `charity-service`, and `donation-service`.

2.  **RabbitMQ Configuration**:
    -   Ensure your RabbitMQ instance is running.
    -   Update the connection details in the `application.properties` files of the services if they differ from the defaults (`localhost:5672`).

3.  **Run the Services**:
    Open a terminal for each backend service located in the `backend/` directory and run them using the Gradle wrapper. It is recommended to start them in the following order:
    -   `discovery-service`
    -   `user-service`
    -   `charity-service`
    -   `donation-service`
    -   `api-gateway`

    ```bash
    # Example for starting user-service
    cd backend/user-service
    ./gradlew bootRun
    ```

### Frontend Setup

1.  **Navigate to the frontend directory**:
    ```bash
    cd frontend
    ```

2.  **Install dependencies**:
    ```bash
    npm install
    ```

3.  **Configure Environment**:
    -   Create a `.env.development` file in the `frontend` root.
    -   Add the API Gateway URL:
        ```env
        VITE_API_BASE_URL=http://localhost:8080
        ```

4.  **Run the application**:
    ```bash
    npm run dev
    ```
    The application will be available at `http://localhost:5173`.

## CI/CD Pipeline

The project is configured with GitLab CI/CD pipelines for each service. The pipelines are defined in the `.gitlab-ci.yml` file within each service's directory and automate the following stages:

1.  **Build**: Compiles the application and builds a Docker image.
2.  **Test**: Runs unit tests (JUnit/Mockito) and E2E tests (Cypress). SonarQube analysis is also configured.
3.  **Security**: Scans source code and Docker images for vulnerabilities using Trivy.
4.  **Deploy**: Pushes the Docker image to Azure Container Registry (ACR) and deploys it to Azure Kubernetes Service (AKS) for the backend services and Azure Web App for the frontend. (Note: The deployment steps are currently commented out in the CI files).
