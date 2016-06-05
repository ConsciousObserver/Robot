package test.robot.util;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.validation.constraints.Max;

import test.robot.rest.TouchpadRest.DeltaXy;

public class RobotUtil {
	private static Robot robot;
	private static double TOUCHPAD_SPEED = .7;
	private static int DELTA_MIN = 10;
	private static int DELTA_MAX = 100;
	private static int MAX_HOP = 30;
	
	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static File captureScreen() {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage capture = null;
		File file = null;
		try {
			capture = robot.createScreenCapture(screenRect);
			String fileName = getUniqueName();
			file = File.createTempFile(fileName, ".jpg");
			ImageIO.write(capture, "JPEG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}

	public static void click(int x, int y) {
		System.out.println(x + " : " + y);
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	public static void move(int x, int y) {
		robot.mouseMove(x, y);
	}

	public static void moveDelta(int deltaX, int deltaY, long delay, boolean varrySpeed) {
		double distance = hypotenuse(deltaX, deltaY);
		if(distance > DELTA_MIN) {
			if(varrySpeed) {
				deltaX = (int)(deltaX * Math.abs(deltaX) * TOUCHPAD_SPEED); //more distance covered for fast movements
				deltaY = (int)(deltaY * Math.abs(deltaY) * TOUCHPAD_SPEED);
			}
		}
		double newDistance = hypotenuse(deltaX, deltaY);
		//System.out.println(newDistance);
		if(newDistance > DELTA_MAX) {
			double ratio = DELTA_MAX / newDistance;
			deltaX *= ratio;
			deltaY *= ratio;
			newDistance = DELTA_MAX;
		}

		/*if(newDistance > 2 * MAX_HOP) {
			System.out.println("smoothing : " + newDistance);
			smootheHops(deltaX, deltaY, delay, newDistance);
		}*/
		
		Point currentPoint = MouseInfo.getPointerInfo().getLocation();
		
		int moveToX = (int)(currentPoint.getX() + deltaX);
		int moveToY = (int)(currentPoint.getY() + deltaY);
		//System.out.println(moveToX + " : " + moveToY);
		
		robot.mouseMove(moveToX, moveToY);
	}

	/*private static void smootheHops(int deltaX, int deltaY, long delay, double newDistance) {
		long currentTime = System.currentTimeMillis();
		double ratio = MAX_HOP / newDistance;
		int hopCount = (int)(newDistance / MAX_HOP);
		int xHop = (int)(deltaX * ratio);
		int yHop = (int)(deltaY * ratio);
		int timeQuanta = (int) (delay * ratio);
		List<DeltaXy> deltaList = new ArrayList<DeltaXy>();
		for(int i = 0; i < hopCount; i++) {
			DeltaXy xy = new DeltaXy();
			xy.setDeltaX(xHop);
			xy.setDeltaY(yHop);
			currentTime += timeQuanta;
			xy.setTimestamp(currentTime);
			deltaList.add(xy);
		}
		System.out.println(deltaList.size());
		moveDelta(deltaList, false);
	}*/
	
	public static void click() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	private static String getUniqueName() {
		return UUID.randomUUID().toString();
	}
	
	public static double hypotenuse(double x, double y) {
		return Math.sqrt(x*x + y*y);
	}

	public static void moveDelta(List<DeltaXy> deltaList, boolean varrySpeed) {		
		long extraTime = 0;

		for(int i = 0; i < deltaList.size(); i++) {
			DeltaXy xy = deltaList.get(i);

			moveDelta(xy.getDeltaX(), xy.getDeltaY(), xy.getTimestamp(), varrySpeed);
			if(i > 0) {
				DeltaXy lastXy = deltaList.get(i-1);
				long sleepTime = xy.getTimestamp() - lastXy.getTimestamp() - extraTime;
				//System.out.println("extratime : " + sleepTime);
				if(sleepTime > 0) {
					extraTime = sleep(sleepTime);
				} else {
					extraTime = 0;
				}
			}
		}
	}
	
	/*
	 * This method returns the extra time it took in waking up
	 */
	public static long sleep(long time) {
		long startTime = System.currentTimeMillis();
		time = time > 200 ? 200 : time;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return System.currentTimeMillis() - startTime - time;
	}
}
