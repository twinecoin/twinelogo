package org.twinecoin.logo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TwineLogo {

	public static void main(String[] args) {
		
		StringBuilder buf = new StringBuilder();
		
		SVG.openSVG(buf);
		
		for (int s = 0; s < 360; s += 120) {
			writeTwist(buf, 128, 128, 96, 12, s, 12, true);
		}
		
		writeT(buf);
		
		SVG.closeSVG(buf);
		
		String SVGString = buf.toString();
		
		File dir = new File("img");
		dir.mkdirs();
		
		File file = new File(dir, "twinelogo.svg");
		
		if (!writeOutputFile(file, SVGString)) {
			System.out.println("Unable to open file " + file + " for writing");
		}
	}
	
	public static void writeT(StringBuilder buf) {
		List<Float> points = new ArrayList<Float>();

		for (int x = 72; x <= 184; x += 1) {
			points.add((float) x);
			float h = (float) ((32.0 * Math.pow(128 - x, 2)) / Math.pow(64, 2));
			points.add(82 + h);
		}
		
		SVG.writePolyLine(buf, points, true, false, 20);
		
		SVG.writeLine(buf, 114, 67, 114, 189, true, 8, true);
		SVG.writeLine(buf, 128, 64, 128, 192, true, 8, true);
		SVG.writeLine(buf, 142, 67, 142, 189, true, 8, true);
	
	}
	
	public static void writeTwist(StringBuilder buf, float cx, float cy, float r, float tr, float degreesShift, int twistRate, boolean hideInward) {
		float et = (float) (Math.PI * 2);
		
		float shift = (float) (Math.PI * degreesShift / 180);
		
		float threshold = (float) 0.5;
		
		List<Float> points = new ArrayList<Float>();
		
		int Xstep = 1;
		
		int startX = 0;
		float cosTwist;
		
		do {
			float th = (et * startX) / 360;
			float twTh = shift + twistRate * th;
			
			cosTwist = (float) Math.cos(twTh);
			
			startX += Xstep;
		} while (hideInward && cosTwist < threshold);
		
		boolean active = !hideInward;
				
		for (int x = startX; x < 360 + startX; x += Xstep) {
			float th = (et * x) / 360;
			float twTh = shift + twistRate * th;
			
			cosTwist = (float) Math.cos(twTh);
			
			boolean draw = cosTwist < threshold || !hideInward;
			
			if (draw && !active) {
				active = true;
			}
			
			if (active && !draw) {
				SVG.writePolyLine(buf, points, true, false, 2);
				points.clear();
				active = false;
			}
			
			if (draw) {
				float R = (float) (r + tr * Math.sin(twTh));

				points.add((float) (cx + R * Math.sin(th)));
				points.add((float) (cx + R * Math.cos(th)));
			}
		}
		
		if (points.size() > 0) {
			SVG.writePolyLine(buf, points, true, false, 2);
		}

	}
	
	private static boolean writeOutputFile(File file, String data) {
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
			
			writer.write(data);
			
			writer.close();
		} catch (IOException e) {
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {}
			}
		}
		return true;
	}
}
