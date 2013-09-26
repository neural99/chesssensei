package se.lannstrom.chesssensei.model.rules.tests;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.rules.ChessRuleStrategy.BishopStrategy;
import se.lannstrom.chesssensei.model.util.FENParser;
import junit.framework.TestCase;

public class BishopStrategyTest extends TestCase {

	private Board startingBoard;
	private Board testBoard1;
	private Board testBoard2;
	private BishopStrategy bishopStrategy;
	private Board centerBoard;

	protected void setUp() throws Exception {
		super.setUp();
		
		startingBoard = Board.createStartingBoard();
		FENParser parser = new FENParser();
		testBoard1 = parser.parse("b7/8/8/8/8/8/8/8 w KQkq - 0 1", new PrintWriter(System.out));
		testBoard2 = parser.parse("1b6/8/8/8/8/8/8/8 w KQkq - 0 1", new PrintWriter(System.out));
		centerBoard = parser.parse("8/8/8/4b3/8/8/8/8 w KQkq - 0 1", new PrintWriter(System.out));
		
		bishopStrategy = new BishopStrategy();
	}

	public void testIsValid() {
		/* Basic test that should fail because the path is blocked */
		boolean a1 = bishopStrategy.isValid(startingBoard, new ChessMove(new BoardPosition(0, 0), new BoardPosition(7, 7), ChessColor.WHITE));
		assertFalse(a1);
		
		/* Basic test that should fail because the squares are not on a diagonal */
		boolean a2 = bishopStrategy.isValid(testBoard1, new ChessMove(new BoardPosition(1, 0), new BoardPosition(7, 7), ChessColor.WHITE));
		assertFalse(a2);
		
		/* Basic test that should pass */
		boolean a3 = bishopStrategy.isValid(testBoard1, new ChessMove(new BoardPosition(0, 0), new BoardPosition(7, 7), ChessColor.WHITE));
		assertTrue(a3);
		
		/* Basic test that should pass */
		boolean a4 = bishopStrategy.isValid(testBoard1, new ChessMove(new BoardPosition(1, 0), new BoardPosition(7, 6), ChessColor.WHITE));
		assertTrue(a4);
	}

	public void testGetValid() {
		BoardPosition a1 = new BoardPosition(0, 0);
		
		List<ChessMove> moves = bishopStrategy.getValid(testBoard1, new BoardPosition(0, 0), ChessColor.WHITE);
		for (int i = 1; i < 8; i++) {
			ChessMove cm = new ChessMove(a1, new BoardPosition(i, i), ChessColor.WHITE);
			assertTrue(moves.contains(cm));
		} 
		
		assertFalse(moves.contains(new ChessMove(a1, a1, ChessColor.WHITE)));
		
		BoardPosition e5 = new BoardPosition("E5");
		
		List<ChessMove> facit = new ArrayList<ChessMove>();
		facit.add(new ChessMove(e5, new BoardPosition("D6"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("C7"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("B8"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("D4"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("C3"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("B2"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("A1"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("F6"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("G7"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("H8"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("F4"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("G3"), ChessColor.WHITE));
		facit.add(new ChessMove(e5, new BoardPosition("H2"), ChessColor.WHITE));
		
		List<ChessMove> moves2 = bishopStrategy.getValid(centerBoard, new BoardPosition("E5"), ChessColor.WHITE);
		for (ChessMove m : facit) {
			moves2.contains(m);
		}
		
		BoardPosition b1 = new BoardPosition("B1");
		List<ChessMove> moves3 = bishopStrategy.getValid(testBoard2, new BoardPosition("B1"), ChessColor.WHITE);
		assertTrue(moves3.contains(new ChessMove(b1, new BoardPosition("A2"), ChessColor.WHITE)));
		assertFalse(moves3.contains(new ChessMove(b1, new BoardPosition("A1"), ChessColor.WHITE)));
		assertFalse(moves3.contains(new ChessMove(b1, new BoardPosition("B1"), ChessColor.WHITE)));
		
	}
	
}
