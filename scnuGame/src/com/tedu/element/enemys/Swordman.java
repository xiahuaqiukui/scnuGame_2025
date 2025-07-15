package com.tedu.element.enemys;

import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;

import java.util.Random;

public class Swordman extends Enemy {
    private boolean attack1_time = false;
    private boolean attack2_time = false;
    private boolean shot = false;
    private boolean lunge = false;

    public Swordman(){
        setName("swordman");
        setEnemy_max_hp(100);
    }

    @Override
    protected void attack(long gameTime) {

    }

    @Override
    protected void updateImage(long gameTime) {

    }

    @Override
    protected void behavioralControl() {
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
        int targetX = target.getX();
        int targeY = target.getY();
        int currentX = this.getX();
        int currentY = this.getY();

        if(isUsingSkill==false){
            Random rand  =  new Random();
            skillSeed=rand.nextInt(4);
        }
        /**
         *
         * 0:attack1
         * 1:attack2
         * 2:lunge
         * 3:shot
         * 4:ultimate_skill
         * */
        switch(skillSeed){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }


    }
}
