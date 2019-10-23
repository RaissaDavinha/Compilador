import java.util.Stack;


public class TabelaSimbolos {
	Stack<Simbolo> simbolos = new Stack<Simbolo>();
	Simbolo simbolo = new Simbolo();

	
	public void insereTabela(String lexema, String tipo) {
		simbolo.lexema = lexema;
		simbolo.tipo = tipo;
		
		simbolos.push(simbolo);
	}
	
	public boolean pesquisaDuplicaVar(String Lexema){
		simbolo.lexema = Lexema;
		simbolo.tipo = "variavel";
//		System.out.println(Lexema);
//		System.out.println(simbolos.contains(simbolo));
//		System.out.println(simbolo);
//		System.out.println(simbolos);
		return simbolos.contains(simbolo);
		
	}
	
	public void colocaTipo(String tipo) {
		Simbolo simbolo = new Simbolo();
		simbolo.tipo = tipo;
		
		simbolos.push(simbolo);
	}
	
}