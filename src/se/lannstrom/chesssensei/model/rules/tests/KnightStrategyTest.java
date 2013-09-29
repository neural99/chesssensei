package se.lannstrom.chesssensei.model.rules.tests;

import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.rules.KnightStrategy;
import junit.framework.TestCase;

public class KnightStrategyTest extends TestCase {

	private Board startingBoard;
	private KnightStrategy knightStrategy;

	protected void setUp() throws Exception {
		super.setUp();
		
		startingBoard = Board.createStartingBoard();
		knightStrategy = new KnightStrategy();
	}
	
	BoardPosition b1 = new BoardPosition("B1");
	BoardPosition c3 = new BoardPosition("C3");
	BoardPosition c4 = new BoardPosition("C4");

	public void testIsValid() {
		boolean s1 = knightStrategy.isValid(startingBoard, new ChessMove(b1, c3, ChessColor.WHITE));
		assertTrue(s1);
		boolean s2 = knightStrategy.isValid(startingBoard, new ChessMove(b1, c4, ChessColor.WHITE));
		assertFalse(s2);
	}

	public void testGetValid() {
		List<ChessMove> moves1 = knightStrategy.getValid(startingBoard, b1, ChessColor.WHITE);
		assertTrue(moves1.size() == 2);
	}

}
