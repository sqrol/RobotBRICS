package frc.robot.StateMachine.StatesAutoOMS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoEnd implements IState {
    private boolean finish, treeMode = false;

    public AutoEnd() {

    }

    public AutoEnd(boolean treeMode) {
        this.treeMode = treeMode;
    }

    @Override
    public void initialize() {
        if(Main.camMap.get("targetFound") == 1.0 && Main.camMap.get("currentColorIndex") != 1.0) {
            Main.sensorsMap.put("camTask", 11.0);
        }
        Main.switchMap.put("liftStop", false);
        Main.switchMap.put("rotateStop", false);
        Main.switchMap.put("glideStop", false);    
    }

    @Override
    public void execute() {
        if(treeMode) {
            Main.motorControllerMap.put("targetLiftPos", 0.0);
            if(Main.switchMap.get("limitSwitchLift")) {
                Main.motorControllerMap.put("glideMode", 0.0);
                Main.sensorsMap.put("targetGlidePos", 0.0);
                if(Main.switchMap.get("glideStop")) {
                    Main.motorControllerMap.put("targetRotateDegree", 0.0);
                    finish = true;
                }
            } 
        } else {
            if(!Main.switchMap.get("limitSwitchGlide")) {
                Main.motorControllerMap.put("targetLiftPos", 60.0);
                if(Main.switchMap.get("liftStop")) {
                    Main.motorControllerMap.put("glideMode", 0.0);
                    Main.motorControllerMap.put("targetGlidePos", 0.0);
                } 
            } else {
                Main.motorControllerMap.put("setGlideSpeed", 0.0); 
                Main.switchMap.put("initLift", true);
                if(Main.switchMap.get("limitSwitchLift")) {
                    Main.motorControllerMap.put("targetRotateDegree", 0.0);
                    finish = true;
                }
            }
        }
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("camTask", 0.0);
        Main.camMap.put("targetFound", 0.0);
        if(!Main.stringMap.get("detectedFruit").equals("none")) {
            Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_DROP);
        } else {
            Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_FLOOR);
        }
        Main.sensorsMap.put("camTask", 0.0);
        Main.camMap.put("currentColorIndex", 0.0);
        Main.motorControllerMap.put("targetLiftPos", 0.0);
        Main.switchMap.put("initLift", false);
    }

    @Override
    public boolean isFinished() {
        return finish;
        // return Main.switchMap.get("liftStop") && Main.switchMap.get("rotateStop") && Main.switchMap.get("limitSwitchGlide") && StateMachine.iterationTime > 2;
    }
}