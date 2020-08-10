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
import java.util.jar.JarEntry;

import javax.swing.*;

public class Main extends JPanel implements MouseListener{
    static int gameSizeInt = 8;
    static JPanel panel = new JPanel() ;
    static int turn = 2;
    static int humancount = 2;
    static int cpu = 2;
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
    static boolean playerturn = false;
    static boolean acceptedMove;
    static boolean skippedMove = false;
    static int human = 1;
    static PureMCTS AI;
    static Point AI_Move;
    static int[] AI_wins;
    static final int PLAYOUTS = 500;

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
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
                panel.repaint();
            }

        });
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
            }

        });
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
                g.drawString("Black = " + humancount + "  White = " + cpu, fontX, fontY);


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
        frame.setJMenuBar(menuBar);
        panel.addMouseListener(this);
        frame.pack();
        // Display frame after all components added
        frame.setVisible(true);
        System.out.println("DRAW BOARD");



    }

    public static void main(String[] args) throws InterruptedException {
//        final int PLAYOUTS = 500;
	    // write your code here
//        Reversi game = new Reversi();
//        game.newGame();
        Scanner input = new Scanner(System.in);
        //int[] move = {0, 0};
//        boolean acceptedMove;
//        availableMoves = game.validMoves();
//        Point nextMove = new Point(0,0);
//        skippedMove = false;
        human = 1;
//        PureMCTS AI;
//        Point AI_Move;
//        int[] AI_wins;
        startGame();
//        while(true) {
//            while(true) {
//                System.out.println("Would you like to make a move first? (Y/N)");
//                String goFirst = input.next();
//                goFirst.toLowerCase();
//                if (goFirst.equals("n") || goFirst.equals("no")) {
//                    human = 2;
//                    AI_wins = new int[availableMoves.size()];
//                    for (int n = 0; n < availableMoves.size(); n++) {
//                        AI_Move = availableMoves.get(n);
//                        for (int i = 0; i < PLAYOUTS; i++) {
//                            AI = new PureMCTS(game);
//                            AI_wins[n] += AI.randomPlayout(AI_Move);
//                        }
//                    }
//                    int largest = 0;
//                    for (int i = 1; i < AI_wins.length; i++) {
//                        if (AI_wins[i] > AI_wins[largest]) {
//                            largest = i;
//                        }
//                    }
//                    // Do move:
//                    AI_Move = availableMoves.get(largest);
//                    game.makeMove(AI_Move.y, AI_Move.x);
//                    availableMoves = game.validMoves();
//                    System.out.println("Player's piece is: O");
//                    break;
//                } else if (goFirst.equals("y") || goFirst.equals("yes")) {
//                    human = 1;
//                    playerturn = true;
//                    System.out.println("Player's piece is: X");
//                    break;
//                } else {
//                    System.out.println("Invalid response.");
//                }
//
//            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Main();
                }
            });

//            while (true) {
                whosTurn(game, human);
                printBoard(game);
                if (availableMoves.isEmpty()) {
                    if (skippedMove) {
                        System.out.println("No move available! Ending game.");
//                        break;
                    }
                    System.out.println("No move available! Skipping turn.");
                    game.forceSkipMove();
                    availableMoves = game.validMoves();
                    skippedMove = true;
                } else {
//                    while (true) {
//                        acceptedMove = false;
//                        mouseclick = false;
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
////                        while (!mouseclick){
////                            System.out.println(mouseclick);
////                            TimeUnit.SECONDS.sleep(1);
////                        }
//                        System.out.println(nextMove);
//                        nextMove.setLocation(move[1], move[0]);
//                        for (final Point validMove : availableMoves) {
//                            if (nextMove.equals(validMove)) {
//                                skippedMove = false;
//                                game.makeMove(move[0], move[1]);
//                                availableMoves = game.validMoves();
//                                acceptedMove = true;
//                                panel.repaint();
//                                break;
//                            }
//                        }
//                        if (!acceptedMove) {
//                            System.out.println("Invalid move");
//                        } else {
//                            break;
//                        }
//                    }
                }

//                // Human move accepted or skipped, now is AI's turn.
//                if (availableMoves.isEmpty()) {
//                    if (skippedMove) {
//                        System.out.println("No move available! Ending game.");
//                        break;
//                    }
//                    System.out.println("No move available! Skipping turn.");
//                    game.forceSkipMove();
//                    availableMoves = game.validMoves();
//                    skippedMove = true;
//                } else {
//                    AI_wins = new int[availableMoves.size()];
//                    for (int n = 0; n < availableMoves.size(); n++) {
//                        AI_Move = availableMoves.get(n);
//                        for (int i = 0; i < PLAYOUTS; i++) {
//                            AI = new PureMCTS(game);
//                            AI_wins[n] += AI.randomPlayout(AI_Move);
//                        }
//                    }
//                    int largest = 0;
//                    for (int i = 1; i < AI_wins.length; i++) {
//                        if (AI_wins[i] > AI_wins[largest]) {
//                            largest = i;
//                        }
//                    }
//                    // Do move:
//                    AI_Move = availableMoves.get(largest);
//                    game.makeMove(AI_Move.y, AI_Move.x);
//                    availableMoves = game.validMoves();
//                    panel.repaint();
//                }

