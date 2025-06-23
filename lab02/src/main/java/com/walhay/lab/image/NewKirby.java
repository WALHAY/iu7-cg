package com.walhay.lab.image;

import static com.walhay.lab.utils.VectorHelper.*;

import com.walhay.lab.interfaces.*;
import com.walhay.lab.primitives.*;
import com.walhay.lab.utils.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;

public class NewKirby implements IAffineTransform, IDrawable {
	private LinkedList<IDrawable> drawables = new LinkedList<>();

	public static final int WIDTH = 920;
	public static final int HEIGHT = 783;

	private static final Color BODY_COLOR = new Color(239, 165, 198, 255);
	private static final Color UNDER_LEG_COLOR = new Color(208, 31, 99, 255);
	private static final Color UPPER_LEG_COLOR = new Color(160, 34, 95, 255);
	private static final Color CHEEK_COLOR = new Color(223, 99, 151, 255);
	private static final Color TONGUE_COLOR = new Color(221, 118, 161, 255);
	private static final Color MOUTH_COLOR = new Color(107, 33, 99, 255);
	private static final Color EYE_COLOR = new Color(30, 124, 188, 255);

	private Point midPoint = new Point(WIDTH / 2, HEIGHT / 2);

	// Used for measuring stroke size coefficient
	private Line lineUnderLeg;

	private static final double LINE_SCALE_FACTOR = 0.005;

	private Basis basis = new Basis();

	public NewKirby() {
		Point legCenter = new Point(460, HEIGHT - 18);
		Ellipse legUnder = new Ellipse(legCenter, 389, 268, UNDER_LEG_COLOR, Color.BLACK);
		Ellipse legUpper = new Ellipse(legCenter.copy(), 307, 176, UPPER_LEG_COLOR, UPPER_LEG_COLOR);

		Rectangle legUnderHider = new Rectangle(new Point(0, HEIGHT - 17), new Point(WIDTH, HEIGHT - 17),
												new Point(0, HEIGHT + 400), Color.WHITE);

		Rectangle legBetweenHider =
			new Rectangle(new Point(WIDTH / 2 - 25, HEIGHT - 120), new Point(WIDTH / 2 + 25, HEIGHT - 120),
						  new Point(WIDTH / 2 - 25, HEIGHT), Color.WHITE, Color.BLACK);

		Point lineOffset = new Point(389, 0);
		lineUnderLeg = new Line(removeVector(legCenter, lineOffset), addVector(legCenter, lineOffset));

		Point bodyCenter = new Point(460, HEIGHT - 441);
		Point leftHandCenter = new Point(140, HEIGHT - 402);
		Point rightHandCenter = new Point(810, 402);
		Point leftCheekCenter = addVector(bodyCenter, new Point(-127, 41));
		Point rightCheekCenter = addVector(bodyCenter, new Point(178, 33));

		Ellipse leftHand = new Ellipse(leftHandCenter, addVector(leftHandCenter, new Point(-120, 43)),
									   addVector(leftHandCenter, new Point(-30, -86)), BODY_COLOR, Color.BLACK);
		Ellipse rightHand = new Ellipse(rightHandCenter, addVector(rightHandCenter, new Point(59, -66)),
										addVector(rightHandCenter, new Point(84, 75)), BODY_COLOR, Color.BLACK);

		Ellipse body = new Ellipse(bodyCenter, 321, 321, BODY_COLOR, Color.BLACK);
		Ellipse leftCheek = new Ellipse(leftCheekCenter, 47, 28, CHEEK_COLOR, CHEEK_COLOR);
		Ellipse rightCheek = new Ellipse(rightCheekCenter, 47, 28, CHEEK_COLOR, CHEEK_COLOR);

		BezierCurve mouthUpper = new BezierCurve(new Point(436, HEIGHT - 403), MOUTH_COLOR, Color.BLACK);
		mouthUpper.addPoint(new Point(465, HEIGHT - 420), new Point(445, HEIGHT - 412), new Point(455, HEIGHT - 417));
		mouthUpper.addPoint(new Point(488, HEIGHT - 419), new Point(472, HEIGHT - 421), new Point(480, HEIGHT - 420));
		mouthUpper.addPoint(new Point(519, HEIGHT - 403), new Point(488, HEIGHT - 419), new Point(509, HEIGHT - 412));

		BezierCurve mouthLower = new BezierCurve(new Point(436, HEIGHT - 403), MOUTH_COLOR, Color.BLACK);
		mouthLower.addPoint(new Point(449, HEIGHT - 376), new Point(439, HEIGHT - 393), new Point(443, HEIGHT - 384));
		mouthLower.addPoint(new Point(480, HEIGHT - 339), new Point(456, HEIGHT - 362), new Point(470, HEIGHT - 339));
		mouthLower.addPoint(new Point(510, HEIGHT - 376), new Point(491, HEIGHT - 340), new Point(504, HEIGHT - 362));
		mouthLower.addPoint(new Point(519, HEIGHT - 403), new Point(515, HEIGHT - 384), new Point(519, HEIGHT - 403));
		mouthUpper.addCurve(mouthLower);

		BezierCurve curve = new BezierCurve(new Point(449, HEIGHT - 376), TONGUE_COLOR, Color.BLACK);
		curve.addPoint(new Point(480, HEIGHT - 384), new Point(458, HEIGHT - 381), new Point(468, HEIGHT - 384));
		curve.addPoint(new Point(510, HEIGHT - 376), new Point(490, HEIGHT - 385), new Point(500, HEIGHT - 382));

		BezierCurve curveLower = new BezierCurve(new Point(449, HEIGHT - 376), MOUTH_COLOR, Color.BLACK);
		curveLower.addPoint(new Point(480, HEIGHT - 339), new Point(456, HEIGHT - 362), new Point(470, HEIGHT - 339));
		curveLower.addPoint(new Point(510, HEIGHT - 376), new Point(491, HEIGHT - 340), new Point(504, HEIGHT - 362));

		curve.addCurve(curveLower);

		Collections.addAll(drawables, legUnder, legUpper, lineUnderLeg, legBetweenHider, legUnderHider, leftHand,
						   rightHand, body, mouthUpper, curve, leftCheek, rightCheek);
		drawables.addAll(getEyeLeft());
		drawables.addAll(getEyeRight());
	}

