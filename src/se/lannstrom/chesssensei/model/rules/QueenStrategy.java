package se.lannstrom.chesssensei.model.rules;

import java.util.ArrayList;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;

public class QueenStrategy implements ChessPieceStrategy {

	private RookStrategy rookStrategy;
	private BishopStrategy bishopStrategy;

	public QueenStrategy() {
		rookStrategy = new RookStrategy();
		bishopStrategy = new BishopStrategy();
	}
	
	@Override
	public boolean isValid(Board b, ChessMove move) {
		return rookStrategy.isValid(b, move) ||
				bishopStrategy.isValid(b, move);
	}

	@Override
	public List<ChessMove> getValid(Board b, BoardPosition from,
			ChessColor color) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		moves.addAll(rookStrategy.getValid(b, from, color));
		moves.addAll(bishopStrategy.getValid(b, from, color));
		return moves;
	}

}
