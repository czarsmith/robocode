package smi.robots.lib;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.Robot;
import robocode.Rules;
import robocode.util.Utils;
import smi.robots.lib.guns.VirtualBullet;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public class MyUtils {
    public static double getHeading(double x1, double y1, double x2, double y2) {
        double xo = x2-x1;
        double yo = y2-y1;
        double h = getDistance( x1,y1, x2,y2 );
        if( xo > 0 && yo >= 0 )
        {
            return Math.asin( xo / h );
        }
        if( xo >= 0 && yo < 0 )
        {
            return Math.PI - Math.asin( xo / h );
        }
        if( xo < 0 && yo <= 0 )
        {
            return Math.PI + Math.asin( -xo / h );
        }
        if( xo <= 0 && yo > 0 )
        {
            return 2.0*Math.PI - Math.asin( -xo / h );
        }
        return 0;
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y2 - y1;
        return Math.sqrt(x * x + y * y);
    }
    
    public static double getAngularVelocity(double headingBefore, double headingAfter,
        long timeBefore, long timeAfter) {
        if (timeBefore >= timeAfter) {
            return 0;
        }

        if (Utils.isNear(headingBefore, headingAfter)) {
            return 0;
        }

        double ret = getBearing(headingBefore, headingAfter);
        while (ret > Math.PI) ret -= Math.PI * 2;

        return ret / (timeAfter - timeBefore);
    }

    /**
     * Computes the bearing between two angles.
     * @return A value between -PI and PI
     */
    public static double getBearing(double from, double to) {
        double ret = Utils.normalAbsoluteAngle(to - from);
        while (ret > Math.PI) ret -= Math.PI * 2;
        return ret;
    }

    public static Point2D.Double computeFutureBearingCircular(double fromX, double fromY,
        double targetAngularVelocity, double targetVelocity,  double targetX,
        double targetY, double targetHeading, double firePower, double maxX, double maxY) {
        double newX = targetX;
        double newY = targetY;
        double diff;
        for (int i = 0; i < 20; i++){
            diff = Math.round(Math.sqrt(Math.pow(newX-fromX,2) +
                Math.pow(newY-fromY,2))/Rules.getBulletSpeed(firePower));
            if (Math.abs(targetAngularVelocity) > 0.00001) { // Circular
                double radius = targetVelocity/targetAngularVelocity;
                double tothead = diff * targetAngularVelocity;
                newY = targetY + (Math.sin(targetHeading + tothead) * radius) - 
                              (Math.sin(targetHeading) * radius);
                newX = targetX + (Math.cos(targetHeading) * radius) - 
                              (Math.cos(targetHeading + tothead) * radius);
            }
            else { // Linear
                newY = targetY + Math.cos(targetHeading) * targetVelocity * diff;
                newX = targetX + Math.sin(targetHeading) * targetVelocity * diff;
            }

            // Make sure we don't aim to a point outside the battlefield.
            newX = Math.max(Math.min(newX, maxX), 0);
            newY = Math.max(Math.min(newY, maxY), 0);
        }

        return new Point2D.Double(newX, newY);
    }

    public static boolean bulletHasArrived(VirtualBullet b, long now) {
        Intel enemy = RobotManager.getEnemy(b.getEnemy());
        double enemyX = enemy.getX();
        double enemyY = enemy.getY();
        double distToEnemy = MyUtils.getDistance(b.getFromX(), b.getFromY(), enemyX, enemyY);
        double bulletDistance = Rules.getBulletSpeed(b.getPower()) *
            (now - b.getStartTime());
        return bulletDistance > distToEnemy;
    }

    public static void drawCircle(Robot robot, Color c, double x, double y, int stroke) {
        robot.getGraphics().setColor(c);
        robot.getGraphics().setStroke(new BasicStroke(stroke));
        robot.getGraphics().drawOval((int)x - 5, (int)y - 5, 10, 10);
    }

    public static void drawAimPoint(Robot robot, VirtualBullet bullet) {
        if (bullet.getToX() != -1 && bullet.getToY() != -1) {
            MyUtils.drawCircle(robot, Color.RED, (int)bullet.getToX(), (int)bullet.getToY(), 2);
        }
    }
    
    public static void drawBullet(Robot robot, VirtualBullet bullet, Color color) {
        double dist = Rules.getBulletSpeed(bullet.getPower()) * (robot.getTime() - bullet.getStartTime());
        double x = bullet.getFromX() + Math.sin(bullet.getHeading()) * dist;
        double y = bullet.getFromY() + Math.cos(bullet.getHeading()) * dist;
        robot.getGraphics().setColor(color);
        robot.getGraphics().setStroke(new BasicStroke(2));
        robot.getGraphics().fillOval((int)x - 2, (int)y - 2, 4, 4);
    }

    public static double normalizeBearing(double ang) {
        while (ang > Math.PI) ang -= 2*Math.PI;
        while (ang < -Math.PI) ang += 2*Math.PI;
        return ang;
    }
}
