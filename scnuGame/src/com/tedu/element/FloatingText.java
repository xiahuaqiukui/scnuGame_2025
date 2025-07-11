package com.tedu.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;


public class FloatingText extends ElementObj{
	private String text;
	private Color color;
	private int count = 50;
	
	public FloatingText(int x, int y, int w, int h, ImageIcon icon,String text, Color color) {
		super(x, y, w, h, icon);
		this.text = text;
		this.color = color;
	}
	
	@Override
	public void showElement(Graphics g) {
		if (this.count > 0) {
            g.setColor(this.color);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString(this.text, this.getX(), this.getY());
            
            this.setY(getY()-1); // 每帧向上飘浮
            this.count--;
        } else {
        	this.setLive(false);
        }
	}
}
