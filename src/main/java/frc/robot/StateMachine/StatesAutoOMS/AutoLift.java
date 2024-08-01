package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoLift implements IState {

    private ArrayList<IState> newStates = new ArrayList<>();
    
    private boolean flag = true;
    private boolean stateEnd = false;
    private boolean liftStop = false;

    private double fruitPosY = 0;
    private double lastLiftPos, currentLiftPos = 0;

    private double liftSpeed = 0;

    private int camMiddleForLift = 27;

    private final double[][] arrForLift = { { 30, 250, 460 }, { 80, 40, 20 } };

    private final double[][] speedForLift = { { 0, 30, 50, 100, 200, 330, 500, 700, 850, 1000 }, { 0, 8, 15, 20, 25, 35, 40, 50, 60, 100 } };


    public AutoLift() {
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("servoGripRotate", 20.0);
        Main.camMap.put("camTask", 5.0);
        liftStop = false;
    }

    @Override
    public void execute() {
        
        fruitPosY = Main.camMap.get("currentCenterY");

        if(fruitPosY == 0 && flag) {
            liftSpeed = -60.0;
        } else {
            liftSpeed = Functions.TransitionFunction(liftSpeed, speedForLift);
            flag = false;
        }

        Main.motorControllerMap.put("liftSpeed", liftSpeed);
        liftStop = Functions.BooleanInRange(camMiddleForLift - fruitPosY, -2, 2);

        SmartDashboard.putBoolean("AutoLift.liftStop", liftStop);

        SmartDashboard.putNumber("camMiddleForLift - fruitPosY", camMiddleForLift - fruitPosY);

        if(StateMachine.iterationTime > 10) {
            newStates.add(new AutoEnd());
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        if(liftStop && StateMachine.iterationTime > 1) {
            newStates.add(new AutoGlide(true));
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }
    }

    @Override
    public void finilize() {
        
    }

    @Override
    public boolean isFinished() {
        return stateEnd;
    }
    
}