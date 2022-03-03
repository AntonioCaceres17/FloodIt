import java.util.ArrayList;
import java.util.Arrays;
import tester.*;
import javalib.impworld.*;
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
    if (this.color.equals(color)) {
      
    }
    if (this.flooded = true) {
      this.color = color;
    }
    else {
      this.flooded = true;
      this.color = color;
    }
  }
  // EFFECT: Updates this cells neighbors given the flood status
  // and color.
  void updateNeighbors(Color thisColor) { 
    if (this.flooded) {
      if(this.right != null && !right.flooded
          && this.right.color.equals(thisColor)) {
        this.right.flooded = true;
      }
      if(this.bottom != null && !bottom.flooded
          && this.bottom.color.equals(thisColor)) {
        this.bottom.flooded = true;
      }
      if(this.left != null && !left.flooded
          && this.left.color.equals(thisColor)) {
        this.left.flooded = true;
      }
      if(this.top != null && !top.flooded
          && this.top.color.equals(thisColor)) {
        this.top.flooded = true;
      }
    }
  }
  
  // draws this cell and the borders around it. 
  WorldImage drawCell() {
    int cellSize = 20;
    WorldImage cellImage = new RectangleImage(cellSize, cellSize, "solid", this.color);
    return cellImage; 
  }
    
}

class FloodItWorld extends World {
  ArrayList<Cell> board;
  ArrayList<Color> colors = new ArrayList<Color>(
      Arrays.asList(Color.red, Color.blue, Color.green, Color.pink, Color.magenta, Color.yellow));
  int dimension;
  Random rand;

  // main constructor -- to play game
  FloodItWorld(int dimension) {
    this.board = new ArrayList<Cell>();
    this.dimension = dimension;
    this.rand = new Random();
    this.makeGrid();
  }

  // convenience constructor -- to test drawing
  FloodItWorld(ArrayList<Cell> board, Random rand) {
    this.board = board;
    this.dimension = board.size();
    this.rand = rand;
    //new ArrayUtils().setNeighbors(this.board, this.dimension);
  }

  // EFFECT: updates the board
  void makeGrid() {
    for (int row = 0; row < this.dimension; row += 1 ) {
      for (int col = 0; col < this.dimension; col += 1 ) {
        this.board.add(new Cell(col, row, this.getColor(this.colors)));
      }
    }
    this.board.get(0).flooded = true;
    new ArrayUtils().setNeighbors(this.board, dimension);
  }
  
  // generates a random color from the given list of colors
  Color getColor(ArrayList<Color> colors) {
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
  
  // Mouse handler to click on a cell
  // EFFECT: floods the given cell
  public void onMouseClicked(Posn pos) {
    Cell clicked = cellClicked(pos);
    if (clicked == null) {}
    else {
      Cell newCell = board.get(0);
     newCell.color = clicked.color;
     board.set(0, newCell);
      for (Cell c : board) {
      c.updateNeighbors(newCell.color);
   }
    }
  } 
  
  public Cell cellClicked(Posn pos) {
    for (Cell c : board) {
      // Position constant: (50 * 20x,  50 * 20y)
      int x = (50 + 20 * c.x);
      int y = (50 + 20 * c.y);
      if ((x - 10) < pos.x && (x + 10) > pos.x
          && (y - 10) < pos.y && (y + 10) > pos.y) {
        return c;
      }
    }
    return null;
  }

  //Update the world scene
  public void updateGrid() { 
    Color thisCellColor = this.board.get(0).color;
    for(int index = 0; index < (this.board.size()) - 1; index++) {
        if(this.board.get(index).flooded) {
          this.board.get(index).color = thisCellColor;
          this.makeScene();
          this.board.get(index).updateNeighbors(thisCellColor);
          this.makeScene();
        }
      }
    }

  // calls updateGrid onTick.
  public void onTick() {
    updateGrid();
  }
}


class ArrayUtils {
  
  // links all the cells in this grid together
  void setNeighbors(ArrayList<Cell> board, int dimension) {
    for (int i = 0; i < board.size(); i += 1) { 
      Cell check = board.get(i);
      // set left and right
      if (check.x != 0) {
        check.addLeft(board.get(i - 1));
      }
      // set top and bottom
      if (check.y != 0) {
        check.addTop(board.get(i - dimension));
      }
    }
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
  
  ArrayList<Color> colors = new ArrayList<Color>(
      Arrays.asList(Color.red, Color.blue, Color.green, Color.pink, Color.magenta, Color.yellow));
 
  ArrayList<Cell> testGrid;
  FloodItWorld testWorld;
  FloodItWorld testWorld2;
  FloodItWorld testWorld3;

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
    testGrid = new ArrayList<Cell>(
        Arrays.asList(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9));
    testWorld = new FloodItWorld(testGrid, new Random(seed));
    testWorld2 = new FloodItWorld(new ArrayList<Cell>(
        Arrays.asList(cell1)), new Random(seed2));
    testWorld3 = new FloodItWorld(10);
  } 

  void testFloodIt(Tester t) {
    this.initData();
    testWorld3.bigBang(500, 500, 0.5);
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
    t.checkExpect(new FloodItWorld(3).board.size(), 9);
    t.checkExpect(new FloodItWorld(4).board.size(), 16);
  } 
  
  void testGetColor(Tester t) {
    this.initData();
    //System.out.print(colors.get(new Random(seed).nextInt(colors.size())));
    t.checkExpect(testWorld.getColor(colors), Color.pink);
    //System.out.print(colors.get(new Random(seed2).nextInt(colors.size())));
    t.checkExpect(testWorld2.getColor(colors), Color.green);
  }
  
  void testSetNeighbors(Tester t) {
    this.initData();
    t.checkExpect(cell1.top, null);
    t.checkExpect(cell1.bottom, null);
    t.checkExpect(cell1.left, null);
    t.checkExpect(cell1.right, null);
    t.checkExpect(cell5.top, null);
    t.checkExpect(cell5.bottom, null);
    t.checkExpect(cell5.left, null);
    t.checkExpect(cell5.right, null);
    new ArrayUtils().setNeighbors(testGrid, 3);
    t.checkExpect(cell1.top, null);
    t.checkExpect(cell1.bottom, cell4);
    t.checkExpect(cell1.left, null);
    t.checkExpect(cell1.right, cell2);
    t.checkExpect(cell5.top, cell2);
    t.checkExpect(cell5.bottom, cell8);
    t.checkExpect(cell5.left, cell4);
    t.checkExpect(cell5.right, cell6);
  }

  void testMakeScene(Tester t) { 
    this.initData();
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
  }
  
  //Tests the updateGrid method
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