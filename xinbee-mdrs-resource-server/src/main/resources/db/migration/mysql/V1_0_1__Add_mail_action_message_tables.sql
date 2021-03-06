CREATE TABLE `r_mail_actions`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
    `created_date`       datetime     DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` datetime     DEFAULT CURRENT_TIMESTAMP,
    `action_date`        datetime     DEFAULT NULL,
    `api_user`           varchar(255) NOT NULL,
    `custom_vars`        varchar(255) DEFAULT NULL,
    `email`              varchar(255) NOT NULL,
    `mailing_id`         varchar(255) DEFAULT NULL,
    `status`             varchar(11)  NOT NULL,
    `task_id`            varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `mail_topic_messages`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
    `created_date`       datetime DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` datetime DEFAULT CURRENT_TIMESTAMP,
    `message`            longtext,
    `topic`              varchar(255) NOT NULL,
    `poll_date`          datetime DEFAULT CURRENT_TIMESTAMP,
    `locked`             bit(1)   DEFAULT b'0',
    PRIMARY KEY (`id`)
);