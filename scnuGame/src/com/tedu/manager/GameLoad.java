package com.tedu.manager;

import com.tedu.element.ElementObj;
import com.tedu.element.MapObj;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

/**
 * @说明  假的加载器
 * @author renjj
 *
 */

public class GameLoad {

	private static ElementManager em=ElementManager.getManager();

	// 图片集合 使用map来进行存储 枚举类型配合（拓展）
	// 人物图像map
	public static Map<String, ImageIcon> imgMap;
	
	static {
		// 加载人物图像
		imgMap = new HashMap<String, ImageIcon>();
		imgMap.put("left", new ImageIcon("image/1 Woodcutter/Woodcutter_left.png"));
		imgMap.put("right", new ImageIcon("image/1 Woodcutter/Woodcutter_right.png"));
	}

	private static Properties pro = new Properties();

	public static void MapLoad(int mapId){
		String mapName="com/tedu/text/"+mapId+".map";
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream maps=classLoader.getResourceAsStream(mapName);
		if(maps==null){
			System.out.println("配置文件读取异常，请重新安装");
			return;
		}
		try {
			pro.load(maps);
			Enumeration<?> names=pro.propertyNames();
			while(names.hasMoreElements()){
				String key= names.nextElement().toString();
				System.out.println(pro.getProperty(key));
				String []arrs=pro.getProperty(key).split(";");
				for(int i=0;i<arrs.length;i++){
					ElementObj element = new MapObj().createElement(key+","+arrs[i]);
					System.out.println(element);
					em.addElement(element,GameElement.MAPS);
				}

			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void loadImg(){

	}

}
