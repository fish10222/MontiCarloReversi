package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;
import java.lang.Math;

public class Reversi {

    // Human is 1 on board.
    // CPU is 2 on board.
    private int[][] game_Board = new int[4][4];

    private int human_Score = 0;
    private int CPU_Score = 0;

    public int current_Move = 0;
    private final int board_length = 4;

    public void newGame(int first_Player){
        // Set who has the first move
        current_Move = first_Player;
        for (int[] row: game_Board){
            Arrays.fill(row, 0);
        }
        game_Board[1][1] = 1;
        game_Board[1][2] = 2;
        game_Board[2][1] = 2;
        game_Board[2][2] = 1;
    }

    public int pieceAt(int y, int x){
        return game_Board[y][x];
    }

    public int whoWon(){
        if (human_Score == CPU_Score){
            return 0;
        }
        else if (human_Score > CPU_Score){
            return 1;
        }
        else{
            return 2;
        }
    }

    public int[] scores(){
        int[] score = new int[2];
        score[0] = human_Score;
        score[1] = CPU_Score;
        return score;
    }

    public void forceSkipMove() {
        if (current_Move == 1){
            current_Move = 2;
        }
        else {
            current_Move = 1;
        }
    }

    public int whosTurn(){
        return current_Move;
    }

    public boolean makeMove(int y, int x) {
        // Board location occupied
        if (game_Board[y][x] != 0){
            return false;
        }
        // If the player is currently a human
        // Alternate player move after marking the board
        if (current_Move == 1){
            game_Board[y][x] = 1;
        }
        else{
            game_Board[y][x] = 2;
        }
        updateBoard(y,x);
        updateScore();

        // Flip who's move it is.
        forceSkipMove();
        return true;
    }

    private void updateBoard(int y, int x){
        int target = current_Move;
        // Variable to store distance between newly inserted piece and nearest common piece.
        int distance_R = 0;
        int distance_L = 0;
        boolean R_stop = false;
        boolean L_stop = false;

        // Check in horizontal direction ( - )
        for (int n = 1; n < board_length; n++) {
            // Scan towards left side
            if (x - n >= 0 && !L_stop) {
                // Will trigger at the first same piece found.
                if (game_Board[y][x - n] == target) {
                    distance_L = n - 1;
                    L_stop = true;
                }
                // Space note occupied yet.
                else if (game_Board[y][x - n] == 0) {
                    L_stop = true;
                }
            }
            // Scan towards Right side
            if (x + n < board_length && !R_stop) {
                // Will trigger at the first same piece found.
                if (game_Board[y][x + n] == target) {
                    distance_R = n - 1;
                    R_stop = true;
                }
                // Space note occupied yet.
                else if (game_Board[y][x + n] == 0) {
                    R_stop = true;
                }
                // Terminate early condition.
                if (L_stop && R_stop) {
                    break;
                }
            }
        }
        // Change captured pieces.
        for (int n = 1; n <= distance_L; n++){
            game_Board[y][x-n] = target;
        }
        for (int n = 1; n <= distance_R; n++){
            game_Board[y][x+n] = target;
        }

        // RESET local variables
        distance_R = 0; // R for up
        distance_L = 0; // L for down
        R_stop = false;
        L_stop = false;
        // Check in Vertical direction ( | )
        for (int n = 1; n < board_length; n++){
            // Scan towards left side
            if (y - n >= 0 && !L_stop){
                // Will trigger at the first same piece found.
                if ( y - n >= 0)
                    if (game_Board[y-n][x] == target){
                        distance_L = n-1;
                        L_stop = true;
                    }
                    // Space note occupied yet.
                    else if(game_Board[y-n][x] == 0){
                        L_stop = true;
                    }
                }
            // Scan towards Right side
            if (y + n < board_length && !R_stop){
                // Will trigger at the first same piece found.
                if (game_Board[y+n][x] == target){
                    distance_R = n-1;
                    R_stop = true;
                }
                // Space note occupied yet.
                else if(game_Board[y+n][x] == 0){
                    R_stop = true;
                }
            }
            if (L_stop && R_stop){
                break;
            }
        }
        // Change captured pieces.
        for (int n = 1; n <= distance_L; n++){
            game_Board[y-n][x] = target;
        }
        for (int n = 1; n <= distance_R; n++){
            game_Board[y+n][x] = target;
        }

        // RESET local variables
        distance_R = 0;
        distance_L = 0;
        R_stop = false;
        L_stop = false;

        // Check in / diagonal
        for (int n = 1; n < board_length; n++){
            // Scan towards left side
            if (y + n < board_length && x - n >= 0 && !L_stop){
                // Will trigger at the first same piece found.
                if (game_Board[y+n][x-n] == target){
                    distance_L = n-1;
                    L_stop = true;
                }
                // Space note occupied yet.
                else if(game_Board[y+n][x-n] == 0){
                    L_stop = true;
                }
            }
            // Scan towards Right side
            if (y - n >= 0  && x + n < board_length && !R_stop){
                // Will trigger at the first same piece found.
                if (game_Board[y-n][x+n] == target){
                    distance_R = n-1;
                    R_stop = true;
                }
                // Space note occupied yet.
                else if(game_Board[y-n][x+n] == 0){
                    R_stop = true;
                }
            }
            if (L_stop && R_stop){
                break;
            }
        }
        // Change captured pieces.
        for (int n = 1; n <= distance_L; n++){
            game_Board[y+n][x-n] = target;
        }
        for (int n = 1; n <= distance_R; n++){
            game_Board[y-n][x+n] = target;
        }

        // RESET local variables
        distance_R = 0;
        distance_L = 0;
        R_stop = false;
        L_stop = false;

        // Check in \ diagonal
        for (int n = 1; n < board_length; n++){
            // Scan towards left side
            if (y - n >= 0 && x - n >= 0 && !L_stop){
                // Will trigger at the first same piece found.
                if (game_Board[y-n][x-n] == target){
                    distance_L = n-1;
                    L_stop = true;
                }
                // Space note occupied yet.
                else if(game_Board[y-n][x-n] == 0){
                    L_stop = true;
                }
            }
            // Scan towards Right side
            if (y + n < board_length  && x + n < board_length && !R_stop){
                // Will trigger at the first same piece found.
                if (game_Board[y+n][x+n] == target){
                    distance_R = n-1;
                    R_stop = true;
                }
                // Space note occupied yet.
                else if(game_Board[y+n][x+n] == 0){
                    R_stop = true;
                }
            }
            if (L_stop && R_stop){
                break;
            }
        }
        // Change captured pieces.
        for (int n = 1; n <= distance_L; n++){
            game_Board[y-n][x-n] = target;
        }
        for (int n = 1; n <= distance_R; n++){
            game_Board[y+n][x+n] = target;
        }
    }

