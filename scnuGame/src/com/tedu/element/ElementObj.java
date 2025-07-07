package com.tedu.element;

import java.awt.*;

import javax.swing.ImageIcon;

/**
 * @说明 所有元素的基类。
 * @author renjj
 *
 */
public abstract class ElementObj {
	// 每个元素都有位置和图像属性
	private int x;
	private int y;
	private int w;
	private int h;
	protected ImageIcon icon;
	// 还有。。。。 各种必要的状态值，例如：是否生存.
	private boolean live=true;
	// 防止子类继承报错，没有其他意义
	public ElementObj() {}
	/**
	 * @说明 带参数的构造方法; 可以由子类传输数据到父类
	 * @param x    左上角X坐标
	 * @param y    左上角y坐标
	 * @param w    w宽度
	 * @param h    h高度
	 * @param icon  图片
	 */
	
	// 构造函数
	public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.icon = icon;
	}
	
	/**
	 * @说明 抽象方法，显示元素
	 * @param g  画笔 用于进行绘画
	 */
	public abstract void showElement(Graphics g);
	
	/**
	 * @说明 使用父类定义接收键盘事件的方法
	 * 	         只有需要实现键盘监听的子类，重写这个方法(约定)
	 * @说明 方式2 使用接口的方式;使用接口方式需要在监听类进行类型转换
	 * @题外话 约定  配置  现在大部分的java框架都是需要进行配置的.
	 *         约定优于配置
	 * @param bl   点击的类型  true代表按下，false代表松开
	 * @param key  代表触发的键盘的code值  
	 * @扩展 本方法是否可以分为2个方法？1个接收按下，1个接收松开(给同学扩展使用)
	 */
	public void keyClick(int bl, int key) {}
	
	/**
	 * @说明 移动方法; 需要移动的子类，请 重写这个方法
	 */
	protected void move() {}
	protected void updateImage(long gameTime) {}
	protected void attack(long gameTime) {}
	public void die(){}
	
	/**
	 * @设计模式 模板模式;在模板模式中定义 对象执行方法的先后顺序,由子类选择性重写方法
	 *        1.移动  2.换装  3.子弹发射
	 */
	// 模型操作
	public final void model(long gameTime) {


		// 移动
		move();
		// 换装
		updateImage(gameTime);
		// 攻击
		attack(gameTime);
	}
	
	public ElementObj createElement(String str) {
		
		return null;
	}

	public Rectangle getRectangle() {
		return new Rectangle(x,y,w,h);
	}
	//检测碰撞
	public boolean pk(ElementObj obj) {
		return this.getRectangle().intersects(obj.getRectangle());
	}
	
	
	/**
	 * 只要是 VO类 POJO 就要为属性生成 get和set方法
	 */
	// 所有父类，都要有get和set方式，方便子类操作。
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public ImageIcon getIcon() {
		return icon;
	}
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	public void setLive(boolean live) {this.live = live;}
	public boolean isLive(){
		return live;
	}
}
