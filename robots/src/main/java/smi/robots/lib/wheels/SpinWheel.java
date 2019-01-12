package smi.robots.lib.wheels;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;

/**
 * A basic SpinBot pattern.
 */
public class SpinWheel extends Wheel {
    private int direction = 1;

    public SpinWheel(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public void doTurn(boolean takeAction) {
        if (takeAction) {
            robot.setTurnRight(Double.POSITIVE_INFINITY);
            robot.setAhead(100 * direction);
        }
    }

    @Override
    public void onEvent(Event e, boolean takeAction) {
        if (e instanceof HitRobotEvent) {
            changeDirection();
        }
        else if (e instanceof HitWallEvent) {
            changeDirection();
        }
    }
    
    private void changeDirection() {
        direction *= -1;
    }
}
