package se.lannstrom.chesssensei.model.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessPiece;

/**
 * Knows about how the pieces move in chess 
 * @author x1x
 *
 */
public class ChessRuleStrategy {
	private HashMap<ChessPiece, ChessPieceStrategy> pieceStrategies =
			new HashMap<ChessPiece, ChessPieceStrategy>();
	
	public ChessRuleStrategy() {
		setupStandard();
	}
	
	/**
	 * Setup standard rules of chess
	 */
	private void setupStandard() {
		/* Rook */
		RookStrategy rs = new RookStrategy();
		register(ChessPiece.B_ROOK, rs);
		register(ChessPiece.W_ROOK, rs);
	}

	public void doMove(Board b, ChessMove move) {
		/* TODO: Check, en passant, checkmate, etc */

		if (isValidMove(b, move)) {
			ChessPiece cp = b.getPieceAt(move.getFrom());
			b.setPieceAt(move.getTo(), cp);
			b.setPieceAt(move.getFrom(), null);
			b.toggleActive();
		}
	}
	
	public boolean isValidMove(Board b, ChessMove move) {
		/* TODO: Check, en passant, checkmate, etc */
		
		if (b.getActive() == move.getColor()) {
			ChessPiece cp = b.getPieceAt(move.getFrom());
			
			ChessPieceStrategy cps = pieceStrategies.get(cp);
			return cps.isValid(b, move);
			
		} else {
			throw new IllegalArgumentException("ChessMove is not valid. Wrong color is active." +
											   "In Board object active color is: " + b.getActive() + 
											   " and in ChessMove object: " + move.getColor());
		}
	}
	
	public List<ChessMove> getValidMoves(Board b, BoardPosition from, ChessColor c) {
		ChessPiece cp = b.getPieceAt(from);
		ChessPieceStrategy cps = pieceStrategies.get(cp);
		return cps.getValid(b, from, c);
	}
	
	public List<ChessMove> getValidMoves(Board b, ChessColor c) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				BoardPosition from = new BoardPosition(i, j);
				ChessPiece cp = b.getPieceAt(from);
				if (cp.isColor(c)) {
					moves.addAll(getValidMoves(b, from, c));
				}
			}
		}
		return moves;
	}
	
	public void register(ChessPiece cp, ChessPieceStrategy s) {
		pieceStrategies.put(cp, s);
	}
	
}
