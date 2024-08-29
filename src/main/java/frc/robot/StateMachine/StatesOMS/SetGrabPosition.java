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
    private double  val = 0;

    private int index = 0; 

    private static final double STEP = 1.0;

    public SetGrabPosition(String fruit, boolean smooth) {
        this.fruit = fruit;
        this.smooth = smooth;
        this.lastUpdateTime = Timer.getFPGATimestamp();
    }

    public SetGrabPosition(double val) {
        this.val = val;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
        Main.sensorsMap.put("camTask", 0.0);
    }

    @Override
    public void execute() {
        if(fruit.equals("OPEN")) {
            endMovement = smoothServoMovement(55.0, 0.01);
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
