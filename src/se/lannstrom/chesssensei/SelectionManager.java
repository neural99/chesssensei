package se.lannstrom.chesssensei;

import java.util.List;

import android.util.Log;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.rules.ChessRuleStrategy;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;

public class SelectionManager {
	private BoardPosition from;
	private BoardPosition to;
	private ChessColor color;
	
	public enum SelectionState {
		FROM, TO;
	}
	
	private SelectionState selectionState;
	private Board board;
	private ChessRuleStrategy chessRuleStrategy = new ChessRuleStrategy();
	private int[][] moves = new int[8][8];
	
	private SelectionDoneCallback doneCallback;
	
	public SelectionManager(ChessColor c, Board board) {
		color = c;
		this.board = board;
	}
	
	public ChessColor getColor() {
		return color;
	}

	public void setColor(ChessColor color) {
		this.color = color;
	}
	
	public BoardPosition getFrom() {
		return from;
	}

	public void setFrom(BoardPosition from) {
		this.from = from;
	}

	public BoardPosition getTo() {
		return to;
	}

	public void setTo(BoardPosition to) {
		this.to = to;
	}

	public SelectionState getSelectionState() {
		return selectionState;
	}

	public void setSelectionState(SelectionState selectionState) {
		this.selectionState = selectionState;
	}
	
	public void setDoneCallback(SelectionDoneCallback doneCallback) {
		this.doneCallback = doneCallback;
	}

	public void select(int x, int y) {
		if (selectionState == SelectionState.FROM) {
			selectFrom(x, y);
		} else if (selectionState == SelectionState.TO) {
			selectTo(x, y);
		}
	}

	private void selectTo(int x, int y) {
		if (moves[x][y] == 1) {
			to = new BoardPosition(x, y);
			done();
		} else if (board.isMineAt(x, y, color)) {
			from = new BoardPosition(x, y);
			updateMovesSelected();
		}
	}

	private void selectFrom(int x, int y) {
		if (board.isMineAt(x, y, color)) {
			from = new BoardPosition(x, y);
			updateMovesSelected();
			setSelectionState(SelectionState.TO);
		}
	}
	
	private void done() {
		Log.d("Selection", "Done");
		doneCallback.selectionDone();
	}
	
	public void reset() {
		from = null;
		zeroMoves();
		setSelectionState(SelectionState.FROM);
	}

	private void zeroMoves() {
		for (int i = 0; i < 8; i++) 
			for (int j = 0; j < 8; j++)
				moves[i][j] = 0;
	}

	private void updateMovesSelected() {
		List<ChessMove> valid = 
				chessRuleStrategy.getValidMoves(board, from, board.getActive());
		zeroMoves();
		for (ChessMove m : valid) {
			BoardPosition to = m.getTo();
			if (to != null) {
				moves[to.getX()][to.getY()] = 1;
			}
		}
	}

	public ChessMove buildMove() {
		return new ChessMove(from, to, color);
	}
	
	public boolean isValidMove(int x, int y) {
		return moves[x][y] == 1;
	}
}
