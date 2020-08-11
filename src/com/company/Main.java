package com.company;
import com.company.Reversi;
import com.company.PureMCTS;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Instant;
import java.time.Duration;


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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main extends JPanel implements MouseListener{
    static int gameSizeInt = 8;
    static JPanel panel = new JPanel() ;
    static int humancount = 2;
    static int cpu = 2;
    static int fontX = 10;
    static int fontY = 498;
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
    static int AINum =2;
    static int[][] boardValues;
    static boolean playCorner;
    static AtomicInteger wins;
    static final ArrayList<Point> corners = new ArrayList<Point>();
    static List<Thread> threadList;
    static final int MCTS_PLAYOUTS = 5000;
    static int lowest = -1500;
    static boolean PureMCTSAI = true;

    public Main() {

        // Window Properties
        JFrame frame = new JFrame();
        frame.setTitle("Reversi");
        frame.setLocationRelativeTo(null);
        frame.setLocation(450, 150);
        panel.setPreferredSize(new Dimension(361, 385));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                if(help.getState()) {
                    availableMoves = game.validMoves();
                    for (int i = 0; i < availableMoves.size(); i++) {
                        Point curr = availableMoves.get(i);
                        g.setColor(Color.BLUE);
                        g.fillOval(20 + curr.x * 60, 20 + curr.y * 60, 25, 25);
                    }
                }
                g.setColor(Color.BLACK);
                g.setFont(new Font ("Courier New", Font.BOLD, 15));
                if (human == 1) {
                    g.drawString("Black = " + humancount + "  White = " + cpu + "                You are Black", fontX, fontY);
                } else if (human == 2){
                    g.drawString("Black = " + humancount + "  White = " + cpu + "                You are White", fontX, fontY);
                }
            }
        };

        frame.add(panel);
        frame.setLocation(390, 80);
        frame.setPreferredSize(new Dimension(497, 567));
        frame.setSize(487, 536);
        frame.setJMenuBar(menuBar);
        panel.addMouseListener(this);
        frame.pack();
        // Display frame after all components added
        frame.setVisible(true);
        System.out.println("DRAW BOARD");
    }

    public static void AIvAI() {
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


    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        if (args.length != 0){
            // Begin AIvAI if command args is present
            if (args[0].equals("AI")){
                AIvAI();
            }
        } else {
            chooseAI();
            startGame();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Main();
                }
            });
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


    public static void chooseAI(){
//        game.newGame();
//        availableMoves = game.validMoves();
//        skippedMove = false;
        JPanel popup = new JPanel();
        popup.add(new JLabel("Play against Pure MCTS or Heuristic"));
        Object[] options = { "Pure MCTS", "Heuristic"};
        int result = JOptionPane.showOptionDialog(null, popup, "Mode Selection",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);
        if (result == JOptionPane.NO_OPTION) {
            System.out.println("Heuristic");
            PureMCTSAI = false;
            corners.add(new Point(0,0));
            corners.add(new Point(0,7));
            corners.add(new Point(7,0));
            corners.add(new Point(7,7));
            boardValues = new int[][]{
                    {1000, lowest, 1000, 1000, 1000, 1000, lowest, 1000},
                    {lowest, lowest, 0, 0, 0, 0, lowest, lowest},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {1000, 0, 0, 0, 0, 0, 0, 1000},
                    {lowest, lowest, 0, 0, 0, 0, lowest, lowest},
                    {1000, lowest, 1000, 1000, 1000, 1000, lowest, 1000}};
        } else if (result == JOptionPane.YES_OPTION) {
            System.out.println("Pure MCTS");
        }
    }

    public static void startGame(){
//        game.newGame();
        skippedMove = false;
        JPanel popup = new JPanel();
        popup.add(new JLabel("Player goes first?"));
        Object[] options = { "Yes", "No"};
        int result;
        //Makes sure user selects something
        do{
            result = JOptionPane.showOptionDialog(null, popup, "Who goes first?",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, null);
        } while (result == JOptionPane.CLOSED_OPTION);
        if (result == JOptionPane.NO_OPTION) {
            human = 2;
            AINum = 1;
            game.newGame(human);
            availableMoves = game.validMoves();
            if (PureMCTSAI) {
                PureMCTSMove();
            } else {
                heuristicMove();
            }
        } else if (result == JOptionPane.YES_OPTION) {
            human = 1;
            AINum = 2;
            game.newGame(human);
            availableMoves = game.validMoves();
            playerturn = true;
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

        } else if (availableMoves.isEmpty()){
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


    public static void heuristicMove(){
        System.out.println("Improved Heuristics Move");
        printBoard(game);
        availableMoves = game.validMoves();
        playCorner = false;
        AI_Move = availableMoves.get(0);
        AI_wins = new int[availableMoves.size()];
        for (int n = 0; n < availableMoves.size(); n++) {
            AI_Move = availableMoves.get(n);
            int scale = boardValues[(int)AI_Move.getY()][(int)AI_Move.getX()];
            AI_wins[n] = scale;
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
        game.makeMove(AI_Move.y, AI_Move.x);
        updateHeuristicBoard(boardValues, AI_Move, true);
    }

    public static void PureMCTSMove (){
        AI_wins = new int[availableMoves.size()];
        for (int n = 0; n < availableMoves.size(); n++) {
            AI_Move = availableMoves.get(n);
            for (int z = 0; z < PLAYOUTS; z++) {
                AI = new PureMCTS(game);
                AI_wins[n] += AI.randomPlayout(AI_Move, AINum);
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
            }
        }
        panel.repaint();
        if (acceptedMove) {
            if (availableMoves.isEmpty()) {
                if (skippedMove) {
                    System.out.println("No move available! Ending game.");
                }
                System.out.println("No move available! Skipping turn.");
                game.forceSkipMove();
                availableMoves = game.validMoves();
                skippedMove = true;
            } else {
                if (PureMCTSAI) {
                    PureMCTSMove();
                } else {
                    heuristicMove();
                }
                panel.repaint();
            }
        }
        printBoard(game);
        panel.repaint();
        count(game);
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


    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
}
