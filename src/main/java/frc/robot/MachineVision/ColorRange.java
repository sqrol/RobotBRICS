package frc.robot.MachineVision;

import org.opencv.core.*;

public class ColorRange {
    private Point hue;
    private Point saturation;
    private Point value;

    public ColorRange(Point hue, Point saturation, Point value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    public ColorRange() {
        this.hue = new Point(0,0);
        this.saturation = new Point(0,0);
        this.value = new Point(0,0);
    }

    public Point getHue() {
        return hue;
    }

    public Point getSaturation() {
        return saturation;
    }

    public Point getValue() {
        return value;
    }
}
