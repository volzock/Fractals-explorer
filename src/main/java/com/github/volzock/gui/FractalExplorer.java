package com.github.volzock.gui;

import com.github.volzock.fractals.BurningShip;
import com.github.volzock.fractals.FractalGenerator;
import com.github.volzock.fractals.Mandelbrot;
import com.github.volzock.fractals.Tricorn;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStreamImpl;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

        // Center
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

        // South
        JButton resetButton = new JButton("Reset display");
        resetButton.addActionListener(e -> {
            this.fractalGenerator.getInitialRange(this.rectangle2D);
            this.drawFractal(image);
        });

        JButton saveButton = new JButton("Save image");
        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileFilter fileFilter = new FileNameExtensionFilter("PNG Images", "png");
            chooser.setFileFilter(fileFilter);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    ImageIO.write(image.getImage(), "png", file);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error on saving image", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel southPanel = new JPanel();
        southPanel.add(resetButton);
        southPanel.add(saveButton);
        frame.add(southPanel, BorderLayout.SOUTH);

        // North
        JComboBox<FractalGenerator> comboBox =
                new JComboBox<>(new FractalGenerator[]{new Mandelbrot(), new Tricorn(), new BurningShip()});
        comboBox.addActionListener(e -> {
            this.fractalGenerator = (FractalGenerator) ((JComboBox) e.getSource()).getSelectedItem();
            this.fractalGenerator.getInitialRange(this.rectangle2D);
            this.drawFractal(image);
        });

        JLabel label = new JLabel("Fractal: ");

        JPanel northPanel = new JPanel();
        northPanel.add(label);
        northPanel.add(comboBox);
        frame.add(northPanel, BorderLayout.NORTH);

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
