package frc.robot.Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogicCore {

    private boolean firstCall = true;
    private boolean firstCallForInit = true;

    // Выходной массив с командами
    public final ArrayList<String> arrayWithLogic = new ArrayList<String>();

    private String lastCheckpoint = "";

    private String lastCurrentZoneArea = "";

    private boolean firstCallForSubPath = false;

    private final boolean C1Flag = false;
    private final boolean C2Flag = false;

    private final boolean autonomousMode = true; // если true, то едет до финиша,
                                                 // если false, то до контейнера и END

    // Зона 1
    private static final String[] firstTree = { "null", "null", "null"};
    private static final String[][] firstTreeZone =
            {
                    //  1  | 2  |                      | 3  |  4
                    { "1", "2", "null", "null", "null", "3", "4" },
                    //   5  |   6   |   7   |   8   |   9   |   10  |   11
                    { "5", "6", "7", "8", "9", "10", "11" },
                    //  12  |   13   |   14   |   15   |   16   |   17  |   18
                    { "12", "AppleBigRipeCON4", "14", "15", "16", "17", "18" },
                    //  19  |   20   |   21   |   22   |   23   |   24  |   25
                    { "19", "20", "21", "22", "23", "24", "25" } };

    // Зона 2
    private static final String[] secondTree = { "null", "null", "null"};
    private static final String[][] secondTreeZone =
            {
                    //  1  | 2  |                      | 3  |  4
                    { "1", "2", "null", "null", "null", "3", "4" },
                    //   5  |   6   |   7   |   8   |   9   |   10  |   11
                    {   "5", "6", "7", "8", "9", "10", "11" },
                    //  12  |   13   |   14   |   15   |   16   |   17  |   18
                    { "12", "13", "14", "15", "16", "AppleSmallRipeCON1", "18" },
                    //  19  |   20   |   21   |   22   |   23   |   24  |   25
                    { "19", "20", "21", "22", "23", "24", "25" } };

    // Зона 3
    private static final String[] thirdTree = { "null", "null", "null"};
    private static final String[][] thirdTreeZone =
            {
                    //  1  | 2  |                      | 3  |  4
                    { "1", "2", "null", "null", "null", "3", "4" },
                    //   5  |   6   |   7   |   8   |   9   |   10  |   11
                    { "5", "6", "7", "8", "9", "10", "AppleSmallRipeCON3" },
                    //  12  |   13   |   14   |   15   |   16   |   17  |   18
                    { "12", "13", "14", "15", "16", "17", "18" },
                    //  19  |   20   |   21   |   22   |   23   |   24  |   25
                    { "19", "PearRipeCON2", "21", "22", "23", "24", "25" } };

    // Шаблон зоны с номерами
    private static final String[][] zoneWithNumber = {{ "1", "2", "null", "null", "null", "3", "4" }, { "5", "6", "7", "8", "9", "10", "11" },
            { "12", "13", "14", "15", "16", "17", "18" }, { "19", "20", "21", "22", "23", "24", "25" } };

    // Назначаем фрукты, которые возим
    private static final String[] allFullFruitsName = { "AppleBigRipeCON4", "PearRipeCON3", "PearRipeCON4", "AppleSmallRipe", "PearRipe", "RottenBigApple","AppleSmallRipeCON3", "RottenSmallApple", "RottenPear", "AppleSmallRipeCON1", "AppleSmallRipeCON2"};

    private static String lastContainer = "";

    // Назначаем контейнеры для определенного типа фруктов
    static HashMap<String, String> containersForFruits = new HashMap<String, String>() {
        {
            put("AppleBigRipeCON4", "CON4");
            put("RottenBigApple", "CON2");
            put("AppleSmallRipeCON3", "CON3");
            put("AppleSmallRipeCON1", "CON1");
            put("AppleSmallRipeCON2", "CON2");
            put("PearRipeCON4", "CON4");
            put("PearRipeCON2", "CON2");
        }
    };

    public String getNextCommand() {
        String command = arrayWithLogic.get(0);
        arrayWithLogic.remove(0);
        return command;
    }

    public void logicInit() {
        if (firstCallForInit) {
            for (int zoneNum = 1; zoneNum <= 3; zoneNum++) {
                arrayWithLogic.addAll(getFruitsForExport(zoneNum));
            }
            if (!arrayWithLogic.isEmpty() && autonomousMode) {
                arrayWithLogic.add("MOVE_FROM_" + getLastContainer() + "_TO_FINISH");
            } else {
                arrayWithLogic.add("END");
            }
            firstCallForInit = false;

        }
        for (String command : arrayWithLogic) {
            System.err.println(command);
        }
    }

    /**
     * Получение текущего массива зон для дерева
     */
    private String[][] getZoneArray(Integer zoneNum) {
        if (zoneNum == 1) {
            return firstTreeZone;
        }
        if (zoneNum == 2) {
            return secondTreeZone;
        }
        if (zoneNum == 3) {
            return thirdTreeZone;
        }
        return new String[0][0];
    }

    /**
     * Основная функция где мы формируем пути ко всем зонам
     */
    private ArrayList<String> getFruitsForExport(Integer zoneNum)
    {
        ArrayList<String> outArray = new ArrayList<String>();
        String[][] currentZone = getZoneArray(zoneNum);
        String[] currentTree = getTreeArray(zoneNum);
        String zoneName = getZoneName(zoneNum);

        if(C1Flag) {
            outArray.add("C1");
        } else {

            outArray.addAll(grabFromLowerZone(currentZone, zoneName));
            outArray.addAll(grabFromLeftZone(currentZone, zoneName, zoneNum));
            outArray.addAll(grabFromRightZone(currentZone, zoneName, zoneNum));
            outArray.addAll(grabFromTreeZone(currentTree, zoneName));
        }
        if(C2Flag) {
            outArray.add("C2");
        }

        return outArray;
    }

    private static String grabPosGenerate(String inGrabPos) {
        Map<String, String> grabPosMap = new HashMap<>();
        // нормальные захваты  (FIRST LZ, LOZ, TZ)
        //                     (SECOND RZ, LOZ, TZ)
        //                     (THIRD RZ, LOZ, TZ, LZ)
        grabPosMap.put("GRAB_POS_1", "ROTATE_-45");
        grabPosMap.put("GRAB_POS_2", "ROTATE_-45");

        grabPosMap.put("GRAB_POS_3", "ROTATE_45");
        grabPosMap.put("GRAB_POS_4", "ROTATE_45");

        grabPosMap.put("GRAB_POS_5", "ROTATE_-15");
        grabPosMap.put("GRAB_POS_6", "ROTATE_-15");
        grabPosMap.put("GRAB_POS_7", "ROTATE_-15");

        grabPosMap.put("GRAB_POS_9", "ROTATE_10");
        grabPosMap.put("GRAB_POS_10", "ROTATE_10");
        grabPosMap.put("GRAB_POS_11", "ROTATE_10");

        grabPosMap.put("GRAB_POS_12", "ROTATE_15");
        grabPosMap.put("GRAB_POS_13", "ROTATE_15");
        grabPosMap.put("GRAB_POS_14", "ROTATE_15");

        grabPosMap.put("GRAB_POS_16", "ROTATE_-15");
        grabPosMap.put("GRAB_POS_17", "ROTATE_-15");
        grabPosMap.put("GRAB_POS_18", "ROTATE_-15");

        grabPosMap.put("GRAB_POS_19", "ROTATE_45");
        grabPosMap.put("GRAB_POS_20", "ROTATE_45");
        grabPosMap.put("GRAB_POS_21", "ROTATE_-45");

        grabPosMap.put("GRAB_POS_22", "ROTATE_0");

        grabPosMap.put("GRAB_POS_23", "ROTATE_-45");
        grabPosMap.put("GRAB_POS_24", "ROTATE_-45");
        grabPosMap.put("GRAB_POS_25", "ROTATE_-45");

        return grabPosMap.getOrDefault(inGrabPos, "none");
    }

    private static String grabPosGenerate2(String inGrabPos) {
        Map<String, String> grabPosMap = new HashMap<>();
        // неформальные захваты   (FIRST RZ)
        //                        (SECOND LZ)
        //                        (THIRD none)
        grabPosMap.put("GRAB_POS_1", "ROTATE_0");
        grabPosMap.put("GRAB_POS_2", "ROTATE_0");

        grabPosMap.put("GRAB_POS_3", "ROTATE_0");
        grabPosMap.put("GRAB_POS_4", "ROTATE_0");
        grabPosMap.put("GRAB_POS_5", "ROTATE_-15");
        grabPosMap.put("GRAB_POS_6", "ROTATE_0");

        grabPosMap.put("GRAB_POS_7", "ROTATE_15");

        grabPosMap.put("GRAB_POS_9", "ROTATE_-15");
        grabPosMap.put("GRAB_POS_10", "ROTATE_0");
        grabPosMap.put("GRAB_POS_11", "ROTATE_10");
        grabPosMap.put("GRAB_POS_12", "ROTATE_-45");
        grabPosMap.put("GRAB_POS_13", "ROTATE_0");

        grabPosMap.put("GRAB_POS_14", "ROTATE_15");

        grabPosMap.put("GRAB_POS_16", "ROTATE_-15");

        grabPosMap.put("GRAB_POS_17", "ROTATE_0");
        grabPosMap.put("GRAB_POS_18", "ROTATE_15");

        grabPosMap.put("GRAB_POS_19", "ROTATE_-45");
        grabPosMap.put("GRAB_POS_20", "ROTATE_0");

        grabPosMap.put("GRAB_POS_21", "ROTATE_45");
        grabPosMap.put("GRAB_POS_22", "ROTATE_0");
        grabPosMap.put("GRAB_POS_23", "ROTATE_-45");

        grabPosMap.put("GRAB_POS_24", "ROTATE_0");
        grabPosMap.put("GRAB_POS_25", "ROTATE_45");

        return grabPosMap.getOrDefault(inGrabPos, "none");
    }

    /**
     * Захват фруктов с дерева
     */
    private ArrayList<String> grabFromTreeZone(String[] currentTree, String zoneName) {
        ArrayList<String> allFindFruits = new ArrayList<String>();
        String currentZoneName = "TZ";

        for (int i = 0; i < currentTree.length; i++) { // Собираем все фрукты из дерева
            String elemInArray = currentTree[i];
            if (weNeedThisFruit(elemInArray)) { // Смотрим фрукт на ветке тот который нам нужен
                allFindFruits.add(getPosForTree(i) + "/" + elemInArray);
            }
        }

        return subPathForDelivery(allFindFruits, currentZoneName, zoneName);
    }

    private String getPosForTree(int numIndex) {
        if (numIndex == 0) {
            return "GRAB_POS_DOWN";
        }
        if (numIndex == 1) {
            return "GRAB_POS_MID";
        }
        if (numIndex == 2) {
            return "GRAB_POS_UP";
        }
        return "null";
    }

    /**
     * Захват фруктов с позиций 1, 2, 5, 6, 7, 12, 13, 14, 19, 20, 21 для указанного дерева
     */
    private ArrayList<String> grabFromLeftZone(String[][] currentZone, String zoneName, int zoneNum) {
        ArrayList<String> allFindFruits = new ArrayList<String>();
        String currentZoneName = "LZ";
        int[][] indexes = { {0, 0}, {0, 1}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}, {3, 0}, {3, 1} };

        // Проход по каждому индексу в массиве и вывод соответствующего значения
        for (int i = 0; i < indexes.length; i++) { // Собираем все фрукты в зоне если они есть
            String elemInArray = currentZone[indexes[i][0]][indexes[i][1]];
            if (weNeedThisFruit(elemInArray)) { // Смотрим фрукт в зоне тот который нам нужен
//                allFindFruits.add(grabPosGenerate("GRAB_POS_" + zoneWithNumber[indexes[i][0]][indexes[i][1]]) + "/" + elemInArray);
                if (zoneNum == 1 || zoneNum == 3) {
                    allFindFruits.add(grabPosGenerate("GRAB_POS_" + zoneWithNumber[indexes[i][0]][indexes[i][1]]) + "/" + elemInArray);
                } else {
                    allFindFruits.add(grabPosGenerate2("GRAB_POS_" + zoneWithNumber[indexes[i][0]][indexes[i][1]]) + "/" + elemInArray);
                }
            }
        }
        return subPathForDelivery(allFindFruits, currentZoneName, zoneName);
    }

    /**
     * Захват фруктов с позиций 3, 4, 9, 10, 11, 16, 17, 18, 23, 24, 25 для указанного дерева
     */
    private ArrayList<String> grabFromRightZone(String[][] currentZone, String zoneName, int zoneNum) {
        ArrayList<String> allFindFruits = new ArrayList<String>();
        String currentZoneName = "RZ";
        int[][] indexes = { {3, 6}, {3, 5}, {2, 6}, {2, 5}, {2, 4},
                            {1, 6}, {1, 5}, {1, 4}, {0, 6}, {0, 5} };

        // Проход по каждому индексу в массиве и вывод соответствующего значения
        for (int i = 0; i < indexes.length; i++) { // Собираем все фрукты в зоне если они есть
            String elemInArray = currentZone[indexes[i][0]][indexes[i][1]];
            if (weNeedThisFruit(elemInArray)) { // Смотрим фрукт в зоне тот который нам нужен
                if(zoneNum == 2 || zoneNum == 3) {
                    allFindFruits.add(grabPosGenerate("GRAB_POS_" + zoneWithNumber[indexes[i][0]][indexes[i][1]]) + "/" + elemInArray);
                } else {
                    allFindFruits.add(grabPosGenerate2("GRAB_POS_" + zoneWithNumber[indexes[i][0]][indexes[i][1]]) + "/" + elemInArray);
                }
            }
        }
        return subPathForDelivery(allFindFruits, currentZoneName, zoneName);
    }

    /**
     * Захват фруктов с позиций 8, 15, 22 для указанного дерева
     */
    private ArrayList<String> grabFromLowerZone(String[][] currentZone, String zoneName) {
        ArrayList<String> allFindFruits = new ArrayList<String>();
        String currentZoneName = "LOZ";
        int[][] indexes = { {3, 2}, {3, 3}, {3, 4} };

        // Проход по каждому индексу в массиве и вывод соответствующего значения
        for (int i = 0; i < indexes.length; i++) { // Собираем все фрукты в зоне если они есть
            String elemInArray = currentZone[indexes[i][0]][indexes[i][1]];
            if (weNeedThisFruit(elemInArray)) { // Смотрим фрукт в зоне тот который нам нужен
                allFindFruits.add(grabPosGenerate("GRAB_POS_"+ zoneWithNumber[indexes[i][0]][indexes[i][1]]) + "/" + elemInArray);
            }
        }
        return subPathForDelivery(allFindFruits, currentZoneName, zoneName); 
    }

    /**
     * Собираем путь из зоны до контейнера
     */
    private ArrayList<String> subPathForDelivery(ArrayList<String> allFindFruits, String currentZoneName, String zoneName) {
        ArrayList<String> outSubPathForDelivery = new ArrayList<String>();
        if (!allFindFruits.isEmpty()) {

            String currentZoneArea = zoneName + "_" + currentZoneName;

            for (int i = 0; i < allFindFruits.size(); i++) {
                String currentGrabPos = allFindFruits.get(i).split("/")[0]; // Получение позиции GRAB_POS
                String currentFruit = allFindFruits.get(i).split("/")[1]; // Получение фрукта который в GRAB_POS
                String bestWayForCheck = choosingBestZoneForCheck(currentZoneName, zoneName);

                if (firstCall) { // Первый запуск перемещение с зоны страта в первую назначенную зону
                    outSubPathForDelivery.add("MOVE_FROM_START_TO_" + choosingBestZoneForCheck("START", zoneName));
                    outSubPathForDelivery.add("MOVE_FROM_" + choosingBestZoneForCheck("START", zoneName) +"_TO_" + currentZoneArea);
                    firstCall = false;
                }

                if (!currentZoneArea.equals(lastCurrentZoneArea)) {
                    if (firstCallForSubPath) {
                        outSubPathForDelivery.add("MOVE_FROM_" + getLastCheckpoint() + "_TO_" + currentZoneArea);
                    }
                }

                outSubPathForDelivery.add(currentGrabPos);
//                outSubPathForDelivery.add("MOV_IN_" + currentZoneArea + "_TO_" + bestWayForCheck);
                outSubPathForDelivery.add("MOVE_FROM_" + currentZoneArea + "_TO_" + containersForFruits.get(currentFruit));
                lastContainer = containersForFruits.get(currentFruit);
                outSubPathForDelivery.add("RESET_FRUIT");

                if (CheckingLastElement(allFindFruits, i) && autonomousMode) { // Смотрим это последний фрукт для этой зоны или нет
                    outSubPathForDelivery.add("MOVE_FROM_" + containersForFruits.get(currentFruit) + "_TO_" + bestWayForCheck);
                    setLastCheckpoint(bestWayForCheck);
                }

            }
            firstCallForSubPath = true;
            lastCurrentZoneArea = currentZoneArea;
        }
        return outSubPathForDelivery;
    }

    /**
     * Выбираем где лучше выровниться для каждого из зон
     */
    private String choosingBestZoneForCheck(final String currentZoneName, final String zoneName) {
        String out = "";
        if (zoneName.equals("FIRST")) {
            switch (currentZoneName) {
            case "LZ":
                out = "CH1";
                break;
            case "LOZ":
                out = "CH1";
                break;
            case "RZ":
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
            switch (currentZoneName) {
            case "LZ":
                out = "CH2";
                break;
            case "LOZ":
                out = "CH2";
                break;
            case "RZ":
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
            switch (currentZoneName) {
            case "LZ":
                out = "CH2";
                break;
            case "LOZ":
                out = "CH2";
                break;
            case "RZ":
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
        return out;
    }

    /**
     * Проверяем является ли элемент последним в массиве
     */
    private boolean CheckingLastElement(ArrayList<String> allFindFruits, int currentIndex) {
        int lastIndex = allFindFruits.size() - 1;
        return currentIndex == lastIndex;
    }

    /**
     * Узнаем есть ли такой фрукт который на пришел через name
     */
    private boolean weNeedThisFruit(String name) {
        for (String elem : allFullFruitsName) {
            if (elem.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получение название зоны по номеру
     */
    private String getZoneName(Integer zoneNum) {
        return zoneNum == 1 ? "FIRST" : zoneNum == 2 ? "SECOND" : zoneNum== 3 ? "THIRD" : "none";
    }

    /**
     * Получение текущего массива элементов на дереве
     */
    private String[] getTreeArray(Integer zoneNum) {
        if (zoneNum == 1) {
            return firstTree;
        }
        if (zoneNum == 2) {
            return secondTree;
        }
        if (zoneNum == 3) {
            return thirdTree;
        }
        return new String[0];
    }

    /**
     * Получение последней точки робота
     */
    private String getLastCheckpoint() {
        return lastCheckpoint;
    }

    /**
     * Установка последней точки робота
     */
    private void setLastCheckpoint(String lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    private String getLastContainer() {
        return lastContainer;
    }
}