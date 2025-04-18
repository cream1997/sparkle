-- hero角色表
CREATE TABLE IF NOT EXISTS `hero_role`(
                                       `id`           bigint      NOT NULL,
                                       `data`         blob    NOT NULL,
                                       `deleted`      tinyint(1)  NOT NULL DEFAULT 0,
                                       `createTime`  datetime    NOT NULL,
                                       `updateTime`  datetime    NOT NULL,
                                       PRIMARY KEY (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;