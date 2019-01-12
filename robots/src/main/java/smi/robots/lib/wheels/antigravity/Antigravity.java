package smi.robots.lib.wheels.antigravity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Collection;

import robocode.AdvancedRobot;
import robocode.util.Utils;
import smi.robots.lib.MyUtils;
import smi.robots.lib.wheels.Wheel;
import smi.robots.lib.wheels.antigravity.forces.Force;
import smi.robots.lib.wheels.antigravity.forces.PointForce;

/**
 * Every robot, wall, and bullet in the battlefield exhibits a repulsive force
 * on our robot. What we need to do is compute the net force on our robot and
 * move whatever direction that sends us.
 */
public abstract class Antigravity extends Wheel {
    public Antigravity(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public void doTurn(boolean takeAction) {
        move(robot, getForces(), takeAction);
    }

    protected abstract Collection<Force> getForces();

    protected void move(AdvancedRobot robot, Collection<Force> points, boolean takeAction) {
        double xForce = 0;
        double yForce = 0;

        for (Force gf : points) {
            Point2D forceVector = gf.getForceVector(robot);
            xForce += forceVector.getX();
            yForce += forceVector.getY();

            // Draw forces
            if (gf instanceof PointForce) {
                ((PointForce)gf).draw(robot);
            }
        }

        if (takeAction) {
            goTo(robot, robot.getX() - xForce, robot.getY() - yForce);
        }
    }

    void goTo(AdvancedRobot robot, double x, double y) {
        double dist = MyUtils.getDistance(robot.getX(), robot.getY(), x, y);
        double angle = absbearing(robot.getX(), robot.getY(), x, y);
        double r = turnTo(robot, angle);
        robot.setAhead(dist * r);

        robot.getGraphics().setColor(Color.WHITE);
        robot.getGraphics().setStroke(new BasicStroke(2));
        robot.getGraphics().drawLine((int)robot.getX(), (int)robot.getY(), (int)x, (int)y);
    }

    int turnTo(AdvancedRobot robot, double angle) {
        double ang;
        int dir;

        ang = Utils.normalRelativeAngle(robot.getHeadingRadians() - angle);

        if (ang > Math.PI / 2) {
            ang -= 180;
            dir = -1;
        }
        else if (ang < -Math.PI / 2) {
            ang += 180;
            dir = -1;
        }
        else {
            dir = 1;
        }

        robot.setTurnLeftRadians(ang);
        return dir;
    }

    public double absbearing( double x1,double y1, double x2,double y2 ) {
        double xo = x2-x1;
        double yo = y2-y1;
        double h = MyUtils.getDistance(x1,y1, x2,y2 );
        if( xo >= 0 && yo >= 0 )
        {
            return Math.asin( xo / h );
        }
        if( xo >= 0 && yo <= 0 )
        {
            return Math.PI - Math.asin( xo / h );
        }
        if( xo <= 0 && yo <= 0 )
        {
            return Math.PI + Math.asin( -xo / h );
        }
        if( xo <= 0 && yo >= 0 )
        {
            return 2.0*Math.PI - Math.asin( -xo / h );
        }
        return 0;
    }    
}
