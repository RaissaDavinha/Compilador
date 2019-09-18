import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		FileReader fileReader = new FileReader("lexico.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String readContent = reader.readLine();
		List<Token> tokens = new ArrayList<Token>();
		Token token = new Token();
		int pos = 0;
		int linha = 1;
		char letra;
		String id;
		String num = null;
		
		
		
		
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
				num += Character.toString(letra);
				pos++;
				letra = readContent.charAt(pos);
				
				while(Character.isDigit(letra)) {
					num += Character.toString(letra);
					pos++;
					letra = readContent.charAt(pos);
				}
				
				token.setSimbolo("snumero");
				token.setLexema(num);
				tokens.add(token);
				
			} else {
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
					
//					switch
					
				}else {
					if (letra == ':') {
//						Trata Atribuicao
					}else {
						if (letra == '+' || letra == '-' || letra == '*') {
//							Tratar Operador Aritmetico
							id = null;
							if (letra == '+') {
								token.setSimbolo("smais");
							} else {
								if (letra == '-') {
									token.setSimbolo("smenos");
								} else {
									if (letra == '*') {
										token.setSimbolo("smult");
									}
								}
							}
							id = Character.toString(letra);
							token.setLexema(id);
							tokens.add(token);
						}else {
							if (letra == '<' || letra == '>' || letra == '=') {
								id = null;
//								Trata Operador Relacional
								if (letra == '=') {
									token.setSimbolo("sig");
								} else {
									if (letra == '>') {
										if (readContent.charAt(pos+1) == '=') {
											id += Character.toString(letra);
											pos++;
											letra = readContent.charAt(pos);
											token.setSimbolo("smaiorig");	
										} else {
											token.setSimbolo("smaior");
										}
									} else {
										if (readContent.charAt(pos+1) == '=') {
											id += Character.toString(letra);
											pos++;
											letra = readContent.charAt(pos);
											token.setSimbolo("smenorig");	
										} else {
											token.setSimbolo("smenor");
										}
									}
								}
								id += Character.toString(letra);
								token.setLexema(id);
								tokens.add(token);
							}else {
								if (letra == ';' || letra == ',' || letra == '(' || letra == ')' || letra == '.') {
//									Trata Pontuacao
								}
							}
						}
					}
				}
			}
			
			
			readContent = reader.readLine();
			pos = 0;
			linha++;
		}
		
		fileReader.close();

	}

}
