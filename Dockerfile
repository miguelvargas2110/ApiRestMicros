# Usar la imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Crear un directorio para la aplicación
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
# Ajustado para la ubicación de Gradle build/libs
COPY build/libs/TallerApiRest-0.0.1-SNAPSHOT.jar /app/TallerApiRest.jar

# Establecer variables de entorno para la configuración de Spring Boot
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/UserDB
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=ROOT

# Exponer el puerto que utiliza la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/TallerApiRest.jar"]
