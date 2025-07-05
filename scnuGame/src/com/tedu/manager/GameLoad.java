package com.tedu.manager;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * @说明  假的加载器
 * @author renjj
 *
 */

public class GameLoad {
	// 图片集合 使用map来进行存储 枚举类型配合（拓展）
	public static Map<String, ImageIcon> imgMap;
	
	static {
		// 由加载器来加载
		imgMap = new HashMap<String, ImageIcon>();
		imgMap.put("left", new ImageIcon("image/1 Woodcutter/Woodcutter_left.png"));
		imgMap.put("up", new ImageIcon("image/1 Woodcutter/Woodcutter_up.png"));
		imgMap.put("right", new ImageIcon("image/1 Woodcutter/Woodcutter_right.png"));
		imgMap.put("down", new ImageIcon("image/1 Woodcutter/Woodcutter_down.png"));
	}
}
