package com.github.volzock.fractals;

import com.github.volzock.math.ComplexNumber;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator{
    public static final int MAX_ITERATIONS = 1000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.width = 4;

        range.y = -2.5;
        range.height = 4;
    }

    @Override
    public int numIterations(double x, double y) {
        ComplexNumber z = new ComplexNumber(0, 0);
        ComplexNumber c = new ComplexNumber(x, y);

        for (int i = 0; i <= MAX_ITERATIONS; ++i) {
            z.setY(Math.abs(z.getY()));
            z.setX(Math.abs(z.getX()));
            if (z.compareModulePow2(4) >= 0) {
                return i;
            }
            z.pow2();
            z.addition(c);
        }

        return -1;
    }

    @Override
    public String toString() {
        return "Burning Ship";
    }
}
