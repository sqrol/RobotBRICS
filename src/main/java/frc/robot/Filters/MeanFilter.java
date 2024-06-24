package frc.robot.Filters;

import java.util.Arrays;

public class MeanFilter 
{
    private double[] arrayForFilter;
    private int filterPowerInit = 10;

    public MeanFilter()
    {
        this.arrayForFilter = new double[this.filterPowerInit];
    }

    public MeanFilter(int filterPower)
    {
        this.filterPowerInit = filterPower;
        this.arrayForFilter = new double[this.filterPowerInit];
    }

    public double Filter(double val)
    {
        for (int i = filterPowerInit - 1; i > 0; i--)
        {
            this.arrayForFilter[i] = this.arrayForFilter[i - 1];
        }
        this.arrayForFilter[0] = val;

        double sum = 0;
        for (int i = 0; i < this.arrayForFilter.length; ++i)
        {
            sum += this.arrayForFilter[i];
        }
        return sum / this.arrayForFilter.length;
    }

    public void ChangePower(int pow)
    {
        if (pow > filterPowerInit)
        {
            double[] newArr = concat(arrayForFilter, new double[pow - arrayForFilter.length]);
            arrayForFilter = newArr;
            filterPowerInit = pow;
        }
        else
        {
            double[] newArr = new double[pow];
            for (int i = 0; i < pow; ++i)
            {
                newArr[i] = arrayForFilter[i];
            }
            arrayForFilter = newArr;
            filterPowerInit = pow;
        }
    }

    private double[] concat(double[] first, double[] second) {
        double[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
      }
}