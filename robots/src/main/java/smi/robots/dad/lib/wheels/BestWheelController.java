package smi.robots.dad.lib.wheels;

import java.util.HashMap;
import java.util.Map;

import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.Event;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.util.Utils;
import smi.robots.dad.lib.intel.RobotManager;

public class BestWheelController implements WheelController {
    /** <WheelName, WheelScore> */
    private static Map<String, WheelScore> SCORES =
        new HashMap<String, WheelScore>();

    private static int GRACE_PERIOD = 20;
    private static int MAX_HITS_PER_WHEEL = 2;

    private static String CURR_WHEEL = null;
    private Map<String, Wheel> allWheels;
    private Map<String, Wheel> meleeWheels;
    private Map<String, Wheel> vsWheels;
    private AdvancedRobot robot;
    private long startTime = 0;
    private long gracePeriodEnd = 0;
    private int hitCount = 0;

    public BestWheelController(AdvancedRobot robot) {
        this.robot = robot;
        allWheels = new HashMap<String, Wheel>();
        meleeWheels = new HashMap<String, Wheel>();
        vsWheels = new HashMap<String, Wheel>();
        
        // Stop & Go
        registerWheel("Stop And Go", new StopAndGo(robot));

        // Bullet Dodger
        registerWheel("Circular Bullet Dodger", new CircularBulletDodger(robot));
        registerWheel("HeadOn Bullet Dodger", new HeadOnBulletDodger(robot));
        registerWheel("Factor Bullet Dodger", new FactorBulletDodger(robot));

        // Rotator
        registerWheel("Rotator", new Rotator(robot, 400, 225));

        chooseWheel();
    }

    public void doTurn() {
        for (Wheel w : allWheels.values()) {
            allWheels.get(CURR_WHEEL).doTurn(CURR_WHEEL.equals(w.getName()));
        }
    }

    public void onEvent(Event e) {
        if (e instanceof DeathEvent) {
            recordTime();
        }
        else if (e instanceof RobotDeathEvent) {
            if (RobotManager.isTarget(((RobotDeathEvent) e).getName())) {
                chooseWheel();
            }
        }
        else if (e instanceof HitByBulletEvent && robot.getOthers() > 0) {
            if (robot.getTime() > gracePeriodEnd) {
                hitCount++;
                recordDamage((HitByBulletEvent)e);
                
                if (hitCount == MAX_HITS_PER_WHEEL) {
                    chooseWheel();
                }
            }
        }

        for (Wheel w : allWheels.values()) {
            allWheels.get(CURR_WHEEL).onEvent(e, CURR_WHEEL.equals(w.getName()));
        }
    }

    private void registerWheel(String name, Wheel wheel) {
        wheel.setName(name);
        allWheels.put(name, wheel);
        if (!SCORES.containsKey(name)) {
            SCORES.put(name, new WheelScore());
        }

        if (wheel.isMelee()) {
            meleeWheels.put(name, wheel);
        }

        if (wheel.isOneOnOne()) {
            vsWheels.put(name, wheel);
        }
    }

    private void recordDamage(HitByBulletEvent e) {
        if (CURR_WHEEL != null) {
            WheelScore ws = SCORES.get(CURR_WHEEL);
            ws.setDamage(ws.getDamage() + Rules.getBulletDamage(e.getPower()));
        }
    }

    private void recordTime() {
        if (CURR_WHEEL != null) {
            WheelScore ws = SCORES.get(CURR_WHEEL);
            ws.setSurvivalTime(ws.getSurvivalTime() + robot.getTime() - startTime);
        }
    }

    private void chooseWheel() {
        recordTime();

        if (robot.getOthers() > 1) {
            chooseWheel(meleeWheels, SCORES);
        }
        else {
            chooseWheel(vsWheels, SCORES);
        }

        startTime = robot.getTime();
        gracePeriodEnd = robot.getTime() + GRACE_PERIOD;
        hitCount = 0;
    }

    private void chooseWheel(Map<String, Wheel> wheels, Map<String, WheelScore> scores) {
        // Sum scores
        double totalScore = 0;
        for (Wheel w : wheels.values()) {
            totalScore += scores.get(w.getName()).getScore();
        }

        // Set relative strengths
        for (Wheel w : wheels.values()) {
            WheelScore s = scores.get(w.getName());
            s.setStrength(s.getScore() / totalScore);
        }

        // Compute the best.
        double rand = Math.random();
        double strength = 0;
        for (Wheel w : wheels.values()) {
            strength += scores.get(w.getName()).getStrength();
            if (rand < strength) {
                CURR_WHEEL = w.getName();
                strength = scores.get(w.getName()).getStrength();
                break;
            }
        }

        System.out.println(CURR_WHEEL + ": " + strength);
        robot.setBodyColor(allWheels.get(CURR_WHEEL).getColor());
    }

    private class WheelScore {
        private double survivalTime;
        private double damage;
        private double strength;

        public double getScore() {
            if (survivalTime < 50) {
                // This wheel hasn't been used enough yet to determine a
                // reliable score.
                return 1000000;
            }
            else if (Utils.isNear(damage, 0)) {
                return survivalTime;
            }
            else {
                return survivalTime / damage;
            }
        }

        public void setSurvivalTime(double value) {
            this.survivalTime = value;
        }

        public double getSurvivalTime() {
            return survivalTime;
        }

        public void setDamage(double damage) {
            this.damage = damage;
        }

        public double getDamage() {
            return damage;
        }
        
        public void setStrength(double strength) {
            this.strength = strength;
        }
        
        public double getStrength() {
            return strength;
        }
    }
}