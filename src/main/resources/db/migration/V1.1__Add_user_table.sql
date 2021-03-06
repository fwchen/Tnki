CREATE TABLE user (
     id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
     username character(20) NOT NULL UNIQUE,
     password_hash character(150) NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
) AUTO_INCREMENT=10000;