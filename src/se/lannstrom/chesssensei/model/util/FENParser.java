package se.lannstrom.chesssensei.model.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.Board.Castle;
import se.lannstrom.chesssensei.model.Board.ChessPiece;


/**
 * Parser for FEN chess board format.
 * 
 * FEN defines a state of a chess game in progress. 
 * Every variable of the game is captured, so all the information needed to resume the game is
 * stored in the FEN code. 
 * 
 * @author x1x
 *
 */
public class FENParser {

	public class ParseError {
		private String message;
		
		public ParseError(String msg) {
			message = msg;
		}
		
		@Override
		public String toString() {
			return message;
		}
	}

	private ArrayList<ParseError> parseErrors = new ArrayList<ParseError>();
	private Board tmpBoard;
	private BufferedWriter debugWriter;
	private String[] parts;

	private boolean isNumeric(char c) {
		Matcher m = Pattern.compile("[0-9]+").matcher(String.valueOf(c));
		return m.matches();
	}
	
	private ChessPiece[] parseRank(String fenRank) {
		ChessPiece[] rank = new ChessPiece[8];
		
		int strIndex = 0;
		int i = 0;
		while (i < 8 && strIndex < fenRank.length()) {
			char c = fenRank.charAt(strIndex);
			
			/* Is it a skip marker? Or... */
			if (isNumeric(c)) {
				i += Integer.parseInt(String.valueOf(c));
				strIndex++;
				continue;
			}
			/* ... maybe a chess piece? */
			ChessPiece cp = ChessPiece.valueOf(c);
			if (cp == null) {
				addError("Error parsing rank: " + fenRank + ". Unknown character: " + c);
				/* Skip the illegal char */
				i += 1;
				strIndex++;
				continue;
			} 
			
			/* Success */
			rank[i] = cp;
			strIndex++;
			i++;
		}
		
		return rank;
	}
	
	private void addError(String string) {
		parseErrors.add(new ParseError(string));
		try {
			debugWriter.write(string);
			debugWriter.newLine();
			debugWriter.flush();
		} catch (IOException e) {
		}
	}

	public Board parse(String fen, Writer out) {
		tmpBoard = new Board();
		debugWriter = new BufferedWriter(out);
		
		parts = fen.split("\\s+");
		if (parts.length != 6) {
			addError("Not 6 part FEN. Got " + parts.length + " parts");
		}
		
		parsePiecePlacement();
		parseActiveColor();
		parseCastling();
		parseEnPassant();
		parseHalfMoves();
		parseFullMoves();
		
		return tmpBoard;
	}
	
	private void parseFullMoves() {
		String full = getPart(5);
		if (full == null) {
			addError("No full moves in FEN!");
			return;
		}
		
		Matcher m = Pattern.compile("[0-9]+").matcher(full);
		if (!m.matches()) {
			addError("Parsing full moves. Expect a decimal number. Got: " + full);
		} else {
			tmpBoard.setFullMoveCount(Integer.parseInt(full)); 
		}
	}

	private void parseHalfMoves() {
		String half = getPart(4);
		if (half == null) {
			addError("No half moves in FEN!");
			return;
		}
		
		Matcher m = Pattern.compile("[0-9]+").matcher(half);
		if (!m.matches()) {
			addError("Parsing half moves. Expect a decimal number. Got: " + half);
		} else {
			tmpBoard.setHalfMoveCount(Integer.parseInt(half)); 
		}
	}

	private void parseEnPassant() {
		String enPassant = getPart(3);
		if (enPassant == null) {
			addError("No en passant pos in FEN!");
			return;
		}
		
		try {
			if (!"-".equals(enPassant)) {
				BoardPosition bp = BoardPosition.parseAlgebraic(enPassant);
				tmpBoard.setEnPassantTarget(bp);
			}
		} catch (IllegalArgumentException e) {
			addError(e.getMessage());
		}
	}

	private void parseCastling() {
		String cast = getPart(2);
		if (cast == null) {
			addError("No castling info in FEN!");
			return;
		}
		
		for (int i = 0; i < cast.length(); i++) {
			char ch = cast.charAt(i);
			Castle c = Castle.valueOf(ch);
			
			if (c == null) {
				addError("Error parsing available castling. Got " + ch);
			} else {
				if (c == Castle.NONE && i != cast.length() -1) 
					addError("Reading NONE castling available but it is not the last character. Got: " + cast);
				tmpBoard.addAvailableCastle(c);
			}
		}
	}

	private void parseActiveColor() {
		String color = getPart(1);
		if (color == null) {
			addError("No active color in FEN!");
			return;
		}
		
		if (color.length() != 1)
			addError("Error parsing active color. Expected 'w' or 'b'. Got: " + color);
		
		try {
			char ch = color.charAt(0);
			ChessColor ac = ChessColor.valueOf(ch);
			tmpBoard.setActive(ac);
		} catch (IndexOutOfBoundsException e) {
			addError("Error parsing active color. Expected 'w' or 'b'.");
		}
	}

	private String getPart(int i) {
		if (i < parts.length)
			return parts[i];
		else
			return null;
		
	}

	private void parsePiecePlacement() {
		String pieces = getPart(0);
		if (pieces == null) {
			addError("No Piece placement in FEN!");
			return;
		}
		String[] ranks = pieces.split("/");
		int len = ranks.length;
		if (len != 8) {
			addError("Piece placement contains " + len + " ranks!");
		}
		for (int i = 0; i < len; i++) {
			ChessPiece[] rank = parseRank(ranks[i]);
			setRank(rank, i);
		}
	}

	private void setRank(ChessPiece[] pieces, int rank) {
		/* Sanity check */
		if (rank < 8) {
			for (int i = 0; i < 8; i++) {
				ChessPiece cp = pieces[i];
				tmpBoard.setPieceAt(i, rank, cp);
			}
		}
	}
	
}
