CREATE TABLE learn_item (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    front VARCHAR(200) NOT NULL,
    tip VARCHAR(1000),
    back VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
) AUTO_INCREMENT=10000;
