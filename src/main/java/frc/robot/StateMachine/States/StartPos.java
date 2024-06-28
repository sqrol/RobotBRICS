package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        Main.motorControllerMap.put("glideServoSpeed", 0.0);
    }

    @Override
    public void execute() {
        if(!Main.switchMap.get("limitSwitch")) {
            Main.motorControllerMap.put("liftSpeed", 60.0);
        }
        succesInit = Main.switchMap.get("limitSwitch");

        // Main.motorControllerMap.put("speedX", 50.0);
        // Main.motorControllerMap.put("speedZ", 50.0);
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("liftSpeed", 0.0);
        Main.motorControllerMap.put("resetEncLift", 1.0);
    }

    @Override
    public boolean isFinished() {
        return succesInit && StateMachine.iterationTime > 0.5;
        // return false;
    }
}