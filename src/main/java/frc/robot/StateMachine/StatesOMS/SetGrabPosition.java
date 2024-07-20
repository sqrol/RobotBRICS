package frc.robot.StateMachine.StatesOMS;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class SetGrabPosition implements IState{

    private String fruit = "";
    private boolean smooth, endMovement = false;
    private double lastUpdateTime = 0;

    private int index = 0; 

    private static final double STEP = 1.0;

    public SetGrabPosition(String fruit, boolean smooth) {
        this.fruit = fruit;
        this.smooth = smooth;
        this.lastUpdateTime = Timer.getFPGATimestamp();
    }

    public SetGrabPosition(int index) {
        this.index = index;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public void execute() {
        if (fruit.equals("BIG APPLE")) {
            if (smooth) {
                endMovement = smoothServoMovement(30.0, 0.05);
            } else {
                endMovement = smoothServoMovement(30.0, 0.01);
            }
        }

        if (fruit.equals("SMALL APPLE")) {
            if (smooth) {
                endMovement = smoothServoMovement(50.0, 0.05);
            } else {
                endMovement = smoothServoMovement(50.0, 0.01);
            }
        }

        if (fruit.equals("PEAR")) {
            if (smooth) {
                endMovement = smoothServoMovement(66.0, 0.05);
            } else {
                endMovement = smoothServoMovement(66.0, 0.01);
            }
        }

        if (fruit.equals("OPEN")) {
            if (smooth) {
                endMovement = smoothServoMovement(15.0, 0.05);
            } else {
                endMovement = smoothServoMovement(15.0, 0.01);
            }
        
        }

        if (fruit.equals("OPEN SMALL APPLE")) {
            if (smooth) {
                endMovement = smoothServoMovement(29.0, 0.05);
            } else {
                endMovement = smoothServoMovement(29.0, 0.01);
            }
        }
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 1 && endMovement;
    }

    private boolean smoothServoMovement(double targetPosition, double DELAY) {
        double currentPosition = Main.motorControllerMap.get("servoGrabAngle");
        double step = STEP * (targetPosition > currentPosition ? 1 : -1);

        double currentTime = Timer.getFPGATimestamp();

        if (Math.abs(targetPosition - currentPosition) > Math.abs(step)) {
            if (currentTime - lastUpdateTime >= DELAY) {
                currentPosition += step;
                Main.motorControllerMap.put("servoGrab", currentPosition);
                lastUpdateTime = currentTime; 
            }
        }
        return Functions.BooleanInRange(Math.abs(targetPosition - currentPosition), -1, 1); 
    }

}
