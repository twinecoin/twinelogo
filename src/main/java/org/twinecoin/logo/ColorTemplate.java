package org.twinecoin.logo;

import java.awt.Color;

public class ColorTemplate {
	public static ColorTemplate YELLOW_ON_BLUE = new ColorTemplate(
			new Color(255, 255, 64),  // TwistA
			new Color(192, 192, 48),  // TwistB
			new Color(128, 128, 32),  // TwistC
			new Color(192, 192, 48),  // Curve
			new Color(128, 128, 32),  // CurveBorder
			new Color(64, 64, 208),   // Coin
			new Color(255, 255, 64)); // Dots

	public static ColorTemplate YELLOW_ON_GREEN = new ColorTemplate(
			new Color(255, 255, 64),  // TwistA
			new Color(224, 224, 56),  // TwistB
			new Color(196, 196, 48),  // TwistC
			new Color(255, 255, 64),  // Curve
			new Color(128, 128, 32),  // CurveBorder
			new Color(0, 160, 0),   // Coin
			new Color(255, 255, 64)); // Dots

	public final Color[] twistColors;
	public final Color curveColor;
	public final Color curveBorderColor;
	public final Color coinColor;
	public final Color dotsColor;

	private ColorTemplate(Color twistA, Color twistB, Color twistC,
			Color curveColor, Color curveBorderColor,
			Color coinColor, Color dotsColor) {
		this.twistColors = new Color[] {
				twistA, twistB, twistC
		};
		this.curveColor = curveColor;
		this.curveBorderColor = curveBorderColor;
		this.coinColor = coinColor;
		this.dotsColor = dotsColor;
	}
}
