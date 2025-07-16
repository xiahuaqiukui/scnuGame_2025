package com.tedu.manager;

import com.tedu.element.*;
import com.tedu.element.enemys.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.swing.ImageIcon;

/**
 * @说明  假的加载器
 * @author renjj
 *
 */

public class GameLoad {

	private static ElementManager em = ElementManager.getManager();

	// 图片集合 使用map来进行存储 枚举类型配合（拓展）
	// 人物图像map
	//单张图片测试，后面删除
//	public static Map<String, ImageIcon> imgMap=new HashMap<>();

	public static Map<String, List<ImageIcon>> imgMaps=new HashMap<>();

	private static Properties pro = new Properties();

	//包括地图和敌人
	public static void MapLoad(int mapId){
		String mapName="com/tedu/text/"+mapId+".map";
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream maps=classLoader.getResourceAsStream(mapName);
		if(maps==null){
			System.out.println("配置文件读取异常，请重新安装");
			return;
		}
		try {
			pro.clear();
			pro.load(maps);
			Enumeration<?> names=pro.propertyNames();
			while(names.hasMoreElements()){
				String key= names.nextElement().toString();
				String []arrs=pro.getProperty(key).split(";");
				if(key.equals("SWORDMAN")){
					for(int i=0;i<arrs.length;i++){
						ElementObj element = new Swordman().createElement(key+","+arrs[i]);
						((Enemy)element).setTargetList(em.getElementsByKey(GameElement.PLAYER));
						em.addElement(element,GameElement.ENEMY);
					}
				}else if(key.equals("CENTIPEDE")){
					for(int i=0;i<arrs.length;i++){
						ElementObj element = new Centipede().createElement(key+","+arrs[i]);
						((Enemy)element).setTargetList(em.getElementsByKey(GameElement.PLAYER));
						em.addElement(element,GameElement.ENEMY);
					}
				}else if(key.equals("BIG_BLOATED")){
					for(int i=0;i<arrs.length;i++){
						ElementObj element = new Big_bloated().createElement(key+","+arrs[i]);
						((Enemy)element).setTargetList(em.getElementsByKey(GameElement.PLAYER));
						em.addElement(element,GameElement.ENEMY);
					}
				}else if(key.equals("BATTLE_TURTLE")){
					for(int i=0;i<arrs.length;i++){
						ElementObj element = new Battle_turtle().createElement(key+","+arrs[i]);
						((Enemy)element).setTargetList(em.getElementsByKey(GameElement.PLAYER));
						em.addElement(element,GameElement.ENEMY);
					}
				}else{
					for(int i=0;i<arrs.length;i++){
						ElementObj element = new MapObj().createElement(key+","+arrs[i]);
						em.addElement(element,GameElement.MAPS);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	public static void ImgLoad(){
		String texturl="com/tedu/text/GameData.pro";
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		InputStream texts=classLoader.getResourceAsStream(texturl);
		pro.clear();
        try {
            pro.load(texts);
			Set<Object> set =  pro.keySet();
			for(Object key:set){
//				System.out.println(key.toString());
				String url=pro.getProperty(key.toString());
				List<ImageIcon> imageIcons=new ArrayList<>();
				for(int i=1;;i++){
//					image/1 Woodcutter/walk/right/1.png
					String path=url+"/"+i+".png";
					ImageIcon icon = new ImageIcon(path);
					if(icon.getIconWidth()<=0){
						break;
					}
					imageIcons.add(icon);

				}
				imgMaps.put(key.toString(),imageIcons);

			}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	public static void playerLoad(int playerCount){
		if (playerCount == 1) {
			// 暂为player1后续可能做选择
			String player1Str="100,700";
			ElementObj player1 = new Player1().createElement(player1Str);
			em.addElement(player1,GameElement.PLAYER);
			
			// 暂时加入一个药瓶测试
//			ElementObj medicine = new Medicine(100, 500, 10, 10, null);
//			em.addElement(player1,GameElement.MEDICINE);
		} else if (playerCount == 2) {
			String player1Str="100,700";
			ElementObj player1 = new Player1().createElement(player1Str);
			em.addElement(player1,GameElement.PLAYER);
			
			String player2Str="1500,700";
			ElementObj player2 = new Player2().createElement(player2Str);
			em.addElement(player2,GameElement.PLAYER);
		}
	}

	public static void main(String[] args) {
		for (int i=0;i<2000;i+=20){
			System.out.print(i+",800;");
		}
	}


}