    public ArrayList<Point> validMoves(){
        int target = 0;
        // Human Move
        if (current_Move == 1) {
            target = 2;
        }
        else {
            target = 1;
        }
        // List of moves are in the form of their coordinates.
        // Ex. [1,4], [3,4]
        ArrayList<Point> moves = new ArrayList<Point>();

        for (int y = 0; y < board_length; y++) {
            for (int x = 0; x < board_length; x++) {
                if (game_Board[y][x] == target) {
                    // Check piece to right
                    // If empty, check to see if the left if there is a series of target pieces terminating with one of current player
                    if ( x + 1 < board_length) {
                        if (game_Board[y][x + 1] == 0) {
                            for (int n = x - 1; n >= 0; n--) {
                                if (game_Board[y][n] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    continue;
                                } else if (game_Board[y][n] == current_Move) {
                                    moves.add(new Point(x + 1, y));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    if (x - 1 >= 0 ) {
                        // Check piece to Left
                        if (game_Board[y][x - 1] == 0) {
                            for (int n = x + 1; n < board_length; n++) {
                                if (game_Board[y][n] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    continue;
                                } else if (game_Board[y][n] == current_Move) {
                                    moves.add(new Point(x - 1, y));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    // Check piece Above.
                    if (y - 1 >= 0) {
                        if (game_Board[y - 1][x] == 0) {
                            for (int n = y + 1; n < board_length; n++) {
                                if (game_Board[n][x] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    continue;
                                } else if (game_Board[n][x] == current_Move) {
                                    moves.add(new Point(x, y - 1));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    // Check piece Below.
                    if (y + 1 < board_length) {
                        if (game_Board[y + 1][x] == 0) {
                            for (int n = y - 1; n >= 0; n--) {
                                if (game_Board[n][x] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    continue;
                                } else if (game_Board[n][x] == current_Move) {
                                    moves.add(new Point(x, y + 1));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    // Check top right.
                    if (y - 1 >= 0 && x + 1 < board_length) {
                        if (game_Board[y - 1][x + 1] == 0) {
                            int n = y + 1;
                            int m = x - 1;
                            while (n < board_length && m >= 0) {
                                if (game_Board[n][m] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    n++;
                                    m--;
                                    continue;
                                } else if (game_Board[n][m] == current_Move) {
                                    moves.add(new Point(x + 1, y - 1));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    // Check top Left.
                    if (y - 1 >= 0 && x - 1 >= 0 ) {
                        if (game_Board[y - 1][x - 1] == 0) {
                            int n = y + 1;
                            int m = x + 1;
                            while (n < board_length && m < board_length) {
                                if (game_Board[n][m] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    n++;
                                    m++;
                                    continue;
                                } else if (game_Board[n][m] == current_Move) {
                                    moves.add(new Point(x - 1, y - 1));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    // Check bottom Right
                    if (y + 1 < board_length && x + 1 < board_length) {
                        if (game_Board[y + 1][x + 1] == 0) {
                            int n = y - 1;
                            int m = x - 1;
                            while (n >= 0 && m >= 0) {
                                if (game_Board[n][m] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    n--;
                                    m--;
                                    continue;
                                } else if (game_Board[n][m] == current_Move) {
                                    moves.add(new Point(x + 1, y + 1));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    // Check Bottom Left.
                    if (y + 1 < board_length && x - 1 >= 0) {
                        if (game_Board[y + 1][x - 1] == 0) {
                            int n = y - 1;
                            int m = x + 1;
                            while (n >= 0 && m < board_length) {
                                if (game_Board[n][m] == target) {
                                    // If next piece to left is target, continue moving to the left
                                    n--;
                                    m++;
                                    continue;
                                } else if (game_Board[n][m] == current_Move) {
                                    moves.add(new Point(x - 1, y + 1));
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    private void updateScore(){
        int human = 0;
        int CPU = 0;
        for (int y = 0; y < board_length; y++){
            for (int x = 0; x < board_length; x++) {
                if (game_Board[y][x] == 1){
                    human++;
                }
                else if (game_Board[y][x] == 2){
                    CPU++;
                }
            }
        }
        human_Score = human;
        CPU_Score = CPU;
    }
}


