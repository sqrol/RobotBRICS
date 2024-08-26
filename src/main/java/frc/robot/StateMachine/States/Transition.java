package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Logic.CommandList;
import frc.robot.StateMachine.CoreEngine.IState;

public class Transition implements IState {

    private CommandList cmdList;
    private boolean flag = true; 

    private boolean autonomousMode = true;
    
    @Override
    public void initialize() {
        cmdList = new CommandList();
        flag = true; 
    }

    @Override
    public void execute() {
        if (flag && autonomousMode) {
            String command = Main.traverse.execute();
            SmartDashboard.putString("currentCommand", command);
            cmdList.setCurrentCommand(command);
            cmdList.addCommand();
            flag = false; 
        } else if(flag && !autonomousMode) {
            String command = Main.logic.getNextCommand();
            SmartDashboard.putString("currentCommand", command);
            cmdList.setCurrentCommand(command);
            cmdList.addCommand();
            flag = false;
        }
    }    

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return !flag;
    }
}