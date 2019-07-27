package com.mintutiae.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * 
 * @author srinivas.bammidi
 *         //https://memorynotfound.com/convert-image-grayscale-java/
 *         //https://stackoverflow.com/questions/9131678/convert-a-rgb-image-to-grayscale-image-reducing-the-memory-in-java
 *         //https://www.tutorialspoint.com/java_dip/grayscale_conversion.htm
 *
 */
public class ImageParser {

	public static BufferedImage getBufferedImage(String imagePath) throws IOException {
		BufferedImage image = null;
		if (imagePath != null) {
			try {
				image = ImageIO.read(new File(imagePath));
			} catch (IOException | IllegalArgumentException e) {
				try {
					image = ImageIO.read(ImageParser.class.getClass().getResource(imagePath));
				} catch (IOException e1) {
				}
			}
		} else {
			throw new IOException("Image path not provided");
		}
		return image;
	}

	public static boolean isGrayScaleImage(BufferedImage image) {
		int colSize = image.getWidth();
		int rowSize = image.getHeight();
		for (int y = 0; y < rowSize; y++) {
			for (int x = 0; x < colSize; x++) {
				int[] rgb = getRGBArray(image.getRGB(x, y));
				if (!(rgb[0] == rgb[1] && rgb[1] == rgb[2])) {
					return false;
				}
			}
		}
		return true;
	}

	// https://www.geeksforgeeks.org/matlab-rgb-image-to-grayscale-image-conversion/
	public static double[][] getGrayScaleImageMatrix(final BufferedImage image) {
		int colSize = image.getWidth();
		int rowSize = image.getHeight();
		double[][] resultMatrix = new double[rowSize][colSize];
		boolean graySclaeImage = isGrayScaleImage(image);
		if (!graySclaeImage) {
			convertToGrayscale(image);
		}

		for (int y = 0; y < rowSize; y++) {
			for (int x = 0; x < colSize; x++) {
				int rgb = image.getRGB(x, y);

				int red = (rgb >> 16) & 255;
				int green = (rgb >> 8) & 255;
				int blue = (rgb) & 255;

				// https://stackoverflow.com/questions/15972490/bufferedimage-getting-the-value-of-a-pixel-in-grayscale-color-model-picture
				// resultMatrix[y][x] = graySclaeImage ? rgb & 0xFF : (r + g + b) / 3;
				resultMatrix[y][x] = (red * 0.2990) + (green * 0.5870) + (blue * 0.114);
			}
		}
		
		return resultMatrix;
	}

	// https://rosettacode.org/wiki/Grayscale_image
	public static void convertToGrayscale(final BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int rgb = image.getRGB(i, j);

				int alpha = (rgb >> 24) & 255;
				int red = (rgb >> 16) & 255;
				int green = (rgb >> 8) & 255;
				int blue = (rgb) & 255;

				final int lum = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);

				alpha = (alpha << 24);
				red = (lum << 16);
				green = (lum << 8);
				blue = lum;

				rgb = alpha + red + green + blue;

				image.setRGB(i, j, rgb);
			}
		}
	}

	public static int[] getRGBArray(int rgb) {
		int[] rgbArr = new int[3];
		int r = (int) ((rgb >> 16) & 0xff);
		int g = (int) ((rgb >> 8) & 0xff);
		int b = (int) (rgb & 0xff);

		rgbArr[0] = r;
		rgbArr[1] = g;
		rgbArr[2] = b;

		return rgbArr;
	}

	// ToDO: Need to write again
	public static BufferedImage getImageFromMatrix(double[][] imageMatrix, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int xy = (int) imageMatrix[y][x];
				image.setRGB(x, y, xy);
			}
		}
		return image;
	}

	public static void writeImage(String imagePath, BufferedImage image) {
		File ImageFile = new File(imagePath);
		try {
			ImageIO.write(image, "png", ImageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
