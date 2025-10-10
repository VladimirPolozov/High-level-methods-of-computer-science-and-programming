# Чистая архитектура
## Описание слоёв приложения
### Entitie;
- `User` — data class с полями `login`, `passwordHash`, `salt` (без методов проверки, её выносим в use cases);
- `Resource` — data class с полями `name`, `maxVolume`, `parent: Resource?`;
- `Action` — `enum` (`READ`, `WRITE`, `EXECUTE`);
- `Permission` — набор разрешённых Action для пользователя на ресурс;
- `AccessRequest` — DTO с полями: `login, password, resourcePath, action, requestedVolume;
- `ExitCode` — `enum` с кодами завершения.
### Use cases
- `AuthService` проверяет логин/пароль (сравнивает хэш), возвращает результат (`ExitCode`);
- `AccessController` проверяет права на ресурс по пути, учитывая запрпашиваемые действие (`Action`);
- `VolumeValidationUseCase` — отдельный use case для проверки запрошенного объема (`maxVolume`).
### Interface Adapters
- `AppArgsParser` парсит аргументы CLI, возвращает `AccessRequest` или ошибку;
- `UserRepository` (интерфейс) с реализацией в `InMemoryUserRepository` (хардкод из CreateUsers.kt);
- 
## План реструктуризации проекта
### Entities
- вынести data classes: `User` и `Resource`;
- добавить `enum` `Action`;
- добавить `enum` `ExitCode`;
- добавить DTO: `AccessRequest`;
- удалить логику из `User` и `Resource`;
- 
