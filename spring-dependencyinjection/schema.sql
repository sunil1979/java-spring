CREATE DATABASE IF NOT EXISTS `spring_dependencyinjection`
USE `spring_dependencyinjection`;



CREATE TABLE IF NOT EXISTS `message` (
  `id` int(10) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
