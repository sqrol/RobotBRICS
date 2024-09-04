package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;

public class DriveSonic implements IState {

    private double X = 0;
    private double diffX, diffZ = 0;
    private double lastGyro = 0;

    private boolean finishX, finishZ = false;

    private double[][] sonicArray = { { 0, 1, 2.5, 5, 10, 14, 18, 35, 50, 100 }, 
                                      { 0, 3, 10, 12, 18, 35, 50, 65, 78, 80 } };

    private static double[][] degFunction = { { 0.1, 2, 4, 8, 12, 15, 20, 25 },
                                               { 5, 9, 11, 18, 24, 26, 33, 35 } };

    public DriveSonic(double X) {
        this.X = X;
    }

    @Override
    public void initialize() {
        lastGyro = Main.sensorsMap.get("srcGyro");
        Main.motorControllerMap.put("resetEncRight", 1.0);
        Main.motorControllerMap.put("resetEncLeft", 1.0);
    }

    @Override
    public void execute() {
        double backSonicDist = Main.sensorsMap.get("sonicRight");

        diffX = X - backSonicDist;
        diffZ = lastGyro - Main.sensorsMap.get("srcGyro");

        double speedX = Functions.TransitionFunction(diffX, sonicArray);
        double speedZ = Functions.TransitionFunction(diffZ, degFunction);

        Main.motorControllerMap.put("speedX", speedX);
        Main.motorControllerMap.put("speedZ", -speedZ);
        
        finishX = Functions.BooleanInRange(X - backSonicDist, -0.5, 0.5);
        finishZ = Functions.BooleanInRange(lastGyro - Main.sensorsMap.get("srcGyro"), -0.2, 0.2);
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("resetEncRight", 1.0);
        Main.motorControllerMap.put("resetEncLeft", 1.0);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);

        Main.sensorsMap.put("resetGyro", 1.0);
    }

    @Override
    public boolean isFinished() {
        return finishX && finishZ;
    }
}
