package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

public class GameListener implements KeyListener {
    ElementManager em = ElementManager.getManager();

    private Map<Integer, Long> keyFirstPressedTime = new HashMap<>();
    private Map<Integer, Long> keyLastReleasedTime = new HashMap<>();

    // 当前已经按住的键
    private Set<Integer> pressedKeys = new HashSet<>();

    // 当前正在奔跑状态的键
    private Set<Integer> runningKeys = new HashSet<>();

    private final int DOUBLECLICK_THRESHOLD = 300;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        long now = System.currentTimeMillis();
        List<ElementObj> players = em.getElementsByKey(GameElement.PLAYER);

        // 如果是重复 keyPressed（长按造成的），跳过处理
        if (pressedKeys.contains(key)) {
            return;
        }

        pressedKeys.add(key); // 第一次真正按下

        // 是否满足双击条件
        if (keyFirstPressedTime.containsKey(key) && keyLastReleasedTime.containsKey(key)) {
            long down1 = keyFirstPressedTime.get(key);
            long up1 = keyLastReleasedTime.get(key);
            long down2 = now;

            if ((down2 - down1) <= DOUBLECLICK_THRESHOLD && (up1 - down1) <= DOUBLECLICK_THRESHOLD) {
                runningKeys.add(key); // 进入奔跑状态
                for (ElementObj obj : players) {
                    obj.keyClick(2, key); // 只触发一次
                }
                return;
            }
        }

        // 普通单击移动
        keyFirstPressedTime.put(key, now);
        for (ElementObj obj : players) {
            obj.keyClick(1, key); // 只触发一次
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        long now = System.currentTimeMillis();
        List<ElementObj> players = em.getElementsByKey(GameElement.PLAYER);

        pressedKeys.remove(key);      // 松开该键
        runningKeys.remove(key);      // 停止奔跑状态
        keyLastReleasedTime.put(key, now);

        for (ElementObj obj : players) {
            obj.keyClick(0, key); // 停止动作
        }
    }
}
