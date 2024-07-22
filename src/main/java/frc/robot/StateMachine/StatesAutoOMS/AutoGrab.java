package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoGrab implements IState {

    private boolean flag = false;
    private boolean statesEnd = false;
    private boolean objectСaptured = false;
    private int index = 1;

    private double lastUpdateTime = 0.0;
    private static final double STEP = 1.0;

    private ArrayList<IState> newStates = new ArrayList<>();

    public AutoGrab() {

    }

    @Override
    public void initialize() {
        this.flag = false;
        this.statesEnd = false;
    }

    @Override
    public void execute() {
        switch (index) 
        {
            case 1:
                Main.motorControllerMap.put("targetLiftPos", 85.0);
                if (Main.switchMap.get("liftStop") && StateMachine.iterationTime > 5) {
                    index++;
                }
                break;
            case 2:
                Main.motorControllerMap.put("servoGrab", 50.0);
                if (!flag) {
                    index++;
                    flag = true;
                }
                break;
        }

        if (index == 3) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("glideMode", 0.0);
    }

    @Override
    public boolean isFinished() {
        return statesEnd;
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