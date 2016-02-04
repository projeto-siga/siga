/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.itextpdf;

import com.swetake.util.Qrcode;

import java.awt.*;

/**
 * QRcode¥¯¥é¥¹¥é¥¤¥Ö¥é¥êÍÑsample
 * 
 * Âè°ì°ú¿ô¤ò¥Ç¡¼¥¿¤È¤·¤¿QRcode¤ò ¥Æ¥­¥¹¥È¤Ç½ÐÎÏ¤·¤Þ¤¹
 */
class Sample {
	public static void main(String[] args) {

		Qrcode x = new Qrcode();
		x.setQrcodeErrorCorrect('M'); // ¥¨¥é¡¼ÄûÀµ¥ì¥Ù¥ëM
		x.setQrcodeEncodeMode('A'); // http://itrc.jju.edu.cn/nexus-2.0.6/content/repositories/thirdparty/8bit byte ¥â¡¼¥É
		boolean[][] matrix = x.calQrcode(args[0].getBytes());

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[j][i]) {
					System.out.print("@");
				} else {
					System.out.print(" ");
				}
			}
			System.out.print("\n");
		}

	}

	/**
	 * Gets an <CODE>Image</CODE> with the barcode. The image will have to be
	 * scaled in the Y direction by <CODE>yHeight</CODE>for the barcode to
	 * have the right printing aspect.
	 * 
	 * @return the barcode <CODE>Image</CODE>
	 * @throws BadElementException
	 *             on error
	 */
//	public Image getImage() throws BadElementException {
//		paintCode();
//		byte g4[] = CCITTG4Encoder.compress(outBits, bitColumns, codeRows);
//		return Image.getInstance(bitColumns, codeRows, false, Image.CCITTG4,
//				(options & PDF417_INVERT_BITMAP) == 0 ? 0
//						: Image.CCITT_BLACKIS1, g4, null);
//	}

	/**
	 * Creates a <CODE>java.awt.Image</CODE>.
	 * 
	 * @param foreground
	 *            the color of the bars
	 * @param background
	 *            the color of the background
	 * @return the image
	 */
	public static java.awt.Image createQRCodeImage(String url) {
		Qrcode x = new Qrcode();
		x.setQrcodeErrorCorrect('M'); // 15%
		x.setQrcodeEncodeMode('A'); // Alpha
		boolean[][] matrix = x.calQrcode(url.getBytes());
		
		Canvas canvas = new Canvas();
		java.awt.Image img = canvas.createImage(matrix.length, matrix.length);
		Graphics g = img.getGraphics();
		g.setColor(Color.BLACK);
		img.getGraphics().clearRect(0, 0, matrix.length, matrix.length);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[j][i]) {
					img.getGraphics().drawLine(j, i, j, i);
				}
			}
		}
		return img;
	}

}
