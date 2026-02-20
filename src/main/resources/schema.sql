-- 1. 野菜マスター
CREATE TABLE IF NOT EXISTS `vegetables` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `family` varchar(50) DEFAULT NULL,
  `is_tall` tinyint(1) DEFAULT '0',
  `unit_width` int NOT NULL DEFAULT '30',
  `unit_height` int NOT NULL DEFAULT '30',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- 2. 農地データ
CREATE TABLE IF NOT EXISTS `farmlands` (
  `id` int NOT NULL AUTO_INCREMENT,
  `width` int NOT NULL,
  `height` int NOT NULL,
  `gap` int NOT NULL,
  `offset` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- 3. 畝データ（農地に紐づく）
CREATE TABLE IF NOT EXISTS `ridges` (
  `id` int NOT NULL AUTO_INCREMENT,
  `farmland_id` int NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `width` int NOT NULL,
  `height` int NOT NULL,
  `x` int NOT NULL,
  `y` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_ridge_farmland` FOREIGN KEY (`farmland_id`) REFERENCES `farmlands` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- 4. 配置エリア（野菜と畝に紐づく）
CREATE TABLE IF NOT EXISTS `plant_areas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vegetable_id` int NOT NULL,
  `ridge_id` int NOT NULL,
  `quantity` int NOT NULL,
  `rows_count` int NOT NULL,
  `x` int NOT NULL,
  `y` int NOT NULL,
  `width` int NOT NULL,
  `height` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_pa_ridge` FOREIGN KEY (`ridge_id`) REFERENCES `ridges` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pa_vegetable` FOREIGN KEY (`vegetable_id`) REFERENCES `vegetables` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;