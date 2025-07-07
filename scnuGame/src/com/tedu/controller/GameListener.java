package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

public class GameListener implements KeyListener {
    ElementManager em = ElementManager.getManager();

    // 记录每个键的上一次按下/释放时间
    private Map<Integer, Long> keyFirstPressedTime = new HashMap<>();
    private Map<Integer, Long> keyLastReleasedTime = new HashMap<>();
    private Set<Integer> pressedKeys = new HashSet<>();
    private Set<Integer> runningKeys = new HashSet<>();

    // 时间阈值
    private final int DOUBLECLICK_THRESHOLD = 300;  // 双击时间间隔

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        long now = System.currentTimeMillis();
        List<ElementObj> players = em.getElementsByKey(GameElement.PLAYER);

        // 如果已经在奔跑状态，持续奔跑
        if (runningKeys.contains(key)) {
            for (ElementObj obj : players) {
                obj.keyClick(2, key); // 持续奔跑
            }
            return;
        }

        // 如果这个键是首次按下（不是长按重复）
        if (!pressedKeys.contains(key)) {
            pressedKeys.add(key);

            // 判断是否为双击：第一次按下、释放、第二次按下都要记录
            if (keyFirstPressedTime.containsKey(key) && keyLastReleasedTime.containsKey(key)) {
                long down1 = keyFirstPressedTime.get(key);
                long up1 = keyLastReleasedTime.get(key);
                long down2 = now;

                if ((down2 - down1) <= DOUBLECLICK_THRESHOLD && (up1 - down1) <= DOUBLECLICK_THRESHOLD) {
                    // 满足双击条件
                    runningKeys.add(key);
                    for (ElementObj obj : players) {
                        obj.keyClick(2, key); // 开始奔跑
                    }
                    return;
                }
            }

            // 如果不是双击，就作为普通按键处理
            keyFirstPressedTime.put(key, now);
            for (ElementObj obj : players) {
                obj.keyClick(1, key); // 正常移动
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        long now = System.currentTimeMillis();
        List<ElementObj> players = em.getElementsByKey(GameElement.PLAYER);

        pressedKeys.remove(key);
        runningKeys.remove(key);
        keyLastReleasedTime.put(key, now);

        for (ElementObj obj : players) {
            obj.keyClick(0, key); // 停止动作
        }
    }
}
