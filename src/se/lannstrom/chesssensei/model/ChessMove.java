package se.lannstrom.chesssensei.model;

import se.lannstrom.chesssensei.model.Board.Castle;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.Board.ChessPiece;

/**
 * Represents a single chess move. E.g. RA5
 * Immutable.
 *
 * @author x1x
 *
 */
public class ChessMove {
	public enum PromotionPiece {
		QUEEN, KNIGHT, ROOK, BISHOP;

		public ChessPiece getChessPiece(ChessMove m) {
			ChessColor c = m.getColor();
			if (c == ChessColor.WHITE) {
				return ChessPiece.valueOf("W_" + name());
			} else {
				return ChessPiece.valueOf("B_" + name());
			}
		}

		public ChessPiece getChessPiece(ChessColor c) {
			if (c == ChessColor.WHITE) {
				return ChessPiece.valueOf("W_" + name());
			} else {
				return ChessPiece.valueOf("B_" + name());
			}
		}
	}

	/* Ordinary movement */
	private BoardPosition from;
	private BoardPosition to;
	/* Or castling */
	private Castle castling = Castle.NONE;

	private ChessColor color;
	private boolean enPassant;

	private PromotionPiece promotion;

	public ChessMove(ChessMove other) {
		this.from = other.from;
		this.to = other.to;
		this.castling = other.castling;
		this.color = other.color;
		this.enPassant = other.enPassant;
		this.promotion = other.promotion;
	}

	/* TODO: Set from and to */
	public ChessMove(Castle c) {
		castling = c;
		if (castling == Castle.W_KINGSIDE) {
			color = ChessColor.WHITE;
		} else if (castling == Castle.W_QUEENSIDE) {
			color = ChessColor.WHITE;
		} else if (castling == Castle.B_KINGSIDE) {
			color = ChessColor.BLACK;
		} else if (castling == Castle.B_QUEENSIDE) {
			color = ChessColor.BLACK;
		}
	}

	public ChessMove(BoardPosition f, BoardPosition t,
			ChessColor c) {
		from = f;
		to = t;
		color = c;
	}

	public ChessMove(BoardPosition f, BoardPosition t,
			ChessColor c, boolean enPassant) {
		from = f;
		to = t;
		color = c;
		this.enPassant = enPassant;
	}

	public BoardPosition getFrom() {
		return from;
	}

	public BoardPosition getTo() {
		return to;
	}

	public ChessColor getColor() {
		return color;
	}

	public Castle getCastling() {
		return castling;
	}

	public void setCastling(Castle castling) {
		this.castling = castling;
	}

	public boolean isCastle() {
		return castling != Castle.NONE;
	}

	public boolean isEnPassant() {
		return enPassant;
	}

	public PromotionPiece getPromotion() {
		return promotion;
	}

	public void setPromotion(PromotionPiece pp) {
		promotion = pp;
	}

	public void setEnPassant(boolean enPassant) {
		this.enPassant = enPassant;
	}

	public boolean isEqualFromAndTo(ChessMove m) {
		return this.from.equals(m.from) &&
			   this.to.equals(m.to);
	}

	@Override
	public String toString() {
		if (isCastle()) {
			if (castling == Castle.W_KINGSIDE ||
				castling == Castle.B_KINGSIDE) {
				return "0-0";
			} else {
				return "0-0-0";
			}
		} else {
			return "From: " + from.toString() +
				  " To: " + to.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((castling == null) ? 0 : castling.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (enPassant ? 1231 : 1237);
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChessMove other = (ChessMove) obj;
		if (castling != other.castling)
			return false;
		if (color != other.color)
			return false;
		if (enPassant != other.enPassant)
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

}
