CREATE TABLE users (
    login VARCHAR(50) PRIMARY KEY,
    password_hash BINARY(64) NOT NULL,
    salt BINARY(32) NOT NULL
);

CREATE TABLE resources (
    path VARCHAR(255) PRIMARY KEY,
    max_volume INT NOT NULL
);

CREATE TABLE permissions (
    user_login VARCHAR(50) NOT NULL,
    resource_path VARCHAR(255) NOT NULL,
    action VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_login, resource_path, action),
    FOREIGN KEY (user_login) REFERENCES users(login)
);