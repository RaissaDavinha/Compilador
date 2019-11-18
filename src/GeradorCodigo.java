import java.util.ArrayList;
import java.util.Stack;


public class GeradorCodigo {
//	String postFix;
	ArrayList<Token> postFix;
	String inFix;
//	ArrayList<Token> pilha = new ArrayList<>();
//	Stack<Character> pilha = new Stack<>();
	Stack<Token> pilha = new Stack<>();
	int stackPointer = 0;
	int label = 0;
	int i;
	

	public void geraPostFix(ArrayList<Token> infix) {
		int j = infix.size();

		for (i = 0; i < j; i++) {
//			char c =infix.charAt(i);
			Token c = new Token();
			int flag = 0;
			
			c = infix.get(i);
			
			if (i == 0) {
				flag = 1;
			}else {
				if (!Character.isLetterOrDigit(infix.get(i-1).getLexema().charAt(0))) {
					flag = 1;
				}
			}
			if (Character.isLetterOrDigit(c.getLexema().charAt(0))) {
				postFix.add(c);
			} else {
				if (c.getLexema() == "(") {
					pilha.push(c);
					stackPointer++;
				} else {
					if (c.getLexema() == ")") {
						while (!pilha.isEmpty() && pilha.peek().getLexema() != ")") {
							postFix.add(pilha.pop());
						}
						if (!pilha.isEmpty() && pilha.peek().getLexema() != "(") {
//							erro
						} else {
							pilha.pop();
						}
					} else {
						while (!pilha.isEmpty() && postOperand(c, flag) <= postOperand(pilha.peek(), flag)) {
							if (pilha.peek().getLexema() == "(") {
//								erro
							}

							postFix.add(pilha.pop());
						}
					}
					pilha.push(c);
				}
			}
		}

		while (!pilha.isEmpty()) {
			if (pilha.peek().getLexema() == "(") {
//				erro
			}
			postFix.add(pilha.pop());
		}

	}
	
	public static int validaPostFix(ArrayList<Token> postFix) {
		ArrayList<String> auxPostFix = new ArrayList<>();
		
		for (int i = 0; i < postFix.size(); i++) {
			auxPostFix.add(postFix.get(i).getSimbolo());
		}
		
		for (int i = 0; i < auxPostFix.size(); i++) {
//			Validar se for +,-, nao de sinal remover da lista
			if (auxPostFix.get(i) != "sidentificador" && auxPostFix.get(i) != "sinteiro" && auxPostFix.get(i) != "sbooleano" && auxPostFix.get(i) != "svar") {
				if (auxPostFix.get(i) == "smais" || auxPostFix.get(i) == "smenos" || auxPostFix.get(i) == "smult" || auxPostFix.get(i) == "sdiv") {
					if (auxPostFix.get(i-1) == auxPostFix.get(i-2)) {
						auxPostFix.remove(i);
						auxPostFix.remove(i-1);
						i = 0;
					}else {
//						erro
					}
				}else {
					if (auxPostFix.get(i) == "smaiorig" || auxPostFix.get(i) == "smaior" || auxPostFix.get(i) == "smenorig" || auxPostFix.get(i) == "smenor" || auxPostFix.get(i) == "smenorig" || auxPostFix.get(i) == "smenor" || auxPostFix.get(i) == "sig" || auxPostFix.get(i) == "sdif") {
						if (auxPostFix.get(i-1) == auxPostFix.get(i-2)) {
							auxPostFix.remove(i);
							auxPostFix.remove(i-1);
							auxPostFix.set(i-2, "sbooleano");
							i = 0;
						}else {
//							erro
						}
					}
				}
			}
		}
		return 1;
	}
	

	public static int postOperand(Token token, int flag) {
		String c = token.getLexema();
		if (c == "+" && flag == 1|| c == "-" && flag == 1|| c == "nao" && flag == 1) {
			return 6;
		} else {
			if (c == "*" || c == "/") {
				return 5;
			} else {
				if (c == "+" && token.getSimbolo() == "smais" || c == "-" && token.getSimbolo() == "smenos") {
					return 4;
				}
				if (c == ">" || c == "<" || c == ">=" || c == "<=" || c == "=" || c == "!=") {
					return 3;
				} else {
					if (c == "e") {
						return 2;
					} else {
						if (token.getSimbolo() == "se") {
							return 2;
						} else {
							if (token.getSimbolo() == "sou") {
								return 1;
							}
						}
					}
				}
			}
		}
		return -1;
	}

	public void geraLdc() {

	}

	public void geraLdv() {

	}

	public void geraAdd() {

	}

	public void geraSub() {

	}

	public void geraMult() {

	}

	public void geraDiv() {

	}

	public void geraInv() {

	}

	public void geraAnd() {

	}

	public void geraOr() {

	}

	public void geraNeg() {

	}

	public void geraCme() {

	}

	public void geraCma() {

	}

	public void geraCeq() {

	}

	public void geraCdif() {

	}

	public void geraCmeq() {

	}

	public void geraCmaq() {

	}

	public void geraStart() {

	}

	public void geraHlt() {

	}

	public void geraStr() {

	}

	public void geraJmp() {

	}

	public void geraJmpF() {

	}

	public void geraNull() {

	}

	public void geraRd() {

	}

	public void geraPrn() {

	}

	public void geraAlloc() {

	}

	public void geraDalloc() {

	}

	public void geraCall() {

	}

	public void geraReturn() {

	}
}
