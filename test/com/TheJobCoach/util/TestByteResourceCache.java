package com.TheJobCoach.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestByteResourceCache
{
	@Test
	public void test()
	{
		byte[] result1 = ByteResourceCache.getByteResource("/com/TheJobCoach/util/test/byte_file.txt");
		byte[] result2 = ByteResourceCache.getByteResource("/com/TheJobCoach/util/test/byte_file2.txt");
		byte[] result3 = ByteResourceCache.getByteResource("/com/TheJobCoach/util/test/byte_file.txt");

		assertTrue(result1[0] == 'C');
		assertTrue(result1[1] == 'O');
		assertEquals(17, result1.length);
		
		assertTrue(result2[0] == 'T');
		assertTrue(result2[1] == 'T');
		assertEquals(4, result2.length);
		
		// check same object.
		assertEquals(result1, result3);
	}
	
}

