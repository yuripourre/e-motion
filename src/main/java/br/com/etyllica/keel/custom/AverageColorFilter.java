package br.com.etyllica.keel.custom;

import java.awt.image.BufferedImage;
import java.util.List;

import br.com.etyllica.commons.graphics.Color;
import br.com.etyllica.linear.Point2D;
import br.com.etyllica.keel.core.ProcessComponentFilter;
import br.com.etyllica.keel.core.ProcessFilter;
import br.com.etyllica.keel.core.helper.ColorHelper;
import br.com.etyllica.keel.feature.Component;

public class AverageColorFilter implements ProcessFilter<Color>, ProcessComponentFilter<Color> {
	
	public AverageColorFilter() {
		super();
	}
	
	public Color process(BufferedImage buffer) {
		return filter(buffer);
	}
	
	public static Color filter(BufferedImage buffer) {
		int averageRed = 0;
		int averageBlue = 0;
		int averageGreen = 0;
		
		int pixelCount = 0;
						
		for(int j=0; j<buffer.getHeight(); j++) {

			for(int i=0; i<buffer.getWidth(); i++) {
				
				int rgb = buffer.getRGB(i, j);
				
				averageRed += ColorHelper.getRed(rgb);
				
				averageBlue += ColorHelper.getBlue(rgb);
				
				averageGreen += ColorHelper.getGreen(rgb);
				
				pixelCount++;	
			}
		}
		
		averageRed /= pixelCount;
		averageBlue /= pixelCount;
		averageGreen /= pixelCount;
		
		return new Color(averageRed, averageGreen, averageBlue);
	}
	
	@Override
	public Color process(BufferedImage buffer, Component component) {
		return filter(buffer, component);
	}
	
	public static Color filter(BufferedImage buffer, Component component) {
		
		int averageRed = 0;
		int averageBlue = 0;
		int averageGreen = 0;
		
		int pixelCount = 0;
		
		List<Point2D> points = component.getPoints();
		
		for(Point2D point: points) {
			
			int i = (int)point.getX();
			
			int j = (int)point.getY();
			
			int rgb = buffer.getRGB(i, j);
			
			averageRed += ColorHelper.getRed(rgb);
			
			averageBlue += ColorHelper.getBlue(rgb);
			
			averageGreen += ColorHelper.getGreen(rgb);
			
			pixelCount ++;
			
		}
			
		averageRed /= pixelCount;
		
		averageBlue /= pixelCount;
		
		averageGreen /= pixelCount;
		
		return new Color(averageRed, averageGreen, averageBlue);
		
	}
		
}
