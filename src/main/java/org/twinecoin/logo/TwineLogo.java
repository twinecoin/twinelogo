/**
 * MIT License
 * 
 * Copyright (c) 2017 Twinecoin Developers
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
		
		SVG.writeCircle(buf, 128, 128, 127, false, true, 0);
		
		SVG.writeCircle(buf, 128, 128, 85, true, false, 36);
		
		for (int s = 0; s < 360; s += 120) {
			writeTwist(buf, 128, 128, 106, 18, s, 6, true, 2);
		}

		writeT(buf);
		
		SVG.closeSVG(buf);
		
		File dir = new File("img");
		dir.mkdirs();
		
		File file = new File(dir, "twinelogo.svg");
		
		writeFile(buf, file);
		
		buf = new StringBuilder();

		writeHTML(buf);
		
		file = new File(dir, "twinelogo.html");
		
		writeFile(buf, file);
	}
	
	public static void writeFile(StringBuilder buf, File file) {
		String string = buf.toString();
		if (!writeOutputFile(file, string)) {
			System.out.println("Unable to open file " + file + " for writing");
		}
	}
	
	public static void writeHTML(StringBuilder buf) {
		buf.append("<html>\n");
		buf.append(" <head>\n");
		buf.append(" </head>\n");
		buf.append(" <body style=\"background-color:lightgrey\">\n");
		buf.append("  <img src=\"./twinelogo.svg\"/>\n");
		buf.append(" </body>\n");
		buf.append("</html>\n");
		
	}
	
	public static void writeT(StringBuilder buf) {
		List<Float> points = new ArrayList<Float>();

		for (int x = 72; x <= 184; x += 1) {
			points.add((float) x);
			float h = (float) ((32.0 * Math.pow(128 - x, 2)) / Math.pow(64, 2));
			points.add(82 + h);
		}
		
		SVG.writePolyLine(buf, points, true, false, 20);
		
		float r = 106;

		float rate = (float) (2 * Math.PI * r / 128);

		for (int s = 0; s < 360; s += 120) {
			writeTwistLine(buf, 128, 64, 128, 192, r, 12, s, rate, true, 6);
		}
	}
	
	private static boolean drawTest(float cosTwist) {
		return cosTwist > 0.99 || cosTwist < 0.55;
	}
	
	public static void writeTwist(StringBuilder buf, float cx, float cy, float r, float tr, float degreesShift, float twistRate, boolean hideInward, int w) {
		writeTwist(buf, cx, cy, 0, 0, r, tr, degreesShift, twistRate, false, hideInward, w);
	}

	public static void writeTwistLine(StringBuilder buf, float sx, float sy, float ex, float ey, float r, float tr, float degreesShift, float twistRate, boolean hideInward, int w) {
		writeTwist(buf, sx, sy, ex, ey, r, tr, degreesShift, twistRate, true, hideInward, w);
	}

	public static void writeTwist(StringBuilder buf, float cx, float cy, float ex, float ey, float r, float tr, float degreesShift, float twistRate, boolean line, boolean hideInward, int w) {
		float et = (float) (Math.PI * 2);
		
		float shift = (float) (Math.PI * degreesShift / 180);
		
		float dx = ex - cx;
		float dy = ey - cy;
		float mag = (float) Math.sqrt(dx * dx + dy * dy);
		dx /= mag;
		dy /= mag;

		float endDegrees = line ? (360 * mag / et / r) : 360;

		List<Float> points = new ArrayList<Float>();
		
		float Xstep = (float) 0.1;
		
		float startX = line ? -360 : 0;
		float cosTwist;
		
		do {
			float th = (et * startX) / endDegrees;
			float twTh = shift + twistRate * th;
			
			cosTwist = (float) Math.cos(twTh);
			
			startX += Xstep;
		} while (hideInward && drawTest(cosTwist));
		
		boolean active = !hideInward;

		float end = line ? endDegrees : (endDegrees + startX);
				
		for (float x = startX; x <= end; x += Xstep) {
			float th = (et * x) / endDegrees;
			float twTh = shift + twistRate * th;
			
			cosTwist = (float) Math.cos(twTh);
			
			boolean draw = drawTest(cosTwist) || !hideInward;
			
			if (draw && !active) {
				active = true;
			}
			
			if (active && !draw) {
				if (points.size() > 0) {
					SVG.writePolyLine(buf, points, true, false, w);
					points.clear();
				}
				active = false;
			}
			
			if (draw) {
				if (!line) {
					float R = (float) (r + tr * Math.sin(twTh));

					points.add((float) (cx + R * Math.sin(th)));
					points.add((float) (cx + R * Math.cos(th)));
				} else {
					float D = (float) (tr * Math.sin(twTh));

					float d = th * r;

					if (d >= 0 && d <= mag) {
						float lineX = cx + dx * d - dy * D;
						float lineY = cy + dy * d + dx * D;

						points.add(lineX);
						points.add(lineY);
					}
				}
			}
		}
		
		if (points.size() > 0) {
			SVG.writePolyLine(buf, points, true, false, w);
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
