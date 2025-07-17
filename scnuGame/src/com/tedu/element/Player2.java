package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Player2 extends ElementObj{
	// 角色基本属性
	private int player2_max_hp = 90; // 血量
	private int player2_hp = 90;
		
	private int player2_max_vit = 90; // 耐力
	private int player2_vit = 90;
	
	private int player2_max_mp = 150; //法力值
	private int player2_mp =  150;
		
	private int attack = 3; // 攻击力
		
	// 展示当前属性的状态条
	private Bar hpBar = new Bar(220, 100, 100, 10, null, player2_max_hp, player2_hp, Color.RED);
	private Bar vitBar = new Bar(220, 120, 100, 10, null, player2_max_vit, player2_vit, Color.YELLOW);
	private Bar mpBar = new Bar(220, 140, 100, 10, null, player2_max_mp, player2_mp, Color.BLUE);
		
	// 攻击控制
	private int pkType = 0; // 1普攻  2瞬身  3远程
		
	private boolean player2_attacking1 = false; // 正在第1种攻击状态中
	private boolean player2_attacking2 = false; // 正在第2种攻击状态中
	private boolean player2_attacking3 = false; // 正在第3种攻击状态中
		
	private boolean player2_attack1_time = false; // 控制第1种攻击状态的判定时间
	private boolean player2_attack2_time = false; // 控制第2种攻击状态的判定时间
	private boolean player2_attack3_time = false; // 控制第3种攻击子弹发射时间
	
	private long attackTime = 0L; // 用于控制攻击的间隔
	
	private int player2_attack2_avoid_distance_rate = 3; // 后撤技能距离是单次奔跑距离的倍率
		
		
	// 移动状态属性(待机、走路、跳跃、奔跑 4个状态)
	private boolean player2_left_idle = false;
	private boolean player2_right_idle = true;
	private boolean player2_left_walk = false;
	private boolean player2_right_walk = false;
	private boolean player2_left_jump = false;
	private boolean player2_right_jump = false;
	private boolean player2_left_run = false;
	private boolean player2_right_run = false;
		
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
	private final int mpAttack2Consume = 20;
		
	private final int mpAttack3Consume = 45;
	
	// 玩家头像
	private ImageIcon headIcon = null;
	
	// 受到攻击状态
	private boolean underattacking = false;
	private long underAttackPictureTime = 0L;
	private int underAttackPictureIndex = 0;
	
	
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
		
		// 玩家头像
		if (headIcon != null) {
			g.drawImage(this.headIcon.getImage(), 
					230, 10, 
					80, 80, null);
		}
	}
	
	@Override
	public ElementObj createElement(String str) {
		String[] strs = str.split(",");
		this.setX(Integer.parseInt(strs[0]));
		this.setY(Integer.parseInt(strs[1]));
		ImageIcon icon2 = GameLoad.imgMaps.get("player2_right_idle").get(0);
		this.headIcon = GameLoad.imgMaps.get("player2_head").get(0);
		
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
	
	@Override
	public void keyClick(int bl, int key) {
//		System.out.println(bl + "," + key);
		if (bl == 1) { // 单击
			switch (key) {
				/**
				 * @说明 移动控制
				 */
				case 37:
					this.player2_right_walk = false;
					this.player2_right_idle = false;
					this.player2_left_idle = false;
					this.player2_left_walk = true;
					this.player2_left_run = false;
					this.player2_right_run = false;
					this.fx = "left";
					pictureIndex=0;
					break; // 左
				case 39:
					this.player2_right_walk = true;
					this.player2_right_idle = false;
					this.player2_left_idle = false;
					this.player2_left_walk = false;
					this.player2_left_run = false;
					this.player2_right_run = false;
					this.fx = "right";
					pictureIndex=0;
					break; // 右
					
					/**
					 * @说明 战斗控制
					 */
					case 97:
						this.pkType = 1;
						if (player2_vit < vitAttack1Consume) {
							pkType = 0;
						}
						break;
					case 101:
						this.pkType = 2;
						if (player2_vit < vitAttack2Consume || player2_mp < mpAttack2Consume) {
							pkType = 0;
						}
						break;
					case 99:
						this.pkType = 3;
						if (player2_mp < mpAttack3Consume) {
							pkType = 0;
						}
						break;
			}
		} else if(bl==2){ // 双击
			switch (key) {
			/**
			 * @说明 移动控制
			 */
			case 37:
				this.player2_right_walk=false;
				this.player2_right_idle=false;
				this.player2_left_idle=false;
				this.player2_left_walk = false;
				this.player2_left_run = true;
				this.player2_right_run = false;
				this.fx = "left";
				
				pictureIndex=0;
				break; // 左
			case 39:
				this.player2_right_walk=false;
				this.player2_right_idle=false;
				this.player2_left_idle=false;
				this.player2_left_walk = false;
				this.player2_left_run = false;
				this.player2_right_run = true;
				this.fx = "right";
				
				pictureIndex=0;
				break; // 右

			/**
			 * @说明 战斗控制
			 */
			case 97:
				this.pkType = 1;
				if (player2_vit < vitAttack1Consume) {
					pkType = 0;
				}
				break;
			case 101:
				this.pkType = 2;
				if (player2_vit < vitAttack2Consume || player2_mp < mpAttack2Consume) {
					pkType = 0;
				}
				break;
			case 99:
				this.pkType = 3;
				if (player2_mp < mpAttack3Consume) {
					pkType = 0;
				}
				break;
			}
		} else { // 松开
			switch (key) {
				/**
				 * @说明 移动控制
				 */
				case 37:
					this.player2_left_walk = false;
					this.player2_left_idle = true;
					this.player2_left_run = false;
					this.player2_right_walk = false;
					this.player2_right_idle = false;
					this.player2_right_run = false;
					pictureIndex = 0;
					break; // 左
				case 39:
					this.player2_right_walk = false;
					this.player2_right_idle = true;
					this.player2_right_run = false;
					this.player2_left_idle = false;
					this.player2_left_walk = false;
					this.player2_left_run = false;
					pictureIndex = 0;
					break; // 右

				case 98:
					if (!bottomCollider.isCollided()) break;
					if (this.player2_left_idle || this.player2_left_walk || this.player2_left_run) {
						this.player2_left_jump = true;
					} else if (this.player2_right_idle || this.player2_right_walk || this.player2_right_run) {
						this.player2_right_jump = true;
					}
					
				/**
				* @说明 战斗控制
				*/
				case 97:
				case 101:
				case 99:
					this.pkType = 0;
					break;
			}
		}
	}
	
	@Override
	protected void move(long gameTime) {
		// x轴和y轴移动
		playerXMove();
		playerYMove();
		
		// 待机时恢复少量耐力值和大量法力值
		// 行走时恢复少量耐力值和法力值
		// 奔跑时消耗少量耐力值
		if (gameTime - this.moveVitConsumeTime >= 10) {
			moveVitConsumeTime = gameTime;
			
			if (player2_left_idle || player2_right_idle) {
				player2_vit = Math.min(player2_max_vit, player2_vit + 1);
				player2_mp = Math.min(player2_max_mp, player2_mp + 3);
			} else if (player2_left_walk || player2_right_walk) {
				player2_vit = Math.min(player2_max_vit, player2_vit + 1);
				player2_mp = Math.min(player2_max_mp, player2_mp + 1);
			} else if (player2_left_run || player2_right_run) {
				player2_vit = Math.max(0, player2_vit-vitRunConsume);
			}
			vitBar.setNowNum(player2_vit);
			mpBar.setNowNum(player2_mp);
		}
		
		
		// 动完之后初始化
		XSpeed = 0;
		YSpeed = Math.min(20,YSpeed+g);
	}
	
	private int playerMove(int XDistance, int YDistance) {
		int result=0;
		if(playerXMove(XDistance)){
			result+=1;
		}
		if(playerYMove(YDistance)){
			result+=2;
		}
		return result;
	}
	private boolean playerXMove(int XDistance) {
		Collider detectedCollision=null;
		if(XDistance>0){
			detectedCollision=rightCollider;
		}else{
			detectedCollision=leftCollider;
		}

		ColliderMove(XDistance,0);

		if(detectedCollision!=null){
			if(detectedCollision.isCollided()){
				ColliderMove(-XDistance,0);
				return false;
			}else{
				this.setX(this.getX()+XDistance);
				return true;
			}
		}else{
			System.out.println("ERROR：碰撞箱检测出错");
			return false;
		}
	}
	private boolean playerYMove(int YDistance) {
		Collider detectedCollision=null;
		if(YDistance>0){
			detectedCollision=bottomCollider;
		}else{
			//向上移动不予检测
			detectedCollision=null;
		}

		ColliderMove(0,YDistance);

		if(detectedCollision!=null){
			//向下移动
			if(detectedCollision.isCollided()){
				ColliderMove(0,-YDistance);
				return false;
			}else{
				this.setY(this.getY()+YDistance);
				return true;
			}
		}else{
			//向上移动不予检测，直接执行
			this.setY(this.getY()+YDistance);
			return true;
		}
	}
	private void playerXMove(){ 
		// 奔跑时体力不足 改为走路
		if (player2_vit < vitRunConsume && player2_right_run==true) {
			this.player2_right_run = false;
			this.player2_right_walk = true;
		}
		if (player2_vit < vitRunConsume && player2_left_run==true) {
			this.player2_left_run = false;
			this.player2_left_walk = true;
		}
		
		// 移动
		if (this.player2_left_walk && this.getX() > 0) {
			XSpeed=-maxXWalkSpeed;
//			if(!playerXMove(XSpeed)){
//				XSpeed=0;
//			}

//			ColliderMove( XSpeed,0);
//			if(leftCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}

		} else if (this.player2_right_walk && this.getX() < 1200-this.getW()) {
			XSpeed=maxXWalkSpeed;
//			ColliderMove(XSpeed,0);
//			if(rightCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}
		// 奔跑
		} else if (this.player2_left_run && this.getX() > 0) {
			XSpeed=-maxXRunSpeed;
//			ColliderMove(XSpeed,0);
//			if(leftCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}
		} else if (this.player2_right_run && this.getX() < 1800-this.getW()) {
			XSpeed=maxXRunSpeed;
//			ColliderMove(XSpeed,0);
//			if(rightCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}
		}
		
		// 设置位置变换 15ms刷新一次
		if (player2_left_walk || player2_right_walk || this.player2_left_run ||
				this.player2_right_run) {
//			this.setX(this.getX() + XSpeed);
			if(!playerXMove(XSpeed)){
				XSpeed=0;
			}
		}
	}
	private void playerYMove(){
		//按下跳跃键
		if(this.player2_left_jump || this.player2_right_jump){
			this.player2_left_jump=false;
			this.player2_right_jump=false;
			YSpeed=maxYSpeed;
		}

		//默认下落
//		ColliderMove( 0,YSpeed);
//		if(YSpeed>0){
//			if(bottomCollider.isCollided()){
//				ColliderMove( 0,-YSpeed);
//				YSpeed=0;
//			}
//		}
//
//		this.setY(this.getY() + YSpeed);
		if(!playerYMove(YSpeed)){
			YSpeed=0;
		}



	}
	private void ColliderMove(int XMovement,int YMovement){
		// X轴移动
		topCollider.setX(topCollider.getX()+XMovement);
		bottomCollider.setX(bottomCollider.getX()+XMovement);
		leftCollider.setX(leftCollider.getX()+XMovement);
		rightCollider.setX(rightCollider.getX()+XMovement);
		
		// Y轴移动
		topCollider.setY(topCollider.getY()+YMovement);
		bottomCollider.setY(bottomCollider.getY()+YMovement);
		leftCollider.setY(leftCollider.getY()+YMovement);
		rightCollider.setY(rightCollider.getY()+YMovement);
	}
	
	@Override
	protected void updateImage(long gameTime, int sleepTime) {
//		ImageIcon icon=GameLoad.imgMap.get(fx);
//		System.out.println(icon.getIconHeight()); // 得到图片的高度
//		如果高度是小于等于0 就说明你的这个图片路径有问题
		
		// 从数据加载器中加载图片
		List<ImageIcon> imageIcons = null;
		
		// 更新移动图片
		if(gameTime-this.pictureTime >= 5){
			if(this.player2_left_idle||this.player2_right_idle){
				imageIcons = GameLoad.imgMaps.get("player2_"+fx+"_idle");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				
			}else if(this.player2_left_walk||this.player2_right_walk){
				imageIcons = GameLoad.imgMaps.get("player2_"+fx+"_walk");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				
			}else if (this.player2_left_run||this.player2_right_run) {
				imageIcons = GameLoad.imgMaps.get("player2_"+fx+"_run");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				
			}
			// 执行了一次刷新，更新照片更换最后时间
			pictureTime = gameTime;
		}
		
		// 更新攻击图片
		if(gameTime-this.attackPictureTime >= 5){
			if (pkType != 0 || player2_attacking1 || player2_attacking2 || player2_attacking3) {
				if (player2_attacking1 || pkType==1) {
					player2_attacking1 = true;
					
					imageIcons = GameLoad.imgMaps.get("player2_" + fx + "_attack1");
					this.setIcon(imageIcons.get(attackPictureIndex));
					
					attackPictureIndex++;
					
					if (attackPictureIndex == 4) {
						player2_attack1_time = true; // 控制攻击1判定箱何时出现
					}
					
					// 结束攻击设置
					if (attackPictureIndex >= imageIcons.size()) {
						attackPictureIndex = 0;
						player2_attacking1 = false;
						pkType = 0;
					}
					
				} else if (player2_attacking2 || pkType==2){ // 技能动画
					player2_attacking2 = true;
					
					imageIcons = GameLoad.imgMaps.get("player2_" + fx + "_attack2");
					this.setIcon(imageIcons.get(attackPictureIndex));
					
					attackPictureIndex++;
					
					// 移动
					if (this.fx.equals("left") && this.getX() > 0) {
						XSpeed = maxXRunSpeed*player2_attack2_avoid_distance_rate;
						ColliderMove(XSpeed,0);
						if(leftCollider.isCollided()){
							ColliderMove( -XSpeed,0);
							XSpeed=0;
						}
					} else if (this.fx.equals("right") && this.getX() < 1200-this.getW()) {
						XSpeed = -maxXRunSpeed*player2_attack2_avoid_distance_rate;
						ColliderMove(XSpeed,0);
						if(leftCollider.isCollided()){
							ColliderMove( -XSpeed,0);
							XSpeed=0;
						}
					}
					this.setX(this.getX() + XSpeed);
					
					// 控制停止
					if (attackPictureIndex >= imageIcons.size()) {
						attackPictureIndex = 0;
						player2_attacking2 = false;
						pkType = 0;
						player2_attack2_time = true; // 控制技能可以进行碰撞判定
					}
					
				} else if (player2_attacking3 || pkType==3){ // 远程动画
					player2_attacking3 = true;
						
					imageIcons = GameLoad.imgMaps.get("player2_" + fx + "_attack3");
					this.setIcon(imageIcons.get(attackPictureIndex));
					attackPictureIndex++;
						
					if (attackPictureIndex >= imageIcons.size()) {
						attackPictureIndex = 0;
						player2_attacking3 = false;
						pkType = 0;
						player2_attack3_time = true; // 控制子弹可以发射
					}
				}
			}
			
			attackPictureTime = gameTime;
		}
		
		if(gameTime-this.underAttackPictureTime >= 5){
			if (underattacking) {
				imageIcons = GameLoad.imgMaps.get("player2_" + fx + "_hurt");
				this.setIcon(imageIcons.get(underAttackPictureIndex));
				underAttackPictureIndex++;
				
				// 结束受击设置
				if (underAttackPictureIndex >= imageIcons.size()) {
					underAttackPictureIndex = 0;
					underattacking = false;
				}
			}
			
			underAttackPictureTime = gameTime;
		}
		
	}
	
	@Override
	public void attack(long gameTime) {
		if(player2_attack1_time){
			// 消耗体力
			this.player2_vit -= vitAttack1Consume;
			
			// 碰撞箱
			AttackCollider element = new AttackCollider(this.getX(), this.getY(),
					this.getW(), this.getH(), null, this.fx, this.attack,
					1, "player2");
			
			element.fitAttackType();
			ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
			
			player2_attack1_time = false;
		}else if(player2_attack2_time){
			this.player2_vit -= vitAttack2Consume;
			this.player2_mp -= mpAttack2Consume;
			
			// 恢复少量血量
			this.gethpRecover(3);
			
			player2_attack2_time = false;
		}else if(player2_attack3_time){
//			if(gameTime-this.pictureTime >= 5){}
//			List<ImageIcon> imageIcons = GameLoad.imgMaps.get("player1_" + fx + "_attack1");
			this.player2_mp -= mpAttack3Consume;
			
			// 发射子弹
			Bullet element = new Bullet(this.getX(), this.getY(),
					this.getW(), this.getH(), null, this.attack,
					15, this.fx, "player2");
			element.fitImage();
			ElementManager.getManager().addElement(element, GameElement.BULLET);
			
			player2_attack3_time = false;
		}
		
		vitBar.setNowNum(this.player2_vit);
		mpBar.setNowNum(this.player2_mp);
	}
	
	@Override
	public String toString() {
		int x = this.getX();
		int y = this.getY();
		int w = this.getW();
		int h = this.getH();

		// {x:3,y:1,f:right} json格式 弹药样式可拓展
		return "x:"+x+",y:"+y+",w:"+w+",h:"+h+",fx:"+fx+",attack:"+attack+
				",from:player2";
	}
	
	// 受伤
	public void getHurt(int damage) {
		if (!player2_attack2_time && !player2_attack3_time) {
			this.player2_hp = Math.max(0,this.player2_hp - damage);
			hpBar.setNowNum(player2_hp);
				
			// 受击状态设置
			underattacking = true;
			// 打断
			player2_attack1_time = false;
		}
			
		if (player2_hp <= 0) {
			this.setLive(false);
		}
			
	}
	
	// 恢复
	public void gethpRecover(int recover) {
		player2_hp = Math.min(player2_max_hp, player2_hp + recover);
		hpBar.setNowNum(this.player2_hp);
	}
	public void getmpRecover(int recover) {
		player2_mp = Math.min(player2_max_mp, player2_mp + recover);
		mpBar.setNowNum(this.player2_mp);
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
