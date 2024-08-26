package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class CameraTest implements IState {


    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 10.0);
        Main.camMap.put("targetColorFound", 0.0);
        Main.camMap.put("currentColorIndex", 0.0);
    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("servoGripRotate", 112.0);
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("camTask", 0.0);
        Main.camMap.put("targetColorFound", 0.0);
    }

    @Override
    public boolean isFinished() {
        // return Main.camMap.get("targetColorFound") == 1.0;
        return false;
    }
}