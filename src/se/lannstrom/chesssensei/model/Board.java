package se.lannstrom.chesssensei.model;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a single instance of the board in chess.
 * 
 * Mutable. Contains no information how the game is played, that
 * is handled by {@link ChessRuleStrategy}.
 * 
 * @author x1x
 *
 */
public class Board implements Parcelable {
	public static enum ChessPiece {
		W_PAWN("â™™"), W_BISHOP("â™—"), W_KNIGHT("â™˜"), W_ROOK("â™–"), W_QUEEN("â™•"), W_KING("â™”"),
		B_PAWN("â™Ÿ"), B_BISHOP("â™�"), B_KNIGHT("â™ž"), B_ROOK("â™œ"), B_QUEEN("â™›"), B_KING("â™š");
		
		String text;
		ChessPiece(String t) {
			this.text = t;
		}
		
		@Override
		public String toString() {
			return text;
		}
		
		public static ChessPiece valueOf(char san) {
			switch(san) {
			case 'p': return ChessPiece.B_PAWN;
			case 'r': return ChessPiece.B_ROOK;
			case 'n': return ChessPiece.B_KNIGHT;
			case 'b': return ChessPiece.B_BISHOP;
			case 'q': return ChessPiece.B_QUEEN;
			case 'k': return ChessPiece.B_KING;
			case 'P': return ChessPiece.W_PAWN;
			case 'R': return ChessPiece.W_ROOK;
			case 'N': return ChessPiece.W_KNIGHT;
			case 'B': return ChessPiece.W_BISHOP;
			case 'Q': return ChessPiece.W_QUEEN;
			case 'K': return ChessPiece.W_KING;
			default: return null;
			}
		}

		public boolean isColor(ChessColor c) {
			if (c == ChessColor.BLACK) {
				return name().startsWith("B");
			} else {
				return name().startsWith("W");
			}
		}

		public boolean isPawn() {
			return (this == ChessPiece.B_PAWN || this == ChessPiece.W_PAWN);
		}
	}
	
	public static enum ChessColor {
		WHITE, BLACK;
		
		public static ChessColor valueOf(char c) {
			switch(c) {
			case 'w': return ChessColor.WHITE;
			case 'b': return ChessColor.BLACK;
			default: return null;
			}
		}
		
		/**
		 * Return the other color
		 * @param c
		 * @return
		 */
		public static ChessColor getOpponent(ChessColor c) {
			if (c == ChessColor.BLACK)
				return ChessColor.WHITE;
			else
				return ChessColor.BLACK;
		}
	}
	
	public static enum Castle {
		NONE, W_QUEENSIDE, W_KINGSIDE, B_QUEENSIDE, B_KINGSIDE;
		
		public static Castle valueOf(char c) {
			switch(c) {
			case '-': return Castle.NONE;
			case 'K': return Castle.W_KINGSIDE;
			case 'Q': return Castle.W_QUEENSIDE;
			case 'k': return Castle.B_KINGSIDE;
			case 'q': return Castle.B_QUEENSIDE;
			default: return null;
			}
		}

		public static boolean isKingSide(Castle castling) {
			return castling == B_KINGSIDE || castling == W_KINGSIDE;
		}
		
		public static boolean isQueenSide(Castle castling) {
			return castling == B_QUEENSIDE || castling == W_QUEENSIDE;
		}
	}
	
	public static enum GameResult {
		WHITE_WIN, BLACK_WIN, DRAW;
		
		public static GameResult winner(ChessColor c) {
			if (c == ChessColor.BLACK) {
				return BLACK_WIN;
			} else {
				return WHITE_WIN;
			}
		}
	}
	
	private int size = 8;
	private ChessPiece[][] board;
	private ChessColor active;
	private ArrayList<Castle> availableCastle = new ArrayList<Castle>();
	private BoardPosition enPassantTarget;
	private int halfMoveCount;
	private int fullMoveCount;
	
