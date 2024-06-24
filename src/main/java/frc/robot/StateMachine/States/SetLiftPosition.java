package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;

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
        
    }

    @Override
    public boolean isFinished() {
        
        return Functions.BooleanInRange(targetPosition - Main.motorControllerMap.get("currentLiftPos"), -0.3, 0.3);
    }
    
}
