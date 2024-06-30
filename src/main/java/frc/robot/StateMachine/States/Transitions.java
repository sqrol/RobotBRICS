package frc.robot.StateMachine.States;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class Transitions implements IState {

    private boolean succesInit = false;

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        ArrayList<IState> newStates = new ArrayList<>();
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