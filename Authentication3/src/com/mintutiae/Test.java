package com.mintutiae;

public class Test {

	public static void main(String[] args) {
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
		
		System.out.println(guase);
	}

	private static double[][] construct2DMatrix(int m, int n, int value) {
		double[][] tempArray = new double[m][n];
		for (int row = 0; row < m; row++) {
			for (int col = 0; col < n; col++) {
				tempArray[row][col] = value;
			}
		}
		return tempArray;
	}
}
