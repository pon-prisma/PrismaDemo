package it.prisma.businesslayer.bizlib.paas.services.smsaas;

/**
 * This class generate a random password(it is equal to the token).
 *@author l.calicchio
 *
 */

import java.util.Random;
	 
	
public class RandomPassword {
	 
	static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static Random rnd = new Random(System.currentTimeMillis());
	static private final int LENGHT = 8;
	 
	public static String generate() {
		StringBuilder sb = new StringBuilder(LENGHT);
	    for (int i = 0; i < LENGHT; i++) {
	        sb.append(ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
	    }
	    return sb.toString();
	}
	
}
