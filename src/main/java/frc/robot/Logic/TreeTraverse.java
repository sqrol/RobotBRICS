package frc.robot.Logic;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;

public class TreeTraverse {

    private static final String[] TREE_ZONE_NAMES = {"LZ", "LOZ", "TZ", "RZ"};
    private static final String[] TREE_NUMBER = {"FIRST", "SECOND", "THIRD"};

    private static final HashMap<String, String> containersForFruits = new HashMap<String, String>() {
        {
            put(Constants.ROTTEN_PEAR, "CON2");
            put(Constants.BIG_ROTTEN_APPLE, "CON2");
            put(Constants.SMALL_ROTTEN_APPLE, "CON2");

            put(Constants.BIG_RED_APPLE, "CON4");
            put(Constants.SMALL_RED_APPLE, "CON1");
            put(Constants.YELLOW_PEAR, "CON3");
        }
    };

    private static final HashMap<String, String> grabTypeForZones = new HashMap<String, String>() {
        {
            put("LZ", "AUTO_GRAB_UPPER_LZ");
            put("RZ", "AUTO_GRAB_UPPER");
            put("TZ", "AUTO_GRAB_TREE");
            put("LOZ", "AUTO_GRAB_UPPER");
        }
    };

    // Переменные для хранения геттеров и сеттеров
    public static String lastCheckpoint = "";
    public static String treeZone = "";

    // Массивы для хранения команд
    public static ArrayList<String> logicCommand = new ArrayList<>(); // Храним команды для езды по зонам
    public static ArrayList<String> deliveryCommand = new ArrayList<>(); // Храним команды доставки

    // Другие переменные
    public static int count = 0; // Счетчик для перемещения по списку команд
    public static boolean firstCall = true;
    public static boolean firstStep = true;
    public static boolean fruitFind = false;
    public static String findFruitName = "";
    public static String lastCMDWithZone = "";
    public static String bestCPZone = "";
    public boolean lastStepWasGrab = false;

