# Use the official MySQL image as the base image
FROM bitnami/mysql:${mysql_version}

# Set environment variables
ENV MYSQL_ROOT_PASSWORD=root!@#$

# Expose the port on which MySQL runs
EXPOSE 3306

# Start MySQL server
CMD ["mysqld"]