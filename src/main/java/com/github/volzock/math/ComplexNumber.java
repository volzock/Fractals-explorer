package com.github.volzock.math;

public class ComplexNumber {
    private double x;
    private double y;

    public ComplexNumber(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getModulePow2() {
        return this.x * this.x + this.y * this.y;
    }

    public void addition(ComplexNumber anotherNumber) {
        this.x += anotherNumber.getX();
        this.y += anotherNumber.getY();
    }

    public void pow2() {
        double x = this.x, y = this.y;
        this.x = x * x - y * y;
        this.y = 2 * x * y;
    }

    public int compareModulePow2(double anotherModule) {
        return Double.compare(this.getModulePow2(), anotherModule);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
