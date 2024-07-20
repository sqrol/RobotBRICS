package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.Maths.Common.Functions;

public class AutoGlide implements IState {

    private ArrayList<IState> newStates = new ArrayList<>();
    private double fruitPosY = 0;
    private boolean flag = false; 

    private double glideServoSpeed = 0; 

    private Boolean statesEnd = false;
    private boolean glideStop = false;

    private int camMiddleForGrab = 20;
    
    private static final double[][] speedForGlideServo = { { 0, 1, 4, 10, 20, 40, 60, 80, 100 }, { 0, 0.1, 0.1, 0.1, 0.1, 0.25, 0.3, 0.4, 0.4} };

    public AutoGlide() {
        Main.sensorsMap.put("camTask", 2.0);
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("glideMode", 1.0);
        Main.sensorsMap.put("camTask", 2.0); // Режим для выравнивания выдвижного механизма

        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("servoGripRotate", 75.0);
    }

    @Override
    public void execute() {
        fruitPosY = Main.camMap.get("currentCenterY");

        if (fruitPosY == 0 && !flag) {
            glideServoSpeed = 0.4;
            SmartDashboard.putNumber("glideState", 1);
        } else {
            SmartDashboard.putNumber("glideState", 2);
            flag = true; 
            glideServoSpeed = Functions.TransitionFunction(camMiddleForGrab - fruitPosY, speedForGlideServo); 
        }

        SmartDashboard.putNumber("87 - fruitPosY", camMiddleForGrab - fruitPosY); 
        SmartDashboard.putNumber("glideServoSpeed12", glideServoSpeed); 

        SmartDashboard.putNumber("currentGlidePosOLD", Main.sensorsMap.get("currentGlidePos")); 

        Main.motorControllerMap.put("setGlideSpeed", glideServoSpeed);
        glideStop = Functions.BooleanInRange(camMiddleForGrab - fruitPosY, -2, 2);

        SmartDashboard.putBoolean("glideStop", glideStop);

        // if (Main.sensorsMap.get("currentGlidePos") < -2 && glideServoSpeed > 0) { // Тут смотрим где объект если он слишком близко то отъезжаем колесами
        //     Main.motorControllerMap.put("setGlideSpeed", glideServoSpeed);
        //     Main.motorControllerMap.put("speedX", 0.0);
        // } else {
        //     Main.motorControllerMap.put("speedX", glideServoSpeed*10);
        //     Main.motorControllerMap.put("setGlideSpeed", 0.0);
        // }
        

        // Если переехали лимит выдвижного механизма
        if (Main.sensorsMap.get("currentGlidePos") >= 25 || StateMachine.iterationTime > 15) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }

        // Если мы выровнились по объекту то переходим к следующему этапу
        if (glideStop && StateMachine.iterationTime > 2) {
            newStates.add(new AutoGrab()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("setGlideSpeed", 0.0);
        // Main.motorControllerMap.put("glideMode", 0.0);

        Main.sensorsMap.put("camTask", 0.0);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public boolean isFinished() {
        // return false;
        return statesEnd;
    }
}