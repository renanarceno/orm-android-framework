package tcc.greendao;

import java.util.Random;

public class RandomString {

	public static final String letras = "qwertyuioplkjhgfdsazxcvbnm";

	public static String getRandomString(int length) {
		StringBuffer bs = new StringBuffer();
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			int pos = r.nextInt(letras.length());
			bs.append(letras.charAt(pos));
		}
		return bs.toString();
	}

}

