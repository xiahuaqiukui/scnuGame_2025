package com.tedu.controller;

import java.util.List;
import java.util.Map;

import com.tedu.element.AttackCollider;
import com.tedu.element.Bullet;
import com.tedu.element.Collider;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.Medicine;
import com.tedu.element.Player1;
import com.tedu.element.Player2;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameOverPanel;
import com.tedu.show.GameStartMenu;

import javax.swing.*;

public class GameThread extends Thread{
	private ElementManager em;
	public static int sleepTime = 15;
	private int playerCount;
	private boolean gameRunning = true;

	public GameThread(int playerCount) {
		em = ElementManager.getManager();
		this.playerCount = playerCount;
	}

	@Override
	public void run() {
		gameLoad();
		gameRun();
		gameOver();
	}

	private void gameLoad() {
		System.out.println("game loading");
		GameLoad.ImgLoad();
		GameLoad.playerLoad(playerCount);
		GameLoad.MapLoad(playerCount == 1 ? 1 : 3);
	}

	private void gameRun() {
		System.out.println("game running");
		long gameTime = 0L;

		while (gameRunning) {
			Map<GameElement, List<ElementObj>> all = em.getGameElements();
			List<ElementObj> players = em.getElementsByKey(GameElement.PLAYER);

			if(players.isEmpty()) {
				gameRunning = false;
				break;
			}

			Update(all, gameTime);
			checkCollisions();
			gameTime++;

			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkCollisions() {
		List<ElementObj> players = em.getElementsByKey(GameElement.PLAYER);
		List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
		List<ElementObj> bullets = em.getElementsByKey(GameElement.BULLET);
		List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
		List<ElementObj> collider = em.getElementsByKey(GameElement.COLLIDER);
		List<ElementObj> AttackCollider = em.getElementsByKey(GameElement.ATTACKCOLLIDER);
		List<ElementObj> Medicine = em.getElementsByKey(GameElement.MEDICINE);

		ElementPK(enemys, bullets);
		ElementPK(enemys, AttackCollider);
		ElementPK(players, bullets);
		ElementPK(players, AttackCollider);
		ElementPK(players, Medicine);
		RemoveAttackCollider();
		ColliderCollided(collider, maps);
	}

	private void gameOver() {
		System.out.println("Game Over!");
		em.clearAllElements();
		SwingUtilities.invokeLater(() -> {
			GameJFrame currentFrame = (GameJFrame) SwingUtilities.getWindowAncestor(em.getGamePanel());
			currentFrame.getContentPane().removeAll();
			// 不再传递playerCount参数
			currentFrame.add(new GameOverPanel(currentFrame));
			currentFrame.revalidate();
			currentFrame.repaint();
		});
	}

	public void Update(Map<GameElement, List<ElementObj>> all, long gameTime) {
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge);
			for (int i = 0; i < list.size(); i++) {
				ElementObj obj = list.get(i);
				if(!obj.isLive()){
					obj.die();
					list.remove(i--);
					continue;
				}
				obj.model(gameTime, sleepTime);
			}
		}
	}

	public void ElementPK(List<ElementObj> listA, List<ElementObj> listB) {
		for(int i=0;i<listA.size();i++){
			ElementObj underAttack=listA.get(i);
			for(int j=0;j<listB.size();j++){
				ElementObj attack=listB.get(j);
				if(underAttack.pk(attack)){
					handleCollision(underAttack, attack);
					break;
				}
			}
		}
	}

	private void handleCollision(ElementObj underAttack, ElementObj attack) {
		if (attack instanceof Medicine) {
			handleMedicineCollision(underAttack, (Medicine) attack);
			return;
		}

		int hurt = 0;
		String from = "";
		if (attack instanceof AttackCollider) {
			AttackCollider t = (AttackCollider) attack;
			from = t.getFrom();
			hurt = t.getAttack();
		} else if (attack instanceof Bullet) {
			Bullet t = (Bullet) attack;
			from = t.getFrom();
			hurt = t.getAttack();
		}

		if (underAttack instanceof Player1) {
			Player1 t = (Player1) underAttack;
			if (from.equals("enemy") || from.equals("boss")) {
				t.getHurt(hurt);
				attack.setLive(false);
			}
		} else if (underAttack instanceof Player2) {
			Player2 t = (Player2) underAttack;
			if (from.equals("enemy") || from.equals("boss")) {
				t.getHurt(hurt);
				attack.setLive(false);
			}
		} else if (underAttack instanceof Enemy) {
			Enemy t = (Enemy) underAttack;
			if (from.equals("player1") || from.equals("player2")) {
				t.getHurt(hurt);
				attack.setLive(false);
			}
		}
	}

	private void handleMedicineCollision(ElementObj underAttack, Medicine medicine) {
		String medType = medicine.getMedType();
		if (underAttack instanceof Player1) {
			Player1 t = (Player1) underAttack;
			if (medType.equals("small_hp")) {
				t.gethpRecover(medicine.getRecoverHp());
			} else if (medType.equals("small_mix")){
				t.gethpRecover(medicine.getRecoverHp());
				t.getmpRecover(medicine.getRecoverMp());
			}
		} else if (underAttack instanceof Player2) {
			Player2 t = (Player2) underAttack;
			if (medType.equals("small_hp")) {
				t.gethpRecover(medicine.getRecoverHp());
			} else if (medType.equals("small_mix")){
				t.gethpRecover(medicine.getRecoverHp());
				t.getmpRecover(medicine.getRecoverMp());
			}
		}
		medicine.setLive(false);
	}

	public void ColliderCollided(List<ElementObj> listA, List<ElementObj> listB) {
		for(int i=0;i<listA.size();i++){
			ElementObj collider=listA.get(i);
			if(collider instanceof Collider){
				Collider A=(Collider) collider;
				boolean collided = false;
				for(int j=0;j<listB.size();j++){
					ElementObj B=listB.get(j);
					if(A.pk(B)){
						collided=true;
						break;
					}
				}
				A.setCollided(collided);
			}
		}
	}

	public void RemoveAttackCollider() {
		List<ElementObj> AttackCollider = em.getElementsByKey(GameElement.ATTACKCOLLIDER);
		for (ElementObj atkcol : AttackCollider) {
			atkcol.setLive(false);
		}
	}
}