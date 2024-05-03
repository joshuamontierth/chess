package chess;

import java.util.Objects;

public class ChessPosition {
    int rowPos;
    int colPos;
    public ChessPosition(int row, int col) {
        rowPos = row;
        colPos = col;
    }
    public int getRow() {
        return rowPos;
    }

    public int getColumn() {
        return colPos;
    }
    public String toString() {
        return "(" + rowPos + ", " + colPos + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return rowPos == that.rowPos && colPos == that.colPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowPos, colPos);
    }
}
