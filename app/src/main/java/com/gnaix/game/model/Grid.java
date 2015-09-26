package com.gnaix.game.model;

/**
 * 名称: Grid
 * 描述:
 *
 * @author xiangqing.xue
 * @date 15/9/26
 */
public class Grid {
    public Tile[][] field;
    public Tile[][] undoField;
    public Tile[][] bufferField;

    public Grid(int sizeX, int sizeY){
        field  = new Tile[sizeX][sizeY];
        undoField = new Tile[sizeX][sizeY];
        bufferField = new Tile[sizeX][sizeY];
        clearGrid();
        clearUndoGrid();
    }

    /**
     * 清除当前数据
     */
    public void clearGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                field[xx][yy] = null;
            }
        }
    }

    /**
     * 清除上一步数据
     */
    public void clearUndoGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                undoField[xx][yy] = null;
            }
        }
    }
}
