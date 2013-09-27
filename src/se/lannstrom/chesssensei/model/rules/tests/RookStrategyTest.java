package se.lannstrom.chesssensei.model.rules.tests;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.rules.RookStrategy;
import se.lannstrom.chesssensei.model.util.FENParser;
import junit.framework.TestCase;

public class RookStrategyTest extends TestCase {

	private Board testBoard1;
	private FENParser parser;
	private RookStrategy rookStrategy;
	private Board startingBoard;

	protected void setUp() throws Exception {
		super.setUp();
		
		startingBoard = Board.createStartingBoard();
		
		parser = new FENParser();
		testBoard1 = parser.parse("r7/8/8/8/8/8/8/8 w KQkq - 0 1", new PrintWriter(System.out));
		
		rookStrategy = new RookStrategy();
	}

	public void testIsValid() {
		BoardPosition a1 = new BoardPosition("A1");
		BoardPosition a8 = new BoardPosition("A8");
		BoardPosition b1 = new BoardPosition("B1");
		BoardPosition c2 = new BoardPosition("C2");
		BoardPosition h8 = new BoardPosition("H8");
		
		assertTrue(rookStrategy.isValid(testBoard1, new ChessMove(a1, a8, ChessColor.WHITE)));
		assertTrue(rookStrategy.isValid(testBoard1, new ChessMove(a1, a8, ChessColor.WHITE)));
		assertFalse(rookStrategy.isValid(testBoard1, new ChessMove(a1, c2, ChessColor.WHITE)));
		assertFalse(rookStrategy.isValid(testBoard1, new ChessMove(b1, c2, ChessColor.WHITE)));
		assertTrue(rookStrategy.isValid(testBoard1, new ChessMove(a8, a1, ChessColor.WHITE)));
		assertFalse(rookStrategy.isValid(testBoard1, new ChessMove(a1, h8, ChessColor.WHITE)));
		assertTrue(rookStrategy.isValid(testBoard1, new ChessMove(a8, h8, ChessColor.WHITE)));
	}
	
	private BoardPosition p(String s) {
		return new BoardPosition(s);
	}

	public void testGetValid() {
		BoardPosition a1 = p("A1");
		
		ArrayList<ChessMove> facit1 = new ArrayList<ChessMove>();
		facit1.add(new ChessMove(a1, p("B1"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("C1"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("D1"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("E1"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("F1"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("G1"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("H1"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("A2"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("A3"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("A4"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("A5"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("A6"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("A7"), ChessColor.WHITE));
		facit1.add(new ChessMove(a1, p("A8"), ChessColor.WHITE)); /* Capture move */
		List<ChessMove> moves1 = rookStrategy.getValid(testBoard1, a1, ChessColor.WHITE);
		assertTrue(moves1.containsAll(facit1) && facit1.containsAll(moves1));
		
		List<ChessMove> moves2 = rookStrategy.getValid(startingBoard, a1, ChessColor.BLACK);
		assertTrue(moves2.size() == 2);
	} 

}