//            }
//            System.out.println("==========");
//            System.out.println("Game Over!");
//            System.out.println("==========");
//            checkWinner(game, human);
//            while(true) {
//                System.out.println("Would you like to play again? (Y/N)");
//                String goFirst = input.next();
//                goFirst.toLowerCase();
//                if (goFirst.equals("n") || goFirst.equals("no")) {
//                    System.out.println("Good game!");
//                    System.exit(0);
//                }
//                else if (goFirst.equals("y") || goFirst.equals("yes")) {
//                    game.newGame();
//                    availableMoves = game.validMoves();
//                    skippedMove = false;
//                    break;
//                }
//            }
//        }
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

    public static void startGame(){
        game.newGame();
        availableMoves = game.validMoves();
        skippedMove = false;
        JPanel popup = new JPanel();
        popup.add(new JLabel("Player goes first?"));
        Object[] options = { "Yes", "No"};
        int result = JOptionPane.showOptionDialog(null, popup, "Who goes first?",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);

        if (result == JOptionPane.YES_OPTION){
            System.out.println("Yes");
        }

        if (result == JOptionPane.NO_OPTION) {
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
        } else if (result == JOptionPane.YES_OPTION) {
            human = 1;
            playerturn = true;
            System.out.println("Player's piece is: X");
        }
    }

    public static void count(Reversi game){
        int[] scores = game.scores();
        humancount = scores[0];
        cpu = scores[1];
        if ((humancount + cpu) == 64){
            if (humancount > cpu){
                JOptionPane.showMessageDialog(panel, "YOU WIN!", "Result",JOptionPane.INFORMATION_MESSAGE);
            }
            else if (cpu > humancount){
                JOptionPane.showMessageDialog(panel, "YOU LOSE!", "Result",JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(panel, "DRAW!", "Result",JOptionPane.INFORMATION_MESSAGE);
            }
        }

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
        acceptedMove = false;
        int x, y, i = 0, j = 0;
        x = e.getX();
        y = e.getY();
        i = x/60;
        j = y/60;
        System.out.println(j);
        System.out.println(i);
        nextMove.setLocation(i, j);
        System.out.println(nextMove);
        availableMoves = game.validMoves();

        if (availableMoves.isEmpty()) {
            game.forceSkipMove();
            skippedMove = true;
        }
        else {
            System.out.println("available move " + availableMoves);
            for (final Point validMove : availableMoves) {
                if (nextMove.equals(validMove)) {
                    System.out.println("VALID MOVE");
                    move[0] = j;
                    move[1] = i;
                    System.out.println(move);
                    playerturn = true;
                    System.out.println(playerturn);
                    skippedMove = false;
                    game.makeMove(move[0], move[1]);
                    System.out.println("MOVE MADE");
                    availableMoves = game.validMoves();
                    acceptedMove = true;
                    panel.repaint();
                }
            }
        }

        System.out.println(nextMove);
        printBoard(game);
        if (acceptedMove) {
            // Human move accepted or skipped, now is AI's turn.
            if (availableMoves.isEmpty()) {
                if (skippedMove) {
                    System.out.println("No move available! Ending game.");
                }
                System.out.println("No move available! Skipping turn.");
                game.forceSkipMove();
                availableMoves = game.validMoves();
                skippedMove = true;
            } else {
                AI_wins = new int[availableMoves.size()];
                for (int n = 0; n < availableMoves.size(); n++) {
                    AI_Move = availableMoves.get(n);
                    for (int z = 0; z < PLAYOUTS; z++) {
                        AI = new PureMCTS(game);
                        AI_wins[n] += AI.randomPlayout(AI_Move);
                    }
                }
                int largest = 0;
                for (int z = 1; z < AI_wins.length; z++) {
                    if (AI_wins[z] > AI_wins[largest]) {
                        largest = z;
                    }
                }
                // Do move:
                AI_Move = availableMoves.get(largest);
                game.makeMove(AI_Move.y, AI_Move.x);
                availableMoves = game.validMoves();
                panel.repaint();
            }
        }
        printBoard(game);
        panel.repaint();
        count(game);
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
