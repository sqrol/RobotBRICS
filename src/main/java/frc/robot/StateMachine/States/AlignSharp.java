package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AlignSharp implements IState {

    private boolean finishX, finishZ = false;

    private double X = 0;
    private double speedX, diffX = 0;
    private double speedZ = 0;
    private double diffSharp, lastGyro = 0;
    private double coefForTime = 0;
 
    private static double[][] XArray = { { 0, 0.1, 2.5, 5, 10, 15, 25, 30 },
                                          { 0, 0.4, 5, 12, 20, 36, 60, 80 } };

    // private static double[][] XArray = { { 0, 0.1, 2.5, 5, 10, 15, 25, 30 },
    //                                       { 0, 0.4, 4, 6, 10, 15, 20, 30 } };

    private static double[][] degFunction = { { 0.1, 2, 4, 8, 12, 15, 20, 25 },
                                               { 3, 9, 11, 18, 24, 26, 33, 35 } };

    private static double[][] arrayForTime = { { 0, 0.33, 0.66, 1 },
                                               { 0, 0.33, 0.66, 1 } };

    public AlignSharp(double X) {
        this.X = X;
    }

    @Override
    public void initialize() {
        Main.sensorsMap.put("resetGyro", 1.0);
        lastGyro = Main.sensorsMap.get("srcGyro");
        Main.motorControllerMap.put("resetDriveEncs", 1.0);
    }

    @Override
    public void execute() {

        coefForTime = Functions.TransitionFunction(StateMachine.iterationTime, arrayForTime);
        
        double leftSharp = Main.sensorsMap.get("sharpLeft");
        double rightSharp = Main.sensorsMap.get("sharpRight");

        diffX = X - Math.min(leftSharp, rightSharp);
        speedX = Functions.TransitionFunction(diffX, XArray);

        if (Math.min(leftSharp, rightSharp) < 20) {
            diffSharp = leftSharp - rightSharp;
            speedZ = Functions.TransitionFunction(diffSharp, degFunction);
        } else {
            speedZ = lastGyro - Main.sensorsMap.get("srcGyro");
        }

        Main.motorControllerMap.put("speedX", -speedX * coefForTime);
        Main.motorControllerMap.put("speedZ", -speedZ);

        finishX = Functions.BooleanInRange(speedX, -0.5, 0.5);
        finishZ = Functions.BooleanInRange(speedZ, -0.2, 0.2);
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("resetEncRight", 1.0);
        Main.motorControllerMap.put("resetEncLeft", 1.0);
        Main.sensorsMap.put("resetGyro", 1.0);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public boolean isFinished() {
        return finishZ && finishX;
    }
}