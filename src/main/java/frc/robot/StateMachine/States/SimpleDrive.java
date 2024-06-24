package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class SimpleDrive implements IState{

    private double XPosition, ZPosition = 0;
    private double posX, gyro = 0;
    private double speedX, speedZ = 0;

    private boolean finishX, finishZ = false;
    
    private double[][] speedXArray = { { 0, 0.7, 1.4, 3, 5, 10, 12, 14, 18, 35, 50, 100}, 
                                       { 0, 1, 2, 3, 8, 15, 30, 45, 50, 65, 75, 95} };

    private double[][] speedZArray = { { 0.1, 0.5, 1.5, 2, 3, 6, 12, 26, 32, 50 }, 
                                       { 10, 15, 17, 20, 25, 30, 40, 53, 60, 70 } };

    private double[][] speedZArrayJustTurn = { { 0, 1, 5, 8, 10, 26, 41, 60, 90 }, 
                                                 { 0, 2, 5, 10, 15, 20, 35, 45, 70 } };

    private double[][] startKoefSpeedForX = { { 0.33, 0.66, 1 }, { 0.33, 0.66, 1 } };

    public SimpleDrive(double XPosition, double ZPosition){
        this.XPosition = XPosition;
        this.ZPosition = ZPosition;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("resetGyro", 1.0);
        Main.motorControllerMap.put("resetEncRight", 1.0);
        Main.motorControllerMap.put("resetEncLeft", 1.0);
    }

    @Override
    public void execute() {

        double startKoef = Functions.TransitionFunction(StateMachine.iterationTime, startKoefSpeedForX);

        if (XPosition != 0) {
            
            double currentRight = Main.motorControllerMap.get("encRight");
            double currentLeft = Main.motorControllerMap.get("encLeft");

            double gyro = Main.sensorsMap.get("srcGyro");
    
            posX = ((currentRight + currentLeft) / 2) / 48;
    
            double[] polar = Functions.ReImToPolar(posX, 0);
            double[] decard = Functions.PolarToReIm(polar[0], (polar[1] + Math.toRadians(gyro))); 
    
            double diffX = XPosition - decard[0];
            
            speedX = Functions.TransitionFunction(diffX, speedXArray);
            speedZ = Functions.TransitionFunction(-gyro, speedZArray);

            finishX = Functions.BooleanInRange(diffX, -0.5, 0.5); 
            finishZ = Functions.BooleanInRange(speedZ, -0.2, 0.2); 
    
        } else {
            speedX = 0;
            speedZ = Functions.TransitionFunction(ZPosition - gyro, speedZArrayJustTurn);
            
            finishX = true;
            finishZ = Functions.BooleanInRange(speedZ, -0.3, 0.3); 
        }

        Main.motorControllerMap.put("posX", posX);
        Main.motorControllerMap.put("speedX", speedX * startKoef);
        Main.motorControllerMap.put("speedZ", speedZ);
    }

    @Override
    public void finilize() {    
        Main.sensorsMap.put("resetGyro", 1.0);
    }

    @Override
    public boolean isFinished() {    
        return finishX && finishZ;
    }    
}
