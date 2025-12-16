# Чистая архитектура
## Описание слоёв приложения
### Entities
- `User` — data class с полями: login, passwordHash (ByteArray?), salt (ByteArray?). Не содержит логики.
- `Resource` — data class с полями: path (полный путь), maxVolume.
- `Action` — enum (READ, WRITE, EXECUTE).
- `AccessRequest` — DTO с полями: login, password, resourcePath, action, requestedVolume.
- `ExitCode` — enum с кодами завершения. Включает коды ошибок БД (9, 10).
### Services
- `AuthService` — Интерфейс для аутентификации. AuthServiceImpl проверяет логин/пароль (сравнивает хэш), 
использует UserRepository.
- `AccessController` — Интерфейс для проверки прав. AccessControllerImpl проверяет права на ресурс по пути, 
используя ResourceRepository и PermissionRepository.
- `VolumeValidator` — Интерфейс для проверки объема. VolumeValidatorImpl проверяет, доступен ли запрошенный 
объем.
- `ActionAndPathValidator` — Интерфейс для проверки корректности формата пути и действия.
- `RequestProcessor` — Оркестратор. Вызывает AuthService, ActionAndPathValidator, AccessController, VolumeValidator.
### Repositories (Infrastructure Layer)
Адаптеры, реализующие интерфейсы, определенные в Domain.
- `UserRepository` — Интерфейс. Реализован как JdbcUserRepositoryImpl.
- `ResourceRepository` — Интерфейс. Реализован как JdbcResourceRepositoryImpl.
- `AppArgsParser` — Парсит аргументы CLI, возвращает AccessRequest или ошибку.
###  Утилиты и Инфраструктура (Infrastructure/App Layer)
- `HashService` — Хэширует пароль (SHA-256 + соль) и генерирует соль. Работает как синглтон.
- `H2ConnectionProvider` — Управляет JDBC-подключением к базе H2.
- `AppContainer` — Фабрика зависимостей (DI). Содержит логику инициализации паролей в БД (если salt = NULL).
### Планируемая структура проекта
```
project-root/
├── src/
│   ├── domain/                                       # 1. ДОМЕН (Entities, DTO, Interfaces) - ЯДРО
│   │   ├── entities/                                 # Сущности (User, Resource, Action)
│   │   │   ├── Resourse.kt
│   │   │   └── User.kt
│   │   ├── enums/
│   │   │   ├── Action.kt                             # Определяет разрешенные действия
│   │   │   └── ExitCode.kt                           # Коды завершения программы
│   │   ├── dto/
│   │   │   └── AccessRequest.kt                      # Объект передачи данных, который инкапсулирует все аргументы командной строки для передачи в Application Layer.
│   │   ├── exceptions/
│   │   │   └── AuthException.kt                      # Исключение AuthException. Базовый класс для всех ошибок аутентификации
│   │   ├── reopository/                              # ИНТЕРФЕЙСЫ РЕПОЗИТОРИЕВ (Порты, абстракции данных)
│   │   │   ├── UserRepository.kt                     
│   │   │   └── ResourceRepository.kt                 
│   │   └── services/                                 # ИНТЕРФЕЙСЫ СЕРВИСОВ (Абстракции бизнес-логики)
│   │       ├── AccessController.kt                   # Контракт для проверки прав доступа
│   │       ├── ActionAndPathValidator.kt             # Контракт для проверки корректности введенных действия и пути ресурса
│   │       ├── AuthService.kt                        # Контракт для аутентификации пользователя
│   │       └── VolumeValidator.kt                    # Контракт для проверки лимита объема
│   ├── application/                                  # 2. СЛОЙ ПРИЛОЖЕНИЯ 
│   │    └── services/                                # Реализации бизнес-логики
│   │       ├── AccessControllerImpl.kt                       
│   │       ├── ActionAndPathValidatorImpl.kt     
│   │       ├── AuthServiceImpl.kt  
│   │       ├── RequestProcessor.kt                   (Оркестратор)
│   │       └── VolumeValidatorImpl.kt                   
│   ├── infrastructure/                               # 3. АДАПТЕРЫ И ИНФРАСТРУКТУРА
│   │   ├── adapters/                                 
│   │   │   ├── cli/                                  
│   │   │   │   └── AppArgsParser.kt                  
│   │   │   ├── db/                                   
│   │   │   │   └── repositories/                     # Реализации репозиториев (JDBC)
│   │   │   │       ├── JdbcUserRepositoryImpl.kt     
│   │   │   │       └── JdbcResourceRepositoryImpl.kt 
│   │   │   ├── mappers/                              # Маппинг ResultSet <-> Domain
│   │   │   │   ├── UserMapper.kt                  
│   │   │   │   └── ResourceMapper.kt
│   │   │   ├── dto/                             
│   │   │   │   └── JdbcUserDto.kt
│   │   │   ├── security/                             
│   │   │   │   └── HashService.kt                    # Синглтон для хэширования (SHA-256 + соль)
│   │   └── db/                                       
│   │       └── H2ConnectionProvider.kt               # Синглтон для JDBC подключения                      
│   ├── app/                                          # 4. ВНЕШНИЕ ДРАЙВЕРЫ / ТОЧКА ВХОДА
│   │   ├── ExitCodeProcessor.kt                      # Обработка исключений
│   │   └── components/                               
│   │       └── AppContainer.kt                       # Фабрика зависимостей), **включает логику инициализации паролей**
│   └── Main.kt                                       # Точка входа, вызывает AppContainer
└── (root files: скрипты, docs)
    ├── scripts/
    │   ├── init.sql                                  # DDL: CREATE TABLE (USERS, RESOURCES, PERMISSIONS)
    │   ├── fill.sql                                  # DML: MERGE INTO (логины, ресурсы, права, **пароли=NULL**)
    │   ├── init-db.sh                                # Запускает H2 RunScript для init.sql и fill.sql
    │   ├── build.sh                                  
    │   └── run.sh
    ├── documentation/
    └── lib/

```