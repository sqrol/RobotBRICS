package frc.robot.StateMachine.CoreEngine;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.StateMachine.States.*;
import frc.robot.StateMachine.StatesAutoOMS.*;
import frc.robot.StateMachine.StatesOMS.*;

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
        
        // states.add(new AlignSharp(14.0));
        // states.add(new SimpleDrive(0, 90.0));

        // поправить перед сдачей MOVE_FROM_CH1_TO_FIRST_RZ
        // states.add(new DriveSonic(80.0));
        // states.add(new SimpleDrive(0, 90.0));
        // states.add(new DriveSonic(53.0));
        // states.add(new SimpleDrive(0, -90.0));
        // states.add(new SimpleDrive(190 , 0));
        // states.add(new SimpleDrive(0, -90.0));
        // states.add(new AlignSharp(25.0));
        // states.add(new SimpleDrive(0, -90.0));
        // states.add(new SimpleDrive(28, 0));
        // проверить перед сдачей MOVE_FROM_FIRST_TZ_TO_FIRST_RZ

    
        // states.add(new AutoStart());
        // states.add(new OI());
        // states.add(new InitLogic());
        states.add(new Transition());
        // states.add(new ThreshCheck());
        // states.add(new CheckRotten());
        // states.add(new SetRotatePosition(-20));
        // states.add(new AutoStart());
        
        // states.add(new AlignSharp(14.0));
        // states.add(new SimpleDrive(0, 90.0));

        // ch1_to_first_rz_ fixxx



        // states.add(new CheckFruit());
        
        // states.add(new SetGripRotatePosition("FLOOR"));
        // states.add(new AutoStart());
        // states.add(new SetGrabPosition(Constants.GRAB_OPEN));
        // states.add(new CheckRotten());
        // states.add(new Transition());
        // states.add(new ThreshCheck());
        // states.add(new SetGripRotatePosition("CHECK ZONE"));
        // states.add(new AutoStart());
        states.add(new End());
        // states.add(new ThreshCheck());
        // states.add(new ThreshCheck());
        // states.add(new ThreshCheck());
    }

    public void executeStates() {
        if (firstIteration) { 
            startTime = Timer.getFPGATimestamp();
            firstIteration = false;
            currentState = states.get(index);
            currentState.initialize();
        }
        iterationTime = Timer.getFPGATimestamp() - startTime;
        currentState.execute();
        
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
