package com.company;
import com.company.Reversi;
import com.company.PureMCTS;
import com.company.ImprovedAI;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;

import java.time.Instant;
import java.time.Duration;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int MCTS_PLAYOUTS = 2500;
        final int Heuristics_PLAYOUTS = 1500;
        final ArrayList<Point> corners = new ArrayList<Point>();
        corners.add(new Point(0,0));
        corners.add(new Point(0,7));
        corners.add(new Point(7,0));
        corners.add(new Point(7,7));
        boolean yesCorner;
        Reversi game = new Reversi();
        Scanner input = new Scanner(System.in);
        int[] move = {0, 0};
        boolean acceptedMove;
        ArrayList<Point> availableMoves;
        Point nextMove = new Point(0, 0);
        boolean skippedMove = false;
        int human = 1;
        PureMCTS AI;
        ImprovedAI HeuristicAI;
        Point AI_Move;
        int[] AI_wins;
        AtomicInteger wins;
        List<Thread> threadList;
        Instant startTime;
        Instant endTime;
        Duration runTime;
        long MCTS_running_Time = 0;
        long Heuristics_running_Time = 0;
        int CPU_skipped_moves = 0;
        int Heuristics_skipped_moves = 0;

        // HeuristicAI == Human
        // MCTS always == CPU
        while (true) {
            while (true) {
                System.out.println("Would you like the Heuristics AI to make a move first? (Y/N)");
                String goFirst = input.next();
                goFirst.toLowerCase();
                if (goFirst.equals("n") || goFirst.equals("no")) {
                    game.newGame(2);
                    availableMoves = game.validMoves();
                    human = 2;
                    AI_wins = new int[availableMoves.size()];
                    startTime = Instant.now();
                    for (int n = 0; n < availableMoves.size(); n++) {
                        AI_Move = availableMoves.get(n);
                        wins = new AtomicInteger(0);
                        threadList = new ArrayList<Thread>();
                        for (int i = 0; i < MCTS_PLAYOUTS; i++) {
                            //AI = new PureMCTS(game);
                            //AI.randomPlayout(AI_Move);
                            Thread t = new Thread(new PureMCTS(game, AI_Move, wins));
                            t.start();
                            threadList.add(t);
                            //System.out.println("Spawned new Thread " + t.getId());
                        }
                        AI_wins[n] = wins.intValue();
                    }
                    int largest = 0;
                    for (int i = 1; i < AI_wins.length; i++) {
                        if (AI_wins[i] > AI_wins[0]) {
                            largest = i;
                        }
                    }
                    endTime = Instant.now();
                    runTime = Duration.between(startTime, endTime);
                    MCTS_running_Time += runTime.getSeconds();

                    // Do move:
                    AI_Move = availableMoves.get(largest);
                    game.makeMove(AI_Move.y, AI_Move.x);
                    availableMoves = game.validMoves();
                    System.out.println("Player's piece is: O");
                    break;
                } else if (goFirst.equals("y") || goFirst.equals("yes")) {
                    game.newGame(1);
                    availableMoves = game.validMoves();
                    human = 1;
                    System.out.println("Player's piece is: X");
                    break;
                } else {
                    System.out.println("Invalid response.");
                }
            }
            /*
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
                                printBoard(game);
                                System.out.println("Waiting for AI's move...");
                                break;
                            }
                        }
                        if (!acceptedMove) {
                            System.out.println("Invalid move");
                        } else {
                            break;
                        }
                    }
                }*/
            // Human move accepted or skipped, now is AI's turn.
            while (true) {
                // ImprovedAI Move.
                System.out.println("Improved Heuristics Move");
                printBoard(game);
                if (availableMoves.isEmpty()) {
                    if (skippedMove) {
                        System.out.println("No move available! Ending game.");
                        break;
                    }
                    System.out.println("No move available! Skipping turn.");
                    Heuristics_skipped_moves++;
                    game.forceSkipMove();
                    availableMoves = game.validMoves();
                    skippedMove = true;
                } else {
                    yesCorner = false;
                    AI_Move = availableMoves.get(0);
                    startTime = Instant.now();
                    for (final Point corner : corners){
                        if (availableMoves.contains(corner)){
                            AI_Move = corner;
                            yesCorner = true;
                            break;
                        }
                    }
                    if (!yesCorner){
                        AI_wins = new int[availableMoves.size()];
                        for (int n = 0; n < availableMoves.size(); n++) {
                            AI_Move = availableMoves.get(n);
                            wins = new AtomicInteger(0);
                            threadList = new ArrayList<Thread>();
                            for (int i = 0; i < Heuristics_PLAYOUTS; i++) {
                                Thread t = new Thread(new ImprovedAI(game, AI_Move, wins));
                                t.start();
                                threadList.add(t);
                            }
                            AI_wins[n] = wins.intValue();
                        }
                        int smallest = 0;
                        for (int i = 1; i < AI_wins.length; i++) {
                            if (AI_wins[i] < AI_wins[smallest]) {
                                smallest = i;
                            }
                        }
                        AI_Move = availableMoves.get(smallest);
                    }
                    endTime = Instant.now();
                    runTime = Duration.between(startTime, endTime);
                    Heuristics_running_Time += runTime.getSeconds();

                    // Do move:

                    game.makeMove(AI_Move.y, AI_Move.x);
                    skippedMove = false;
                    availableMoves = game.validMoves();
                }

                System.out.println("Pure MCTS Move");
                printBoard(game);
                if (availableMoves.isEmpty()) {
                    if (skippedMove) {
                        System.out.println("No move available! Ending game.");
                        break;
                    }
                    System.out.println("No move available! Skipping turn.");
                    CPU_skipped_moves++;
                    game.forceSkipMove();
                    availableMoves = game.validMoves();
                    skippedMove = true;
                } else {
                    AI_wins = new int[availableMoves.size()];
                    startTime = Instant.now();
                    for (int n = 0; n < availableMoves.size(); n++) {
                        AI_Move = availableMoves.get(n);
                        wins = new AtomicInteger(0);
                        threadList = new ArrayList<Thread>();
                        for (int i = 0; i < MCTS_PLAYOUTS; i++) {
                            //AI = new PureMCTS(game);
                            //AI.randomPlayout(AI_Move);
                            Thread t = new Thread(new PureMCTS(game, AI_Move, wins));
                            t.start();
                            threadList.add(t);
                        }
                        AI_wins[n] = wins.intValue();
                    }
                    int largest = 0;
                    for (int i = 1; i < AI_wins.length; i++) {
                        if (AI_wins[i] > AI_wins[largest]) {
                            largest = i;
                        }
                    }
                    endTime = Instant.now();
                    runTime = Duration.between(startTime, endTime);
                    MCTS_running_Time += runTime.getSeconds();
                    // Do move:
                    AI_Move = availableMoves.get(largest);
                    game.makeMove(AI_Move.y, AI_Move.x);
                    skippedMove = false;
                    availableMoves = game.validMoves();
                }
            }

            System.out.println("==========");
            System.out.println("Game Over!");
            System.out.println("==========");
            checkWinner(game, human);

            System.out.println("\n=============");
            System.out.println("Running Times");
            System.out.println("=============");
            System.out.println("Heuristics Running time: " + Heuristics_running_Time + " seconds");
            System.out.println("MCTS Running time: " + MCTS_running_Time + " seconds\n");

            System.out.println("Heuristics Skipped Moves: " + Heuristics_skipped_moves );
            System.out.println("MCTS Skipped Moves: " + CPU_skipped_moves + "\n");

            while (true) {
                System.out.println("Would you like to play again? (Y/N)");
                String goFirst = input.next();
                goFirst.toLowerCase();
                if (goFirst.equals("n") || goFirst.equals("no")) {
                    System.out.println("Good game!");
                    System.exit(0);
                } else if (goFirst.equals("y") || goFirst.equals("yes")) {
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
        // [Human/Heurstics, MCTS AI]
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
        System.out.println("\n===========");
        System.out.println("Final Score");
        System.out.println("===========");
        System.out.println("Human: " + scores[0]);
        System.out.println("CPU: " + scores[1] + "\n");
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
