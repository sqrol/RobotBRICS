package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoGrab implements IState {

    private String fruit = "";

    private boolean flag = false;
    private boolean statesEnd = false;
    private boolean objectСaptured = false;
    private int index = 1;

    private double lastUpdateTime = 0.0;
    private static final double STEP = 1.0;
    private static final double DELAY = 0.01;

    private ArrayList<IState> newStates = new ArrayList<>();

    private static final HashMap<String, Double> GRAB_MAP = new HashMap<>() {
        {
            put("AppleBigRed", 30.0);
            put("AppleBigRotten", 30.0);

            put("AppleSmallRed", 50.0);
            put("AppleSmallRotten", 50.0);

            put("PearYellow", 66.0);
            put("PearRotten", 66.0);
        }
    };

    private static final HashMap<String, Double> LIFT_MAP = new HashMap<>() {
        {
            put("AppleBigRed", 73.0);
            put("AppleBigRotten", 73.0);

            put("AppleSmallRed", 76.0);
            put("AppleSmallRotten", 76.0);

            put("PearYellow", 75.0);
            put("PearRotten", 75.0);
        }
    };

    public AutoGrab() {

    }

    @Override
    public void initialize() {
        this.flag = false;
        this.statesEnd = false;
        Main.switchMap.put("liftStop", false);
    }

    @Override
    public void execute() {
        SmartDashboard.putString("fruit", getGrippedFruit());
        switch (index) {
            case 1:
                // это проверка что в списке нашлось значение для результата с камеры
                if(LIFT_MAP.get(getGrippedFruit()) != null) {
                    Main.motorControllerMap.put("targetLiftPos", LIFT_MAP.get(getGrippedFruit()));
                    if (Main.switchMap.get("liftStop") && StateMachine.iterationTime > 2) {
                        index++;
                    }
                    break;
                }
                else break;
                
            case 2:
                if(GRAB_MAP.get(getGrippedFruit()) != null) {
                    Main.motorControllerMap.put("servoGrab", GRAB_MAP.get(getGrippedFruit()));
                    if(smoothServoMovement(66.0, DELAY)) {
                        if (!flag) {
                            index++;
                            flag = true;
                        }
                        break;
                    }
                }
                else break;
        }

        if (index == 3) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }
    }

    @Override
    public void finilize() {
        fruit = "";
    }

    @Override
    public boolean isFinished() {
        return statesEnd;
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

    private String getGrippedFruit() {
        switch(Main.camMap.get("grippedFruit").intValue()) {
            case 1: 
                fruit = "AppleBigRed";
                break;
            case 2: 
                fruit = "AppleSmallRed";
                break;
            case 3:
                fruit = "PearYellow";
                break;
        }
        return fruit;
    }
}