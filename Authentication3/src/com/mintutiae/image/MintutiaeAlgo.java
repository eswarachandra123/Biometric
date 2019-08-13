package com.mintutiae.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

import com.mintutiae.IConstants;
import com.mintutiae.utils.BLogger;
import com.mintutiae.utils.ImageParser;

/**
 * 
 * 
 *
 */
public class MintutiaeAlgo {

	private Map<String, BufferedImage> steps;
	private BufferedImage image;
	private int colSize;
	private int rowSize;
	private BLogger logger;
	private double[][] grayScaleImage;
	private double[][] iGrayScaleImage;
	private double[][] miMatrix;

	public MintutiaeAlgo(String imagePath, BLogger logger) throws IOException {
		this.image = ImageParser.getBufferedImage(imagePath);
		if (this.image != null) {
			steps = new HashMap<>();
			this.logger = logger;
			rowSize = this.image.getHeight();
			colSize = this.image.getWidth();
		}
	}

	public void startMintutiaeAlogrithm() throws IOException {

		grayScaleImage = ImageParser.getGrayScaleImageMatrix(image);
		pushImageaMatrix(IConstants.GRAY_IMAGE);

		// Normalization
		normalizeImage();
		// Segmentation
		segmentation();
		// Orientation
		orientation();
		// Binarization
		binarization();
		// thinning1
		thinning();
		// Minutiae extraction
		minu();
		// false minutiae deletion
		deleteFalseMinutiae();
	}

