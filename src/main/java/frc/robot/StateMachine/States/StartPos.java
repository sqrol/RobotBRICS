package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class StartPos implements IState {

    private boolean succesInit = false; 

    @Override
    public void initialize() {
        Main.sensorsMap.put("resetGyro", 1.0);
        Main.motorControllerMap.put("resetEncs", 1.0);
        Main.motorControllerMap.put("resetPID", 1.0);
        Main.motorControllerMap.put("glideMode", 0.0);
        Main.sensorsMap.put("indicationMode", Constants.INDICATION_WAITING);
    }

    @Override
    public void execute() {

        if(!Main.switchMap.get("limitSwitchGlide")) {
            Main.switchMap.put("initGlide", true);
        } 
        if(!Main.switchMap.get("limitSwitchLift")) {
            Main.switchMap.put("initLift", true);
        } 
        
        if(Main.switchMap.get("limitSwitchLift") && Main.switchMap.get("limitSwitchGlide")){
            Main.switchMap.put("initLift", false);
            Main.switchMap.put("initGlide", false);
            Main.motorControllerMap.put("resetEncLift", 1.0);
            succesInit = Main.switchMap.get("limitSwitchLift") && Main.switchMap.get("limitSwitchGlide");
        }
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }    

    @Override
    public void finilize() {
        Main.motorControllerMap.put("liftSpeed", 0.0);
        Main.switchMap.put("initLift", false);
        Main.switchMap.put("initGlide", false);
        Main.motorControllerMap.put("resetEncLift", 0.0);
        Main.sensorsMap.put("indicationMode", Constants.INDICATION_IN_PROGRESS);
    }

    @Override
    public boolean isFinished() {
        return succesInit && StateMachine.iterationTime > 0.5 ;
        // return false;
    }
}