
CREATE TABLE IF NOT EXISTS Users (
    user_id VARCHAR(255) PRIMARY KEY,
    user_email VARCHAR(255),
    user_password VARCHAR(255),
    created_at DATETIME
);