# ========================================================
# PreethiMart - Enterprise Cloud Container Build
# Multi-Stage Dockerfile for Apache Tomcat 9 & Java 17
# ========================================================

# --- Stage 1: Build Application WAR with Maven ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Pre-fetch dependencies to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application sources and build production WAR
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Production Tomcat 9 Runtime ---
FROM tomcat:9.0-jdk17-temurin
LABEL maintainer="Preethi Sundaram <preethi@preethimart.com>"

# Clear default sample applications from webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy PreethiMart WAR as the root web application
COPY --from=builder /workspace/target/preethimart.war /usr/local/tomcat/webapps/ROOT.war

# Install startup entrypoint with dynamic port binding
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN sed -i 's/\r$//' /usr/local/bin/docker-entrypoint.sh && chmod +x /usr/local/bin/docker-entrypoint.sh

# Environment defaults for cloud platforms
ENV PORT=8080
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.awt.headless=true"

EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
