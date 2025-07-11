package com.tedu.controller;

import java.util.List;
import java.util.Map;

import com.tedu.element.Collider;
import com.tedu.element.ElementObj;
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
	// 获取资源管理器
	private ElementManager em;
	// 设置刷新时间
	public static int sleepTime=15; // 15ms一刷
	
	// 初始构造函数
	public GameThread() {
		em = ElementManager.getManager();
	}
	
	// 重写run函数, 执行的进程
	@Override
	public void run() {
		while (true) {
			// 加载资源
			gameLoad();
			// 游戏进行
			gameRun();
			// 游戏场景结束（资源回收/跳转）
			gameOver();
			
			// 游戏结束的刷新时间
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 游戏的加载
	 */
	private void gameLoad() {
		GameLoad.ImgLoad(); // 加载图片
		GameLoad.MapLoad(1); // 加载地图
		GameLoad.playerLoad(); // 可以带参数表示几个人(目前仅有1人)
	}
	
	/**
	 * @说明 游戏进行时
	 * @任务说明 游戏过程中需要做的事情：1.自动化玩家的移动，碰撞，死亡
	 *                                2.新元素的增加(NPC死亡后出现道具)
	 *                                3.暂停等等。。。。。
	 * 先实现主角的移动
	 * */
	private void gameRun() {
		long gameTime = 0L;
		
		// 预留拓展 true可以作为变量 用于控制关卡结束等
		while (true) {
			// 加载所有变量 部分类
			Map<GameElement, List<ElementObj>> all = em.getGameElements();
			List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
			List<ElementObj> bullets = em.getElementsByKey(GameElement.BULLET);
			List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
			List<ElementObj> collider = em.getElementsByKey(GameElement.COLLIDER);
			
			// 更新
			Update(all, gameTime);
			
			// 碰撞检测
			ElementPK(enemys, bullets);
			ColliderCollided(collider, maps);
			
			
			gameTime++;
			
			// 游戏进程的刷新时间
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**游戏切换关卡*/
	private void gameOver() {
		// ......
		
	}
	
//	public void load() {
////		图片导入
//		ImageIcon icon = new ImageIcon("image/1 Woodcutter/Woodcutter_right.png");
//		ElementObj obj = new Player(100,600,100,100,icon);//实例化对象
////		将对象放入到 元素管理器中
////		em.getElementsByKey(GameElement.PLAY).add(obj);
//		em.addElement(obj,GameElement.PLAYER);//直接添加
//
//
//		// 添加一个敌人类，仿照玩家类编写，注意：不需要实现键盘监听
//		// 实现敌人的显示，同时实现最简单的移动，例如:从坐标100，100移动到500，100然后调头
//		// 子弹发射,子弹移动,元素死亡
//		// 只实现了子弹的发射和死亡，思考道具掉落是否与之相近？
//
//
//	}
	
	public void Update(Map<GameElement, List<ElementObj>> all, long gameTime) {
//		GameElement.values();//隐藏方法，返回是一个数组
		for (GameElement ge : GameElement.values()) {
			// 分别取出所有枚举类
			List<ElementObj> list = all.get(ge);
			for (int i = 0; i < list.size(); i++) {
				// 获取枚举类里的对象
				ElementObj obj = list.get(i);
				
				// 若对象死亡则移除(die方法可拓展死亡掉落物品)
				if(!obj.isLive()){
					obj.die();
					list.remove(i--);
					continue;
				}
				
				// 移动+换装+攻击 三个操作
				obj.model(gameTime, sleepTime);
			}
		}
	}
	
	// 碰撞检测(非碰撞箱)
	public void ElementPK(List<ElementObj> listA, List<ElementObj> listB) {
		for(int i=0;i<listA.size();i++){
			ElementObj enemy=listA.get(i);
			for(int j=0;j<listB.size();j++){
				ElementObj file=listB.get(j);
				if(enemy.pk(file)){
					enemy.setLive(false);
					file.setLive(false);
					break;
				}
			}
		}
	}
	
	// 碰撞检测(碰撞箱)
	public void ColliderCollided(List<ElementObj> listA, List<ElementObj> listB) {
		for(int i=0;i<listA.size();i++){
			ElementObj collider=listA.get(i);
			if(collider instanceof Collider){
				Collider A=(Collider) collider;
				boolean collided = false;
				for(int j=0;j<listB.size();j++){
					ElementObj B=listB.get(j);
					if(A.pk(B)){
						collided=true;
					}
				}
				A.setCollided(collided);
				//System.out.println(A+" collided "+collided);
			}
		}
	}


}
