package com.company;
import com.company.Reversi;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Reversi game = new Reversi();
        game.newGame(1);
        Scanner input = new Scanner(System.in);
        int[] move = {0, 0};
        boolean acceptedMove;
        ArrayList<Point> availableMoves = game.validMoves();
        Point nextMove = new Point(0,0);
        boolean skippedMove = false;
        while(true){
            whosTurn(game);
            if (availableMoves.isEmpty()){
                if (skippedMove) {
                    break;
                }
                System.out.println("No move available! Skipping turn.");
                game.forceSkipMove();
                skippedMove = true;
            }
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
            nextMove.setLocation(move[1], move[0]);
            for (final Point validMove : availableMoves){
                if(nextMove.equals(validMove)){
                    skippedMove = false;
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
        System.out.println("==========");
        System.out.println("Game Over!");
        System.out.println("==========");
        checkWinner(game);
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

    public static void checkWinner(Reversi game){
        int winner = game.whoWon();
        int[] scores = game.scores();
        if (winner == 0){
            System.out.println("It's a tie!");
        }
        else if (winner == 1) {
            System.out.println("Human has won!");
        }
        else {
            System.out.println("CPU has won!");
        }
        System.out.println("===========");
        System.out.println("Final Score");
        System.out.println("===========");
        System.out.println("Human: " + scores[0]);
        System.out.println("CPU: " + scores[1]);
    }

    public static void whosTurn(Reversi game){
        if (game.current_Move == 1){
            System.out.println("Human move");
        }
        else {
            System.out.println("CPU move");
        }
    }
}
