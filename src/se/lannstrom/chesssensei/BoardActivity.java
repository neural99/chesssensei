package se.lannstrom.chesssensei;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class BoardActivity extends Activity {

	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_board);
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
		Log.d("Chess","In flipboard");
		BoardView bv = (BoardView) findViewById(R.id.board_view);
		bv.flip();
	}
	
}
