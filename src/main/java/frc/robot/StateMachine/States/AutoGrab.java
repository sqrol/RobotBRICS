package frc.robot.StateMachine.States;

import java.util.HashMap;
import org.opencv.core.Point;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.Maths.Common.Functions;

public class AutoGrab {

    // private boolean flag = false;
    // public int nowStep = 0;
    // private final int MAX_GLIDE_POS = 11;
    // private static HashMap<String, Double> mapForLift = new HashMap<String, Double>() {
    //     {
    //         put("AppleBigRipe", 85.0);
    //         put("BIG GREEN APPLE", 85.0);
    //         put("PeerRipe", 85.0);
    //         put("GREEN PEAR", 85.0);
    //         put("AppleSmallRipe", 95.0);
    //         put("SMALL GREEN APPLE", 95.0);
    //         put("rotten", 95.0);
    //     } 
    // };

    // // private static HashMap<String, Double> mapForLift = new HashMap<String, Double>() {
    // //     {
    // //         put("AppleBigRipe", 95.0);
    // //         put("BIG GREEN APPLE", 95.0);
    // //         put("PeerRipe", 95.0);
    // //         put("GREEN PEAR", 95.0);
    // //         put("AppleSmallRipe", 95.0);
    // //         put("SMALL GREEN APPLE", 95.0);
    // //     } 
    // // };

    // private static HashMap<String, Double> mapForGrip = new HashMap<String, Double>() {
    //     {
    //         put("AppleBigRipe", 60.0);
    //         put("BIG GREEN APPLE", 60.0);
    //         put("PeerRipe", 60.0);
    //         put("GREEN PEAR", 60.0);
    //         put("AppleSmallRipe", 75.0);
    //         put("SMALL GREEN APPLE", 75.0);
    //         put("rotten", 75.0);
    //     }
    // };

    // // private static HashMap<String, Double> mapForGrip = new HashMap<String, Double>() {
    // //     {
    // //         put("AppleBigRipe", 75.0);
    // //         put("BIG GREEN APPLE", 75.0);
    // //         put("PeerRipe", 75.0);
    // //         put("GREEN PEAR", 75.0);
    // //         put("AppleSmallRipe", 75.0);
    // //         put("SMALL GREEN APPLE", 75.0);
    // //     }
    // // };

    // // Переменные для вращающегося механизма
    // private double startKoef, diffSpeed, fruitPosX = 0; 
    // private boolean rotateStop, objectFind, objectNotFound = false; 

    // private boolean objectDetectionFlag, oneObject = true;

    // private double currentTargetDegree = 0; 

    // private static final double[][] speedForRotate =  { { 0, 50, 100, 150, 200 }, { 0, 8, 18, 35, 50} };
    // private double[][] startKoefSpeedForX = { { 0, 0.33, 0.66, 1 }, { 0, 0.33, 0.66, 1 } };
    // // private static final double[][] arrForLift = { { 0, 640} , { 0, 60 } };

    // private static final double[][] arrForLift = { { 0, 290, 640} , { -47, 0, 47} };

    // // Переменные для выдвижного механизма

    // private double fruitPosY = 0; 
    // private double diffSpeedForGlide = 0; 
    // private boolean glideStop = false; 
    // private double stopTimer;

    // private double localTimer;

    // private double lastUpdateTime = 0;
    // private static final double STEP = 1.0; 

    // private static final double[][] speedForGlideServo = { { 0, 4, 10, 20, 40, 60, 80, 100 } ,
    //                                                     { 0, 0.08, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5} };
    
    // public AutoGrab() {
    //     this.nowStep = 0;
    //     this.localTimer = 0;
    //     this.objectNotFound = false;
    //     train.resizeForGlide = false;
    //     // objectDetectionFlag = true; 
    // }

    // public AutoGrab(boolean oneObject) {
    //     this.oneObject = oneObject; 
    //     this.nowStep = 0; 
    //     this.objectNotFound = false;
    // }

    

