package com.company;

import java.util.Arrays;

public class Reversi {

    // Human is 1 on board.
    // CPU is 2 on board.
    private int[][] game_Board;

    private int human_Score = 0;
    private int CPU_Score = 0;

    public boolean player_Move = true;
    private final int board_length = 8;

    public void newGame(){
        for (int[] row: game_Board){
            Arrays.fill(row, 0);
        }
        game_Board[3][3] = 1;
        game_Board[3][4] = 2;
        game_Board[4][3] = 1;
        game_Board[4][4] = 2;
    }

    public boolean makeMove(int x, int y) {
        // Board location occupied
        if (game_Board[x][y] != 0){
            return false;
        }
        // If the player is currently a human
        // Alternate player move after marking the board
        if (player_Move == true){
            game_Board[x][y] = 1;
        }
        else{
            game_Board[x][y] = 2;
        }
        updateBoard(x,y);
        updateScore();
        return true;
    }

    private void updateBoard(int x, int y){
        int target = 1;
        if (player_Move == false){
            target = 2;
        }
        // Variable to store distance between newly inserted piece and nearest common piece.
        int distance_R = 0;
        int distance_L = 0;
        boolean R_stop = false;
        boolean L_stop = false;

        // Check in horizontal direction ( - )
        for (int n = 1; n < board_length; n++){
            // Scan towards left side
            if (x - n >= 0 && !L_stop){
                // Will trigger at the first same piece found.
                if (game_Board[y][x-n] == target){
                    distance_L = n-1;
                    L_stop = true;
                }
                // Space note occupied yet.
                else if(game_Board[y][x-n] == 0){
                    L_stop = true;
                }
            }
            // Scan towards Right side
            if (x + n <= board_length && !R_stop){
                // Will trigger at the first same piece found.
                if (game_Board[y][x+n] == target){
                    distance_R = n-1;
                    R_stop = true;
                }
                // Space note occupied yet.
                else if(game_Board[y][x+n] == 0){
                    R_stop = true;
                }
            }
            // Terminate early condition.
            if (L_stop && R_stop){
                break;
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
            if (y + n <= board_length && !R_stop){
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
            if (y + n <= board_length && x - n >= 0 && !L_stop){
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
            if (y - n >= 0  && x + n <= board_length && !R_stop){
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
            if (y + n <= board_length  && x + n <= board_length && !R_stop){
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

    public boolean[][] validMoves(int target){
        boolean moves[][] = new boolean[8][8];
        for (int x = 0; x < board_length; x++){
            for (int y = 0; y < board_length; y++){
                if (game_Board[x][y] == target){
                    for (int n = x; n < board_length; n++){
                        // Check possible moves horizontally to right.
                        if (game_Board[y][x+n] == 0){
                            if (n == 0) {

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


