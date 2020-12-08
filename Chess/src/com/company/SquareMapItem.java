package com.company;

public class SquareMapItem {

    private int row;
    private int column;
    private int[] moveToW;
    private int[] moveToB;
    private int[] totalMoves;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
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

    public int[] getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int[] totalMoves) {
        this.totalMoves = totalMoves;
    }
}
