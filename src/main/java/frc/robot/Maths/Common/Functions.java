package frc.robot.Maths.Common;

public class Functions {
    public static double TransitionFunction(double value, double[][] values) {
        double result = 0;
        double max_o;
        double max_i;
        double min_o;
        double min_i;
        int minus = 1;
        if (value < 0) {
          minus = -1;
          value = Math.abs(value);
        }
        if (value >= values[0][values[0].length - 1])
          result = values[1][values[1].length - 1];
        else {
          for (int i = 0; i < values[0].length; i++) {
    
            if (value >= values[0][i] && value <= values[0][i + 1]) {
              min_i = values[0][i + 1];
              max_i = values[0][i];
              min_o = values[1][i + 1];
              max_o = values[1][i];
    
              if (value == values[0][i])
                result = values[1][i];
              else {
                result = min_o + (((max_o - min_o) * ((Math.abs(value) - min_i) * 100 / (max_i - min_i))) / 100);
              }
            }
          }
        }
        if (minus < 0) {
          result *= -1;
        }
        return result;
      }

      public static boolean BooleanInRange(double value, double min, double max) {
        return value >= min && value <= max;
      }
    
      public static int InRange(int value, int min, int max) {
        if (value > max) {
          value = max;
        } else if (value < min) {
          value = min;
        }
        return value;
      }

      public static int axis(double x, double y, double curX, double curY) {
        double xDist = 0;
        if (Math.abs(x - curX) != 0) {
          xDist = Math.abs(x - curX);
        }
        double yDist = Math.abs(y - curY);
        if (Functions.BooleanInRange(yDist / xDist, 0.75f, 1.38f) || (xDist < 150 && yDist < 150)) {
        // if (Function.BooleanInRange(yDist / xDist, 0.75f, 2.5f) || (xDist < 150 && yDist < 150)) {
    
          return 0;
        } else {
          if (yDist / xDist > 1) {
            return 1;
          } else
            return 2;
        }
      }

      public static double getLimitedValue(double value, double MIN_LIMIT, double MAX_LIMIT){
        if (value > MAX_LIMIT) {
            value = MAX_LIMIT;
        } else if (value < MIN_LIMIT) {
            value = MIN_LIMIT;
        }
        return value;
    }

      public static double[] ReImToPolar(double x, double y)
    {
        double[] arr = new double[2];
        arr[0] = Math.sqrt((x * x + y * y));  // r
        arr[1] = Math.atan2(y, x);  // theta
        return arr;
    }

    public static double[] PolarToReIm(double r, double theta)
    {
      double[] arr = new double[2];
        arr[0] = r * Math.cos(theta);  // x
        arr[1] = r * Math.sin(theta);  // y
        return arr;
    }
}
