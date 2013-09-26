package se.lannstrom.chesssensei.model;

import java.util.Locale;

/**
 * Refers to a square on a chess board. 
 * 
 * Immutable.
 * 
 * TODO: Implement object pool
 * 
 * @author x1x
 *
 */
public class BoardPosition {
	private int x;
	private int y;
	
	public BoardPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public BoardPosition(BoardPosition other) {
		this.x = other.x;
		this.y = other.y;
	}
	
	public BoardPosition(String algebraic) {
		this(parseAlgebraic(algebraic));
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static BoardPosition parseAlgebraic(String algebraic) {
		if (algebraic.length() != 2) {
			throw new IllegalArgumentException("Expects a two characters string e.g. A1");
		}
		
		int x = -1;
		int y = -1;
		
		String lower = algebraic.toUpperCase(Locale.US);
		String col = lower.substring(0, 1);
		String row = lower.substring(1, 2);
		
		char first = col.charAt(0);
		x = (int) first - 'A';
		
		if (x > 7 || x < 0) {
			throw new IllegalArgumentException("Illegal character in algebraic position " + col);
		}
		
		first = row.charAt(0);
		y = 7 - ((int) first - '1');
		
		if (y > 7 || y < 0) {
			throw new IllegalArgumentException("Illegal character in algebraic position " + row);
		}
		
		return new BoardPosition(x, y);
	}
	
	public String toString() {
		return getStringRepresentation(x, y);
	}

	private static String getStringRepresentation(int x, int y) {
		StringBuilder sb = new StringBuilder();
		
		char col = (char) ('A' + x);
		char row = (char) ('1' + (7 - y));
		sb.append(col);
		sb.append(row);
		
		return sb.toString();
	}
	
	public boolean insideBoard(Board b) {
		return getX() >= 0 && getY() >= 0 &&
			   getX() < b.getSize() && getY() < b.getSize();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		BoardPosition other = (BoardPosition) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
}