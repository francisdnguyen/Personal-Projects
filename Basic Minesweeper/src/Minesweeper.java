import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

	private static final int BOARD_SIZE = 10;
	private static final int NUM_MINES = 10;

	private static boolean[][] mineLocations = new boolean[BOARD_SIZE][BOARD_SIZE];
	private static boolean[][] uncoveredLocations = new boolean[BOARD_SIZE][BOARD_SIZE];

	private static int numUncovered = 0;

	public static void main(String[] args) {
		initializeGame();
		playGame();
	}

	/**
	 * Initializes the game by placing the mines on the board.
	 */
	private static void initializeGame() {
		placeMines();
	}

	/**
	 * Randomly places the mines on the board.
	 */
	private static void placeMines() {
		Random rand = new Random();
		int numMinesPlaced = 0;
		while (numMinesPlaced < NUM_MINES) {
			int row = rand.nextInt(BOARD_SIZE);
			int col = rand.nextInt(BOARD_SIZE);
			if (!mineLocations[row][col]) {
				mineLocations[row][col] = true;
				numMinesPlaced++;
			}
		}
	}

	/**
	 * Main game loop, handles player input and game state.
	 */
	private static void playGame() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			printBoard();
			System.out.print("Enter row and column (e.g. 3 5): ");
			int row = scanner.nextInt();
			int col = scanner.nextInt();
			if (mineLocations[row][col]) {
				gameOver();
				return;
			}
			uncoverSquare(row, col);
			if (checkWin()) {
				gameWon();
				return;
			}
		}
	}

	/**
	 * Prints the current state of the game board.
	 */
	private static void printBoard() {
		System.out.print("  ");
		for (int col = 0; col < BOARD_SIZE; col++) {
			System.out.print(col + " ");
		}
		System.out.println();
		for (int row = 0; row < BOARD_SIZE; row++) {
			System.out.print(row + " ");
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (uncoveredLocations[row][col]) {
					if (mineLocations[row][col]) {
						System.out.print("* ");
					} else {
						int numAdjacentMines = getAdjacentMines(row, col);
						System.out.print(numAdjacentMines + " ");
					}
				} else {
					System.out.print("- ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Uncovers the square at the given row and column, and recursively uncovers all 
	 * adjacent squares with no mines.
	 */
	private static void uncoverSquare(int row, int col) {
		if (!uncoveredLocations[row][col]) {
			uncoveredLocations[row][col] = true;
			numUncovered++;
			if (getAdjacentMines(row, col) == 0) {
				for (int i = row - 1; i <= row + 1; i++) {
					for (int j = col - 1; j <= col + 1; j++) {
						if (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE) {
							if (!(i == row && j == col)) {
								uncoverSquare(i, j);
							}
						}
					}
				}
			}
		}
	}

	// Returns the number of adjacent mines for a given square on the board
	private static int getAdjacentMines(int row, int col) {
		int numAdjacentMines = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE) {
					if (mineLocations[i][j]) {
						numAdjacentMines++;
					}
				}
			}
		}
		return numAdjacentMines;
	}


	// Checks if the player has uncovered all the squares on the board except for the mines
	private static boolean checkWin() {
		return numUncovered == BOARD_SIZE * BOARD_SIZE - NUM_MINES;
	}

	/**
	 * Prints a message indicating that the game is over, and prints the final board state.
	 */
	private static void gameOver() {
		System.out.println("Game over!");
		printBoard();
	}

	/**
	 * Prints a message indicating that the game has been won, and prints the final board state.
	 */
	private static void gameWon() {
		System.out.println("Congratulations, you won!");
		printBoard();
	}

}
