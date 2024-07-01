package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.Logic.CommandList;
import frc.robot.StateMachine.CoreEngine.IState;

public class Transitions implements IState {

    private CommandList cmdList;
    
    @Override
    public void initialize() {
        cmdList = new CommandList();
    }

    @Override
    public void execute() {
        cmdList.setCurrentCommand(Main.traverse.execute());
        cmdList.addCommand();
    }    

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}