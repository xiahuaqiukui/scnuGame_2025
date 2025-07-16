package com.tedu.show;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @说明 游戏的主要面板
 * @author renjj
 * @功能说明 主要进行元素的显示，同时进行界面的刷新(多线程)
 *
 * @题外话 java开发实现思考的应该是：做继承或者是接口实现
 *
 * @多线程刷新 1.本类实现线程接口
 * 	          2.本类定义一个内部类来实现
 */


public class GameMainJPanel extends JPanel implements Runnable{
	private ElementManager em = null;

	public GameMainJPanel() {
		init();
	}

	public void init() {
		em = ElementManager.getManager();
	}

	// 绘制所有元素
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Map<GameElement, List<ElementObj>> all = em.getGameElements();
//		GameElement.values();//隐藏方法，返回是一个数组
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge);
			for (int i = 0; i < list.size(); i++) {
				ElementObj obj = list.get(i);
				obj.showElement(g);//调用每个类的自己的show方法完成自己的显示
			}
		}

		drawScore(g);
//		Set<GameElement> set = all.keySet(); //得到所有的key集合
//		for(GameElement ge:set) { //迭代器
//			List<ElementObj> list = all.get(ge);
//			for(int i=0;i<list.size();i++) {
//				ElementObj obj=list.get(i);
//				obj.showElement(g);//调用每个类的自己的show方法完成自己的显示
//			}
//		}
	}

	@Override
	public void run() {
		while(true) {
			this.repaint();
			// 一般情况下，多线程都会使用一个休眠，控制速度
			try{
				Thread.sleep(10); // 休眠10毫秒 1s刷新100次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private void drawScore(Graphics g) {
		// 设置字体和颜色
		g.setColor(Color.black); // 文字颜色
		g.setFont(new Font("Arial", Font.BOLD, 24)); // 字体样式

		// 在屏幕右上角显示分数
		String scoreText = "Score: " + em.getScore();
		int x = getWidth()/2 - 80; // 距离右侧 150 像素
		int y = 30; // 距离顶部 30 像素

		g.drawString(scoreText, x, y);
	}
}