	public Board(Board other) {
		size = other.size;
		/* Copy board */
		board = new ChessPiece[other.size][other.size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = other.board[i][j];
			}
		}
		active = other.active;
		availableCastle = new ArrayList<Board.Castle>();
		availableCastle.addAll(other.availableCastle);
		enPassantTarget = other.enPassantTarget;
		halfMoveCount = other.halfMoveCount;
		fullMoveCount = other.fullMoveCount;
	}
	
	public Board() {
		board = new ChessPiece[8][8];
	}
	
	public static Board createStartingBoard() {
		Board b = new Board();
		b.active = ChessColor.WHITE;
		
		b.availableCastle.add(Castle.W_KINGSIDE);
		b.availableCastle.add(Castle.B_KINGSIDE);
		b.availableCastle.add(Castle.W_QUEENSIDE);
		b.availableCastle.add(Castle.B_QUEENSIDE);
		
		b.enPassantTarget = null;
		b.halfMoveCount = 0;
		b.fullMoveCount = 0;
		
		b.startingPosition();
		
		return b;
	}
	
	public int getSize() {
		return size;
	}

	public ChessPiece getPieceAt(int x, int y) {
		return board[x][y];
	}
	
	public ChessPiece getPieceAt(BoardPosition bp) {
		return getPieceAt(bp.getX(), bp.getY());
	}
	
	public void setPieceAt(int x, int y, ChessPiece p) {
		board[x][y] = p;
	}
	
	public void setPieceAt(BoardPosition bp, ChessPiece cp) {
		setPieceAt(bp.getX(), bp.getY(), cp);
	}
	
	public ChessColor getActive() {
		return active;
	}

	public void setActive(ChessColor active) {
		this.active = active;
	}

	public ArrayList<Castle> getAvailableCastle() {
		return availableCastle;
	}
	
	public void addAvailableCastle(Castle c) {
		availableCastle.add(c);
	}
	
	public void removeAvailableCastle(Castle c) {
		availableCastle.remove(c);
	}

	public BoardPosition getEnPassantTarget() {
		return enPassantTarget;
	}

	public void setEnPassantTarget(BoardPosition enPassantTarget) {
		this.enPassantTarget = enPassantTarget;
	}

	public int getHalfMoveCount() {
		return halfMoveCount;
	}

	public void setHalfMoveCount(int halfMoveCount) {
		this.halfMoveCount = halfMoveCount;
	}

	public int getFullMoveCount() {
		return fullMoveCount;
	}

	public void setFullMoveCount(int fullMoveCount) {
		this.fullMoveCount = fullMoveCount;
	}

	@Override
	public String toString() {
		return getReadableBoard(false);
	}

	public String getReadableBoard(boolean onlyBoard) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ChessPiece cp = board[j][i];
				if (cp == null) {
					sb.append(".");
				} else {
					sb.append(cp.toString());
				}
			}
			sb.append("\n");
		}
		if (!onlyBoard) {
			sb.append("Active: " + active.name() + "\n");
			sb.append("Castle: " + getCastleString() + "\n");
			sb.append("En Passant: " + enPassantTarget + "\n");
			sb.append("Half move: " + halfMoveCount + "\n");
			sb.append("Full move: " + fullMoveCount + "\n");
		}
		return sb.toString();
	}
	
	private String getCastleString() {
		StringBuilder sb = new StringBuilder();
		for (Castle c : availableCastle) {
			sb.append(c.name());
			sb.append(" ");
		}
		return sb.toString();
	}

	private void startingPosition() {
		/* Black back row */
		setPieceAt(0, 0, ChessPiece.B_ROOK);
		setPieceAt(1, 0, ChessPiece.B_KNIGHT);
		setPieceAt(2, 0, ChessPiece.B_BISHOP);
		setPieceAt(3, 0, ChessPiece.B_QUEEN);
		setPieceAt(4, 0, ChessPiece.B_KING);
		setPieceAt(5, 0, ChessPiece.B_BISHOP);
		setPieceAt(6, 0, ChessPiece.B_KNIGHT);
		setPieceAt(7, 0, ChessPiece.B_ROOK);
		
		/* Black front row */
		setPieceAt(0, 1, ChessPiece.B_PAWN);
		setPieceAt(1, 1, ChessPiece.B_PAWN);
		setPieceAt(2, 1, ChessPiece.B_PAWN);
		setPieceAt(3, 1, ChessPiece.B_PAWN);
		setPieceAt(4, 1, ChessPiece.B_PAWN);
		setPieceAt(5, 1, ChessPiece.B_PAWN);
		setPieceAt(6, 1, ChessPiece.B_PAWN);
		setPieceAt(7, 1, ChessPiece.B_PAWN);
		
		/* White front row */
		setPieceAt(0, 6, ChessPiece.W_PAWN);
		setPieceAt(1, 6, ChessPiece.W_PAWN);
		setPieceAt(2, 6, ChessPiece.W_PAWN);
		setPieceAt(3, 6, ChessPiece.W_PAWN);
		setPieceAt(4, 6, ChessPiece.W_PAWN);
		setPieceAt(5, 6, ChessPiece.W_PAWN);
		setPieceAt(6, 6, ChessPiece.W_PAWN);
		setPieceAt(7, 6, ChessPiece.W_PAWN);
		
		/* White back row */
		setPieceAt(0, 7, ChessPiece.W_ROOK);
		setPieceAt(1, 7, ChessPiece.W_KNIGHT);
		setPieceAt(2, 7, ChessPiece.W_BISHOP);
		setPieceAt(3, 7, ChessPiece.W_QUEEN);
		setPieceAt(4, 7, ChessPiece.W_KING);
		setPieceAt(5, 7, ChessPiece.W_BISHOP);
		setPieceAt(6, 7, ChessPiece.W_KNIGHT);
		setPieceAt(7, 7, ChessPiece.W_ROOK);
	}

	public void toggleActive() {
		if (active == ChessColor.BLACK) {
			active = ChessColor.WHITE; 
		} else {
			active = ChessColor.BLACK;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result
				+ ((availableCastle == null) ? 0 : availableCastle.hashCode());
		result = prime * result + Arrays.hashCode(board);
		result = prime * result
				+ ((enPassantTarget == null) ? 0 : enPassantTarget.hashCode());
		result = prime * result + fullMoveCount;
		result = prime * result + halfMoveCount;
		result = prime * result + size;
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
		Board other = (Board) obj;
		if (active != other.active)
			return false;
		if (availableCastle == null) {
			if (other.availableCastle != null)
				return false;
		} else if (!availableCastle.equals(other.availableCastle))
			return false;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		if (enPassantTarget == null) {
			if (other.enPassantTarget != null)
				return false;
		} else if (!enPassantTarget.equals(other.enPassantTarget))
			return false;
		if (fullMoveCount != other.fullMoveCount)
			return false;
		if (halfMoveCount != other.halfMoveCount)
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	public boolean isEmptyAt(int x, int y) {
		return getPieceAt(x, y) == null;
	}
	
	public boolean isEmptyAt(BoardPosition bp) {
		return getPieceAt(bp) == null;
	}
	
	public boolean isEmptyAt(String bp) {
		return getPieceAt(new BoardPosition(bp)) == null;
	}

	/**
	 * Returns true if the chess piece at bp is an chess piece of 
	 * the opponent to ChessColor c.
	 * 
	 * @param bp
	 * @param c
	 * @return
	 */
	public boolean isOpponentAt(BoardPosition bp, ChessColor c) {
		ChessPiece cp = getPieceAt(bp);
		if (cp != null) {
			return cp.isColor(ChessColor.getOpponent(c));
		} else {
			return false;
		}
	}
	
	public boolean isOpponentAt(int x, int y, ChessColor c) {
		BoardPosition bp = new BoardPosition(x, y);
		return isOpponentAt(bp, c);
	}

	public boolean isEmptyOrOpponent(BoardPosition bp, ChessColor c) {
		return isEmptyAt(bp) || isOpponentAt(bp, c); 
	}
	
	public boolean isEmptyOrOpponent(int x, int y, ChessColor c) {
		BoardPosition bp = new BoardPosition(x, y);
		return isEmptyAt(bp) || isOpponentAt(bp, c); 
	}
	
	public boolean isMineAt(int x, int y, ChessColor c) {
		ChessPiece cp = getPieceAt(x, y);
		if (cp != null) {
			return cp.isColor(c);
		} else {
			return false;
		}
	}

	/**
	 * Find the BoardPosition of the king with color c
	 * 
	 * @param c
	 * @return
	 */
	public BoardPosition findKing(ChessColor c) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (c == ChessColor.WHITE) {
					if (board[i][j] == ChessPiece.W_KING) {
						return new BoardPosition(i, j);
					}
				} else {
					if (board[i][j] == ChessPiece.B_KING) {
						return new BoardPosition(i, j);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Replace the piece at to with the piece at from, i.e. move the
	 * piece.
	 * 
	 * @param from
	 * @param to
	 */
	public void movePiece(BoardPosition from, BoardPosition to) {
		ChessPiece cp = getPieceAt(from);
		setPieceAt(to, cp);
		setPieceAt(from, null);
	}

	public static BoardPosition getStartingKingPos(ChessColor c) {
		if (c == ChessColor.WHITE)
			return new BoardPosition("E1");
		else
			return new BoardPosition("E8");
	}

	public boolean isKingAt(BoardPosition bp, ChessColor c) {
		if (c == ChessColor.WHITE)
			return getPieceAt(bp) == ChessPiece.W_KING;
		else
			return getPieceAt(bp) == ChessPiece.B_KING;
			
	}

	public void setEmptyAt(int x, int y) {
		board[x][y] = null;
	}

	public void setEmptyAt(BoardPosition bp) {
		setEmptyAt(bp.getX(), bp.getY());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(size);
		for (int i = 0; i < 8; i++) {
			out.writeArray(board[i]);
		}
		out.writeSerializable(active);
		out.writeList(availableCastle);
		if (enPassantTarget != null) {
			out.writeString(enPassantTarget.toString());
		} else {
			out.writeString("");
		}
		out.writeInt(halfMoveCount);
		out.writeInt(fullMoveCount);
	}

	public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
		public Board createFromParcel(Parcel in) {
			return new Board(in);
		}

		public Board[] newArray(int size) {
			return new Board[size];
		}
	};
	
    private Board(Parcel in) {
    	size = in.readInt();
    	for (int i = 0; i < 8; i++) {
    		board[i] = (ChessPiece[]) in.readArray(null);
    	}
    	active = (ChessColor) in.readSerializable();
    	in.readList(availableCastle, null);
    	String target = in.readString();
    	if (!target.equals("")) {
    		enPassantTarget = new BoardPosition(target);
    	}
    	halfMoveCount = in.readInt();
    	fullMoveCount = in.readInt();
    }

	public void increaseHalfMoveCount() {
		halfMoveCount++;
	}

	public void resetHalfMoveCount() {
		halfMoveCount = 0;
	}

}
