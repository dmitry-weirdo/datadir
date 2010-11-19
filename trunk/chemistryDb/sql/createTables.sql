drop table enum;
drop table attribute;
drop table entity;
drop table section;

CREATE TABLE `section` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1251;

CREATE TABLE `entity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `section_id` int(11) NOT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `section_id` (`section_id`),
  CONSTRAINT `entity_fk` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1251;

CREATE TABLE `attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` smallint(6) NOT NULL,
  `measure_unit` varchar(255) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `column_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `entity_id` (`entity_id`),
  CONSTRAINT `attribute_fk` FOREIGN KEY (`entity_id`) REFERENCES `entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1251;

CREATE TABLE `enum` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `attribute_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `attribute_id` (`attribute_id`),
  CONSTRAINT `enum_fk` FOREIGN KEY (`attribute_id`) REFERENCES `attribute` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=cp1251;