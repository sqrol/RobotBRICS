package frc.robot.StateMachine.States;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class CameraTest implements IState {


    @Override
    public void initialize() {
        
        Main.camMap.put("targetColorFound", 0.0);
        Main.camMap.put("currentColorIndex", 0.0);
    }

    @Override
    public void execute() {
        if(StateMachine.iterationTime < 2) {
            Main.motorControllerMap.put("targetLiftPos", 3.0);
            Main.motorControllerMap.put("glideMode", 1.0);
            
            
        } else {
            Main.sensorsMap.put("camTask", -2.0);
            Main.motorControllerMap.put("setGlideSpeed", 0.1);
            if(!Main.switchMap.get("trackAutoGlide")) {
                Main.motorControllerMap.put("setGlideSpeed", 0.0);
            }
            
        }
        // Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_DROP);
        // Main.motorControllerMap.put("servoGrab", Constants.GRAB_OPEN);
        
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