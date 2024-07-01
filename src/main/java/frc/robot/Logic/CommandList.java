package frc.robot.Logic;

import java.util.ArrayList;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.*;

public class CommandList {

    private String currentCommandName;
    private ArrayList<IState> newStates = new ArrayList<>();

    public void setCurrentCommand(String commandName) {
        this.currentCommandName = commandName;
        newStates.clear();
    }

    public void addCommand() {
        if (this.currentCommandName.equals("MOVE_FROM_START_TO_CH1")) {
            newStates.add(new SimpleDrive(90, 0)); 
            newStates.add(new SimpleDrive(0, 90)); 
            newStates.add(new AlignSharp(15));
            newStates.add(new SimpleDrive(0, 90));
            newStates.add(new DriveSonic(80));
            newStates.add(new Transitions()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }
    }
}