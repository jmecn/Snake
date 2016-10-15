package net.jmecn.snake.tools;

import static java.awt.image.BufferedImage.*;
import static net.jmecn.snake.core.SnakeConstants.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateBackground {

	public static void main(String[] args) {
		int scale = 5;
		BufferedImage image = new BufferedImage(width*scale, height*scale, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)image.getGraphics();

		
		g.setColor(new Color(235, 236, 244));
		g.fillRect(0, 0, width*scale, height*scale);
		
		g.setColor(Color.gray);
		
		for(int x=UNIT/2*scale; x<width*scale; x+=UNIT*scale) {
			g.drawLine(x, 0, x, height*scale);
		}
		for(int y=UNIT/2*scale; y<height*scale; y+=UNIT*scale) {
			g.drawLine(0, y, width*scale, y);
		}
		
		try {
			ImageIO.write(image, "png", new File("background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
