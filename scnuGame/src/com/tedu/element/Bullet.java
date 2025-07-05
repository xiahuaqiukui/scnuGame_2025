package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

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
	private int speed=10; // 速度
	private String fx;
	// 拓展其他属性  可拓展多种子弹类型：激光、导弹等（玩家类要有子弹类型）
	
	public Bullet() {}
	
	private Bullet(int x, int y, int w, int h, ImageIcon icon, String fx) {
		super(x, y, w, h, icon);
		
		this.attack = 1;
		this.speed = 10;
		this.fx = fx;
	}
	
	// 传输参数，返回对应参数的对象
	@Override
	public ElementObj createBullet(String str) { // 定义字符串的规则
		String[] split = str.split(",");
		for (String str1: split) {
			String[] split2 = str1.split(":");
			switch (split2[0]) {
			case "x": this.setX(Integer.parseInt(split2[1])); break;
			case "y": this.setY(Integer.parseInt(split2[1])); break;
			case "fx": this.fx = split2[1]; break;
			}
		}
		this.setW(10);
		this.setH(10);
		
		return this;
	}
	
	@Override
	public void showElement(Graphics g) {
		g.setColor(Color.red);
		int x = this.getX();
		int y = this.getY();
		switch (this.fx) {
		case "left": y+=50; break;
		case "right": x+=100; y+=50; break;
		}
		g.fillOval(x, y, this.getW(), this.getH());
	}
	
	@Override
	protected void move() {
//		System.out.println(this.fx);
		
		switch (this.fx) {
		case "left": this.setX(this.getX()-this.speed); break;
		case "right": this.setX(this.getX()+this.speed); break;
		}
	}
}
