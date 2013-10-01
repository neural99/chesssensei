package se.lannstrom.chesssensei.model.rules;

import java.util.ArrayList;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;

public class PawnStrategy implements ChessPieceStrategy {

	@Override
	public boolean isValid(Board b, ChessMove move) {
		List<ChessMove> valid =  getValid(b, move.getFrom(), move.getColor());
		return valid.contains(move);
	}

	@Override
	public List<ChessMove> getValid(Board b, BoardPosition from,
			ChessColor color) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>(); 
		
		int orient = getOrient(color);
		int pawnRank = getPawnRank(b, color);
		
		BoardPosition singleForward = new BoardPosition(from.getX(), from.getY() + orient * 1);
		BoardPosition doubleForward = new BoardPosition(from.getX(), from.getY() + orient * 2);
		BoardPosition rightDiagForward = new BoardPosition(from.getX() + 1, from.getY() + orient * 1);
		BoardPosition leftDiagForward = new BoardPosition(from.getX() - 1, from.getY() + orient * 1);
		
		if (singleForward.insideBoard(b) && b.isEmptyAt(singleForward)) {
			moves.add(new ChessMove(from, singleForward, color));
			
			/* Allow double step move on first move */
			if (from.getY() == pawnRank && b.isEmptyAt(doubleForward)) {
				moves.add(new ChessMove(from, doubleForward, color));
			}
		}
		
		PawnCapture rightCaputre = canCapture(rightDiagForward, orient, b, color);
		if (rightCaputre == PawnCapture.ORDINARY) {
			moves.add(new ChessMove(from, rightDiagForward, color));
		} else if (rightCaputre == PawnCapture.ENPASSANT) {
			moves.add(new ChessMove(from, rightDiagForward, color, true));
		}

		PawnCapture leftCapture = canCapture(leftDiagForward, orient, b, color);
		if (leftCapture == PawnCapture.ORDINARY) {
			moves.add(new ChessMove(from, leftDiagForward, color));
		} else if (leftCapture == PawnCapture.ENPASSANT) {
			moves.add(new ChessMove(from, leftDiagForward, color, true));
		}
		
		return moves;
	}
	
	private int getPawnRank(Board b, ChessColor color) {
		int pawnRank = 0;
		if (color == ChessColor.WHITE) {
			pawnRank = b.getSize() - 2;
		} else {
			pawnRank = 1;
		}
		return pawnRank;
	}

	private int getOrient(ChessColor color) {
		int orient = 0;
		if (color == ChessColor.WHITE) {
			orient = -1;
		} else {
			orient = 1;
		}
		return orient;
	}
	
	private enum PawnCapture {
		ORDINARY, ENPASSANT; 
	}

	public PawnCapture canCapture(BoardPosition diagForward, int orient,
			Board b, ChessColor color) {
		BoardPosition enPassantTarget = b.getEnPassantTarget();
		
		BoardPosition enPassantPawn = null;
		boolean isOpponentEnPassant = false;
		
		if (enPassantTarget != null) {
			enPassantPawn = new BoardPosition(enPassantTarget.getX(),
								  enPassantTarget.getY() + (-1) * orient);
		
			isOpponentEnPassant = enPassantPawn.insideBoard(b) &&
									  b.isOpponentAt(enPassantPawn, color);
		}

		if (diagForward.insideBoard(b)) {
			if (b.isOpponentAt(diagForward, color)) {
				return PawnCapture.ORDINARY;
			} else if (diagForward.equals(enPassantTarget)
					&& isOpponentEnPassant) {
				return PawnCapture.ENPASSANT;
			}
		}
		
		return null;
	}

}
