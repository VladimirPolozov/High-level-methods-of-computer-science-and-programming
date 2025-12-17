Сущности:
1. Пользователи (users):
- login (PK)
- password_hash
- salt
2. Ресурсы (resources):
- path (PK) — полный путь
- max_volume
3. Права доступа (permissions):
- user_login (FK)
- resource_path (FK)
- action (READ/WRITE/EXECUTE)

ER-Диаграмма:

erDiagram
    USERS {
        string login PK "Логин пользователя"
        byte password_hash "Хэш пароля"
        byte salt "Соль"
    }

    RESOURCES {
        string path PK "Путь ресурса"
        int max_volume "Максимальный объем"
    }

    PERMISSIONS {
        string user_login FK "Логин пользователя"
        string resource_path FK "Путь ресурса"
        string action "Действие"
    }

    USERS ||--o{ PERMISSIONS : HAS
    RESOURCES ||--o{ PERMISSIONS : GRANTS


Описание:
Интерфейсы ResourceRepository и ResourceRepository остаются, 
а вот [InMemoryResourceRepositoryImpl.kt](../src/infrastructure/adapters/db/repositories/InMemoryResourceRepositoryImpl.kt)
и [InMemoryUserRepositoryImpl.kt](../src/infrastructure/adapters/db/repositories/InMemoryUserRepositoryImpl.kt) 
будут заменены, также в проект добавятся sql скрипты для создания и заполнения, файл
H2ConnectionProvider.kt для подключения к базе. Также обновим enum класс с ошибками.

Драйвер:
Используется H2 (встроенный файловый движок). Подключение через JDBC.