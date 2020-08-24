package edu.neumont.chessmasters.controllers;

import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.Move;
import edu.neumont.chessmasters.models.GameSettings;
import edu.neumont.chessmasters.models.pieces.Piece;
import edu.neumont.chessmasters.models.pieces.PieceColor;
import edu.neumont.chessmasters.exceptions.IncompleteMoveException;
import me.travja.utils.utils.IOUtils;

import java.io.EOFException;
import java.util.regex.Pattern;

public class PlayerMove {

    private        Board      board;
    private static PlayerMove inst;
		// Half-turns, starting at 0. To get turn number, (counter/2) + 1
    private        int        counter  = -1;
    private        String     status   = null;
    private        boolean    gameOver = false;
		private        GameSettings options;
    public         String     positionOne;
    public         String     positionTwo;

		public String getColorName() { return getColorName(counter); }
		public static String getColorName(int halfturn) { return halfturn % 2 == 0 ? "White" : "Black"; }

		public PieceColor getColor() { return getColor(counter); }
		public static PieceColor getColor(int halfturn) { return halfturn % 2 == 0 ? PieceColor.WHITE : PieceColor.BLACK; }

		public int getTurn() { return getTurn(counter); }
		public static int getTurn(int halfturn) { return halfturn / 2 + 1; }

		public boolean isWhite() { return isWhite(counter); }
		public static boolean isWhite(int halfturn) { return halfturn % 2 == 0; }

		public boolean isBlack() { return isBlack(counter); }
		public static boolean isBlack(int halfturn) { return halfturn % 2 == 1; }

		public GameSettings getSettings() { return options; }

    private PlayerMove() {
        this(new Board());
    }

    private PlayerMove(Board board) {
        //inst = this;
        this.board = board;
				this.counter = -1;
				this.setStatus(null);
				this.gameOver = false;
    }

    public static PlayerMove inst() {
			if (inst == null) inst = new PlayerMove();
        return inst;
    }

		public static PlayerMove inst(Board board) {
			inst = new PlayerMove(board);
        return inst;
		}

    public Board getBoard() { return board; }

    public void run(GameSettings options) throws EOFException {
			this.options = options;
        gameOver = false;
        boolean keepPlaying;
        do {
            keepPlaying = RequestMove();
						this.flushStatus();
						if (this.gameOver) {
							return;
						}
        } while (keepPlaying);
    }

    public void setGameOver() { this.gameOver = true; }

    public void setStatus(String status) { this.status = status; }
    public void setStatusIfEmpty(String status) { if (this.getStatus() == null) this.setStatus(status); }

    public String getStatus() { return status; }

		public void flushStatus() {
			if (this.getStatus() != null) {
				System.out.println(this.getStatus());
			}
			this.setStatus(null);
		}

