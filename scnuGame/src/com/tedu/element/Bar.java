package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Bar extends ElementObj{
	private int maxNum;
	private int nowNum;
	private Color color;
	
	public Bar() {}
	
	public Bar(int x, int y, int w, int h, ImageIcon icon, int maxNum, int nowNum, Color color) {
		super(x, y, w, h, icon);
		this.maxNum = maxNum;
		this.nowNum = nowNum;
		this.color = color;
	}
	
	@Override
    public void showElement(Graphics g) {
		g.setColor(this.color);
		float per = (float)nowNum / (float)maxNum;
//		System.out.println(per);
		int realNumW = (int) (per * this.getW());
		g.fillRect(this.getX(), this.getY(), realNumW, this.getH());
		
		g.setColor(Color.GRAY);
		g.fillRect(this.getX() + realNumW, this.getY(), this.getW() - realNumW, this.getH());
		
		// 3. 画文本（居中显示 nowNum / maxNum）
	    String text = nowNum + " / " + maxNum;
	    g.setColor(Color.BLACK);
	    int textWidth = g.getFontMetrics().stringWidth(text);
	    int textHeight = g.getFontMetrics().getAscent();

	    // 计算居中位置
	    int textX = this.getX() + (this.getW() - textWidth) / 2;
	    int textY = this.getY() + (this.getH() + textHeight) / 2 - 2;

	    g.drawString(text, textX, textY);
    }
	
	public void setNowNum(int nowNum) {
		this.nowNum = nowNum;
	}
}
