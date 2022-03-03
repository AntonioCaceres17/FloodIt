import java.util.ArrayList;
import java.util.Arrays;
import tester.*;
import javalib.impworld.*;
import javalib.worldcanvas.WorldCanvas;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;


// represents a single square of the game area
class Cell {
  int x; 
  int y;
  Color color;  
  boolean flooded;
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = false;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }

  // EFFECT: change the top cell of this cell to the given cell
  void addTop(Cell topCell) {
    this.top = topCell;
    topCell.bottom = this;
  }

  // EFFECT: change the left cell of this cell to the given cell
  void addLeft(Cell leftCell) {
    this.left = leftCell;
    leftCell.right = this;
  }

  // EFFECT: flood this cell and change the color to the given color
  void floodCell(Color color) {
    this.flooded = true;
    this.color = color;
  }

  // draws this cell
  WorldImage drawCell() {
    int cellSize = 20;
    return new RectangleImage(cellSize, cellSize, "solid", this.color);
  }
}

class FloodItWorld extends World {
  ArrayList<Cell> board;
  ArrayList<Color> colors = new ArrayList<Color>(
      Arrays.asList(Color.red, Color.blue, Color.green, Color.pink, Color.magenta, Color.yellow));
  int dimension;
  // int colorsNum;
  Random rand;

  // main constructor -- to play game
  FloodItWorld(int dimension) {
    this.dimension = dimension;
    this.rand = new Random();
    makeGrid();
  }
  
  // convenience constructor -- to test
  FloodItWorld(ArrayList<Cell> board, Random rand) {
    this.board = board;
    this.dimension = board.size();
    this.rand = rand;
  }

  // EFFECT: updates the board in this world
  void makeGrid() {
    this.board = new ArrayList<Cell>();
    for (int row = 0; row < this.dimension; row += 1 ) {
      for (int col = 0; col < this.dimension; col += 1 ) {
        this.board.add(new Cell(col, row, this.getColor()));
      }
    }
    // sets neighbors
    for (int i = 0; i < board.size(); i += 1) { 
      Cell check = board.get(i);
      // set top and bottom
      if (check.y != 0) {
        check.addTop(board.get(i - this.dimension));
      }
      // set left and right
      if (check.x != 0) {
        check.addLeft(board.get(i - 1));
      }
    }
  }
  
  // generates a random color from the default list of colors
  Color getColor() {
    return colors.get(rand.nextInt(colors.size()));
  }
  
  // draws the board
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(500, 500);
    for (Cell c : this.board) {
      scene.placeImageXY(c.drawCell(), 50 + 20 * c.x, 50 + 20 * c.y);
    }
    return scene; 
  }
}


class ExamplesFloodIt {
  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;
  Cell cell5;
  Cell cell6;
  Cell cell7;
  Cell cell8;
  Cell cell9;

  int seed = 1;
  int seed2 = 3;
 
  ArrayList<Cell> testGrid = new ArrayList<Cell>(
      Arrays.asList(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9));
  World testWorld = new FloodItWorld(testGrid, new Random(seed));

  void initData() {
    cell1 = new Cell(0, 0, Color.pink);
    cell2 = new Cell(1, 0, Color.yellow);
    cell3 = new Cell(2, 0, Color.blue);
    cell4 = new Cell(0, 1, Color.gray);
    cell5 = new Cell(1, 1, Color.green);
    cell6 = new Cell(2, 1, Color.red);
    cell7 = new Cell(0, 2, Color.blue);
    cell8 = new Cell(1, 2, Color.lightGray);
    cell9 = new Cell(2, 2, Color.magenta);
  } 
  
  void testFloodIt(Tester t) {
    this.initData();
    testWorld.bigBang(500, 500);
  }

  void testAddTop(Tester t) {
    this.initData();
    t.checkExpect(cell1.bottom, null);
    t.checkExpect(cell4.top, null);
    cell4.addTop(cell1); 
    t.checkExpect(cell1.bottom, cell4);
    t.checkExpect(cell4.top, cell1);
  }

  void testAddLeft(Tester t) {
    this.initData();
    t.checkExpect(cell7.right, null);
    t.checkExpect(cell8.left, null);
    cell8.addLeft(cell7);
    t.checkExpect(cell7.right, cell8);
    t.checkExpect(cell8.left, cell7);
  }

  void testAddTopAndLeft(Tester t) {
    this.initData();
    t.checkExpect(cell5.top, null);
    t.checkExpect(cell5.bottom, null);
    t.checkExpect(cell5.left, null);
    t.checkExpect(cell5.right, null);
    cell2.addLeft(cell1);
    cell3.addLeft(cell2);
    cell4.addTop(cell1);
    cell5.addTop(cell2);
    cell5.addLeft(cell4);
    cell6.addTop(cell3);
    cell6.addLeft(cell5);
    cell7.addTop(cell4);
    cell8.addTop(cell5);
    cell8.addLeft(cell7);
    cell9.addTop(cell6);
    cell9.addLeft(cell8);
    t.checkExpect(cell5.top, cell2);
    t.checkExpect(cell5.bottom, cell8);
    t.checkExpect(cell5.left, cell4);
    t.checkExpect(cell5.right, cell6);
  }

  void testFloodCell(Tester t) {
    this.initData();
    t.checkExpect(cell1.flooded, false);
    t.checkExpect(cell1.color, Color.pink);
    cell4.floodCell(Color.magenta);
    t.checkExpect(cell4.flooded, true);
    t.checkExpect(cell4.color, Color.magenta);
  }
  
  void testDrawCell(Tester t) {
    this.initData();
    t.checkExpect(cell1.drawCell(), new RectangleImage(20, 20, "solid", Color.pink));
    t.checkExpect(cell2.drawCell(), new RectangleImage(20, 20, "solid", Color.yellow));
    t.checkExpect(cell3.drawCell(), new RectangleImage(20, 20, "solid", Color.blue));
  }

  void testMakeGrid(Tester t) {
    this.initData();
  } 

  void testMakeScene(Tester t) { 
    this.initData();
    WorldCanvas Canvas = new WorldCanvas(300, 300);
    WorldScene scene = new WorldScene(500, 500); 
    scene.placeImageXY(cell1.drawCell(), 50, 50);
    scene.placeImageXY(cell2.drawCell(), 70, 50);
    scene.placeImageXY(cell3.drawCell(), 90, 50);
    scene.placeImageXY(cell4.drawCell(), 50, 70);
    scene.placeImageXY(cell5.drawCell(), 70, 70);
    scene.placeImageXY(cell6.drawCell(), 90, 70);
    scene.placeImageXY(cell7.drawCell(), 50, 90);
    scene.placeImageXY(cell8.drawCell(), 70, 90);
    scene.placeImageXY(cell9.drawCell(), 90, 90);
    t.checkExpect(testWorld.makeScene(), scene);
    Canvas.drawScene(testWorld.makeScene());
    Canvas.show();
  }
  
  void testUpdateGrid(Tester t) { 
    this.initData();
    Color FloodColor = this.testWorld3.board.get(0).color;
    //Checks if the other indexes are false other than the top left cell
    t.checkExpect(this.testWorld3.board.get(1).flooded, false);
    testWorld3.board.get(1).flooded = true;
    testWorld3.updateGrid();
    t.checkExpect(this.testWorld3.board.get(1).color, FloodColor);
  }
  
}