package se.lannstrom.chesssensei.model.rules;

import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;

public interface ChessPieceStrategy {
	public boolean isValid(Board b, ChessMove move);
	public List<ChessMove> getValid(Board b, BoardPosition from, ChessColor color);
}
