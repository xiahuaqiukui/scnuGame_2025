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
	// 人物图像map
	public static Map<String, ImageIcon> imgMap;
	
	static {
		// 加载人物图像
		imgMap = new HashMap<String, ImageIcon>();
		imgMap.put("left", new ImageIcon("image/1 Woodcutter/Woodcutter_left.png"));
		imgMap.put("right", new ImageIcon("image/1 Woodcutter/Woodcutter_right.png"));
	}
}
