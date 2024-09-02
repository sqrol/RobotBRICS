package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Logic.CommandList;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class Transition implements IState {

    private CommandList cmdList;
    private boolean flag = true; 
    private static int count = 0; 

    private String[] cmd = { 
        "", 
        "", 
        ""};

    private Boolean end = false; 

    private boolean autonomousMode = true;
    
    @Override
    public void initialize() {
        cmdList = new CommandList();
        flag = true; 
        end = false; 
    }

    @Override
    public void execute() {
        // String command = cmd[count];
        // if (flag) {
        //     String command = Main.traverse.execute();
        //     SmartDashboard.putString("currentCommand", command);
        //     flag = false;
        // }
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);

        if (flag && autonomousMode) {
            String command = Main.traverse.execute();
            SmartDashboard.putString("currentCommand", command);
            cmdList.setCurrentCommand(command);
            cmdList.addCommand();
            count++; 
            if (!command.equals("none")) {
                end = true; 
            }
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
        SmartDashboard.putNumber("Count123: ", count); 
    }

    @Override
    public boolean isFinished() {
        return !flag && end;
        // return !flag && StateMachine.iterationTime > 2;
    }
}