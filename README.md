# Fifty Two Books Tracker

A Spring Boot REST API demo app for tracking the 52-book challenge, containerized with Docker and deployed to AWS EKS. It uses AWS RDS (PostgreSQL) for persistence, S3 for storage, ECR for container registry, and GitHub Actions for CI/CD.

## Features

- **CRUD** operations on books
- Persistent storage in **PostgreSQL (RDS)**
- Static asset storage in **S3**
- Container image hosting in **ECR**
- Kubernetes orchestration in **EKS**
- Automated build & deploy via **GitHub Actions**

## Tech Stack

- Java 17 & Spring Boot  
- PostgreSQL on AWS RDS  
- AWS S3 for object storage  
- Docker & AWS ECR  
- Kubernetes & AWS EKS  
- CI/CD with GitHub Actions

## Prerequisites

- AWS CLI configured with an IAM user/role that can manage EKS, ECR, RDS, S3  
- `kubectl` installed  
- Docker installed  
- A GitHub repository with these **Secrets** defined:

- `AWS_ACCESS_KEY_ID`  
- `AWS_SECRET_ACCESS_KEY`  
- `AWS_REGION` (e.g. `eu-west-1`)  
- `ECR_REPOSITORY` (e.g. `123456789012.dkr.ecr.eu-west-1.amazonaws.com/fifty-two-books-tracker`)  
- `EKS_CLUSTER_NAME`  
- `KUBE_NAMESPACE` (e.g. `default`)  
- `DB_HOST` (RDS endpoint)  
- `DB_PORT` (e.g. `5432`)  
- `DB_NAME`  
- `DB_USERNAME`  
- `DB_PASSWORD`  
- `AWS_S3_BUCKET_NAME`

## Local Setup

1. Clone the repo:  
   ```bash
   git clone https://github.com/your-org/fifty-two-books-tracker.git
   cd fifty-two-books-tracker
2. Build with Maven:
   ```bash
   mvn clean package
3. Run locally:
   ```bash
   java -jar target/*.jar
4. The API will be available at http://localhost:8080.

## CI/CD with GitHub Actions

The workflow in `.github/workflows/ci-cd.yml` does:

- **Build the Docker image** (latest + SHA tags)
- **Push to AWS ECR**
- **Apply Kubernetes manifests** and update the Deployment image on EKS

Make sure your GitHub Secrets match the ones listed above.

## API Endpoints

| Method | Path           | Description        |
| ------ | -------------- | ------------------ |
| GET    | `/books`       | List all books     |
| POST   | `/books`       | Create a new book  |
| GET    | `/books/{id}`  | Retrieve one book  |
| PUT    | `/books/{id}`  | Update a book      |
| DELETE | `/books/{id}`  | Delete a book      |

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

