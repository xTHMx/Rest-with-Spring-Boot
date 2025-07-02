FROM ubuntu:latest
LABEL authors="tulio"

# Usa uma imagem base do Java 21
FROM eclipse-temurin:21-jdk

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR compilado para dentro do container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta usada pela aplicação (ajuste se necessário)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]