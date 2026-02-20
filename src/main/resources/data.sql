-- 既存データを削除して初期化（外部キー制約を考慮した順番）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `plant_areas`;
TRUNCATE TABLE `ridges`;
TRUNCATE TABLE `farmlands`;
TRUNCATE TABLE `vegetables`;
SET FOREIGN_KEY_CHECKS = 1;
-- 野菜データの投入
INSERT INTO `vegetables` (
    `id`,
    `name`,
    `family`,
    `is_tall`,
    `unit_width`,
    `unit_height`
  )
VALUES (1, 'サツマイモ', 'ヒルガオ科', 0, 30, 30),
  (2, 'トウモロコシ', 'イネ科', 1, 30, 30),
  (3, 'ミニトマト', 'ナス科', 1, 40, 40),
  (4, 'オクラ', 'アオイ科', 1, 30, 30),
  (5, 'ピーマン', 'ナス科', 0, 40, 40),
  (6, 'キュウリ', 'ウリ科', 1, 60, 60),
  (7, 'ナス', 'ナス科', 0, 60, 60),
  (8, '枝豆', 'マメ科', 0, 30, 30),
  (9, 'ジャガイモ', 'ナス科', 0, 30, 30),
  (10, 'カボチャ', 'ウリ科', 0, 120, 120);