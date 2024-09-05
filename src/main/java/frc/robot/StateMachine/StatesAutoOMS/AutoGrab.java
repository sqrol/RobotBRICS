package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoGrab implements IState {

    private String fruit = "";

    private boolean treeMode = false;

    private boolean flag = false;
    private boolean stateEnd = false;
    
    private boolean treeEnd = false;

    private int index = 1;
    private int branchNumber = 0;

    private double LIFT_POS = 0.0;
    private double GRAB_POS = 79.0;

    private double lastUpdateTime = 0.0;
    private double startTime = 0.0;

    private static final double STEP = 1.0;
    private static final double DELAY = 0.015;

    private ArrayList<IState> newStates = new ArrayList<>();

    private static final HashMap<String, Double> GRAB_MAP = new HashMap<>() {
        {   // 79.0
            put(Constants.BIG_RED_APPLE, 79.0);
            put(Constants.BIG_ROTTEN_APPLE, 79.0);

            put(Constants.SMALL_RED_APPLE, 79.0);
            put(Constants.SMALL_ROTTEN_APPLE, 79.0);

            put(Constants.YELLOW_PEAR, 79.0);
            put(Constants.ROTTEN_PEAR, 79.0);
        }
    };

    private static final HashMap<String, Double> LIFT_MAP = new HashMap<>() {
        {
            put(Constants.BIG_RED_APPLE, 73.0);
            put(Constants.BIG_ROTTEN_APPLE, 73.0);

            put(Constants.SMALL_RED_APPLE, 77.0);
            put(Constants.SMALL_ROTTEN_APPLE, 77.0);

            put(Constants.YELLOW_PEAR, 75.0);
            put(Constants.ROTTEN_PEAR, 75.0);
        }
    };

    public AutoGrab() {

    }

    public AutoGrab(boolean treeMode, int branchNumber) {
        // this.treeMode = treeMode;
        this.branchNumber = branchNumber;
        index = 2;
        this.flag = false;
        this.stateEnd = false;
    }

    @Override
    public void initialize() {
        this.flag = false;
        this.stateEnd = false;
        Main.switchMap.put("liftStop", false);
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {

        if(treeMode && !flag) {
            if(branchNumber == 1) {
                LIFT_POS = 3.0;
            }

        } else {
            LIFT_POS = 78.0;
        }

        if(index == 1) {
            Main.motorControllerMap.put("targetLiftPos", LIFT_POS);
            if(Main.switchMap.get("liftStop") && StateMachine.iterationTime > 2) {
                index++;
            }
        }

        if(index == 2) {
            if(smoothServoMovement(GRAB_POS, DELAY)) {
                if(StateMachine.iterationTime > 3) {
                    index++;
                }
            }
        }

        if (index == 3) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        // if(treeMode) {
        //     newStates.add(new AutoEnd(true)); 
        //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
        //     stateEnd = true;
        // }
    }

    @Override
    public void finilize() {
        // fruit = "";
    }

    @Override
    public boolean isFinished() {
        return stateEnd;
    }

    private boolean smoothServoMovement(double targetPosition, double DELAY) {
        double currentPosition = Main.motorControllerMap.get("servoGrabAngle");
        double step = STEP * (targetPosition > currentPosition ? 1 : -1);

        double currentTime = Timer.getFPGATimestamp();

        if (Math.abs(targetPosition - currentPosition) > Math.abs(step)) {
            if (currentTime - lastUpdateTime >= DELAY) {
                currentPosition += step;
                Main.motorControllerMap.put("servoGrab", currentPosition);
                lastUpdateTime = currentTime; 
            }
        }
        return Functions.BooleanInRange(Math.abs(targetPosition - currentPosition), -1, 1); 
    }
}