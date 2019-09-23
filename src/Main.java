import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException, LexicoException {
		FileReader fileReader = new FileReader("test1.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String auxContent = null;
		String fileContent = "";
		List<Token> tokens = new ArrayList<Token>();
		Token token = new Token();
		int fileContentIndex = 0;
		char controlCharacter;
		String tokenBuilder;
		
		
		auxContent = reader.readLine();
		while (auxContent != null) {
			fileContent += auxContent;
			fileContent += '\n';
			auxContent = reader.readLine();
		}

		fileReader.close();
		
		System.out.print(fileContent);
		
	
		
		while (fileContentIndex < fileContent.length()) {
			
			token = new Token();
			
			// le primeiro caracter
			controlCharacter = fileContent.charAt(fileContentIndex);
			fileContentIndex++;
			
			// le todos os espaços
			if (controlCharacter == ' ') {
				while (controlCharacter == ' ') {
					if (fileContentIndex < fileContent.length()) {
						controlCharacter = fileContent.charAt(fileContentIndex);
						if (controlCharacter == ' ')
							fileContentIndex++;
					} else {
						break;
					}
				}
			} else {
				if (controlCharacter == '\n') {
					while (controlCharacter == '\n') {
						if (fileContentIndex < fileContent.length()) {
							controlCharacter = fileContent.charAt(fileContentIndex);
							if (controlCharacter == '\n')
								fileContentIndex++;
						} else {
							break;
						}
					}
				} else {
					// ler comentario
					if (controlCharacter == '{') {
						// procura caracter de fim de comentario
						while (controlCharacter != '}') {
							// caso for fim de arquivo e n tiver fim de comentario
							if (fileContentIndex < fileContent.length()) {
								controlCharacter = fileContent.charAt(fileContentIndex);
								fileContentIndex++;
							} else {
								throw new LexicoException("Sem fim de comentário na linha: " + returnLineOfToken(fileContentIndex - 1, fileContent));
							}
							
						}
						
					} else {
						if (Character.isDigit(controlCharacter)) {
							tokenBuilder = "";
							while(Character.isDigit(controlCharacter)) {
								tokenBuilder = tokenBuilder + controlCharacter;
								if (fileContentIndex < fileContent.length()) {
									controlCharacter = fileContent.charAt(fileContentIndex);
									fileContentIndex++;
								} else {
									break;
								}
							}
							
							token.setSimbolo("snumero");
							token.setLexema(tokenBuilder);
							token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
							tokens.add(token);
							
						} else {
							if (Character.isLetter(controlCharacter) && controlCharacter != ';' && controlCharacter != ':') {
								tokenBuilder = "";
								while((controlCharacter == '_' || Character.isDigit(controlCharacter) || Character.isLetter(controlCharacter)) && controlCharacter != ';' && controlCharacter != ':' && controlCharacter != '\n') {
									tokenBuilder += controlCharacter;
									if (fileContentIndex < fileContent.length()) {
										controlCharacter = fileContent.charAt(fileContentIndex);
										if ((controlCharacter == '_' || Character.isDigit(controlCharacter) || Character.isLetter(controlCharacter)) && controlCharacter != ';' && controlCharacter != ':' && controlCharacter != '\n')
											fileContentIndex++;
									} else {
										break;
									}
								}
								
								token.setLexema(tokenBuilder);
								token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
								switch (tokenBuilder) {
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
								tokens.add(token);
							} else {
								if (controlCharacter == ':') {
									tokenBuilder = "";
									if (fileContentIndex < fileContent.length()) {
										if (fileContent.charAt(fileContentIndex) == '=') {
											fileContentIndex++;
											tokenBuilder = ":=";
											token.setLexema(tokenBuilder);
											token.setSimbolo("satribuicao");
											token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
											tokens.add(token);
										}else {
											tokenBuilder = ":";
											token.setLexema(tokenBuilder);
											token.setSimbolo("sdoispontos");
											token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
											tokens.add(token);
										}
									} else {
										tokenBuilder = ":";
										token.setLexema(tokenBuilder);
										token.setSimbolo("sdoispontos");
										token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
										tokens.add(token);
									}
								} else {
									if (controlCharacter == '+' || controlCharacter == '-' || controlCharacter == '*') {
			//							Tratar Operador Aritmetico
										tokenBuilder = "";
										if (controlCharacter == '+') {
											token.setSimbolo("smais");
										} else {
											if (controlCharacter == '-') {
												token.setSimbolo("smenos");
											} else {
												if (controlCharacter == '*') {
													token.setSimbolo("smult");
												}
											}
										}
										tokenBuilder = Character.toString(controlCharacter);
										token.setLexema(tokenBuilder);
										token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
										tokens.add(token);
									}else {
										if (controlCharacter == '<' || controlCharacter == '>' || controlCharacter == '=') {
			//								Trata Operador Relacional
											tokenBuilder = "";
											if (controlCharacter == '=') {
												token.setSimbolo("sig");
											} else {
												if (controlCharacter == '>') {
													if (fileContentIndex < fileContent.length()) {
														if (fileContent.charAt(fileContentIndex) == '=') {
															fileContentIndex++;
															tokenBuilder += Character.toString(controlCharacter);
															controlCharacter = fileContent.charAt(fileContentIndex);
															token.setSimbolo("smaiorig");	
														} else {
															token.setSimbolo("smaior");
														}
													} else {
														token.setSimbolo("smaior");
													}
												} else {
													if (fileContentIndex < fileContent.length()) {
														if (fileContent.charAt(fileContentIndex) == '=') {
															fileContentIndex++;
															tokenBuilder += Character.toString(controlCharacter);
															controlCharacter = fileContent.charAt(fileContentIndex);
															token.setSimbolo("smenorig");	
														} else {
															token.setSimbolo("smenor");
														}
													} else {
														token.setSimbolo("smenor");
													}
												}
											}
											tokenBuilder += Character.toString(controlCharacter);
											token.setLexema(tokenBuilder);
											token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
											tokens.add(token);
										}else {
											if (controlCharacter == ';' || controlCharacter == ',' || controlCharacter == '(' || controlCharacter == ')' || controlCharacter == '.') {
			//									Trata Pontuacao
												if(controlCharacter == ';') {
													tokenBuilder = ";";
													token.setLexema(tokenBuilder);
													token.setSimbolo("sponto_virgula");
													token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
												}
												if(controlCharacter == ',') {
													tokenBuilder = ",";
													token.setLexema(tokenBuilder);
													token.setSimbolo("svirgula");
													token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
												}
												if(controlCharacter == '(') {
													tokenBuilder = "(";
													token.setLexema(tokenBuilder);
													token.setSimbolo("sabre_parenteses");
													token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
												}
												if(controlCharacter == ')') {
													tokenBuilder = ")";
													token.setLexema(tokenBuilder);
													token.setSimbolo("sfecha_parenteses");
													token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
												}
												if(controlCharacter == '.') {
													tokenBuilder = ".";
													token.setLexema(tokenBuilder);
													token.setSimbolo("sponto");
													token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
												}
												tokens.add(token);
											} else {
												throw new LexicoException("Erro Léxico na linha:" + returnLineOfToken(fileContentIndex - 1, fileContent));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
			
		for (int i = 0; i < tokens.size(); i++) {
			if (i > 0) {
				if (tokens.get(i - 1).getLinha() < tokens.get(i).getLinha()) {
					System.out.print('\n');
				}
			}
			System.out.print("<" + tokens.get(i).getSimbolo() + "(" + tokens.get(i).getLexema() + ")" + ">");
		}
	}

	
	static int returnLineOfToken(int fileContentIndex, String fileContent) {
		int lineNumber;
		int i;
		for (lineNumber = 0, i = 0; i < fileContentIndex; i++) {
			if (fileContent.charAt(i) == '\n') {
				lineNumber++;
			}
		}
		return lineNumber + 1;
	}
}
