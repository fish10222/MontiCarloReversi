package com.company;
import com.company.Reversi;
import com.company.PureMCTS;

import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JPanel implements MouseListener{
    static int gameSizeInt = 8;
    static JPanel panel = new JPanel() ;
    static int turn = 2;
    static int black = 0;
    static int white = 0;
    static int frei = 0;
    static int blue = 0;
    static int fontX = 10;
    static int fontY = 498;
    static int noblue = 0;
    static boolean noOneWin = false;
    static int[] move = {0, 0};
    static Point nextMove = new Point(0,0);
    static ArrayList<Point> availableMoves = new ArrayList<Point>();
    static Reversi game = new Reversi();
    static boolean mouseclick = false;

    public Main() {

        // Window Properties
        JFrame frame = new JFrame();
        frame.setTitle("Reversi");
        frame.setLocationRelativeTo(null);
        frame.setLocation(450, 150);
        panel.setPreferredSize(new Dimension(361, 385));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground( new Color(18, 199, 24));

        // JMenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem newGame = new JMenuItem("New Game");
        JCheckBoxMenuItem help = new JCheckBoxMenuItem("Enable Help");
        JMenuItem exitGame = new JMenuItem("Close Game");
        menuBar.add(file);
        file.add(newGame);
        file.add(help);
        help.setSelected(true);
        file.addSeparator();
        file.add(exitGame);

        exitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int i = 0; i < gameSizeInt; i++)
                    for (int j = 0; j < gameSizeInt; j++) {
                        g.setColor(new Color(18, 199, 24));
                        g.fillRect( j * 60,   i * 60, 60, 60);
                        g.setColor(Color.black);
                        g.drawRect( j * 60,  i * 60, 60, 60);
                    }
                for(int i = 0 ;i < 8;i++){
                    for(int j = 0 ;j < 8;j++){
                        switch (game.pieceAt(j,i)) {
                            case 0:   break;

                            case 1:
                                g.setColor(Color.BLACK);
                                g.fillOval(5+i * 60, 5+j * 60, 50, 50);
                                break;
                            case 2:
                                g.setColor(Color.WHITE);
                                g.fillOval(5+i * 60, 5+j * 60, 50, 50);
                                break;
                            case -1:
                                if(help.getState()){
                                    g.setColor(Color.BLUE);
                                    g.fillOval(20+i * 60, 20+j * 60, 25, 25);
                                }
                                break;

                        }
                    }
                }
                g.setColor(Color.BLACK);
                g.setFont(new Font ("Courier New", Font.BOLD, 15));
                if(frei == 0){
                    if(black > white){
                        g.drawString("Black win     Black = " + black + "  White = " + white, fontX, fontY);
                    }else if(black == white || noOneWin){
                        g.drawString("No one win     Black = " + black + "  White = " + white, fontX, fontY);
                    }else{
                        g.drawString("White win     Black = " + black + "  White = " + white, fontX, fontY);
                    }
                }else{
                    if(turn == 1){
                        g.drawString("Black Turn     Black = " + black + "  White = " + white, fontX, fontY);
                    }else{
                        g.drawString("White Turn     Black = " + black + "  White = " + white, fontX, fontY);
                    }
                }
            }

//            @Override
//            public Dimension getPreferredSize() {
//                if(gameSizeInt == 6){
//                    return new Dimension(361, 385);
//                }else if(gameSizeInt == 8){
//                    return new Dimension(481, 505);
//                }else{
//                    return new Dimension(601, 625);
//                }
//            }
        };

        frame.add(panel);
        frame.setLocation(390, 80);
        frame.setPreferredSize(new Dimension(497, 557));
        frame.setSize(487, 557);
        //frame.setJMenuBar(menuBar);
        panel.addMouseListener(this);
        frame.pack();
        // Display frame after all components added
        frame.setVisible(true);
        System.out.println("DRAW BOARD");

    }

    public static void main(String[] args) throws InterruptedException {
        final int PLAYOUTS = 500;
	    // write your code here
//        Reversi game = new Reversi();
        game.newGame();
        Scanner input = new Scanner(System.in);
        //int[] move = {0, 0};
        boolean acceptedMove;
        availableMoves = game.validMoves();
//        Point nextMove = new Point(0,0);
        boolean skippedMove = false;
        int human = 1;
        PureMCTS AI;
        Point AI_Move;
        int[] AI_wins;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });

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
                        mouseclick = false;
//                        System.out.println("Please type the Y Coordinate of your next move");
//                        int number = input.nextInt();
//                        if (number >= 0 && number < 8) {
//                            move[0] = number;
//                        }
//                        System.out.println("Please type the X Coordinate of your next move");
//                        number = input.nextInt();
//                        if (number >= 0 && number < 8) {
//                            move[1] = number;
//                        }
                        while (!mouseclick){
                            System.out.println(mouseclick);
                            TimeUnit.SECONDS.sleep(1);
                        }
                        System.out.println(nextMove);
//                        nextMove.setLocation(move[1], move[0]);
                        for (final Point validMove : availableMoves) {
                            if (nextMove.equals(validMove)) {
                                skippedMove = false;
                                game.makeMove(move[0], move[1]);
                                availableMoves = game.validMoves();
                                acceptedMove = true;
                                panel.repaint();
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

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x, y, i = 0, j = 0;
        x = e.getX();
        y = e.getY();
        i = x/60;
        j = y/60;
        System.out.println(x);
        System.out.println(j);
        System.out.println("X is: " + i);
        System.out.println("Y is: " + j);
        nextMove.setLocation(j, i);
        for (final Point validMove : availableMoves) {
            if (nextMove.equals(validMove)) {
                System.out.println("VALID MOVE");
                move[0] = j;
                move[1] = i;
                mouseclick = true;
                System.out.println(mouseclick);
            }
        }
//        if(accepted(i,j)){
//            if(turn == 1){
//                data[i][j]=turn;
//                fillAll(i, j);
//                turn = 2;
//            }else{
//                data[i][j]=turn;
//                fillAll(i, j);
//                turn = 1;
//            }
//            help();
//
//            panel.repaint();
//        }
        panel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
}
