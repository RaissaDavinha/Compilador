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

	public boolean verificaDeclaDuplicVar(String lexema) { // se encontrar declaração de variavel duplicada retorna true
		for (int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).tipo.contains("variavel")) {
				if (simbolos.get(i).lexema.equals(lexema)) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean verificaVarDeclarada(String lexema) { // retorna true se ja estiver declarada
		for (int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).tipo.contains("variavel")) {
				if (simbolos.get(i).lexema.equals(lexema)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean verificaDeclaDuplic(String lexema) { // se encontrar declaração de variavel duplicada retorna true
		for (int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				return true;
			}
		}
		return false;
	}
	
	

	public boolean verificaProcDeclarada(String lexema) { // retorna true se ja estiver declarada
		for (int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).tipo.contains("proc")) {
				if (simbolos.get(i).lexema.equals(lexema)) {
					return true;
				}
			}
		}
		return false;
	}
	

	public boolean verificaFuncDeclarada(String lexema) { // retorna true se ja estiver declarada
		for (int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).tipo.contains("funcao")) {
				if (simbolos.get(i).lexema.equals(lexema)) {
					return true;
				}
			}
		}
		return false;
	}

	
	
	public boolean verificaFuncaoVar(String lexema) {
		for (int i = simbolos.size() - 1; i >= 0 ; i--) {
			if (simbolos.get(i).getLexema().equals(lexema) && (simbolos.get(i).getTipo().equals("funcao booleano") || simbolos.get(i).getTipo().equals("funcao inteiro") || simbolos.get(i).getTipo().equals("variavel inteiro") || simbolos.get(i).getTipo().equals("variavel booleano"))) {
				return true;
			}
		}
		return false;
	}
	
	public int verificaTipoIndentificador(String lexema) {
		int j = simbolos.size() - 1;
		while (j >= 0) {
			if (lexema.equals(simbolos.get(j).lexema)) {
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
				j--;
			}
		}
		return 0;
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
	

	
	public int returnRotulo(String lexema) {
		for(int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getLexema().equals(lexema)) {
				return simbolos.get(i).rotulo;
			}
		}
		
		return 0;
	}
	
	public void limpaNivel(int nivel) {
		for(int i = simbolos.size() - 1; i >= 0; i--) {
			if (simbolos.get(i).getNivel() == nivel) {
				simbolos.remove(i);
			}
		}
	}


}