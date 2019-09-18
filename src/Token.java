
public class Token {
	String lexema;
	String simbolo;
	
	
	
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



	public Token pegaToken(char letra) {
		if (Character.isDigit(letra)) {
//			Então trata digito
		}else {
			if (Character.isLetter(letra)) {
//				Trata identificador e Palavra Reservada
			}else {
				if (letra == ':') {
//					Trata Atribuicao
				}else {
					if (letra == '+' || letra == '-' || letra == '*') {
//						Tratar Operador Aritmetico
					}else {
						if (letra == '<' || letra == '>' || letra == '=') {
//							Trata Operador Relacional
						}else {
							if (letra == ';' || letra == ',' || letra == '(' || letra == ')' || letra == '.') {
//								Trata Pontuacao
							}
						}
					}
				}
			}
		}
		return null;
		
	}
}
