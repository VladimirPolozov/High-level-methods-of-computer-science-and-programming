INSERT INTO users (login, password_hash, salt) VALUES
('alice', 'хэш_алисы', 'соль_алисы'),
('bob', 'хэш_боба', 'соль_боба');

INSERT INTO resources (path, name, max_volume) VALUES
('A', 'A', 100),
('A.A8B', 'A8B', 50),
('A.A8B.C', 'C', 30),
('A.A8B.C.f_d', 'f_d', 10);

INSERT INTO permissions (user_login, resource_path, action) VALUES
('alice', 'docs', 'READ'),
('alice', 'docs.internal', 'READ'),
('alice', 'docs.internal', 'WRITE'),
('bob', 'A', 'READ'),
('bob', 'A', 'EXECUTE'),
('bob', 'A.A8B', 'WRITE');