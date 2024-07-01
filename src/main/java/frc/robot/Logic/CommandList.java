package frc.robot.Logic;

import java.util.ArrayList;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.SimpleDrive;
import frc.robot.StateMachine.States.Transitions;

public class CommandList {

    private String currentCommandName;
    private ArrayList<IState> newStates = new ArrayList<>();

    public void setCurrentCommand(String commandName) {
        this.currentCommandName = commandName;
        newStates.clear();
    }

    public void addCommand() {
        if (this.currentCommandName.equals("MOV_IN_THIRD_LZ_TO_CH2")) {
            newStates.add(new SimpleDrive(100, 0)); 
            newStates.add(new SimpleDrive(100, 0)); 
            newStates.add(new SimpleDrive(100, 0)); 
            newStates.add(new Transitions()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }
    }

}