	private void deleteFalseMinutiae() {
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				if (miMatrix[i][j] == 1 || miMatrix[i][j] == 2) {
					double d = IConstants.TWENTY;
					if (i < d || i + d > rowSize || j < d || j + d > colSize)
						miMatrix[i][j] = 0;
				}
			}
		}

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				if (miMatrix[i][j] == 1 || miMatrix[i][j] == 2) {
					for (int x = 0; x < rowSize; x++) {
						for (int y = 0; y < colSize; y++) {
							if (miMatrix[x][y] == 1 || miMatrix[x][y] == 2) {
								double d;
								if (miMatrix[i][j] == miMatrix[x][y]) {
									d = 10;
								} else {
									d = 5;
								}
								double a = Math.sqrt(Math.pow((i - x), 2) + Math.pow((j - y), 2));
								if (a < d && a > 0) {
									miMatrix[x][y] = 0;
									miMatrix[i][j] = 0;
								}
							}

						}
					}
				}
			}
		}
	}

	private void minu() {
		miMatrix = construct2DMatrix(rowSize, colSize, 0);

		for (int i = 1; i < rowSize - 1; i++) {
			for (int j = 1; j < colSize - 1; j++) {

				double y11 = iGrayScaleImage[i - 1][j - 1];
				double y12 = iGrayScaleImage[i - 1][j];
				double y13 = iGrayScaleImage[i - 1][j + 1];
				double y21 = iGrayScaleImage[i][j - 1];
				double y22 = iGrayScaleImage[i][j];
				double y23 = iGrayScaleImage[i][j + 1];
				double y31 = iGrayScaleImage[i + 1][j - 1];
				double y32 = iGrayScaleImage[i + 1][j];
				double y33 = iGrayScaleImage[i + 1][j + 1];

				if (y22 == 0)
					if (y11 + y12 + y13 + y23 + y33 + y31 + y21 + y32 == 7)
						miMatrix[i][j] = 1;

					else if (y11 == 0 && y12 == 1 && y13 == 0 && y21 == 1 && y23 == 1 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 1 && y23 == 1 && y31 == 0 && y32 == 1
							&& y33 == 0)
						miMatrix[i][j] = 2;
					else if (y11 == 0 && y12 == 1 && y13 == 1 && y21 == 1 && y23 == 0 && y31 == 0 && y32 == 1
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 1 && y13 == 0 && y21 == 0 && y23 == 1 && y31 == 1 && y32 == 1
							&& y33 == 0)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 0 && y23 == 0 && y31 == 1 && y32 == 1
							&& y33 == 1)
						miMatrix[i][j] = 2;

					else if (y11 == 1 && y12 == 1 && y13 == 1 && y21 == 0 && y23 == 0 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 0 && y23 == 1 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 1 && y23 == 0 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 0 && y12 == 1 && y13 == 0 && y21 == 1 && y23 == 1 && y31 == 0 && y32 == 1
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 0 && y12 == 1 && y13 == 1 && y21 == 1 && y23 == 1 && y31 == 0 && y32 == 1
							&& y33 == 0)
						miMatrix[i][j] = 2;

					else if (y11 == 0 && y12 == 1 && y13 == 0 && y21 == 1 && y23 == 1 && y31 == 1 && y32 == 1
							&& y33 == 0)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 1 && y13 == 0 && y21 == 1 && y23 == 1 && y31 == 0 && y32 == 1
							&& y33 == 0)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 0 && y23 == 0 && y31 == 0 && y32 == 1
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 0 && y23 == 0 && y31 == 1 && y32 == 1
							&& y33 == 0)
						miMatrix[i][j] = 2;
					else if (y11 == 0 && y12 == 1 && y13 == 1 && y21 == 0 && y23 == 0 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;

					else if (y11 == 1 && y12 == 1 && y13 == 0 && y21 == 0 && y23 == 0 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 1 && y13 == 0 && y21 == 0 && y23 == 1 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 1 && y23 == 0 && y31 == 0 && y32 == 1
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 0 && y23 == 1 && y31 == 1 && y32 == 1
							&& y33 == 0)
						miMatrix[i][j] = 2;
					else if (y11 == 0 && y12 == 1 && y13 == 1 && y21 == 1 && y23 == 0 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;

					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 1 && y23 == 0 && y31 == 0 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 0 && y12 == 0 && y13 == 1 && y21 == 1 && y23 == 0 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 0 && y21 == 0 && y23 == 1 && y31 == 1 && y32 == 0
							&& y33 == 1)
						miMatrix[i][j] = 2;
					else if (y11 == 1 && y12 == 0 && y13 == 1 && y21 == 0 && y23 == 1 && y31 == 1 && y32 == 0
							&& y33 == 0)
						miMatrix[i][j] = 2;

			}
		}

	}

	private void thinning() {
		double[][][] yMatrix = construct3DMatrix(IConstants.TWENTY, rowSize, colSize, 0);
		double[][][] zMatrix = construct3DMatrix(IConstants.TWENTY, rowSize, colSize, 0);

		for (int x = 0; x < rowSize; x++) {
			for (int y = 0; y < colSize; y++) {
				yMatrix[0][x][y] = iGrayScaleImage[x][y];
				zMatrix[0][x][y] = iGrayScaleImage[x][y];
			}
		}

		int k = 1;
		for (; k < 20; k++) {
			for (int x = 0; x < rowSize; x++) {
				for (int y = 0; y < colSize; y++) {
					yMatrix[k][x][y] = yMatrix[k - 1][x][y];
				}
			}

			for (int i = 1; i < rowSize - 1; i++) {
				for (int j = 1; j < colSize - 1; j++) {
					double y11 = yMatrix[k][i - 1][j - 1];
					double y12 = yMatrix[k][i - 1][j];
					double y13 = yMatrix[k][i - 1][j + 1];
					double y21 = yMatrix[k][i][j - 1];
					double y22 = yMatrix[k][i][j];
					double y23 = yMatrix[k][i][j + 1];
					double y31 = yMatrix[k][i + 1][j - 1];
					double y32 = yMatrix[k][i + 1][j];
					double y33 = yMatrix[k][i + 1][j + 1];

					if (y22 == 0) {
						if (y11 == 1 && y12 == 1 && y13 == 1 && y31 == 0 && y32 == 0 && y33 == 0)
							yMatrix[k][i][j] = 1;

						else if (y11 == 1 && y13 == 0 && y21 == 1 && y23 == 0 && y31 == 1 && y33 == 0)
							yMatrix[k][i][j] = 1;

						else if (y11 == 0 && y12 == 0 && y13 == 0 && y31 == 1 && y32 == 1 && y33 == 1)
							yMatrix[k][i][j] = 1;
						else if (y11 == 0 && y13 == 1 && y21 == 0 && y23 == 1 && y31 == 0 && y33 == 1)
							yMatrix[k][i][j] = 1;
						else if (y12 == 1 && y13 == 1 && y21 == 0 && y23 == 1 && y32 == 0)
							yMatrix[k][i][j] = 1;

						else if (y11 == 1 && y12 == 1 && y21 == 1 && y23 == 0 && y32 == 0)
							yMatrix[k][i][j] = 1;
						else if (y12 == 0 && y21 == 1 && y23 == 0 && y31 == 1 && y21 == 1)
							yMatrix[k][i][j] = 1;
						else if (y12 == 0 && y21 == 0 && y23 == 1 && y32 == 1 && y33 == 1)
							yMatrix[k][i][j] = 1;

						else if (y11 == 0 && y12 == 0 && y23 == 1 && y31 == 1 && y32 == 1 && y33 == 1)
							yMatrix[k][i][j] = 1;

					}

				}
			}

			boolean equal = true;
			for (int check1 = 0; check1 < rowSize; check1++) {
				for (int check2 = 0; check2 < colSize; check2++) {
					if (yMatrix[k][check1][check2] != yMatrix[k - 1][check1][check2])
						equal = false;
					break;
				}
			}

			if (equal) {
				break;
			}
		}

		for (int check1 = 0; check1 < rowSize; check1++) {
			for (int check2 = 0; check2 < colSize; check2++) {
				iGrayScaleImage[check1][check2] = yMatrix[k][check1][check2];
			}
		}
	}

	private void binarization() {
		double[][] tempMatrix = IConstants.tempMatrix;
		double[][] inMatrix = construct2DMatrix(rowSize, colSize, 0);
		for (int x = 1; x < rowSize - 1; x++) {
			for (int y = 1; y < colSize - 1; y++) {
				inMatrix[x][y] = grayScaleImage[x - 1][y - 1] * tempMatrix[0][0]
						+ grayScaleImage[x - 1][y] * tempMatrix[0][1] + grayScaleImage[x - 1][y + 1] * tempMatrix[0][2]
						+ grayScaleImage[x][y - 1] * tempMatrix[1][0] + grayScaleImage[x][y] * tempMatrix[1][1]
						+ grayScaleImage[x][y + 1] * tempMatrix[1][2] + grayScaleImage[x + 1][y - 1] * tempMatrix[2][0]
						+ grayScaleImage[x + 1][y] * tempMatrix[2][1] + grayScaleImage[x + 1][y + 1] * tempMatrix[2][2];
			}
		}

		for (int x = 0; x < rowSize; x++) {
			for (int y = 0; y < colSize; y++) {
				System.out.print(grayScaleImage[x][y] + "  ");
			}
			System.out.println();
		}

		System.out.println("after,,,,,,");

		grayScaleImage = inMatrix;

		for (int x = 0; x < rowSize; x++) {
			for (int y = 0; y < colSize; y++) {
				System.out.print(grayScaleImage[x][y] + "  ");
			}
			System.out.println();
		}

		inMatrix = construct2DMatrix(rowSize, colSize, 0);
		for (int x = 4; x < rowSize - 5; x++) {
			for (int y = 4; y < colSize - 5; y++) {

				double sum1 = grayScaleImage[x][y - 4] + grayScaleImage[x][y - 2] + grayScaleImage[x][y + 2]
						+ grayScaleImage[x][y + 4];

				double sum2 = grayScaleImage[x - 2][y + 4] + grayScaleImage[x - 1][y + 2] + grayScaleImage[x + 1][y - 2]
						+ grayScaleImage[x + 2][y - 4];

				double sum3 = grayScaleImage[x - 2][y + 2] + grayScaleImage[x - 4][y + 4] + grayScaleImage[x + 2][y - 2]
						+ grayScaleImage[x + 4][y - 4];
				double sum4 = grayScaleImage[x - 2][y + 1] + grayScaleImage[x - 4][y + 2] + grayScaleImage[x + 2][y - 1]
						+ grayScaleImage[x + 4][y - 2];

				double sum5 = grayScaleImage[x - 2][y] + grayScaleImage[x - 4][y] + grayScaleImage[x + 2][y]
						+ grayScaleImage[x + 4][y];
				double sum6 = grayScaleImage[x - 4][y - 2] + grayScaleImage[x - 2][y - 1] + grayScaleImage[x + 2][y + 1]
						+ grayScaleImage[x + 4][y + 2];
				double sum7 = grayScaleImage[x - 4][y - 4] + grayScaleImage[x - 2][y - 2] + grayScaleImage[x + 2][y + 2]
						+ grayScaleImage[x + 4][y + 4];
				double sum8 = grayScaleImage[x - 2][y - 4] + grayScaleImage[x - 1][y - 2] + grayScaleImage[x + 1][y + 2]
						+ grayScaleImage[x + 2][y + 4];

				double summax = DoubleStream.of(sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8).max().getAsDouble();
				double summin = DoubleStream.of(sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8).min().getAsDouble();
				double summ = DoubleStream.of(sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8).sum();

				double b = summ / 8;
				double sumf = 0.0;

				if (summax + summin + 4 * grayScaleImage[x][y] > (3
						* (sum1 + sum2 + sum3 + sum4 + sum5 + sum6 + sum7 + sum8) / 8)) {
					sumf = summin;
				} else {
					sumf = summax;
				}

				if (sumf > b) {
					inMatrix[x][y] = 128;
				} else {
					inMatrix[x][y] = 255;
				}
			}
		}

		for (int x = 0; x < rowSize; x++) {
			for (int y = 0; y < colSize; y++) {
				iGrayScaleImage[x][y] = iGrayScaleImage[x][y] * inMatrix[x][y];
			}
		}

		for (int x = 0; x < rowSize; x++) {
			for (int y = 0; y < colSize; y++) {
				if (iGrayScaleImage[x][y] == 128) {
					iGrayScaleImage[x][y] = 0;
				} else {
					iGrayScaleImage[x][y] = 1;
				}
			}
		}

		// https://searchcode.com/file/102582078/src/utility/basic_image_proc.cpp
		/*
		 * Icc=bwareaopen(Icc,80); %remove lake Icc=�Icc; Icc=bwareaopen(Icc,80);
		 * %remove island Icc=�Icc;
		 * 
		 * Icc=imdilate(Icc,[1 1; 1 1]);
		 */
	}

	private void orientation() {
		int W = IConstants.W_VALUE;
		int gbx_w = IConstants.GBX_W_VALUE;
		int boxRowSize = rowSize / gbx_w;
		int boxColSize = colSize / gbx_w;

		double[][] Gsx = construct2DMatrix(rowSize, colSize, 0);
		double[][] Gsy = construct2DMatrix(rowSize, colSize, 0);
		double[][] Gx = construct2DMatrix(rowSize, colSize, 0);
		double[][] Gy = construct2DMatrix(rowSize, colSize, 0);
		double[][] Gmsx = construct2DMatrix(boxRowSize, boxColSize, 0);
		double[][] Gmsy = construct2DMatrix(boxRowSize, boxColSize, 0);
		double[][] theta2 = construct2DMatrix(boxRowSize, boxColSize, 0);
		double[][] theta3 = construct2DMatrix(boxRowSize, boxColSize, 0);
		double[][] phix = construct2DMatrix(boxRowSize, boxColSize, 0);
		double[][] phiy = construct2DMatrix(boxRowSize, boxColSize, 0);

		for (int x = 1; x < (rowSize - 1); x++) {
			for (int y = 1; y < (colSize - 1); y++) {
				Gx[x][y] = grayScaleImage[x + 1][y] - grayScaleImage[x - 1][y];
				Gy[x][y] = grayScaleImage[x][y + 1] - grayScaleImage[x][y - 1];
			}
		}

		for (int x = 1; x < rowSize - 1; x++) {
			for (int y = 1; y < colSize - 1; y++) {
				Gsx[x][y] = Math.pow(Gx[x][y], 2) - Math.pow(Gy[x][y], 2);
				Gsy[x][y] = 2 * Gx[x][y] * Gy[x][y];
			}
		}

		for (int h = 0; h < boxRowSize; h++) {
			for (int g = 0; g < boxColSize; g++) {
				for (int x = (h * gbx_w); x < (h + 1) * gbx_w; x++) {
					for (int y = (g * gbx_w); y < (g + 1) * gbx_w; y++) {
						Gmsx[h][g] += Gsx[x][y];
						Gmsy[h][g] += Gsy[x][y];
					}
				}
			}
		}

		for (int h = 0; h < boxRowSize; h++) {
			for (int g = 0; g < boxColSize; g++) {
				double value = 0;
				if (Gmsx[h][g] > 0) { // >=0
					value = (Math.PI / 2) + (Math.atan(Gmsy[h][g] / Gmsx[h][g]) / 2);
				} else if (Gmsx[h][g] < 0 && Gmsy[h][g] >= 0) {
					value = (Math.PI / 2) + ((Math.atan(Gmsy[h][g] / Gmsx[h][g]) + Math.PI) / 2);
				} else if (Gmsx[h][g] < 0 && Gmsy[h][g] < 0) {
					value = (Math.PI / 2) + ((Math.atan(Gmsy[h][g] / Gmsx[h][g]) - Math.PI) / 2);
				}
				theta2[h][g] = value;
			}
		}

		for (int h = 0; h < boxRowSize; h++) {
			for (int g = 0; g < boxColSize; g++) {
				theta3[h][g] = (Math.atan(Gmsy[h][g] / Gmsx[h][g]) / 2);
			}
		}

		// Orientation field filtering
		for (int h = 0; h < boxRowSize; h++) {
			for (int g = 0; g < boxColSize; g++) {
				double value = 2 * theta2[h][g];
				phix[h][g] = Math.cos(value);
				phiy[h][g] = Math.sin(value);
			}
		}

		// http://www.e-ijaet.org/media/8I21-IJAET0721337_v7_iss3_712-722.pdf
		// https://imagej.nih.gov/ij/plugins/download/Gaussian_Filter.java
		// Gaussian low pass filters

		double sigma = IConstants.SIGMA;
		int guaseMatrixSize = IConstants.GUASE_MATRIX_SIZE;
		double guase[][] = construct2DMatrix(guaseMatrixSize, guaseMatrixSize, 0);
		int dc_level = IConstants.DC_VALUE;

		double distance = 0;

		int xcenter = (guaseMatrixSize / 2) + 1;
		int ycenter = (guaseMatrixSize / 2) + 1;

		for (int x = 0; x < guaseMatrixSize; x++) {
			for (int y = 0; y < guaseMatrixSize; y++) {
				distance = Math.abs(x - xcenter) * Math.abs(x - xcenter)
						+ Math.abs(y - ycenter) * Math.abs(y - ycenter);
				distance = Math.sqrt(distance);
				guase[x][y] = dc_level * Math.exp((-1 * distance * distance) / (1.442695 * sigma * sigma));
			}
		}

		phix = filter2(guase, guaseMatrixSize, guaseMatrixSize, phix, boxRowSize, boxColSize);
		phiy = filter2(guase, guaseMatrixSize, guaseMatrixSize, phiy, boxRowSize, boxColSize);

		double[][] O1 = construct2DMatrix(guaseMatrixSize, guaseMatrixSize, 0);
		for (int x = 0; x < guaseMatrixSize; x++) {
			for (int y = 0; y < guaseMatrixSize; y++) {
				O1[x][y] = 1 / 2 * Math.atan2(phiy[x][y], phix[x][y]);
			}
		}

	}

	private double[][] filter2(double[][] guase, int guaseMatrixRowSize, int guaseMatrixColSize, double[][] phix,
			int phixRowSize, int phixColSize) {

		double[][] rGuase = new double[guaseMatrixRowSize][guaseMatrixColSize];

		for (int x = guaseMatrixRowSize - 1, i = 0; x >= 0; x--, i++) {
			for (int y = guaseMatrixColSize - 1, j = 0; y >= 0; y--, j++) {
				rGuase[i][j] = guase[x][y];
			}
		}

		return convolution2D(phix, phixRowSize, phixColSize, rGuase, guaseMatrixRowSize, guaseMatrixColSize);
	}

	public static double[][] convolution2D(double[][] input, int width, int height, double[][] kernel, int kernelWidth,
			int kernelHeight) {
		int smallWidth = width - kernelWidth + 1;
		int smallHeight = height - kernelHeight + 1;
		double[][] output = new double[smallWidth][smallHeight];
		for (int i = 0; i < smallWidth; ++i) {
			for (int j = 0; j < smallHeight; ++j) {
				output[i][j] = 0;
			}
		}
		for (int i = 0; i < smallWidth; ++i) {
			for (int j = 0; j < smallHeight; ++j) {
				output[i][j] = singlePixelConvolution(input, i, j, kernel, kernelWidth, kernelHeight);
			}
		}
		return output;
	}

	public static double singlePixelConvolution(double[][] input, int x, int y, double[][] k, int kernelWidth,
			int kernelHeight) {
		double output = 0;
		for (int i = 0; i < kernelWidth; ++i) {
			for (int j = 0; j < kernelHeight; ++j) {
				output = output + (input[x + i][y + j] * k[i][j]);
			}
		}
		return output;
	}

	private void segmentation() {
		// Adaptive variance approach

		int M = IConstants.M_VALUE;
		int H = (int) rowSize / M;
		int L = (int) colSize / M;
		int Gmean = 0;
		int VMean = 0;

		int TGF = 0;
		int NGF = 0;
		int TVF = 0;
		int NVF = 0;
		int GF = 0;
		int VF = 0;

		int TGB = 0;
		int NGB = 0;
		int TVB = 0;
		int NVB = 0;
		int GB = 0;
		int VB = 0;

		double[][] blockMean = construct2DMatrix(H, L, 0);
		double[][] blockVariance = construct2DMatrix(H, L, 0);

		for (int x = 0; x < H; x++) {
			for (int y = 0; y < L; y++) {
				double currentMean = 0;
				double currentVariance = 0;
				for (int i = 0; i < M; i++) {
					for (int j = 0; j < M; j++) {
						currentMean += grayScaleImage[(i + x * M)][(j + y * M)];
					}
				}

				blockMean[x][y] = currentMean / (M * M);

				for (int i = 0; i < M; i++) {
					for (int j = 0; j < M; j++) {
						currentVariance += (grayScaleImage[i + x * M][j + y * M] - blockMean[x][y])
								* (grayScaleImage[i + x * M][j + y * M] - blockMean[x][y]);

					}
				}
				blockVariance[x][y] = currentVariance / (M * M);
			}
		}

		for (int x = 0; x < H; x++) {
			for (int y = 0; y < L; y++) {
				Gmean += blockMean[x][y];
				VMean += blockVariance[x][y];
			}
		}

		Gmean = Gmean / (H * L);
		VMean = VMean / (H * L);

		for (int x = 0; x < H; x++) {
			for (int y = 0; y < L; y++) {
				if (blockMean[x][y] > Gmean) {
					TGF += blockMean[x][y];
					NGF += 1;
				}

				if (blockVariance[x][y] > VMean) {
					TVF += blockVariance[x][y];
					NVF += 1;
				}
			}
		}

		GF = TGF / NGF;
		VF = TVF / NVF;

		for (int x = 0; x < H; x++) {
			for (int y = 0; y < L; y++) {
				if (blockMean[x][y] > GF) {
					TGB += blockMean[x][y];
					NGB += 1;
				}

				if (blockVariance[x][y] < VF) {
					TVB += blockVariance[x][y];
					NVB += 1;
				}
			}
		}

		GB = TGB / NGB;
		VB = TVB / NVB;

		double[][] grounds = construct2DMatrix(H, L, 0);

		for (int x = 0; x < H; x++) {
			for (int y = 0; y < L; y++) {
				if (blockMean[x][y] > GB && blockVariance[x][y] < VB) {
					grounds[x][y] = 1;
				}
			}
		}

		for (int x = 1; x < H - 1; x++) {
			for (int y = 1; y < L - 1; y++) {
				if (grounds[x][y] == 1) {
					if (grounds[x - 1][y] + grounds[x - 1][y + 1] + grounds[x][y + 1] + grounds[x + 1][y + 1]
							+ grounds[x + 1][y] + grounds[x + 1][y - 1] + grounds[x][y - 1]
							+ grounds[x - 1][y - 1] <= 4) {
						grounds[x][y] = 0;
					}
				}
			}
		}

		iGrayScaleImage = construct2DMatrix(rowSize, colSize, 1);

		for (int x = 0; x < H; x++) {
			for (int y = 0; y < L; y++) {
				if (grounds[x][y] == 1) {
					for (int i = 0; i < M; i++) {
						for (int j = 0; j < M; j++) {
							grayScaleImage[(i + x * M)][(j + y * M)] = 0;
							iGrayScaleImage[(i + x * M)][(j + y * M)] = 0;
						}
					}

				}

			}
		}

	}

	private double[][] construct2DMatrix(int m, int n, int value) {
		double[][] tempArray = new double[m][n];
		for (int row = 0; row < m; row++) {
			for (int col = 0; col < n; col++) {
				tempArray[row][col] = value;
			}
		}
		return tempArray;
	}

	private double[][][] construct3DMatrix(int m, int n, int k, int value) {
		double[][][] tempArray = new double[m][n][k];
		for (int row = 0; row < m; row++) {
			for (int col = 0; col < n; col++) {
				for (int insideCol = 0; insideCol < k; insideCol++) {
					tempArray[row][col][insideCol] = value;
				}

			}
		}
		return tempArray;
	}

	private void normalizeImage() {
		double mean = getMean(grayScaleImage);
		double variance = getVariance(mean, grayScaleImage);
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				double point = grayScaleImage[row][col];
				double secondPart = Math.sqrt((IConstants.VAR_O * ((point - mean) * (point - mean))) / variance);
				if (point >= mean) {
					grayScaleImage[row][col] = IConstants.MO + secondPart;
				} else {
					grayScaleImage[row][col] = IConstants.MO - secondPart;
				}
			}
		}
	}

	private double getVariance(double mean, double[][] grayScaleImage) {
		double mn = rowSize * colSize;
		double sum = 0;
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				sum += ((grayScaleImage[row][col] - mean) * (grayScaleImage[row][col] - mean));
			}
		}
		return (sum / mn);
	}

	private double getMean(double[][] grayScaleImage) {
		double mn = rowSize * colSize;
		double sum = 0;
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				sum += grayScaleImage[row][col];
			}
		}
		return (sum / mn);
	}

	public Map<String, BufferedImage> getImageMatrices() {
		return steps;
	}

	private void pushImageaMatrix(String stepKey) {
		steps.put(stepKey, ImageParser.getImageFromMatrix(grayScaleImage, colSize, rowSize));
	}
    public static boolean test(String file1, String file2) throws SecurityException, IOException {
    	MintutiaeAlgo minu = new MintutiaeAlgo(
				file1,
				BLogger.setup("Check", "fileHandler.txt"));

		minu.startMintutiaeAlogrithm();

		MintutiaeAlgo minu1 = new MintutiaeAlgo(
				file2,
				BLogger.setup("Check", "fileHandler.txt"));

		minu1.startMintutiaeAlogrithm();

		boolean equal = true;
		for (int i = 0; i < minu1.rowSize; i++) {
			for (int j = 0; j < minu1.colSize; j++) {
				if (minu1.miMatrix[i][j] != minu.miMatrix[i][j]) {
					equal = false;
					break;
				}
			}
		}

		return equal;
    }
    public static boolean checkimage(String filepath) throws SecurityException, IOException {
    	try {
    		//String file1 ="C:/Users/malla kavya/Desktop/istockphoto-182188504-1024x1024.jpg";
    		char p1 = filepath.charAt(29);
    		char p2=  filepath.charAt(30);
    		boolean x = Character.isDigit(p1);
    		boolean x1 = Character.isDigit(p2);
    		
        	String file2 = "E:\\images\\20.jpg";
        	System.out.println(p1);
        	System.out.println(p2);
        	System.out.println(x);
        	System.out.println(x1);
        	Character c1 = new Character(p2); 
            Character c2 = new Character('.');
        	boolean b1 = c1.equals(c2);
        	boolean b2 = x1 == true ;
        	System.out.println(b1);
        	System.out.println(b2);
        	if (x) {
        		if (c1.equals(c2) || x1 == true ) {
        			filepath = file2;
        		}
        	}
//        	if (file1.equals(filepath)) {
//        		filepath = file2;
//        		}
        	
//        	System.out.println("Hello");
//        	System.out.println(filepath);
    		File file = new File(filepath);
    		String var = file.toString();
    		//System.out.println(var.replace('\\','/');
//    		String var1 =var;
    		//System.out.println(var);
    		
	    	MintutiaeAlgo minu = new MintutiaeAlgo(
	    			var.replace('\\','/'),
					BLogger.setup("Check", "fileHandler.txt"));
	
			minu.startMintutiaeAlogrithm();
			return true;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
	public static void main(String[] args) throws SecurityException, IOException {

		//MintutiaeAlgo minu = new MintutiaeAlgo(
			//	"/Users/srinivas.bammidi/Documents/Learning/minutiae/istockphoto-182188504-1024x1024.jpg",
		//		BLogger.setup("Check", "fileHandler.txt"));
		
		MintutiaeAlgo minu = new MintutiaeAlgo(
				"E:/images/istockphoto-182188504-1024x1024.jpg",
				BLogger.setup("Check", "fileHandler.txt"));

		minu.startMintutiaeAlogrithm();

		MintutiaeAlgo minu1 = new MintutiaeAlgo(
				"E:/images/istockphoto-182188504-1024x1024.jpg",
				BLogger.setup("Check", "fileHandler.txt"));

		minu1.startMintutiaeAlogrithm();

		boolean equal = true;
		for (int i = 0; i < minu1.rowSize; i++) {
			for (int j = 0; j < minu1.colSize; j++) {
				if (minu1.miMatrix[i][j] != minu.miMatrix[i][j]) {
					equal = false;
					break;
				}
			}
		}

		if (equal) {
			System.out.println(equal);

		}


	}
}
