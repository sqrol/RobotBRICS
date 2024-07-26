package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class ThreshCheck implements IState {

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 3.0);

    }

    @Override
    public void execute() {
        

    }

    @Override
    public void finilize() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }
    
}