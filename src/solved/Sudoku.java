package solved;
import java.util.Arrays;
import java.util.List;

class Sudoku {

    // board
    private int[][] board;
    private int[][] boardSolved;

    // size of Sudoku grids
    private static final int SIZE = 9;
    // size of 1 box
    private static final int BOX_SIZE = 3;
    private static final int EMPTY_CELL = 0;
    // constraints: cell, line, column, boxes
    private static final int CONSTRAINTS = 4;
    // value for each cell
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = SIZE;
    // starting index for cover matrix
    private static final int COVER_START_INDEX = 1;

    // Constructor
    Sudoku(int[][] board) {
        this.board = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, SIZE);
        }
    }

    // Index in the cover matrix
    private int indexInCoverMatrix(int row, int column, int num) {
        return (row - 1) * SIZE * SIZE + (column - 1) * SIZE + (num - 1);
    }

    // Building of an empty cover matrix
    private int[][] createCoverMatrix() {
        int[][] coverMatrix = new int[SIZE * SIZE * MAX_VALUE][SIZE * SIZE * CONSTRAINTS];

        int header = 0;
        header = createCellConstraints(coverMatrix, header);
        header = createRowConstraints(coverMatrix, header);
        header = createColumnConstraints(coverMatrix, header);
        createBoxConstraints(coverMatrix, header);

        return coverMatrix;
    }

    private int createBoxConstraints(int[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= SIZE; row += BOX_SIZE) {
            for (int column = COVER_START_INDEX; column <= SIZE; column += BOX_SIZE) {
                for (int n = COVER_START_INDEX; n <= SIZE; n++, header++) {
                    for (int rowDelta = 0; rowDelta < BOX_SIZE; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < BOX_SIZE; columnDelta++) {
                            int index = indexInCoverMatrix(row + rowDelta, column + columnDelta, n);
                            matrix[index][header] = 1;
                        }
                    }
                }
            }
        }

        return header;
    }

    private int createColumnConstraints(int[][] matrix, int header) {
        for (int column = COVER_START_INDEX; column <= SIZE; column++) {
            for (int n = COVER_START_INDEX; n <= SIZE; n++, header++) {
                for (int row = COVER_START_INDEX; row <= SIZE; row++) {
                    int index = indexInCoverMatrix(row, column, n);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createRowConstraints(int[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= SIZE; row++) {
            for (int n = COVER_START_INDEX; n <= SIZE; n++, header++) {
                for (int column = COVER_START_INDEX; column <= SIZE; column++) {
                    int index = indexInCoverMatrix(row, column, n);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createCellConstraints(int[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= SIZE; row++) {
            for (int column = COVER_START_INDEX; column <= SIZE; column++, header++) {
                for (int n = COVER_START_INDEX; n <= SIZE; n++) {
                    int index = indexInCoverMatrix(row, column, n);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    // Converting Sudoku grid as a cover matrix
    private int[][] convertInCoverMatrix(int[][] grid) {
        int[][] coverMatrix = createCoverMatrix();

        // Taking into account the values already entered in Sudoku's grid instance
        for (int row = COVER_START_INDEX; row <= SIZE; row++) {
            for (int column = COVER_START_INDEX; column <= SIZE; column++) {
                int n = grid[row - 1][column - 1];

                if (n != EMPTY_CELL) {
                    for (int num = MIN_VALUE; num <= MAX_VALUE; num++) {
                        if (num != n) {
                            Arrays.fill(coverMatrix[indexInCoverMatrix(row, column, num)], 0);
                        }
                    }
                }
            }
        }
        return coverMatrix;
    }

    private int[][] convertDLXListToGrid(List<DancingNode> answer) {
        int[][] result = new int[SIZE][SIZE];

        for (DancingNode n : answer) {
            DancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.column.name);

            for (DancingNode tmp = n.right; tmp != n; tmp = tmp.right) {
                int val = Integer.parseInt(tmp.column.name);

                if (val < min) {
                    min = val;
                    rcNode = tmp;
                }
            }

            // we get line and column
            int ans1 = Integer.parseInt(rcNode.column.name);
            int ans2 = Integer.parseInt(rcNode.right.column.name);
            int r = ans1 / SIZE;
            int c = ans1 % SIZE;
            // and the affected value
            int num = (ans2 % SIZE) + 1;
            // we affect that on the result grid
            result[r][c] = num;
        }

        return result;
    }

    void solve() {
        int[][] cover = convertInCoverMatrix(board);
        DLX dlx = new DLX(cover);
        dlx.solve();
        try {
        boardSolved = convertDLXListToGrid(dlx.result);
        display(boardSolved); } catch (NullPointerException e) {
            System.out.println("Error! Can't not be solved.");
        }
    }

    // show the final result in command prompt
    void display(int[][] result) {
        System.out.println("SOLVED: ");
        int size = result.length;
        for (int[] aResult : result) {
            StringBuilder ret = new StringBuilder();
            for (int j = 0; j < size; j++) {
                ret.append(aResult[j]).append(" ");
            }
            System.out.println(ret);
        }
        System.out.println();
    }
}
