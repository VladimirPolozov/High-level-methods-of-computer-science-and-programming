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
- `VolumeValidationUseCase` — отдельный use case для проверки запрошенного объема (`maxVolume`);
- `RequestProcessor` — композитный use case, который последовательно вызывает AuthUseCase, AccessCheckUseCase, VolumeValidationUseCase и возвращает общий ExitCode.
## План реструктуризации проекта
Составить план реструктуризации проекта под принципы чистой архитектуры:
Что нужно вынести в отдельные пакеты.
Какие интерфейсы/абстракции необходимо добавить
