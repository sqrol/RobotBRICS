package frc.robot.StateMachine.States;

import java.util.ArrayList;

import frc.robot.Logic.TreeTraverse;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class Transitions implements IState {

    private TreeTraverse traverse = new TreeTraverse();

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        ArrayList<IState> newStates = new ArrayList<>();

        if(traverse.execute().equals("arg0")) {
            
        }

        newStates.add(new SimpleDrive(100, 0)); 
        newStates.add(new SimpleDrive(100, 0)); 
        newStates.add(new SimpleDrive(100, 0)); 
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }    

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}