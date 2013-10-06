package com.TheJobCoach.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestStringResourceCache
{
	@Test
	public void test()
	{
		String result1 = StringResourceCache.getStringResource("/com/TheJobCoach/util/test/string_file.html");
		String result2 = StringResourceCache.getStringResource("/com/TheJobCoach/util/test/string_file2.html");
		String result3 = StringResourceCache.getStringResource("/com/TheJobCoach/util/test/string_file.html");
		System.out.println(result1);
		System.out.println(result2);
		assertEquals(true, result1.contains("CONTENU2"));
		assertEquals(true, result2.contains("CONTENU4"));
		assertEquals(result1, result3);
	}
	
}

