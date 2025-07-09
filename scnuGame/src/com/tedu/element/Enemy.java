package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Enemy extends ElementObj{

    private String name="steamMan";
    private int hp;
    private int detectingDistance=250;
    private ElementObj target=null;
    private List<ElementObj> targetList=null;


    private long attackTime = 0L; // 攻击间隔
    private long attack1;

    private boolean Enemy_left_idle = false;
    private boolean Enemy_right_idle = true;
//    private boolean Enemy_left_walk = false;
//    private boolean Enemy_right_walk = false;
    private boolean Enemy_left_jump=false;
    private boolean Enemy_right_jump=false;
    private boolean Enemy_left_run=false;
    private boolean Enemy_right_run=false;


    private Collider topCollider;
    private Collider bottomCollider;
    private Collider leftCollider;
    private Collider rightCollider;
    private int maxXSpeed=3;
    private int maxYSpeed=-15;
    private int XSpeed=0;
    private int YSpeed=0;
    private int g=1;
    private int maxXRunSpeed=10;
    private long pictureTime=0L;

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
        getHurt(1);
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

    private void pathfinding() {
        if (target == null) {
            // 寻找目标（比如玩家）
            for(ElementObj tar:targetList){
                if(Math.abs(tar.getX()-this.getX())<detectingDistance){
                    target=tar;
                    return;
                }
                else{
                    Enemy_left_idle=true;
                }


            }
            return;
        }

        // 简单追踪逻辑
        int targetX = target.getX();
        int currentX = this.getX();


        if (targetX < currentX) {
            // 目标在左边

            if(leftCollider.getX()<targetX){}


            XSpeed = -maxXSpeed;
            Enemy_left_run = true;
            Enemy_right_run = false;
            Enemy_right_idle=false;
            Enemy_left_idle=false;
            fx="left";
        } else if (targetX > currentX) {
            // 目标在右边
            XSpeed = maxXSpeed;
            Enemy_right_run = true;
            Enemy_left_run = false;
            Enemy_right_idle=false;
            Enemy_left_idle=false;
            fx="right";
        } else {
            XSpeed = 0;
        }

    }

    @Override
    protected void move() {
        pathfinding();
        EnemyXMove();
        EnemyYMove();
        XSpeed=0;
        YSpeed=Math.min(20,YSpeed+g);
    }
    private void EnemyXMove(){
        if (this.Enemy_left_run && this.getX() > 0) {
            XSpeed=-maxXSpeed;
            ColliderMove( XSpeed,0);
            if(leftCollider.isCollided()){
                ColliderMove( -XSpeed,0);
                XSpeed=0;
                //Enemy_left_attack;
            }
        } else if (this.Enemy_right_run && this.getX() < 1200-this.getW()) {
            XSpeed=maxXSpeed;
            ColliderMove(XSpeed,0);
            if(rightCollider.isCollided()){
                ColliderMove( -XSpeed,0);
                XSpeed=0;
            }
        }
        this.setX(this.getX() + XSpeed);
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
        List<ImageIcon> imageIcons = null;
        if(gameTime-this.pictureTime>=10){
//            System.out.println(Enemy_left_idle);
//            System.out.println(Enemy_left_run);
            if(this.Enemy_left_idle||this.Enemy_right_idle){
                imageIcons = GameLoad.imgMaps.get(name+"_"+fx+"_idle");
                this.setIcon(imageIcons.get(pictureIndex));
                pictureIndex++;
                pictureIndex%=imageIcons.size();

            }else if (this.Enemy_left_run||this.Enemy_right_run) {
                imageIcons = GameLoad.imgMaps.get(name+"_"+fx+"_run");
//                System.out.println(imageIcons);
                this.setIcon(imageIcons.get(pictureIndex));
                pictureIndex++;
                pictureIndex%=imageIcons.size();
            }



            pictureTime = gameTime;
        }


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

    public List<ElementObj> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<ElementObj> targetList) {
        this.targetList = targetList;
    }
}
