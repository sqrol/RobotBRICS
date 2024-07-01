package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class SetLiftPosition implements IState{

    private double targetPosition = 0;

    public SetLiftPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("targetLiftPos", targetPosition);
        
    }

    @Override
    public void finilize() {
        Main.switchMap.put("liftStop", false);
        Main.motorControllerMap.put("liftSpeed", 0.0);
    }

    @Override
    public boolean isFinished() {
        return Main.switchMap.get("liftStop") && StateMachine.iterationTime > 0.5;
    }  
}
