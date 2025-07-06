package com.tedu.element;

import com.tedu.manager.GameElement;

import java.awt.*;

public class Collider extends ElementObj {

    @Override
    public void showElement(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());
    }
    public Collider(){}
    public Collider(int x, int y, int w, int h) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
    }
}