    public String execute(int index) {

        String outCMD = "none";
        String currentTreeName = "";
        String currentTreeZoneName = "";
        int kastilVar = 0;

        findFruitName = Main.stringMap.get("detectedFruit");
        fruitFind = !Main.stringMap.get("detectedFruit").equals("none");

        if (lastStepWasGrab) { // Последняя команда было сканирование поэтому проверяем нашли мы что-нибудь
            SmartDashboard.putNumber("lastStepWasGrabCheck", 1);
            deliveryCommand.clear(); // Чистим массив с командами
            if(fruitFind) { // Строим путь для доставки фрукта в контейнер если нашли что-нибудь
                System.err.println("fruitFind: " + fruitFind);
                System.err.println("findFruitName: " + findFruitName);
                SmartDashboard.putNumber("lastStepWasGrabCheck", 2);
                String[] currentZone = lastCMDWithZone.split("_"); // Разделяем строку по символу "_" (последние два элемента)
                currentTreeName = currentZone[currentZone.length - 2]; // Номер дерева
                currentTreeZoneName = currentZone[currentZone.length - 1]; // Название зоны дерева

                
                // if (index > 3) {
                //     kastilVar = 2;
                //     SmartDashboard.putNumber("lastStepWasGrabCheck", 3);
                // }   // Ебаный костыль!!! Я больше нечего не успел придумать(

                // if (currentTreeName.equals("FIRST") && currentTreeZoneName.equals("LZ")) {
                //     kastilVar = 0;
                //     SmartDashboard.putNumber("lastStepWasGrabCheck", 3);
                // }   // Ебаный костыль!!! Я больше нечего не успел придумать(
                SmartDashboard.putNumber("lastStepWasGrabCheck", 4);
                bestCPZone = choosingBestZoneForCheck(currentTreeZoneName, currentTreeName);

                addIfNotDuplicate("MOVE_FROM_" + currentTreeName + "_" + currentTreeZoneName + "_TO_" + getConForFruit(findFruitName), deliveryCommand);
                addIfNotDuplicate("RESET_FRUIT", deliveryCommand);
                addIfNotDuplicate("MOVE_FROM_"+ getConForFruit(findFruitName) +"_TO_" + bestCPZone, deliveryCommand);
                addIfNotDuplicate("MOVE_FROM_"+ bestCPZone +"_TO_" + currentTreeName + "_" + currentTreeZoneName, deliveryCommand);
                addIfNotDuplicate("AUTO_GRAB_UPPER", deliveryCommand);

                logicCommand.addAll(index+kastilVar, deliveryCommand);
                lastStepWasGrab = false;

                // Сброс переменных
                fruitFind = false;
                findFruitName = "";

                Main.stringMap.put("detectedFruit", "none");
            } else {
                SmartDashboard.putNumber("lastStepWasGrabCheck", 5);
                lastStepWasGrab = false;
            }
        } 

        if (firstCall) {
            SmartDashboard.putNumber("firstCallCheck", 1); // Формируем все пути без доставки только один раз в самом начале
            Boolean treeChange = false;
            for (int i = 0; i < TREE_NUMBER.length; i++) {
                for (int j = 0; j < TREE_ZONE_NAMES.length; j++) {

                    currentTreeName = TREE_NUMBER[i];
                    currentTreeZoneName = TREE_ZONE_NAMES[j];

                    if (firstStep) { 
                        SmartDashboard.putNumber("firstCallCheck", 2);// Переход из зоны старта в зону первого дерева
                        bestCPZone = choosingBestZoneForCheck(currentTreeZoneName, currentTreeName);
                        addIfNotDuplicate("MOVE_FROM_START_TO_" + bestCPZone, logicCommand);
                        addIfNotDuplicate("MOVE_FROM_" + bestCPZone +"_TO_" + currentTreeName + "_" + currentTreeZoneName, logicCommand);
                        firstStep = false;
                    } else {
                        SmartDashboard.putNumber("firstCallCheck", 3);
                        if (treeChange) {
                            SmartDashboard.putNumber("firstCallCheck", 4);
                            addIfNotDuplicate("MOVE_FROM_"+ getLastCheckpoint() +"_TO_" + currentTreeName + "_" + currentTreeZoneName, logicCommand);
                            treeChange = false;
                        } else {
                            SmartDashboard.putNumber("firstCallCheck", 5);
                            addIfNotDuplicate("MOVE_FROM_"+ getLastTreeZone() +"_TO_" + currentTreeName + "_" + currentTreeZoneName, logicCommand);
                        }
                    }

                    SmartDashboard.putNumber("firstCallCheck", 6);
                    addIfNotDuplicate(getGrabModeInArray(currentTreeZoneName), logicCommand);
                    setLastTreeZone(currentTreeName + "_" + currentTreeZoneName);
                }

                if (i != TREE_NUMBER.length - 1) { 
                    SmartDashboard.putNumber("firstCallCheck", 7);// Если это последнее дерево условие не срабатывает
                    addIfNotDuplicate("MOVE_FROM_"+ getLastTreeZone() +"_TO_" + choosingBestZoneForCheck(TREE_ZONE_NAMES[0], TREE_NUMBER[i+1]), logicCommand);
                    setLastCheckpoint(choosingBestZoneForCheck(TREE_ZONE_NAMES[0], TREE_NUMBER[i+1]));
                    treeChange = true;
                }
            }
            SmartDashboard.putNumber("firstCallCheck", 8);
            currentTreeName = TREE_NUMBER[TREE_NUMBER.length - 1];
            currentTreeZoneName = TREE_ZONE_NAMES[TREE_ZONE_NAMES.length - 1];
            addIfNotDuplicate("MOVE_FROM_" + getLastTreeZone() +"_TO_" + choosingBestZoneForCheck(currentTreeZoneName, currentTreeName), logicCommand);
            addIfNotDuplicate("MOVE_FROM_" + choosingBestZoneForCheck(currentTreeZoneName, currentTreeName) + "_TO_FINISH", logicCommand);
            addIfNotDuplicate("END", logicCommand);

            firstCall = false;
            
        }

        outCMD = logicCommand.get(index);

        if (outCMD.equals("AUTO_GRAB_UPPER") || outCMD.equals("AUTO_GRAB_TREE") || outCMD.equals("AUTO_GRAB_UPPER_LZ")) {
            SmartDashboard.putNumber("firstCallCheck", 10);
            lastStepWasGrab = true;
        } else {
            lastCMDWithZone = outCMD;
        }

        System.out.println("!!!!!!!!!!!!!!");
        for (String elem : logicCommand) {
            System.out.println(elem);
        } 
        System.out.println("!!!!!!!!!!!!!!");

        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!! LogicCommand Array !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for(String cmd : logicCommand) {
            System.err.println(cmd);
        }
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!! deliveryCommand Array !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for(String cmd : deliveryCommand) {
            System.err.println(cmd);
        }
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    // Debug
        // if (outCMD.equals("MOVE_FROM_SECOND_TZ_TO_SECOND_LOZ")) {
        //    fruitFind = true;
        //    findFruitName = "SmallRedApple";
        // }
        
        System.err.println("Command: " + outCMD);
        System.err.println("index: " + index);
        System.err.println("count: " + count);
        
        System.err.println("detectedFruit from HashMap: " + Main.stringMap.getOrDefault("detectedFruit", "none"));
        System.err.println("\n");
        return outCMD;
    }

