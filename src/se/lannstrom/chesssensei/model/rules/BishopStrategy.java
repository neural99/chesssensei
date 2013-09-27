package se.lannstrom.chesssensei.model.rules;

import java.util.ArrayList;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessColor;

public class BishopStrategy implements ChessPieceStrategy {
	
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
		
		BishopStrategy.ChessDiagonal d = calcDiagonal(from, to);
		if (d != null) {
			return isEmptyDiagonal(d, b, move.getColor());
		} else {
			return false;
		}
	}

	private boolean isEmptyDiagonal(BishopStrategy.ChessDiagonal d, Board b, ChessColor c) {
		BoardPosition bp = d.start;

		/* Traverse diagonal until we reach a piece or the end of the board */
		while (!bp.equals(d.end)) {
			int nX = bp.getX() + d.vX;
			int nY = bp.getY() + d.vY;
			bp = new BoardPosition(nX, nY);
			
			if (!bp.insideBoard(b)) {
				/* Reached the end of the board */
				return true; 
			}
			
			/* Reached a piece */
			if (!b.isEmptyAt(bp)) {
				break;
			}
		}
		
		if (bp.equals(d.end)) {
			return b.isEmptyOrOpponent(bp, c);
		} else {
			return false;
		}
		
	}
	
	private ArrayList<ChessMove> enumDiag(BishopStrategy.ChessDiagonal d, Board b, ChessColor c) {
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
			} else if (b.isOpponentAt(bp, c)) {
				moves.add(new ChessMove(d.start, bp, c));
				break;
			} else { 
				break;
			}
		}
		
		return moves;
	}
	
	/**
	 * Get the diagonals around the piece like this:
	 * x   x
	 *   B
	 * x   x
	 * 
	 * @param from
	 * @param b
	 * @param c 
	 * @return
	 */
	private ArrayList<BishopStrategy.ChessDiagonal> getDiagonals(BoardPosition from, Board b, ChessColor c) {
		ArrayList<BishopStrategy.ChessDiagonal> diagonals = new ArrayList<BishopStrategy.ChessDiagonal>();
		
		int[] v = {-1, 1};
		for (int i = 0; i < v.length; i++) {
			for (int j = 0; j < v.length; j++) {
				BoardPosition bp = new BoardPosition(from.getX() + v[i], 
													 from.getY() + v[j]);
				
				if (bp.insideBoard(b) && b.isEmptyOrOpponent(bp, c)) {
					BishopStrategy.ChessDiagonal d = calcDiagonal(from, bp);
					if (d != null) {
						extendDiagonal(d, b);
						diagonals.add(d);
					}
				}
			}
		}
		
		return diagonals;
	}

	/**
	 * Extend the diagonal to the end of the board
	 * 
	 * @param d
	 * @param b
	 */
	private void extendDiagonal(BishopStrategy.ChessDiagonal d, Board b) {
		BoardPosition bp = d.start;

		while (bp.insideBoard(b)) {
			d.end = bp;
			
			int nX = bp.getX() + d.vX;
			int nY = bp.getY() + d.vY;
			bp = new BoardPosition(nX, nY);
		}
	}

	private BishopStrategy.ChessDiagonal calcDiagonal(BoardPosition from, BoardPosition to) {
		/* Safe-guard against division by zero */
		if (from.equals(to))
			return null;
		
		int diffX = to.getX() - from.getX();
		int diffY = to.getY() - from.getY();
		
		if (Math.abs(diffX) == Math.abs(diffY)) {
			BishopStrategy.ChessDiagonal cd = new ChessDiagonal(from, to);
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
		
		for (BishopStrategy.ChessDiagonal d : getDiagonals(from, b, color)) {
			ArrayList<ChessMove> diagMoves = enumDiag(d, b, color);
			moves.addAll(diagMoves);
		}
		
		return moves;
	}
	
}