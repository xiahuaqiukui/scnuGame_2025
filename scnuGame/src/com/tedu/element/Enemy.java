package com.tedu.element;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy extends ElementObj{

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),
                this.getX(),this.getY(),
                this.getH(),this.getH(),null );
    }
    @Override
    public ElementObj createElement(String str) {
        System.out.println(str);
        String []arr=str.split(",");
        ImageIcon icon=null;
        switch (arr[0]){
            case "ENEMY":icon=new ImageIcon("image/enemy/enemy.png");break;
        }
        int x=Integer.parseInt(arr[1]);
        int y=Integer.parseInt(arr[2]);
        int w=icon.getIconWidth();
        int h=icon.getIconHeight();
        System.out.println("W:"+w+" H:"+h);
        this.setH(h);
        this.setW(w);
        this.setX(x);
        this.setY(y);
        this.setIcon(icon);
        return this;
    }
}
