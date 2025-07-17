package com.tedu.element;

import java.awt.*;

import javax.swing.ImageIcon;

public class AttackCollider extends ElementObj{
	private int attack; // 角色攻击力,不是实际造成的伤害
	private String fx; // 角色朝向
	private int attackType; // 攻击判定箱类型
	private String from; // 攻击是由谁发出的

	//线的属性
	private int W;
	private int startX=0;
	private int startY=0;
	private int endX=0;
	private int endY=0;
	
	public AttackCollider() {}
	
	public AttackCollider(int x, int y, int w, int h, ImageIcon icon, String fx,
			int attack, int attackType, String from) {
		super(x, y, w, h, icon);
		
		this.fx = fx;
		this.attack = attack;
		this.attackType = attackType;
		this.from = from;
	}
	public AttackCollider(int x, int y, int w, int h, ImageIcon icon, String fx,
						  int attack, int attackType, String from, int endX, int endY) {
		super(x, y, w, h, icon);
		this.fx = fx;
		this.attack = attack;
		this.attackType = attackType;
		this.from = from;
		this.startX = x;
		this.startY = y;
		this.endX = endX;
		this.endY = endY;
		this.W = w;
	}
	
	@Override
	public void showElement(Graphics g) {
		if (from.equals("boss")) {
			Graphics2D g2d = (Graphics2D) g;
			if (attackType==3) {
				g2d.setColor(Color.MAGENTA);
				g2d.setStroke(new BasicStroke(W));
				double angle = Math.atan2(endY - startY, endX - startX);
				int newX = (int) (startX + 5000 * Math.cos(angle));
				int newY = (int) (startY + 5000 * Math.sin(angle));
				g2d.drawLine(startX, startY, newX, newY);
			}
		}else{
//			g.setColor(Color.red);
//			g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());
		}
	}
	
	@Override
	protected void move(long gameTime) {}
	
	public void setAttackType(int attackType) {
		if (from.equals("player1")) {
			this.attackType = attackType;
		}else if(from.equals("enemy")) {
			this.attackType = attackType;
		}else if (from.equals("player2")) {
			this.attackType = attackType;
		}else if (from.equals("boss")) {
			this.attackType = attackType;
		}
		
		// 设置敌人的攻击方式
	}
	
	public void fitAttackType() {
		// 角色的攻击判定箱和技能匹配
		if (from.equals("player1") || from.equals("player2")) {
			if (attackType==1) {
				this.setW(50);
				switch (this.fx) {
					case "left": break;
					case "right": this.setX(this.getX()+50); break;
				}
			} else if (attackType==2 && from.equals("player1")){
				this.setW(150);
				this.setX(this.getX()-25);
			}
		}
		// 怪物的攻击判定箱和攻击方式匹配
		else if(from.equals("enemy")){
			if (attackType==1) {
				this.setW(50);
				switch (this.fx) {
					case "left": this.setX(this.getX()-25);break;
					case "right": this.setX(this.getX()+70); break;
				}
			}
		}
		else if(from.equals("boss")){
			if (attackType==1) {
				this.setW(120);
				switch (this.fx) {
					case "left": this.setX(this.getX()-25);break;
					case "right": this.setX(this.getX()+130); break;
				}
			} else if (attackType==2) {
				this.setW(350);
				switch (this.fx) {
					case "left": this.setX(this.getX()-25);break;
					case "right": this.setX(this.getX()+130); break;
				}
			}else if (attackType==3) {

			}
		}
		

	}
	
	public int getAttack() {
		// 不同攻击类型返回不同倍率
		// 玩家
		if (from.equals("player1")) {
			if (attackType==1) {
				return attack;
			} else if (attackType==2) {
				return 4*attack;
			}
		}
		// 敌人
		else if (from.equals("enemy")) {
			if(attackType==1){
				return attack;
			}
		}else if(from.equals("boss")){
			return attack;
		} else if (from.equals("player2")){
			if(attackType==1){
				return attack;
			}
		}

		
		return attack;
	}
	
	public String getFrom() {
		return this.from;
	}


}
