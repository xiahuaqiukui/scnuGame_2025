package com.tedu.manager;

import com.tedu.element.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

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
	//单张图片测试，后面删除
//	public static Map<String, ImageIcon> imgMap=new HashMap<>();

	public static Map<String, List<ImageIcon>> imgMaps=new HashMap<>();

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
			pro.clear();
			pro.load(maps);
			Enumeration<?> names=pro.propertyNames();
			while(names.hasMoreElements()){
				String key= names.nextElement().toString();
				System.out.println("KEY:"+key);
				String []arrs=pro.getProperty(key).split(";");
				if(key.equals("ENEMY")){
					for(int i=0;i<arrs.length;i++){
						ElementObj element = new Enemy().createElement(key+","+arrs[i]);
						System.out.println(element);
						em.addElement(element,GameElement.ENEMY);
					}
				}else{
					for(int i=0;i<arrs.length;i++){
						ElementObj element = new MapObj().createElement(key+","+arrs[i]);
						System.out.println(element);
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
				String url=pro.getProperty(key.toString());
				System.out.println("key:"+key+",url:"+url);
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
				//System.out.println(GameLoad.imgMaps.keySet());
				//System.out.println(GameLoad.imgMaps.get("player1_right_walk"));
			}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	public static void playerLoad(){
		String playerStr="100,700";
		ElementObj player = new Player1().createElement(playerStr);
		ElementObj topCollider=new Collider(player.getX(), player.getY()-5,player.getW(),5);
		ElementObj bottomCollider=new Collider(player.getX(), player.getY()+100,player.getW(),5);
		ElementObj leftCollider=new Collider(player.getX()-5, player.getY(),5,player.getH()-20);
		ElementObj rightCollider=new Collider(player.getX()+100, player.getY(),5,player.getH()-20);
		((Player1)player).setTopCollider((Collider) topCollider);
		((Player1)player).setBottomCollider((Collider) bottomCollider);
		((Player1)player).setLeftCollider((Collider) leftCollider);
		((Player1)player).setRightCollider((Collider) rightCollider);
		em.addElement(player,GameElement.PLAYER);
		em.addElement(topCollider,GameElement.COLLIDER);
		em.addElement(bottomCollider,GameElement.COLLIDER);
		em.addElement(leftCollider,GameElement.COLLIDER);
		em.addElement(rightCollider,GameElement.COLLIDER);
	}


}
