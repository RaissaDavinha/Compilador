import java.util.ArrayList;

public class TabelaSimbolos {
	ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>();

	int stackPointer = 0;

	public void insereTabela(String lexema, String tipo, int nivel) {
		Simbolo simbolo = new Simbolo();
		simbolo.lexema = lexema;
		simbolo.tipo = tipo;
		simbolo.nivel = nivel;
		simbolos.add(simbolo);
//		simbolos.add(stackPointer, simbolo);
//			System.out.println("---------------------------------");
//			System.out.println(simbolos.get(stackPointer));
//			System.out.println("---------------------------------");
		System.out.println();
		stackPointer++;
	}

	public boolean pesquisaDuplicaVar(String lexema, int nivel) {
		for (int i = 0; i < simbolos.size(); i++) {
			if (lexema.equals(simbolos.get(i).getLexema()) && nivel == simbolos.get(i).getNivel()) {
				if (simbolos.get(i).getTipo() == "variavel") {
					System.out.println("duplicado");
					return false;
				}
			}
		}

		return true;
	}

	public boolean pesquisaTodaVar(String lexema) {
		for (int i = 0; i < simbolos.size(); i++) {
			if (lexema.equals(simbolos.get(i).getLexema())) {
				if (simbolos.get(i).getTipo() == "variavel") {
					System.out.println("duplicado");
					return true;
				}
			}
		}

		return false;
	}

	public boolean pesquisaTodaTab(String lexema) {
		System.out.println(simbolos);
		for (int i = 0; i < simbolos.size(); i++) {
			if (lexema.equals(simbolos.get(i).getLexema())) {
				return true;
			}
		}

		return false;
	}

	public void colocaTipo(String tipo) {
		Simbolo simbolo = new Simbolo();
		simbolo.tipo = tipo;

		simbolos.add(simbolo);
		stackPointer++;
	}

	public boolean pesquisaDeclProc(String lexema, int nivel) {
		for (int i = 0; i < simbolos.size(); i++) {
			if (lexema.equals(simbolos.get(i).getLexema()) && simbolos.get(i).getNivel() == nivel) {
				if (simbolos.get(i).getTipo() == "procedimento") {
					System.out.println("duplicado");
					return false;
				}
			}
		}
		return true;
	}

	public boolean pesquisaDeclFunc(String lexema, int nivel) {
		for (int i = 0; i < simbolos.size(); i++) {
			if (lexema.equals(simbolos.get(i).getLexema()) && simbolos.get(i).getNivel() == nivel) {
				if (simbolos.get(i).getTipo() == "funcao") {
					System.out.println("duplicado");
					return false;
				}
			}
		}
		return true;
	}

	public boolean pesquisaTipo(String lexema) {
		for (int i = 0; i < simbolos.size(); i++) {
			if (lexema.equals(simbolos.get(i).getLexema())) {
				return false;
			}

//			System.out.println("=======================");
//			System.out.println(Lexema);
//			System.out.println(simbolos.get(i).getLexema());
//			System.out.println("=======================");
		}
		return true;

	}

}