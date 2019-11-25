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

	public boolean verificaDeclaDuplicVar(String lexema, ArrayList<Integer> nivelList) { // se encontrar declaração de variavel duplicada retorna true
		int i = nivelList.size() - 1;
		int j = 0;
		
		while (j < simbolos.size()) {
			if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && simbolos.get(j).tipo.equals("variavel")) {
				return true;
			} else {
				j++;
			}
		}
		j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && !simbolos.get(j).tipo.equals("variavel")) {
					return true;
				} else {
					j++;
				}
			}
			j = 0;
			i--;
		} while (i >= 0);
		
		return false;
	}
	
	public boolean verificaDeclaDuplicProc(String lexema, ArrayList<Integer> nivelList) { // se encontrar declaração de variavel duplicada retorna true
		int i = nivelList.size() - 1;
		int j = 0;
		
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema)) {
					return true;
				} else {
					j++;
				}
			}
			j = 0;
			i--;
		} while (i >= 0);
		
		return false;
	}
	
	public boolean verificaVarDeclarada(String lexema, int nivel) { // retorna true se ja estiver declarada
		int i = 0;
		
		while (i < simbolos.size()) {
			if (simbolos.get(i).nivel == nivel && lexema.equals(simbolos.get(i).lexema) && simbolos.get(i).tipo.equals("variavel")) {
				return true;
			} else {
				i++;
			}
		}
		return false;
	}
	public boolean verificaProcFuncDeclarada(String lexema, int nivel) { // retorna true se ja estiver declarada
		int i = 0;
		
		while (i < simbolos.size()) {
			if (simbolos.get(i).nivel == nivel && lexema.equals(simbolos.get(i).lexema) && !simbolos.get(i).tipo.equals("variavel") && !simbolos.get(i).tipo.equals("nomedeprograma")) {
				return true;
			} else {
				i++;
			}
		}
		return false;
	}
	public boolean verificaIndentificadorFuncao(String lexema, int nivel) {
		int i = 0;
		
		while (i < simbolos.size()) {
			if (simbolos.get(i).nivel == nivel && lexema.equals(simbolos.get(i).lexema) && simbolos.get(i).tipo.equals("funcao booleano") && simbolos.get(i).tipo.equals("funcao inteiro")) {
				return true;
			} else {
				i++;
			}
		}
		return false;
	}
	
	public boolean verificaDeclaradoTudo(String lexema, int nivel) { // retorna true se ja estiver declarada
		int i = 0;
		
		while (i < simbolos.size()) {
			if (simbolos.get(i).nivel == nivel && lexema.equals(simbolos.get(i).lexema) && !simbolos.get(i).tipo.equals("nomedeprograma")) {
				return true;
			} else {
				i++;
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

	

}