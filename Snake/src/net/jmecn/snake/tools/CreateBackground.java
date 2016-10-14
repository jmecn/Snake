package net.jmecn.snake.tools;

import static java.awt.image.BufferedImage.*;
import static net.jmecn.snake.core.SnakeConstants.*;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateBackground {

	public static void main(String[] args) {
		BufferedImage image = new BufferedImage(width, height, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)image.getGraphics();

		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.lightGray);
		
		for(int x=snakeBodyRadius; x<width; x+=snakeBodyRadius*2) {
			g.drawLine(x, 0, x, height);
		}
		for(int y=snakeBodyRadius; y<height; y+=snakeBodyRadius*2) {
			g.drawLine(0, y, width, y);
		}
		
		try {
			ImageIO.write(image, "png", new File("background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
