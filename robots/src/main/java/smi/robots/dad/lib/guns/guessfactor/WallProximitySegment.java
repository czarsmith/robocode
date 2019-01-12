package smi.robots.dad.lib.guns.guessfactor;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.intel.Intel;

public class WallProximitySegment extends Segment {
    private double dist;
    private boolean closeToWall;
    private double maxX;
    private double maxY;

    public WallProximitySegment(double dist, boolean closeToWall) {
        this.dist = dist;
        this.closeToWall = closeToWall;

        addSubSegment(new DistanceSegment(0, 100));
        addSubSegment(new DistanceSegment(100, 200));
        addSubSegment(new DistanceSegment(200, 300));
        addSubSegment(new DistanceSegment(300, 400));
        addSubSegment(new DistanceSegment(400, Double.MAX_VALUE));
    }

    @Override
    public void init(AdvancedRobot robot) {
        super.init(robot);
        this.maxX = robot.getBattleFieldWidth();
        this.maxY = robot.getBattleFieldHeight();
    }

    @Override
    protected boolean matches(Intel enemy, long time) {
        return closeToWall == (enemy.getX() < dist || enemy.getY() < dist ||
            enemy.getX() > maxX - dist || enemy.getY() > maxY - dist);
    }
}
