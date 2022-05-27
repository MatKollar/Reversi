package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.controls.GameLogic;
import sk.stuba.fei.uim.oop.gui.Labels;
import sk.stuba.fei.uim.oop.player.Player;

import javax.swing.*;
import java.awt.*;

@Getter @Setter
public class Board extends JPanel {
    private int boardSize;
    private JPanel panelBoard;
    private StoneColor[][] logicBoard;
    private Labels labels;

    public Board(int size, Labels labels) {
        super(new BorderLayout());
        boardSize = size;
        this.logicBoard = new StoneColor[boardSize][boardSize];
        this.labels = labels;
        this.initializePanelBoard();
        this.initializeLogicBoard();
    }

    private void initializePanelBoard() {
        this.panelBoard = new JPanel(new GridLayout(this.boardSize,this.boardSize));
        for(int row = 0; row < this.boardSize; row++) {
            for(int col = 0; col < this.boardSize; col++) {
                JPanel tile = new JPanel(new BorderLayout());
                tile.setBackground(new Color(0x488D12));
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                this.panelBoard.add(tile);
            }
        }
        this.add(this.panelBoard, BorderLayout.CENTER);
    }

    private void initializeLogicBoard() {
        for(int row = 0; row < boardSize; row++) {
            for(int col = 0; col < boardSize; col++) {
                logicBoard[row][col] = StoneColor.NONE;
            }
        }
    }

    public void emptyLogicBoard() {
        for (int row = 0; row < this.boardSize; row++) {
            for (int col = 0; col < this.boardSize; col++) {
                this.logicBoard[row][col] = StoneColor.NONE;
            }
        }
    }

    public void repaintBoard(JFrame frame, JLabel boardSizeLabel, int size) {
        boardSizeLabel.setText("Board size:  " + size + "x" + size);
        frame.add(this);
        frame.revalidate();
    }

    public void highlightTile(int row, int col) {
        JPanel tile = (JPanel)this.panelBoard.getComponent(this.getIndex(row, col));
        tile.setBackground(new Color(0x6ED029));
        this.panelBoard.updateUI();
    }

    public void revertTileColor(int lastChangedRow, int lastChangedCol) {
        JPanel tile = (JPanel)this.panelBoard.getComponent(this.getIndex(lastChangedRow, lastChangedCol));
        tile.setBackground(new Color(0x488D12));
    }

    public void placeStone(int row, int col, Player currentPlayer, GameLogic logic) {
        if(this.logicBoard[row][col] == StoneColor.NONE || this.logicBoard[row][col] == StoneColor.POSSIBLE) {
            this.logicBoard[row][col] = currentPlayer.getStone();
        }
        this.lookForPossibleStones(logic, labels, currentPlayer);
    }

    public void lookForPossibleStones(GameLogic logic, Labels labels, Player currentPlayer) {
        labels.playerScore = 0;
        labels.computerScore = 0;
        this.removePossibleStones();
        for(int row = 0; row < this.boardSize; row++) {
            for(int col = 0; col < this.boardSize; col++) {
                JPanel tile = (JPanel)this.panelBoard.getComponent(getIndex(row, col));
                tile.removeAll();
                if (logicBoard[row][col] == StoneColor.NONE && logic.checkForChange(row, col, currentPlayer.getStone(), false)) {
                    addStone(row, col, StoneColor.POSSIBLE);
                    if(this.logicBoard[row][col] == StoneColor.NONE) {
                        this.logicBoard[row][col] = StoneColor.POSSIBLE;
                    }
                }
                if (logicBoard[row][col] == StoneColor.BLACK) {
                    addStone(row, col, StoneColor.BLACK);
                    labels.playerScore++;
                }
                if (logicBoard[row][col] == StoneColor.WHITE) {
                    addStone(row, col, StoneColor.WHITE);
                    labels.computerScore++;
                }
            }
        }
        labels.getPlayerScoreLabel().setText("Your score: " + labels.playerScore);
        labels.getComputerScoreLabel().setText("Computer score: " + labels.computerScore);
    }

    private void removePossibleStones() {
        for(int row = 0; row < this.boardSize; row++) {
            for(int col = 0; col < this.boardSize; col++) {
                if (logicBoard[row][col] == StoneColor.POSSIBLE) {
                    logicBoard[row][col] = StoneColor.NONE;
                }
            }
        }
    }

    private void addStone(int row, int col, StoneColor owner) {
        JPanel tile = (JPanel)this.panelBoard.getComponent(getIndex(row, col));
        tile.removeAll();
        Stone stone = new Stone(owner, boardSize);
        tile.setBackground(new Color(0x488D12));
        tile.add(stone);
        this.panelBoard.updateUI();
    }

    public int getIndex(int row, int col) {
        return (row * this.boardSize) + col;
    }
}