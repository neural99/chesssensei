package se.lannstrom.chesssensei.model;

import java.util.ArrayList;

import se.lannstrom.chesssensei.model.rules.ChessRuleStrategy;

public class ChessProblem {

	private Board board;
	private ChessRuleStrategy chessRuleStrategy;
	private ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
	private int nextMove = 0;
	
	public boolean isDone() {
		return nextMove == moves.size();
	}
	
	public void doNextMove() {
		ChessMove move = moves.get(nextMove++);
		chessRuleStrategy.doMove(board, move);
	}
	
	public boolean isCorrectNextMove(ChessMove m) {
		return moves.get(nextMove).equals(m);
	}
	
	public ChessMove getNextMove() {
		return moves.get(nextMove);
	}
	
}
