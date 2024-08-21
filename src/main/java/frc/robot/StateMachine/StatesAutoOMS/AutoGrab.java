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

    private boolean flag, stateEnd = false;
    
    private boolean objectСaptured = false;

    private int index = 1;
    private int branchNumber = 0;

    private double lastUpdateTime = 0.0;

    private static final double STEP = 1.0;
    private static final double DELAY = 0.01;

    private ArrayList<IState> newStates = new ArrayList<>();

    private static final HashMap<String, Double> GRAB_MAP = new HashMap<>() {
        {
            put(Constants.BIG_RED_APPLE, 42.0);
            put(Constants.BIG_ROTTEN_APPLE, 42.0);

            put(Constants.SMALL_RED_APPLE, 50.0);
            put(Constants.SMALL_ROTTEN_APPLE, 50.0);

            put(Constants.YELLOW_PEAR, 43.0);
            put(Constants.ROTTEN_PEAR, 43.0);
        }
    };

    private static final HashMap<String, Double> LIFT_MAP = new HashMap<>() {
        {
            put(Constants.BIG_RED_APPLE, 73.0);
            put(Constants.BIG_ROTTEN_APPLE, 73.0);

            put(Constants.SMALL_RED_APPLE, 76.0);
            put(Constants.SMALL_ROTTEN_APPLE, 76.0);

            put(Constants.YELLOW_PEAR, 75.0);
            put(Constants.ROTTEN_PEAR, 75.0);
        }
    };

    public AutoGrab() {

    }

    public AutoGrab(boolean treeMode) {
        this.treeMode = treeMode;
        index = 2;
    }

    @Override
    public void initialize() {
        this.flag = false;
        this.stateEnd = false;
        Main.switchMap.put("liftStop", false);
    }

    @Override
    public void execute() {

        switch (index) {
            case 1:
            // здесь он пропускает момент с проверкой iterationTime и сразу переходит на второй кейс
                Main.motorControllerMap.put("servoGrab", 19.0);
                if(LIFT_MAP.get(Main.stringDutyMap.get("detectedFruit")) != null) {
                    Main.motorControllerMap.put("targetLiftPos", LIFT_MAP.get(Main.stringDutyMap.get("detectedFruit")));
                    if(Main.switchMap.get("liftStop")) {
                        if(StateMachine.iterationTime > 5) {
                            index++;
                            break;
                        }
                        
                    }
                } else {
                    index++;
                    break;
                }
                
            case 2:
                if(GRAB_MAP.get(Main.stringDutyMap.get("detectedFruit")) != null) {
                    if(smoothServoMovement(GRAB_MAP.get(Main.stringDutyMap.get("detectedFruit")), DELAY)) {
                        if (!flag) {
                            index++;
                            flag = true;
                        }
                        break;
                    }
                }
                else {
                    index++;
                    break;
                }
        }

        if (index == 3) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }
    }

    @Override
    public void finilize() {
        fruit = "";
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