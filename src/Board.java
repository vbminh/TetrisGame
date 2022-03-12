import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	final int BoardWidth = 20;
    final int BoardHeight = 20;

    Timer timer;
    boolean isFallingFinished = false;
    boolean isStarted = false;
    boolean isPaused = false;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    int count = 0;
    int score = 0;
    int propSpeed = 3;
    int numOfShape = 7;
    JLabel statusbar;
    MyShape curPiece;
    MyShape.Tetrominoes[] board;
    Application myApp;

    public Board(Application app, Tetris parent) {
        myApp = app;
        setPreferredSize(new Dimension(300, 600));
        setFocusable(true);
        setBorder(BorderFactory.createEtchedBorder(Color.black, Color.green));
        curPiece = new MyShape();
        timer = new Timer(1500 / propSpeed, this);

        statusbar = parent.getStatusBar();
        board = new MyShape.Tetrominoes[BoardWidth * BoardHeight];
        addKeyListener(new TAdapter());
        clearBoard();
    }

    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    int squareWidth() {
        return (int) getSize().getWidth() / BoardWidth;
    }

    int squareHeight() {
        return (int) getSize().getHeight() / BoardHeight;
    }

    MyShape.Tetrominoes shapeAt(int x, int y) {
        return board[(y * BoardWidth) + x];
    }

    public void start() {
        if (isPaused) {
            return;
        }
        this.grabFocus();
        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();
        statusbar.setText("Game Started!");
    }

    public void pause() {
        if (!isStarted) {
            return;
        }

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            statusbar.setText("Game paused - Press P to continue.");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }

    public void stop() {
        curPiece.setShape(MyShape.Tetrominoes.NoShape);
        timer.stop();
        isStarted = false;
        statusbar.setText("GAME OVER!");
        myApp.tetris.statuspane.setEnabled(true);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g2D);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                MyShape.Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != MyShape.Tetrominoes.NoShape) {
                    drawSquare(g2D, 0 + j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getShape() != MyShape.Tetrominoes.NoShape) {
            for (int i = 0; i < 6; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g2D, 0 + x * squareWidth(),
                        boardTop + (BoardHeight - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
    }

    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
            board[i] = MyShape.Tetrominoes.NoShape;
        }
        repaint();
    }

    private void pieceDropped() {
        for (int i = 0; i < 6; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BoardWidth) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void newPiece() {
        curPiece.setRandomShape(numOfShape);
        curX = BoardWidth / 2 + 1;
        curY = BoardHeight - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {
            stop();
        }
    }

    private boolean tryMove(MyShape newPiece, int newX, int newY) {
        for (int i = 0; i < 6; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) {
                return false;
            }
            if (shapeAt(x, y) != MyShape.Tetrominoes.NoShape) {
                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines() {
        statusbar.setText("On the move...");
        int numFullLines = 0;
        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == MyShape.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    count = 0;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                ++count;
                if (count >= 2) {
                    statusbar.setText(count + " combo!");
                    score += 500 * propSpeed * count;
                } else {
                    statusbar.setText("Good!");
                    score += 500 * propSpeed;
                }
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j) {
                        board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                    }
                }
                myApp.tetris.statuspane.scored(score);
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            isFallingFinished = true;
            curPiece.setShape(MyShape.Tetrominoes.NoShape);
            repaint();
        }
    }

    private void drawSquare(Graphics2D g2D, int x, int y, MyShape.Tetrominoes shape) {
        Color colors[] = {new Color(0, 0, 0), new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0),
            new Color(15, 232, 76), new Color(123, 200, 152),
            new Color(12, 25, 136), new Color(154, 232, 76),
            new Color(100, 200, 150), new Color(70, 125, 136)
        };

        Color color = colors[shape.ordinal()];

        g2D.setColor(color);
        g2D.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g2D.setColor(color.brighter());
        g2D.drawLine(x, y + squareHeight() - 1, x, y);
        g2D.drawLine(x, y, x + squareWidth() - 1, y);

        g2D.setColor(color.darker());
        g2D.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g2D.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    public void setSpeed(int speed) {
        propSpeed = speed;
        statusbar.setText("You need to start game again...");
        timer.stop();
        timer = new Timer(1500 / propSpeed, this);
    }

    class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            if (!isStarted || curPiece.getShape() == MyShape.Tetrominoes.NoShape) {
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P') {
                pause();
                return;
            }

            if (isPaused) {
                return;
            }

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    tryMove(curPiece, curX - 1, curY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(curPiece, curX + 1, curY);
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(curPiece.rotateRight(), curX, curY);
                    break;
                case KeyEvent.VK_UP:
                    tryMove(curPiece.rotateLeft(), curX, curY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
                case 'd':
                    oneLineDown();
                    break;
                case 'D':
                    oneLineDown();
                    break;
            }

        }
    }
}
