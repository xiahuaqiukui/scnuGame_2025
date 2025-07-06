package com.tedu.show;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @说明 游戏窗体 主要实现的功能：关闭，显示，最大最小化
 * @author renjj
 * @功能说明   需要嵌入面板,启动主线程等等
 * @窗体说明  swing awt  窗体大小（记录用户上次使用软件的窗体样式）
 * 
 * @分析 1.面板绑定到窗体
 *       2.监听绑定
 *       3.游戏主线程启动
 *       4.显示窗体
 */

public class GameJFrame extends JFrame{
	public static int GameX = 1700; // 横向长度
	public static int GameY = 920;  // 纵向长度
	private JPanel jPanel = null;   // 正在显示的面板
	private KeyListener keyListener = null;  // 键盘监听器
	private MouseMotionListener mouseMotionListener=null;  //鼠标监听
	private MouseListener mouseListener=null;
	private Thread mainThread = null;  // 游戏主线程
	
	// 构造函数+初始化
	public GameJFrame() {
		init();
	}
	
	// 初始化
	public void init() {
		this.setSize(GameX, GameY); // 游戏窗口大小设置
		this.setTitle("scnuGame_2025");  // 游戏标题
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置退出并关闭
		this.setLocationRelativeTo(null); // 屏幕居中
	}

	// 窗体布局
	/*存档，读档。button   给大家扩展的*/
	public void addButton() {
//		this.setLayout(manager);//布局格式，可以添加控件
	}
	
	// 窗口启动方法---启动所有元素
	public void start() {
		if(jPanel!=null) {
			this.add(jPanel);
		}
		if(keyListener !=null) {
			this.addKeyListener(keyListener);
		}
		if(mainThread !=null) {
			mainThread.start(); // 启动线程
		}
		// 显示界面
		this.setVisible(true);
		
		// 界面的刷新
		if (this.jPanel instanceof Runnable) {
			new Thread((Runnable)this.jPanel).start();
		}
	}
	
	
	/*set注入：等大家学习ssm 通过set方法注入配置文件中读取的数据;讲配置文件
	 * 中的数据赋值为类的属性
	 * 构造注入：需要配合构造方法
	 * spring 中ioc 进行对象的自动生成，管理。
	 * */
	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}
	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}
	public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
		this.mouseMotionListener = mouseMotionListener;
	}
	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}
	public void setMainThread(Thread mainThread) {
		this.mainThread = mainThread;
	}
}
