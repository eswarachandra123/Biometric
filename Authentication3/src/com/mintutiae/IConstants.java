package com.mintutiae;

public interface IConstants {
	String GRAY_IMAGE = "GRAY";
	String NORMALIZATION = "NORMALIZATION";
	String SEGMENTATION = "SEGMENTATION";
	String SEGMENTATION_I = "SEGMENTATION_I";

	int VAR_O = 150;
	int MO = 50;
	int W_VALUE = 12;
	int M_VALUE = 12;
	int GBX_W_VALUE = 25;
	double SIGMA = 2;
	int GUASE_MATRIX_SIZE = 10;
	int DC_VALUE = 255;

	double[][] tempMatrix = new double[][] {
			{ 0.111111111111111, 0.111111111111111, 0.111111111111111 },
			{ 0.111111111111111, 0.111111111111111, 0.111111111111111 },
			{ 0.111111111111111, 0.111111111111111, 0.111111111111111 } };
	int TWENTY = 20;
}
