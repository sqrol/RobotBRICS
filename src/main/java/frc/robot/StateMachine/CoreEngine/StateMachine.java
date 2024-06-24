package frc.robot.StateMachine.CoreEngine;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.StateMachine.States.*;

public class StateMachine { 
    private static double startTime;
    public static IState currentState;
    public static boolean firstIteration = true;
    public static double iterationTime;
    public static int index = 0;
    public static ArrayList<IState> states = new ArrayList<>();

    public void initStates() { 
        states.add(new Start());
        states.add(new SetLiftPosition(50));
        states.add(new SetLiftPosition(-1));
        states.add(new SetLiftPosition(50));
        states.add(new SetLiftPosition(-1));
    } 

    public void executeStates() {
        if (firstIteration) { 
            startTime = Timer.getFPGATimestamp();
            firstIteration = false;
            currentState = states.get(index);
            currentState.initialize();
        }
        currentState.execute(); 
        if (currentState.isFinished()) { 
            firstIteration = true;
            StateMachine.index++;
            currentState.finilize();
        }
        iterationTime = Timer.getFPGATimestamp() - startTime;
    }    

    public boolean isProgramFinished() {
        return false;
    }
}