    private String choosingBestZoneForCheck(final String currentZoneName, final String zoneName) {
        String out = "";
        if (zoneName.equals("FIRST")) {
            switch(currentZoneName) {
                case "LZ":
                    out = "CH1";
                    break;
                case "RZ":
                    out = "CH1";
                    break;
                case "LOZ":
                    out = "CH1";
                    break;
                case "TZ":
                    out = "CH1";
                    break;
                case "START":
                    out = "CH1";
                    break;
                default:
                    out = "null";
                    break;
            }
        }
        if (zoneName.equals("SECOND")) {
            switch(currentZoneName) {
                case "LZ":
                    out = "CH2";
                    break;
                case "RZ":
                    out = "CH2";
                    break;
                case "LOZ":
                    out = "CH2";
                    break;
                case "TZ":
                    out = "CH2";
                    break;
                case "START":
                    out = "CH2";
                    break;
                default:
                    out = "null";
                    break;
            }
        }
        if (zoneName.equals("THIRD")) {
            switch(currentZoneName) {
                case "LZ":
                    out = "CH2";
                    break;
                case "RZ":
                    out = "CH3";
                    break;
                case "LOZ":
                    out = "CH3";
                    break;
                case "TZ":
                    out = "CH3";
                    break;
                case "START":
                    out = "CH3";
                    break;
                default:
                    out = "null";
                    break;
            }
        }
        return out;
    }

    private static String getGrabModeInArray(final String zoneName) {
        return grabTypeForZones.getOrDefault(zoneName, "none");
    }

    private static String getConForFruit(final String fruitName) {
        return containersForFruits.getOrDefault(fruitName, "none");
    }

    private static void setLastCheckpoint(String lastCheckpoint) {
        TreeTraverse.lastCheckpoint = lastCheckpoint;
    }

    private static void setLastTreeZone(final String treeZone) {
        TreeTraverse.treeZone = treeZone;
    }

    private static String getLastTreeZone() {
        return TreeTraverse.treeZone;
    }

    private static String getLastCheckpoint() {
        return TreeTraverse.lastCheckpoint;
    }

    private void addIfNotDuplicate(String command, ArrayList arr) {
        if (!arr.contains(command)) {
            arr.add(command);
        }
    }
}