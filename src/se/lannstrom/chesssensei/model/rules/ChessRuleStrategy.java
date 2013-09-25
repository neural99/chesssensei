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
	private HashMap<ChessPiece, ChessPieceStrategy> pieceStrategies = new HashMap<ChessPiece, ChessPieceStrategy>();   
	
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
	
	private static class RookStrategy implements ChessPieceStrategy {

		/*
		 * Returns true if movement along the same rank or file and the squares 
		 * between from and to are empty.
		 * 
		 * (non-Javadoc)
		 * @see se.lannstrom.chesssensei.model.rules.ChessPieceStrategy#isValid(se.lannstrom.chesssensei.model.Board, se.lannstrom.chesssensei.model.ChessMove)
		 */
		public boolean isValid(Board b, ChessMove move) {
			int fromFile = move.getFrom().getY();
			int fromRank = move.getFrom().getX();
			int toFile = move.getTo().getY();
			int toRank = move.getTo().getX();
			
			if (fromFile == toFile) {
				return isEmptyFile(b, fromRank, fromFile, toRank);
			} else if (fromRank == toRank) {
				return isEmptyRank(b, fromRank, fromFile, toFile);
			} else {
				/* No diagonal movement allowed */
				return false;
			}
		}

		/**
		 * Check if rank is empty from fromFile to toFile
		 * 
		 * @param b
		 * @param rank
		 * @param fromFile
		 * @param toFile
		 * @return
		 */
		private boolean isEmptyRank(Board b, int rank, int fromFile, int toFile) {
			boolean empty = true;
			for (int x = fromFile; x <= toFile; x++) {
				empty = empty && b.isEmptyAt(x, rank);
			}
			return empty;
		}

		/**
		 * Check if file is empty from fromRank to toRank
		 * 
		 * @param b
		 * @param file
		 * @param fromRank
		 * @param toRank
		 * @return
		 */
		private boolean isEmptyFile(Board b, int file, int fromRank, int toRank) {
			boolean empty = true;
			for (int y = fromRank; y <= toRank; y++) {
				empty = empty && b.isEmptyAt(file, y);
			}
			return empty;
		}

		@Override
		public List<ChessMove> getValid(Board b, BoardPosition from,
				ChessColor color) {
			ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
			
			List<ChessMove> sameRank = getSameRankMoves(b, from, color);
			moves.addAll(sameRank);
			
			List<ChessMove> sameFile = getSameFileMoves(b, from, color);
			moves.addAll(sameFile);
			
			return moves;
		}

		private List<ChessMove> getSameFileMoves(Board b, BoardPosition from,
				ChessColor color) {
			ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
			int file = from.getX();
			
			for (int y = from.getY() + 1; y < b.getSize() && b.isEmptyAt(file, y); y++) {
				moves.add(new ChessMove(from, new BoardPosition(file, y), color));
			}
			
			for (int y = from.getY() - 1; y >= 0 && b.isEmptyAt(file, y); y--) {
				moves.add(new ChessMove(from, new BoardPosition(file, y), color));
			}
			
			return moves;
		}

		private List<ChessMove> getSameRankMoves(Board b, BoardPosition from,
				ChessColor color) {
			ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
			int rank = from.getY();
			
			for (int x = from.getX() + 1; x < b.getSize() && b.isEmptyAt(x, rank); x++) {
				moves.add(new ChessMove(from, new BoardPosition(x, rank), color));
			}
			for (int x = from.getX() - 1; x >= 0 && b.isEmptyAt(x, rank); x--) {
				moves.add(new ChessMove(from, new BoardPosition(x, rank), color));
			}
			
			return moves;
		}
	}
	
}
