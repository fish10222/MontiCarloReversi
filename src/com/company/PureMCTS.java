package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import com.company.Reversi;

public class PureMCTS {
    Random rand = new Random();
    Reversi game;

    public PureMCTS(Reversi currentGame){
        game = currentGame;
    }

    public void randomPlayouts(){
        Point nextMove;


            ArrayList<Point> availableMoves = game.validMoves();
            nextMove = availableMoves.get(rand.nextInt(availableMoves.size()));

    }

}

