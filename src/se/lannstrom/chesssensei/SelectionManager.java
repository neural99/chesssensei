package se.lannstrom.chesssensei;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.Board.ChessPiece;
import se.lannstrom.chesssensei.model.ChessMove.PromotionPiece;
import se.lannstrom.chesssensei.model.rules.ChessRuleStrategy;
import se.lannstrom.chesssensei.model.BoardPosition;
import se.lannstrom.chesssensei.model.ChessMove;

/* TODO: Pawn promotion and castling. Maybe move confirmation too. */
public class SelectionManager {
	private BoardView boardView;

	private BoardPosition from;
	private BoardPosition to;
	private ChessColor color;

	private PromotionPiece promotion;

	public enum SelectionState {
		FROM, TO;
	}

	private SelectionState selectionState;
	private Board board;
	private ChessRuleStrategy chessRuleStrategy = new ChessRuleStrategy();
	private int[][] moves = new int[8][8];

	private SelectionDoneCallback doneCallback;

	private boolean disabled;

	public SelectionManager(BoardView bv, ChessColor col, Board board) {
		boardView = bv;
		color = col;
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
		if (disabled)
			return;
		
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
		/* Prompt for a promotion piece if needed */
		if (isPromotionMove()) {
			promptForPromotionPieceBeforeCallback();
		} else {
			doneCallback.selectionDone();
		}
	}

	public void reset() {
		from = null;
		zeroMoves();
		setSelectionState(SelectionState.FROM);
	}

	private void zeroMoves() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				moves[i][j] = 0;
			}
		}
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
		ChessMove m = new ChessMove(from, to, color);
		m.setPromotion(promotion);
		return m;
	}

	/**
	 * Returns true if we should display (x, y) in board coordinates as a
	 * valid move target
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isValidMove(int x, int y) {
		return moves[x][y] == 1;
	}

	/**
	 * Do we need to prompt for which piece the user wants to promote to?
	 */
	private boolean isPromotionMove() {
		ChessPiece cp = board.getPieceAt(from);
		if (color == ChessColor.WHITE) {
			return (cp == ChessPiece.W_PAWN) && (to.getY() == 0);
		} else {
			return (cp == ChessPiece.B_PAWN) && (to.getY() == 7);
		}
	}

	/**
	 * Let the use select which piece to promote to
	 */
	private void promptForPromotionPieceBeforeCallback() {
		LayoutInflater inflater = (LayoutInflater)
		boardView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		AlertDialog.Builder builder = new AlertDialog.Builder(boardView.context);

		View view = inflater.inflate(R.layout.promotion_dialog, null);
		builder.setView(view);

		final PromotionSelectionView psv = (PromotionSelectionView)
				view.findViewById(R.id.promotion_view);

		/* Set color to the current moves color */
		psv.setColor(color);

		builder.setPositiveButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				PromotionPiece p = psv.getSelectedPiece();
				if (p != null) {
					Log.d("ChessSensei", p.toString());
					promotion = p;
					dialog.dismiss();

					doneCallback.selectionDone();
				}
			}
		});

		builder.create().show();
	}

	public void disableSelection() {
		disabled = true;
		zeroMoves();
	}
}
