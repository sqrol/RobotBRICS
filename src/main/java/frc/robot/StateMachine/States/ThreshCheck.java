package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class ThreshCheck implements IState {

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 7.0);

    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("servoGripRotate", 115.0);
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}