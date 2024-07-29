package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.Transition;
import frc.robot.Maths.Common.Functions;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;

public class AutoStart implements IState {

    private boolean flag;
    
    private boolean treeMode = false;
    private double GRIP_ROTATE = 0.0;
    private ArrayList<IState> newStates = new ArrayList<>();
    private double currentColorIndex = 0; 
    private static double startTime = 0;

    public AutoStart() {
        this.treeMode = false;
        GRIP_ROTATE = 70.0;
    }

    public AutoStart(boolean treeMode) {
        this.treeMode = treeMode;
        GRIP_ROTATE = 70.0; // 30
    }

    @Override
    public void initialize() {
        Main.camMap.put("currentColorIndex", 0.0);
        
        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);

        flag = false;
    }

    @Override
    public void execute() {
        
        // Перебераем цвета 
        double currentTime = Timer.getFPGATimestamp();

        if (currentTime - startTime >= 1) {
            startTime = currentTime;
            if (Main.camMap.get("targetFound") == 0) {
                currentColorIndex++; 
            }   
        }

        SmartDashboard.putNumber("currentColorIndex", currentColorIndex);
        Main.camMap.put("currentColorIndex", currentColorIndex);

        if(treeMode) {
            Main.sensorsMap.put("camTask", 4.0);

            if(Main.camMap.get("targetFound") != 0 ) {
                newStates.add(new AutoRotate(true));
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                flag = true;
            }

        } else {
            Main.sensorsMap.put("camTask", 1.0);
            if (Main.camMap.get("targetFound") != 0 && StateMachine.iterationTime > 1) {
                newStates.add(new AutoRotate()); 
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                flag = true;
            }
        }
    }

    @Override
    public void finilize() {
        if (!flag) Main.sensorsMap.put("camTask", 0.0);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 10 || flag;
        // return false;
    }
}