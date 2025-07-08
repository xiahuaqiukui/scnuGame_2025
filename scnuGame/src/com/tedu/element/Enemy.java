package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy extends ElementObj{

    private String name="steamMan";
    private int hp;
    private ElementObj target;

    private boolean Enemy_left_idle = false;
    private boolean Enemy_right_idle = true;
    private boolean Enemy_left_walk = false;
    private boolean Enemy_right_walk = false;
    private boolean Enemy_left_jump=false;
    private boolean Enemy_right_jump=false;
    private boolean Enemy_left_run=false;
    private boolean Enemy_right_run=false;
    private Collider topCollider;
    private Collider bottomCollider;
    private Collider leftCollider;
    private Collider rightCollider;
    private int maxXSpeed=1;
    private int maxYSpeed=-15;
    private int XSpeed=0;
    private int YSpeed=0;
    private int g=1;
    private int maxXRunSpeed=10;
    private long pictureTime=0L;
    private long attackTime=0L;
    private int pictureIndex=0;

    private String fx = "right";

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),
                this.getX(),this.getY(),
                this.getH(),this.getH(),null );
    }
    @Override
    public ElementObj createElement(String str) {
        /**
         * str格式：
         * ENEMY=x坐标,y坐标;
         *
         * */
        System.out.println(str);
        String []arr=str.split(",");
        ImageIcon icon=GameLoad.imgMaps.get(name+"_right_idle").get(0);
        this.setH(icon.getIconHeight());
        this.setW(icon.getIconWidth());
        this.setX(Integer.parseInt(arr[1]));
        this.setY(Integer.parseInt(arr[2]));
        this.setIcon(icon);
        ElementObj topCollider=new Collider(getX(), getY()-5,getW(),5);
        ElementObj bottomCollider=new Collider(getX(), getY()+100,getW(),5);
        ElementObj leftCollider=new Collider(getX()-5, getY(),5,getH()-15);
        ElementObj rightCollider=new Collider(getX()+100, getY(),5,getH()-15);
        setTopCollider((Collider) topCollider);
        setBottomCollider((Collider) bottomCollider);
        setLeftCollider((Collider) leftCollider);
        setRightCollider((Collider) rightCollider);

        ElementManager.getManager().addElement(topCollider, GameElement.COLLIDER);
        ElementManager.getManager().addElement(bottomCollider,GameElement.COLLIDER);
        ElementManager.getManager().addElement(leftCollider,GameElement.COLLIDER);
        ElementManager.getManager().addElement(rightCollider,GameElement.COLLIDER);
        return this;
    }

    public void getHurt(){
        hp--;
        if(hp<=0){
            setLive(false);
        }
    }
    public void getHurt(int demage){
        hp-=demage;
        if(hp<=0){
            setLive(false);
        }
    }

    @Override
    public void setLive(boolean live) {
        super.setLive(live);
        topCollider.setLive(live);
        bottomCollider.setLive(live);
        leftCollider.setLive(live);
        rightCollider.setLive(live);
    }

    public void pathfinding(){

    }

    @Override
    protected void move() {

        EnemyYMove();
        YSpeed=Math.min(20,YSpeed+g);
    }
    private void EnemyYMove(){
        //按下跳跃键
        if(this.Enemy_left_jump||this.Enemy_right_jump){
            this.Enemy_left_jump=false;
            this.Enemy_right_jump=false;
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

    @Override
    protected void updateImage(long gameTime, int sleepTime) {

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

    public Collider getTopCollider() {
        return topCollider;
    }

    public void setTopCollider(Collider topCollider) {
        this.topCollider = topCollider;
    }

    public Collider getRightCollider() {
        return rightCollider;
    }

    public void setRightCollider(Collider rightCollider) {
        this.rightCollider = rightCollider;
    }

    public Collider getLeftCollider() {
        return leftCollider;
    }

    public void setLeftCollider(Collider leftCollider) {
        this.leftCollider = leftCollider;
    }

    public Collider getBottomCollider() {
        return bottomCollider;
    }

    public void setBottomCollider(Collider bottomCollider) {
        this.bottomCollider = bottomCollider;
    }
}
