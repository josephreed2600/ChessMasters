package edu.neumont.chessmasters.controllers;

import edu.neumont.chessmasters.models.*;
import edu.neumont.chessmasters.models.pieces.*;

public class BoardTester {
	
	public static boolean Run(Move[] moves) {
		Board board = new Board();
		return Run(moves, board);
	}
	public static boolean Run(Move[] moves, Board board) {
		System.out.println(board);
		boolean status = true; // ok by default
		for (Move move : moves) {
			status &= board.movePiece(move);
			System.out.println(board);
		System.out.println("+ " + board.isInCheck(PieceColor.WHITE));
		System.out.println("# " + board.isInCheckmate(PieceColor.WHITE));
		}
		return status;
	}
	
	public static void Run() {
		Board board = new Board();
		Run(FoolsMate, board);
		System.out.println("+ " + board.isInCheck(PieceColor.WHITE));
		System.out.println("# " + board.isInCheckmate(PieceColor.WHITE));
	}

	public static final Move[] FoolsMate = {
		new Move("f2", "f3"),
		new Move("e7", "e5"),
		new Move("g2", "g4"),
		new Move("d8", "h4")
	};

}