	private LinkedList<BezierCurve> getEyeLeft() {
		LinkedList<BezierCurve> parts = new LinkedList<>();
		BezierCurve leftEyeUp = new BezierCurve(new Point(368, HEIGHT - 558), Color.WHITE, Color.BLACK);
		leftEyeUp.addPoint(new Point(407, HEIGHT - 614), new Point(371, HEIGHT - 581), new Point(362, HEIGHT - 620));
		leftEyeUp.addPoint(new Point(437, HEIGHT - 530), new Point(434, HEIGHT - 594), new Point(439, HEIGHT - 559));

		BezierCurve leftEyeDown = new BezierCurve(new Point(438, HEIGHT - 509), EYE_COLOR, Color.BLACK);
		leftEyeDown.addPoint(new Point(422, HEIGHT - 455), new Point(436, HEIGHT - 491), new Point(435, HEIGHT - 467));
		leftEyeDown.addPoint(new Point(407, HEIGHT - 445), new Point(417, HEIGHT - 452), new Point(412, HEIGHT - 446));
		leftEyeDown.addPoint(new Point(381, HEIGHT - 462), new Point(399, HEIGHT - 443), new Point(384, HEIGHT - 455));
		leftEyeDown.addPoint(new Point(368, HEIGHT - 519), new Point(372, HEIGHT - 479), new Point(368, HEIGHT - 500));

		BezierCurve leftEyeMiddleUp = new BezierCurve(new Point(437, HEIGHT - 530), Color.BLACK);
		leftEyeMiddleUp.addPoint(new Point(393, HEIGHT - 530), new Point(417, HEIGHT - 529),
								 new Point(408, HEIGHT - 528));
		leftEyeMiddleUp.addPoint(new Point(368, HEIGHT - 558), new Point(382, HEIGHT - 532),
								 new Point(378, HEIGHT - 544));

		BezierCurve leftEyeMiddleDown = new BezierCurve(new Point(368, HEIGHT - 519), Color.BLACK);
		leftEyeMiddleDown.addPoint(new Point(387, HEIGHT - 508), new Point(375, HEIGHT - 513),
								   new Point(382, HEIGHT - 509));
		leftEyeMiddleDown.addPoint(new Point(415, HEIGHT - 505), new Point(399, HEIGHT - 504),
								   new Point(408, HEIGHT - 503));
		leftEyeMiddleDown.addPoint(new Point(437, HEIGHT - 509), new Point(424, HEIGHT - 505),
								   new Point(431, HEIGHT - 506));

		BezierCurve leftEyeMiddle = new BezierCurve(new Point(437, HEIGHT - 530), Color.BLACK, Color.BLACK);
		leftEyeMiddle.addPoint(new Point(393, HEIGHT - 530), new Point(417, HEIGHT - 529),
							   new Point(408, HEIGHT - 528));
		leftEyeMiddle.addPoint(new Point(368, HEIGHT - 558), new Point(382, HEIGHT - 532),
							   new Point(378, HEIGHT - 544));
		leftEyeMiddle.addPoint(new Point(368, HEIGHT - 519), new Point(368, HEIGHT - 519),
							   new Point(368, HEIGHT - 519));
		leftEyeMiddle.addPoint(new Point(387, HEIGHT - 508), new Point(375, HEIGHT - 513),
							   new Point(382, HEIGHT - 509));
		leftEyeMiddle.addPoint(new Point(415, HEIGHT - 505), new Point(399, HEIGHT - 504),
							   new Point(408, HEIGHT - 503));
		leftEyeMiddle.addPoint(new Point(437, HEIGHT - 509), new Point(424, HEIGHT - 505),
							   new Point(431, HEIGHT - 506));
		leftEyeMiddle.addPoint(new Point(437, HEIGHT - 530), new Point(437, HEIGHT - 530),
							   new Point(437, HEIGHT - 530));

		leftEyeUp.addCurve(leftEyeMiddleUp);
		leftEyeDown.addCurve(leftEyeMiddleDown);

		Collections.addAll(parts, leftEyeMiddle, leftEyeUp, leftEyeDown);

		return parts;
	}

