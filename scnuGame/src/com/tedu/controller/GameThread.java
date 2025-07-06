package com.tedu.controller;

import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.element.ElementObj;
import com.tedu.element.Player;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/**
 * @说明 游戏的主线程，用于控制游戏加载，游戏关卡，游戏运行时自动化
 * 		游戏判定；游戏地图切换 资源释放和重新读取。。。
 * @author renjj
 * @继承 使用继承的方式实现多线程(一般建议使用接口实现)
 */

public class GameThread extends Thread{
	private ElementManager em;
	public GameThread() {
		em = ElementManager.getManager();
	}
	
	@Override
	public void run() {
		while (true) {
		// 游戏开始前（加载资源）
			gameLoad();
		// 游戏进行中
			gameRun();
		// 游戏场景结束（资源回收）
			gameOver();
			
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 游戏的加载
	 */
	private void gameLoad() {

		GameLoad.MapLoad(1);
		load();

	}
	
	/**
	 * @说明  游戏进行时
	 * @任务说明  游戏过程中需要做的事情：1.自动化玩家的移动，碰撞，死亡
	 *                                 2.新元素的增加(NPC死亡后出现道具)
	 *                                 3.暂停等等。。。。。
	 * 先实现主角的移动
	 * */
	private void gameRun() {
		long gameTime=0L;
		while (true) { // 预留拓展 true可以作为变量 用于控制关卡结束等
			Map<GameElement, List<ElementObj>> all = em.getGameElements();
//			GameElement.values(); // 隐藏方法 返回值是一个数组，数组顺序是定义枚举的顺序
			for (GameElement ge:GameElement.values()) {
				List<ElementObj> list = all.get(ge);
				for(int i=0;i<list.size();i++) {
					ElementObj obj=list.get(i);
					obj.model();//调用每个类的自己的show方法完成自己的显示
				}
			}


			gameTime++;
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void moveAndUpdate(Map<GameElement, List<ElementObj>> all,long gameTime){

	}


	/**游戏切换关卡*/
	private void gameOver() {
		// ......
		
	}
	
	public void load() {
//		图片导入
		ImageIcon icon = new ImageIcon("image/1 Woodcutter/Woodcutter_right.png");
		ElementObj obj = new Player(100,600,100,100,icon);//实例化对象
//		将对象放入到 元素管理器中
//		em.getElementsByKey(GameElement.PLAY).add(obj);
		em.addElement(obj,GameElement.PLAYER);//直接添加
		
		
		// 添加一个敌人类，仿照玩家类编写，注意：不需要实现键盘监听
		// 实现敌人的显示，同时实现最简单的移动，例如:从坐标100，100移动到500，100然后调头
		// 子弹发射,子弹移动,元素死亡
		// 只实现了子弹的发射和死亡，思考道具掉落是否与之相近？
		
		
	}
}