    // private boolean smoothServoMovement(double targetPosition, double DELAY) {
    //     double currentPosition = train.getGripServoValue();
    //     double step = STEP * (targetPosition > currentPosition ? 1 : -1);

    //     double currentTime = Timer.getFPGATimestamp();

    //     if (Math.abs(targetPosition - currentPosition) > Math.abs(step)) {
    //         if (currentTime - lastUpdateTime >= DELAY) {
    //             currentPosition += step;
    //             train.setGripServoValue(currentPosition); // Устанавливаем новое положение серво
    //             lastUpdateTime = currentTime; // Обновляем время последнего обновления
    //         }
    //     }     

    //     return Function.BooleanInRange(Math.abs(targetPosition - currentPosition), -1, 1); 
    // }
    
    // private void servoController(double gripValue, double gripRotateValue) {
    //     train.setGripServoValue(gripValue);
    //     train.setGripRotateServoValue(gripRotateValue);
    // }

    // @Override
    // public boolean initialize() {
    //     RobotContainer.train.nowTask = 2; 
    //     return false;
    // }

    // @Override
    // public boolean execute() {
        
        
    //     double startTime = Timer.getFPGATimestamp();

    //     switch (nowStep) {
    //         case 0: // Выравнивание вращающегося механизма на объекте
    //             // train.liftToMovePos(60);
    //             servoController(15, 82); // 130 270
    //             SmartDashboard.putString("detectionResult", train.detectionResult);
    //             if (train.centersForClass.isEmpty()) {
    //                 fruitPosX = 0; 
    //                 objectFind = false;
    //             } else {
    //                 if (!objectDetectionFlag) {
                        
    //                     for (Point center : train.centersForClass) {
                            
    //                         fruitPosX  = center.x; 
    //                     }
                        
    //                     if (!oneObject) {
    //                         objectDetectionFlag = false;
    //                     }
    //                 }
    //                 objectFind = true;
    //             }
                
    //             if (oneObject) {
    //                 SmartDashboard.putBoolean("objectFind", objectFind);
    //                 if (objectFind) {

    //                     this.startKoef = Function.TransitionFunction(Timer.getFPGATimestamp() - StateMachine.iterationTime, startKoefSpeedForX);
    //                     this.diffSpeed = -Function.TransitionFunction(280 - this.fruitPosX, speedForRotate);
    //                     this.rotateStop = Function.BooleanInRange(this.diffSpeed, -5, 5);
    //                     train.rotateMotorSpeedThread = diffSpeed * startKoef;
    //                     SmartDashboard.putNumber("diffSpeedAutoGrab", diffSpeed);

    //                 } else {

    //                     train.rotateMotorSpeedThread = 0;

    //                 }

    //                 } else {

    //                     currentTargetDegree = Function.TransitionFunction(fruitPosX, arrForLift);
    //                     SmartDashboard.putNumber("currentTargetDegree: ", currentTargetDegree);
    //                     this.rotateStop = train.rotateToPos(currentTargetDegree); 
    //                 }

    //             SmartDashboard.putBoolean("rotateStop", rotateStop);
    //             if (rotateStop) {

    //                 train.rotateMotorSpeedThread = 0;

    //             if (this.rotateStop && Timer.getFPGATimestamp() - stopTimer > 2) {
                    
    //                 localTimer = Timer.getFPGATimestamp();
    //                 nowStep++;
    //             }   

    //             } else {
                    
    //                 stopTimer = Timer.getFPGATimestamp();
    //             } 

    //             if (objectFind) {
    //                 objectNotFound = false; // Ничего не нашли выходим из данной команды
    //             } else {
    //                 objectNotFound = Timer.getFPGATimestamp() - StateMachine.iterationTime > 5; // Ничего не нашли выходим из данной команды
    //             }
                
    //             break;
    //         case 1:
    //             // double startTime = Timer.getFPGATimestamp();
    //             train.setGripRotateServoValue(93);
    //             SmartDashboard.putBoolean("resizeForGlide", train.resizeForGlide);
    //             train.resizeForGlide = true;
                
