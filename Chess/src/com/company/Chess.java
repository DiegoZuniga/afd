package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class Chess {

    private static final int ROWS_NUM = 4;

    // Max moves to mark a play as looser
    private static final int TOTAL_PLAYS_PER_PLAYER = ROWS_NUM * ROWS_NUM;

    private static final int MAX_MOVES_PER_WINNER_PLAY = 6;

    // File path of winner plays
    private static final String WINNER_PLAYS_PATH = "winner-plays.txt";

    // File path of all plays
    private static final String ALL_PLAYS_PATH = "all-plays.txt";

    // Initial state of player 1
    private static final int q0_1 = 1;

    // Initial state of player 2
    private static final int q0_2 = 4;

    // Final (winner) state of player 1
    private static final int finalState1 = 16;

    // Final (winner) state of player 2
    private static final int finalState2 = 13;

    // Map to hold the row, col coordinates and possible moves of each square
    private static Map<Integer, SquareMapItem> squaresMap;

    // Total threads created by backtracking
    private static int totalIterations = 0;

    // Files to print results
    private static BufferedWriter winnerPlaysFileWriter;
    private static BufferedWriter allPlaysFileWriter;

    public static void main(String[] args) throws IOException {
        Chess chess = new Chess();
        squaresMap = new HashMap<>();

        // First we create the chess board of size ROWS_NUM
        Square [][] board = chess.createBoard();

        // Uncomment next line to see the board
        //printMatrix(board);

        // Uncomment next line to see the moves
        //printMoves(board);

        // Print moves and plays in files
        winnerPlaysFileWriter = getFileWriter(WINNER_PLAYS_PATH);
        allPlaysFileWriter = getFileWriter(ALL_PLAYS_PATH);

        printPlaysFile(board);

        winnerPlaysFileWriter.close();
        allPlaysFileWriter.close();

        // Uncomment next line to see total threads created by the backtracking
        System.out.println(totalIterations);
    }

    private static BufferedWriter getFileWriter(String path) {
        BufferedWriter writer = null;
        File file = new File(path);
        if(file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            writer = new BufferedWriter(new FileWriter(path, true));
        } catch (IOException e) {
            System.err.println(e);
        }
        return writer;
    }

    private static void printPlaysFile(Square[][] board) throws IOException {
        printPlaysR(q0_1, finalState1, getNextMoves(q0_1), 0, "" + q0_1);
        printPlaysR(q0_2, finalState2, getNextMoves(q0_2), 0, "" + q0_2);
    }

    private static int[] getNextMoves(int actualSquare) {
        return squaresMap.get(actualSquare).getTotalMoves();
    }

    // ------------------------------------------------------------
    // Backtracking method to get all the possible moves
    // ------------------------------------------------------------
    private static void printPlaysR(int square, int finalState, int[] nextMoves, int movesCounter, String path) throws IOException {
        if(square == finalState) {
            // Print winner plays in file
            String [] pathSplit = path.split(", ");
            if(pathSplit.length <= MAX_MOVES_PER_WINNER_PLAY) {
                path += ", " + square;
                winnerPlaysFileWriter.append(path.substring(2) + "\n");
                allPlaysFileWriter.append(path.substring(2) + "\n");

                // Uncomment next line to print winner plays
                //System.out.println(path.substring(2));
            }
            return;
        } else if(movesCounter < TOTAL_PLAYS_PER_PLAYER) {
            path = path + ", " + square;
            for(int i = 0 ; i < nextMoves.length ; i++) {
                if (nextMoves[i] != 0) {
                    totalIterations++;
                    movesCounter++;
                    printPlaysR(nextMoves[i], finalState, squaresMap.get(nextMoves[i]).getTotalMoves(), movesCounter, path);
                }
            }
        }  else if(movesCounter == TOTAL_PLAYS_PER_PLAYER) {
            // Print looser plays in file
            allPlaysFileWriter.append(path.substring(2) + "\n");
            return;
        }
    }

    // ------------------------------------------------------------
    // Create of board in an int matrix [][]
    // ------------------------------------------------------------
    private Square[][] createBoard() {
        int [][] board = new int[ROWS_NUM][ROWS_NUM];
        Square [][] squareBoard = new Square[ROWS_NUM][ROWS_NUM];
        int totalCounter = 1;
        for(int row = ROWS_NUM - 1 ; row >= 0 ; row--) {
            for(int col = 0 ; col < ROWS_NUM ; col ++) {
                if(col == ROWS_NUM) {
                    col = 0;
                }
                board[row][col] = totalCounter;
                SquareMapItem squareMapItem = new SquareMapItem();
                squareMapItem.setRow(row);
                squareMapItem.setColumn(col);
                squaresMap.put(totalCounter, squareMapItem);
                totalCounter++;
            }
        }

        char color = 'w';
        int rowCopy = 0;
        for(int row = ROWS_NUM - 1 ; row >= 0 ; row--) {
            for(int col = 0 ; col < ROWS_NUM ; col ++) {
                Square square = new Square();
                square.setNumber(board[row][col]);
                square.setColor(color);
                squareBoard[rowCopy][col] = square;
                if(color == 'w') {
                    color = 'b';
                } else {
                    color = 'w';
                }
            }
            if(color == 'w') {
                color = 'b';
            } else {
                color = 'w';
            }
            rowCopy++;
        }

        squareBoard = getSquareMoves(squareBoard);
        return squareBoard;
    }

    // ------------------------------------------------------------
    // Get valid moves for each state
    // ------------------------------------------------------------
    private Square[][] getSquareMoves(Square[][] board) {
        for(int row = 0 ; row < board.length ; row++) {
            for(int col = 0 ; col < board[row].length ; col++) {
                getMoves(row, col, board);
            }
        }
        return board;
    }

    // ------------------------------------------------------------
    // Get valid moves of row/col square
    // ------------------------------------------------------------
    private void getMoves(int row, int col, Square[][] board) {
        int wMoves[] = new int [4];
        int bMoves[] = new int [4];
        int totalMoves[] = new int[8];
        int wMovesCounter = 0;
        int bMovesCounter = 0;
        int totalMovesCounter = 0;
        // Check top boundary
        if(row > 0) {
            // Top left
            if(col > 0) {
                if(board[row-1][col-1].getColor() == 'w') {
                    wMoves[wMovesCounter++] = board[row - 1][col - 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row - 1][col - 1].getNumber();
                } else {
                    bMoves[bMovesCounter++] = board[row - 1][col - 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row - 1][col - 1].getNumber();
                }
            }
            // Top right
            if(col < ROWS_NUM - 1) {
                if(board[row-1][col+1].getColor() == 'w') {
                    wMoves[wMovesCounter++] = board[row - 1][col + 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row - 1][col + 1].getNumber();
                } else {
                    bMoves[bMovesCounter++] = board[row - 1][col + 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row - 1][col + 1].getNumber();
                }
            }
            // Top
            if(board[row-1][col].getColor() == 'w') {
                wMoves[wMovesCounter++] = board[row - 1][col].getNumber();
                totalMoves[totalMovesCounter++] = board[row - 1][col].getNumber();
            } else {
                bMoves[bMovesCounter++] = board[row - 1][col].getNumber();
                totalMoves[totalMovesCounter++] = board[row - 1][col].getNumber();
            }
        }
        // Check bottom boundary
        if(row < ROWS_NUM - 1) {
            // Bottom left
            if(col > 0) {
                if(board[row+1][col-1].getColor() == 'w') {
                    wMoves[wMovesCounter++] = board[row + 1][col - 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row + 1][col - 1].getNumber();
                } else {
                    bMoves[bMovesCounter++] = board[row + 1][col - 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row + 1][col - 1].getNumber();
                }
            }
            // Bottom right
            if(col < ROWS_NUM - 1) {
                if(board[row+1][col+1].getColor() == 'w') {
                    wMoves[wMovesCounter++] = board[row + 1][col + 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row + 1][col + 1].getNumber();
                } else {
                    bMoves[bMovesCounter++] = board[row + 1][col + 1].getNumber();
                    totalMoves[totalMovesCounter++] = board[row + 1][col + 1].getNumber();
                }
            }
            // Bottom
            if(board[row+1][col].getColor() == 'w') {
                wMoves[wMovesCounter++] = board[row + 1][col].getNumber();
                totalMoves[totalMovesCounter++] = board[row + 1][col].getNumber();
            } else {
                bMoves[bMovesCounter++] = board[row + 1][col].getNumber();
                totalMoves[totalMovesCounter++] = board[row + 1][col].getNumber();
            }
        }
        // Check left boundary
        if(col > 0) {
            // Left
            if(board[row][col - 1].getColor() == 'w') {
                wMoves[wMovesCounter++] = board[row][col - 1].getNumber();
                totalMoves[totalMovesCounter++] = board[row][col - 1].getNumber();
            } else {
                bMoves[bMovesCounter++] = board[row][col - 1].getNumber();
                totalMoves[totalMovesCounter++] = board[row][col - 1].getNumber();
            }
        }
        // Check right boundary
        if(col < ROWS_NUM - 1) {
            // Right
            if(board[row][col + 1].getColor() == 'w') {
                wMoves[wMovesCounter++] = board[row][col + 1].getNumber();
                totalMoves[totalMovesCounter++] = board[row][col + 1].getNumber();
            } else {
                bMoves[bMovesCounter++] = board[row][col + 1].getNumber();
                totalMoves[totalMovesCounter++] = board[row][col + 1].getNumber();
            }
        }

        board[row][col].setMoveToW(wMoves);
        board[row][col].setMoveToB(bMoves);
        board[row][col].setTotalMoves(totalMoves);
        squaresMap.get(board[row][col].getNumber()).setMoveToW(wMoves);
        squaresMap.get(board[row][col].getNumber()).setMoveToB(wMoves);
        squaresMap.get(board[row][col].getNumber()).setTotalMoves(totalMoves);
    }

    private static void printMatrix(Square [][] matrix) {

        for(int row = 0 ; row < matrix.length ; row++) {
            for(int col = 0 ; col < matrix[row].length ; col++) {
                System.out.print(matrix[row][col].getNumber() + "-" + matrix[row][col].getColor() + " ");
            }
            System.out.println();
        }
    }

    private static void printMoves(Square [][] matrix) {

        for(int row = 0 ; row < matrix.length ; row++) {
            for(int col = 0 ; col < matrix[row].length ; col++) {
                System.out.print("Moves for: " + matrix[row][col].getNumber());
                System.out.print(" | White: ");
                for(int i = 0 ; i < matrix[row][col].getMoveToW().length ; i++) {
                    if(matrix[row][col].getMoveToW()[i] != 0) {
                        System.out.print(matrix[row][col].getMoveToW()[i] + ", ");
                    }
                }
                System.out.print(" | Black: ");
                for(int i = 0 ; i < matrix[row][col].getMoveToB().length ; i++) {
                    if(matrix[row][col].getMoveToB()[i] != 0) {
                        System.out.print(matrix[row][col].getMoveToB()[i] + ", ");
                    }
                }
                System.out.print(" | Total: ");
                for(int i = 0 ; i < matrix[row][col].getTotalMoves().length ; i++) {
                    if(matrix[row][col].getTotalMoves()[i] != 0) {
                        System.out.print(matrix[row][col].getTotalMoves()[i] + ", ");
                    }
                }
                System.out.println();
            }
        }
    }
}
