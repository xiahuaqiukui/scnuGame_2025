package com.tedu.game;

import javax.swing.SwingUtilities;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;
import com.tedu.show.GameStartMenu;

public class GameStart {
	/**
	 * 程序的唯一入口
	 */
	public static void main(String[] args) {
//		实例化框架
		GameStartMenu gsm = new GameStartMenu();
		
		SwingUtilities.invokeLater(() -> {
            GameStartMenu menu = new GameStartMenu();
            menu.setVisible(true);
        });
	}

}
