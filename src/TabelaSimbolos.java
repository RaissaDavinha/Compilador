import java.util.ArrayList;


public class TabelaSimbolos {
	ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>();
	Simbolo simbolo = new Simbolo();
	int stackPointer = 0;

	
	public void insereTabela(String lexema, String tipo) {
		simbolo.lexema = lexema;
		simbolo.tipo = tipo;
		simbolos.add(simbolo);
		stackPointer++;
	}
	
	public boolean pesquisaDuplicaVar(String Lexema){
		for (int i = 0; i < simbolos.size(); i++) {
			if (Lexema.equals(simbolos.get(i).getLexema())) {
				System.out.println("duplicado");
				return false;
			}
//			System.out.println("=======================");
//			System.out.println(Lexema);
//			System.out.println(simbolos.get(i).getLexema());
//			System.out.println("=======================");
		}
		
		 return true;
	}
	
	public void colocaTipo(String tipo) {
		Simbolo simbolo = new Simbolo();
		simbolo.tipo = tipo;
		
		simbolos.add(stackPointer, simbolo);
		stackPointer++;
	}

	public boolean teste(String s1, String s2) {
		if (s1.equals(s2)) {
			System.out.println("igual");
			return true;
		}else {
			System.out.println("not");
			return false;
		}
	}

	public boolean pesquisaDeclProc(String lexema) {
		for (int i = 0; i < simbolos.size(); i++) {
			if (lexema.equals(simbolos.get(i).getLexema())) {
				System.out.println("duplicado");
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