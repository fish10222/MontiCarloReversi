package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.company.Reversi;

public class ImprovedAI implements Runnable {
    Random rand = new Random();
    Point first_Move;
    Reversi game;
    AtomicInteger totalWins;

    public ImprovedAI(Reversi currentGame, Point AI_Move, AtomicInteger wins){
        // Invoke Copy Constructor
        game = new Reversi(currentGame);
        first_Move = AI_Move;
        totalWins = wins;
    }

    @Override
    public void run(){
        Point nextMove;
        Reversi simulatedGame;
        game.makeMove(first_Move.y, first_Move.x);
        ArrayList<Point> availableMoves = game.validMoves();
        boolean skippedMove = false;
        int OppMoves = 0;
        int AIMoves = 0;
        int[] simulatedGameNoMoves;
        Point simulatedMove;
        for ( int m = 0; m < 5; m++) {
            // Random AI makes move
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
                OppMoves += availableMoves.size();
                nextMove = availableMoves.get(rand.nextInt(availableMoves.size()));
                for (final Point validMove : availableMoves) {
                    if (nextMove.equals(validMove)) {
                        skippedMove = false;
                        game.makeMove(nextMove.y, nextMove.x);
                        availableMoves = game.validMoves();
                        break;
                    }
                }
            }
            // Heuristics AI make move
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
                // Select the move that reduces the number of available moves the Opponent has
                simulatedGameNoMoves = new int[availableMoves.size()];
                for (int i = 0; i < availableMoves.size(); i++) {
                    simulatedMove = availableMoves.get(i);
                    simulatedGame = new Reversi(game);
                    simulatedGame.makeMove(simulatedMove.y, simulatedMove.x);
                    simulatedGameNoMoves[i] = simulatedGame.validMoves().size();
                }
                // Select the move with the lowest resulting moves for the opponent.
                int smallest = 0;
                for (int i = 1; i < simulatedGameNoMoves.length; i++) {
                    if (simulatedGameNoMoves[i] < simulatedGameNoMoves[smallest]) {
                        smallest = i;
                    }
                }
                // Make that move.
                nextMove = availableMoves.get(smallest);
                for (final Point validMove : availableMoves) {
                    if (nextMove.equals(validMove)) {
                        skippedMove = false;
                        game.makeMove(nextMove.y, nextMove.x);
                        availableMoves = game.validMoves();
                        break;
                    }
                }
            }
        }

        int currentTurn = game.whosTurn();
        int winner = game.whoWon();
        int HeursticMoves;
        int RanAIMoves;
        int[] scores = game.scores();
        int scaling = scores[1] - scores[0];
        if (OppMoves > 16 ){
            scaling = scaling*2;
        }

        if (currentTurn == game.Human_indentifier) {
            HeursticMoves = game.validMoves().size();
            game.forceSkipMove();
            RanAIMoves = game.validMoves().size();
        } else {
            RanAIMoves = game.validMoves().size();
            game.forceSkipMove();
            HeursticMoves = game.validMoves().size();
        }
        int value = RanAIMoves - HeursticMoves;

        if (winner == game.CPU_identifier) {

            value += scaling;
        }
        else {
            value -= scaling;
        }
        totalWins.addAndGet(value);
    }
}

