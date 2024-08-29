package frc.robot.Logic;

import java.util.ArrayList;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.*;
import frc.robot.StateMachine.StatesAutoOMS.AutoEnd;
import frc.robot.StateMachine.StatesAutoOMS.AutoStart;
import frc.robot.StateMachine.StatesOMS.*;

public class CommandList {

    private String currentCommandName;
    private ArrayList<IState> newStates = new ArrayList<>();

    public void setCurrentCommand(String commandName) {
        this.currentCommandName = commandName;
        newStates.clear();
    }

    public void addCommand() {

    if(currentCommandName.equals("AUTO_GRAB_UPPER")) {
        newStates.add(new AutoStart());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("RESET_FRUIT")) {
        newStates.add(new SetGrabPosition("OPEN", true));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("C1")) {

        // Проезд на 1 метр вперед
        newStates.add(new SimpleDrive(105, 0));
        newStates.add(new StartPos());


        // Проезд змейкой
        newStates.add(new SimpleDrive(62, 0));
        newStates.add(new SimpleDrive(0, -90));

        newStates.add(new SimpleDrive(75, 0));
        newStates.add(new SimpleDrive(0, 90));

        newStates.add(new SimpleDrive(60, 0));
        newStates.add(new SimpleDrive(0, 90));

        newStates.add(new SimpleDrive(75, 0));
        newStates.add(new SimpleDrive(0, -90));

        newStates.add(new SimpleDrive(65, 0));
        newStates.add(new SimpleDrive(0, -90));

        newStates.add(new SimpleDrive(71, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(60, 0));
        newStates.add(new StartPos());


        // Работа ультразвуковых датчиков
        newStates.add(new SonicCheck());
        newStates.add(new StartPos());


        // Работа инфракрасных датчиков
        newStates.add(new SharpCheck());
        newStates.add(new StartPos());


        // Распознавание объекта
        newStates.add(new CheckFruit());
        newStates.add(new End());

        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("C2")) {

        // Идентификация и отображение
        newStates.add(new CheckRotten());
        newStates.add(new StartPos());


        // Распознавание элемента по его геометрии
        newStates.add(new CheckFruit());
        newStates.add(new StartPos());


        // Управление элементом
        newStates.add(new AutoStart());
        newStates.add(new End());

        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// --------------------------------- ЗАХВАТЫ ---------------------------------

    if(currentCommandName.equals("ROTATE_0")) {
        newStates.add(new SetRotatePosition(0));
        newStates.add(new AutoStart());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("ROTATE_10")) {
        newStates.add(new SetRotatePosition(10));
        newStates.add(new AutoStart());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("ROTATE_15")) {
        newStates.add(new SetRotatePosition(15));
        newStates.add(new AutoStart());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("ROTATE_-15")) {
        newStates.add(new SetRotatePosition(-15));
        newStates.add(new AutoStart());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("ROTATE_45")) {
        newStates.add(new SetRotatePosition(45));
        newStates.add(new AutoStart());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("ROTATE_-45")) {
        newStates.add(new SetRotatePosition(-45));
        newStates.add(new AutoStart());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }
        
// --------------------------------- ИЗ СТАРТА КО ВСЕМ ЧЕКПОИНТАМ ---------------------------------

    if (currentCommandName.equals("MOVE_FROM_START_TO_CH1")) {
        newStates.add(new SimpleDrive(90, 0)); 
        newStates.add(new SimpleDrive(0, 90)); 
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(82));
        newStates.add(new Reset());
        newStates.add(new Transition()); 
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_START_TO_CH2")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(68));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(10));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_START_TO_CH3")) {
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(-70, 0));
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// --------------------------------- ИЗ CH1 К ДЕРЕВЬЯМ ---------------------------------

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_LZ")) {
        newStates.add(new SimpleDrive(13, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_TZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(40));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(56, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(6, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_LOZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(40));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(56, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(6, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FRIST_RZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(40));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(80, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(6, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_LZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(208, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(20));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(16, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_TZ")) {
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(208, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(33, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(6, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_LOZ")) {
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(208, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(33, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(6, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_RZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(232, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_THIRD_LZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(185, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(35, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_THIRD_TZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(167, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(35, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_THIRD_LOZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(167, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(35, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_THIRD_RZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(126, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// ------------------------------------- ИЗ CH2 К ДЕРЕВЬЯМ -------------------------------------

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_SECOND_LZ")) {
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(53 ));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(23));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_SECOND_TZ")) {
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(45));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(73, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_SECOND_LOZ")) {
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(45));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(73, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_SECOND_RZ")) {
        newStates.add(new DriveSonic(70));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_THIRD_LZ")) {
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(40));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// ------------------------------------- ИЗ CH3 К ДЕРЕВЬЯМ -------------------------------------

    if(currentCommandName.equals("MOVE_FROM_CH3_TO_THIRD_LZ")) {
        newStates.add(new SimpleDrive(110, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(25));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(40));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH3_TO_THIRD_TZ")) {
        newStates.add(new SimpleDrive(110, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(43));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(55, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH3_TO_THIRD_LOZ")) {
        newStates.add(new SimpleDrive(110, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(43));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(55, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH3_TO_THIRD_RZ")) {
        newStates.add(new SimpleDrive(125, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// ------------------------------ ПРОМЕЖУТОЧНЫЕ МЕЖДУ ЗОНАМИ ДЕРЕВА ------------------------------

    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_FIRST_TZ")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(48, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(20, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_FIRST_LOZ")) {
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_FIRST_RZ")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(10, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(15, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CH1")) {
        newStates.add(new SimpleDrive(-20, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(95, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(82));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_SECOND_TZ")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(20, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(10, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_SECOND_LOZ")) {
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_SECOND_RZ")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(60, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(70));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CH2")) {
        newStates.add(new DriveSonic(10));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_THIRD_TZ")) {
        newStates.add(new DriveSonic(20));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(87, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(12, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_THIRD_LOZ")) {
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_THIRD_RZ")) {
        newStates.add(new SimpleDrive(-15, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(75, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(12, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CH2")) {
        newStates.add(new SimpleDrive(-15, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(68));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(10));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CH3")) {
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// ------------------------------------ ИЗ ЗОН В КОНТЕЙНЕРЫ ------------------------------------

    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON1")) {
        newStates.add(new DriveSonic(10));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14)); 
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-39, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-39, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-39, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-6, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(20, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON1")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(30, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-40, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-40, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-160, 0));
        newStates.add(new DriveSonic(10));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(76));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-30, 0));
        newStates.add(new DriveSonic(20));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(72, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(72, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(97, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(200, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(200, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(200, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-6, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(20, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(210, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON2")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(65));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(20, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(210, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-20, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(181, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-20, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(181, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(24));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-160, 0));
        newStates.add(new DriveSonic(10));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(76));
        newStates.add(new SimpleDrive(0, 180));
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(20));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-5, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(56));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(203, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(144, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(144, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(123, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-11, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-11, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-11, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-6, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(67));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON3")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(65));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON3")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(30, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_CON3")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(30, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-30, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(110, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON4")) {
        newStates.add(new SimpleDrive(-4, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON4")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(60, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON4")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(60, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON4")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(85, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON4")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(184, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON4")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(184, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_CON4")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(77));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(184, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON4")) {
        newStates.add(new SimpleDrive(-6, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(258, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON4")) {
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(60));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(260, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON4")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(173, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_CON4")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(173, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON4")) {
        newStates.add(new DriveSonic(90));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// --------------------------------- ИЗ КОНТЕЙНЕРОВ В ЧЕКПОИНТЫ ---------------------------------

    if(currentCommandName.equals("MOVE_FROM_CON1_TO_CH1")) {
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, 180));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(82));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON1_TO_CH2")) {
        newStates.add(new SimpleDrive(-280, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(30, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(10));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON1_TO_CH3")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, 180));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_CON2_TO_CH1")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON2_TO_CH2")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(75));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(200, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(20, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(10));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON2_TO_CH3")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(82));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_CON3_TO_CH1")) {
        newStates.add(new SimpleDrive(-60, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(225, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(82));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON3_TO_CH2")) {
        newStates.add(new SimpleDrive(-90, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(10));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON3_TO_CH3")) {
        newStates.add(new SimpleDrive(-80, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(220, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_CON4_TO_CH1")) {
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(30, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(82));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON4_TO_CH2")) {
        newStates.add(new SimpleDrive(-50, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(10));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON4_TO_CH3")) {
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// --------------------------------- ИЗ КОНТЕЙНЕРОВ НА ФИНИШ ---------------------------------

    if(currentCommandName.equals("MOVE_FROM_CON1_TO_FINISH")) {
        newStates.add(new SimpleDrive(-160, 0));
        newStates.add(new End());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON2_TO_FINISH")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(90, 0));
        newStates.add(new End());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON3_TO_FINISH")) {
        newStates.add(new SimpleDrive(-75, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(140, 0));
        newStates.add(new End());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON4_TO_FINISH")) {
        newStates.add(new SimpleDrive(-70, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(75, 0));
        newStates.add(new End());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("END")) {
        newStates.add(new End()); 
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }
    }
}