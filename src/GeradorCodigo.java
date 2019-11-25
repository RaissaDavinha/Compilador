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
	
   56 + 9
   56 9 +					
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
//					Verificar se coloca os ()
					pilha.push(c);
					stackPointer++;
				} else {
					if (c.getLexema() == ")") {
						while (!pilha.isEmpty() && pilha.peek().getLexema() != ")") {
							if (pilha.peek().getLexema() != "(") {
								postFix.add(pilha.pop());
							}else {
								pilha.pop();
							}
							
						}
					} else {
						while (!pilha.isEmpty() && postOperand(c, flag) <= postOperand(pilha.peek(), flag)) {
							if (pilha.peek().getLexema() == "(") {
//								erro
							}

//							postFix.add(pilha.pop());
						}
					}
					pilha.push(c);
				}
			}
			flag = 0;
		}

		while (!pilha.isEmpty()) {
			if (pilha.peek().getLexema() == "(") {
//				erro
			}
			postFix.add(pilha.pop());
		}

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

	public static int validaPostFix(ArrayList<Token> postFix) {
		ArrayList<String> auxPostFix = new ArrayList<>();
		
		for (int i = 0; i < postFix.size(); i++) {
			auxPostFix.add(postFix.get(i).getSimbolo());
		}
		
		for (int i = 0; i < auxPostFix.size(); i++) {
//			Validar se for +,-, nao de sinal remover da lista
			if (auxPostFix.get(i) != "sidentificador" && auxPostFix.get(i) != "sinteiro" && auxPostFix.get(i) != "sbooleano" && auxPostFix.get(i) != "svar") {
				if (auxPostFix.get(i) == "smais" || auxPostFix.get(i) == "smenos" || auxPostFix.get(i) == "smult" || auxPostFix.get(i) == "sdiv") {
					if (auxPostFix.get(i-1) == auxPostFix.get(i-2) && auxPostFix.get(i-1) == "sinteiro") {
						auxPostFix.remove(i);
						auxPostFix.remove(i-1);
						i = 0;
					}else {
//						erro
					}
				}else {
					if (auxPostFix.get(i) == "smaiorig" || auxPostFix.get(i) == "smaior" || auxPostFix.get(i) == "smenorig" || auxPostFix.get(i) == "smenor" || auxPostFix.get(i) == "smenorig" || auxPostFix.get(i) == "smenor") {
						if (auxPostFix.get(i-1) == auxPostFix.get(i-2) && auxPostFix.get(i-1) == "sinteiro") {
							auxPostFix.remove(i);
							auxPostFix.remove(i-1);
							auxPostFix.set(i-2, "sbooleano");
							i = 0;
						}else {
//							erro
						}
					}
					if(auxPostFix.get(i) == "sou" || auxPostFix.get(i) == "se"){
						if (auxPostFix.get(i-1) == auxPostFix.get(i-2) && auxPostFix.get(i-1) == "sbooleano") {
							auxPostFix.remove(i);
							auxPostFix.remove(i-1);
							auxPostFix.set(i-2, "sbooleano");
							i = 0;
						}else {
//							erro
						}
					}
					if(auxPostFix.get(i) == "sig" || auxPostFix.get(i) == "sdif" ){
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
	

	

		public void geraLdc(int k) {
		codigoGerado += "LDC " + k +"\n"; 
	}

	public void geraLdv(int k) {
		codigoGerado += "LDV " + k +"\n"; 
	}

	public void geraAdd() {
		codigoGerado += "ADD" + "\n"; 
	}

	public void geraSub() {
		codigoGerado += "SUB" + "\n"; 
	}

	public void geraMult() {
		codigoGerado += "MULT" + "\n"; 
	}

	public void geraDiv() {
		codigoGerado += "DIV" + "\n"; 
	}

	public void geraInv() {
		codigoGerado += "INV" + "\n"; 
	}

	public void geraAnd() {
		codigoGerado += "AND" + "\n"; 
	}

	public void geraOr() {
		codigoGerado += "OR" + "\n"; 
	}

	public void geraNeg() {
		codigoGerado += "NEG" + "\n"; 
	}

	public void geraCme() {
		codigoGerado += "CME" + "\n"; 
	}

	public void geraCma() {
		codigoGerado += "CMA" + "\n"; 
	}

	public void geraCeq() {
		codigoGerado += "CEQ" + "\n"; 
	}

	public void geraCdif() {
		codigoGerado += "CDIF" + "\n"; 
	}

	public void geraCmeq() {
		codigoGerado += "CMEQ" + "\n"; 
	}

	public void geraCmaq() {
		codigoGerado += "CMAQ" + "\n"; 
	}

	public void geraStart() {
		codigoGerado += "START" + "\n";
	}

	public void geraHlt() {
		codigoGerado += "HLT" + "\n";
	}

	public void geraStr(int n) {
		codigoGerado += "STR " + n + "\n"; 
	}

	public void geraJmp(String t) {
		codigoGerado += "JMP " + t + "\n"; 
	}

	public void geraJmpF(String t) {
		codigoGerado += "JMPF " + t + "\n"; 
	}

	public void geraNull() {
		codigoGerado += "NULL" + "\n"; 
	}

	public void geraRd() {
		codigoGerado += "RD" + "\n"; 
	}

	public void geraPrn() {
		codigoGerado += "PRN" + "\n"; 
	}

	public void geraAlloc(int m, int n) {
		codigoGerado += "ALLOC " + m + n + "\n"; 
	}

	public void geraDalloc(int m, int n) {
		codigoGerado += "DALLOC " + m + n + "\n"; 
	}

	public void geraCall(String t) {
		codigoGerado += "CALL " + t + "\n";
	}

	public void geraReturn() {
		codigoGerado += "RETURN" + "\n";
	}
	public void geraLabel() {
		codigoGerado += "L" + label + "\n";
		label++;
	}
}
