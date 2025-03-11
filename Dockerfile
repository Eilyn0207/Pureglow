# Etapa de construcción (Build)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copiar archivos de configuración de Gradle para cachear dependencias
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Descargar dependencias sin compilar el proyecto (para aprovechar la caché de Docker)
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente después de instalar dependencias
COPY src /app/src

# Compilar el proyecto sin ejecutar pruebas
RUN ./gradlew build -x test --no-daemon

# Etapa de ejecución (Runtime)
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
