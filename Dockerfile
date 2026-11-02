# Etapa 1: compilar a aplicação Java com Maven
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# Copia o projeto
COPY pom.xml .
COPY src ./src

# Gera o arquivo WAR
RUN mvn clean package -DskipTests

# Etapa 2: executar no Apache Tomcat
FROM tomcat:11-jre25-temurin

# Remove a aplicação padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Publica o WAR como aplicação raiz
COPY --from=build /app/target/Fixa-Backend.war /usr/local/tomcat/webapps/ROOT.war

# Configura o Tomcat para a porta utilizada pelo Render
RUN sed -i 's/port="8080"/port="10000"/' /usr/local/tomcat/conf/server.xml

# Porta pública do serviço
EXPOSE 10000

# Inicia o servidor
CMD ["catalina.sh", "run"]