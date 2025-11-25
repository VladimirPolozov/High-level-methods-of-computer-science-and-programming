MERGE INTO resources (path, name, max_volume) KEY(path) VALUES
('A', 'A', 100),
('A.B', 'B', 50),
('A.B.C', 'C', 20),
('A.B.C.f_d', 'f_d', 500),
('X.Y', 'Y', 1000);

MERGE INTO permissions (user_login, resource_path, action) KEY(user_login, resource_path, action) VALUES
('alice', 'A.B', 'READ'),
('bob', 'A.B', 'READ'),
('alice', 'A.B.C.f_d', 'READ'),
('admin', 'X.Y', 'EXECUTE');