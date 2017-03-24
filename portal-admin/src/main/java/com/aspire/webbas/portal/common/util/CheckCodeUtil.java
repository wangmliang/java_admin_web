package com.aspire.webbas.portal.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.servlet.http.HttpSession;

@SuppressWarnings("unused")
public final class CheckCodeUtil {
	private static int width = 90;
	private static int height = 30;
	private static int codeCount = 4;
	private static int xx = 15;
	private static int fontHeight = 25;
	private static int codeY = 25;
	static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	private static final String CHECK_CODE = "RANDOM_CHECK_CODE";
	private static final String CHECK_CODE_TIMESTAMP = "RANDOM_CHECK_CODE_TIMESTAMP";
	private static int DEFAULT_EXPIRE_TIME = 300000;

	public static BufferedImage createImage(HttpSession session) {
		String randomString = generateRamonWord();
		session.setAttribute("RANDOM_CHECK_CODE", randomString);
		session.setAttribute("RANDOM_CHECK_CODE_TIMESTAMP",
				new Long(System.currentTimeMillis()));

		return createImage(randomString);
	}

	public static String getCheckCode(HttpSession session) throws Exception {
		String randomString = (String) session
				.getAttribute("RANDOM_CHECK_CODE");
		// Long randomTimestamp = (Long)session.getAttribute("RANDOM_CHECK_CODE_TIMESTAMP");
		session.removeAttribute("RANDOM_CHECK_CODE");
		session.removeAttribute("RANDOM_CHECK_CODE_TIMESTAMP");
		// long now = System.currentTimeMillis();

		return randomString;
	}

	public static boolean check(HttpSession session, String checkCode) {
		if ((checkCode == null) || ("".equals(checkCode.trim()))) {
			return false;
		}

		String randomString = (String) session
				.getAttribute("RANDOM_CHECK_CODE");
		Long randomTimestamp = (Long) session
				.getAttribute("RANDOM_CHECK_CODE_TIMESTAMP");
		if (randomString != null) {
			session.removeAttribute("RANDOM_CHECK_CODE");
			session.removeAttribute("RANDOM_CHECK_CODE_TIMESTAMP");
		} else {
			return false;
		}

		long now = System.currentTimeMillis();

		if (randomTimestamp.longValue() + DEFAULT_EXPIRE_TIME < now) {
			return false;
		}

		return checkCode.trim().equalsIgnoreCase(randomString);
	}

	private static BufferedImage createImage(String code) {
		BufferedImage buffImg = new BufferedImage(width, height, 1);

		Graphics gd = buffImg.getGraphics();

		Random random = new Random();

		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		Font font = new Font("Fixedsys", 1, fontHeight);

		gd.setFont(font);

		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);

		gd.setColor(Color.BLACK);
		for (int i = 0; i < 40; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}

		int red = 0;
		int green = 0;
		int blue = 0;

		char[] codeCh = code.toCharArray();

		for (int i = 0; i < codeCh.length; i++) {
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			gd.setColor(new Color(red, green, blue));
			gd.drawString(String.valueOf(codeCh[i]), (i + 1) * xx, codeY);
		}

		return buffImg;
	}

	private static String generateRamonWord() {
		StringBuffer randomCode = new StringBuffer();

		Random random = new Random();

		for (int i = 0; i < codeCount; i++) {
			String code = String.valueOf(codeSequence[random.nextInt(36)]);

			randomCode.append(code);
		}

		return randomCode.toString();
	}
}