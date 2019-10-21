import java.io.IOException;

public class Main {
	static int rotulo;
	static Token token = new Token();

	public static void main(String[] args) throws IOException, LexicoException, SintaticoException {
		rotulo = 1;
		token = AnalisadorLexico.getToken();

		if (token.simbolo == "sprograma") {
			token = AnalisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
				token = AnalisadorLexico.getToken(); 
				if (token.simbolo == "sponto_virgula") {
					//comeca analisa_bloco
					analisaBloco();
				} else {
					throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
				}
				if (token.simbolo == "sponto") {
//					Verifica se acabou arquivo e comentarios(Nao deve precisar)
				}
			}

		}
	}
	
	public static void analisaBloco() throws IOException, SintaticoException, LexicoException {
		token = AnalisadorLexico.getToken();
		analisaEtVariaveis();
		analisaSubrotina();
		analisaComando();
	}
	
	public static void analisaComando() throws IOException, SintaticoException, LexicoException {
		if (token.simbolo == "sinicio") {
			token = AnalisadorLexico.getToken();
			//analisa comando simples
			analisaComandoSimples(token.simbolo);
			while (token.simbolo != "sfim") {
				if (token.simbolo == "sponto_virgula") {
					token = AnalisadorLexico.getToken();
					if (token.simbolo != "sfim") {
						analisaComandoSimples(token.simbolo);
					}
				} else {
					throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
				}
				token = AnalisadorLexico.getToken();
			}
		} else {
			throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
		}
	}

	public static void analisaComandoSimples(String simbolo) throws IOException, SintaticoException, LexicoException {
		switch (simbolo) {
		case "sidentificador":
//			Analisa_atrib_chprocedimento Ok
			token = AnalisadorLexico.getToken();
			if (token.simbolo == "satribuicao") {
//				Analisa Atribuicao Fazer (token, expressao) Ok?
				token = AnalisadorLexico.getToken();
				analisaExpressao();
			} else {
//				chama procedimento Fazer
			}
			break;
		case "sse":
//			Analisa_se ok
			token = AnalisadorLexico.getToken();
//			Analisa_expressao Ok
			analisaExpressao();
			if (token.simbolo == "sentao") {
				token = AnalisadorLexico.getToken();
//				analisa_comando_simples Ok
				analisaComandoSimples(token.simbolo);
				if (token.simbolo == "ssenao") {
					token = AnalisadorLexico.getToken();
//					analisa_comando_simples Oka
					analisaComandoSimples(token.simbolo);
				}
			}else {
				throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
			}
			break;
		case "senquanto":
//			Analisa_enquanto
			
//			Vermelho
			token = AnalisadorLexico.getToken();
//			Analisa_expressão Ok
			analisaExpressao();
			if (token.simbolo == "sfaca") {
				token = AnalisadorLexico.getToken();
//				analisa_expressao_simples Ok
				analisaExpressaoSimples();
				
			}else {
				throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
			}
			break;
		case "sleia":
//			Analisa_leia Ok
			token = AnalisadorLexico.getToken();
			if (token.simbolo == "sabre_parenteses") {
				token = AnalisadorLexico.getToken();
				if (token.simbolo == "sidentificador") {
//					if (pesquisa_declvar_tabela(token.lexema)) {
//						Pesquisa em toda tabela
						token = AnalisadorLexico.getToken();
						if (token.simbolo == "sfecha_parenteses") {
							token = AnalisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
						}
//					}else {
//						throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
//					}
				} else {
					throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
				}
			} else {
				throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
			}

			break;
			
		case "sescreva":
//			Analisa_ escreva Ok
			token = AnalisadorLexico.getToken();
			if (token.simbolo == "sabre_parenteses") {
				token = AnalisadorLexico.getToken();
				if (token.simbolo == "sidentificador") {
//					Azul
//					if (pesquisa_ declvarfunc_tabela(token.lexema)) {
						token = AnalisadorLexico.getToken();
						if (token.simbolo == "sfecha_parenteses") {
							token = AnalisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
						}
//					}else {
//						throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
//					}
				}else{
						throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
					}
			}else{
					throw new SintaticoException("Erro Sintatico na linha:"+token.linha);
				}
			break;
		}


	}

	public static void analisaFator() throws SintaticoException, IOException, LexicoException {
		if (token.simbolo == "sidentificador") {
//			Codigo azul
		} else {
			if (token.simbolo == "snumero") {
				token = AnalisadorLexico.getToken();
			} else {
				if (token.simbolo == "snao") {
					token = AnalisadorLexico.getToken();
//					analisa_fator Ok
					analisaFator();
				} else {
					if (token.simbolo == "sabre_parenteses") {
						token = AnalisadorLexico.getToken();
//						analisa_expressao Ok
						analisaExpressao();
						if (token.simbolo == "sfecha_parenteses") {
							token = AnalisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
						}
					} else {
						if (token.lexema == "verdadeiro" || token.lexema == "falso") {
							token = AnalisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
						}
					}
				}
			}
		}
	}
	
	public static void analisaExpressao() throws IOException, LexicoException, SintaticoException {
		analisaExpressaoSimples();
		if (token.simbolo == "smaior" || token.simbolo == "smaiorig" || token.simbolo == "sig" || token.simbolo == "smenor" || token.simbolo == "smenorig" || token.simbolo == "sdif" ){
			token = AnalisadorLexico.getToken();
			analisaExpressaoSimples();
		}
	}
	
	public static void analisaExpressaoSimples() throws IOException, LexicoException, SintaticoException {
		if (token.simbolo == "smais" || token.simbolo == "smenos") {
			token = AnalisadorLexico.getToken();
			analisaTermo();
			while(token.simbolo == "smais" || token.simbolo == "smenos" || token.simbolo == "sou") {
				token = AnalisadorLexico.getToken();
				analisaTermo();
			}
		}
	}

	public static void analisaTermo() throws SintaticoException, IOException, LexicoException {
		analisaFator();
		while(token.simbolo == "smult" || token.simbolo == "sdiv" || token.simbolo == "se") {
			token = AnalisadorLexico.getToken();
			analisaFator();
		}
	}

	public static void analisaEtVariaveis() throws SintaticoException, IOException, LexicoException {
		if(token.simbolo == "svar") {
			token = AnalisadorLexico.getToken();
			if(token.simbolo == "sidentificador") {
				while (token.simbolo == "sidentificador") {
					analisaVariaveis();
					token = AnalisadorLexico.getToken();
					if (token.simbolo == "sponto_virgula") {
						token = AnalisadorLexico.getToken();
					} else {
						throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
					}
				}
			}
		}	
	}

