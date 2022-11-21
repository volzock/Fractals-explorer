package com.github.volzock.gui;

import com.github.volzock.fractals.BurningShip;
import com.github.volzock.fractals.FractalGenerator;
import com.github.volzock.fractals.Mandelbrot;
import com.github.volzock.fractals.Tricorn;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FractalExplorer {
    private final int height;
    private final int width;
    private FractalGenerator fractalGenerator;
    private final Rectangle2D.Double rectangle2D;

    private JImageDisplay image;
    private JButton resetButton;
    private JButton saveButton;
    private JComboBox<FractalGenerator> comboBox;

    private int rowsRemaing;

    private class FractalWorker extends SwingWorker<Object, Object> {
        final private int yCoord;
        private ArrayList<Integer> xCoords;

        public FractalWorker(int y) {
            this.yCoord = y;
        }

        @Override
        protected Object doInBackground() throws Exception {
            xCoords = new ArrayList<>();

            for (int x = 0; x < width; ++x) {
                double _x = FractalGenerator.getCoord(rectangle2D.x,
                        rectangle2D.x + rectangle2D.width, width,x);
                double _y = FractalGenerator.getCoord(rectangle2D.y,
                        rectangle2D.y + rectangle2D.height, height,this.yCoord);

                int iterationQuantity = fractalGenerator.numIterations(_x, _y);
                if (iterationQuantity == -1) {
                    xCoords.add(0);
                } else {
                    xCoords.add(Color.HSBtoRGB(0.7f + (float) iterationQuantity / 200f, 1f, 1f));
                }
            }

            return null;
        }

        @Override
        protected void done() {
            for (int x = 0; x < xCoords.size() - 1; ++x) {
                image.setRGB(x, yCoord, xCoords.get(x));
            }
            image.repaint(0, yCoord, width, 1);
            if (--rowsRemaing == 0) {
                enableUI(true);
            }
        }
    }

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
        image = new JImageDisplay(this.width, this.height);
        frame.add(image, BorderLayout.CENTER);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (rowsRemaing == 0) {
                    int x = e.getX(), y = e.getY();
                    double _x = FractalGenerator.getCoord(rectangle2D.x,
                            rectangle2D.x + rectangle2D.width, height, x);
                    double _y = FractalGenerator.getCoord(rectangle2D.y,
                            rectangle2D.y + rectangle2D.height, width,y);

                    fractalGenerator.recenterAndZoomRange(rectangle2D, _x, _y, 0.5);
                    drawFractal(image);
                }
            }
        });

        // South
        resetButton = new JButton("Reset display");
        resetButton.addActionListener(e -> {
            this.fractalGenerator.getInitialRange(this.rectangle2D);
            this.drawFractal(image);
        });

        saveButton = new JButton("Save image");
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
        comboBox =
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
        enableUI(false);
        rowsRemaing = this.height;
        for (int y = 0; y < this.height; ++y) {
            FractalWorker fractalWorker = new FractalWorker(y);
            try {
                fractalWorker.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

     private void enableUI(boolean val) {
//        this.image.setEnabled(val);
        this.comboBox.setEnabled(val);
        this.resetButton.setEnabled(val);
        this.saveButton.setEnabled(val);
    }
}
