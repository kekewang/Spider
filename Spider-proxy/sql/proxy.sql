CREATE TABLE `spider`.`proxy` (
  `id` INT NOT NULL,
  `ip` VARCHAR(45) NULL COMMENT '代理ip',
  `port` INT NULL COMMENT '端口号',
  `status` INT NULL,
  PRIMARY KEY (`id`));
