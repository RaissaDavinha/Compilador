public class SemanticoException extends Exception {
	public SemanticoException(String msg) {
		super(msg);
		IDE.sendToConsole(msg);
	}
}