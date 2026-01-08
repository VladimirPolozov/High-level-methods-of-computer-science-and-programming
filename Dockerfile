# Базовый образ — OpenJDK 17
FROM openjdk:17-slim

# Копируем JAR и зависимости
COPY out/app.jar /app.jar

# Указываем команду запуска
CMD ["java", "-jar", "/app.jar"]