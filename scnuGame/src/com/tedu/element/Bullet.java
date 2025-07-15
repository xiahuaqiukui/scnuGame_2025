package com.tedu.element;

import java.awt.*;
import java.text.Normalizer.Form;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import com.tedu.manager.GameLoad;

/**
 * @说明 玩家子弹类，由玩家对象调用创建
 */

/**
 * @子类构建方法 1.重写show方法
 * 				2.选择性重写其他方法
 * 				3.思考并定义子类特有的属性
 */

public class Bullet extends ElementObj{

	private int pictureIndex=0;
	private long pictureTime=0L;

	private int attack; // 攻击力
	private int speed; // 速度
	private String fx;
	private String from; // 子弹是由谁发出的
	private String key=null;

	private int startX=0;
	private int startY=0;
	private int endX=0;
	private int endY=0;
	// 拓展其他属性  可拓展多种子弹类型：激光、导弹等（玩家类要有子弹类型）
	
	public Bullet() {}
	
	
	public Bullet(int x, int y, int w, int h, ImageIcon icon, int attack, int speed, String fx,
			String from) {
		super(x, y, w, h, icon);
		this.attack = attack;
		this.speed = speed;
		this.fx = fx;
		this.from = from;
		
		if (from.equals("player1")) {
			this.setIcon(GameLoad.imgMaps.get("player1_bullet1").get(0));
		} else if (from.equals("player2")) {
			this.setIcon(GameLoad.imgMaps.get("player2_bullet2").get(0));
		}
		
	}
	public Bullet(int x, int y, int w, int h, ImageIcon icon, int attack, int speed, String fx,
				  String from,String key) {
		super(x, y, w, h, icon);
		this.attack = attack;
		this.speed = speed;
		this.fx = fx;
		this.from = from;
		this.key = key;
		this.setIcon(GameLoad.imgMaps.get(key).get(0));
	}
	public Bullet(int x, int y, int w, int h, int attack, int speed,int endX,int endY,
				  String from) {
		super(x, y, w, h, null);
		this.attack = attack;
		this.speed = speed;
		this.from = from;
		this.startX=x;
		this.startY=y;
		this.endX=endX;
		this.endY=endY;
	}
	
	// 传输参数，返回对应参数的对象
//	@Override
//	public ElementObj createElement(String str) { // 定义字符串的规则
//		String[] split = str.split(",");
//		for (String str1: split) {
//			String[] split2 = str1.split(":");
//			switch (split2[0]) {
//			case "x": this.setX(Integer.parseInt(split2[1])); break;
//			case "y": this.setY(Integer.parseInt(split2[1])); break;
//			case "fx": this.fx = split2[1]; break;
//			default: break;
//			}
//		}
//		
//		return this;
//	}


	@Override
	//只需要一张图片的绘画
	public void showElement(Graphics g) {
		if(icon==null){
			if(from.equals("boss")){
				Random random = new Random();
				g.setColor(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
				g.drawOval(this.getX(), this.getY(), this.getW(), this.getH());
				return;
			}
		}
		Image img = this.getIcon().getImage();
		int drawX = this.getX();
		int drawY = this.getY();
		int drawWidth = this.getW();
		int drawHeight = this.getH();

		if ("left".equals(fx)) {
			// 朝向左边时，固定右下角坐标 (drawX + drawWidth, drawY + drawHeight)
			g.drawImage(img,
					drawX + drawWidth, drawY,  // 绘制起点在右下角
					-drawWidth, drawHeight,   // 负宽度实现水平翻转
					null);
		} else {
			// 朝向右边时，正常绘制
			g.drawImage(img,
					drawX, drawY,
					drawWidth, drawHeight,
					null);
		}
	}

	@Override
	protected void updateImage(long gameTime, int sleepTime) {
		if(gameTime-this.pictureTime>=7)
		if(key!=null){
			List<ImageIcon> imageIcons = GameLoad.imgMaps.get(key);
			pictureIndex%=imageIcons.size();
			setIcon(imageIcons,pictureIndex);
			pictureTime=gameTime;
			pictureIndex++;
		}
	}

	@Override
	protected void move(long gameTime) {
		//System.out.println(this.fx);
		if(fx==null){
			if(from.equals("boss")){
				double angle=Math.atan2(endY - startY, endX - startX);
				// 计算方向向量（单位向量）
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);

				// 按速度移动
				this.setX(this.getX() + (int) (this.speed * dx));
				this.setY(this.getY() + (int) (this.speed * dy));
			}
			return;
		}
		switch (this.fx) {
			case "left": this.setX(this.getX()-this.speed); break;
			case "right": this.setX(this.getX()+this.speed); break;
		}


	}
	
	public int getAttack() {
		// 不同类型敌人发射的子弹有不同倍率
		int at = this.attack;
		if (from.equals("player1")) {
			at = 2 * this.attack;
		} else if (from.equals("player2")) {
			at = 3 * this.attack;
		}
		
		return at;
	}
	
	public void fitImage() {
		// 为了使得子弹从角色手中发射出去，根据图片方位调整
		// 角色1 角色二
		if (from.equals("player1") || from.equals("player2")) {
			switch (this.fx) {
				case "left": this.setY(this.getY()+50); break;
				case "right": 
					this.setX(this.getX()+100); 
					this.setY(this.getY()+50); 
					break;
			}
			
			// 设置子弹长宽
			this.setW(60);
			this.setH(20);
		} else if (from.equals("enemy")) {
			switch (this.fx) {
				case "left": this.setX(this.getX()+50); break;
				case "right":
					this.setX(this.getX()+100);
					break;
			}
			this.setY(this.getY()+13);

			// 设置子弹长宽
			this.setW(icon.getIconHeight()*2);
			this.setH(icon.getIconHeight()*2);
		}else if (from.equals("boss")) {
			switch (this.fx) {
				case "left": this.setX(this.getX()+50); break;
				case "right":
					this.setX(this.getX()+100);
					break;
			}
			this.setY(this.getY()+5);

			// 设置子弹长宽
			this.setW(icon.getIconHeight()*2);
			this.setH(icon.getIconHeight()*2);
		}

		return;
	}
	
	public String getFrom() {
		return this.from;
	}

	protected void setIcon(List<ImageIcon> imageIcons, int index) {
		ImageIcon t = imageIcons.get(index);
		this.setIcon(t);

		int newWidth = t.getIconWidth() * 2;
		int newHeight = t.getIconHeight() * 2;

		// 记录旧的右下角坐标
		int oldRightX = this.getX() + this.getW();
		int oldBottomY = this.getY() + this.getH();

		// 更新宽高
		this.setW(newWidth);
		this.setH(newHeight);

		// 调整坐标，使右下角不变
		if ("left".equals(fx)) {
			this.setX(oldRightX - newWidth);  // 新X = 旧右下角X - 新宽度
		}
		this.setY(oldBottomY - newHeight);    // 新Y = 旧底部Y - 新高度

		// 更新碰撞箱
	}
}
