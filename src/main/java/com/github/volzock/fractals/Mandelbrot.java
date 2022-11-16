package com.github.volzock.fractals;

import com.github.volzock.math.ComplexNumber;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    public static final int MAX_ITERATIONS = 300;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    @Override
    public int numIterations(double x, double y) {
        ComplexNumber z = new ComplexNumber(0, 0);
        ComplexNumber c = new ComplexNumber(x, y);

        for (int i = 0; i <= MAX_ITERATIONS; ++i) {
            if (z.compareModulePow2(4) >= 0) {
                return i;
            }
            z.pow2();
            z.addition(c);
        }

        return -1;
    }
}