public static void analisaVariaveis() throws SintaticoException, IOException, LexicoException {
	do {
		if (token.simbolo == "sidentificador") {
				token = AnalisadorLexico.getToken();
				if (token.simbolo == "svirgula" || token.simbolo == "sdoispontos") {
					if (token.simbolo == "svirgula") {
						token = AnalisadorLexico.getToken();
						if (token.simbolo == "sdoispontos") {
							throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
						}
					}
				} else {
					throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
				}
//			} else {
//				throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
//			}
		} else {
			throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
		}

	} while (token.simbolo == "sdoispontos");
	token = AnalisadorLexico.getToken();
	//analisa_tipo
	if (token.simbolo != "sinteiro" && token.simbolo != "sbooleano") {
		throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
	} else {
//		coloca_tipo_tabela(token.lexema)
	}
	token = AnalisadorLexico.getToken();
}

	public static void analisaSubrotina() throws SintaticoException, IOException, LexicoException {
		if (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			while (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
				if(token.simbolo == "sprocedimento") {
					 analisa_declaração_procedimento();
				} else {
					analisa_declaração_funcao();
				}
				if(token.simbolo == "sponto_virgula") {
					token = AnalisadorLexico.getToken();
				} else {
					throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
				}
			}
		}
	}
	
	public static void analisa_declaração_procedimento() throws SintaticoException, IOException, LexicoException {
		token = AnalisadorLexico.getToken();
		if(token.simbolo =="sidentificador") {
			token = AnalisadorLexico.getToken();
			if(token.simbolo == "sponto_virgula") {
				analisaBloco();
			} else {
				throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
			}
		} else {
			throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
		}
	}
	
	public static void analisa_declaração_funcao() throws SintaticoException, IOException, LexicoException {
		token = AnalisadorLexico.getToken();
		if(token.simbolo =="sidentificador") {
			token = AnalisadorLexico.getToken();
			if(token.simbolo == "sdoispontos") {
				token = AnalisadorLexico.getToken();
				if(token.simbolo == "sinteiro" || token.simbolo == "sbooleano") {
					token = AnalisadorLexico.getToken();
					if(token.simbolo == "sponto_virgula") {
						analisaBloco();
					}
				} else {
					throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
				}
			} else {
				throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
			}
		} else {
			throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
		}
	}
	
	public static void chamadaProcedimento() throws SintaticoException, IOException, LexicoException {
		if(token.simbolo == "sponto_virgula") {
			
		} else {
			throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
		}
	}
}