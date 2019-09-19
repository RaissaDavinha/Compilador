import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException, LexicoException {
		FileReader fileReader = new FileReader("lexico.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String readContent = reader.readLine();
		List<Token> tokens = new ArrayList<Token>();
		Token token = new Token();
		int pos = 0;
		int linha = 1;
		char letra;
		String id;
		String num;
		
		
		
		
		while (readContent != null) {
			letra = readContent.charAt(pos);
			while((letra == '{') || (letra == ' ') || (readContent != null)) {
				if (letra == '{') {
					while(letra != '}' && readContent != null) {
						pos++;
						letra = readContent.charAt(pos);
					}
					while((letra == ' ') && readContent != null) {
						pos++;
						letra = readContent.charAt(pos);
					}
				}
				
			}
			
			if (Character.isDigit(letra)) {
				num = String.valueOf(letra);
				pos++;
				letra = readContent.charAt(pos);
				
				while(Character.isDigit(letra)) {
					num = num + letra;
					pos++;
					letra = readContent.charAt(pos);
				}
				
				token.setSimbolo("snumero");
				token.setLexema(num);
				token.setLinha(linha);
//				tokens.add(token);
				
			}else {
				if (Character.isLetter(letra)) {
					id = String.valueOf(letra);
					pos++;
					letra = readContent.charAt(pos);
					
					while(letra == '_' || Character.isDigit(letra) || Character.isLetter(letra)) {
						id = id + letra;
						pos++;
						letra = readContent.charAt(pos);
					}
					token.setLexema(id);
					token.setLinha(linha);
					
					switch (id) {
					case "programa":
						token.setSimbolo("sprograma");
						break;
						
					case "se":
						token.setSimbolo("sse");
						break;
						
					case "entao":
						token.setSimbolo("sentao");
						break;
						
					case "ssenao":
						token.setSimbolo("sprograma");
						break;
						
					case "enquanto":
						token.setSimbolo("senquanto");
						break;
						
					case "faca":
						token.setSimbolo("sfaca");
						break;
					
					case "inicio":
						token.setSimbolo("sinicio");
						break;
						
					case "fim":
						token.setSimbolo("sfim");
						break;
						
					case "escreva":
						token.setSimbolo("sescreva");
						break;
						
					case "leia":
						token.setSimbolo("sleia");
						break;
						
					case "var":
						token.setSimbolo("svar");
						break;
						
					case "inteiro":
						token.setSimbolo("sinteiro");
						break;
					
					case "booleano":
						token.setSimbolo("sbooleano");
						break;
					
					case "verdadeiro":
						token.setSimbolo("sverdadeiro");
						break;
						
					case "falso":
						token.setSimbolo("sfalso");
						break;
						
					case "procedimento":
						token.setSimbolo("sprocedimento");
						break;
						
					case "funcao":
						token.setSimbolo("sfuncao");
						break;
						
					case "div":
						token.setSimbolo("sdiv");
						break;
						
					case "e":
						token.setSimbolo("se");
						break;
						
					default:
						token.setSimbolo("sidentificador");
					}
					
				}else {
					if (letra == ':') {
						if (readContent.charAt(pos + 1) == '=') {
							num = ":=";
							token.setLexema(num);
							token.setSimbolo("satribuicao");
							token.setLinha(linha);
							pos++;
//							pos++;
						}else {
							num = ":";
							token.setLexema(num);
							token.setSimbolo("sdoispontos");
							token.setLinha(linha);
//							pos++;
						}
					}else {
						if (letra == '+' || letra == '-' || letra == '*') {
//							Tratar Operador Aritmetico
						}else {
							if (letra == '<' || letra == '>' || letra == '=') {
//								Trata Operador Relacional
							}else {
								if (letra == ';' || letra == ',' || letra == '(' || letra == ')' || letra == '.') {
//									Trata Pontuacao
									if(letra == ';') {
										num = ";";
										token.setLexema(num);
										token.setSimbolo("sponto_virgula");
										token.setLinha(linha);
									}
									if(letra == ',') {
										num = ",";
										token.setLexema(num);
										token.setSimbolo("svirgula");
										token.setLinha(linha);
									}
									if(letra == '(') {
										num = "(";
										token.setLexema(num);
										token.setSimbolo("sabre_parenteses");
										token.setLinha(linha);
									}
									if(letra == ')') {
										num = ")";
										token.setLexema(num);
										token.setSimbolo("sfecha_parenteses");
										token.setLinha(linha);
									}
									if(letra == '.') {
										num = ".";
										token.setLexema(num);
										token.setSimbolo("sponto");
										token.setLinha(linha);
									}
								}else {
									throw new LexicoException("Erro Léxico na linha:" + linha);
								}
							}
						}
					}
				}
			}
			
			tokens.add(token);
			readContent = reader.readLine();
			pos = 0;
			linha++;
		}
		
		fileReader.close();

	}

}
