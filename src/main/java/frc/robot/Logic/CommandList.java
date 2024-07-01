package frc.robot.Logic;

import java.util.ArrayList;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.*;

public class CommandList {

    private String currentCommandName;
    private ArrayList<IState> newStates = new ArrayList<>();

    public void setCurrentCommand(String commandName) {
        this.currentCommandName = commandName;
        newStates.clear();
    }

    public void addCommand() {
        
        if (currentCommandName.equals("MOVE_FROM_START_TO_CH1")) {
            newStates.add(new SimpleDrive(90, 0)); 
            newStates.add(new SimpleDrive(0, 90)); 
            newStates.add(new AlignSharp(14));
            newStates.add(new SimpleDrive(0, 90));
            newStates.add(new DriveSonic(82));
            newStates.add(new Transition()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_FINISH")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_START_TO_CH2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH2_TO_FINISH")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_START_TO_CH3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH3_TO_FINISH")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_LZ")) {
            newStates.add(new DriveSonic(90));
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_FIRST_TZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_FIRST_TZ")) {
            
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_FIRST_RZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_FRIST_RZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_LZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_SECOND_TZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_TZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_SECOND_RZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_SECOND_RZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_THIRD_LZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_THIRD_TZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_THIRD_TZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_THIRD_RZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_CH1_TO_THIRD_RZ")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

// ------------------------------------ ИЗ ЗОН В КОНТЕЙНЕРЫ ------------------------------------

        if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON1")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON2")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON3")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_FIRST_LZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_TZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_FIRST_RZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_SECOND_LZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_TZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_SECOND_RZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }


        if(currentCommandName.equals("MOVE_FROM_THIRD_LZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_TZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }

        if(currentCommandName.equals("MOVE_FROM_THIRD_RZ_TO_CON4")) {
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
        }
    }
}