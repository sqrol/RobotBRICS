package frc.robot.StateMachine.StatesOMS;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class SetRotatePosition implements IState{

    private double targetDegree = 0;

    public SetRotatePosition(double targetDegree) {
        this.targetDegree = targetDegree;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("targetRotateDegree", targetDegree);
    }

    @Override
    public void finilize() {
        Main.switchMap.put("rotateStop", false);
    }

    @Override
    public boolean isFinished() {
        return Main.switchMap.get("rotateStop") && StateMachine.iterationTime > 2;
    }
    
}
