package se.lannstrom.chesssensei;

import se.lannstrom.chesssensei.model.Board;

public interface ActiveColorChangeListener {
	public void activeColor(Board.ChessColor c);
}
