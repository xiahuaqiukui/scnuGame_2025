package com.tedu.game;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

public class GameStart {
	/**
	 * 程序的唯一入口
	 */
	public static void main(String[] args) {
		// 实例化框架
		GameJFrame gj = new GameJFrame();
		// 实例化面板
		GameMainJPanel jp = new GameMainJPanel();
		// 实例化键盘监听
		GameListener listenser = new GameListener();
		// 实例化主线程
		GameThread mainThread = new GameThread();
		
		
		// 嵌入框架（注入）
		gj.setjPanel(jp);
		gj.setKeyListener(listenser);
		gj.setMainThread(mainThread);
		
		// 启动窗口（框架）
		gj.start();
		
	}

}
