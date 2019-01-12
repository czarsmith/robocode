package smi.robots.dad.lib.wheels;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.HitByBulletEvent;
import robocode.Rules;
import smi.robots.dad.lib.MyUtils;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.Observation;
import smi.robots.dad.lib.intel.RobotManager;
import smi.robots.dad.lib.wheels.antigravity.BulletDodger;
import smi.robots.dad.lib.wheels.antigravity.factor.DefaultSegment;
import smi.robots.dad.lib.wheels.antigravity.factor.Factor;
import smi.robots.dad.lib.wheels.antigravity.factor.Segment;
import smi.robots.dad.lib.wheels.antigravity.forces.LinearTravelingTangentalForce;

public class FactorBulletDodger extends BulletDodger {
    private static Segment SEGMENT = new DefaultSegment();

    public FactorBulletDodger(AdvancedRobot robot) {
        this(robot, DEFAULT_BULLET_MAGNITUDE, DEFAULT_BULLET_DECAY);
    }

    public FactorBulletDodger(AdvancedRobot robot, double bulletStrength, double bulletDecay) {
        super(robot, bulletStrength, bulletDecay);
    }

    @Override
    public Color getColor() {
        return Color.PINK;
    }

    @Override
    public void onEvent(Event e, boolean takeAction) {
        super.onEvent(e, takeAction);

        if (e instanceof HitByBulletEvent) {
            onHitByBullet((HitByBulletEvent)e);
        }
    }

    protected void onHitByBullet(HitByBulletEvent e) {
        for (LinearTravelingTangentalForce force : liveBullets) {
            double dist1 = MyUtils.getDistance(robot.getX(), robot.getY(),
                force.getStartX(), force.getStartY());
            double dist2 = MyUtils.getDistance(force.getX(), force.getY(),
                force.getStartX(), force.getStartY());
            double deltaDist = Math.abs(dist1 - dist2);

            if (deltaDist <= 32) {
                Observation obs =
                    RobotManager.getMyRobot().getObservationAtTime((int)force.getStartTime());

                // Like Guess Factor, figure out what factor the enemy used to
                // hit me.
                Point2D.Double forwardPt = computePoint(obs, force.getStartX(),
                    force.getStartY(), force.getBulletSpeed(), true);
                Point2D.Double reversePt = computePoint(obs, force.getStartX(),
                        force.getStartY(), force.getBulletSpeed(), false);

                double fromHeading = MyUtils.getHeading(force.getStartX(), force.getStartY(),
                    reversePt.x, reversePt.y);
                double toHeading = MyUtils.getHeading(force.getStartX(), force.getStartY(),
                    forwardPt.x, forwardPt.y);

                // Compute the best bearing based on the best factor.
                double maxSpread = Math.abs(
                        MyUtils.getBearing(fromHeading, toHeading));
                double actualDelta = Math.abs(
                    MyUtils.getBearing(fromHeading, e.getHeadingRadians()));
                double factorUsed = actualDelta / maxSpread * 2 - 1;
                // Round to the nearest tenth.
                factorUsed = Math.round(factorUsed * 10) / 10d;

                // Save this factor.  They will likely use it again.
                SEGMENT.saveFactor(RobotManager.getEnemy(force.getEnemy()), factorUsed);
            }
        }
    }

    protected void createBullet(Intel enemy) {
        Intel myIntel = RobotManager.getMyRobot();
        double de = enemy.getPreviousEnergy() - enemy.getEnergy();
        double bulletSpeed = Rules.getBulletSpeed(de);

        Point2D.Double forwardPt = computePoint(myIntel.getLatestObservation(), enemy.getX(), enemy.getY(), bulletSpeed, true);
        Point2D.Double reversePt = computePoint(myIntel.getLatestObservation(), enemy.getX(), enemy.getY(), bulletSpeed, false);

        double fromHeading = MyUtils.getHeading(enemy.getX(), enemy.getY(),
            reversePt.getX(), reversePt.getY());
        double toHeading = MyUtils.getHeading(enemy.getX(), enemy.getY(),
            forwardPt.getX(), forwardPt.getY());

        // Compute the best bearing based on the best factor.
        Factor fact = SEGMENT.getBestFactor(enemy);
        double bestFactor = fact.getFactor();

        double maxSpread = MyUtils.getBearing(fromHeading, toHeading);
        double bestHeading = fromHeading + maxSpread/2 * (bestFactor+1);

        long expires = robot.getTime() + (long)(enemy.getDistance() / bulletSpeed) + 10;

        LinearTravelingTangentalForce tbf =
            new LinearTravelingTangentalForce(enemy.getName(), enemy.getX(), enemy.getY(),
                enemy.getPreviousTime(),
                bestHeading, bulletSpeed, expires, bulletStrength, bulletDecay);
        liveBullets.add(tbf);
    }

    private Point2D.Double computePoint(Observation myObs,
        double startX, double startY, double bulletSpeed, boolean forward) {
        double newX = myObs.getX();
        double newY = myObs.getY();
        boolean positiveDirection = (forward == myObs.getVelocity() >= 0);
        double timeToImpact = MyUtils.getDistance(startX, startY, newX, newY) / bulletSpeed;
        double speed = myObs.getVelocity();
        for (int t = 0; t < timeToImpact; t++) {
            speed = adjustSpeed(speed, positiveDirection);
            newX += Math.sin(myObs.getHeadingRadians()) * speed;
            newY += Math.cos(myObs.getHeadingRadians()) * speed;
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
}