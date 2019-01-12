package smi.robots.lib.wheels;

import robocode.Event;

public interface WheelController {
    public void doTurn();
    public void onEvent(Event e);
}
