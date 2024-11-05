﻿--CREATE DATABASE elice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
--USE elice;

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) NOT NULL,
    user_pw VARCHAR(100) NOT NULL,
    user_name VARCHAR(20) NOT NULL,
    contact VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_date TIMESTAMP DEFAULT NULL,
    login_lock TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS users_auth (
    user_id VARCHAR(50) NOT NULL,
    authorities VARCHAR(100) NOT NULL DEFAULT 'ROLE_USER',
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS board (
    board_id BIGINT NOT NULL AUTO_INCREMENT,
    board_title VARCHAR(50) NOT NULL,
    PRIMARY KEY (board_id)
);

CREATE TABLE IF NOT EXISTS post (
    post_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    board_id BIGINT NOT NULL,
    post_title VARCHAR(100) NOT NULL,
    post_content VARCHAR(3000) NOT NULL,
    reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_date TIMESTAMP DEFAULT NULL,
    view_count INTEGER DEFAULT 0,
    PRIMARY KEY (post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (board_id) REFERENCES board(board_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    post_id BIGINT NOT NULL,
    comment_content VARCHAR(1000) NOT NULL,
    reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS scrap (
    scrap_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    post_id BIGINT NOT NULL,
    PRIMARY KEY (scrap_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS follow (
    follow VARCHAR(50) NOT NULL,
    follower VARCHAR(50) NOT NULL,
    reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follow, follower),
    FOREIGN KEY (follow) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (follower) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS upload (
    file_uuid VARCHAR(255) NOT NULL,
    post_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    upload_path VARCHAR(255) DEFAULT NULL,
    upload_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    thumbnail_path VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (file_uuid),
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE ON UPDATE CASCADE
);