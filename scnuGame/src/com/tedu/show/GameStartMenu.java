package com.tedu.show;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

public class GameStartMenu extends JFrame{
	// 窗口属性
	public static int GameX = 1700; // 横向长度
	public static int GameY = 920;  // 纵向长度
	private JPanel jPanel = null;   // 正在显示的面板
	private KeyListener keyListener = null;  // 键盘监听器
	private MouseMotionListener mouseMotionListener = null;  //鼠标监听
	private MouseListener mouseListener = null;
	private Thread mainThread = null;  // 游戏主线程
	
	private Image backgroundImage = null;
	
	// 按钮
    private JButton singlePlayerBtn; // 单人游戏按钮
    private JButton twoPlayerBtn; // 双人游戏按钮
    private JButton exitBtn; // 退出按钮

    public GameStartMenu() {
        // 设置窗口属性
        setTitle("像素乱斗");
        setSize(GameX, GameY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 退出即结束程序运行
        setLocationRelativeTo(null); // 居中显示

        // 分层面板 内容与背景
        // 主面板
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(GameX, GameY));
        
        // 加载背景图片
        try {
            // 从文件加载图片（替换为你的图片路径）
            backgroundImage = Toolkit.getDefaultToolkit().getImage("image/background/1.jpg");
//          System.out.println(backgroundImage);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果加载失败，设置默认颜色为黑色
            setBackground(Color.BLACK);
        }
        
        // 背景面板
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制背景
                if (backgroundImage != null) {
                	g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setBounds(0, 0, GameX, GameY);
        
        // 将背景面板加入主面板
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // 创建内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false); // 透明背景
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBounds(GameX/2-300, GameY/2-200, 600, 400);
        
        // 将内容面板加入主面板
        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        
        // 添加游戏标题
        JLabel titleLabel = new JLabel("像素乱斗");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 64));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); // 控制与下方的距离

        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 30));
        buttonPanel.setMaximumSize(new Dimension(300, 250));

        // 单人游戏按钮
        singlePlayerBtn = addButton("单人模式");
        singlePlayerBtn.addActionListener(e -> startGame(1));

        // 双人游戏按钮
        twoPlayerBtn = addButton("双人对战");
        twoPlayerBtn.addActionListener(e -> startGame(2));

        // 退出按钮
        exitBtn = addButton("退出游戏");
        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(singlePlayerBtn);
        buttonPanel.add(twoPlayerBtn);
        buttonPanel.add(exitBtn);

        // 添加所有组件到内容面板
        contentPanel.add(titleLabel);
        contentPanel.add(buttonPanel);
        
        
        // 界面注入主面板
        this.add(layeredPane);
    }
	
    private JButton addButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 28));
        button.setBackground(new Color(70, 130, 180, 200));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 60));

        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 220, 220));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180, 200));
            }
        });

        return button;
    }
    
    private void startGame(int playerCount) {
        // 关闭菜单窗口，启动主游戏窗口
        SwingUtilities.invokeLater(() -> {
            this.dispose(); // 关闭菜单界面
            // 启动主游戏窗口
            GameJFrame gj = new GameJFrame();
            GameMainJPanel jp = new GameMainJPanel();
            GameListener listener = new GameListener();
            GameThread th = new GameThread(playerCount);
            
            // 注入
            gj.setjPanel(jp);
            gj.setKeyListener(listener);
            gj.setMainThread(th);
            
            // 可在此处将playerCount传递给后续逻辑
            gj.start();
        });
    }
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameStartMenu menu = new GameStartMenu();
            menu.setVisible(true);
        });
    }
}
