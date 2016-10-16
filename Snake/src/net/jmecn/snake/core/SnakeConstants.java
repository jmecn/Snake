package net.jmecn.snake.core;

public interface SnakeConstants {

	int width = 1280;
	int height = 720;
	int UNIT = 8;
	
	int foodRadius = 3;
	
	int speed = 80;
	int snakeBodyRadius = 6;
	int deadBodyRadius = 5;
	int snakeMinLength = 30;// 蛇的最短长度
	int snakeBodyGrow = 5;
	
	int foodMovingDistance = 181;// 食物在靠近蛇头的时候会自动吸附
	
	int food_img_px = 40;
	int body_img_px = 80;
}
