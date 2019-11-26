
public class SintaticoException extends Exception{
	public SintaticoException(String msg) {
		super(msg);
		IDE.sendToConsole(msg);
	}
}