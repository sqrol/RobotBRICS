package frc.robot.StateMachine.States;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class ThreshCheck implements IState {

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 7.0);

    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_ZONE);
        
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}