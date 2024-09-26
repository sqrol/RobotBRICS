package frc.robot.Logic;

import java.util.ArrayList;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.*;
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

    if(currentCommandName.equals("AUTO_GRAB_UPPER_LZ")) {
        newStates.add(new AutoStart("LZ"));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("AUTO_GRAB_TREE")) {
        newStates.add(new AutoStart(true, 1));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("RESET_FRUIT")) {
        newStates.add(new SetGrabPosition("OPEN", true));
        newStates.add(new SetRotatePosition(0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("C1")) {
        // Идентификация и отображение
        newStates.add(new StartPos());
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

    if(currentCommandName.equals("C2")) {

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
        newStates.add(new SetRotatePosition(0));
        
        newStates.add(new AutoStart());
        newStates.add(new Transition()); 
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }
        
// --------------------------------- ИЗ СТАРТА КО ВСЕМ ЧЕКПОИНТАМ ---------------------------------

    if (currentCommandName.equals("MOVE_FROM_START_TO_CH1")) { // !!
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new Reset());
        newStates.add(new Transition()); 
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM__TO_CH2")) {
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

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_LZ")) { // !!
        
        newStates.add(new DriveSonic(80.0));
        newStates.add(new SimpleDrive(62, 0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_TZ")) { // !!
        newStates.add(new DriveSonic(80.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(53.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(110, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(14, 0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_LOZ")) { // !!
        newStates.add(new DriveSonic(80.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(53.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(110, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(14, 0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_RZ")) { // !!
        newStates.add(new DriveSonic(80.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(53.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(190 , 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(25.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(28, 0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_LZ")) { // !! 
        newStates.add(new SimpleDrive(58, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(57, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(216, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(8, 0));
        
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

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_LOZ")) { // !!
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(53.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(276, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(25, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(13, 0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_RZ")) {
        
        newStates.add(new SimpleDrive(42, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(67, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(240, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(28, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(66.0));


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
        
        newStates.add(new DriveSonic(15.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(60.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(94, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(22, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_THIRD_RZ")) { // !!
        
        newStates.add(new DriveSonic(15.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(146, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(13, 0));
        
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_THIRD_LOZ")) { // !!
        
        newStates.add(new DriveSonic(19.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(76.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(5, 0));
        
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_CH2_TO_SECOND_TZ")) {
        newStates.add(new DriveSonic(15.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(60.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(80, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(21, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_SECOND_LOZ")) {
        newStates.add(new DriveSonic(15.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(60.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(80, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(19, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_SECOND_RZ")) {
        newStates.add(new DriveSonic(67));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_THIRD_TZ")) { // !!
        newStates.add(new DriveSonic(19.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(76.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(5, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CH2_TO_THIRD_LZ")) { // !!
        
        newStates.add(new DriveSonic(26));
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

    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_FIRST_LOZ")) { // !!
        newStates.add(new ResetCameraValues());
        newStates.add(new SimpleDrive(-17, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(64, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(12, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_FIRST_TZ")) { // !!
        
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_FIRST_RZ")) { // !!
        newStates.add(new SimpleDrive(-9, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(86, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(34, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CH2")) { // !!
        newStates.add(new SimpleDrive(-40, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(91, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_SECOND_LOZ")) {
        newStates.add(new SimpleDrive(-4, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(28.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_SECOND_TZ")) {
        
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_SECOND_RZ")) {
        newStates.add(new SimpleDrive(-10, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(80.0));
        newStates.add(new SimpleDrive(16, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(67.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CH2")) { // !!
        newStates.add(new ResetCameraValues());
        newStates.add(new DriveSonic(10));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_THIRD_LOZ")) {
        newStates.add(new DriveSonic(20));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new DriveSonic(50));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(87, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(6, 0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_THIRD_TZ")) {
        newStates.add(new ResetCameraValues());
        newStates.add(new SimpleDrive(-5, 0));
        // newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_THIRD_RZ")) {
        newStates.add(new SimpleDrive(-15, 0));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SimpleDrive(75, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(25, 0));
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
        newStates.add(new SimpleDrive(-150, 0));
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// ------------------------------------ ИЗ ЗОН В КОНТЕЙНЕРЫ ------------------------------------

    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON1")) { // !!
        newStates.add(new SimpleDrive(-110, 0));
        newStates.add(new DriveSonic(15.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON1")) { // !!
        newStates.add(new SimpleDrive(-23, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(167, 0));
        newStates.add(new AlignSharp(24.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON1")) { // !!
        newStates.add(new SimpleDrive(-23, 0));
        newStates.add(new SetGripRotatePosition("FOR DROP"));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(167, 0));
        newStates.add(new AlignSharp(24.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON1")) { // !!
        newStates.add(new SimpleDrive(-25, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(53.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(267, 0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));

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
        newStates.add(new DriveSonic(54.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(258, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(88, 0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(15.0));
        newStates.add(new SimpleDrive(0, -90.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON1")) {
        newStates.add(new DriveSonic(15.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new DriveSonic(54.0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(90, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(79, 0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-61, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(243, 0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_CON1")) { // !!
        newStates.add(new SimpleDrive(-61, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(243, 0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON1")) {
        newStates.add(new SimpleDrive(-121, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(131, 0));
newStates.add(new AlignSharp(14.0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON2")) { // !!
        newStates.add(new SimpleDrive(-10, 0.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(53.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(175, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON2")) { // !!
        newStates.add(new SimpleDrive(-23, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(109, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON2")) { // !!
        newStates.add(new SimpleDrive(-23, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(109, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON2")) { // !!
        newStates.add(new SimpleDrive(-64, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON2")) {
        newStates.add(new SimpleDrive(-5, 0));
        newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON2")) {
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }
    // 
    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_CON2")) {
        // newStates.add(new SimpleDrive(-50, 0)); // это секонд лоз ту кон2
        // newStates.add(new SimpleDrive(0, 90));
        // newStates.add(new DriveSonic(77));
        // newStates.add(new SimpleDrive(0, 90));
        // newStates.add(new SimpleDrive(200, 0));
        // newStates.add(new SimpleDrive(0, 90));
        // newStates.add(new AlignSharp(14));
        // newStates.add(new SimpleDrive(0, 90));
        // newStates.add(new DriveSonic(24));
        // newStates.add(new SimpleDrive(0, -90));
        // newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON2")) { // !! поменять с секонд лоз

        newStates.add(new DriveSonic(54.0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new DriveSonic(54.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(94, 0));
newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON2")) { // !!
        newStates.add(new DriveSonic(19.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(52.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));

        //// reset
        newStates.add(new SetGrabPosition("OPEN", true));
        newStates.add(new SetRotatePosition(0));

        // con2_to_ch2
        newStates.add(new SimpleDrive(-95, 0));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new Transition());

        // ch2_to_third_loz
        newStates.add(new DriveSonic(19.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(50.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(76.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(5, 0));

        // auto_grab
        newStates.add(new AutoStart());

        // third_loz_to_con1
        newStates.add(new SimpleDrive(-61, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(243, 0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new End());
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


    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON3")) { // !!
        newStates.add(new SimpleDrive(-26, 0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(107, 0));
newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON3")) { // !!
        newStates.add(new SimpleDrive(-26, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(95, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(71, 0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON3")) { // !!
        newStates.add(new SimpleDrive(-26, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(95, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(71, 0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-33, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new DriveSonic(60.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(171, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-26, 0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new DriveSonic(60.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(169, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-21, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(60.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(174, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_LOZ_TO_CON3")) { // !!
        newStates.add(new SimpleDrive(-21, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new DriveSonic(60.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(174, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON3")) {
        newStates.add(new DriveSonic(54.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(225, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON3")) {
        newStates.add(new DriveSonic(15.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new DriveSonic(54.0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(227, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-14, 0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(155, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_LOZ_TO_CON3")) {
        newStates.add(new SimpleDrive(-14, 0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(155, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON3")) { // !!
        newStates.add(new SimpleDrive(-102, 0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON4")) { // !!
        newStates.add(new SimpleDrive(-92, 0));
        newStates.add(new DriveSonic(10.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON4")) { // !!
        newStates.add(new SimpleDrive(-17, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(153, 0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_LOZ_TO_CON4")) { // !!
        newStates.add(new SimpleDrive(-17, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(153, 0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));

        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON4")) {
        
        newStates.add(new SimpleDrive(-35, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new DriveSonic(60.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(263, 0));
newStates.add(new AlignSharp(14.0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new AlignSharp(14.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new AlignSharp(14.0));

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
        newStates.add(new SimpleDrive(-100, 0));
        newStates.add(new DriveSonic(90));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

// --------------------------------- ИЗ КОНТЕЙНЕРОВ В ЧЕКПОИНТЫ ---------------------------------

    if(currentCommandName.equals("MOVE_FROM_CON1_TO_CH1")) { // !!
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON1_TO_CH2")) {
        newStates.add(new SimpleDrive(-61, 0));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(266, 0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(25, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON1_TO_CH3")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new DriveSonic(15));
        newStates.add(new SimpleDrive(0, 180));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_CON2_TO_CH1")) { // !!
        newStates.add(new SimpleDrive(-56, 0));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new SimpleDrive(-278, 0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON2_TO_CH2")) {
        newStates.add(new SimpleDrive(-95, 0));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(20.0));
        newStates.add(new SimpleDrive(0, 90.0));      
        
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON2_TO_CH3")) {
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new DriveSonic(80));
        newStates.add(new SimpleDrive(0, 90));
        newStates.add(new AlignSharp(14));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(82));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }


    if(currentCommandName.equals("MOVE_FROM_CON3_TO_CH1")) { // !!
        newStates.add(new SimpleDrive(-129, 0));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));

        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON3_TO_CH2")) {
        newStates.add(new SimpleDrive(-38, 0));
newStates.add(new SimpleDrive(0, -90.0));
newStates.add(new SimpleDrive(196, 0));
newStates.add(new AlignSharp(14.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new AlignSharp(14.0));
newStates.add(new SimpleDrive(0, 90.0));

        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON3_TO_CH3")) {
        newStates.add(new SimpleDrive(-80, 0));
        newStates.add(new SetGripRotatePosition("FLOOR"));
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


    if(currentCommandName.equals("MOVE_FROM_CON4_TO_CH1")) { // !!
        newStates.add(new SimpleDrive(0, -90.0));
        newStates.add(new SimpleDrive(-118, 0));
        newStates.add(new SetGripRotatePosition("FLOOR"));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new AlignSharp(14.0));
        newStates.add(new SimpleDrive(0, 90.0));
        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON4_TO_CH2")) {
        newStates.add(new SimpleDrive(-125, 0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new DriveSonic(54.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new SimpleDrive(193, 0));
newStates.add(new AlignSharp(14.0));
newStates.add(new SimpleDrive(0, 90.0));
newStates.add(new AlignSharp(14.0));
newStates.add(new SimpleDrive(0, 90.0));

        newStates.add(new Reset());
        newStates.add(new Transition());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("MOVE_FROM_CON4_TO_CH3")) {
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new DriveSonic(80));
        newStates.add(new SetGripRotatePosition("FLOOR"));
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

    if(currentCommandName.equals("MOVE_FROM_CH3_TO_FINISH")) {
        newStates.add(new SimpleDrive(87, 0));
        newStates.add(new SimpleDrive(0, -90));
        newStates.add(new SimpleDrive(78, 0));
        newStates.add(new End());
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    if(currentCommandName.equals("END")) {
        newStates.add(new End()); 
        StateMachine.states.addAll(StateMachine.index + 1, newStates);
    }

    // if(currentCommandName.matches("(.*)_START_(.*)_0")) {
    //     // newStates.add(new SimpleDrive(45, 0));
    //     newStates.add(new Transition());
    //     StateMachine.states.addAll(StateMachine.index + 1, newStates);

    // }
    // if(currentCommandName.matches("MOVE_FROM_CH2_(.*)_0")) {
    //     newStates.add(new Transition());
    //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
    // }
    // if(currentCommandName.equals("RESET_FRUIT_0")) {
    //     newStates.add(new SimpleDrive(-50, 0));     
    //     newStates.add(new SetGrabPosition("OPEN", true));
    //     newStates.add(new Transition());
    //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
    // }

    // if(currentCommandName.matches("MOVE_FROM_CH(.*)_0")) {
    //     newStates.add(new ResetCameraValues());
    //     newStates.add(new SimpleDrive(50, 0));
    //     newStates.add(new Transition());
    //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
    // }

    // if(currentCommandName.matches("MOVE_FROM_CON(.*)_TO_CH(.*)_0")) {
    //     newStates.add(new Transition());
    //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
    // }

    // if(currentCommandName.matches("MOVE_FROM_(.*)Z_TO_CON(.*)_0")) {
    //     newStates.add(new ResetCameraValues());
    //     newStates.add(new Transition());
    //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
    // }

    // if(currentCommandName.matches("MOVE_FROM_(.*)Z_0")) {
    //     newStates.add(new Transition());
    //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
    // }

    
}
}