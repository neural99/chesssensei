package se.lannstrom.chesssensei.model.rules.tests;

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
	BoardPosition a4 = new BoardPosition("A4");
	BoardPosition a6 = new BoardPosition("A6");
	
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
		startingBoard = Board.createStartingBoard();
		System.out.println(startingBoard);
		ChessMove move1 = new ChessMove(a2, a4, ChessColor.WHITE);
		chessRuleStrategy.doMove(startingBoard, move1);
		System.out.println(startingBoard);
		ChessMove move2 = new ChessMove(a7, a6, ChessColor.BLACK);
		chessRuleStrategy.doMove(startingBoard, move2);
		System.out.println(startingBoard);
	}

}
