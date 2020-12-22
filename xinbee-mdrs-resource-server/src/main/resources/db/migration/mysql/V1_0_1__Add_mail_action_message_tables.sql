CREATE TABLE `r_mail_actions`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT,
    `created_date`       datetime     DEFAULT NULL,
    `last_modified_date` datetime     DEFAULT NULL,
    `action_date`        datetime     DEFAULT NULL,
    `api_user`           varchar(255) NOT NULL,
    `custom_vars`        varchar(255) DEFAULT NULL,
    `email`              varchar(255) NOT NULL,
    `mailing_id`         varchar(255) DEFAULT NULL,
    `status`             int(11) NOT NULL,
    `task_id`            varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `mail_topic_messages`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT,
    `created_date`       datetime DEFAULT NULL,
    `last_modified_date` datetime DEFAULT NULL,
    `message`            longtext,
    `topic`              varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);