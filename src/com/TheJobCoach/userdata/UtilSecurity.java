package com.TheJobCoach.userdata;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilSecurity
{
	private static Logger logger = LoggerFactory.getLogger(UtilSecurity.class);
	private final static int SALT_SIZE = 32;
	private final static int PASSWORD_SIZE = 8;
	static MessageDigest digest;
	
	final static Random r = new SecureRandom();
	
	static {
		try
		{
			digest = MessageDigest.getInstance("SHA-256");
		}
		catch (Exception e)
		{
			logger.error("Unhandled error with digest generation. Continuing with void digest... : " + e.getMessage());
		}
	}

	public static String getSalt()
	{
		byte[] salt = new byte[SALT_SIZE];
		r.nextBytes(salt);
		byte[] b64 = Base64.encodeBase64(salt);
		String t = "";
		try {
			t = new String(b64, "UTF-8");
		}
		catch (Exception e)
		{
			// should never happen
			logger.error("Unhandled error with salt generation. Continuing with void salt... : " + e.getMessage());
		}
		return t;
	}

	public static String getPassword()
	{
		return RandomStringUtils.randomAlphabetic(PASSWORD_SIZE);
	}
	
	static public String getHash(String key)
	{
		byte[] hash;
		byte[] org;
		String result = key;
		
		try
		{
			org = key.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Unhandled error with hash generation. Continuing with same key... : " + e.getMessage());
			return key;
		}
		hash = digest.digest(org);
		byte[] b64 = Base64.encodeBase64(hash);
		try {
			result = new String(b64, "UTF-8");
		}
		catch (Exception e)
		{
			// should never happen
			logger.error("Unhandled error with salt generation. Continuing with void salt... : " + e.getMessage());
		}
		return result;
	}
	
	public static boolean compareHashedSaltedPassword(String password, String salt, String hash)
	{
		String finalPass = salt + password;
		return hash.equals(getHash(finalPass));
	}
	
}
