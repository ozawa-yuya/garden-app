package com.example.gardenapp.service;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.example.gardenapp.model.*;

public class GardenViewer extends JFrame{
    private List<Ridge> ridges;
    private final int SCALE = 1; // 1cm = 1px（画面に合わせて調整）

    public GardenViewer(List<Ridge> ridges) {
        this.ridges = ridges;
        setTitle("菜園レイアウトビューア");
        setSize(1200, 800); // ウィンドウサイズ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

//        描画パネルの追加
        add(new GardenPanel());
    }

    private class GardenPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

//            アンチエイリアス（線を綺麗にする）
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Ridge ridge : ridges) {
//                畝の描画 (グレーの枠線)
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRect(ridge.getX() * SCALE, ridge.getY() * SCALE,
                             ridge.getWidth() * SCALE, ridge.getHeight() * SCALE);
                g2d.drawString(ridge.getName(), ridge.getX() * SCALE, (ridge.getY() * SCALE) - 5);

//                作付エリアの描画
                for (PlantArea area : ridge.getPlantAreaList()) {
                    // 野菜の種類ごとに色を変える（簡易版）
                    g2d.setColor(getColorByFamily(area.getVegetable().getFamily()));
                    g2d.fillRect(area.getX() * SCALE, area.getY() * SCALE,
                                 area.getWidth() * SCALE, area.getHeight() * SCALE);

//                    枠線
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(area.getX() * SCALE, area.getY() * SCALE,
                                 area.getWidth() * SCALE, area.getHeight() * SCALE);

//                    野菜名の表示（小さいので拡大時のみ推奨）
                    g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    g2d.drawString(area.getVegetable().getName(),
                                   (area.getX() * SCALE) + 5, (area.getY() * SCALE) + 15);
                }
            }
        }
//        科名に基づいて色を割り当てる
        private Color getColorByFamily(String family) {
            switch (family) {
                case "ナス科": return new Color(150, 100, 200, 180); // 紫
                case "ヒルガオ科": return new Color(255, 200, 150, 180); // オレンジ
                case "イネ科": return new Color(255, 255, 150, 180); // 黄色
                case "マメ科": return new Color(150, 255, 150, 180); // 緑
                default: return new Color(200, 255, 200, 180);
            }
        }
    }
}
