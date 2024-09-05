package frc.robot.Logic;

import java.util.ArrayList;
import java.util.HashMap;

import frc.robot.Constants;
import frc.robot.Main;

public class TreeTraverse {

    private static final String[] TREE_ZONE_NAMES = {"LZ", "TZ", "LOZ", "RZ"};
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
            put("LZ", "AUTO_GRAB_UPPER");
            put("RZ", "AUTO_GRAB_UPPER");
            put("TZ", "AUTO_GRAB_UPPER");
            put("LOZ", "AUTO_GRAB_UPPER");
        }
    };

    // Переменные для хранения геттеров и сеттеров
    private static String lastCheckpoint = "";
    private static String treeZone = "";

    // Массивы для хранения команд
    private static ArrayList<String> logicCommand = new ArrayList<>(); // Храним команды для езды по зонам
    private static ArrayList<String> deliveryCommand = new ArrayList<>(); // Храним команды доставки

    // Другие переменные
    private static int count = 0; // Счетчик для перемещения по списку команд
    private static boolean fristCall = true;
    private static boolean fristStep = true;
    private static boolean fruitFind = false;
    private static String findFruitName = "SmallRedApple";
    private static String lastCMDWithZone = "";
    private static String bestCPZone = "";
    private static boolean lastStepWasGrab = false;

    public String execute(int index) {

        String outCMD = "none";
        String currentTreeName = "";
        String currentTreeZoneName = "";
        int kastilVar = 1;

        findFruitName = Main.stringMap.get("detectedFruit");
        fruitFind = !Main.stringMap.get("detectedFruit").equals("none");

        if (lastStepWasGrab) { // Последняя команда было сканирование поэтому проверяем нашли мы что-нибудь
            deliveryCommand.clear(); // Чистим массив с командами
            if (fruitFind) { // Строим путь для доставки фрукта в контейнер если нашли что-нибудь

                String[] currentZone = lastCMDWithZone.split("_"); // Разделяем строку по символу "_" (последние два элемента)
                currentTreeName = currentZone[currentZone.length - 2]; // Номер дерева
                currentTreeZoneName = currentZone[currentZone.length - 1]; // Название зоны дерева

                if (currentTreeName.equals("FIRST") && currentTreeZoneName.equals("LZ")) kastilVar = 0;  // Ебаный костыль!!! Я больше нечего не успел придумать(

                bestCPZone =  choosingBestZoneForCheck(currentTreeZoneName, currentTreeName);

                deliveryCommand.add("MOVE_FROM_" + currentTreeName + "_" + currentTreeZoneName + "_TO_" + getConForFruit(findFruitName));
                deliveryCommand.add("RESET_FRUIT");
                deliveryCommand.add("MOVE_FROM_"+ getConForFruit(findFruitName) +"_TO_" + bestCPZone);
                deliveryCommand.add("MOVE_FROM_"+ bestCPZone +"_TO_" + currentTreeName + "_" + currentTreeZoneName);
                deliveryCommand.add("AUTO_GRAB_UPPER");

                logicCommand.addAll(count+kastilVar, deliveryCommand);
                lastStepWasGrab = false;

                // Сброс переменных
                fruitFind = false;
                findFruitName = "";

                Main.stringMap.put("detectedFruit", "none");
            }
        }

        if (fristCall) { // Формируем все пути без доставки только один раз в самом начале
            Boolean treeChange = false;
            for (int i = 0; i < TREE_NUMBER.length; i++) {
                for (int j = 0; j < TREE_ZONE_NAMES.length; j++) {

                    currentTreeName = TREE_NUMBER[i];
                    currentTreeZoneName = TREE_ZONE_NAMES[j];

                    if (fristStep) { // Переход из зоны старта в зону первого дерева
                        bestCPZone = choosingBestZoneForCheck(currentTreeZoneName, currentTreeName);
                        logicCommand.add("MOVE_FROM_START_TO_" + bestCPZone);
                        logicCommand.add("MOVE_FROM_" + bestCPZone +"_TO_" + currentTreeName + "_" + currentTreeZoneName);
                        fristStep = false;
                    } else {
                        if (treeChange) {
                            logicCommand.add("MOVE_FROM_"+ getLastCheckpoint() +"_TO_" + currentTreeName + "_" + currentTreeZoneName);
                            treeChange = false;
                        } else {
                            logicCommand.add("MOVE_FROM_"+ getLastTreeZone() +"_TO_" + currentTreeName + "_" + currentTreeZoneName);
                        }
                    }
                    logicCommand.add(getGrabModeInArray(currentTreeZoneName));
                    setLastTreeZone(currentTreeName + "_" + currentTreeZoneName);
                }

                if (i != TREE_NUMBER.length - 1) { // Если это последнее дерево условие не срабатывает
                    logicCommand.add("MOVE_FROM_"+ getLastTreeZone() +"_TO_" + choosingBestZoneForCheck(TREE_ZONE_NAMES[0], TREE_NUMBER[i+1]));
                    setLastCheckpoint(choosingBestZoneForCheck(TREE_ZONE_NAMES[0], TREE_NUMBER[i+1]));
                    treeChange = true;
                }
            }

            currentTreeName = TREE_NUMBER[TREE_NUMBER.length - 1];
            currentTreeZoneName = TREE_ZONE_NAMES[TREE_ZONE_NAMES.length - 1];
            logicCommand.add("MOVE_FROM_"+ getLastTreeZone() +"_TO_" + choosingBestZoneForCheck(currentTreeZoneName, currentTreeName));
            logicCommand.add("MOVE_FROM_"+ choosingBestZoneForCheck(currentTreeZoneName, currentTreeName) +"_TO_FINISH" );
            logicCommand.add("END");

            fristCall = false;
        }

        outCMD = logicCommand.get(index);

        if (outCMD.equals("AUTO_GRAB_UPPER") || outCMD.equals("AUTO_GRAB_TREE")) { // Тут смотрю только один тип захвата
            lastStepWasGrab = true;
        } else {
            lastCMDWithZone = outCMD;
        }

        // Debug
//        if (outCMD.equals("MOVE_FROM_SECOND_TZ_TO_SECOND_LOZ")) {
//            fruitFind = true;
//            findFruitName = "SmallRedApple";
//        }

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

    private String getGrabModeInArray(final String zoneName) {
        return grabTypeForZones.getOrDefault(zoneName, "none");
    }

    private String getConForFruit(final String fruitName) {
        return containersForFruits.getOrDefault(fruitName, "none");
    }

    private void setLastCheckpoint(String lastCheckpoint) {
        TreeTraverse.lastCheckpoint = lastCheckpoint;
    }

    private void setLastTreeZone(final String treeZone) {
        TreeTraverse.treeZone = treeZone;
    }

    private String getLastTreeZone() {
        return TreeTraverse.treeZone;
    }

    private String getLastCheckpoint() {
        return TreeTraverse.lastCheckpoint;
    }
}