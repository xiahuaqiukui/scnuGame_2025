package com.tedu.show;

import com.tedu.manager.ElementManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameOverPanel extends JPanel {
    private final JFrame parentFrame;

    public GameOverPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);


        JLabel gameOverLabel = new JLabel("游戏结束，\n得分:"+ElementManager.getManager().getScore(), SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("微软雅黑", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.RED);

        JButton menuBtn = new JButton("返回主菜单");
        styleButton(menuBtn);
        menuBtn.addActionListener(this::handleMenu);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gameOverLabel, gbc);
        gbc.gridy = 1;
        add(menuBtn, gbc);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
    }

    private void handleMenu(ActionEvent e) {
        parentFrame.dispose();
        showMainMenu();
    }

    private void showMainMenu() {
        SwingUtilities.invokeLater(() -> {
            GameStartMenu menu = new GameStartMenu();
            menu.setVisible(true);
        });
    }
}