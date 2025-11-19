CREATE TABLE users (
    login VARCHAR(255) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL
);

CREATE TABLE resources (
    path VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    max_volume INT NOT NULL
);

CREATE TABLE permissions (
    user_login VARCHAR(255),
    resource_path VARCHAR(255),
    action VARCHAR(10) NOT NULL,
    PRIMARY KEY (user_login, resource_path, action),
    FOREIGN KEY (user_login) REFERENCES users(login),
    FOREIGN KEY (resource_path) REFERENCES resources(path)
);