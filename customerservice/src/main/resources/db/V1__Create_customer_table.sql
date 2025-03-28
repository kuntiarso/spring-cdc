CREATE TABLE IF NOT EXISTS `customer` (
    `customer_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `phone_number` VARCHAR(20),
    `address` TEXT,
    `city` VARCHAR(100),
    `state` VARCHAR(100),
    `zip_code` VARCHAR(20),
    `country` VARCHAR(100),
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` DATETIME DEFAULT NULL,
    `created_by` VARCHAR(100) NOT NULL,
    `updated_by` VARCHAR(100) NOT NULL
);
