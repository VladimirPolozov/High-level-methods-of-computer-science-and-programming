# Чистая архитектура
## Описание слоёв приложения
### Entities
- `User` — data class с полями `login`, `passwordHash`, `salt` (без методов проверки, её выносим в use cases);
- `Resource` — data class с полями `name`, `maxVolume`, `parent: Resource?`;
- `Action` — `enum` (`READ`, `WRITE`, `EXECUTE`);
- `Permission` — набор разрешённых Action для пользователя на ресурс;
- `AccessRequest` — DTO с полями: `login, password, resourcePath, action, requestedVolume;
- `ExitCode` — `enum` с кодами завершения.
### Services
- `AuthService` проверяет логин/пароль (сравнивает хэш), возвращает результат (`ExitCode`);
- `AccessController` проверяет права на ресурс по пути, учитывая запрпашиваемые действие (`Action`);
- `VolumeValidationService` проверяет, доступен ли пользователю запрошенный объём (`maxVolume`);
- `RequestProcessor` вызывает `AuthService`, `AccessController`, `VolumeValidationService`.
### Repositories
- `AppArgsParser` парсит аргументы CLI, возвращает `AccessRequest` или ошибку;
- `UserRepository` (интерфейс) с реализацией в `InMemoryUserRepository` (хардкод из CreateUsers.kt);
- аналогичный `ResourceRepository` (интерфейс) с реализацией в `InMemoryResourceRepository`.
### Утилиты
- `HashPassword` хэширует пароль пользователя (работает как паттерн Singltone);
- `ExitCodeProcessor` обрабатывает исключения и возвращает код завершение программы;
## План реструктуризации проекта
### Entities
- [ ] вынести data classes: `User` и `Resource`;
- [ ] добавить `enum` `Action`;
- [ ] добавить `enum` `ExitCode`;
- [ ] добавить DTO: `AccessRequest`;
- [ ] удалить логику из `User` и `Resource`.
### Services
- [ ] создать интерфейсы: `AuthService`, `ResourceRepository`, `AccessController`, `VolumeValidator`;
- [ ] реализовать сервисы: `AuthService`, `AccessController`, `VolumeValidationService`, `RequestProcessor`;
### Repositories
- [ ] реализовать `InMemoryUserRepository`, `InMemoryResourceRepository`.
### Дополнительные "утилиты"
- [ ] реализовать хэширования
- [ ] реализовать обработку исключений и возврат кодов приложений
### Планируемая структура проекта
```
project-root/
├── src/
│   ├── domain/                                       # Внутренний слой: Entities (независимые данные)
│   │   ├── entities/                                 # Data classes для домена
│   │   │   ├── User.kt                               # Data class: логин, хэш/соль (ByteArray), permissions (Map<String, Set<Action>>)
│   │   │   ├── Resource.kt                           # Data class: name, maxVolume, permissions (Set<Action>?), parent: Resource?
│   │   │   └── Action.kt                             # Enum: READ, WRITE, EXECUTE (права доступа)
│   │   ├── enums/                                    # Бизнес-константы
│   │   │   └── ExitCode.kt                           # Enum: SUCCESS(0), UNAUTHORIZED(2), FORBIDDEN(3), etc. (коды выхода)
│   │   ├── dto/                                      # DTO для передачи данных (граница слоёв)
│   │   │   └── AccessRequest.kt                      # Data class: login, password, path, action: Action, volume: Int
│   ├── interfaces/                                   # Здесь интерфейсы сервисов
│   │    ├── AuthService.kt                           # Интерфейс: authenticate(login, password): User? (аутентификация)
│   │    ├── AccessController.kt                      # Интерфейс: checkPermission(user, resource, action): ExitCode (права + наследование)
│   │    └── VolumeValidator.kt                       # Интерфейс: validate(volume, resource): ExitCode (лимит объёма)
│   ├── application/                                  # Application Layer: Use Cases (бизнес-логика, оркестрация)
│   │    └── services/                                # Сервисы
│   │       ├── AuthService.kt                        # Implements AuthService: хэширование SHA-256 + соль, возврат User или null
│   │       ├── AccessController.kt                   # Implements AccessController: навигация по parent, сбор inherited permissions
│   │       ├── VolumeValidator.kt                    # Implements VolumeValidator: volume <= maxVolume && >=0
│   │       └── RequestProcessor.kt                   # Композит: process(request: AccessRequest): ExitCode (оркестрация: auth → find → access → volume)
│   ├── infrastructure/                               # Interface Adapters: Адаптеры (репозитории, парсеры)
│   │   ├── adapters/                                 # Конкретные реализации интерфейсов
│   │   │   ├── interfaces/                           # интерфейс репозиториев
│   │   │   │   ├── UserRepository.kt                 # Интерфейс: findByLogin(login): User? (абстракция хранения пользователей)
│   │   │   │   └── ResourceRepository.kt             # Интерфейс: findByPath(path): Resource? (поиск по иерархии)
│   │   │   ├── repositories/                         # In-memory хранилища (хардкод)
│   │   │   │   ├── InMemoryUserRepository.kt         # Implements IUserRepository: listOf<User> (из CreateUsers.kt, с хэшами)
│   │   │   │   └── InMemoryResourceRepository.kt     # Implements IResourceRepository: root Resource, buildTree (parent-ссылки, из CreateResources.kt)
│   │   │   └── AppArgsParser.kt                      # parse(args: Array<String>): AccessRequest? (kotlinx-cli: флаги, справка на -h/invalid)
│   │   └── HashPassword.kt                           # хэширует пароль пользователя (работает как паттерн Singltone);
│   └── app/                                          # Frameworks & Drivers: Точка входа, инфраструктура
│       ├── Main.kt                                   # Entry point: парсинг → фабрика → process → exitProcess(code)
│       ├── ExitCodeProcessor.kt                      # обрабатывает исключения и возвращает код завершение программы;
│       └── components/                               # Фабрики для DI (простая инъекция)
│           └── AppComponents.kt                      # Object: createDefault(): RequestProcessor (создаёт InMemory* + use cases)
└── (root files: скрипты, docs)
    ├── build.sh                                                      # Сборка: kotlinc -include-runtime -d app.jar src/main/kotlin/**/*.kt
    ├── run.sh                                                        # Запуск: java -jar app.jar "$@"
    ├── test.sh                                                       # Тесты: 10+ кейсов (args → run.sh → check $? → OK/FAIL, summary X/10)
    ├── README.md                                                     # Docs: Авторы, инструкции сборки/запуска, описание архитектуры/слоёв
    ├── .gitignore                                                    # *.class, app.jar, target/, *.swp
```
