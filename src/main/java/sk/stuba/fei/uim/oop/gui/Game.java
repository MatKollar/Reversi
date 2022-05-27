package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {

    public Game() {
        JFrame frame = new JFrame("Reversi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel bottomMenu = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        JPanel body = new JPanel(new BorderLayout());

        JLabel currentPlayerLabel = new JLabel("Your Turn");
        currentPlayerLabel.setBorder(BorderFactory.createEmptyBorder(0, 250, 20, 0));
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(currentPlayerLabel, BorderLayout.WEST);

        JLabel boardSizeLabel = new JLabel("Board size:  6x6");
        boardSizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 60));
        boardSizeLabel.setFont(new Font("Arial", Font.BOLD, 15));
        header.add(boardSizeLabel, BorderLayout.EAST);

        JLabel playerScoreLabel = new JLabel("Your score: 2");
        JLabel computerScoreLabel = new JLabel("Computer score: 2");
        playerScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        computerScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        body.add(playerScoreLabel, BorderLayout.WEST);
        body.add(computerScoreLabel, BorderLayout.EAST);

        JLabel winnerLabel = new JLabel("", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        Labels labels = new Labels(boardSizeLabel, playerScoreLabel, computerScoreLabel, winnerLabel);
        GameLogic logic = new GameLogic(frame, labels);

        JButton resetButton = new JButton("Reset game");
        resetButton.addActionListener(logic);
        resetButton.setFocusable(false);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 6,12, 6);
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(logic);
        slider.setFocusable(false);

        bottomMenu.add(body, BorderLayout.CENTER);
        bottomMenu.add(winnerLabel, BorderLayout.SOUTH);
        bottomMenu.add(header, BorderLayout.NORTH);
        bottomMenu.add(resetButton, BorderLayout.WEST);
        bottomMenu.add(slider, BorderLayout.EAST);

        frame.addKeyListener(logic);
        frame.setFocusable(true);
        frame.add(logic.getBoard(), BorderLayout.CENTER);
        frame.add(bottomMenu, BorderLayout.SOUTH);
        frame.setSize(600, 650);
        frame.setVisible(true);
    }
}