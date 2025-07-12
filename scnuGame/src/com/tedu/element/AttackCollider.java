package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class AttackCollider extends ElementObj{
	private int attack; // 角色攻击力,不是实际造成的伤害
	private String fx; // 角色朝向
	private int attackType; // 攻击判定箱类型
	private String from; // 攻击是由谁发出的
	
	public AttackCollider() {}
	
	public AttackCollider(int x, int y, int w, int h, ImageIcon icon, String fx,
			int attack, int attackType, String from) {
		super(x, y, w, h, icon);
		
		this.fx = fx;
		this.attack = attack;
		this.attackType = attackType;
		this.from = from;
	}
	
	@Override
	public void showElement(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());
	}
	
	@Override
	protected void move(long gameTime) {}
	
	public void setAttackType(int attackType) {
		if (from.equals("player1")) {
			this.attackType = attackType;
		}
		
		// 设置敌人的攻击方式
	}
	
	public void fitAttackType() {
		// 角色的攻击判定箱和技能匹配
		if (from.equals("player1")) {
			if (attackType==1) {
				this.setW(50);
				
				switch (this.fx) {
					case "left": break;
					case "right": this.setX(this.getX()+50); break;
				}
			} else if (attackType==2){
				this.setW(150);
				this.setX(this.getX()-25);
			}
		}
		
		// 怪物的攻击判定箱和攻击方式匹配
	}
	
	public int getAttack() {
		// 不同攻击类型返回不同倍率
		// 玩家
		if (from.equals("player1")) {
			if (attackType==1) {
				return attack;
			} else if (attackType==2) {
				return 3*attack;
			}
		}
		// 敌人
		
		return attack;
	}
	
	public String getFrom() {
		return this.from;
	}
}
