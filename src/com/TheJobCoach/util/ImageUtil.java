package com.TheJobCoach.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil
{
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public static byte[] resizeImage(String context, byte[] source, int max)
	{
		if (source == null)
			return null;
		InputStream is = new ByteArrayInputStream(source);
		BufferedImage img;
		try
		{
			img = ImageIO.read(is);
		}
		catch (IOException e1)
		{
			logger.debug("Error reading image from context " + context);
			return null;
		}
		if (img == null)
			return null;

		int width = img.getWidth();
		int height = img.getHeight();
		
		int newWidth = 0;
		int newHeight = 0;
	
		if (width > height)
		{
			newWidth = max;
			newHeight = height * max / width;
		}
		else
		{
			newHeight = max;
			newWidth = width * max / height;
		}
		
		logger.debug("Conversion from " + width + "x" + height + " to: "  + newWidth + "x" + newHeight);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		BufferedImage dbi = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dbi.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance((float)newWidth / (float)width, (float)newHeight / (float)height);
        g.drawRenderedImage(img, at);
		
		try
		{
			ImageIO.write(dbi, "jpg", baos);
		}
		catch (IOException e)
		{
			logger.debug("Error writing image from context " + context);
			return null;
		}		
		return baos.toByteArray();
	}
}
