
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MinesweeperGUI extends JFrame implements ActionListener {
	private static final int BOARD_SIZE = 10;
	private static final int NUM_MINES = 10;
	private JButton[][] squares;
	private boolean[][] mineLocations;
	private boolean[][] uncoveredLocations;

	public MinesweeperGUI() {
		setTitle("Minesweeper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// create grid of buttons for game board
		JPanel board = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		squares = new JButton[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				JButton square = new JButton();
				square.setPreferredSize(new Dimension(30, 30));
				square.addActionListener(this);
				board.add(square);
				squares[i][j] = square;
			}
		}

		// add game board to frame
		getContentPane().add(board, BorderLayout.CENTER);

		// initialize game
		initializeBoard();
		displayBoard();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initializeBoard() {
		mineLocations = new boolean[BOARD_SIZE][BOARD_SIZE];
		uncoveredLocations = new boolean[BOARD_SIZE][BOARD_SIZE];
		int minesToPlace = NUM_MINES;
		while (minesToPlace > 0) {
			int row = (int)(Math.random() * BOARD_SIZE);
			int col = (int)(Math.random() * BOARD_SIZE);
			if (!mineLocations[row][col]) {
				mineLocations[row][col] = true;
				minesToPlace--;
			}
		}
	}

	private void displayBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (uncoveredLocations[i][j]) {
					if (mineLocations[i][j]) {
						squares[i][j].setText("*");
					} else {
						squares[i][j].setText("" + getAdjacentMines(i, j));
					}
					squares[i][j].setEnabled(false);
				} else {
					squares[i][j].setText("");
					squares[i][j].setEnabled(true);
				}
			}
		}
	}

	private int getAdjacentMines(int row, int col) {
		int count = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && mineLocations[i][j]) {
					count++;
				}
			}
		}
		return count;
	}

	private void uncoverSquare(int row, int col) {
		if (uncoveredLocations[row][col]) {
			return;
		}
		uncoveredLocations[row][col] = true;
		if (mineLocations[row][col]) {
			gameOver();
			return;
		}
		if (getAdjacentMines(row, col) == 0) {
			// recursively uncover adjacent squares
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (i >= 0 && i < BOARD_SIZE && j < BOARD_SIZE) {
						uncoverSquare(i, j);
					}
				}
			}
		}
		if (checkForWin()) {
			JOptionPane.showMessageDialog(this, "You win!");
			System.exit(0);
		}
		displayBoard();
	}

	private void gameOver() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				uncoveredLocations[i][j] = true;
			}
		}
		displayBoard();
		JOptionPane.showMessageDialog(this, "Game over!");
		System.exit(0);
	}

	private boolean checkForWin() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (!mineLocations[i][j] && !uncoveredLocations[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		int row = -1;
		int col = -1;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (squares[i][j] == button) {
					row = i;
					col = j;
				}
			}
		}
		uncoverSquare(row, col);
	}
}

