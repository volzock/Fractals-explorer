package com.github.volzock.gui;

import com.github.volzock.fractals.FractalGenerator;
import com.github.volzock.fractals.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int height;
    private int width;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double rectangle2D;

    public FractalExplorer(int x, int y) {
        this.height = y;
        this.width = x;
        this.rectangle2D = new Rectangle2D.Double();
        this.fractalGenerator = new Mandelbrot();
        this.fractalGenerator.getInitialRange(this.rectangle2D);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JImageDisplay image = new JImageDisplay(this.width, this.height);
        frame.add(image, BorderLayout.CENTER);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX(), y = e.getY();
                double _x = FractalGenerator.getCoord(rectangle2D.x,
                        rectangle2D.x + rectangle2D.width, height, x);
                double _y = FractalGenerator.getCoord(rectangle2D.y,
                        rectangle2D.y + rectangle2D.height, width,y);

                fractalGenerator.recenterAndZoomRange(rectangle2D, _x, _y, 0.5);
                drawFractal(image);
            }
        });

        JButton button = new JButton("I'm button. Tap on me");
        frame.add(button, BorderLayout.SOUTH);
        button.addActionListener(e -> {
            this.fractalGenerator.getInitialRange(this.rectangle2D);
            this.drawFractal(image);
        });

        drawFractal(image);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal(JImageDisplay image) {
        for (int x = 0; x < this.height; ++x) {
            for (int y = 0; y < this.width; ++y) {
                double _x = FractalGenerator.getCoord(this.rectangle2D.x,
                        this.rectangle2D.x + this.rectangle2D.width, this.width,x);
                double _y = FractalGenerator.getCoord(this.rectangle2D.y,
                        this.rectangle2D.y + this.rectangle2D.height, this.height,y);

                int iterationQuantity = fractalGenerator.numIterations(_x, _y);
                if (iterationQuantity == -1) {
                    image.setRGB(x, y, 0);
                } else {
                    image.setRGB(x, y, Color.HSBtoRGB(0.7f + (float) iterationQuantity / 200f, 1f, 1f));
                }
            }
        }
        image.repaint();
    }
}
