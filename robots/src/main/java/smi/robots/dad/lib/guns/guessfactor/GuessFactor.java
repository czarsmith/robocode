package smi.robots.dad.lib.guns.guessfactor;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import smi.robots.dad.lib.MyUtils;
import smi.robots.dad.lib.firepower.FirePower;
import smi.robots.dad.lib.guns.Gun;
import smi.robots.dad.lib.guns.VirtualBullet;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;

public class GuessFactor extends Gun {
    /**
     * Static saves data between rounds.
     */
    private static Segment SEGMENT = new DefaultSegment();

    double forwardX;
    double forwardY;
    double reverseX;
    double reverseY;
    private double minXY;
    private double maxX;
    private double maxY;

    private List<GuessBullet> bullets;

    public GuessFactor(AdvancedRobot robot, FirePower firepower) {
        super(robot, firepower, Color.ORANGE);
        bullets = new ArrayList<GuessBullet>();
        minXY = 18;
        maxX = robot.getBattleFieldWidth() - 18;
        maxY = robot.getBattleFieldHeight() - 18;
        SEGMENT.init(robot);
    }

    public String getName() {
        return getClass().getName();
    }

    @Override
    public VirtualBullet fire(Intel enemy, double firepower) {
        double fromHeading = MyUtils.getHeading(robot.getX(), robot.getY(),
            reverseX, reverseY);
        double toHeading = MyUtils.getHeading(robot.getX(), robot.getY(),
            forwardX, forwardY);

        // Compute the best bearing based on the best factor.
        Factor fact = SEGMENT.getBestFactor(enemy);
        double bestFactor = fact.getFactor();
        double maxSpread = MyUtils.getBearing(fromHeading, toHeading);
        double bestHeading = fromHeading + maxSpread/2 * (bestFactor+1);

        if (enemy.getEnergy() == 0) {
            // Head On.  The enemy is disabled.
            bestHeading = robot.getHeadingRadians() + enemy.getBearingRadians();
        }

        GuessBullet gb = new GuessBullet(getName(), enemy.getName(), robot.getX(),
            robot.getY(), bestHeading, robot.getTime(), firepower, fromHeading,
            toHeading);
        bullets.add(gb);

        processBullets();

        return gb;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
    }

    private void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        if (!RobotManager.isTarget(enemy)) {
            return;
        }

        double fp = firepower.getPower();
        // Figure out where the enemy would be if they suddenly accelerated
        // or reversed direction and went top speed perpendicular to my bearing.
        Point2D.Double forwardPt = computePoint(enemy, fp, true);
        Point2D.Double reversePt = computePoint(enemy, fp, false);
        forwardX = forwardPt.x;
        forwardY = forwardPt.y;
        reverseX = reversePt.x;
        reverseY = reversePt.y;

//        MyUtils.drawCircle(robot, Color.WHITE, (int)forwardX, (int)forwardY, 2);
//        MyUtils.drawCircle(robot, Color.ORANGE, (int)reverseX, (int)reverseY, 2);
    }

    /**
     * Computes the location of the enemy if they went full speed perpendicular
     * to our bearing to them.
     * 
     * @param enemy The enemy
     * @param firepower The firepower of a bullet (used to compute time to impact).
     * @param forward True if we want to know what happens if the enemy goes
     * forward, otherwise false
     */
    private Point2D.Double computePoint(Intel enemy, double firepower, boolean forward) {
        double robotX = robot.getX();
        double robotY = robot.getY();
        double newX = enemy.getX();
        double newY = enemy.getY();
        boolean positiveDirection = (forward == enemy.getVelocity() >= 0);
        for (int i = 0; i < 20; i++) {
            double timeToImpact = MyUtils.getDistance(
                robotX, robotY, newX, newY) / Rules.getBulletSpeed(firepower);
            double speed = enemy.getVelocity();
            newX = enemy.getX();
            newY = enemy.getY();
            for (int t = 0; t < timeToImpact; t++) {
                speed = adjustSpeed(speed, positiveDirection);
                newX += Math.sin(enemy.getHeadingRadians()) * speed;
                newY += Math.cos(enemy.getHeadingRadians()) * speed;
            }

            newX = Math.max(minXY, Math.min(newX, maxX));
            newY = Math.max(minXY, Math.min(newY, maxY));
        }
        
        return new Point2D.Double(newX, newY);
    }

    private double adjustSpeed(double currSpeed, boolean positiveDirection) {
        if (positiveDirection) {
            if (currSpeed < 0) {
                return currSpeed + Rules.DECELERATION;
            }
            else {
                return Math.min(currSpeed + Rules.ACCELERATION, Rules.MAX_VELOCITY);
            }
        }
        else {
            if (currSpeed > 0) {
                return currSpeed - Rules.DECELERATION;
            }
            else {
                return Math.max(currSpeed - Rules.ACCELERATION, -Rules.MAX_VELOCITY);
            }
        }
    }

    private void processBullets() {
        // For each bullet, see if it has hit or missed yet and record the score.
        Iterator<GuessBullet> iter = bullets.iterator();
        while(iter.hasNext()) {
            GuessBullet b = iter.next();
            long now = robot.getTime();
            if (MyUtils.bulletHasArrived(b, now)) {
                Intel enemy = RobotManager.getEnemy(b.getEnemy());
                // Skip the bullet if it passed the enemy while we weren't looking.
                // In that case, we don't know where the enemy was when it got hit
                // so we want to avoid misinformation.
                if (enemy.getTime() == now && enemy.getPreviousTime() == now-1) {
                    double actualHeading = MyUtils.getHeading(b.getFromX(), b.getFromY(),
                        enemy.getX(), enemy.getY());
                    double maxSpread = Math.abs(
                        MyUtils.getBearing(b.getFromHeading(), b.getToHeading()));
                    double actualDelta = Math.abs(
                        MyUtils.getBearing(b.getFromHeading(), actualHeading));
                    double factor = actualDelta / maxSpread * 2 - 1;
                    // Round to the nearest tenth.
                    factor = Math.round(factor * 10) / 10d;
    
                    SEGMENT.saveFactor(enemy, b, factor);
                }
                iter.remove();
            }
        }
    }
}
