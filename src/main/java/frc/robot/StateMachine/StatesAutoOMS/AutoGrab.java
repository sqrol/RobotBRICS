package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoGrab implements IState {

    private boolean flag = false;
    private boolean statesEnd = false;
    private boolean objectСaptured = false;
    private int index = 1;

    private double lastUpdateTime = 0.0;
    private static final double STEP = 1.0;

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
            put("AppleBigRed", 30.0);
            put("AppleBigRotten", 30.0);

            put("AppleSmallRed", 50.0);
            put("AppleSmallRotten", 50.0);

            put("PearYellow", 66.0);
            put("PearRotten", 66.0);
        }
    };

    public AutoGrab() {

    }

    @Override
    public void initialize() {
        this.flag = false;
        this.statesEnd = false;
    }

    @Override
    public void execute() {
        String temp = ""; // временная переменная, чтобы не ругался компилятор
        switch (index) 
        {
            case 1:
                // это проверка что в списке нашлось значение для результата с камеры
                if(LIFT_MAP.get(temp) != null) {
                    Main.motorControllerMap.put("targetLiftPos", LIFT_MAP.get(temp));
                    if (Main.switchMap.get("liftStop") && StateMachine.iterationTime > 5) {
                        index++;
                    }
                    break;
                }
                else break;
                
            case 2:
                if(GRAB_MAP.get(temp) != null) {
                    Main.motorControllerMap.put("servoGrab", GRAB_MAP.get(temp));
                    if (!flag) {
                        index++;
                        flag = true;
                    }
                    break;
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
        Main.motorControllerMap.put("glideMode", 0.0);
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
}