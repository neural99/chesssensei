package se.lannstrom.chesssensei.model.rules;

import java.util.ArrayList;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessColor;

public class RookStrategy implements ChessPieceStrategy {

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
		ChessColor color = move.getColor();
		
		if (fromX == toX) {
			int startY = Math.min(fromY, toY);
			int endY = Math.max(fromY, toY);
			return isEmptyFile(b, fromX, startY, endY, color);
		} else if (fromY == toY) {
			int startX = Math.min(fromX, toX);
			int endX = Math.max(fromX, toX);
			return isEmptyRank(b, fromY, startX, endX, color);
		} else {
			/* No diagonal movement allowed */
			return false;
		}
	}

	/**
	 * Check if rank is empty from fromFile to toFile inclusively or 
	 * there is an opponent piece at (toX, rank) square
	 * 
	 * @param b
	 * @param rank
	 * @param startX
	 * @param endX
	 * @param c 
	 * @return
	 */
	private boolean isEmptyRank(Board b, int rank, int startX, int endX, ChessColor c) {
		boolean empty = true;
		for (int x = startX + 1; x < endX; x++) {
			empty = empty && b.isEmptyAt(x, rank);
		}
		return empty && b.isEmptyOrOpponent(endX, rank, c);
	}

	/**
	 * Check if file is empty from fromRank to toRank
	 * 
	 * @param b
	 * @param file
	 * @param c 
	 * @param fromRank
	 * @param toRank
	 * @return
	 */
	private boolean isEmptyFile(Board b, int file, int startY, int endY, ChessColor c) {
		boolean empty = true;
		for (int y = startY + 1; y < endY; y++) {
			empty = empty && b.isEmptyAt(file, y);
		}
		return empty && b.isEmptyOrOpponent(file, endY, c);
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
		
		int y = 0;
		for (y = from.getY() + 1; y < b.getSize() && b.isEmptyAt(file, y); y++) {
			moves.add(new ChessMove(from, new BoardPosition(file, y), color));
		}
		/* Add if first reached chess piece is an opponent */
		if (y < b.getSize() && b.isOpponentAt(file, y, color)) {
			moves.add(new ChessMove(from, new BoardPosition(file, y), color));
		}
		
		for (y = from.getY() - 1; y >= 0 && b.isEmptyAt(file, y); y--) {
			moves.add(new ChessMove(from, new BoardPosition(file, y), color));
		}
		/* Add if first reached chess piece is an opponent */
		if (y >= 0 && b.isOpponentAt(file, y, color)) {
			moves.add(new ChessMove(from, new BoardPosition(file, y), color));
		}
		
		return moves;
	}

	private List<ChessMove> getSameRankMoves(Board b, BoardPosition from,
			ChessColor color) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		int rank = from.getY();
		
		int x = 0;
		for (x = from.getX() + 1; x < b.getSize() && b.isEmptyAt(x, rank); x++) {
			moves.add(new ChessMove(from, new BoardPosition(x, rank), color));
		}
		/* Add if first reached chess piece is an opponent */
		if (x < b.getSize() && b.isOpponentAt(x, rank, color)) {
			moves.add(new ChessMove(from, new BoardPosition(x, rank), color));
		}
		
		for (x = from.getX() - 1; x >= 0 && b.isEmptyAt(x, rank); x--) {
			moves.add(new ChessMove(from, new BoardPosition(x, rank), color));
		}
		/* Add if first reached chess piece is an opponent */
		if (x >= 0 && b.isOpponentAt(x, rank, color)) {
			moves.add(new ChessMove(from, new BoardPosition(x, rank), color));
		}
		
		return moves;
	}
}