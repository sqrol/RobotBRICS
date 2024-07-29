package frc.robot.Logic;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;

public class TreeTraverse {

    // Данные и конфигурации
    private static final String[] TREE_ZONE_NAMES = {"LZ", "TZ", "RZ"};
    private static final String[] TREE_NAMES = {"FIRST", "SECOND", "THIRD"};

    private static final HashMap<String, String> containersForFruits = new HashMap<String, String>() {
        {
            put("rotten", "CON2");
            put("AppleSmallRed", "CON4");
            put("AppleBigRed", "CON1");
            put("PearYellow", "CON3");
        }
    };

    private static final HashMap<String, String> grabTypeForZones = new HashMap<String, String>() {
        {
            put("LZ", "AUTO_GRAB_UPPER");
            put("RZ", "AUTO_GRAB_UPPER");
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

        findFruitName = getGrippedFruit();

        SmartDashboard.putBoolean("FruitHui: ", fruitFind); 
        SmartDashboard.putString("FruitHuiName: ", findFruitName); 
        
        if (firstCall) {
            outIndex = "MOVE_FROM_START_TO_CH1";
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
            outIndex = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + getBestWayForCheckForZone(getLastTreeZone());
            setLastCheckpoint(getBestWayForCheckForZone(getLastTreeZone()));
        }

        if (step == 1) {
            outIndex = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + "FINISH";
        }
        return outIndex;
    }

    // private String processCurrentTreeZone(Integer currentTreeNumber, Integer currentTreeZoneNumber, Boolean ChangeTreeNumber) {
    //     String outIndex = "none";

    //         if (firstItter) {

    //             outIndex = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
    //             setLastTreeZone(TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber]);
    //             this.currentTreeZoneSteps = 0;
    //             firstItter = false;

    //         } else {
    //             if (fruitFind && lastAutoGrab) {

    //                 if (deliverySteps < 4) {
    //                     SmartDashboard.putNumber("deliverySteps", deliverySteps);
    //                     if (deliverySteps == 0) {
    //                         outIndex = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + getConForFruit(findFruitName);
    //                         setLastCheckpoint(getBestWayForCheckForZone(getLastTreeZone()));

    //                         this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
    //                         this.currentTreeZoneSteps = -1;
    //                     }
    //                     SmartDashboard.putNumber("deliverySteps", deliverySteps);
    //                     if (deliverySteps == 1) {
    //                         SmartDashboard.putNumber("this.currentTreeZoneNumber", this.lastCurrentTreeZoneNumber);
    //                         SmartDashboard.putNumber("lastCurrentTreeZoneNumber", lastCurrentTreeZoneNumber);
    //                         outIndex = "RESET_FRUIT";
    //                         // fruitFind = false;
    //                         // findFruitName = "";
    //                         this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
    //                         this.currentTreeZoneSteps = -1;
    //                     }
    //                     SmartDashboard.putNumber("deliverySteps", deliverySteps);
    //                     if (deliverySteps == 2) {
    //                         outIndex = "MOVE_FROM_" + getConForFruit(findFruitName) + "_TO_" + getLastCheckpoint();

    //                         this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
    //                         this.currentTreeZoneSteps = -1;
    //                     }
    //                     SmartDashboard.putNumber("deliverySteps", deliverySteps);
    //                     deliverySteps++;
    //                     SmartDashboard.putNumber("deliverySteps", deliverySteps);
    //                     if (deliverySteps == 3) {

    //                         fruitFind = false;
    //                         findFruitName = "";
    //                         lastAutoGrab = false;
    //                     }
    //                     SmartDashboard.putNumber("deliverySteps", deliverySteps);
    //                 }

    //             } else {
    //                 if (ChangeTreeNumber) {
    //                     outIndex = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + getBestWayForCheckForZone(getLastTreeZone());
    //                     lastStepChange = true;
    //                     this.currentTreeZoneSteps = -1;
    //                 } else {
    //                     outIndex = createPathForZone(currentTreeNumber, currentTreeZoneNumber);
    //                 }
    //             }
    //         }
    //     return outIndex;
    // }

    private String processCurrentTreeZone(Integer currentTreeNumber, Integer currentTreeZoneNumber, Boolean ChangeTreeNumber) {
        String outIndex = "none";
    
        if (firstItter) {
            outIndex = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
            setLastTreeZone(TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber]);
            this.currentTreeZoneSteps = 0;
            firstItter = false;
    
        } else {
            if (fruitFind && lastAutoGrab) {
                if (deliverySteps < 4) {
                    SmartDashboard.putNumber("deliverySteps11", deliverySteps);
    
                    switch (deliverySteps) {
                        case 0:
                            outIndex = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + getConForFruit(findFruitName);
                            setLastCheckpoint(getBestWayForCheckForZone(getLastTreeZone()));
                            this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
                            this.currentTreeZoneSteps = -1;
                            SmartDashboard.putNumber("deliverySteps", 0);
                            break;
    
                        case 1:
                            
                            outIndex = "RESET_FRUIT";
                            this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
                            this.currentTreeZoneSteps = -1;
                            SmartDashboard.putNumber("deliverySteps", 1);
                            break;
    
                        case 2:
                            outIndex = "MOVE_FROM_" + getConForFruit(findFruitName) + "_TO_" + getLastCheckpoint();
                            this.currentTreeZoneNumber = lastCurrentTreeZoneNumber;
                            this.currentTreeZoneSteps = -1;
                            SmartDashboard.putNumber("deliverySteps", 2);
                            break;
    
                        case 3:
                            fruitFind = false;
                            findFruitName = "";
                            lastAutoGrab = false;
                            SmartDashboard.putNumber("deliverySteps", 3);
                            break;
                    }
    
                    deliverySteps++;
                    SmartDashboard.putNumber("deliverySteps", deliverySteps);
                    System.out.println("Incremented deliverySteps" + deliverySteps);
                }
            } else {
                if (ChangeTreeNumber) {
                    outIndex = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + getBestWayForCheckForZone(getLastTreeZone());
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
                outIndex = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
                lastStepChange = false;
            } else {
                if (!lastAutoGrab) {
                    SmartDashboard.putString("lastCheckpoint", getLastCheckpoint());
                    outIndex = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
                    setLastTreeZone(TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber]);
                    lastAutoGrab = false;
                } else {
                    outIndex = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + TREE_NAMES[currentTreeNumber] + "_" + TREE_ZONE_NAMES[currentTreeZoneNumber];
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
        if (zoneName.equals("FIRST")) {
            switch (currentZoneName) {
                case "LZ":
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
                case "RZ":
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

    private String getGrippedFruit() {
        String detectionResult = "";
        switch(Main.camMap.get("grippedFruit").intValue()) {
            case 1: 
                detectionResult = "AppleBigRed";
                fruitFind = true;
                break;
            case 2: 
                detectionResult = "AppleSmallRed";
                fruitFind = true;
                break;
            case 3:
                detectionResult = "PearYellow";
                fruitFind = true;
                break;
        }
        return detectionResult;
    }
}