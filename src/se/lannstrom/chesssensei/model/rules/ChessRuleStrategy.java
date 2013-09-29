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
		register(ChessPiece.W_ROOK, new RookStrategy());
		register(ChessPiece.B_ROOK, new RookStrategy());
		
		/* Bishop */
		register(ChessPiece.W_BISHOP, new BishopStrategy());
		register(ChessPiece.B_BISHOP, new BishopStrategy());
		
		/* Knight */
		register(ChessPiece.W_KNIGHT, new KnightStrategy());
		register(ChessPiece.B_KNIGHT, new KnightStrategy());
		
		/* Queen */
		register(ChessPiece.W_QUEEN, new QueenStrategy());
		register(ChessPiece.B_QUEEN, new QueenStrategy());
		
		/* King */
		register(ChessPiece.W_KING, new KingStrategy());
		register(ChessPiece.B_KING, new KingStrategy());
		
		/* Pawn */
		register(ChessPiece.W_PAWN, new PawnStrategy());
		register(ChessPiece.B_PAWN, new PawnStrategy());
	}
	
	public List<ChessMove> getValidMoves(Board b, BoardPosition from, ChessColor c) {
		ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
		List<ChessMove> moves = getMoves(b, from, c);
		for (ChessMove m : moves) {
			if (isValidMove(b, m)) {
				validMoves.add(m);
			}
		}
		return validMoves;
	}
	
	public void doMove(Board b, ChessMove move) {
		if (isValidMove(b, move)) {
			executeMove(b, move);
		}
	}

	private void executeMove(Board b, ChessMove move) {
		/* TODO: Update all status in board */
		ChessPiece cp = b.getPieceAt(move.getFrom());
		if (cp != null && cp.isPawn()) {
			updateEnPassantTarget(b, move);
		}
		
		b.setPieceAt(move.getTo(), cp);
		b.setPieceAt(move.getFrom(), null);
		
		b.toggleActive();
	}
	
	private void updateEnPassantTarget(Board b, ChessMove move) {
		BoardPosition from = move.getFrom();
		BoardPosition to = move.getTo();
		ChessColor color = move.getColor();
		
		boolean isDoubleStep = Math.abs(to.getY() - from.getY()) == 2;
		if (isDoubleStep) {
			int dir = 0;
			if (color == ChessColor.WHITE) {
				dir = -1;
			} else {
				dir = 1;
			}
			BoardPosition singleStep = new BoardPosition(from.getX(), from.getY() + dir);
			b.setEnPassantTarget(singleStep);
		}
	}

	private boolean isValidMove(Board b, ChessMove move) {
		if (b.getActive() == move.getColor() && 
			!move.getFrom().equals(move.getTo())) {
			
			/* Check if activeColor is in check after move */
			Board copy = new Board(b);
			executeMove(copy, move);
			List<ChessMove> opponentMoves = 
					getAllMoves(copy, ChessColor.getOpponent(move.getColor()));

			if (containsKingCapture(copy, move.getColor(), opponentMoves)) {
				return false;
			} else {
				return true;
			}

		} else {
			throw new IllegalArgumentException("ChessMove is not valid. Wrong color is active." +
											   "In Board object active color is: " + b.getActive() + 
											   " and in ChessMove object: " + move.getColor());
		}
	}
	
	private boolean containsKingCapture(Board b, ChessColor c, List<ChessMove> opponentMoves) {
		BoardPosition kingPos = b.findKing(c);
		if (kingPos != null) {
			for (ChessMove move : opponentMoves) {
				if (move.getTo().equals(kingPos)) {
					return true;
				}
			}
		}
		return false;
	}

	private List<ChessMove> getMoves(Board b, BoardPosition from, ChessColor c) {
		ChessPiece cp = b.getPieceAt(from);
		ChessPieceStrategy cps = pieceStrategies.get(cp);
		return cps.getValid(b, from, c);
	}
	
	public List<ChessMove> getAllMoves(Board b, ChessColor c) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				BoardPosition from = new BoardPosition(i, j);
				ChessPiece cp = b.getPieceAt(from);
				if (cp != null && cp.isColor(c)) {
					moves.addAll(getMoves(b, from, c));
				}
			}
		}
		return moves;
	}
	
	public void register(ChessPiece cp, ChessPieceStrategy s) {
		pieceStrategies.put(cp, s);
	}
	
}
