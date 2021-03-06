package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import com.company.Reversi;

public class PureMCTS {
    Random rand = new Random();
    Reversi game;

    public PureMCTS(Reversi currentGame){
        // Invoke Copy Constructor
        game = new Reversi(currentGame);
    }

    public int randomPlayout(Point AI_Move, int player){
        Point nextMove;
        game.makeMove(AI_Move.y, AI_Move.x);
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
        int opponent = -1;
        if (player == 1){
            opponent = 2;
        }
        if (player == 2){
            opponent = 1;
        }
        if (winner == opponent){
            return -1;
        }
        if (winner == player){
            return 1;
        }
        if (winner == 0){
            return 0;
        }
        return 0;
    }
}

