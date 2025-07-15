package com.tedu.element.enemys;

import com.tedu.element.AttackCollider;
import com.tedu.element.Bullet;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class Swordman extends Enemy {
    private int attack=1;
    private int UltimateAttackTimes=0;//大招连段次数
    private int MaxUltimateAttackTimes=3;//大招连段次数

    //attack时间
    private boolean attack1_time = false;
    private boolean lunge_time = false;
    private boolean shot_time = false;
    private boolean ultimate_time = false;


    //状态
    private boolean shot = false;
    private boolean lunge = false;
    private boolean ultimate=false;
    private boolean ultimateAttacking=false;
    private boolean alreadyChargingUltimate=false;



    private long SkillOccupationTime=0;


    private int lungeStartX;
    private int lungeEndX;
    private int resilience;

//    private int[] point = getRandomPointOnBorder(GameJFrame.GameX, GameJFrame.GameY);
    private int startX=0;
    private int startY=0;
    private int endX=0;
    private int endY=0;



    public Swordman(){
        setName("swordman");
        setEnemy_max_hp(100);
        resilience = 20;


    }



    @Override
    public void getHurt(int demage) {
        super.getHurt(demage);
        resilience--;
    }

    @Override
    protected void attack(long gameTime) {
        if(attack1_time){
            AttackCollider element = new AttackCollider(this.getX(), this.getY(),
                    this.getW(), this.getH(), null, this.getFx(), this.attack,
                    1, "boss");
            element.fitAttackType();
            ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
            attack1_time=false;
        }else if (lunge_time){
            AttackCollider element = new AttackCollider(this.getX(), this.getY(),
                    this.getW(), this.getH(), null, this.getFx(), this.attack,
                    2, "boss");
            element.fitAttackType();
            ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
            lunge_time=false;
        }
        else if(shot_time){
            Bullet element = new Bullet(this.getX(), this.getY(),
                    this.getW(), this.getH(), null, this.attack,
                    15, this.getFx(), "boss","swordman_bullet");
            element.fitImage();
            ElementManager.getManager().addElement(element, GameElement.BULLET);
            shot_time = false;
        }else if (ultimate_time){

            //获取target的当前位置，
            // 画一条从屏幕外射向该位置的线
            if(attackPictureIndex==4){
                int[] point = getRandomPointOnBorder(GameJFrame.GameX, GameJFrame.GameY);
                startX= point[0];
                startY= point[1];
                endX=target.getX()+ target.getW()/2;
                endY=target.getY()+ target.getH()/2;
            }else if(attackPictureIndex>=5&&attackPictureIndex<=7){
                AttackCollider element = new AttackCollider(startX, startY,
                        5, this.getH(), null, this.getFx(), 0,
                        3, "boss",endX,endY);
                ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);
            }
            if(attackPictureIndex>7&&attackPictureIndex<=9){
                Bullet ele = new Bullet(startX,startY,30,30,attack*3,80,endX,endY,"boss");
                ElementManager.getManager().addElement(ele, GameElement.BULLET);
//                AttackCollider element = new AttackCollider(startX, startY,
//                        50, this.getH(), null, this.getFx(), attack*10,
//                        3, "boss",endX,endY);
//                ElementManager.getManager().addElement(element, GameElement.ATTACKCOLLIDER);

//                System.out.println(element.pk(target));
            }
            if(attackPictureIndex==10){
                ultimate_time=false;
            }

        }
    }

    @Override
    protected void updateImage(long gameTime) {
        super.updateImage(gameTime);
        if(Enemy_left_attack1||Enemy_right_attack1){
            canMove=false;
            canTurn=false;
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_attack1");
            if (attackPictureIndex == 2||attackPictureIndex==4) {
                attack1_time = true; // 控制攻击1判定箱何时出现
            }
            attackPictureIndex%=imageIcons.size();
            ImageIcon t = imageIcons.get(attackPictureIndex);
            setIcon(imageIcons,attackPictureIndex);
            //如果是最后一张图片
            if(attackPictureIndex==imageIcons.size()-1){
                isUsingSkill=false;
                attackPictureIndex=0;
                canMove=true;
                canTurn=true;
                Enemy_left_attack1=false;
                Enemy_right_attack1=false;
                int XDis = Math.max(Math.abs(this.getX())/2,Math.abs(GameJFrame.GameX-this.getX())/2);
                EnemyXMove(XDis-this.getX());
            }
            attackPictureIndex++;
        }
        else if(lunge){
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_lunge");

            if(attackPictureIndex==1||attackPictureIndex==2){
                lunge_time=true;
            }
            attackPictureIndex%=imageIcons.size();
            setIcon(imageIcons,attackPictureIndex);
            attackPictureIndex++;


        }else if(shot){
            canMove=false;
            canTurn=false;
            List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_shot");
            if(attackPictureIndex==1){
                shot_time=true;
            }
            if(attackPictureIndex==imageIcons.size()-1){
                attackPictureIndex=0;
                canMove=true;
                canTurn=true;
                shot=false;
                isUsingSkill=false;
                SkillOccupationTime=0;
            }
            attackPictureIndex%=imageIcons.size();
            setIcon(imageIcons,attackPictureIndex);
            attackPictureIndex++;
        }
        else if(ultimate){
            //没有充能动画
            if(!alreadyChargingUltimate){
                List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_ultimate_skill_charging");
                attackPictureIndex%=imageIcons.size();

                setIcon(imageIcons,attackPictureIndex);
                attackPictureIndex++;
                if(attackPictureIndex==imageIcons.size()){
                    attackPictureIndex=0;
                    alreadyChargingUltimate=true;
                }
            }else{
                List<ImageIcon> imageIcons = GameLoad.imgMaps.get(getName()+ "_ultimate_skill");
                attackPictureIndex%=imageIcons.size();
                setIcon(imageIcons,attackPictureIndex);
                attackPictureIndex++;
                if(attackPictureIndex>=4&&attackPictureIndex<=9){
                    ultimate_time=true;
                }
                if(attackPictureIndex==imageIcons.size()){
                    attackPictureIndex=0;
                    alreadyChargingUltimate=false;
                    UltimateAttackTimes++;
                }
            }
            //连段结束
            if(UltimateAttackTimes>MaxUltimateAttackTimes){
                ultimate=false;
                isUsingSkill=false;
                SkillOccupationTime=0;
                setG(1);
            }
        }
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
        int targetX = target.getX()+ target.getW()/2;
        int targeY = target.getY();
        int currentX = this.getX()+this.getW()/2;
        int currentY = this.getY();


        if(isUsingSkill==false){
            Random rand  =  new Random();
            skillSeed=rand.nextInt(4);
//            skillSeed=3;
        }

        switch(skillSeed){
            case 0://走过去砍两刀
                isUsingSkill=true;
                if ((targetX < currentX)&&(Math.abs(leftCollider.getX()-targetX)>80)) {
                    // 目标在左边且不在攻击范围内
                    XSpeed = -maxXSpeed;
                    Enemy_right_run = false;
                    Enemy_right_idle=false;
                    Enemy_left_idle=false;
                    Enemy_left_run = true;
                    setFx("left");
                } else if ((targetX > currentX)&&(Math.abs(rightCollider.getX()-targetX)>80)) {
                    // 目标在右边且不在攻击范围内
                    XSpeed = maxXSpeed;
                    Enemy_left_run = false;
                    Enemy_left_idle=false;
                    Enemy_right_idle=false;
                    Enemy_right_run = true;
                    setFx("right");
                }else{
                    Attack1(target);
                }
                break;
            case 1://远程攻击
                setG(1);
                SkillOccupationTime++;
                if(!isUsingSkill){
                    //初始化技能
                    isUsingSkill=true;
                    SkillOccupationTime=0;

                    int tempX=0;
                    if(targetX>GameJFrame.GameX-targetX){
                        tempX=targetX/2;
                        setFx("right");
                    }else{
                        tempX=targetX+(GameJFrame.GameX-targetX)/2;
                        setFx("left");
                    }
                    EnemyXMove(tempX-currentX);
                }
                Enemy_left_run=false;
                Enemy_right_run=false;
                Enemy_left_idle=false;
                Enemy_right_idle=false;
                shot=true;
                break;
            case 2://突刺
                SkillOccupationTime++;
                //初始化
                if(!isUsingSkill){

                    isUsingSkill=true;
                    SkillOccupationTime=0;
                    setG(0);//无重力

                    //选择突刺方向
                    if(targetX>GameJFrame.GameX-targetX){
                        lungeStartX=0+100;
                        lungeEndX=GameJFrame.GameX;
                    }else{
                        lungeStartX=GameJFrame.GameX-100;
                        lungeEndX=0;
                    }
                    //移动到突刺地点
                    EnemyXMove(lungeStartX-currentX);
                }
                if(SkillOccupationTime<10){
                    if(lungeEndX < currentX){
                        setFx("left");
                    }else if(lungeEndX > currentX){
                        setFx("right");
                    }
                    break;
                }
                lunge(target);
                break;
            case 3://大招
                SkillOccupationTime++;
                //初始化
                if(!isUsingSkill){

                    isUsingSkill=true;
                    SkillOccupationTime=0;
                    setG(0);//无重力
                    YSpeed=-1;

                    int tempX=GameJFrame.GameX/2-this.getW()/2;
                    EnemyXMove(tempX-currentX);
                }
                if(currentY<550){
                    YSpeed=0;
                }
                ultimate=true;
                break;
        }


    }
    /**
     *
     * @attack1:二段普通砍击，砍完瞬移走
     * @lunge:向一边突刺突刺
     * @shot:远离玩家，向玩家发射一道剑光
     * @ultimate_skill:当血量降低到50%以下，有概率释放。boss瞬移至屏幕中间，有全屏伤害
     * */

    private void Attack1(ElementObj target){
        setG(1);
        int targetX = target.getX()+ target.getW()/2;
        int targeY = target.getY();
        int currentX = this.getX()+this.getW()/2;
        int currentY = this.getY();


        if((targetX < currentX)&&(Math.abs(leftCollider.getX()-targetX)<=80)){
            // 目标在左边且在攻击范围内
            setFx("left");
            Enemy_left_run=false;
            Enemy_left_attack1=true;
        }else if((targetX > currentX)&&(Math.abs(rightCollider.getX()-targetX)<=80)){
            // 目标在右边且在攻击1范围内
            setFx("right");
            Enemy_right_run = false;
            Enemy_right_attack1=true;
        }
    }
    private void lunge(ElementObj target){
        lunge=true;
        int targetX = target.getX()+ target.getW()/2;
        int targeY = target.getY();
        int currentX = this.getX()+this.getW()/2;
        int currentY = this.getY();
        Enemy_left_run=false;
        Enemy_right_run = false;


        if(Math.abs(lungeEndX - currentX)<=50){
            XSpeed = 0;
            lunge=false;
            isUsingSkill=false;
            attackPictureIndex=0;
        } else if(lungeEndX < currentX){
            setFx("left");
            XSpeed = -50;
        }else if(lungeEndX > currentX){

            setFx("right");
            XSpeed = 50;
        }

    }

    public static int[] getRandomPointOnBorder(int width, int height) {
        Random random = new Random();
        int edge = random.nextInt(4);

        // 每条边的坐标范围
        int[][] edges = {
                {0, width, 0, 0},       // 上边 (x随机, y=0)
                {width, width, 0, height}, // 右边 (x=width, y随机)
                {0, width, height, height}, // 下边 (x随机, y=height)
                {0, 0, 0, height}        // 左边 (x=0, y随机)
        };

        int[] selectedEdge = edges[edge];
        int x = selectedEdge[0] + (selectedEdge[1] - selectedEdge[0]) * random.nextInt(1);
        int y = selectedEdge[2] + (selectedEdge[3] - selectedEdge[2]) * random.nextInt(1);

        return new int[]{x, y};
    }

}
