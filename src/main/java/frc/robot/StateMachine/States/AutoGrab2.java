package frc.robot.StateMachine.States;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.Maths.Common.Functions;
import frc.robot.Main;

public class AutoGrab2 implements IState {

    private Boolean foundFlag; 
    private double lastRotateDegree = 0;
    private double currentTargetDegree = 0;
    private double fruitPosX = 0;
    
    private static final double[][] arrForLift = { { 0, 290, 640} , { -47, 0, 47} }; 

    public AutoGrab2() {
        this.foundFlag = false;
    }

    @Override
    public void initialize() {
        // Начинаем работу
        Main.sensorsMap.put("camTask", 1.0);

        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("servoGripRotate", 70.0);
        Main.camMap.put("glideObjectFound", 0.0);
    }

    @Override
    public void execute() {

        // Поворачиваем поворотный механизм к найденному фрукту
        if (Main.camMap.get("targetFound") != 0) {
            foundFlag = true;
            fruitPosX = Main.camMap.get("currentCenterX");
            // нужно получить текущий угол поворотного механизма чтобы просто к нему прибавлять или отнимать (lastRotateDegree)
        }

        if (foundFlag) {
            currentTargetDegree = Functions.TransitionFunction(fruitPosX, arrForLift);
            Main.motorControllerMap.put("targetRotateDegree", currentTargetDegree);
        }

        // Выдвигаем выдвижной механизм к объекту
        Main.sensorsMap.put("camTask", 2.0);
        // Начинаем выдвигает выдвижной механизм
        // if (currentGlidePosition <= 20) {
        // }
        if (Main.camMap.get("glideObjectFound") != 0) {
            // Останавливаем выдвижной механизм
            // Опускаем лифт (данный параметр делаем контролируемым) (glideObjectFound)
            // Закрываем захват 
        }

        // searchTypeForGlide
        // searchTypeForRotate

        // Возврат СМО в исходное положение с учетом вводных параметров

        // Нужно сделать отдельные классы для поворота, выравнивания и завершения работы с объектом с учетом входных параметров

    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("camTask", 0.0);
    }

    @Override
    public boolean isFinished() {
        return Main.camMap.get("targetFound") == 0 && StateMachine.iterationTime > 10 || Main.switchMap.get("rotateStop") && foundFlag;
    }

}