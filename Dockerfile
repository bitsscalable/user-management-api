FROM maven:3.9.4-eclipse-temurin-17

# Set a working directory inside the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Run Maven to clean and install the application
RUN mvn clean install

# Expose the port your application runs on (default is 8080 for Spring Boot)
EXPOSE 8080

# Command to start the application using Maven
CMD ["mvn", "spring-boot:run"]

