public class LexicoException extends Exception{
	public LexicoException(String msg) {
		super(msg);
		IDE.sendToConsole(msg);
	}
}