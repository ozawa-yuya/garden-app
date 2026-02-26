-- 各テーブルを古い順（子から親）に削除してリセットする
DROP TABLE IF EXISTS plant_areas;
DROP TABLE IF EXISTS ridges;
DROP TABLE IF EXISTS farmlands;
DROP TABLE IF EXISTS vegetables;

-- 1. 野菜マスター（基本情報の定義）
CREATE TABLE IF NOT EXISTS vegetables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    family VARCHAR(50) DEFAULT NULL,
    height INT NOT NULL DEFAULT 60,
    unit_width INT NOT NULL DEFAULT 30,
    unit_height INT NOT NULL DEFAULT 30,
    UNIQUE (name)
);

-- 2. 農地データ（計算の土台となる計画の親データ）
CREATE TABLE IF NOT EXISTS farmlands (
    id INT AUTO_INCREMENT PRIMARY KEY,
    width INT NOT NULL,
    height INT NOT NULL,
    gap INT NOT NULL,
    offset INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. 畝データ（農地に紐づく子データ）
CREATE TABLE IF NOT EXISTS ridges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    farmland_id INT NOT NULL,
    name VARCHAR(100) DEFAULT NULL,
    width INT NOT NULL,
    height INT NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    CONSTRAINT fk_ridge_farmland FOREIGN KEY (farmland_id) REFERENCES farmlands (id) ON DELETE CASCADE
);

-- 4. 配置エリア（野菜と畝に紐づく孫データ）
CREATE TABLE IF NOT EXISTS plant_areas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vegetable_id INT NOT NULL,
    ridge_id INT NOT NULL,
    quantity INT NOT NULL,
    rows_count INT NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    width INT NOT NULL,
    height INT NOT NULL,
    CONSTRAINT fk_pa_ridge FOREIGN KEY (ridge_id) REFERENCES ridges (id) ON DELETE CASCADE,
    CONSTRAINT fk_pa_vegetable FOREIGN KEY (vegetable_id) REFERENCES vegetables (id)
);
