package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;
import java.text.Normalizer.Form;

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
	private int attack; // 攻击力
	private int speed; // 速度
	private String fx;
	private String from; // 子弹是由谁发出的
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
			ImageIcon icon2 = null;
			if (fx.equals("right"))
				icon2 = GameLoad.imgMaps.get("player1_right_bullet1").get(0);
			else
				icon2 = GameLoad.imgMaps.get("player1_left_bullet1").get(0);
			this.setIcon(icon2);
		}
		
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
	public void showElement(Graphics g) {
//		g.setColor(Color.red);
//		g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}
	
	@Override
	protected void move(long gameTime) {
		//System.out.println(this.fx);
		switch (this.fx) {
			case "left": this.setX(this.getX()-this.speed); break;
			case "right": this.setX(this.getX()+this.speed); break;
		}
	}
	
	public int getAttack() {
		// 暂时只返回攻击数值
		return attack;
	}
	
	public void fitImage() {
		// 为了使得子弹从角色手中发射出去，根据图片方位调整
		// 角色1
		if (from.equals("player1")) {
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
		}
		
		return;
	}
	
	public String getFrom() {
		return this.from;
	}
}
