package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Player2 extends ElementObj{
	// 角色基本属性
	private int player2_max_hp = 70; // 血量
	private int player2_hp = 50;
		
	private int player2_max_vit = 80; // 耐力
	private int player2_vit = 50;
	
	private int player2_max_mp = 150; //法力值
	private int player2_mp =  50;
		
	private int attack = 1; // 攻击力
		
	// 展示当前属性的状态条
	private Bar hpBar = new Bar(220, 100, 100, 10, null, player2_max_hp, player2_hp, Color.RED);
	private Bar vitBar = new Bar(220, 120, 100, 10, null, player2_max_vit, player2_vit, Color.YELLOW);
	private Bar mpBar = new Bar(220, 140, 100, 10, null, player2_max_vit, player2_vit, Color.BLUE);
		
	// 攻击控制
	private int pkType = 0; // 1普攻  2瞬身  3远程
		
	private boolean player2_attacking1 = false; // 正在第1种攻击状态中
	private boolean player2_attacking2 = false; // 正在第2种攻击状态中
	private boolean player2_attacking3 = false; // 正在第3种攻击状态中
		
	private boolean player2_attack1_time = false; // 控制第1种攻击状态的判定时间
	private boolean player2_attack2_time = false; // 控制第2种攻击状态的判定时间
	private boolean player2_attack3_time = false; // 控制第3种攻击子弹发射时间
	
	private long attackTime = 0L; // 用于控制攻击的间隔
	
	private int player2_attack2_avoid_distance_rate = 5; // 冲刺技能距离是单次奔跑距离的倍率
		
		
	// 移动状态属性(待机、走路、跳跃、奔跑 4个状态)
	private boolean player1_left_idle = false;
	private boolean player1_right_idle = true;
	private boolean player1_left_walk = false;
	private boolean player1_right_walk = false;
	private boolean player1_left_jump = false;
	private boolean player1_right_jump = false;
	private boolean player1_left_run = false;
	private boolean player1_right_run = false;
		
	// 碰撞箱
	private Collider topCollider;
	private Collider bottomCollider;
	private Collider leftCollider;
	private Collider rightCollider;
		
		
	// 速度
	private int maxXWalkSpeed = 3; // x轴最大走路速度
	private int maxXRunSpeed = 9; // x轴最大奔跑速度
	private int maxYSpeed = -15; // 最大下落/上升速度
	private int XSpeed = 0; // 当前x轴速度
	private int YSpeed = 0; // 当前y轴速度
		
	// 重力(下落速度相关)
	private int g=1;
		
	// 换装图片轮播
	private long pictureTime = 0L; // 移动照片切换间隔时间
	private long attackPictureTime = 0L; // 攻击照片切换间隔时间
	
	private int pictureIndex = 0; // 移动照片切换序号
	private int attackPictureIndex = 0; // 攻击照片切换序号
		
	// 主角朝向(初始默认为右)
	private String fx = "right"; // 左或右
		
	// 控制移动消耗体力的间隔(防止过快)
	private long moveVitConsumeTime = 0;
		
	// 体力消耗+魔力消耗 数值
	private final int vitRunConsume = 2;
	private final int vitAttack1Consume = 5;
	
	private final int vitAttack2Consume = 20;
	private final int mpAttack2Consume = 10;
		
	private final int mpAttack3Consume = 35;
	
	
	public Player2(){}
	// 初始化玩家属性，目前仅继承父类属性
	public Player2(int x, int y, int w, int h, ImageIcon icon) {
		super(x, y, w, h, icon);
	}
	
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
		
		// 血量条 耐力条 魔力条
		hpBar.showElement(g);
		vitBar.showElement(g);
		mpBar.showElement(g);
	}
	
	@Override
	public ElementObj createElement(String str) {
		String[] strs = str.split(",");
		this.setX(Integer.parseInt(strs[0]));
		this.setY(Integer.parseInt(strs[1]));
		ImageIcon icon2 = GameLoad.imgMaps.get("player2_right_idle").get(0);
		
		// 设置图片大小和形状
		this.setH(100);
		this.setW(100);
		this.setIcon(icon2);
		
		ElementObj topCollider = new Collider(getX(), getY()-5, getW(), 5);
		ElementObj bottomCollider = new Collider(getX(), getY()+100, getW(), 5);
		ElementObj leftCollider = new Collider(getX()-5, getY(), 5, getH()-15);
		ElementObj rightCollider = new Collider(getX()+100, getY(), 5, getH()-15);
		
		setTopCollider((Collider) topCollider);
		setBottomCollider((Collider) bottomCollider);
		setLeftCollider((Collider) leftCollider);
		setRightCollider((Collider) rightCollider);

		ElementManager.getManager().addElement(topCollider,GameElement.COLLIDER);
		ElementManager.getManager().addElement(bottomCollider,GameElement.COLLIDER);
		ElementManager.getManager().addElement(leftCollider,GameElement.COLLIDER);
		ElementManager.getManager().addElement(rightCollider,GameElement.COLLIDER);
		
		return this;
	}
	
	
	
	
	
	
	public Collider getTopCollider() {
		return topCollider;
	}

	public void setTopCollider(Collider topCollider) {
		this.topCollider = topCollider;
	}

	public Collider getBottomCollider() {
		return bottomCollider;
	}

	public void setBottomCollider(Collider bottomCollider) {
		this.bottomCollider = bottomCollider;
	}

	public Collider getLeftCollider() {
		return leftCollider;
	}

	public void setLeftCollider(Collider leftCollider) {
		this.leftCollider = leftCollider;
	}

	public Collider getRightCollider() {
		return rightCollider;
	}

	public void setRightCollider(Collider rightCollider) {
		this.rightCollider = rightCollider;
	}

	@Override
	public void setLive(boolean live) {
		super.setLive(live);
		topCollider.setLive(live);
		bottomCollider.setLive(live);
		leftCollider.setLive(live);
		rightCollider.setLive(live);
	}
}
