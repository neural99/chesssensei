package se.lannstrom.chesssensei.model.rules.tests;

import java.io.PrintWriter;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.rules.QueenStrategy;
import se.lannstrom.chesssensei.model.util.FENParser;
import junit.framework.TestCase;

public class QueenStrategyTest extends TestCase {

	private QueenStrategy queenStrategy;
	private Board startingBoard;
	private Board testBoard1;
	private Board testBoard2;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		queenStrategy = new QueenStrategy();
		
		startingBoard = Board.createStartingBoard();
		FENParser parser = new FENParser();
		testBoard1 = parser.parse("q7/8/8/8/8/8/8/8 w KQkq - 0 1", new PrintWriter(System.out));
		testBoard2 = parser.parse("1q6/8/8/8/8/8/8/8 w KQkq - 0 1", new PrintWriter(System.out));
	}

	public void testIsValid() {
		BoardPosition a1 = new BoardPosition("A1");
		BoardPosition a8 = new BoardPosition("A8");
		BoardPosition h1 = new BoardPosition("H1");
		BoardPosition h8 = new BoardPosition("H8");
		BoardPosition c5 = new BoardPosition("C5");
		BoardPosition h7 = new BoardPosition("H7");
		BoardPosition b1 = new BoardPosition("B1");
		
		boolean s1 = queenStrategy.isValid(testBoard1, new ChessMove(a1, a8, ChessColor.WHITE));
		assertTrue(s1);
		boolean s2 = queenStrategy.isValid(testBoard1, new ChessMove(a1, h1, ChessColor.WHITE));
		assertTrue(s2);
		boolean s3 = queenStrategy.isValid(testBoard1, new ChessMove(a1, h8, ChessColor.WHITE));
		assertTrue(s3);
		boolean s4 = queenStrategy.isValid(testBoard1, new ChessMove(a1, c5, ChessColor.WHITE));
		assertFalse(s4);
		
		boolean s5 = queenStrategy.isValid(testBoard2, new ChessMove(a1, c5, ChessColor.WHITE));
		assertFalse(s5);
		boolean s6 = queenStrategy.isValid(testBoard2, new ChessMove(b1, h7, ChessColor.WHITE));
		assertTrue(s6);
	}

	public void testGetValid() {
		BoardPosition a1 = new BoardPosition("A1");
		List<ChessMove> moves1 = queenStrategy.getValid(startingBoard, a1, ChessColor.BLACK);
		assertTrue(moves1.size() == 3);
		
		List<ChessMove> moves2 = queenStrategy.getValid(testBoard1, a1, ChessColor.WHITE);
		assertTrue(moves2.size() == 21);
		
		List<ChessMove> moves3 = queenStrategy.getValid(testBoard1, a1, ChessColor.BLACK);
		assertTrue(moves3.size() == 20);
		assertFalse(moves3.contains(new ChessMove(a1, new BoardPosition("A8"), ChessColor.BLACK)));
	}

}
