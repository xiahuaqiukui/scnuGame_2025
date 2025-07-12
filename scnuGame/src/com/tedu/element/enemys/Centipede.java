package com.tedu.element.enemys;

import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.util.List;

public class Centipede extends Enemy{


    public Centipede(){
        setName("centipede");
    }

    @Override
    protected void updateImage() {
        super.updateImage();
        if(Enemy_left_attack1||Enemy_right_attack1){
            canMove=false;
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_attack");
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
