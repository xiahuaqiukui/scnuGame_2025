package com.tedu.element;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.manager.GameLoad;

public class Player extends ElementObj{
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
	private boolean left = false;
	private boolean up = false;
	private boolean right = false;
	private boolean down = false;
	
	// 变量，记录当前主角朝向(默认为右)
	private String fx = "right";
	
	// 初始化玩家属性，目前仅继承父类属性
	public Player(int x, int y, int w, int h, ImageIcon icon) {
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
	
	
	/**
	 * @说明 重写方法： 重写的要求：方法名称 和参数类型序列 必须和父类的方法一样
	 * @重点 监听的数据需要改变状态值
	 */
	// 重写keyClick函数，监听键盘操作，改变玩家方向状态
	@Override
	public void keyClick(boolean bl, int key) {
		if (bl) {
			/**
			 * @移动控制 移动方式改变
			 */
			switch (key) {
			case 65: this.right=false; this.left = true; this.fx = "left"; 	break; // 左
			case 87: this.down=false; this.up = true; this.fx = "up"; 		break; // 上
			case 68: this.left=false; this.right = true; this.fx = "right"; break; // 右
			case 83: this.up=false; this.down = true; this.fx = "down"; 	break; // 下
			}
		} else {
			switch (key) {
			case 65: this.left = false; 	break; // 左
			case 87: this.up = false; 		break; // 上
			case 68: this.right = false; 	break; // 右
			case 83: this.down = false; 	break; // 下
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
		if (this.left && this.getX() > 0) {
			this.setX(this.getX() - 10);
		}
		if (this.up && this.getY() > 0) {
			this.setY(this.getY() - 10);
		}
		if (this.right && this.getX() < 1200-this.getW()) {
			this.setX(this.getX() + 10);
		}
		if (this.down && this.getY() < 800-this.getH()-50) {
			this.setY(this.getY() + 10);
		}
	}
	
	@Override
	protected void updateImage() {
//		ImageIcon icon=GameLoad.imgMap.get(fx);
//		System.out.println(icon.getIconHeight());//得到图片的高度
//		如果高度是小于等于0 就说明你的这个图片路径有问题
		
		// 从数据加载器中加载图片
		this.setIcon(GameLoad.imgMap.get(fx));
	}
}
