package net.jmecn.snake.core;

import com.jme3.math.Quaternion;

public interface SnakeConstants {

	int width = 1280;
	int height = 720;
	int UNIT = 8;
	
	int foodMinCount = 200;// 食物的最少数量
	int snakeMinLength = 30;// 蛇的最短长度
	
	int foodRadius = 3;
	
	// 移动速度
	int speed = 80;
	int speedup = 160;// 加速后是双倍速度
	// 右旋速度 90°每秒
	Quaternion rotRight = new Quaternion(0.0f, 0.0f, -0.38268346f, 0.9238795f);
	// 左旋速度 90°每秒
	Quaternion rotLeft = new Quaternion(0.0f, 0.0f, -0.38268346f, 0.9238795f);
	
	int snakeBodyRadius = 6;
	int deadBodyRadius = 5;
	int snakeBodyGrow = 5;
	
	int foodMovingDistance = 181;// 食物在靠近蛇头的时候会自动吸附
	
	int food_img_px = 40;
	int body_img_px = 80;
}
