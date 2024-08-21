package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class InitLogic implements IState {

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        Main.logic.logicInit();
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 0.3;
    }
    
}