public class Simbolo {
	int nivel;
	String lexema;
	String tipo;
	int rotulo;
	
	
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
//	public String getMemoria() {
//		return memoria;
//	}
//	public void setMemoria(String memoria) {
//		this.memoria = memoria;
//	}
	@Override
	public String toString() {
		return "Simbolo [lexema=" + lexema + ", tipo=" + tipo + "]";
	}
	
}