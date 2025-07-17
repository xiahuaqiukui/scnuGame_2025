package com.tedu.element;

import com.tedu.manager.GameElement;

import java.awt.*;

public class Collider extends ElementObj {
    private boolean isCollided=false;

    @Override
    public void showElement(Graphics g) {
//        g.setColor(Color.blue);
//        g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());
    }
    public Collider(){}
    public Collider(int x, int y, int w, int h) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
    }

    public boolean isCollided() {
        return isCollided;
    }

    public void setCollided(boolean collided) {
        isCollided = collided;
    }
}
