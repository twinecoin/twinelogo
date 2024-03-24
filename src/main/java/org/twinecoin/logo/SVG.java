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
import java.awt.Color;
import java.util.List;


public class SVG {
	public static void openSVG(StringBuilder buf) {
		buf.append("<?xml version=\"1.0\"?>\n");
		buf.append("<svg width=\"256\" height=\"256\" xmlns=\"http://www.w3.org/2000/svg\">\n");
	}

	public static void closeSVG(StringBuilder buf) {
		buf.append("</svg>\n");
	}

	public static void writePolyLine(StringBuilder buf, List<Float> points, boolean ink, boolean closed, int w) {
		int c = ink ? 0 : 255;
		writePolyLine(buf, points, c, c, c, closed, w);
	}

	public static void writePolyLine(StringBuilder buf, List<Float> points, Color color, boolean closed, int w) {
		writePolyLine(buf, points, color.getRed(), color.getGreen(), color.getBlue(), closed, w);
	}

	public static void writePolyLine(StringBuilder buf, List<Float> points, int r, int g, int b, boolean closed, int w) {
		if (closed) {
			buf.append("  <polygon ");
		} else {
			buf.append("  <polyline ");
		}
		StringBuilder pointsBuilder = new StringBuilder();
		for (int i = 0; i < points.size(); i += 2) {
			if (i != 0) {
				writeSpace(pointsBuilder);
			}
			writeFloat(pointsBuilder, points.get(i));
			pointsBuilder.append(", ");
			writeFloat(pointsBuilder, points.get(i + 1));
			writeSpace(pointsBuilder);
		}
		writeTag(buf, "points", pointsBuilder.toString());
		writeSpace(buf);

		writeTag(buf, "style", "fill:none;stroke-width:" + w + ";stroke:rgb(" + r + ", " + g + ", " + b + ")" + ";stroke-linecap:round");

		buf.append("/>\n");
	}

	public static void writeLine(StringBuilder buf, float x1, float y1, float x2, float y2) {
		writeLine(buf, x1, y1, x2, y2, true);
	}

	public static void writeLine(StringBuilder buf, float x1, float y1, float x2, float y2, boolean ink) {
		writeLine(buf, x1, y1, x2, y2, ink, 1);
	}

	public static void writeLine(StringBuilder buf, float x1, float y1, float x2, float y2, boolean ink, int w) {
		writeLine(buf, x1, y1, x2, y2, ink, w, false);
	}

	public static void writeLine(StringBuilder buf, float x1, float y1, float x2, float y2, boolean ink, int w, boolean linecap) {
		buf.append("  <line ");
		writeTag(buf, "x1", x1);
		writeSpace(buf);
		writeTag(buf, "y1", y1);
		writeSpace(buf);
		writeTag(buf, "x2", x2);
		writeSpace(buf);
		writeTag(buf, "y2", y2);
		writeSpace(buf);
		String c = ink ? "0" : "255";
		String style = "stroke:rgb(" + c + ", " + c + ", " + c + ");stroke-width:" + w;
		if (linecap) {
			style += ";stroke-linecap:round";
		}
		writeTag(buf, "style", style);
		buf.append("/>\n");
	}

	public static void writeCircle(StringBuilder buf, float cx, float cy, float r, Color ink, Color fill, int dots) {
		float w = fill != null ? 0 : 2;
		r = fill != null ? r : (r - (w / 2));

		buf.append("  <circle ");
		writeTag(buf, "cx", cx);
		writeSpace(buf);
		writeTag(buf, "cy", cy);
		writeSpace(buf);
		writeTag(buf, "r", r);
		writeSpace(buf);
		Color tempColor = fill == null ? ink : fill;
		String colorString = "rgb(" + tempColor.getRed() + ", " + tempColor.getGreen() + ", " + tempColor.getBlue() + ")";
		String style = "";
		if (ink != null) {
			style += "stroke:" + colorString + ";";
		}

		style += "fill:";
		style += fill != null ? colorString : "none";
		style += ";";

		style += "stroke-width:";
		style += w;
		style += ";";

		if (dots > 0) {
			style += "stroke-linecap:round;";

			style += "stroke-dasharray:";

			float length = (float) (2 * Math.PI * r);
			float step = length / 2 / dots;

			float dash = (float) (step * 0.125);
			float space = step - dash;
			style += dash + ", " + space;

			style += ";";
		}

		writeTag(buf, "style", style);
		buf.append("/>\n");
	}

	private static void writeSpace(StringBuilder buf) {
		buf.append(" ");
	}

	private static void writeTag(StringBuilder buf, String tag, String s) {
		buf.append(tag);
		buf.append("=");
		buf.append("\"" + s + "\"");
	}

	private static void writeTag(StringBuilder buf, String tag, float x) {
		buf.append(tag);
		buf.append("=\"");
		writeFloat(buf, x);
		buf.append("\"");
	}

	private static void writeFloat(StringBuilder buf, float x) {
		buf.append(x);
	}

	private static void writeFloat(StringBuilder buf, float x, int r) {
		String format = String.format("%%.%df", r);
		buf.append(String.format(format, x));
	}

}
