package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class SetGlidePosition implements IState{

    private double targetPosition = 0;

    public SetGlidePosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public void execute() {
        Main.sensorsMap.put("targetGlidePos", targetPosition);
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("glideServoSpeed", 0.0);
    }

    @Override
    public boolean isFinished() {
        return Main.switchMap.get("glideStop");
    }
    
}
