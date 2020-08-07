package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.company.Reversi;

public class PureMCTS implements Runnable {
    Random rand = new Random();
    Point first_Move;
    Reversi game;
    AtomicInteger totalWins;

    public PureMCTS(Reversi currentGame, Point AI_Move, AtomicInteger wins){
        // Invoke Copy Constructor
        game = new Reversi(currentGame);
        first_Move = AI_Move;
        totalWins = wins;
    }

    @Override
    public void run(){
        Point nextMove;
        game.makeMove(first_Move.y, first_Move.x);
        ArrayList<Point> availableMoves = game.validMoves();
        boolean skippedMove = false;
        while(true) {
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
        }
        int winner = game.whoWon();
        if (winner == 1){
            totalWins.addAndGet(-1);
            return;
        }
        if (winner == 2){
            totalWins.addAndGet(1);
            return;
        }
        if (winner == 0){
            totalWins.addAndGet(-1);
            return;
        }
    }
}

