package com.herle.java.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRBARCodeUtil {
	private static final Logger myLogger = LoggerFactory.getLogger(QRBARCodeUtil.class);

	public static void main(String[] args) {
		generateBARCode("herle@rhrk.uni-kl.de", "./src/main/resources/herle@rhrk.uni-kl.de.png", "png", 250, 250);
	}

	public static void generateQRCode(String textMessage, String filePath, String fileType, int width, int height) {

		try {

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(textMessage, BarcodeFormat.QR_CODE, width, height);
			generateImageFromBitMatrix(bitMatrix, filePath, fileType, width, height);

		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		myLogger.info(" You have successfully created QR Code.");
	}

	public static void generateBARCode(String textMessage, String filePath, String fileType, int width, int height) {
		BitMatrix bitMatrix;

		try {
			// Write Barcode
			bitMatrix = new Code128Writer().encode(textMessage, BarcodeFormat.CODE_128, width, height);
			generateImageFromBitMatrix(bitMatrix, filePath, fileType, width, height);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myLogger.info(" You have successfully created BAR Code.");
	}

	public static void generatePDF417Code(String textMessage, String filePath, String fileType, int width, int height) {
		BitMatrix bitMatrix;

		// Write PDF417
		try {

			bitMatrix = new PDF417Writer().encode(textMessage, BarcodeFormat.PDF_417, width / 2, height);

			generateImageFromBitMatrix(bitMatrix, filePath, fileType, width / 2, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myLogger.info(" You have successfully created PDF417 Code.");
	}

	public static void generateImageFromBitMatrix(BitMatrix bitMatrix, String filePath, String fileType, int width,
			int height) throws IOException {
		File myFile = new File(filePath);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE); // background color
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.BLACK); // QR/Barcode color

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (bitMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		ImageIO.write(image, fileType, myFile);

	}
}
