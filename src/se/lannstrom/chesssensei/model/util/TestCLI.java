package se.lannstrom.chesssensei.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.Castle;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.rules.ChessRuleStrategy;

/**
 * A CLI interface for testing the Chess Rules
 * 
 * @author x1x
 *
 */
public class TestCLI {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Board board = Board.createStartingBoard();
		ChessRuleStrategy crs = new ChessRuleStrategy();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println(board);
			System.out.println("");
			
			ChessMove move = null;
			
			Castle c = readCastle(br, board.getActive());
			if (c == null) {
				BoardPosition from = readBoardPosition(br, "From: ");
				BoardPosition to = readBoardPosition(br, "To: ");
				move = new ChessMove(from, to, board.getActive());
			} else {
				move = new ChessMove(c);
			}
			if (crs.doMove(board, move)) {
				System.out.println("Valid move");
			} else {
				System.out.println("Invalid move");
			}
		}
	}

	private static Castle readCastle(BufferedReader br,
			ChessColor active) throws IOException {
		System.out.println("Castle: ");
		String line = br.readLine();
		if ("0-0".equals(line.trim())) {
			if (active == ChessColor.WHITE) 
				return Castle.W_KINGSIDE;
			else
				return Castle.B_KINGSIDE;
		} else if ("0-0-0".equals(line.trim())) {
			if (active == ChessColor.WHITE) 
				return Castle.W_QUEENSIDE;
			else
				return Castle.B_QUEENSIDE;
		} else {
			return null;
		}
	}

	private static BoardPosition readBoardPosition(BufferedReader br,
			String prompt) throws IOException {
		BoardPosition bp = null;
		while (bp == null) {
			System.out.println(prompt);
			
			String line = br.readLine();
			try {
				bp = new BoardPosition(line.trim());
			} catch (IllegalArgumentException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		return bp;
	}

}