		/**
		 * Does not return until a valid move is made or the user forfeits.
		 * 0. Increment the half-turn counter
		 * 1. Clear en passant targets for this player
		 * 2. Print board
		 * 2.1 If we're in a stalemate, say so and end the game
		 * 3. Do-while(not a valid move or a forfeit):
		 * 3.1 Request user input
		 * 3.2 Check whether it's a predefined command
		 * 3.2.1 Execute command
		 * 3.2.2 Continue if it wasn't a forfeit
		 * 3.3 Attempt to parse as a move
		 * 3.3.1 If successful, attempt to apply the resultant move
		 * 3.3.1.1 If move is successful, return
		 * 3.3.2 If either of these fails, continue prompting
		 */
		public boolean RequestMove() throws EOFException {
		 // 0. Increment the half-turn counter
			counter++;

		 // 1. Clear en passant targets for this player
			board.clearPassant(getColor());

		 // 2. Print board
			System.out.println("\n\n" + board);

		 // 2.1 If we're in a stalemate, say so and end the game
			if (!gameOver && board.checkStalemate(getColor())) {
				this.setStatus(getColorName() + " has been forced into a stalemate. 1/2-1/2");
				this.setGameOver();
				return false;
			}

		 // 3. Do-while(not a valid move or a forfeit):
			do {
		 // 3.1 Request user input
				System.out.println("\n" + getColorName() + " to move");
				String input = IOUtils.promptForString(getTurn() + (isWhite() ? ". " : "... "));
		 // 3.2 Check whether it's a predefined command
				switch (input) {
					case "exit":
		 // 3.2.1 Execute command
						System.exit(0); // exits application
					case "forfeit":
		 // 3.2.1 Execute command
						this.setStatus(getColorName() + " has elected to forfeit. " + (isWhite() ? "0-1" : "1-0"));
						this.flushStatus();
						this.setGameOver();
						return false; // exits method, indicating game is over
					case "board":
						System.out.println("\n\n" + board + "\n");
						continue; // skips rest of loop and asks again for a move
					case "help":
					case "?":
		 // 3.2.1 Execute command
						helpMenu();
						continue; // skips rest of loop and asks again for a move
		 // 3.2.2 Continue if it wasn't a forfeit
					default: break; // carries on with loop
				}

				try {

		 // 3.3 Attempt to parse as a move
					Move move;
					try {
						move = Move.fromFreeform(input, board);
					} catch (IncompleteMoveException e) {
						// we'll prompt for more input one more time
						// if they don't complete the move, screw them
						//System.out.print(isWhite() ? "  " : "    "); // i guess the next line trims prompts
						input += " " + IOUtils.promptForString("to: ");
						move = Move.fromFreeform(input, board);
					}

		 // 3.3.1 If successful, attempt to apply the resultant move
					if (AttemptMove(move, board, getColor())) return true; // exits the method, indicating a successful move
					else {
						this.setStatusIfEmpty("Illegal move");
						this.flushStatus();

		 // 3.3.2 If either of these fails, continue prompting
							continue;
					}

				} catch (Exception e) {
					this.setStatusIfEmpty("Invalid move syntax or unrecognized command: \""+input+"\". Try specifying a source and a destination square, like so: a2 a4");
					this.flushStatus();
					
		 // 3.3.2 If either of these fails, continue prompting
					continue;
				}

			} while (true); // continue looping until something returns true or false

			// we better not get here tho
			//throw new IllegalStateException("We escaped a do-while(true) in PlayerMove");
		}

		/*
    //Returns a boolean dependent on if the player intends to quit the game or not.
    public boolean MenuPrompt() throws EOFException {
        PieceColor current = counter % 2 == 0 ? PieceColor.WHITE : PieceColor.BLACK;
        board.clearPassant(current);
        System.out.println("\n\n" + board);
        boolean movePieceCheck;
        MoveResult result = null;
        do {
            if (!gameOver) {
                boolean stalemate = board.checkStalemate(current);
                if (stalemate) {
                    setStatus(current + ", you have been forced into a STALEMATE! " + current.getOpposite() + ", I guess this sort of means you win.");
                    gameOver = true;
                }
            }
            if (status != null) {
                System.out.println(status);
                setStatus(null);
            }
            if (gameOver)
                return false;

            StringBuilder sb = new StringBuilder();
            if (counter == 0)
                sb.append("Welcome to Chess Masters");
            sb.append("\n\n");

            System.out.println(sb);
            String input = null;
            boolean isInt = false;


            do {

                positionOne = null;
                positionTwo = null;
                if (input != null && !input.equalsIgnoreCase("board"))
                    System.out.println("Unrecognized command. Type 'help' for help.");
                input = IOUtils.promptForString((counter % 2 == 0 ? "White" : "Black") + ", it's your turn:\nEnter a choice: ").toLowerCase();
                movePieceCheck = Pattern.matches("[A-Ha-h][1-8][\\s][A-Ha-h][1-8]", input);

                if (movePieceCheck) {
                    String[] text = input.split(Pattern.quote(" "));
                    positionOne = text[0];
                    positionTwo = text[1];

                    input = "1";

                } else if (input.equals("help")) {
                    input = "2";
                } else if (input.equals("board")) {
                    System.out.println("\n\n" + board);
                    continue;
                }


                try {
                    Integer.parseInt(input);
                    isInt = true;
                } catch (NumberFormatException e) {
                    isInt = false;
                }
            } while (!isInt && !input.equalsIgnoreCase("quit"));


            boolean check = input.equalsIgnoreCase("quit");

            if (!check) {

                switch (Integer.parseInt(input)) {
                    case 1:
                        result = (positionOne == null || positionTwo == null) ? MoveResult.FAILED : PromptMove();
                        if (result == MoveResult.FAILED) {
                            if (status == null)
                                setStatus("That move could not be performed.");
                        } else {
                            counter++;
                        }


                        break;
                    case 2:
                        helpMenu();
                        break;
                    case 3:
                        return false;
                }
                return true;
            }
        } while (result == MoveResult.FAILED);
        return false;
    }
*/

