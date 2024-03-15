#FROM arm64v8/mysql:latest
FROM bitnami/mysql:latest

# Set environment variables
ENV MYSQL_ROOT_PASSWORD=root1234

# Copy the initialization script into the docker-entrypoint-initdb.d directory
COPY init.sql /docker-entrypoint-initdb.d/

# Expose port if necessary
EXPOSE 3306

