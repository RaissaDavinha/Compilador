import java.util.ArrayList;

public class TabelaSimbolos {
	ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>();

	int stackPointer = 0;

	public void insereTabela(String lexema, String tipo, int nivel, int rotulo) {
		Simbolo simbolo = new Simbolo();
		simbolo.lexema = lexema;
		simbolo.tipo = tipo;
		simbolo.nivel = nivel;
		simbolo.rotulo = rotulo;
		simbolos.add(simbolo);
		stackPointer++;
	}

	public boolean verificaDeclaDuplicVar(String lexema, ArrayList<Integer> nivelList) { // se encontrar declaração de variavel duplicada retorna true
		int i = nivelList.size() - 1;
		int j = 0;
		
		while (j < simbolos.size()) {
			if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && (simbolos.get(j).tipo.equals("variavel inteiro") || simbolos.get(j).tipo.equals("variavel booleano"))) {
				return true;
			} else {
				j++;
			}
		}
		j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && !simbolos.get(j).tipo.equals("variavel inteiro") && !simbolos.get(j).tipo.equals("variavel booleano")) {
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
	
	public boolean verificaVarDeclarada(String lexema, ArrayList<Integer> nivelList) { // retorna true se ja estiver declarada
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && (simbolos.get(j).tipo.equals("variavel inteiro") || simbolos.get(j).tipo.equals("variavel booleano"))) {
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
	public boolean verificaProcFuncDeclarada(String lexema, ArrayList<Integer> nivelList) { // retorna true se ja estiver declarada
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && !simbolos.get(j).tipo.equals("variavel inteiro") && !simbolos.get(j).tipo.equals("variavel booleano") && !simbolos.get(j).tipo.equals("nomedeprograma")) {
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
	public boolean verificaIndentificadorFuncao(String lexema, ArrayList<Integer> nivelList) {
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && (simbolos.get(j).tipo.equals("funcao booleano") || simbolos.get(j).tipo.equals("funcao inteiro"))) {
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
	public boolean verificaVariavelInteiro(String lexema, ArrayList<Integer> nivelList) {
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && simbolos.get(j).tipo.contains("variavel")) {
					if (simbolos.get(j).tipo.equals("variavel inteiro")) {
						return true;
					} else {
						return false;
					}
				} else {
					j++;
				}
			}
			j = 0;
			i--;
		} while (i >= 0);
		
		return false;
	}
	
	public int verificaTipoIndentificador(String lexema, ArrayList<Integer> nivelList) {
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema)) {
					switch (simbolos.get(j).tipo) {
						case "variavel inteiro":
						return 1;
						
						case "variavel booleano":
						return 2;
						
						case "funcao inteiro":
						return 3;
						
						case "funcao booleano":
						return 4;
						
						default:
						return 0;
					}
				
				} else {
					j++;
				}
			}
			j = 0;
			i--;
		} while (i >= 0);
		
		return 0;
	}
	
	public boolean verificaDeclaradoTudo(String lexema, ArrayList<Integer> nivelList) { // retorna true se ja estiver declarada
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel <= nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && !simbolos.get(j).tipo.equals("nomedeprograma") && !simbolos.get(j).tipo.equals("procedimento")) {
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
	
	public void colocaTipo(String tipo, int count) { // tipo = "variavel inteiro" ou  "variavel booleano"
		int index = simbolos.size() - 1;
		int i;
		Simbolo auxSimbolo = new Simbolo();
		for (i = 0; i < count; i++, index--) {
			auxSimbolo = simbolos.get(index);
			auxSimbolo.tipo = tipo;
			simbolos.set(index, auxSimbolo);
		}
	}
	public int returnVarRotulo(String lexema, ArrayList<Integer> nivelList) {
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && (simbolos.get(j).tipo.equals("variavel inteiro") || simbolos.get(j).tipo.equals("variavel booleano"))) {
					return simbolos.get(j).rotulo;
				} else {
					j++;
				}
			}
			j = 0;
			i--;
		} while (i >= 0);
		
		return 0;
	}
	
	public int returnVarRotulo(String lexema, int nivel) {
		for(int i = 0; i < simbolos.size(); i++) {
			if (simbolos.get(i).getLexema().equals(lexema) && simbolos.get(i).getNivel() == nivel) {
				return simbolos.get(i).rotulo;
			}
		}
		
		return 0;
	}
	
	public void limpaNivel(int nivel) {
		for(int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getNivel() >= nivel) {
				simbolos.remove(i);
			}
		}
	}
	
	public int returnProcFuncRotulo(String lexema, ArrayList<Integer> nivelList) { // retorna true se ja estiver declarada
		int i = nivelList.size() - 1;
		int j = 0;
		do {
			while (j < simbolos.size()) {
				if (simbolos.get(j).nivel == nivelList.get(i) && lexema.equals(simbolos.get(j).lexema) && !simbolos.get(j).tipo.equals("variavel inteiro") && !simbolos.get(j).tipo.equals("variavel booleano") && !simbolos.get(j).tipo.equals("nomedeprograma")) {
					return simbolos.get(j).rotulo;
				} else {
					j++;
				}
			}
			j = 0;
			i--;
		} while (i >= 0);
		
		return 0;
	}

}