	// true for success, false for failure
	// 0. Ensure that the source square has a piece
	// 1. Ensure that the piece to move is of the correct color
	// 2. Attempt the move
	public boolean AttemptMove(Move move, Board board, PieceColor color) {
		Piece p = board.getSquare(move.from);
	// 0. Ensure that the source square has a piece
		if (p == null) {
			this.setStatus("The source square (" + move.from.toString() + ") is empty");
			return false;
		}
	// 1. Ensure that the piece to move is of the correct color
		if (p.getColor() != color) {
			this.setStatus("The requested piece (" + p.getNotation() + move.from.toString() + ") is of the wrong color");
			return false;
		}
	// 2. Attempt the move
		return board.movePiece(move);
	}

// ensure that a piece exists in the source square
// ensure that piece is of correct color
// attempt to move the piece
/*
	public MoveResult PromptMove() {
		MoveResult result = MoveResult.FAILED;
		boolean checkPiece = true;
		boolean checkSecond = true;
		checkPiece = CheckMove(positionOne);
		if (checkPiece) {
			Piece p = board.getSquare(positionOne);
			if (p == null) {
				checkPiece = false;
				setStatus("no piece here");
				return MoveResult.FAILED;
			} else {
				PieceColor colorPiece = p.getColor();
				if (counter % 2 == 0) {
					if (colorPiece != PieceColor.WHITE) {
						checkPiece = false;
						setStatus("wrong piece color");
						return MoveResult.FAILED;
					}
				} else {
					if (colorPiece != PieceColor.BLACK) {
						checkPiece = false;
						setStatus("wrong piece color");
						return MoveResult.FAILED;
					}
				}
			}
		}
		checkSecond = CheckMove(positionTwo);
		if (!checkSecond) {
			setStatus("You have chosen an incorrect location "); // ????
			return MoveResult.FAILED;
		}
		if (result != MoveResult.QUIT) {
			boolean c = board.movePiece(positionOne, positionTwo);
			result = c ? MoveResult.MOVED : MoveResult.FAILED;
		}
		return result;
	}
	*/

/*
    private boolean CheckMove(String po1) {
        boolean checkerOne = Pattern.matches("[A-Ha-h][1-8]", po1);
        return checkerOne;
    }

    private boolean QuitGame(String s) {
        String quit = "quit";

        if (s.equals(quit)) {
            System.out.println("You have chosen to forfeit the game");
            return true;
//            System.exit(0);
        }

        return false;
    }
		*/

    private void helpMenu() throws EOFException {
        StringBuilder helper = new StringBuilder(" --[ Helper commands ]-- ");

        helper.append("\n\nTo move, simply input your target piece (a2) and your destination (a4). Ex: 'a2 a4' moves the piece at a2 to a4")
                .append("\n\nTo quit/forfeit the game simply type 'quit' whenever\n")
                .append("\nEn Passant: When a '*' is displayed, an en passant is possible. This occurs when a pawn takes its initial move two spaces,\n")
                .append("but could be intercepted by an opposing piece. If the opportunity is not taken, it is lost.\n");
        //System.out.println(
        setStatus(helper.toString());
				this.flushStatus();
    }


		/*
    public enum MoveResult {
        QUIT,
        MOVED,
        FAILED
    }
		*/

}
