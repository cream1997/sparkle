-- 账户表
CREATE TABLE IF NOT EXISTS `account`
(
    `id`           bigint      NOT NULL,
    `username` varchar(50) NOT NULL,
    `password`     varchar(50) NOT NULL,
    `deleted`      tinyint(1)  NOT NULL DEFAULT 0,
    `createTime`  datetime    NOT NULL,
    `updateTime`  datetime    NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `account_un` (`username`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- 账户信息表
CREATE TABLE IF NOT EXISTS `account_info`(
                                          `id`           bigint      NOT NULL,
                                          `data`         blob    NOT NULL,
                                          `deleted`      tinyint(1)  NOT NULL DEFAULT 0,
                                          `createTime`  datetime    NOT NULL,
                                          `updateTime`  datetime    NOT NULL,
                                          PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;