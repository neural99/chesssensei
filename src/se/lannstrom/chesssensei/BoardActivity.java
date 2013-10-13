package se.lannstrom.chesssensei;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessColor;
import se.lannstrom.chesssensei.model.Board.GameResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class BoardActivity extends Activity implements SelectionDoneCallback {

	final Context context = this;

	private BoardView boardView;
	private TextView activeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);

		/* Get references to the views */
		boardView = (BoardView) findViewById(R.id.board_view);
		activeTextView = (TextView) findViewById(R.id.active_player);
		
		restoreBoardState(savedInstanceState);
		
		/* Setup done selection callback */
		boardView.getSelectionManager().setDoneCallback(this);

		setupActiveTextView();
	}

	private void restoreBoardState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			Board savedBoard = (Board) savedInstanceState.getParcelable("board");
			if (savedBoard != null) {
				boardView.setBoard(savedBoard);
			}
		}
	}

	/**
	 * Make the text view active_player show whose turn it currently is
	 */
	private void setupActiveTextView() {
		boardView.addActiveColorChangeListener(new ActiveColorChangeListener() {

			@Override
			public void activeColor(ChessColor c) {
				StringBuilder sb = new StringBuilder();
				sb.append(getString(R.string.active_player));
				sb.append(" ");
				if (c == ChessColor.WHITE) {
					sb.append(getString(R.string.white_player));
				} else {
					sb.append(getString(R.string.black_player));
				}

				activeTextView.setText(sb.toString());
				activeTextView.invalidate();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.board_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Show an about dialog
	 */
	public void showAboutDialog(MenuItem item) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		alertDialogBuilder.setMessage("ChessSensei\n\nBy Daniel Lännström\nCopyright 2013\n")
						  .setTitle("About")
						  .setNeutralButton("OK", null);
		alertDialogBuilder.create().show();
	}

	public void flipBoard(View view) {
		BoardView bv = (BoardView) findViewById(R.id.board_view);
		bv.flip();
	}

	@Override
	public void selectionDone() {
		boardView.doMove();
		
		GameResult gr = boardView.getResult();
		if (gr != null) {
			gameOver(gr);
		}
	}

	private void gameOver(GameResult gr) {
		/* TODO: Move this to strings.xml */
		String msg;
		if (gr == GameResult.WHITE_WIN) {
			msg = "White player wins";
		} else if (gr == GameResult.BLACK_WIN) {
			msg = "Black player wins";
		} else {
			msg = "It is a draw";
		}
        new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Game over")
        .setMessage(msg)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            }

        })
        .show();
        
        boardView.disableSelection();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("board", boardView.getBoard());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	endGame();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * End the current game by finishing the activity
	 */
	public void endGame() {
        new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.quit)
        .setMessage(R.string.really_quit)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                BoardActivity.this.finish();    
            }

        })
        .setNegativeButton(R.string.no, null)
        .show();
	}
	
	public void endGame(MenuItem item) {
		endGame();
	}
	
}
