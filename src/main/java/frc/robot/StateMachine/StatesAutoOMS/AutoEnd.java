package frc.robot.StateMachine.StatesAutoOMS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        Main.switchMap.put("liftStop", false);
        Main.switchMap.put("rotateStop", false);
        Main.switchMap.put("glideStop", false);        
    }

    @Override
    public void execute() {
        if(treeMode) {
            Main.motorControllerMap.put("glideMode", 1.0);
            if(StateMachine.iterationTime < 1.5) {
                SmartDashboard.putNumber("AutoEnd check", 1111);
                Main.motorControllerMap.put("setGlideSpeed", -0.2);
            } else {
                Main.motorControllerMap.put("setGlideSpeed", 0.0);
                if(StateMachine.iterationTime > 5) {
                    Main.motorControllerMap.put("targetLiftPos", 0.0);
                    Main.motorControllerMap.put("servoGripRotate", 145.0);
                    if(Main.switchMap.get("limitSwitchLift")) {
                        SmartDashboard.putNumber("AutoEnd check", 2222);
                        Main.motorControllerMap.put("targetRotateDegree", 0.0);
                        if(Main.switchMap.get("rotateStop")) {
                            SmartDashboard.putNumber("AutoEnd check", 3333);
                            Main.motorControllerMap.put("setGlideSpeed", -0.32);
                            if(Main.switchMap.get("limitSwitchGlide")) {
                                finish = true;
                            }
                        }
                    }
                } 
            }
        } else {
            Main.motorControllerMap.put("targetLiftPos", 0.0);
            if(Main.switchMap.get("liftStop")) {
                Main.motorControllerMap.put("glideMode", 1.0);
                Main.motorControllerMap.put("setGlideSpeed", -0.32);
                if(Main.switchMap.get("limitSwitchGlide")) {
                    Main.motorControllerMap.put("targetRotateDegree", 0.0);
                    if(Main.switchMap.get("rotateStop")) {
                        finish = true;
                    }
                }
            }
        }
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("servoGripRotate", 82.0);
        Main.sensorsMap.put("camTask", 0.0);
        // Main.camMap.put("targetFound", 0.0);
    }

    @Override
    public boolean isFinished() {
        return finish;
        // return Main.switchMap.get("liftStop") && Main.switchMap.get("rotateStop") && Main.switchMap.get("limitSwitchGlide") && StateMachine.iterationTime > 2;
    }
}