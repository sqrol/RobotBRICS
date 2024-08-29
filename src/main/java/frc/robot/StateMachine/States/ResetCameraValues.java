package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class ResetCameraValues implements IState {

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Main.sensorsMap.put("camTask", 25.0);
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 0.33;
    }
}