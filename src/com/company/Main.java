package com.company;
import com.company.Reversi;
import com.company.PureMCTS;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;

public class Main {

    public static void main(String[] args) {
        final int PLAYOUTS = 500;
	    // write your code here
        Reversi game = new Reversi();
        game.newGame();
        Scanner input = new Scanner(System.in);
        int[] move = {0, 0};
        boolean acceptedMove;
        ArrayList<Point> availableMoves = game.validMoves();
        Point nextMove = new Point(0,0);
        boolean skippedMove = false;
        int human = 1;
        PureMCTS AI;
        Point AI_Move;
        int[] AI_wins;
        while(true) {
            while(true) {
                System.out.println("Would you like to make a move first? (Y/N)");
                String goFirst = input.next();
                goFirst.toLowerCase();
                if (goFirst.equals("n") || goFirst.equals("no")) {
                    human = 2;
                    AI_wins = new int[availableMoves.size()];
                    for (int n = 0; n < availableMoves.size(); n++) {
                        AI_Move = availableMoves.get(n);
                        for (int i = 0; i < PLAYOUTS; i++) {
                            AI = new PureMCTS(game);
                            AI_wins[n] += AI.randomPlayout(AI_Move);
                        }
                    }
                    int largest = 0;
                    for (int i = 1; i < AI_wins.length; i++) {
                        if (AI_wins[i] > AI_wins[largest]) {
                            largest = i;
                        }
                    }
                    // Do move:
                    AI_Move = availableMoves.get(largest);
                    game.makeMove(AI_Move.y, AI_Move.x);
                    availableMoves = game.validMoves();
                    System.out.println("Player's piece is: O");
                    break;
                } else if (goFirst.equals("y") || goFirst.equals("yes")) {
                    human = 1;
                    System.out.println("Player's piece is: X");
                    break;
                } else {
                    System.out.println("Invalid response.");
                }

            }
            while (true) {
                whosTurn(game, human);
                printBoard(game);
                if (availableMoves.isEmpty()) {
                    if (skippedMove) {
                        System.out.println("No move available! Ending game.");
                        break;
                    }
                    System.out.println("No move available! Skipping turn.");
                    game.forceSkipMove();
                    availableMoves = game.validMoves();
                    skippedMove = true;
                } else {
                    while (true) {
                        acceptedMove = false;
                        System.out.println("Please type the Y Coordinate of your next move");
                        int number = input.nextInt();
                        if (number >= 0 && number < 8) {
                            move[0] = number;
                        }
                        System.out.println("Please type the X Coordinate of your next move");
                        number = input.nextInt();
                        if (number >= 0 && number < 8) {
                            move[1] = number;
                        }
                        nextMove.setLocation(move[1], move[0]);
                        for (final Point validMove : availableMoves) {
                            if (nextMove.equals(validMove)) {
                                skippedMove = false;
                                game.makeMove(move[0], move[1]);
                                availableMoves = game.validMoves();
                                acceptedMove = true;
                                break;
                            }
                        }
                        if (!acceptedMove) {
                            System.out.println("Invalid move");
                        } else {
                            break;
                        }
                    }
                }
                // Human move accepted or skipped, now is AI's turn.
                if (availableMoves.isEmpty()) {
                    if (skippedMove) {
                        System.out.println("No move available! Ending game.");
                        break;
                    }
                    System.out.println("No move available! Skipping turn.");
                    game.forceSkipMove();
                    availableMoves = game.validMoves();
                    skippedMove = true;
                } else {
                    AI_wins = new int[availableMoves.size()];
                    for (int n = 0; n < availableMoves.size(); n++) {
                        AI_Move = availableMoves.get(n);
                        for (int i = 0; i < PLAYOUTS; i++) {
                            AI = new PureMCTS(game);
                            AI_wins[n] += AI.randomPlayout(AI_Move);
                        }
                    }
                    int largest = 0;
                    for (int i = 1; i < AI_wins.length; i++) {
                        if (AI_wins[i] > AI_wins[largest]) {
                            largest = i;
                        }
                    }
                    // Do move:
                    AI_Move = availableMoves.get(largest);
                    game.makeMove(AI_Move.y, AI_Move.x);
                    availableMoves = game.validMoves();
                }
            }
            System.out.println("==========");
            System.out.println("Game Over!");
            System.out.println("==========");
            checkWinner(game, human);
            while(true) {
                System.out.println("Would you like to play again? (Y/N)");
                String goFirst = input.next();
                goFirst.toLowerCase();
                if (goFirst.equals("n") || goFirst.equals("no")) {
                    System.out.println("Good game!");
                    System.exit(0);
                }
                else if (goFirst.equals("y") || goFirst.equals("yes")) {
                    game.newGame();
                    availableMoves = game.validMoves();
                    skippedMove = false;
                    break;
                }
            }
        }
    }

    public static void printBoard(Reversi game){
        int piece;
        System.out.println("   0 | 1 | 2 | 3 | 4 | 5 | 6 | 7  ");
        System.out.println("   -------------------------------");
        for (int y = 0; y < 8; y++){
            System.out.print(y + "|");
            for (int x = 0; x < 8; x++){
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

    public static void checkWinner(Reversi game, int human){
        int winner = game.whoWon();
        int[] scores = game.scores();
        if (winner == 0){
            System.out.println("It's a tie!");
        }
        else if (winner == human) {
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

    public static void whosTurn(Reversi game, int human){
        if (game.current_Move == human){
            System.out.println("Human move");
        }
        else {
            System.out.println("CPU move");
        }
    }
}
