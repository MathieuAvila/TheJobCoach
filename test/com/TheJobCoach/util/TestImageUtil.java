package com.TheJobCoach.util;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class TestImageUtil
{
	/*public static byte[] resizeImage(String context, byte[] source, int max)*/
	
	// byte[] getByteResource(String key);

	public void writeToFile(String path, byte[] img) throws IOException
	{
		FileOutputStream writer = new FileOutputStream(path);
		writer.write(img, 0 , img.length);
		writer.close();
	}

	void simpleCompare(String srcRes, String dstRes, int factor) throws IOException
	{
		byte[] src = ByteResourceCache.getByteResource(srcRes);
		byte[] dst = ImageUtil.resizeImage("test", src, factor);
		
		writeToFile("/tmp/test.jpg", dst);
		
		byte[] cmp = ByteResourceCache.getByteResource(dstRes);
		assertTrue(Arrays.equals(dst, cmp));
	}
	
	@Test
	public void test_resizeImage() throws IOException
	{
		// std cases, from PNG
		simpleCompare("/com/TheJobCoach/util/test/test1_97x140.png", "/com/TheJobCoach/util/test/test1_97x140_64_44x64.jpg", 64);
		simpleCompare("/com/TheJobCoach/util/test/test2_300x225.png", "/com/TheJobCoach/util/test/test2_300x225_64_64x48.jpg", 64);
		
		// tricky ones.
		assertNull(ImageUtil.resizeImage("test", new byte[0], 64));
		assertNull(ImageUtil.resizeImage("test", null, 64));
	}
	

}
