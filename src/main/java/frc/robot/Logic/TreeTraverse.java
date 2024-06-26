package frc.robot.Logic;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class TreeTraverse {

    // Данные и конфигурации
    private static final String[] TREE_ZONE_NAMES = {"RZ", "TZ", "LZ"};
    private static final String[] TREE_NAMES = {"FRIST", "SECOND", "THIRD"};

    private static final HashMap<String, String> containersForFruits = new HashMap<String, String>() {
        {
            put("rotten", "CON2");
            put("AppleSmallRipe", "CON4");
            put("AppleBigRipe", "CON1");
            put("PeerRipe", "CON3");
        }
    };

    private static final HashMap<String, String> grabTypeForZones = new HashMap<String, String>() {
        {
            put("LZ", "AUTO_GRAB_UPPER");
            put("RZ", "AUTO_GRAB_UPPER");
            put("LOZ", "AUTO_GRAB_UPPER");
            put("UZL", "AUTO_GRAB_UPPER");
            put("UZR", "AUTO_GRAB_UPPER");
            put("TZ", "AUTO_GRAB_UPPER");
        }
    };

    // Вот эти переменные необходимо изменить при нахождении фрукта AutoGrab'ом
    private String findFruitName = "";
    private boolean fruitFind = false; // Для отладки установлено в true

    private boolean firstCall = true;
    private boolean lastAutoGrab, lastStepChange, firstItter, treeNumberChange = true;
    private int currentTreeNumber, currentTreeZoneNumber, currentTreeZoneSteps, deliverySteps, endSteps, lastCurrentTreeNumber, lastCurrentTreeZoneNumber = 0;
    private String lastCheckpoint = "";
    private String treeZone = "";

    public String execute() {
        String outIndex = "none";

        // findFruitName = train.detectionResult;
        // fruitFind = train.fruitFind;

        SmartDashboard.putBoolean("FruitHui: ", fruitFind); 
        SmartDashboard.putString("FruitHuiName: ", findFruitName); 

        if (firstCall) {
            outIndex = "MOV_IN_START_TO_CH1";
            setLastCheckpoint("CH1");
            firstItter = true;
            firstCall = false;
        } else {
            if (currentTreeNumber < TREE_NAMES.length) {
                if (currentTreeZoneNumber < TREE_ZONE_NAMES.length) {
                    if (currentTreeZoneSteps < 2) {

                        outIndex = processCurrentTreeZone(currentTreeNumber, currentTreeZoneNumber, treeNumberChange);
                        currentTreeZoneSteps++;

                        if (currentTreeZoneSteps + 1 == 3) {
                            currentTreeZoneSteps = 0;
                            currentTreeZoneNumber++;
                        }
                    }

                    if (currentTreeZoneNumber + 1 == TREE_ZONE_NAMES.length + 1) {
                        currentTreeZoneNumber = 0;
                        currentTreeNumber++;
                    }
                }
            } else {
                if (endSteps < 2) {
                    outIndex = processEnd(endSteps);
                    endSteps++;
                } 
            }
        }

        treeNumberChange = checkChangeTreeNumber();
        lastCurrentTreeNumber = currentTreeNumber;

        return outIndex;
    }


    private String processEnd(Integer step) {
        String outIndex = "none";
        if (step == 0) {
            outIndex = "MOV_IN_" + getLastTreeZone() + "_TO_" + getBestWayForCheckForZone(getLastTreeZone());
            setLastCheckpoint(getBestWayForCheckForZone(getLastTreeZone()));
        }

        if (step == 1) {
            outIndex = "MOV_IN_" + getLastCheckpoint() + "_TO_" + "FINISH";
        }
        return outIndex;
    }

    private String processCurrentTreeZone(Integer currentTreeNumber, Integer currentTreeZoneNumber, Boolean ChangeTreeNumber) {
        String outIndex = "none";

            if (firstItter) {

                outIndex = "MOV_IN_" + getLastCheckpoint() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
                setLastTreeZone(TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber]);
                this.currentTreeZoneSteps = 0;
                firstItter = false;

            } else {
                if (fruitFind && lastAutoGrab) {

                    if (deliverySteps < 5) {

                        if (deliverySteps == 0) {
                            outIndex = "MOV_IN_" + getLastTreeZone() + "_TO_" + getConForFruit(findFruitName);
                            setLastCheckpoint(getBestWayForCheckForZone(getLastTreeZone()));

                            this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
                            this.currentTreeZoneSteps = -1;
                        }

                        if (deliverySteps == 1) {
                            outIndex = "RESET_FRUIT";
                            // train.fruitFind = false;
                            // train.detectionResult = "";
                            this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
                            this.currentTreeZoneSteps = -1;
                        }

                        if (deliverySteps == 2) {
                            outIndex = "MOV_IN_" + getConForFruit(findFruitName) + "_TO_" + getLastCheckpoint();

                            this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
                            this.currentTreeZoneSteps = -1;
                        }

                        deliverySteps++;

                        if (deliverySteps == 3) {

                            // train.fruitFind = false;
                            // train.detectionResult = "";
                            lastAutoGrab = false;
                        }
                    }

                } else {
                    if (ChangeTreeNumber) {
                        outIndex = "MOV_IN_" + getLastTreeZone() + "_TO_" + getBestWayForCheckForZone(getLastTreeZone());
                        lastStepChange = true;
                        this.currentTreeZoneSteps = -1;
                    } else {
                        outIndex = createPathForZone(currentTreeNumber, currentTreeZoneNumber);
                    }
                }
            }
        return outIndex;
    }

    private String createPathForZone(Integer currentTreeNumber, Integer currentTreeZoneNumber) {
        String outIndex = "none";

        if (currentTreeZoneSteps == 0) {
            if (lastStepChange) {
                outIndex = "MOV_IN_" + getLastCheckpoint() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
                lastStepChange = false;
            } else {
                if (!lastAutoGrab) {
                    SmartDashboard.putString("lastCheckpoint", getLastCheckpoint());
                    outIndex = "MOV_IN_" + getLastCheckpoint() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
                    setLastTreeZone(TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber]);
                    lastAutoGrab = false;
                } else {
                    outIndex = "MOV_IN_" + getLastTreeZone() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
                    setLastTreeZone(TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber]);
                    lastAutoGrab = false;
                }
            }
        }

        if (currentTreeZoneSteps == 1) {
            outIndex = getGrabModeInArray(TREE_ZONE_NAMES[currentTreeZoneNumber]);
            setLastTreeZone(TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber]);
            lastCurrentTreeZoneNumber = this.currentTreeZoneNumber;
            deliverySteps = 0;
            lastAutoGrab = true;
        }
        return outIndex;
    }

    /**
     * Узнаем в какой контейнер нужно вести найденный фрукт
     */
    private String getConForFruit(String fruitName) {
        return containersForFruits.getOrDefault(fruitName, "none");
    }

    private String getBestWayForCheckForZone(String getLastTreeZone) {
        String[] replaceZone = getLastTreeZone.split("_");
        return choosingBestZoneForCheck(replaceZone[1], replaceZone[0]);
    }

    private boolean checkChangeTreeNumber() {
        return currentTreeNumber != lastCurrentTreeNumber;
    }

    /**
     * Узнаем какой тип распознавания нужно использовать для заданной зоны
     */
    private String getGrabModeInArray(String zoneName) {
        return grabTypeForZones.getOrDefault(zoneName, "none");
    }

    /**
     * Установка последней Checkpoint
     */
    private void setLastCheckpoint(String lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    /**
     * Установка последней зоны в который мы были у дерева
     */
    private void setLastTreeZone(String treeZone) {
        this.treeZone = treeZone;
    }

    /**
     * Получение последней зоны в который мы были у дерева
     */
    private String getLastTreeZone() {
        return this.treeZone;
    }

    /**
     * Получение последней точки робота
     */
    private String getLastCheckpoint() {
        return lastCheckpoint;
    }

    private String choosingBestZoneForCheck(String currentZoneName, String zoneName) {
        String out = "";
        if (zoneName.equals("FRIST")) {
            switch (currentZoneName) {
                case "LZ":
                    out = "CH1";
                    break;
                case "RZ":
                    out = "CH1";
                    break;
                case "LOZ":
                    out = "CH1";
                    break;
                case "UZL":
                    out = "CH1";
                    break;
                case "UZR":
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
                    out = "CH1";
                    break;
                case "RZ":
                    out = "CH1";
                    break;
                case "LOZ":
                    out = "CH1";
                    break;
                case "UZL":
                    out = "CH1";
                    break;
                case "UZR":
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
        if (zoneName.equals("THIRD")) {
            switch (currentZoneName) {
                case "LZ":
                    out = "CH1";
                    break;
                case "RZ":
                    out = "CH1";
                    break;
                case "LOZ":
                    out = "CH1";
                    break;
                case "UZL":
                    out = "CH1";
                    break;
                case "UZR":
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
        return out;
    }
}