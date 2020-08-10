/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;
import com.company.Reversi;
import com.company.PureMCTS;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import java.time.Instant;
import java.time.Duration;

/**
 *
 * @author gerlandl
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        final int MCTS_PLAYOUTS = 10000;
        final int NUMBER_OF_GAMES = 100;
        final ArrayList<Point> corners = new ArrayList<Point>();
        corners.add(new Point(0,0));
        corners.add(new Point(0,7));
        corners.add(new Point(7,0));
        corners.add(new Point(7,7));

        Reversi game = new Reversi();
        PureMCTS AI;
        Scanner input = new Scanner(System.in);
        int[] move = {0, 0};
        boolean acceptedMove;
        ArrayList<Point> availableMoves;
        Point nextMove = new Point(0, 0);
        boolean skippedMove = false;
        int MCTS = 2;
        int human = 1;
        Point AI_Move;
        int[] AI_wins;
        AtomicInteger wins;
        List<Thread> threadList;
        Instant startTime;
        Instant endTime;
        Duration runTime;
        boolean playCorner;
        int lowest = -1500;

        int HeuristicTotalScore = 0;
        int MCTSTotalScore = 0;

        // {Heuristic Wins, MCTS Wins, Ties}
        int[] stats = {0, 0, 0};

        double MCTS_running_Time = 0;
        double Heuristics_running_Time = 0;
        double MCTS_moves = 0;
        double Heuristics_moves = 0;

        // HeuristicAI == Human
        // MCTS always == CPU
        for (int b = 0; b < NUMBER_OF_GAMES; b++) {
            int[][] boardValues = {
                    { 1000, lowest, 1000, 1000, 1000, 1000, lowest, 1000},
                    {lowest, lowest, 0, 0, 0, 0, lowest, lowest},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {lowest, lowest, 0, 0, 0, 0, lowest, lowest},
                    { 1000, lowest, 1000, 1000, 1000, 1000, lowest, 1000}};
            while (true) {
                //System.out.println("Would you like the Heuristics AI to make a move first? (Y/N)");
                //String goFirst = input.next();
                String goFirst = "n";
                goFirst.toLowerCase();
                if (goFirst.equals("n") || goFirst.equals("no")) {
                    game.newGame(2);

                    availableMoves = game.validMoves();
                    MCTS = 1;
                    human = 2;
                    AI_wins = new int[availableMoves.size()];
                    startTime = Instant.now();
                    for (int n = 0; n < availableMoves.size(); n++) {
                        AI_Move = availableMoves.get(n);
                        wins = new AtomicInteger(0);
                        for (int i = 0; i < MCTS_PLAYOUTS; i++) {
                            AI = new PureMCTS(game);
                            AI_wins[n] += AI.randomPlayout(AI_Move, MCTS);
                        }
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
                    MCTS_moves++;
                    game.makeMove(AI_Move.y, AI_Move.x);
                    availableMoves = game.validMoves();
                    //System.out.println("Player's piece is: O");
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

            while (true) {
                /*whosTurn(game, human);
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
                }
                // Human move accepted or skipped, now is AI's turn.*/

                // ImprovedAI Move.
                System.out.println("Improved Heuristics Move");
                printBoard(game);
                availableMoves = game.validMoves();
                if (availableMoves.isEmpty()) {
                    if (skippedMove) {
                        //System.out.println("No move available! Ending game.");
                        break;
                    }
                    //System.out.println("No move available! Skipping turn.");
                    game.forceSkipMove();
                    availableMoves = game.validMoves();
                    skippedMove = true;
                } else {
                    playCorner = false;
                    AI_Move = availableMoves.get(0);
                    AI_wins = new int[availableMoves.size()];
                    startTime = Instant.now();
                    for (int n = 0; n < availableMoves.size(); n++) {
                        AI_Move = availableMoves.get(n);
                        int scale = boardValues[(int)AI_Move.getY()][(int)AI_Move.getX()];
                        AI_wins[n] = scale;
                        //System.out.println(boardValues[(int)AI_Move.getY()][(int)AI_Move.getX()]);
                        wins = new AtomicInteger(0);
                        if (corners.contains(AI_Move)){
                            // Play the corner. Skip other possible moves.
                            playCorner = true;
                            break;
                        }
                        threadList = new ArrayList<Thread>();
                        for (int i = 0; i < MCTS_PLAYOUTS; i++) {
                            AI = new PureMCTS(game);
                            AI_wins[n] += AI.randomPlayout(AI_Move, human);
                        }
                    }
                    if (!playCorner) {
                        int largest = 0;
                        for (int i = 1; i < AI_wins.length; i++) {
                            if (AI_wins[i] > AI_wins[largest]) {
                                largest = i;
                            }
                        }
                        AI_Move = availableMoves.get(largest);
                    }
                    endTime = Instant.now();
                    runTime = Duration.between(startTime, endTime);
                    Heuristics_running_Time += runTime.getSeconds();
                    // Do move:
                    game.makeMove(AI_Move.y, AI_Move.x);
                    updateHeuristicBoard(boardValues, AI_Move, true);
                    Heuristics_moves++;
                    skippedMove = false;

                }
                availableMoves = game.validMoves();
                System.out.println("Pure MCTS Move");
                printBoard(game);
                if (availableMoves.isEmpty()) {
                    if (skippedMove) {
                        //System.out.println("No move available! Ending game.");
                        break;
                    }
                    //System.out.println("No move available! Skipping turn.");
                    game.forceSkipMove();
                    availableMoves = game.validMoves();
                    skippedMove = true;
                } else {
                    AI_wins = new int[availableMoves.size()];
                    startTime = Instant.now();
                    for (int n = 0; n < availableMoves.size(); n++) {
                        AI_Move = availableMoves.get(n);
                        wins = new AtomicInteger(0);
                        for (int i = 0; i < MCTS_PLAYOUTS; i++) {
                            AI = new PureMCTS(game);
                            AI_wins[n] += AI.randomPlayout(AI_Move, MCTS);
                        }
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
                    MCTS_moves++;
                    updateHeuristicBoard(boardValues, AI_Move, false);
                    skippedMove = false;
                }
            }

            System.out.println("==========");
            System.out.println("Game Over!");
            System.out.println("==========");
            checkWinner(game, human, stats);

            System.out.println("\n=============");
            System.out.println("Running Times");
            System.out.println("=============");
            System.out.println("Heuristics Running time: " + Heuristics_running_Time + " seconds");
            System.out.println("MCTS Running time: " + MCTS_running_Time + " seconds\n");

            int[] gameScores = game.scores();
            HeuristicTotalScore += gameScores[0];
            MCTSTotalScore += gameScores[1];

            while (true) {
                //System.out.println("Would you like to play again? (Y/N)");
                //String goFirst = input.next();
                String goFirst = "y";
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

        System.out.println("\n=======================");
        System.out.println("Finished Running" + " games");
        System.out.println("=========================");
        System.out.println("Heuristics Won: " + stats[0] + "/" + NUMBER_OF_GAMES + " times");
        System.out.println("MCTS Won: " + stats[1] + "/" + NUMBER_OF_GAMES + " times");
        System.out.println("Ties: " + stats[2] + "/" + NUMBER_OF_GAMES + " times\n");

        double avgHeuristicRunTime = Heuristics_running_Time/Heuristics_moves;
        double avgMCTSRunTime = MCTS_running_Time/MCTS_moves;

        System.out.println("Heuristics Average Time Per Move: " + avgHeuristicRunTime +  " seconds");
        System.out.println("MCTS Average Time Per Move: " + avgMCTSRunTime +  " seconds");
        System.out.println("Heuristics Made: " + Heuristics_moves + " moves.");
        System.out.println("MCTS Made: " + MCTS_moves + " moves.\n");

        System.out.println("Heuristics Total Score: " + HeuristicTotalScore);
        System.out.println("MCTS Total Score: " + MCTSTotalScore);

        double heuristicAverageScorePerGame = HeuristicTotalScore/(double)NUMBER_OF_GAMES;
        double MCTSAverageScorePerGame = MCTSTotalScore/(double)NUMBER_OF_GAMES;

        System.out.println("Heuristics Average Score: " + heuristicAverageScorePerGame);
        System.out.println("MCTS Average Score: " + MCTSAverageScorePerGame);

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

    public static void checkWinner(Reversi game, int human, int[] stats){
        int winner = game.whoWon();
        // [Human/Heurstics, MCTS AI]
        int[] scores = game.scores();

        if (winner == 0){
            System.out.println("It's a tie!");
            stats[2] = stats[2] + 1;
        }
        else if (winner == human) {
            System.out.println("Human has won!");
            stats[0] = stats[0] + 1;
        }
        else {
            System.out.println("CPU has won!");
            stats[1] = stats[1] + 1;
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

    public static void updateHeuristicBoard (int[][] board, Point move, boolean heuristic){
        if (heuristic) {
            if (move.getY() == 0) {
                if (move.getX() == 0) {
                    board[0][1] = 1000;
                    //board[0][2] = 1000;
                    board[1][0] = 1000;
                    //board[2][0] = 1000;
                    board[1][1] = 1000;
                }
                if (move.getX() == 7) {
                    //board[0][5] = 1000;
                    board[0][6] = 1000;
                    board[1][7] = 1000;
                    //board[2][7] = 1000;
                    board[1][6] = 1000;
                }
            }
            if (move.getY() == 7) {
                if (move.getX() == 0) {
                    board[7][1] = 1000;
                    //board[7][2] = 1000;
                    //board[5][0] = 1000;
                    board[6][0] = 1000;
                    board[6][1] = 1000;
                }
                if (move.getX() == 7) {
                    board[7][6] = 1000;
                    //board[7][5] = 1000;
                    board[6][7] = 1000;
                    //board[5][7] = 1000;
                    board[6][6] = 1000;
                }
            }
        } else {
            if (move.getY() == 0) {
                if (move.getX() == 0) {
                    board[0][1] = 500;
                    //board[0][2] = 500;
                    board[1][0] = 500;
                    //board[2][0] = 500;
                    board[1][1] = 500;
                }
                if (move.getX() == 7) {
                    //board[0][5] = 500;
                    board[0][6] = 500;
                    board[1][7] = 500;
                    //board[2][7] = 500;
                    board[1][6] = 500;
                }
            }
            if (move.getY() == 7) {
                if (move.getX() == 0) {
                    board[7][1] = 500;
                    //board[7][2] = 500;
                    //board[5][0] = 500;
                    board[6][0] = 500;
                    board[6][1] = 500;
                }
                if (move.getX() == 7) {
                    board[7][6] = 500;
                    //board[7][5] = 500;
                    board[6][7] = 500;
                    //board[5][7] = 500;
                    board[6][6] = 500;
                }
            }
        }
    }
}
