package se.lannstrom.chesssensei;

import se.lannstrom.chesssensei.model.Board.ChessColor;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class BoardActivity extends Activity {

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

		setupActiveTextView();
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


}
