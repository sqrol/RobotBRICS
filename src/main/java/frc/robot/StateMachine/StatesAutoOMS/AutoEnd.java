package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoEnd implements IState {

    public AutoEnd() {

    }

    @Override
    public void initialize() {
        Main.switchMap.put("liftStop", false);
        Main.switchMap.put("rotateStop", false);
        Main.switchMap.put("glideStop", false);        
    }

    @Override
    public void execute() {
        
        Main.motorControllerMap.put("targetLiftPos", 0.0);
        if(Main.switchMap.get("liftStop")) {
            Main.motorControllerMap.put("targetRotateDegree", 0.0);
            if(Main.switchMap.get("rotateStop")) {
                Main.motorControllerMap.put("glideMode", 0.0);
                Main.sensorsMap.put("targetGlidePos", 0.0);
                Main.motorControllerMap.put("servoGripRotate", 79.0);
            }
        }
    }

    @Override
    public void finilize() {
        // Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("serGripRotate", 79.0);
    }

    @Override
    public boolean isFinished() {
        return Main.switchMap.get("liftStop") && Main.switchMap.get("rotateStop") && Main.switchMap.get("glideStop") && StateMachine.iterationTime > 2;
    }
}