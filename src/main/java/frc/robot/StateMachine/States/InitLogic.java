package frc.robot.StateMachine.States;

import java.io.File;
import java.io.PrintStream;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class InitLogic implements IState {

    @Override
    public void initialize() {
        try {
            System.setOut(new PrintStream(new File("/home/pi/Desktop/pathsSemiAuto.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
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