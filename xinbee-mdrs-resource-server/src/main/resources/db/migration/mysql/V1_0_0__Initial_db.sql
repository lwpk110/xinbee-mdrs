CREATE TABLE `mail_delivery_channels`
(
    `id`        int(11)     NOT NULL auto_increment,
    `name`      varchar(45) NOT NULL,
    `provider`  varchar(45) NOT NULL,
    `host`      varchar(45) NOT NULL,
    `port`      int(11)     NOT NULL,
    `user`      varchar(45)  DEFAULT NULL,
    `pass`      varchar(255) DEFAULT NULL,
    `send_from` varchar(255) DEFAULT NULL,
    `reply`     varchar(255) DEFAULT NULL,
    `config`    varchar(255) DEFAULT NULL,
    `protocol`  varchar(45) NOT NULL,
    `deleted`   bit(1)       DEFAULT b'0',
    `disabled`  bit(1)       DEFAULT b'0',
    PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `mail_delivery_tasks`
(
    `id`                 bigint(20)   NOT NULL auto_increment,
    `name`               varchar(255) NOT NULL,
    `recipient_count`    int(11)      NOT NULL,
    `status`             varchar(45)  NOT NULL,
    `finished_date`      datetime     DEFAULT NULL,
    `custom_fields`      varchar(255) DEFAULT NULL,
    `created_date`       datetime     DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` datetime     DEFAULT CURRENT_TIMESTAMP,
    `version`            bigint(20)   NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `mail_delivery_recipients`
(
    `id`         bigint(20) NOT NULL,
    `recipients` longtext   NOT NULL,
    `count`      int(11)    NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `mail_delivery_templates`
(
    `id`            int(11)  NOT NULL auto_increment,
    `name`          varchar(255) DEFAULT NULL,
    `content`       longtext NULL,
    `substitutions` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `mail_delivery_logs`
(
    `id`                 bigint(20)   NOT NULL auto_increment,
    `name`               varchar(255)          DEFAULT NULL,
    `task_id`            bigint(20)   NOT NULL,
    `email`              varchar(255) NOT NULL,
    `status`             varchar(45)  NOT NULL,
    `invoke_date`        datetime     DEFAULT CURRENT_TIMESTAMP,
    `retry_count`        int(11)      NOT NULL default 0,
    `template_id`        int(11)      DEFAULT NULL,
    `subject`            varchar(255) DEFAULT NULL,
    `channel_id`         int(11)      DEFAULT NULL,
    `created_date`       datetime     DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` datetime     DEFAULT CURRENT_TIMESTAMP,
    `version`            bigint(20)   NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
);



alter table mail_delivery_logs
    add constraint FK_PP18oOOrEf4YVozmwulnWQfLC foreign key (task_id)
        references mail_delivery_tasks (id);
