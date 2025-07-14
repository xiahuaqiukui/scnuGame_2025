package com.tedu.element.enemys;

import com.tedu.element.AttackCollider;
import com.tedu.element.Bullet;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class Battle_turtle extends Enemy{

    private boolean attack1_time = false; // 控制第1种攻击状态的判定时间
    private boolean attack2_time = false; // 控制第1种攻击状态的判定时间
    private long SkillOccupationTime=0;
    private int attack=1;

    public Battle_turtle(){
        setName("battle_turtle");
        setEnemy_max_hp(20);
    }


    @Override
    protected void attack(long gameTime) {
        if(attack1_time){
            if(attackPictureIndex==2){
                Bullet element = new Bullet(this.getX(), this.getY(),
                        this.getW(), this.getH(), null, this.attack * 2,
                        10, this.fx, "enemy","battle_turtle_bullet1");
                element.fitImage();
                ElementManager.getManager().addElement(element, GameElement.BULLET);
                attack1_time = false;
            }else if(attackPictureIndex==3){
                Bullet element = new Bullet(this.getX(), this.getY(),
                        this.getW(), this.getH(), null, this.attack * 2,
                        10, this.fx, "enemy","battle_turtle_bullet1");
                element.fitImage();
                ElementManager.getManager().addElement(element, GameElement.BULLET);
                attack1_time = false;
            }



        }else if(attack2_time){
            AttackCollider element = new AttackCollider(this.getX(), this.getY(),
                    this.getW(), this.getH(), null, this.fx, this.attack,
                    1, "enemy");
            element.fitAttackType();
            ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
            attack2_time=false;

        }
    }

    @Override
    protected void updateImage(long gameTime) {
        super.updateImage(gameTime);
        if(ishurt==true){
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+"_hurt");
            pictureIndex%=imageIcons.size();
            setIcon(imageIcons,pictureIndex);
            pictureIndex++;
            if(attackPictureIndex==imageIcons.size()-1){
                attackPictureIndex=0;
                canMove=true;
                ishurt=false;
                isUsingSkill=false;
            }
            attackPictureIndex%=imageIcons.size();
            ImageIcon t = imageIcons.get(attackPictureIndex);
            setIcon(imageIcons,attackPictureIndex);
            attackPictureIndex++;
        }
        else if(Enemy_left_attack2||Enemy_right_attack2){
            SkillOccupationTime=0;
            canMove=false;
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_attack2");
            if (attackPictureIndex == 3) {
                attack2_time = true; // 控制攻击1判定箱何时出现
            }
            if(attackPictureIndex==imageIcons.size()-1){
                attackPictureIndex=0;
                canMove=true;
                Enemy_left_attack2=false;
                Enemy_right_attack2=false;
                isUsingSkill=false;
            }
            attackPictureIndex%=imageIcons.size();
            ImageIcon t = imageIcons.get(attackPictureIndex);
            setIcon(imageIcons,attackPictureIndex);
            attackPictureIndex++;
        }
        else if(Enemy_left_attack1||Enemy_right_attack1){
            SkillOccupationTime=0;
            canMove=false;
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_attack1");
//            System.out.println(imageIcons);
            if (attackPictureIndex == 2||attackPictureIndex==3) {
                attack1_time = true; // 控制攻击1判定箱何时出现
            }
            if(attackPictureIndex==imageIcons.size()-1){
                attackPictureIndex=0;
                canMove=true;
                Enemy_left_attack1=false;
                Enemy_right_attack1=false;
                isUsingSkill=false;
                SkillOccupationTime=0;
            }

            attackPictureIndex%=imageIcons.size();
            ImageIcon t = imageIcons.get(attackPictureIndex);
            setIcon(imageIcons,attackPictureIndex);
            attackPictureIndex++;
        }

    }

    @Override
    protected void behavioralControl() {
//        super.behavioralControl();
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
        int targeY = target.getY();
        int currentX = this.getX();
        int currentY = this.getY();

        if(SkillOccupationTime>100)isUsingSkill=false;
        if(isUsingSkill==false){
            Random rand = new Random();
            skillSeed=rand.nextInt(4);
//            skillSeed=0;

        }

        switch(skillSeed){
            case 0:
                if(Math.abs(targeY-currentY)>=50 ){break;}
                SkillOccupationTime++;
                isUsingSkill=true;
                if ((targetX < currentX)&&(Math.abs(leftCollider.getX()-(targetX+target.getRectangle().getWidth()/2))<=100)){
                    XSpeed = maxXSpeed;
                    Enemy_left_run = false;
                    Enemy_left_idle=false;
                    Enemy_right_idle=false;
                    Enemy_right_run = true;
                    fx="right";
                }else if ((targetX >= currentX)&&(Math.abs(rightCollider.getX()-(targetX+target.getRectangle().getWidth()/2))<=100)){
                    SkillOccupationTime++;
                    XSpeed = maxXSpeed;
                    Enemy_right_run = false;
                    Enemy_right_idle=false;
                    Enemy_left_idle=false;
                    Enemy_left_run = true;
                    fx="left";
                }else if((targetX < currentX)&&(Math.abs(leftCollider.getX()-(targetX+target.getRectangle().getWidth()/2))>=100)){
                    // 目标在左边
                    fx="left";
                    Enemy_left_run=false;
                    Enemy_left_attack1=true;
                }else if((targetX >= currentX)&&(Math.abs(rightCollider.getX()-(targetX+target.getRectangle().getWidth()/2))>=100)){
                    // 目标在右边
                    fx="right";
                    Enemy_right_run = false;
                    Enemy_right_attack1=true;
                }
                break;
            case 1:
                isUsingSkill=true;
                if ((targetX < currentX)&&(Math.abs(leftCollider.getX()-(targetX+target.getRectangle().getWidth()/2))>=30)) {
                    // 目标在左边且不在攻击范围内
                    XSpeed = -maxXSpeed;
                    Enemy_right_run = false;
                    Enemy_right_idle=false;
                    Enemy_left_idle=false;
                    Enemy_left_run = true;
                    fx="left";
                } else if ((targetX > currentX)&&(Math.abs(rightCollider.getX()-(targetX+target.getRectangle().getWidth()/2))>=30)) {
                    // 目标在右边且不在攻击范围内
                    XSpeed = maxXSpeed;
                    Enemy_left_run = false;
                    Enemy_left_idle=false;
                    Enemy_right_idle=false;
                    Enemy_right_run = true;
                    fx="right";
                }
                else if((targetX < currentX)&&(Math.abs(leftCollider.getX()-(targetX+target.getRectangle().getWidth()/2))<30)){
                    // 目标在左边且在攻击1范围内
                    fx="left";
                    Enemy_left_run=false;
                    Enemy_left_attack2=true;
                }else if((targetX > currentX)&&(Math.abs(rightCollider.getX()-(targetX+target.getRectangle().getWidth()/2))<30)){
                    // 目标在右边且在攻击1范围内
                    fx="right";
                    Enemy_right_run = false;
                    Enemy_right_attack2=true;
                }
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }




}
