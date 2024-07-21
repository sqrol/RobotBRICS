package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class Reset implements IState {

    @Override
    public void initialize() {
    

    }

    @Override
    public void execute() {
        Main.sensorsMap.put("resetGyro", 1.0);
        Main.motorControllerMap.put("resetEncs", 1.0);
        Main.motorControllerMap.put("resetPID", 1.0);
    }

    @Override
    public void finilize() {
        

    }

    @Override
    public boolean isFinished() {
        
        return StateMachine.iterationTime > 1;
    }
    
}
