public class Simbolo {
	//	int escopo;
	String lexema;
	String tipo;
//	String memoria;
	
	
//	public int getEscopo() {
//		return escopo;
//	}
//	public void setEscopo(int escopo) {
//		this.escopo = escopo;
//	}
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