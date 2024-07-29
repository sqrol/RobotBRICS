package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Logic.CommandList;
import frc.robot.StateMachine.CoreEngine.IState;

public class Transition implements IState {

    private CommandList cmdList;
    
    @Override
    public void initialize() {
        cmdList = new CommandList();
    }

    @Override
    public void execute() {
        String command = Main.traverse.execute();
        SmartDashboard.putString("currentCommand", command);
        cmdList.setCurrentCommand(command);
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