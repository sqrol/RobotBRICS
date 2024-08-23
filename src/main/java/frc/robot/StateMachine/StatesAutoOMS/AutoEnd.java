package frc.robot.StateMachine.StatesAutoOMS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoEnd implements IState {
    private boolean finish = false;
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
        // boolean stop = Main.switchMap.get("rotateStop");

        // SmartDashboard.putBoolean("AutoEnd.rotateStop", stop);
        Main.motorControllerMap.put("targetLiftPos", 0.0);
        if(Main.switchMap.get("liftStop")) {
            Main.motorControllerMap.put("glideMode", 1.0);
            Main.motorControllerMap.put("setGlideSpeed", -0.32);
            // Main.motorControllerMap.put("servoGripRotate", 79.0);
            if(Main.switchMap.get("limitSwitchGlide")) {
                Main.motorControllerMap.put("targetRotateDegree", 0.0);
                if(Main.switchMap.get("rotateStop")) {
                    finish = true;
                }
            }
        }
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("servoGripRotate", 76.0);
        Main.camMap.put("targetFound", 0.0);
    }

    @Override
    public boolean isFinished() {
        return finish ;
        // return Main.switchMap.get("liftStop") && Main.switchMap.get("rotateStop") && Main.switchMap.get("limitSwitchGlide") && StateMachine.iterationTime > 2;
    }
}