
-- Вставка тестовых ресурсов (необходимы для прохождения test.sh)
-- Используем INSERT OR IGNORE, чтобы не было конфликтов с PRIMARY KEY
INSERT OR IGNORE INTO resources (path, name, max_volume) VALUES
('A.B', 'B', 50),
('A.B.C', 'C', 20),
('A.B.C.f_d', 'f_d', 500),
('X.Y', 'Y', 1000),
('A', 'A', 100);

-- Вставка прав доступа (Permissions)
INSERT OR IGNORE INTO permissions (user_login, resource_path, action) VALUES
-- Тест 1: Успешное выполнение: alice read A.B
('alice', 'A.B', 'READ'),
-- Тест 5: Нет доступа: bob write A.B (У Bob есть только READ доступ)
('bob', 'A.B', 'READ'),
-- Тест 9: Превышение объема: alice read A.B.C.f_d (Max volume 500)
('alice', 'A.B.C.f_d', 'READ'),
-- Тест 10: Успешное выполнение admin: admin execute X.Y
('admin', 'X.Y', 'EXECUTE');