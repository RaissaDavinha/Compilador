
public class Token {
	@Override
	public String toString() {
		return "Token [lexema=" + lexema + ", simbolo=" + simbolo + ", linha=" + linha + "]";
	}



	String lexema;
	String simbolo;
	int linha;
	int coluna;
	
	
	public int getColuna() {
		return coluna;
	}



	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	
	public int getLinha() {
		return linha;
	}



	public void setLinha(int linha) {
		this.linha = linha;
	}



	public String getLexema() {
		return lexema;
	}



	public void setLexema(String lexema) {
		this.lexema = lexema;
	}



	public String getSimbolo() {
		return simbolo;
	}



	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}


}