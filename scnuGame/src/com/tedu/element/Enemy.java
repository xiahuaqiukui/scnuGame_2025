package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends ElementObj{

    /// 加载使用的属性
    private String name="";
    protected List<ElementObj> targetList=null;
    private long pictureTime=0L;
    protected int pictureIndex=0;
    protected int attackPictureIndex=0;

    ///敌人基本属性
    private int enemy_max_hp = 10; // 血量
    private int enemy_hp = enemy_max_hp;
    protected int detectingDistance=250;
    protected ElementObj target=null;
    private long attackTime = 0L; // 攻击间隔
    protected int maxXSpeed=3;
    protected int maxYSpeed=-15;
    protected int XSpeed=0;
    protected int YSpeed=0;

    /// 状态控制
    protected String fx = "right";
    protected boolean ishurt = false;
    protected boolean Enemy_left_idle = false;
    protected boolean Enemy_right_idle = true;
//    private boolean Enemy_left_walk = false;
//    private boolean Enemy_right_walk = false;
    protected boolean Enemy_left_jump=false;
    protected boolean Enemy_right_jump=false;
    protected boolean Enemy_left_run=false;
    protected boolean Enemy_right_run=false;
    protected boolean Enemy_left_attack1=false;
    protected boolean Enemy_right_attack1=false;
    protected boolean Enemy_left_attack2=false;
    protected boolean Enemy_right_attack2=false;

    /// 碰撞箱
    protected Collider topCollider;
    protected Collider bottomCollider;
    protected Collider leftCollider;
    protected Collider rightCollider;


    /// 其他基本配置
    private int g=1;
    protected boolean canMove=true;
    protected boolean isUsingSkill;
    /**
     * 通过随机数决定使用哪种攻击方式
     */
    protected int skillSeed;

    @Override
    public void showElement(Graphics g) {
        Image img = this.getIcon().getImage();
        int drawX = this.getX();
        int drawY = this.getY();
        int drawWidth = this.getW();
        int drawHeight = this.getH();

        if ("left".equals(fx)) {
            // 朝向左边时，固定右下角坐标 (drawX + drawWidth, drawY + drawHeight)
            g.drawImage(img,
                    drawX + drawWidth, drawY,  // 绘制起点在右下角
                    -drawWidth, drawHeight,   // 负宽度实现水平翻转
                    null);
        } else {
            // 朝向右边时，正常绘制
            g.drawImage(img,
                    drawX, drawY,
                    drawWidth, drawHeight,
                    null);
        }
    }





    @Override
    public ElementObj createElement(String str) {
        /**
         * str格式：
         * ENEMY=x坐标,y坐标;
         *
         * */
//        System.out.println(str);
        String []arr=str.split(",");
        ImageIcon icon=GameLoad.imgMaps.get(name+"_idle").get(0);
        this.setH(icon.getIconHeight()*2);
        this.setW(icon.getIconHeight()*2);
        this.setX(Integer.parseInt(arr[1]));
        this.setY(Integer.parseInt(arr[2]));
        this.setIcon(icon);
        ElementObj topCollider=new Collider(getX(), getY()-5,getW(),5);
        ElementObj bottomCollider=new Collider(getX(), getY()+getH(),getW(),5);
        ElementObj leftCollider=new Collider(getX()-5, getY(),5,getH()-15);
        ElementObj rightCollider=new Collider(getX()+this.getW(), getY(),5,getH()-15);
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
        enemy_hp-=demage;
        
        // 设置在敌人图像范围内随机弹出
        int fx = (int) (this.getX() + (Math.random()*this.getW()));
        int fy = (int) (this.getY() + (Math.random()*this.getH()));
        
        // 添加飘字效果
        ElementObj element = new FloatingText(fx, fy, this.getW(), this.getH()
        		, null, new String("-"+demage), Color.RED);
		ElementManager.getManager().addElement(element, GameElement.FLOATINGTEXT);
        
        if(enemy_hp<=0){
            setLive(false);
        }
        canMove=false;
        ishurt=true;
    }

    @Override
    public void setLive(boolean live) {
        super.setLive(live);
        topCollider.setLive(live);
        bottomCollider.setLive(live);
        leftCollider.setLive(live);
        rightCollider.setLive(live);
    }

    ///具体敌人需要重写
    protected void behavioralControl() {
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
        if ((targetX < currentX)&&(Math.abs(leftCollider.getX()-(targetX+target.getRectangle().getWidth()/2))>=50)) {
            // 目标在左边且不在攻击范围内
            XSpeed = -maxXSpeed;
            Enemy_right_run = false;
            Enemy_right_idle=false;
            Enemy_left_idle=false;
            Enemy_left_run = true;
            fx="left";
        } else if ((targetX > currentX)&&(Math.abs(rightCollider.getX()-(targetX+target.getRectangle().getWidth()/2))>=50)) {
            // 目标在右边且不在攻击范围内
            XSpeed = maxXSpeed;
            Enemy_left_run = false;
            Enemy_right_idle=false;
            Enemy_left_idle=false;
            Enemy_right_run = true;
            fx="right";
        }

    }

    @Override
    protected void move(long gameTime) {


        behavioralControl();
        if(canMove){
            EnemyXMove();
            EnemyYMove();
        }
        XSpeed=0;
        YSpeed=Math.min(20,YSpeed+g);
    }

    private int EnemyMove(int XDistance, int YDistance) {
        int result=0;
        if(EnemyXMove(XDistance)){
            result+=1;
        }
        if(EnemyYMove(YDistance)){
            result+=2;
        }
        return result;
    }

    private boolean EnemyXMove(int XDistance) {
        Collider detectedCollision=null;
        if(XDistance>0){
            detectedCollision=rightCollider;
        }else{
            detectedCollision=leftCollider;
        }

        ColliderMove(XDistance,0);

        if(detectedCollision!=null){
            if(detectedCollision.isCollided()){
                ColliderMove(-XDistance,0);
                return false;
            }else{
                this.setX(this.getX()+XDistance);
                return true;
            }
        }else{
            System.out.println("ERROR：碰撞箱检测出错");
            return false;
        }

    }
    //YDistance正数表示向下边移动
    private boolean EnemyYMove(int YDistance) {
        Collider detectedCollision=null;
        if(YDistance>0){
            detectedCollision=bottomCollider;
        }else{
            //向上移动不予检测
            detectedCollision=null;
        }

        ColliderMove(0,YDistance);

        if(detectedCollision!=null){
            //向下移动
            if(detectedCollision.isCollided()){
                ColliderMove(0,-YDistance);
                return false;
            }else{
                this.setY(this.getY()+YDistance);
                return true;
            }
        }else{
            //向上移动不予检测，直接执行
            this.setY(this.getY()+YDistance);
            return true;
        }
    }
    private void EnemyXMove(){
        if (this.Enemy_left_run && this.getX() > 0) {
            XSpeed=-maxXSpeed;
//            ColliderMove( XSpeed,0);
//            if(leftCollider.isCollided()){
//                ColliderMove( -XSpeed,0);
//                XSpeed=0;
//                //Enemy_left_attack;
//            }
        } else if (this.Enemy_right_run && this.getX() < 1200-this.getW()) {
            XSpeed=maxXSpeed;
//            ColliderMove(XSpeed,0);
//            if(rightCollider.isCollided()){
//                ColliderMove( -XSpeed,0);
//                XSpeed=0;
//            }
        }
//        this.setX(this.getX() + XSpeed);
        if(!EnemyXMove(XSpeed)){
            XSpeed=0;
        }
    }
    private void EnemyYMove(){
        //按下跳跃键
        if(this.Enemy_left_jump||this.Enemy_right_jump){
            this.Enemy_left_jump=false;
            this.Enemy_right_jump=false;
            YSpeed=maxYSpeed;
        }

        //默认下落
//        ColliderMove( 0,YSpeed);
//        if(YSpeed>0){
//            if(bottomCollider.isCollided()){
//                ColliderMove( 0,-YSpeed);
//                YSpeed=0;
//            }
//        }

//        this.setY(this.getY() + YSpeed);
        if(!EnemyYMove(YSpeed)){
            YSpeed=0;
        }

    }

    @Override
    protected void updateImage(long gameTime, int sleepTime) {
        if(gameTime-this.pictureTime>=8){
            updateImage(gameTime);
            pictureTime = gameTime;
        }
    }
    ///具体敌人需要重写
    protected void updateImage(long gameTime){
        if(this.Enemy_left_idle||this.Enemy_right_idle){
            //左右待机动画
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(name+"_idle");
            pictureIndex%=imageIcons.size();
            setIcon(imageIcons,pictureIndex);
            pictureIndex++;

        }else if (this.Enemy_left_run||this.Enemy_right_run) {
            //左右跑步动画

            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(name+"_run");
            pictureIndex%=imageIcons.size();
            setIcon(imageIcons,pictureIndex);
            pictureIndex++;
        }
    }
    /**
     * 移动所有碰撞箱（统一使用此方法移动碰撞箱）
     * @param deltaX X轴移动距离（正数向右，负数向左）
     * @param deltaY Y轴移动距离（正数向下，负数向上）
     */
    private void ColliderMove(int deltaX, int deltaY) {
        // 移动顶部碰撞箱
        topCollider.setX(topCollider.getX() + deltaX);
        topCollider.setY(topCollider.getY() + deltaY);

        // 移动底部碰撞箱
        bottomCollider.setX(bottomCollider.getX() + deltaX);
        bottomCollider.setY(bottomCollider.getY() + deltaY);

        // 移动左侧碰撞箱
        leftCollider.setX(leftCollider.getX() + deltaX);
        leftCollider.setY(leftCollider.getY() + deltaY);

        // 移动右侧碰撞箱
        rightCollider.setX(rightCollider.getX() + deltaX);
        rightCollider.setY(rightCollider.getY() + deltaY);
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

    public int getEnemy_max_hp() {
        return enemy_max_hp;
    }

    public void setEnemy_max_hp(int enemy_max_hp) {
        this.enemy_max_hp = enemy_max_hp;
    }

    public void setTargetList(List<ElementObj> targetList) {
        this.targetList = targetList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void setIcon(List<ImageIcon> imageIcons, int index) {
        ImageIcon t = imageIcons.get(index);
        this.setIcon(t);

        int newWidth = t.getIconWidth() * 2;
        int newHeight = t.getIconHeight() * 2;

        // 记录旧的右下角坐标
        int oldRightX = this.getX() + this.getW();
        int oldBottomY = this.getY() + this.getH();

        // 更新宽高
        this.setW(newWidth);
        this.setH(newHeight);

        // 调整坐标，使右下角不变
        if ("left".equals(fx)) {
            this.setX(oldRightX - newWidth);  // 新X = 旧右下角X - 新宽度
        }
        this.setY(oldBottomY - newHeight);    // 新Y = 旧底部Y - 新高度

        // 更新碰撞箱
        updateColliders();
    }


    private void updateColliders() {
        int x = this.getX();
        int y = this.getY();
        int w = this.getW();
        int h = this.getH();

        // 更新碰撞箱（跟随缩放后的位置）
        topCollider.setX(x);
        topCollider.setY(y - 5);       // 顶部碰撞箱（稍微超出）
        topCollider.setW(w);
        topCollider.setH(5);

        bottomCollider.setX(x);
        bottomCollider.setY(y + h);    // 底部碰撞箱（保持底部不变）
        bottomCollider.setW(w);
        bottomCollider.setH(5);

        leftCollider.setX(x - 5);
        leftCollider.setY(y);
        leftCollider.setW(5);
        leftCollider.setH(h - 15);     // 稍微减少高度，避免脚部碰撞太敏感

        rightCollider.setX(x + w);
        rightCollider.setY(y);
        rightCollider.setW(5);
        rightCollider.setH(h - 15);
    }

}
