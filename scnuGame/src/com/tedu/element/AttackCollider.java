package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class AttackCollider extends ElementObj{
	private int attack; // 攻击力
	private String fx; // 角色朝向
	private int attackType; // 攻击判定箱类型
	
	public AttackCollider() {}
	
	private AttackCollider(int x, int y, int w, int h, ImageIcon icon, String str) {
		super(x, y, w, h, icon);
	}
	
	@Override
	public ElementObj createElement(String str) { // 定义字符串的规则
		String[] split = str.split(",");
		for (String str1: split) {
			String[] split2 = str1.split(":");
			switch (split2[0]) {
				case "x": this.setX(Integer.parseInt(split2[1])); break;
				case "y": this.setY(Integer.parseInt(split2[1])); break;
				case "w": this.setW(Integer.parseInt(split2[1])); break;
				case "h": this.setH(Integer.parseInt(split2[1])); break;
				case "fx": this.fx = split2[1]; break;
				case "attack": this.attack = Integer.parseInt(split2[1]); break; // 角色攻击力
				case "attackType": this.attackType = Integer.parseInt(split2[1]); break; // 角色攻击类型
				default: break;
			}
		}
		
		return this;
	}
	
	@Override
	public void showElement(Graphics g) {}
	
	@Override
	protected void move(long gameTime) {}
	
	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}
	
	public void fitAttackType() {
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
}
