package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Medicine extends ElementObj{
	// 药瓶属性
	private String medType;// small_hp  small_mix
	
	private int small_recover_hp = 20;
	
	private int mix_small_recover_hp = 10;
	private int mix_small_recover_mp = 10;
	
	// 碰撞箱
	protected Collider topCollider;
    protected Collider bottomCollider;
    protected Collider leftCollider;
    protected Collider rightCollider;
	
    // 移动
    private final int g = 1;
    private int YSpeed = 0;
    private int MaxYSpeed = 5;
	
	public Medicine(int x, int y, int w, int h, ImageIcon icon) {
		super(x, y, w, h, icon);
		randomMedType();
		if (medType.equals("small_hp")) {
			this.setIcon(GameLoad.imgMaps.get("medicine1").get(0));
			this.setW(25);
			this.setH(25);
		}else if (medType.equals("small_mix")) {
			this.setIcon(GameLoad.imgMaps.get("medicine2").get(0));
			this.setW(25);
			this.setH(25);
		}
		
		System.out.println(this.getIcon());
		
		topCollider=new Collider(getX(), getY()-5,getW(),5);
        bottomCollider=new Collider(getX(), getY()+getH(),getW(),5);
        leftCollider=new Collider(getX()-5, getY(),5,getH());
        rightCollider=new Collider(getX()+this.getW(), getY(),5,getH());
        
        ElementManager.getManager().addElement(topCollider, GameElement.COLLIDER);
        ElementManager.getManager().addElement(bottomCollider,GameElement.COLLIDER);
        ElementManager.getManager().addElement(leftCollider,GameElement.COLLIDER);
        ElementManager.getManager().addElement(rightCollider,GameElement.COLLIDER);
	}
	
	public void randomMedType() {
		Random r = new Random();
		int t = r.nextInt(100);
		if (t<50) {
			medType = "small_hp";
		} else {
			medType = "small_mix";
		}
	}
	
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}
	
	// 药瓶的自然移动
	@Override
    protected void move(long gameTime) {
        MedicineYMove();
        YSpeed = Math.min(20,YSpeed+g);
    }
	
	protected void MedicineYMove(){
        if(!MedicineYMove(YSpeed)){
            YSpeed=0;
        }
    }
	
	protected boolean MedicineYMove(int YDistance) {
        Collider detectedCollision = null;
        if(YDistance>0){
            detectedCollision=bottomCollider;
        }else{
            detectedCollision=null;
        }

        ColliderMove(0,YDistance);

        if(detectedCollision!=null){
            if(detectedCollision.isCollided()){
                ColliderMove(0,-YDistance);
                return false;
            }else{
                this.setY(this.getY()+YDistance);
                return true;
            }
        }else{
            this.setY(this.getY()+YDistance);
            return true;
        }
    }
	
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
	
	@Override
    public void setLive(boolean live) {
        super.setLive(live);
        topCollider.setLive(live);
        bottomCollider.setLive(live);
        leftCollider.setLive(live);
        rightCollider.setLive(live);
    }
}
