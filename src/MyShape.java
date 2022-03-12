import java.util.Random;
import java.lang.Math;


public class MyShape {

    enum Tetrominoes { NoShape, ZShape, SShape, LineShape, 
               TShape, SquareShape, LShape, MirroredLShape, 
               CrossShape, TLongShape, SLongShape, SixSquareShape,
               AShape, CShape  };

    private Tetrominoes pieceShape;
    private int coords[][];
    private int[][][] coordsTable;


    public MyShape() {

        coords = new int[6][2];
        setShape(Tetrominoes.NoShape);

    }

    public void setShape(Tetrominoes shape) {

         coordsTable = new int[][][] {
        		 // Normal
            { { 0, 0 } ,  { 0, 0 },  { 0, 0 },  { 0, 0 },  { 0, 0 },  { 0, 0 } },
            { { 0, -1 },  { 0, 0 },  { -1, 0},  { -1, 1},  { 0, 0 },  { 0, 0 } },
            { { 0, -1 },  { 0, 0 },  { 1, 0 },  { 1, 1 },  { 0, 0 },  { 0, 0 } },
            { { 0, -1 },  { 0, 0 },  { 0, 1 },  { 0, 2 },  { 0, 0 },  { 0, 0 } },
            { { -1, 0 },  { 0, 0 },  { 1, 0 },  { 0, 1 },  { 0, 0 },  { 0, 0 } },
            { { 0, 0 } ,  { 1, 0 },  { 0, 1 },  { 1, 1 },  { 0, 0 },  { 0, 0 } },
            { { -1, -1},  { 0, -1},  { 0, 0 },  { 0, 1 },  { 0, 0 },  { 0, 0 } },
            { { 1, -1 },  { 0, -1},  { 0, 0 },  { 0, 1 },  { 0, 0 },  { 0, 0 } },
            	// Hard
            { { -1, 0 },  { 0, 1 },  { 1, 0 },  { 0, -1},  { 0, 0 },  { 0, 0 } },
            { { -1, -1 },  { 0, 1 },  { 1, 1 },  { 0, -1},  { 0, 0 },  { 0, 0 } },
            { {-1, 1},  { 0, -1},  { 0, 0 },  { 0, 1 },  { 1, 1},  { 0, 0 }},
            	// Extreme
            { {-1, 0},  { -1, 1},  { 0, 0 },  { 0, 1 },  { 1, 1},  { 1, 0 }},
            { {-1, 0},  { -1, 1},  { 0, 1 },  { 1, 1 },  { 1, 0},  { 0, 1 }},
            { {-1, -1},  { -1, 1},  { 0, 0 },  { 0, 1 },  { 1, 1},  { 1, -1 }}
        };

        for (int i = 0; i < 6 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;

    }

    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Tetrominoes getShape()  { return pieceShape; }

    public void setRandomShape(int num)
    {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % num + 1;
        Tetrominoes[] values = Tetrominoes.values(); 
        setShape(values[x]);
    }

    public int minX()
    {
      int m = coords[0][0];
      for (int i = 0; i < 6; i++) {
          m = Math.min(m, coords[i][0]);
      }
      return m;
    }


    public int minY() 
    {
      int m = coords[0][1];
      for (int i = 0; i < 6; i++) {
          m = Math.min(m, coords[i][1]);
      }
      return m;
    }

    public MyShape rotateLeft() 
    {
        if (pieceShape == Tetrominoes.SquareShape || pieceShape == Tetrominoes.CrossShape)
            return this;

        MyShape result = new MyShape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 6; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public MyShape rotateRight()
    {
        if (pieceShape == Tetrominoes.SquareShape || pieceShape == Tetrominoes.CrossShape)
            return this;

        MyShape result = new MyShape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 6; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}
