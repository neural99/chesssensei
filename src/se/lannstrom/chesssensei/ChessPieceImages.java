package se.lannstrom.chesssensei;

import java.util.HashMap;

import se.lannstrom.chesssensei.model.Board;
import se.lannstrom.chesssensei.model.Board.ChessPiece;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ChessPieceImages {

	private HashMap<ChessPiece, Bitmap> bigBitmaps = new HashMap<Board.ChessPiece, Bitmap>();

	private ChessPieceImages(Context c) {
		loadChessPieceImages(c);
	}

	private static ChessPieceImages instance;

	public static ChessPieceImages getInstance(Context c) {
		if (instance == null) {
			instance = new ChessPieceImages(c);
		}
		return instance;
	}

	/**
	 * Load the appropriate chess piece images
	 * @param c
	 */
	private void loadChessPieceImages(Context c) {
		Bitmap tmp = null;

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.bking);
		bigBitmaps.put(ChessPiece.B_KING, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.wking);
		bigBitmaps.put(ChessPiece.W_KING, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.bqueen);
		bigBitmaps.put(ChessPiece.B_QUEEN, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.wqueen);
		bigBitmaps.put(ChessPiece.W_QUEEN, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.brook);
		bigBitmaps.put(ChessPiece.B_ROOK, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.wrook);
		bigBitmaps.put(ChessPiece.W_ROOK, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.bbishop);
		bigBitmaps.put(ChessPiece.B_BISHOP, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.wbishop);
		bigBitmaps.put(ChessPiece.W_BISHOP, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.bknight);
		bigBitmaps.put(ChessPiece.B_KNIGHT, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.wknight);
		bigBitmaps.put(ChessPiece.W_KNIGHT, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.bpawn);
		bigBitmaps.put(ChessPiece.B_PAWN, tmp);

		tmp = BitmapFactory.decodeResource(c.getResources(), R.raw.wpawn);
		bigBitmaps.put(ChessPiece.W_PAWN, tmp);
	}

	public Bitmap getScaledChessPiece(ChessPiece cp, int dstDiag) {
		Bitmap big = bigBitmaps.get(cp);
		Bitmap scaled = Bitmap.createScaledBitmap(big, dstDiag, dstDiag, true);
		return scaled;
	}

}
