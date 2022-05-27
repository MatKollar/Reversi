package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.StoneColor;
import sk.stuba.fei.uim.oop.gui.Labels;
import sk.stuba.fei.uim.oop.player.Computer;
import sk.stuba.fei.uim.oop.player.Player;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameLogic extends UniversalAdapter {
    @Getter
    private Board board;
    private int boardSize;
    private StoneColor[][] logicBoard;
    private final Player player;
    private final Computer computer;
    private Player currentPlayer;
    private final JFrame frame;
    private final Labels labels;
    private int lastChangedRow;
    private int lastChangedCol;
    private boolean positionChanged;
    private boolean canHighlight;

    public GameLogic(JFrame frame, Labels labels) {
        this.frame = frame;
        this.board = new Board(6, labels);
        this.board.addMouseListener(this);
        this.board.addMouseMotionListener(this);
        this.logicBoard = this.board.getLogicBoard();
        this.player = new Player(StoneColor.BLACK);
        this.computer = new Computer(StoneColor.WHITE);
        this.currentPlayer = this.player;
        this.labels = labels;
        this.positionChanged = false;
        this.canHighlight = true;
        this.startNewGame();
    }

    private void startNewGame() {
        this.logicBoard = this.board.getLogicBoard();
        this.boardSize = this.board.getBoardSize();
        this.board.emptyLogicBoard();
        this.labels.getWinnerLabel().setText("");
        board.placeStone((this.boardSize/2) - 1,(this.boardSize/2) - 1, player, this);
        board.placeStone((this.boardSize/2) - 1,(this.boardSize/2), computer, this);
        board.placeStone((this.boardSize/2),(this.boardSize/2) - 1, computer, this);
        board.placeStone((this.boardSize/2),(this.boardSize/2), player, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = (e.getX() / (600/this.boardSize));
        int row = (e.getY() / (540/this.boardSize));
        if (this.logicBoard[row][col] == StoneColor.POSSIBLE && checkForChange(row, col, StoneColor.BLACK, false)) {
            this.makeMove(row, col);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int col = (e.getX() / (600/this.boardSize));
        int row = (e.getY() / (540/this.boardSize));
        if (this.logicBoard[row][col] == StoneColor.POSSIBLE && this.canHighlight) {
            this.lastChangedRow = row;
            this.lastChangedCol = col;
            this.canHighlight = false;
            this.board.highlightTile(row, col);
        }
        if (this.lastChangedRow != row || this.lastChangedCol != col) {
            this.positionChanged = true;
        }
        if (positionChanged) {
            this.board.revertTileColor(this.lastChangedRow, this.lastChangedCol);
            this.positionChanged = false;
            this.canHighlight = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 27) {
            this.frame.dispose();
            System.exit(0);
        } else if (e.getKeyChar() == 'r') {
            this.startNewGame();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int sliderValue =((JSlider) e.getSource()).getValue();
        if (sliderValue == 6 || sliderValue == 8 || sliderValue == 10 || sliderValue == 12) {
            this.newBoard(sliderValue);
            this.startNewGame();
        }
    }

    private void newBoard(int size) {
        this.frame.remove(board);
        this.board = new Board(size, labels);
        this.board.addMouseListener(this);
        this.board.addMouseMotionListener(this);
        this.board.repaintBoard(frame, labels.getBoardSizeLabel(), size);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.startNewGame();
    }

    private void makeMove(int row, int col) {
        checkForChange(row, col, StoneColor.BLACK, true);
        board.placeStone(row, col, player, this);
        computerMakeMove();
    }

    public boolean checkForChange(int row, int col, StoneColor owner, boolean changeStones) {
        int numberOfStones = 0;
        boolean canChange = false;
        for (int adjRow = -1; adjRow < 2; adjRow++) {
            for (int adjCol = -1; adjCol < 2; adjCol++) {
                if (adjCol == 0 && adjRow == 0) {
                    continue;
                }
                int checkedRow = row + adjRow;
                int checkedCol = col + adjCol;
                if (checkedRow >= 0 && checkedCol >= 0 && checkedRow < this.boardSize && checkedCol < this.boardSize) {
                    if (isThereEnemyStone(logicBoard, checkedRow, checkedCol, owner)) {
                        for (int distance = 1; distance < this.boardSize; distance++) {
                            int checkAdjRow = row + (distance * adjRow);
                            int checkAdjCol = col + (distance * adjCol);
                            if (isOutOfBoard(checkAdjRow, checkAdjCol)) {
                                continue;
                            }
                            if (isThereNoStone(checkAdjRow, checkAdjCol)) {
                                break;
                            }
                            if(logicBoard[checkAdjRow][checkAdjCol] == owner) {
                                if (changeStones) {
                                    currentPlayer.flipStones(row, col, adjRow, adjCol, distance, logicBoard);
                                }
                                numberOfStones += distance - 1;
                                canChange = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        computer.setComputerMove(row, col, numberOfStones, currentPlayer);
        return canChange;
    }

    private boolean isThereEnemyStone(StoneColor[][] logicBoard, int row, int col, StoneColor owner) {
        owner = owner == StoneColor.BLACK ? StoneColor.WHITE : StoneColor.BLACK;
        return logicBoard[row][col] == owner;
    }

    private boolean isOutOfBoard(int checkAdjRow, int checkAdjCol) {
        return checkAdjRow < 0 || checkAdjCol < 0  || checkAdjRow > (this.boardSize-1) || checkAdjCol > (this.boardSize-1);
    }

    private boolean isThereNoStone(int checkAdjRow, int checkAdjCol) {
        return logicBoard[checkAdjRow][checkAdjCol] == StoneColor.POSSIBLE || logicBoard[checkAdjRow][checkAdjCol] == StoneColor.NONE;
    }

    private void computerMakeMove() {
        this.currentPlayer = this.computer;
        board.lookForPossibleStones(this, labels, currentPlayer);
        computer.selectStone(board, this);
        this.swapPLayer();
    }

    private void swapPLayer() {
        this.currentPlayer = this.player;
        board.lookForPossibleStones(this, labels, currentPlayer);
        this.checkWinner();
    }

    private void checkWinner() {
        for(int row = 0; row < this.boardSize; row++) {
            for(int col = 0; col < this.boardSize; col++) {
                if (logicBoard[row][col] == StoneColor.POSSIBLE) {
                    return;
                }
            }
        }
        this.printWinner();
    }

    private void printWinner() {
        if (labels.playerScore > labels.computerScore) {
            labels.getWinnerLabel().setText("You won!");
        } else if (labels.playerScore < labels.computerScore) {
            labels.getWinnerLabel().setText("The computer won!");
        }  else {
            labels.getWinnerLabel().setText("It's a draw!");
        }
    }
}