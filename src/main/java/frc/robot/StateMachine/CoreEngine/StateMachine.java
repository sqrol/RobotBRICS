package frc.robot.StateMachine.CoreEngine;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.StateMachine.States.*;

public class StateMachine { 
    private static double startTime = 0;;
    public static IState currentState;
    public static boolean firstIteration = true;
    public static double iterationTime;
    public static int index = 0;
    public static ArrayList<IState> states = new ArrayList<>();

    public StateMachine() {
        StateMachine.firstIteration = true;
    }

    public void initStates() { 
        states.add(new StartPos());
        states.add(new AlignSharp(10));
        states.add(new End());
    }

    public void executeStates() {
        if (firstIteration) { 
            startTime = Timer.getFPGATimestamp();
            firstIteration = false;
            currentState = states.get(index);
            currentState.initialize();
        }
        currentState.execute(); 
        iterationTime = Timer.getFPGATimestamp() - startTime;
        if (currentState.isFinished()) { 
            currentState.finilize();
            firstIteration = true;
            StateMachine.index++;
        }  
    }    

    public boolean isProgramFinished() {
        return false;
    }
}
