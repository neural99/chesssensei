package se.lannstrom.chesssensei.model.util;

import java.io.OutputStreamWriter;

import se.lannstrom.chesssensei.model.Board;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FENParser parser = new FENParser();
		Board b = parser.parse("3r1r1k/1p3p1p/p2p4/4n1NN/6bQ/1BPq4/P3p1PP/1R5K w - - 0 1", new OutputStreamWriter(System.out));
		System.out.println(b);
	}

}
