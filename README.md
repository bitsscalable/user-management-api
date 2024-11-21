
## Prerequisites

Ensure you have the following installed on your system:

- [Docker](https://www.docker.com/) (for containerization)

- Run the mongodb as a container
```
docker run --name mongo-db -p 27017:27017 -d mongo:latest
```

## Getting Started

### Step 1: Clone the Repository

```bash
git clone https://github.com/bitsscalable/book-exchange-api.git
cd book-exchange-api
```

### Step 2: Build the Docker image

docker build -t book-exchange-api .

### Step 3: Run the container

docker run -d -p 8080:8080 book-exchange-api


