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
	
	// 双击检测间隔, 参数
	private int lastKey = -1;
	private long lastKeyPressTime = 0; // 最后一次按键的时间
	private final int DOUBLE_CLICK_HIGH_THRESHOLD = 500; // 500ms
	private final int DOUBLE_CLICK_LOW_THRESHOLD = 100; // 100ms
	
	/**
	 * 通过set来记录所有按下的键
	 * 第一次按下，加入到set1中
	 * 第二次按下，与前一次按下键相同，若在时间阈值内，加入到set2中
	 * 松开从set1和set2删除
	 */
	private Set<Integer> set1 = new HashSet<Integer>();
	private Set<Integer> set2 = new HashSet<Integer>();
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		测试按键key值
//		System.out.println("keyPressed"+e.getKeyCode());
		
		int key = e.getKeyCode(); // 获取此时键盘值
		long currentTime = System.currentTimeMillis(); // 获取此时时间值
		
		// 获取玩家集合,可以得到每个玩家的按键
		List<ElementObj> player = em.getElementsByKey(GameElement.PLAYER);
		
		if (this.set2.contains(key)) {
			for (ElementObj obj:player) {
				obj.keyClick(2, key);
			}
			return ;
		}
		
		if (this.set1.contains(key)) {
			if (currentTime - lastKeyPressTime <= DOUBLE_CLICK_HIGH_THRESHOLD
			 && currentTime - lastKeyPressTime >= DOUBLE_CLICK_LOW_THRESHOLD
			 && lastKey == key)
			{
		        set2.add(key);
		        for (ElementObj obj:player) {
					obj.keyClick(2, key);
				}
		        return ;
		    } else {
		    	for (ElementObj obj:player) {
					obj.keyClick(1, key);
				}
		    	lastKey = key;
		    	lastKeyPressTime = currentTime;
		    	return ;
		    }
		}
		
		// 既不在set1也不在set2
		set1.add(key);
		lastKey = key;
        lastKeyPressTime = currentTime;
		for (ElementObj obj:player) {
			obj.keyClick(1, key);
		}
	}

	// 松开
	@Override
	public void keyReleased(KeyEvent e) {
//		System.out.println("keyReleased"+e.getKeyCode());
		int key = e.getKeyCode();
		if (this.set2.contains(key)) {
			set2.remove(key); // 移除数据
		}
		if (this.set1.contains(key)) {
			set1.remove(key); // 移除数据
		}
		
		// 获取玩家集合
		List<ElementObj> player = em.getElementsByKey(GameElement.PLAYER);
		for (ElementObj obj:player) {
			obj.keyClick(0, key);
		}
	}
	
}
