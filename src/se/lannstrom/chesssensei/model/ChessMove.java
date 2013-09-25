package se.lannstrom.chesssensei.model;

import se.lannstrom.chesssensei.model.Board.ChessColor;

/**
 * Represents a single chess move. E.g. RA5
 * Immutable.
 * 
 * @author x1x
 *
 */
public class ChessMove {
	private BoardPosition from;
	private BoardPosition to;
	private ChessColor color;
	
	public ChessMove(ChessMove other) {
		this.from = other.from;
		this.to = other.to;
		this.color = other.color;
	}
	
	public ChessMove(BoardPosition f, BoardPosition t,
			ChessColor c) {
		from = f;
		to = t;
		color = c;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
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
		if (color != other.color)
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