    //             if (train.centersForClass.isEmpty()) {
    //                 fruitPosY = 0; 
    //                 objectFind = false;
    //             } else {
    //                 train.nowTask = 2;
    //                 for (Point center : train.centersForClass) {
    //                     fruitPosY  = center.y; 
    //                 }
    //                 objectFind = true;
    //             }
                
    //             double glideServoSpeed = Function.TransitionFunction(295 - fruitPosY, speedForGlideServo);
                
    //             glideStop = Function.BooleanInRange(295 - fruitPosY, -3, 3);
    //             SmartDashboard.putNumber("270 - fruitPosY", 295 - fruitPosY);
                
    //             // if(train.currentGlidePosition >= MAX_GLIDE_POS && !glideStop) {
    //             //     glideServoSpeed = 0;
    //             //     train.servoGlidePosition(0);
    //             //     if(train.currentGlidePosition == 0) {
    //             //         objectFind = false;
    //             //     }
    //             // }

    //             if(objectFind) {
    //                 train.justMoveForGlide(glideServoSpeed * 0.9);
    //                 if (glideStop) {
    //                     train.justMoveForGlide(0);

    //                     if (Timer.getFPGATimestamp() - stopTimer > 0.8) {
    //                         train.resizeForGlide = false;
    //                         localTimer = Timer.getFPGATimestamp();
    //                         train.detectionResult = "rotten";
    //                         train.fruitFind = true;
    //                         nowStep++;
    //                     } else {
    //                         stopTimer = Timer.getFPGATimestamp();
    //                     }
    //                 }
    //             }
    //             break;
    //         case 2: // Опускаем лифт для захвата
    //             train.nowTask = 0;
    //             if (train.liftToMovePos(mapForLift.get(train.detectionResult)) && Timer.getFPGATimestamp() - localTimer > 1) {
    //                 localTimer = Timer.getFPGATimestamp();
    //                 nowStep++;
    //             }
    //             break;
    //         case 3: // Захват 
    //         train.nowTask = 0;
    //             if (smoothServoMovement(mapForGrip.get(train.detectionResult), 0.01) && Timer.getFPGATimestamp() - localTimer > 1) {
    //                 localTimer = Timer.getFPGATimestamp();
    //                 nowStep++;
    //             }
    //             break;
    //         case 4: // Возврат СМО в исходное сотояние
    //             if(train.liftToMovePos(-1)) {
    //                 train.servoGlidePosition(0);
    //                 if (Timer.getFPGATimestamp() - localTimer > 1 && train.rotateToPos(0) && train.currentGlidePosition == 0) {
    //                     if(train.glideExit) {
    //                         train.detectionResult = "";
    //                         localTimer = 0;
    //                         train.nowTask = 0;
    //                         objectFind = false;
    //                         train.rotateMotorSpeedThread = 0;
    //                         SmartDashboard.putString("EXIT", "FOUND");
    //                         return true;
    //                     }
    //                 }      
    //             }
    //             break;
    //         default:
    //             nowStep = 0;
    //             train.detectionResult = "";
    //             localTimer = 0; 
    //             train.nowTask = 0;
    //             train.rotateToPos(0);
    //             objectFind = false;
    //             SmartDashboard.putString("EXIT", "default");
    //             return true;
    //     }

    //     if (objectNotFound) { // Ничего не нашли выходим из данной команды
    //         train.detectionResult = "";
    //         train.rotateMotorSpeedThread = 0;
    //         localTimer = 0;
    //         train.nowTask = 0;
    //         train.rotateToPos(0);
    //         objectFind = false;
    //         SmartDashboard.putString("EXIT", "NOT FOUND");
    //         return true;
    //     }
        
    //     train.setAxisSpeed(0, 0);

    //     return false;
    // }

    // @Override
    // public void finilize() {

    // }

    // @Override
    // public boolean isFinished() {
    //     // TODO Auto-generated method stub
    //     return false;
    // }
}
