package com.utility;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import org.apache.log4j.Logger;

public class RandomString {
	public static Logger log = Logger.getLogger(RandomString.class.getName());

	/**
	 * Generate a random string.
	 */
	public String nextString() {
		log.debug("alphanum 5:" + alphanum + " random 1:[" + random + "] symbols [" + symbols.toString() + "]");
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

	public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String lower = upper.toLowerCase(Locale.ROOT);

	public static final String digits = "0123456789";

	public static final String alphanum = upper + lower + digits;

	private final Random random;

	private final char[] symbols;

	private final char[] buf;

	public RandomString(int length, Random random, String symbols) {
		if (length < 1)
			throw new IllegalArgumentException();
		if (symbols.length() < 2)
			throw new IllegalArgumentException();
		// this.random = Objects.requireNonNull(random);
		this.random = Objects.requireNonNull(random);
		this.symbols = symbols.toCharArray();
		this.buf = new char[length];
		log.debug("alphanum 3 [" + alphanum + "] random [" + random + " ]length :" + length);
	}

	/**
	 * Create an alphanumeric string generator.
	 */
	public RandomString(int length, Random random) {
		this(length, random, alphanum);
		log.debug("alphanum 2 [" + alphanum + "] random [" + random + " ]length :" + length);
	}

	/**
	 * Create an alphanumeric strings from a secure generator.
	 */
	public RandomString(int length) {
		this(length, new SecureRandom());
		log.debug("alphanum 1 [" + alphanum + "] random [" + random + " ]length :" + length);
	}

	/**
	 * Create session identifiers.
	 */
	public RandomString() {

		this(21);
		log.debug("alphanum 0 [" + alphanum + "] random [" + random + " ]");
	}

	public static void main(String a[]) {
		RandomString gen = new RandomString(4, new Random(), "B@");
		// RandomString gen = new RandomString();
		System.out.print(gen.nextString());
	}

}
