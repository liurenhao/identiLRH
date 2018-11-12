package com.lrhii.imagehandle;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

public class ImageHandle {
	public static void main(String[] args) throws Exception {
		Set<Color> rgbSet = new HashSet<Color>();
		BufferedImage sourceImg = ImageIO.read(new File("E://a.png"));
		int width = sourceImg.getWidth();
		int height = sourceImg.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = sourceImg.getRGB(i, j);
				Color color = new Color(rgb);
				if(color.getRed() > color.getBlue() && color.getRed() > color.getGreen()
						&& color.getBlue() == 0 && color.getGreen() == 0){
					sourceImg.setRGB(i, j, Color.white.getRGB());
				}
//				if(color.getRed() > color.getBlue() && color.getRed() > color.getGreen()
//						&& color.getBlue() < 2 && color.getGreen() < 2){
//					sourceImg.setRGB(i, j, Color.white.getRGB());
//				}
			}
		}
		
		ImageIO.write(sourceImg, "PNG", new File("E://c.png"));
		
		
//		System.out.println(width * height);
//		System.out.println(rgbSet.size());
//		for (Color color : rgbSet) {
//			System.out.println(color.getRed() + "\t" + color.getGreen() + 
//					"\t" + color.getBlue() + "\t" + color.getRGB());
//		}
	}
}