	private LinkedList<BezierCurve> getEyeRight() {
		LinkedList<BezierCurve> parts = new LinkedList<>();
		BezierCurve rightEyeUp = new BezierCurve(new Point(602, HEIGHT - 558), Color.WHITE, Color.BLACK);
		rightEyeUp.addPoint(new Point(563, HEIGHT - 614), new Point(599, HEIGHT - 581), new Point(608, HEIGHT - 620));
		rightEyeUp.addPoint(new Point(533, HEIGHT - 530), new Point(536, HEIGHT - 594), new Point(531, HEIGHT - 559));

		BezierCurve rightEyeDown = new BezierCurve(new Point(532, HEIGHT - 509), EYE_COLOR, Color.BLACK);
		rightEyeDown.addPoint(new Point(548, HEIGHT - 455), new Point(534, HEIGHT - 491), new Point(535, HEIGHT - 467));
		rightEyeDown.addPoint(new Point(563, HEIGHT - 445), new Point(553, HEIGHT - 452), new Point(558, HEIGHT - 446));
		rightEyeDown.addPoint(new Point(589, HEIGHT - 462), new Point(571, HEIGHT - 443), new Point(586, HEIGHT - 455));
		rightEyeDown.addPoint(new Point(602, HEIGHT - 519), new Point(598, HEIGHT - 479), new Point(602, HEIGHT - 500));

		BezierCurve rightEyeMiddleUp = new BezierCurve(new Point(533, HEIGHT - 530), Color.BLACK);
		rightEyeMiddleUp.addPoint(new Point(577, HEIGHT - 530), new Point(553, HEIGHT - 529),
								  new Point(562, HEIGHT - 528));
		rightEyeMiddleUp.addPoint(new Point(602, HEIGHT - 558), new Point(588, HEIGHT - 532),
								  new Point(592, HEIGHT - 544));

		BezierCurve rightEyeMiddleDown = new BezierCurve(new Point(602, HEIGHT - 519), Color.BLACK);
		rightEyeMiddleDown.addPoint(new Point(583, HEIGHT - 508), new Point(595, HEIGHT - 513),
									new Point(588, HEIGHT - 509));
		rightEyeMiddleDown.addPoint(new Point(555, HEIGHT - 505), new Point(571, HEIGHT - 504),
									new Point(562, HEIGHT - 503));
		rightEyeMiddleDown.addPoint(new Point(533, HEIGHT - 509), new Point(546, HEIGHT - 505),
									new Point(539, HEIGHT - 506));

		BezierCurve rightEyeMiddle = new BezierCurve(new Point(533, HEIGHT - 530), Color.BLACK, Color.BLACK);
		rightEyeMiddle.addPoint(new Point(577, HEIGHT - 530), new Point(553, HEIGHT - 529),
								new Point(562, HEIGHT - 528));
		rightEyeMiddle.addPoint(new Point(602, HEIGHT - 558), new Point(588, HEIGHT - 532),
								new Point(592, HEIGHT - 544));
		rightEyeMiddle.addPoint(new Point(602, HEIGHT - 519), new Point(602, HEIGHT - 519),
								new Point(602, HEIGHT - 519));
		rightEyeMiddle.addPoint(new Point(583, HEIGHT - 508), new Point(592, HEIGHT - 513),
								new Point(588, HEIGHT - 509));
		rightEyeMiddle.addPoint(new Point(555, HEIGHT - 505), new Point(571, HEIGHT - 504),
								new Point(562, HEIGHT - 503));
		rightEyeMiddle.addPoint(new Point(533, HEIGHT - 509), new Point(546, HEIGHT - 505),
								new Point(539, HEIGHT - 506));
		rightEyeMiddle.addPoint(new Point(533, HEIGHT - 530), new Point(533, HEIGHT - 530),
								new Point(533, HEIGHT - 530));

		rightEyeUp.addCurve(rightEyeMiddleUp);
		rightEyeDown.addCurve(rightEyeMiddleDown);

		Collections.addAll(parts, rightEyeMiddle, rightEyeUp, rightEyeDown);

		return parts;
	}

	@Override
	public void draw(Graphics graphics) {
		Graphics2D g2d = (Graphics2D)graphics;
		float strokeSize = (float)(lineUnderLeg.getLineLength() * LINE_SCALE_FACTOR);
		g2d.setStroke(new BasicStroke(strokeSize));
		for (IDrawable drawable : drawables) drawable.draw(graphics);
	}

	@Override
	public void rotate(double angle) {
		basis.rotate(angle);
		updateBasis();
	}

	@Override
	public void scale(double scale) {
		basis.scale(scale);
		updateBasis();
	}

	@Override
	public void transpose(Vector2D move) {
		basis.transpose(move);
		updateBasis();
	}

	@Override
	public void changeBasis(Basis basis) {
		this.basis = basis;
		updateBasis();
	}

	private void updateBasis() {
		for (IDrawable drawable : drawables) drawable.changeBasis(basis);
	}
}
