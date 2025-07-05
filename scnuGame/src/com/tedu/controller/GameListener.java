package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @说明 监听类，用于监听用户的操作 KeyListener
 * @author renjj
 *
 */

public class GameListener implements KeyListener{
	// 获取游戏资源管理器
	ElementManager em = ElementManager.getManager();
	
	/*能否通过一个集合来记录所有按下的键，如果重复触发，就直接结束
	 * 同时，第1次按下，记录到集合中，第2次判定集合中否有。
	 *       松开就直接删除集合中的记录。
	 * set集合
	 * */
	private Set<Integer> set = new HashSet<Integer>();
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	// 按下
	// 左37 上38 右39 下40
	// a65  w87  d68  s83
	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println("keyPressed"+e.getKeyCode());
		int key = e.getKeyCode();
		if (this.set.contains(key)) {
			return ;
		}
		set.add(key);
		
		// 获取玩家集合,可以得到每个玩家的按键
		List<ElementObj> player = em.getElementsByKey(GameElement.PLAYER);
		for (ElementObj obj:player) {
			obj.keyClick(true, key);
		}
	}

	// 松开
	@Override
	public void keyReleased(KeyEvent e) {
//		System.out.println("keyReleased"+e.getKeyCode());
		int key = e.getKeyCode();
		if (!this.set.contains(key)) {
			return ;
		}
		set.remove(key); // 移除数据
		
		// 获取玩家集合
		List<ElementObj> player = em.getElementsByKey(GameElement.PLAYER);
		for (ElementObj obj:player) {
			obj.keyClick(false, key);
		}
	}
	
}
