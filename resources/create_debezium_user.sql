CREATE USER IF NOT EXISTS 'dbezium'@'%' IDENTIFIED BY 'dbezium@secret';

REVOKE ALL PRIVILEGES ON *.* FROM 'dbezium'@'%';

GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'dbezium'@'%';

FLUSH PRIVILEGES;
