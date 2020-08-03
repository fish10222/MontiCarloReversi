package com.company;
import com.company.Reversi;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Reversi game = new Reversi();
        game.newGame(1);
        Scanner input = new Scanner(System.in);
        int[] move = {0, 0};
        boolean acceptedMove = false;
        ArrayList<int[]> availableMoves = game.validMoves();
        while(!availableMoves.isEmpty()){
            acceptedMove = false;
            printBoard(game);
            System.out.println("Please type the Y Coordinate of your next move");
            int number = input.nextInt();
            if (number >= 0 && number < 8){
                move[0]=number;
            }
            System.out.println("Please type the X Coordinate of your next move");
            number = input.nextInt();
            if (number >= 0 && number < 8){
                move[1]=number;
            }
            for (final int[] validMove : availableMoves){
                if(Arrays.equals(validMove, move)){
                    game.makeMove(move[0], move[1]);
                    availableMoves = game.validMoves();
                    acceptedMove = true;
                    break;
                }
            }
            if (!acceptedMove){
                System.out.println("Invalid move");
            }
        }
    }

    public static void printBoard(Reversi game){
        int piece;
        System.out.println("   0 | 1 | 2 | 3 | 4 | 5 | 6 | 7  ");
        System.out.println("   -------------------------------");
        for (int y = 0; y < 4; y++){
            System.out.print(y + "|");
            for (int x = 0; x < 4; x++){
                piece = game.pieceAt(y,x);
                // Blank
                if ( piece == 0){
                    System.out.print(" - |");
                }
                else if (piece == 1){
                    System.out.print(" X |");
                }
                else if (piece == 2){
                    System.out.print(" O |");
                }
            }
            System.out.println("");
            System.out.println("   -------------------------------");
        }
    }
}
