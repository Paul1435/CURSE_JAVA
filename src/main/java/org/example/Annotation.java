package org.example;

public class Annotation {
    private String signature;
    private Figure figure;

    public Annotation(String signature, Figure figure) {
        this.signature = signature;
        this.figure = figure;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    @Override
    public String toString() {
        return figure.toString() + ":" + " " + signature;
    }

}
