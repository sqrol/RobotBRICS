package frc.robot.StateMachine.CoreEngine;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.StateMachine.States.*;
import frc.robot.StateMachine.StatesAutoOMS.*;
import frc.robot.StateMachine.StatesOMS.*;

public class StateMachineOMS { 
    private static double startTimeOMS = 0;;
    public static IState currentStateOMS;
    public static boolean firstIterationOMS = true;
    public static double iterationTimeOMS;
    public static int index = 0;
    public static boolean endOMSWork = false;
    public static ArrayList<IState> statesOMS = new ArrayList<>();

    public StateMachineOMS() {
        StateMachineOMS.firstIterationOMS = true;
    }

    public void initStates() { 

    }

    public void executeStates() {
        if (firstIterationOMS) { 
            startTimeOMS = Timer.getFPGATimestamp();
            firstIterationOMS = false;
            currentStateOMS = statesOMS.get(index);
            currentStateOMS.initialize();
        }
        currentStateOMS.execute(); 
        iterationTimeOMS = Timer.getFPGATimestamp() - startTimeOMS;
        if (currentStateOMS.isFinished()) { 
            currentStateOMS.finilize();
            firstIterationOMS = true;
            StateMachineOMS.index++;
        }  
    }    

    public boolean isProgramFinished() {
        return false;
    }
}
