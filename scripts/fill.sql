MERGE INTO users (login, password_hash, salt) KEY(login) VALUES
('alice', NULL, NULL),
('bob', NULL, NULL),
('admin', NULL, NULL);

MERGE INTO resources (path, max_volume) KEY(path) VALUES
('A', 100),
('A.B',  50),
('A.B.C', 20),
('A.B.C.f_d', 500),
('X.Y',  1000);

MERGE INTO permissions (user_login, resource_path, action) KEY(user_login, resource_path, action) VALUES
('alice', 'A.B', 'READ'),
('bob', 'A.B', 'READ'),
('alice', 'A.B.C.f_d', 'READ');