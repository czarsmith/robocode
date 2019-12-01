package smi.robots.dad.lib.guns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.util.Utils;
import smi.robots.dad.lib.MyUtils;
import smi.robots.dad.lib.firepower.FirePower;
import smi.robots.dad.lib.guns.guessfactor.GuessFactor;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;

public class BestGunController extends GunController {
    private static Map<String, List<GunStat>> GUN_STATS = new HashMap<String, List<GunStat>>();
    private Map<String, Gun> guns;
    private List<VirtualBullet> allBullets;

    public BestGunController(AdvancedRobot robot, FirePower firepower) {
        this(robot, firepower,
            new GuessFactor(robot, firepower),
            new PatternMatcher(robot, firepower),
            new HeadOnGun(robot, firepower),
            new CircularGun(robot, firepower));
    }

    public BestGunController(AdvancedRobot robot, FirePower firepower, Gun... guns) {
        super(robot, firepower);
        this.guns = new HashMap<String, Gun>();
        this.allBullets = new ArrayList<VirtualBullet>();

        for (Gun gun : guns) {
            registerGun(gun);
        }
    }
    
    @Override
    public void doTurn() {
        super.doTurn();

        // Only fire a virtual bullet if we have current intel on the target.
        if (RobotManager.getTarget() != null && RobotManager.getTarget().getTime() == robot.getTime()) {
            Gun bestGun = getBestGun(RobotManager.getTarget().getName(), false);

            robot.setGunColor(bestGun.getColor());
            robot.setBulletColor(bestGun.getColor());

            for (Gun gun : guns.values()) {
                VirtualBullet bullet = gun.fire(RobotManager.getTarget(), firepower.getPower());
                allBullets.add(bullet);

                if (gun == bestGun) {
                    robot.setTurnGunLeftRadians(Utils.normalRelativeAngle(
                        robot.getGunHeadingRadians() - bullet.getHeading()));

                    MyUtils.drawAimPoint(robot, bullet);
                }
            }
        }

        processBullets();

        for (VirtualBullet b : allBullets) {
            MyUtils.drawBullet(robot, b, guns.get(b.getGun()).getColor());
        }
    }

    private void registerGun(Gun gun) {
        guns.put(gun.getName(), gun);
    }

    private Gun getBestGun(String enemy, boolean recent) {
        Gun ret = guns.values().iterator().next();
        List<GunStat> scores = GUN_STATS.get(enemy);
        if (scores != null) {
            double bestScore = Double.MIN_VALUE;
            double thisScore = 0;
//            int i = 0;
            for (GunStat score : scores) {
                thisScore = recent ? score.getRecentScore() : score.getTotalScore();
                if (thisScore > bestScore) {
                    bestScore = thisScore;
                    ret = guns.get(score.getGun());
                }

//                robot.getGraphics().setColor(guns.get(score.getGun()).getColor());
//                robot.getGraphics().setStroke(new BasicStroke(2));
//                robot.getGraphics().drawString(score.getGun() +
//                    ": " + score.getTotalScore(), 10, 100 - (i * 20));
//                i++;
            }
        }
        return ret;
    }

    /**
     * See which virtual bullets have passed the target and count whether or
     * not they missed.
     */
    private void processBullets() {
        resetRecentStats();

        // For each bullet, see if it has hit or missed yet and record the score.
        Iterator<VirtualBullet> iter = allBullets.iterator();
        while(iter.hasNext()) {
            VirtualBullet b = iter.next();
            long now = robot.getTime();
            if (MyUtils.bulletHasArrived(b, robot.getTime())) {
                Intel enemy = RobotManager.getEnemy(b.getEnemy());
                if (enemy.getTime() == now &&
                    enemy.getPreviousTime() == now-1) { 
                    double distToEnemy = MyUtils.getDistance(
                        b.getFromX(), b.getFromY(), enemy.getX(), enemy.getY());
                    // The bullet has passed the enemy.  Check the bullet's heading
                    // against the bearing to the enemy, and see if it's close enough
                    // to have hit them.
                    double margin = Math.abs(Math.atan(robot.getWidth()/2/distToEnemy));
                    double currentBearingToEnemy = MyUtils.getHeading(
                        b.getFromX(), b.getFromY(), enemy.getX(), enemy.getY());
                    double error = Math.abs(MyUtils.getBearing(currentBearingToEnemy, b.getHeading()));
    
                    if (error < margin) { // Hit
                        List<GunStat> stats = GUN_STATS.get(enemy.getName());
                        if (stats == null) {
                            stats = new ArrayList<GunStat>();
                            GUN_STATS.put(enemy.getName(), stats);
                        }
                        GunStat stat = null;
                        for (GunStat gs : stats) {
                            if (gs.getGun().equals(b.getGun())) {
                                stat = gs;
                                break;
                            }
                        }
                        if (stat == null) {
                            stat = new GunStat(b.getGun());
                            stats.add(stat);
                        }
                        stat.setTotalScore(stat.getTotalScore() + 1);
                        stat.setRecentScore(stat.getRecentScore() + 1);
                    }
                }
                iter.remove();
            }
        }
    }

    private void resetRecentStats() {
        if (robot.getGunHeat() > robot.getGunCoolingRate()) {
            // We only reset the recent stats if we're firing this turn.  This
            // way, we always know which gun is working best since the last
            // time we fired.
            return;
        }
        for (List<GunStat> stats : GUN_STATS.values()) {
            for (GunStat gs : stats) {
                gs.setRecentScore(0);
            }
        }
    }

    public void onEvent(Event e) {
        super.onEvent(e);

        for (Gun gun : guns.values()) {
            gun.onEvent(e);
        }        
    }

    private class GunStat {
        private String gun;
        private double totalScore;
        private double recentScore;

        public GunStat(String gun) {
            this.gun = gun;
        }

        public String getGun() {
            return gun;
        }
        
        public void setTotalScore(double score) {
            this.totalScore = score;
        }

        public double getTotalScore() {
            return totalScore;
        }

        public void setRecentScore(double score) {
            this.recentScore = score;
        }

        public double getRecentScore() {
            return recentScore;
        }
    }
}
