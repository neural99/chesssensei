package se.lannstrom.chesssensei.model.rules.test;

import java.util.List;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.rules.ChessRuleStrategy;
import junit.framework.TestCase;

public class ChessRuleStrategyTest extends TestCase {

	private ChessRuleStrategy chessRuleStrategy;
	private Board startingBoard;

	protected void setUp() throws Exception {
		super.setUp();
		
		chessRuleStrategy = new ChessRuleStrategy();
		startingBoard = Board.createStartingBoard();
	}

	BoardPosition a2 = new BoardPosition("A2");
	BoardPosition a7 = new BoardPosition("A7");
	
	public void testGetValidMoves() {
		List<ChessMove> moves1;
		moves1 = chessRuleStrategy.getValidMoves(startingBoard, a2, ChessColor.WHITE);
		assertTrue(moves1.size() == 2);
		
		List<ChessMove> moves2;
		startingBoard.setActive(ChessColor.BLACK);
		moves2 = chessRuleStrategy.getValidMoves(startingBoard, a7, ChessColor.BLACK);
		assertTrue(moves2.size() == 2);
		startingBoard.setActive(ChessColor.WHITE);
	}

	public void testDoMove() {
		fail("Not yet implemented");
	}

}
