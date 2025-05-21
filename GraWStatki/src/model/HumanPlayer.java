package model;

public class HumanPlayer extends Player {
    @Override
    public int[] makeMove() {
        return new int[]{-1, -1}; // ruch będzie sterowany przez GUI
    }
}
