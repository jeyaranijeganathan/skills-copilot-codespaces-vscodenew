package RcbFirstSpinBot;

import dev.robocode.tankroyale.botapi.Bot;
import dev.robocode.tankroyale.botapi.BotInfo;
import dev.robocode.tankroyale.botapi.events.HitBotEvent;
import dev.robocode.tankroyale.botapi.events.ScannedBotEvent;
import dev.robocode.tankroyale.botapi.events.HitByBulletEvent;
import java.awt.Color;

// ------------------------------------------------------------------
// SpinBot
// ------------------------------------------------------------------
// A sample bot original made for Robocode by Mathew Nelson.
// Ported to Robocode Tank Royale by Flemming N. Larsen.
//
// Moves in a circle, firing hard when an enemy is detected.
// ------------------------------------------------------------------
public class RcbFirstSpinBot extends Bot {

    // The main method starts our bot
    public static void main(String[] args) {
        new RcbFirstSpinBot().start();
    }

    // Constructor, which loads the bot config file
    RcbFirstSpinBot() {
        super(BotInfo.fromFile("RcbFirstSpinBot.json"));
    }

    // Called when a new round is started -> initialize and do some movement
    @Override
    public void run() {
        setBodyColor(Color.yellow);
        setTurretColor(Color.green);
        setRadarColor(Color.black);
        setScanColor(Color.red);

        // Repeat while the bot is running
        while (isRunning()) {
            // Tell the game that when we take move, we'll also want to turn right... a lot
            setTurnLeft(10_000);
            // Limit our speed to 5
            setMaxSpeed(5);
            // Start moving (and turning)
            forward(10_000);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    // We scanned another bot -> fire hard!
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        fire(3);
    }

    // We hit another bot -> if it's our fault, we'll stop turning and moving,
    // so we need to turn again to keep spinning.
    @Override
    public void onHitBot(HitBotEvent e) {
        var direction = directionTo(e.getX(), e.getY());
        var bearing = calcBearing(direction);
        if (bearing > -10 && bearing < 10) {
            fire(3);
        }
        if (e.isRammed()) {
            turnLeft(10);
        }
    }

   // We were hit by a bullet -> turn perpendicular to the bullet
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        // Calculate the bearing to the direction of the bullet
        var bearing = calcBearing(e.getBullet().getDirection());

        // Turn 90 degrees to the bullet direction based on the bearing
        turnLeft(90 - bearing);
    }
}