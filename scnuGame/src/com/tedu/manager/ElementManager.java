package com.tedu.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tedu.element.ElementObj;

import javax.swing.*;

/**
 * @说明 本类是元素管理器，专门存储所有的元素，同时，提供方法
 * 		给予视图和控制获取数据
 * @author renjj
 * @问题一：存储所有元素数据，怎么存放？ list map set 3大集合
 * @问题二：管理器是视图和控制要访问，管理器就必须只有一个，单例模式
 */

public class ElementManager {
	// private List<Object> mapList;
	// private List<Object> playerList;
	// private List<Object> enemyList;
	
	// String 作为 Key 匹配所有的元素，player -> List<Object> playerList
	//								 enemy --> List<Object> enemyList
	// 枚举类型作为Map的Key，用于区分不一样的资源，用于获取资源
	// List中元素的泛型应该是元素的基类
	// 所有元素都可以存放到map集合中，显示模块只需要获得这个map就可以
	// 显示是有的界面需要显示的函数（调用元素基类的方法 showElement()）

	/// 得分
	private int score=0;
	private JPanel gamePanel;

	// 用Map配合Enum和List结构化存储元素
	private Map<GameElement, List<ElementObj>> gameElements;
	
	// 获取所有元素
	public Map<GameElement, List<ElementObj>> getGameElements() {
		return gameElements;
	}
	
	// 添加元素(多半由加载器调用)
	public void addElement(ElementObj obj,GameElement ge) {
		gameElements.get(ge).add(obj);//添加对象到集合中，按key值就行存储
	}
	
	// 取出某一类元素，依据key返回list集合
	public List<ElementObj> getElementsByKey(GameElement ge){
		return gameElements.get(ge);
	}
	
	/**
	 * 单例模式：内存中有且只有一个实例。
	 * 饿汉模式-是启动就自动加载实例
	 * 饱汉模式-是需要使用的时候才加载实例
	 * 
	 * 编写方式：
	 * 1.需要一个静态的属性(定义一个常量) 单例的引用
	 * 2.提供一个静态的方法(返回这个实例) return单例的引用
	 * 3.一般为防止其他人自己使用(类是可以实例化),所以会私有化构造方法
	 *    ElementManager em=new ElementManager();
	 */
	
	// 初始化元素管理器
	private static ElementManager EM = null; // 引用
	// synchronized线程锁->保证本方法执行中只有一个线程
	// 返回元素管理器给其他部分，使其可以取用元素
	public static synchronized ElementManager getManager() { // 加锁
		if (EM == null) { // 空值判定
			EM = new ElementManager();
		}
		return EM;
	}

	
	// 构造函数+初始化
	private ElementManager() { // 构造方法私有化
		init();
	}
	
//	static {// 饿汉实例化对象  静态语句块是在类被加载的时候直接执行
//		EM = new ElementManager(); // 只会执行一次
//	}
	
	// 初始化
	public void init() {
		// hashMap hash散列
		gameElements = new HashMap<GameElement,List<ElementObj>>();
		
		// 将每种元素集合都放入到Map中
//		gameElements.put(GameElement.PLAYER, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.MAPS, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.ENEMY, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.BOSS, new ArrayList<ElementObj>());

		for (GameElement ge : GameElement.values()) {
			gameElements.put(ge,new ArrayList<>());
		}
		// 其他各种实体+效果......
	}

	public static void clearAllElements() {
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = EM.getGameElements().get(ge);
			if (list != null) {
				list.clear();
			}
		}

	}


	public void addScore(int score) {
		this.score += score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}


	public void setGamePanel(JPanel panel) {
		this.gamePanel = panel;
	}

	public JPanel getGamePanel() {
		return gamePanel;
	}

}
