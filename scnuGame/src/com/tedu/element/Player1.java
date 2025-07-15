package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Player1 extends ElementObj{
	/**
	 * 移动属性:
	 * 1.单属性  配合  方向枚举类型使用; 一次只能移动一个方向 
	 * 2.双属性  上下 和 左右    配合boolean值使用 例如： true代表上 false 为下 不动？？
	 *                      需要另外一个变来确定是否按下方向键
	 *                约定    0 代表不动   1代表上   2代表下  
	 * 3.4属性  上下左右都可以  boolean配合使用  true 代表移动 false 不移动
	 * 						同时按上和下 怎么办？后按的会重置先按的
	 * 说明：以上3种方式 是代码编写和判定方式 不一样
	 * 说明：游戏中非常多的判定，建议灵活使用 判定属性；很多状态值也使用判定属性
	 *      多状态 可以使用map<泛型,boolean>;set<判定对象> 判定对象中有时间
	 *      
	 * @问题 1.图片要读取到内存中： 加载器  临时处理方式，手动编写存储到内存中     
	 *       2.什么时候进行修改图片(因为图片是在父类中的属性存储)
	 *       3.图片应该使用什么集合进行存储
	 */
	
	// 角色基本属性
	private int player1_max_hp = 100; // 血量
	private int player1_hp = 50;
	
	private int player1_max_vit = 100; // 耐力
	private int player1_vit = 50;
	
	private int player1_max_mp = 100; //法力值
	private int player1_mp =  50;
	
	private int attack = 1; // 攻击力
	
	// 展示当前属性的状态条
	private Bar hpBar = new Bar(20, 100, 100, 10, null, player1_max_hp, player1_hp, Color.RED);
	private Bar vitBar = new Bar(20, 120, 100, 10, null, player1_max_vit, player1_vit, Color.YELLOW);
	private Bar mpBar = new Bar(20, 140, 100, 10, null, player1_max_vit, player1_vit, Color.BLUE);
	
	// 攻击控制
	private int pkType = 0; // 1普攻  2重击  3远程
	
	private boolean player1_attacking1 = false; // 正在第1种攻击状态中
	private boolean player1_attacking2 = false; // 正在第2种攻击状态中
	private boolean player1_attacking3 = false; // 正在第3种攻击状态中
	
	private boolean player1_attack1_time = false; // 控制第1种攻击状态的判定时间
	private boolean player1_attack2_time = false; // 控制第2种攻击状态的判定时间
	private boolean player1_attack3_time = false; // 控制第3种攻击子弹发射时间
	
	private long attackTime = 0L; // 攻击间隔
	
	// 用于控制不同攻击的cd
	private long player1_attack1_over_time = 0L; // 第1种攻击上次攻击结束时间
	private long player1_attack2_over_time = 0L; // 第2种攻击上次攻击结束时间
	private long player1_attack3_over_time = 0L; // 第3种攻击上次攻击结束时间
	private long player1_attack1_cd_time = 0L; // 第1种攻击的cd时间
	private long player1_attack2_cd_time = 0L; // 第2种攻击的cd时间
	private long player1_attack3_cd_time = 0L; // 第3种攻击的cd时间
	
	private int player1_attack2_rush_distance_rate = 5; // 冲刺技能距离是单次奔跑距离的倍率
	
	
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
	private int maxXWalkSpeed = 2; // x轴最大走路速度
	private int maxXRunSpeed = 7; // x轴最大奔跑速度
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
	private final int vitAttack2Consume = 30;
	
	private final int mpAttack3Consume = 40;
	
	public Player1(){}
	// 初始化玩家属性，目前仅继承父类属性
	public Player1(int x, int y, int w, int h, ImageIcon icon) {
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
		ImageIcon icon2 = GameLoad.imgMaps.get("player1_right_idle").get(0);
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

	/**
	 * @说明 重写方法： 重写的要求：方法名称 和参数类型序列 必须和父类的方法一样
	 * @重点 监听的数据需要改变状态值
	 */
	// 重写keyClick函数，监听键盘操作，玩家操作
	@Override
	public void keyClick(int bl, int key) {
//		System.out.println(bl + "," + key);
		if (bl == 1) { // 单击
			switch (key) {
				/**
				 * @说明 移动控制
				 */
				case 65:
					this.player1_right_walk = false;
					this.player1_right_idle = false;
					this.player1_left_idle = false;
					this.player1_left_walk = true;
					this.player1_left_run = false;
					this.player1_right_run = false;
					this.fx = "left";
					pictureIndex=0;
					break; // 左
				case 68:
					this.player1_right_walk = true;
					this.player1_right_idle = false;
					this.player1_left_idle = false;
					this.player1_left_walk = false;
					this.player1_left_run = false;
					this.player1_right_run = false;
					this.fx = "right";
					pictureIndex=0;
					break; // 右
					
					/**
					 * @说明 战斗控制
					 */
					case 74:
						this.pkType = 1;
						if (player1_vit < vitAttack1Consume) {
							pkType = 0;
						}
						break;
					case 76:
						this.pkType = 2;
						if (player1_vit < vitAttack2Consume) {
							pkType = 0;
						}
						break;
					case 73:
						this.pkType = 3;
						if (player1_mp < mpAttack3Consume) {
							pkType = 0;
						}
						break;
			}
		} else if(bl==2){ // 双击
			switch (key) {
			/**
			 * @说明 移动控制
			 */
			case 65:
				this.player1_right_walk=false;
				this.player1_right_idle=false;
				this.player1_left_idle=false;
				this.player1_left_walk = false;
				this.player1_left_run = true;
				this.player1_right_run = false;
				this.fx = "left";
				
//				if (player1_vit < vitRunConsume) {
//					this.player1_left_run = false;
//					this.player1_left_walk = true;
//				}
				
				pictureIndex=0;
				break; // 左
			case 68:
				this.player1_right_walk=false;
				this.player1_right_idle=false;
				this.player1_left_idle=false;
				this.player1_left_walk = false;
				this.player1_left_run = false;
				this.player1_right_run = true;
				this.fx = "right";
				
//				if (player1_vit < vitRunConsume) {
//					this.player1_right_run = false;
//					this.player1_right_walk = true;
//				}
				
				pictureIndex=0;
				break; // 右

			/**
			 * @说明 战斗控制
			 */
			case 74:
				this.pkType = 1;
				if (player1_vit < vitAttack1Consume) {
					pkType = 0;
				}
				break;
			case 76:
				this.pkType = 2;
				if (player1_vit < vitAttack2Consume) {
					pkType = 0;
				}
				break;
			case 73:
				this.pkType = 3;
				if (player1_mp < mpAttack3Consume) {
					pkType = 0;
				}
				break;
			}
		} else { // 松开
			switch (key) {
				/**
				 * @说明 移动控制
				 */
				case 65:
					this.player1_left_walk = false;
					this.player1_left_idle = true;
					this.player1_left_run = false;
					this.player1_right_walk = false;
					this.player1_right_idle = false;
					this.player1_right_run = false;
					pictureIndex = 0;
					break; // 左
				case 68:
					this.player1_right_walk = false;
					this.player1_right_idle = true;
					this.player1_right_run = false;
					this.player1_left_idle = false;
					this.player1_left_walk = false;
					this.player1_left_run = false;
					pictureIndex = 0;
					break; // 右

				case 75:
					if (!bottomCollider.isCollided()) break;
					if (this.player1_left_idle || this.player1_left_walk || this.player1_left_run) {
						this.player1_left_jump = true;
					} else if (this.player1_right_idle || this.player1_right_walk || this.player1_right_run) {
						this.player1_right_jump = true;
					}
					
				/**
				* @说明 战斗控制
				*/
				case 76:
				case 73:
				case 74:
					this.pkType = 0;
					break;
			}
		}
	}
	
	// 重写角色移动方式
	@Override
	protected void move(long gameTime) {
		// x轴和y轴移动
		playerXMove();
		playerYMove();

//		System.out.println(XSpeed);
//		System.out.println(YSpeed);
		
		// 待机时恢复大量耐力值和法力值
		// 行走时恢复少量耐力值
		// 奔跑时消耗少量耐力值
		if (gameTime - this.moveVitConsumeTime >= 10) {
			moveVitConsumeTime = gameTime;
			
			if (player1_left_idle || player1_right_idle) {
				player1_vit = Math.min(player1_max_vit, player1_vit + 2);
				player1_mp = Math.min(player1_max_mp, player1_mp + 2);
			} else if (player1_left_walk || player1_right_walk) {
				player1_vit = Math.min(player1_max_vit, player1_vit + 1);
			} else if (player1_left_run || player1_right_run) {
				player1_vit = Math.max(0, player1_vit-vitRunConsume);
			}
			vitBar.setNowNum(player1_vit);
			mpBar.setNowNum(player1_mp);
		}
		
		
		// 动完之后初始化
		XSpeed = 0;
		YSpeed = Math.min(20,YSpeed+g);
	}

	/**
	 * @说明：0表示移动失败，1和2表示移动成功
	 * @0：位置不会移动，留在原地
	 * @1，仅X轴移动成功
	 * @2，仅Y轴移动成功
	 * @3，全部移动成功
	 */
	//distance表示移动的距离
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
	
	/**
	 * @说明：false表示移动失败，true表示移动成功
	 * @移动失败：位置不会移动，留在原地
	 * @移动成功，位置和碰撞箱移动特定距离
	 * @详细使用方法：参照无参数的playerXMove和playerYMove
	 */
	//XDistance正数表示向右边移动
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
	
	//YDistance正数表示向下边移动
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
	// X轴移动，走路、奔跑状态
	private void playerXMove(){ 
		// 奔跑时体力不足 改为走路
		if (player1_vit < vitRunConsume && player1_right_run==true) {
			this.player1_right_run = false;
			this.player1_right_walk = true;
		}
		if (player1_vit < vitRunConsume && player1_left_run==true) {
			this.player1_left_run = false;
			this.player1_left_walk = true;
		}
		
		// 移动
		if (this.player1_left_walk && this.getX() > 0) {
			XSpeed=-maxXWalkSpeed;
//			if(!playerXMove(XSpeed)){
//				XSpeed=0;
//			}

//			ColliderMove( XSpeed,0);
//			if(leftCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}

		} else if (this.player1_right_walk && this.getX() < 1200-this.getW()) {
			XSpeed=maxXWalkSpeed;
//			ColliderMove(XSpeed,0);
//			if(rightCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}
		// 奔跑
		} else if (this.player1_left_run && this.getX() > 0) {
			XSpeed=-maxXRunSpeed;
//			ColliderMove(XSpeed,0);
//			if(leftCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}
		} else if (this.player1_right_run && this.getX() < 1800-this.getW()) {
			XSpeed=maxXRunSpeed;
//			ColliderMove(XSpeed,0);
//			if(rightCollider.isCollided()){
//				ColliderMove( -XSpeed,0);
//				XSpeed=0;
//			}
		}
		
		// 设置位置变换 15ms刷新一次
		if (player1_left_walk || player1_right_walk || this.player1_left_run ||
				this.player1_right_run) {
//			this.setX(this.getX() + XSpeed);
			if(!playerXMove(XSpeed)){
				XSpeed=0;
			}
		}
	}
	
	private void playerYMove(){
		//按下跳跃键
		if(this.player1_left_jump || this.player1_right_jump){
			this.player1_left_jump=false;
			this.player1_right_jump=false;
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
	
	// 玩家的动画刷新(攻击/待机/走路/奔跑动画)
	@Override
	protected void updateImage(long gameTime, int sleepTime) {
//		ImageIcon icon=GameLoad.imgMap.get(fx);
//		System.out.println(icon.getIconHeight()); // 得到图片的高度
//		如果高度是小于等于0 就说明你的这个图片路径有问题
		
		// 从数据加载器中加载图片
		List<ImageIcon> imageIcons = null;
		
		// 更新移动图片
		if(gameTime-this.pictureTime >= 5){
			if(this.player1_left_idle||this.player1_right_idle){
				imageIcons = GameLoad.imgMaps.get("player1_"+fx+"_idle");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				
			}else if(this.player1_left_walk||this.player1_right_walk){
				imageIcons = GameLoad.imgMaps.get("player1_"+fx+"_walk");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				
			}else if (this.player1_left_run||this.player1_right_run) {
				imageIcons = GameLoad.imgMaps.get("player1_"+fx+"_run");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				
			}
			
			// 执行了一次刷新，更新照片更换最后时间
			pictureTime = gameTime;
		}
		
		// 更新攻击图片
		if(gameTime-this.attackPictureTime >= 5){
			if (pkType != 0 || player1_attacking1 || player1_attacking2 || player1_attacking3) {
				if (player1_attacking1 || pkType==1) {
					player1_attacking1 = true;
					
					imageIcons = GameLoad.imgMaps.get("player1_" + fx + "_attack1");
					this.setIcon(imageIcons.get(attackPictureIndex));
					
					attackPictureIndex++;
					
					if (attackPictureIndex == 4) {
						player1_attack1_time = true; // 控制攻击1判定箱何时出现
					}
					
					// 结束攻击设置
					if (attackPictureIndex >= imageIcons.size()) {
						attackPictureIndex = 0;
						player1_attacking1 = false;
						pkType = 0;
					}
					
				} else if (player1_attacking2 || pkType==2){ // 技能动画
					player1_attacking2 = true;
					
					imageIcons = GameLoad.imgMaps.get("player1_" + fx + "_attack2");
					this.setIcon(imageIcons.get(attackPictureIndex));
					
					attackPictureIndex++;
					
					// 移动
					if (this.fx.equals("left") && this.getX() > 0) {
						XSpeed = -maxXRunSpeed*player1_attack2_rush_distance_rate;
						ColliderMove(XSpeed,0);
						if(leftCollider.isCollided()){
							ColliderMove( -XSpeed,0);
							XSpeed=0;
						}
					} else if (this.fx.equals("right") && this.getX() < 1200-this.getW()) {
						XSpeed = maxXRunSpeed*player1_attack2_rush_distance_rate;
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
						player1_attacking2 = false;
						pkType = 0;
						player1_attack2_time = true; // 控制技能可以进行碰撞判定
					}
					
				} else if (player1_attacking3 || pkType==3){ // 远程动画
					player1_attacking3 = true;
						
					imageIcons = GameLoad.imgMaps.get("player1_" + fx + "_attack3");
					this.setIcon(imageIcons.get(attackPictureIndex));
					attackPictureIndex++;
						
					if (attackPictureIndex >= imageIcons.size()) {
						attackPictureIndex = 0;
						player1_attacking3 = false;
						pkType = 0;
						player1_attack3_time = true; // 控制子弹可以发射
					}
				}
			}
			
			attackPictureTime = gameTime;
		}

	}

	@Override
	public void attack(long gameTime) {
		if(player1_attack1_time){
			// 消耗体力
			this.player1_vit -= vitAttack1Consume;
			
			// 碰撞箱
			AttackCollider element = new AttackCollider(this.getX(), this.getY(),
					this.getW(), this.getH(), null, this.fx, this.attack,
					1, "player1");
			
			element.fitAttackType();
			ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
			
			player1_attack1_time = false;
		}else if(player1_attack2_time){
			this.player1_vit -= vitAttack2Consume;
			
			// 碰撞箱
			AttackCollider element = new AttackCollider(this.getX(), this.getY(),
					this.getW(), this.getH(), null, this.fx, this.attack,
					2, "player1");
			
			element.fitAttackType();
			ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
			
			player1_attack2_time = false;
		}else if(player1_attack3_time){
//			if(gameTime-this.pictureTime >= 5){}
//			List<ImageIcon> imageIcons = GameLoad.imgMaps.get("player1_" + fx + "_attack1");
			this.player1_mp -= mpAttack3Consume;
			
			// 发射子弹
			Bullet element = new Bullet(this.getX(), this.getY(),
					this.getW(), this.getH(), null, this.attack,
					10, this.fx, "player1");
			element.fitImage();
			ElementManager.getManager().addElement(element, GameElement.BULLET);
			
			player1_attack3_time = false;
		}
		
		vitBar.setNowNum(this.player1_vit);
		mpBar.setNowNum(this.player1_mp);
	}
	
	@Override
	public String toString() {
		int x = this.getX();
		int y = this.getY();
		int w = this.getW();
		int h = this.getH();

		// {x:3,y:1,f:right} json格式 弹药样式可拓展
		return "x:"+x+",y:"+y+",w:"+w+",h:"+h+",fx:"+fx+",attack:"+attack+
				",from:player1";
	}
	
	// 受伤
	public void getHurt(int damage) {
		this.player1_hp = Math.max(0,this.player1_hp - damage);
		hpBar.setNowNum(player1_hp);
		
		// 受击图片
		
		if (player1_hp <= 0) {
			this.setLive(false);
		}
		
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
