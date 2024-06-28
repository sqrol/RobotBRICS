package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        Main.switchMap.put("glideStop", false);
    }

    @Override
    public void execute() {
        Main.sensorsMap.put("targetGlidePos", targetPosition);
    }

    @Override
    public void finilize() {
        SmartDashboard.putNumber("FNILIZE SETGLIDEPOS", 123123);
        Main.motorControllerMap.put("glideServoSpeed", 0.0);
        Main.sensorsMap.put("targetGlidePos", 0.0);
        Main.switchMap.put("glideStop", false);
    }

    @Override
    public boolean isFinished() {
        return Main.switchMap.get("glideStop");
    }
    
}
