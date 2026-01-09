CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_login VARCHAR(50) NOT NULL,
    resource_path VARCHAR(255) NOT NULL,
    action VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_login) REFERENCES users(login)
);