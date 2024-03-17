# Use the bitnami/mysql image with the specified version
ARG MYSQL_VERSION
FROM bitnami/mysql:${MYSQL_VERSION}
#FROM arm64v8/mysql:${MYSQL_VERSION}

# Set environment variable for MySQL root password
ENV MYSQL_ROOT_PASSWORD=root1234

# Copy the initialization script into the container
COPY init.sql /docker-entrypoint-initdb.d/init.sql

# Expose port if necessary
EXPOSE 3306

# docker build --build-arg MYSQL_VERSION=8.0 -t MySQL
# docker run -p 3306:3306 MySQL
