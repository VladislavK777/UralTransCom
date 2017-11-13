--Таблица станций

CREATE TABLE `restapi`.`stations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `station_key` VARCHAR(6) NOT NULL COMMENT 'Код станции',
  `station_name` VARCHAR(150) NOT NULL COMMENT 'Название станции',
  `road` VARCHAR(45) NULL COMMENT 'Дорога, сокращенное название(Пример: МСК)',
  `roadFull` VARCHAR(100) NULL COMMENT 'Полное название ЖД(Пример: Московская ж.д.)',
  PRIMARY KEY (`id`))
COMMENT = 'Станции; Коды; ЖД';

--Таблица расстояний

CREATE TABLE `restapi`.`distancebetweentwostations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `station_key_start` VARCHAR(6) NOT NULL COMMENT 'Код станции старта',
  `station_name_start` VARCHAR(100) NOT NULL COMMENT 'Название станции старта',
  `road_station_start` VARCHAR(10) NULL COMMENT 'Дорога станции старта(сокращенная форма)',
  `station_key_end` VARCHAR(6) NOT NULL COMMENT 'Код станции конца',
  `station_name_end` VARCHAR(100) NOT NULL COMMENT 'Название станции конца',
  `road_station_end` VARCHAR(10) NULL COMMENT 'Дорога станции конца(сокращенная форма)',
  `distance` INT NOT NULL COMMENT 'Расстояние между станцией старта и станцией конца(км)',
  PRIMARY KEY (`id`));
  
--Таблица железных дорог

CREATE TABLE `restapi`.`roads` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `road_name` VARCHAR(10) NULL COMMENT 'Сокращенное название ЖД (МСК)',
  `road_fullname` VARCHAR(100) NULL COMMENT 'Полное название ЖД (Москвоская ж.д.)',
  `country_of_road` VARCHAR(100) NULL COMMENT 'Страна ЖД (Россия)',
  PRIMARY KEY (`id`))
COMMENT = 'Таблица железных дорог';

--Таблица грузов

CREATE TABLE `restapi`.`cargo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `key_cargo` VARCHAR(8) NOT NULL COMMENT 'Код груза (8 знаков)',
  `name_cargo` VARCHAR(300) NOT NULL COMMENT 'Наименование груза',
  `class_cargo` INT NOT NULL COMMENT 'Класс груза (0, 1, 2, 3)',
  PRIMARY KEY (`id`))
COMMENT = 'Таблица грузов и их классы';


