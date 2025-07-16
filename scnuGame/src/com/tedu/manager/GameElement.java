package com.tedu.manager;

// 用于作为管理器管理元素的索引

public enum GameElement {
	/**
	 * PLAY 玩家
	 * MAPS 地图
	 * ENEMY 敌人
	 * BOSS  boss
	 * 子弹。。。。。。
	 */
	// 前面的会背后面的覆盖
	MAPS, ENEMY, BOSS, BULLET, DIE, PLAYER, MEDICINE,
	BAR, FLOATINGTEXT, COLLIDER, ATTACKCOLLIDER;
//	我们定义的枚举类型，在编译的时候，虚拟机会自动帮助生成class文件，并且会
//	加载很多的代码和方法
//	private GameElement() {
//		
//	}
//	private GameElement(int id) {
//		
//	}
	
}
