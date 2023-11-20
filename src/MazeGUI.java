import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MazeGUI extends JFrame {

    private int[][] maze;
    private int targetX, targetY;

    public MazeGUI(int[][] maze) {
        this.maze = maze;

        setTitle("Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        MazePanel mazePanel = new MazePanel();
        add(mazePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        findShortestPath();
    }

    private void findShortestPath() {
        List<Integer> path = new ArrayList<>();
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        // Find the start and target positions
        int startX = -1, startY = -1;
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                if (maze[y][x] == 'S') {
                    startX = x;
                    startY = y;
                } else if (maze[y][x] == 'T') {
                    targetX = x;
                    targetY = y;
                }
            }
        }

        if (findShortestPathUtil(startX, startY, visited, path)) {
            markPath(path);
        }
    }

    private boolean findShortestPathUtil(int x, int y, boolean[][] visited, List<Integer> path) {
        if (x == targetX && y == targetY) {
            return true;
        }

        if (isValidMove(x, y, visited)) {
            visited[y][x] = true;
            path.add(x);
            path.add(y);

            if (findShortestPathUtil(x + 1, y, visited, path) ||
                    findShortestPathUtil(x - 1, y, visited, path) ||
                    findShortestPathUtil(x, y + 1, visited, path) ||
                    findShortestPathUtil(x, y - 1, visited, path)) {
                return true;
            }

            path.remove(path.size() - 1);
            path.remove(path.size() - 1);
        }

        return false;
    }

    private boolean isValidMove(int x, int y, boolean[][] visited) {
        return (x >= 0 && x < maze[0].length && y >= 0 && y < maze.length &&
                maze[y][x] != '1' && !visited[y][x]);
    }

    private void markPath(List<Integer> path) {
        for (int i = 0; i < path.size(); i += 2) {
            int x = path.get(i);
            int y = path.get(i + 1);
            
            maze[y][x] = Color.BLUE.getRGB();
        }
        repaint();
    }

    class MazePanel extends JPanel {
        private static  int CELL_SIZE = 30;
        private static  int MAZE_PADDING = 50;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[0].length; x++) {
                    int cellValue = maze[y][x];
                    Color color;
                    if (cellValue == '1') {
                        color = Color.BLACK;
                    } else if (cellValue == '0') {
                        color = Color.WHITE;
                    } else if (cellValue == 'S'){
                        color = Color.RED;
                    }else if (cellValue == 'T'){
                        color = Color.GREEN;
                    }
                    else {
                        color = new Color(cellValue);
                    }
                    g.setColor(color);
                    g.fillRect(x * CELL_SIZE + MAZE_PADDING, y * CELL_SIZE + MAZE_PADDING, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * CELL_SIZE + MAZE_PADDING, y * CELL_SIZE + MAZE_PADDING, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            int width = maze[0].length * CELL_SIZE + MAZE_PADDING * 2;
            int height = maze.length * CELL_SIZE + MAZE_PADDING * 2;
            return new Dimension(width, height);
        }
    }

    public static void main(String[] args) {
        int[][] maze = {
                {'1', '1', '1', '1', '1', '1', '1', '1', '1', '1'},
                {'1', 'S', '0', '0', '1', '0', '1', '0', '0', '1'},
                {'1', '0', '1', '0', '0', '0', '1', '0', '1', '1'},
                {'1', '0', '1', '0', '0', '0', 'T', '0', '1', '1'},
                {'1', '0', '0', '0', '1', '0', '0', '0', '0', '1'},
                {'1', '1', '1', '1', '1', '1', '1', '1', '0', '1'},
                {'1', '0', '0', '0', '0', '0', '0', '0', '1', '1' },
                {'1', '1', '1', '1', '1', '1', '1', '1', '1', '1'}
        };

        SwingUtilities.invokeLater(() -> new MazeGUI(maze));
    }
}
