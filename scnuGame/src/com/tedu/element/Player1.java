package com.tedu.element;

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
	// 移动属性(四属性方式)
	private boolean player1_left_idle = false;
	private boolean player1_right_idle = true;
	private boolean player1_left_walk = false;
	private boolean player1_right_walk = false;
	private boolean player1_left_jump=false;
	private boolean player1_right_jump=false;
	private Collider topCollider;
	private Collider bottomCollider;
	private Collider leftCollider;
	private Collider rightCollider;
	private int maxXSpeed=1;
	private int maxYSpeed=-15;
	private int XSpeed=0;
	private int YSpeed=0;
	private int g=1;
	private int runSpeed=5;
	private long pictureTime=0L;
	private long attackTime=0L;
	private int pictureIndex=0;
	// 变量，记录当前主角朝向(默认为右)
	private String fx = "right";
	// 战斗控制
	private int pkType = 0; // true 攻击   false 停止

	public Player1(){}
	// 初始化玩家属性，目前仅继承父类属性
	public Player1(int x, int y, int w, int h, ImageIcon icon) {
		super(x, y, w, h, icon);
	}
	// 重写showElement函数，使视图层可以显示图象
	@Override
	public void showElement(Graphics g) {

		// 展示图像
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}

	@Override
	public ElementObj createElement(String str) {
		String[] strs = str.split(",");
		this.setX(Integer.parseInt(strs[0]));
		this.setY(Integer.parseInt(strs[1]));
		ImageIcon icon2= GameLoad.imgMaps.get("player1_right_idle").get(0);
		this.setH(100);
		this.setW(100);
		this.setIcon(icon2);
		return this;
	}

	/**
	 * @说明 重写方法： 重写的要求：方法名称 和参数类型序列 必须和父类的方法一样
	 * @重点 监听的数据需要改变状态值
	 */
	// 重写keyClick函数，监听键盘操作，玩家操作
	@Override
	public void keyClick(int bl, int key) {
		System.out.println(bl+","+key);
		if (bl==1) {
			switch (key) {
				/**
				 * @说明 移动控制
				 */
				case 65:
					this.player1_right_walk=false;
					this.player1_right_idle=false;
					this.player1_left_idle=false;
					this.player1_left_walk = true;
					this.fx = "left";
					pictureIndex=0;
					break; // 左
				case 68:
					this.player1_left_walk=false;
					this.player1_left_idle=false;
					this.player1_right_idle=false;
					this.player1_right_walk = true;
					this.fx = "right";
					pictureIndex=0;
					break; // 右

				/**
				 * @说明 战斗控制
				 */
				case 74: this.pkType = 1; break; // 开启攻击状态
				case 76: this.pkType = 2; break;
				case 73: this.pkType = 3; break;
			}
		} else {
			switch (key) {
				/**
				 * @说明 移动控制
				 */
				case 65:
					this.player1_left_walk = false;
					this.player1_left_idle = true;
					pictureIndex = 0;
					break; // 左
				case 68:
					this.player1_right_walk = false;
					this.player1_right_idle = true;
					pictureIndex = 0;
					break; // 右
				case 75:
					if (!bottomCollider.isCollided()) break;
					if (this.player1_left_idle || this.player1_left_walk) {
						this.player1_left_jump = true;
					} else if (this.player1_right_idle || this.player1_right_walk) {
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
	
//	@Override
//	public int compareTo(Play o) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
	
	// 重写角色移动方式
	@Override
	protected void move() {



		playerXMove();
		playerYMove();


		System.out.println(XSpeed);
		System.out.println(YSpeed);
		XSpeed=0;
		YSpeed=Math.min(20,YSpeed+g);

	}
	private void playerXMove(){
		if (this.player1_left_walk && this.getX() > 0) {
			XSpeed=-maxXSpeed;
			ColliderMove( XSpeed,0);
			if(leftCollider.isCollided()){
				ColliderMove( -XSpeed,0);
				XSpeed=0;
			}
		}
		else if (this.player1_right_walk && this.getX() < 1200-this.getW()) {
			XSpeed=maxXSpeed;
			ColliderMove(XSpeed,0);
			if(rightCollider.isCollided()){
				ColliderMove( -XSpeed,0);
				XSpeed=0;
			}
		}
		this.setX(this.getX() + XSpeed);
	}
	private void playerYMove(){
		//按下跳跃键
		if(this.player1_left_jump||this.player1_right_jump){
			this.player1_left_jump=false;
			this.player1_right_jump=false;
			YSpeed=maxYSpeed;
		}

		//默认下落
		ColliderMove( 0,YSpeed);
		if(YSpeed>0){
			if(bottomCollider.isCollided()){
				ColliderMove( 0,-YSpeed);
				YSpeed=0;
			}
		}

		this.setY(this.getY() + YSpeed);

	}

	private void ColliderMove(int XMovement,int YMovement){
		topCollider.setX(topCollider.getX()+XMovement);
		bottomCollider.setX(bottomCollider.getX()+XMovement);
		leftCollider.setX(leftCollider.getX()+XMovement);
		rightCollider.setX(rightCollider.getX()+XMovement);
		topCollider.setY(topCollider.getY()+YMovement);
		bottomCollider.setY(bottomCollider.getY()+YMovement);
		leftCollider.setY(leftCollider.getY()+YMovement);
		rightCollider.setY(rightCollider.getY()+YMovement);
	}

	@Override
	protected void updateImage(long gameTime) {
//		ImageIcon icon=GameLoad.imgMap.get(fx);
//		System.out.println(icon.getIconHeight());//得到图片的高度
//		如果高度是小于等于0 就说明你的这个图片路径有问题
		// 从数据加载器中加载图片
		if(gameTime-this.pictureTime>=10){
			if(this.player1_left_idle||this.player1_right_idle){
				List<ImageIcon> imageIcons = GameLoad.imgMaps.get("player1_"+fx+"_idle");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				pictureTime=gameTime;
			}else if(this.player1_left_walk||this.player1_right_walk){
				List<ImageIcon> imageIcons = GameLoad.imgMaps.get("player1_"+fx+"_walk");
				this.setIcon(imageIcons.get(pictureIndex));
				pictureIndex++;
				pictureIndex%=imageIcons.size();
				pictureTime=gameTime;
			}


		}

	}

	@Override
	public void attack(long gameTime) {
		if (pkType == 0) {
			return ;
		}else if(pkType == 1){
			if(gameTime-this.attackTime>=50){
				attackTime=gameTime;
				// 将构造类的多个步骤封装成一个方法，返回值直接是这个对象

			}
		}else if(pkType == 2){
			if(gameTime-this.attackTime>=50){
				attackTime=gameTime;
				// 将构造类的多个步骤封装成一个方法，返回值直接是这个对象

			}
		}else if(pkType == 3){
			if(gameTime-this.attackTime>=50){
				attackTime=gameTime;
				// 将构造类的多个步骤封装成一个方法，返回值直接是这个对象
				ElementObj element = new Bullet().createElement(this.toString());
				// 装入集合当中
				ElementManager.getManager().addElement(element, GameElement.BULLET);
			}
		}

	}
	@Override
	public String toString() {
		int x = this.getX();
		int y = this.getY();
		switch (this.fx) {
			case "left": y+=50; break;
			case "right": x+=100; y+=50; break;
		}

		// {x:3,y:1,f:right} json格式 弹药样式可拓展
		return "x:"+x+",y:"+y+",fx:"+this.fx;
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
}
