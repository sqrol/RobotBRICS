package frc.robot.Maths.Sensors;

import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class SensorsSimple {

    private static double deltaX, deltaY, deltaZ;
    private static boolean stopX, stopY, stopZ;

    private static double[][] timeSpeed = { {0, 0.15, 0.3, 0.9}, {0, 0.36, 0.7, 1} };

    private static double[][] sonicSpeed = {{0, 2, 4, 10, 18, 25, 40}, {0, 9, 15, 20, 36, 60, 80}};
    private static double[][] sharpSpeedX = {{0, 1, 4, 10, 18, 25, 40}, {0, 8, 13, 18, 36, 60, 80}};

    private static double[][] speedZfunc ={{ 0, 5, 8, 12, 15, 22, 60}, {0, 8, 15, 20, 25, 30, 60}};

    // backWall - IC, sideWall - sonic
    public static void correct(double sideWall, double backWall, boolean startAcceleration, boolean endAcceleration) {
        double startAccelerationK = 1;
        double endAccelerationSonicK = 0; 
        double endAccelerationSharpK = 0; 
        double endAccelerationGyroK = 0;

        if (startAcceleration) {
            startAccelerationK = Functions.TransitionFunction(StateMachine.iterationTime, timeSpeed);
        }

        calcDeltaX(backWall, endAcceleration);
        calcDeltaY(sideWall, endAcceleration);
        calcDeltaZ(backWall, endAcceleration);
        
        if (endAcceleration) {
            endAccelerationSonicK = Functions.TransitionFunction(-deltaY, sonicSpeed);
            endAccelerationSharpK = Functions.TransitionFunction(-deltaX, sharpSpeedX);
        } else {
            endAccelerationSonicK = Functions.TransitionFunction(-deltaY, sonicSpeed);
            endAccelerationSharpK = Functions.TransitionFunction(-deltaX, sharpSpeedX);
        }

        endAccelerationGyroK = Functions.TransitionFunction(-deltaZ, speedZfunc);

        Main.motorControllerMap.put("speedX", endAccelerationSharpK * startAccelerationK);
        Main.motorControllerMap.put("speedY", endAccelerationSonicK * startAccelerationK);
        Main.motorControllerMap.put("speedZ", endAccelerationGyroK * startAccelerationK);
    }

    public static boolean isFinished() {
        return stopX && stopY && stopZ;
    }

    private static void calcDeltaX(double backWall, boolean endAcceleration) {
        double activeSharp;
        if (backWall != 0) {
        if (Main.sensorsMap.get("sharpLeft") > Main.sensorsMap.get("sharpRight")) {
            activeSharp = Main.sensorsMap.get("sharpLeft");
        } else {
            activeSharp = Main.sensorsMap.get("sharpRight");
        }
        deltaX = activeSharp - backWall;
            double error;
            if (endAcceleration) {
                error = 0.1;
                stopX = Functions.BooleanInRange(deltaX, -error, error);
            } else {
                error = 5;
                stopX = Functions.BooleanInRange(deltaX, -error, error);
            }
        } else {
            stopX = true;
        }
    }

    private static void calcDeltaY(double straightWall, boolean endAcceleration) {
        if (straightWall != 0) {
            if (straightWall > 0) {
                deltaY = Main.sensorsMap.get("sonicRight") - straightWall;
            } else {
                deltaY = Math.abs(straightWall) - Main.sensorsMap.get("sonicLeft");
            } 
            double error;
            if (endAcceleration) {
                error = 0.5;
                stopY = Functions.BooleanInRange(deltaY, -error, error);
            } else {
                error = 5;
                stopY = Functions.BooleanInRange(deltaY, -error, error);
            }
        } else {
            stopY = true;
        }
    }

    private static void calcDeltaZ(double backWall, boolean endAcceleration) {
        double averageSharpDistance = (Main.sensorsMap.get("sharpLeft") + Main.sensorsMap.get("sharpRight")) / 2;
        if (averageSharpDistance > backWall) {
            stopZ = true;
            deltaZ = -Main.sensorsMap.get("srcGyro");
        } else {
            deltaZ = Main.sensorsMap.get("sharpRight") - Main.sensorsMap.get("sharpLeft");
            double error;
            if (endAcceleration) {
                error = 0.1;
                stopZ = Functions.BooleanInRange(deltaZ, -error, error);
            } else {
                error = 5;
                stopZ = Functions.BooleanInRange(deltaZ, -error, error);
            }
        }
    }
}
