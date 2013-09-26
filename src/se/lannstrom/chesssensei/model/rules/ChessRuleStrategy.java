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
	
	public static class BishopStrategy implements ChessPieceStrategy {
		
		public static class ChessDiagonal {
			/* In-data */
			BoardPosition start;
			BoardPosition end;
			
			/* Out-data */
			int vX; 
			int vY;
			
			public ChessDiagonal(BoardPosition s, BoardPosition e) {
				start = s;
				end = e;
			}
		}
		
		@Override
		public boolean isValid(Board b, ChessMove move) {
			BoardPosition from = move.getFrom();
			BoardPosition to = move.getTo();
			
			ChessDiagonal d = calcDiagonal(from, to);
			if (d != null) {
				return isEmptyDiagonal(d, b);
			} else {
				return false;
			}
		}

		private boolean isEmptyDiagonal(ChessDiagonal d, Board b) {
			BoardPosition bp = d.start;
			
			while (!bp.equals(d.end)) {
				int nX = bp.getX() + d.vX;
				int nY = bp.getY() + d.vY;
				bp = new BoardPosition(nX, nY);
				
				if (!bp.insideBoard(b)) {
					/* Reached the end of the board */
					return true; 
				}
				
				if (!b.isEmptyAt(bp)) {
					return false;
				}
			}
			
			/* Reached end of diagonal */
			return true;
		}
		
		private ArrayList<ChessMove> enumDiag(ChessDiagonal d, Board b, ChessColor c) {
			ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
			
			BoardPosition bp = d.start;
			
			while (!bp.equals(d.end)) {
				int nX = bp.getX() + d.vX;
				int nY = bp.getY() + d.vY;
				bp = new BoardPosition(nX, nY);
				
				if (!bp.insideBoard(b)) {
					break;
				}
				
				if (b.isEmptyAt(bp)) {
					moves.add(new ChessMove(d.start, bp, c));
				} else { 
					break;
				}
			}
			
			return moves;
		}
		
		private ArrayList<ChessDiagonal> getDiagonals(BoardPosition from, Board b) {
			ArrayList<ChessDiagonal> diagonals = new ArrayList<ChessDiagonal>();
			
			int[] v = {-1, 1};
			for (int i = 0; i < v.length; i++) {
				for (int j = 0; j < v.length; j++) {
					BoardPosition bp = new BoardPosition(from.getX() + v[i], 
														 from.getY() + v[j]);
					
					if (bp.insideBoard(b) && b.isEmptyAt(bp)) {
						ChessDiagonal d = calcDiagonal(from, bp);
						if (d != null) {
							extendDiagonal(d, b);
							diagonals.add(d);
						}
					}
				}
			}
			
			return diagonals;
		}

		private void extendDiagonal(ChessDiagonal d, Board b) {
			BoardPosition bp = d.start;

			while (bp.insideBoard(b)) {
				d.end = bp;
				
				int nX = bp.getX() + d.vX;
				int nY = bp.getY() + d.vY;
				bp = new BoardPosition(nX, nY);
			}
		}

		private ChessDiagonal calcDiagonal(BoardPosition from, BoardPosition to) {
			/* Safe-guard against division by zero */
			if (from.equals(to))
				return null;
			
			int diffX = to.getX() - from.getX();
			int diffY = to.getY() - from.getY();
			
			if (Math.abs(diffX) == Math.abs(diffY)) {
				ChessDiagonal cd = new ChessDiagonal(from, to);
				cd.vX = diffX / Math.abs(diffX);
				cd.vY = diffY / Math.abs(diffY);
				return cd;
			} else {
				return null;
			}
		}

		@Override
		public List<ChessMove> getValid(Board b, BoardPosition from,
				ChessColor color) {
			ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
			
			for (ChessDiagonal d : getDiagonals(from, b)) {
				ArrayList<ChessMove> diagMoves = enumDiag(d, b, color);
				moves.addAll(diagMoves);
			}
			
			return moves;
		}
		
	}
	
	public static class RookStrategy implements ChessPieceStrategy {

		/*
		 * Returns true if movement along the same rank or file and the squares 
		 * between from and to are empty.
		 * 
		 * (non-Javadoc)
		 * @see se.lannstrom.chesssensei.model.rules.ChessPieceStrategy#isValid(se.lannstrom.chesssensei.model.Board, se.lannstrom.chesssensei.model.ChessMove)
		 */
		public boolean isValid(Board b, ChessMove move) {
			int fromX = move.getFrom().getX();
			int fromY = move.getFrom().getY();
			int toX = move.getTo().getX();
			int toY = move.getTo().getY();
			
			if (fromX == toX) {
				int startY = Math.min(fromY, toY);
				int endY = Math.max(fromY, toY);
				return isEmptyFile(b, fromX, startY, endY);
			} else if (fromY == toY) {
				int startX = Math.min(fromX, toX);
				int endX = Math.max(fromX, toX);
				return isEmptyRank(b, fromY, startX, endX);
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
		private boolean isEmptyRank(Board b, int rank, int fromX, int toX) {
			boolean empty = true;
			for (int x = fromX + 1; x < toX; x++) {
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
		private boolean isEmptyFile(Board b, int file, int startY, int endY) {
			boolean empty = true;
			for (int y = startY + 1; y < endY; y++) {
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
