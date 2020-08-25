package edu.neumont.chessmasters.models;

//import;

public class GameSettings {
	//public enum ColorSelection { ON, OFF, AUTO }
	public boolean debug = false;
	public boolean start = false;
	public boolean needsWrapping;
	//public ColorSelection color = ColorSelection.AUTO;
	public Boolean color = null;
	public Boolean unicode = null;
	public Boolean traceMoves = false;
	public boolean flip = false;
	public String filePath;
	public String fileContents;
	public String[] raw;
	public GameSettings(String[] raw) {
		this.raw = raw;
	}
}
