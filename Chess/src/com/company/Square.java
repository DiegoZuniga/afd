package com.company;

public class Square {

    private int number;
    private char color;
    private boolean occupied;
    private int[] moveToW;
    private int[] moveToB;
    private int[] totalMoves;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int[] getMoveToW() {
        return moveToW;
    }

    public void setMoveToW(int[] moveToW) {
        this.moveToW = moveToW;
    }

    public int[] getMoveToB() {
        return moveToB;
    }

    public void setMoveToB(int[] moveToB) {
        this.moveToB = moveToB;
    }

    @Override
    public String toString() {
        return "Square{" +
                "number=" + number +
                ", color=" + color +
                ", occupied=" + occupied +
                '}';
    }

    public int[] getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int[] totalMoves) {
        this.totalMoves = totalMoves;
    }
}
