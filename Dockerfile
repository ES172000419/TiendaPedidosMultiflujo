FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY TiendaPedidosMultiflujo/dist/TiendaPedidosMultiflujo.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
