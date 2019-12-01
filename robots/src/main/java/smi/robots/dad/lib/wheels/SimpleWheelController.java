package smi.robots.dad.lib.wheels;

import robocode.AdvancedRobot;
import robocode.Event;

public class SimpleWheelController implements WheelController {
    private AdvancedRobot robot;
    private Wheel meleeWheel;
    private Wheel vsWheel;

    public SimpleWheelController(AdvancedRobot robot, Wheel wheel) {
        this(robot, wheel, wheel);
    }
    
    public SimpleWheelController(AdvancedRobot robot,
        Wheel meleeWheel, Wheel vsWheel) {
        this.robot = robot;
        this.meleeWheel = meleeWheel;
        this.vsWheel = vsWheel;
    }

    public void doTurn() {
        if (robot.getOthers() == 1) {
            vsWheel.doTurn(true);
        }
        else if (robot.getOthers() > 1) {
            meleeWheel.doTurn(true);
        }
    }
    
    public void onEvent(Event e) {
        if (robot.getOthers() == 1) {
            vsWheel.onEvent(e, true);
        }
        else if (robot.getOthers() > 1) {
            meleeWheel.onEvent(e, true);
        }
    }
}
