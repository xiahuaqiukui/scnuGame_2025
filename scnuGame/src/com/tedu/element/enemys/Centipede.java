package com.tedu.element.enemys;

import com.tedu.element.AttackCollider;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.util.List;

public class Centipede extends Enemy{

    private boolean attack1_time = false; // 控制第1种攻击状态的判定时间
    private long SkillOccupationTime=0;
    private int attack=4;//技能一的攻击力

    public Centipede(){
        setName("centipede");
        setEnemy_max_hp(10);
    }

    @Override
    protected void attack(long gameTime) {
        if(attack1_time){
            AttackCollider element = new AttackCollider(this.getX(), this.getY(),
                    this.getW(), this.getH(), null, this.fx, this.attack,
                    1, "enemy");
            element.fitAttackType();
            ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
            attack1_time=false;
        }
    }

    @Override
    protected void updateImage(long gameTime) {
        super.updateImage( gameTime);
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
        else if(Enemy_left_attack1||Enemy_right_attack1){
            canMove=false;
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_attack");
            if (attackPictureIndex == 3) {
                attack1_time = true; // 控制攻击1判定箱何时出现
            }
            if(attackPictureIndex==imageIcons.size()-1){
                attackPictureIndex=0;
                canMove=true;
                Enemy_left_attack1=false;
                Enemy_right_attack1=false;
            }

            attackPictureIndex%=imageIcons.size();
            ImageIcon t = imageIcons.get(attackPictureIndex);
            setIcon(imageIcons,attackPictureIndex);
            attackPictureIndex++;
        }

    }

    @Override
    protected void behavioralControl() {
        super.behavioralControl();
        if (target == null|| !target.isLive()) {
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
        if((targetX < currentX)&&(Math.abs(leftCollider.getX()-(targetX+target.getRectangle().getWidth()/2))<50)){
            // 目标在左边且在攻击1范围内
            Enemy_left_run=false;
            Enemy_left_attack1=true;
        }else if((targetX > currentX)&&(Math.abs(rightCollider.getX()-(targetX+target.getRectangle().getWidth()/2))<50)){
            // 目标在左边且在攻击1范围内
            Enemy_right_run = false;
            Enemy_right_attack1=true;
        }
    }
}
