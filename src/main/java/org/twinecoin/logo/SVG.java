package org.twinecoin.logo;
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
		
		String c = ink ? "0" : "255";
		writeTag(buf, "style", "fill:none;stroke-width:" + w + ";stroke:rgb(" + c + ", " + c + ", " + c + ")" + ";stroke-linecap:round");
		
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
		writeFloat(buf, x, 1);
	}
	
	private static void writeFloat(StringBuilder buf, float x, int r) {
		String format = String.format("%%.%df", r);
		buf.append(String.format(format, x));
	}

}
