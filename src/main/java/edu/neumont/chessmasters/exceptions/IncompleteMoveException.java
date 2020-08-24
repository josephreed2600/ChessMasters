package edu.neumont.chessmasters.exceptions;

public class IncompleteMoveException extends Exception {
	public IncompleteMoveException(String msg) { super(msg); }
	public IncompleteMoveException(String msg, Throwable exception) { super(msg, exception); }
}
