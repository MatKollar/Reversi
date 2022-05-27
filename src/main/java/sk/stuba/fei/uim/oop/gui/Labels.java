package sk.stuba.fei.uim.oop.gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter @Setter
public class Labels {
    private JLabel boardSizeLabel;
    private JLabel playerScoreLabel;
    private JLabel computerScoreLabel;
    private JLabel winnerLabel;
    public int playerScore;
    public int computerScore;

    public Labels(JLabel boardSizeLabel, JLabel playerScoreLabel, JLabel computerScoreLabel, JLabel winnerLabel) {
        this.boardSizeLabel = boardSizeLabel;
        this.playerScoreLabel = playerScoreLabel;
        this.computerScoreLabel = computerScoreLabel;
        this.winnerLabel = winnerLabel;
        this.playerScore = 0;
        this.computerScore = 0;
    }
}
