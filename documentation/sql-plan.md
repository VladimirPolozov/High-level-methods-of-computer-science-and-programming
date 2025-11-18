Сущности:
1. Пользователи (users):
- login (PK)
- password_hash
- salt
2. Ресурсы (resources):
- path (PK) — полный путь
- name
- max_volume
3. Права доступа (permissions):
- user_login (FK)
- resource_path (FK)
- action (READ/WRITE/EXECUTE)

ER-Диаграмма:

erDiagram
USERS {
string login PK
string password_hash
string salt
}

    RESOURCES {
        string path PK
        string name
        int max_volume
    }

    PERMISSIONS {
        string user_login FK
        string resource_path FK
        string action
    }

    USERS ||--o{ PERMISSIONS : has
    RESOURCES ||--o{ PERMISSIONS : grants


Описание:
Интерфейсы ResourceRepository и ResourceRepository остаются, 
а вот [InMemoryResourceRepositoryImpl.kt](../src/infrastructure/adapters/repositories/InMemoryResourceRepositoryImpl.kt)
и [InMemoryUserRepositoryImpl.kt](../src/infrastructure/adapters/repositories/InMemoryUserRepositoryImpl.kt) 
будут заменены, также в проект добавятся sql скрипты для создания и заполнения, файл
di.DatabaseManager.kt для подключения к базе. Также обновим enum класс с ошибками.

Драйвер:
Используется H2 (встроенный файловый движок). Подключение через JDBC.