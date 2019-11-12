import java.io.IOException;

import javax.swing.SwingUtilities;

public class SintaticMain {
	static int rotulo;
	static Token token = new Token();
	static AnalisadorLexico analisadorLexico;
	static TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();
	static String nivel;
	
	public static void sintaticMain() throws IOException, LexicoException, SintaticoException {
		
		try {
			analisadorLexico = new AnalisadorLexico("oi.txt");
			
			// teste lexico
			//Token lastToken = null;
			//try {
			//lastToken = token = analisadorLexico.getToken();
			//while (token != null) {
			//	if (lastToken.getLinha() < token.getLinha()) {
			//		System.out.print('\n');
			//	}
			//	System.out.print("<" + token.getSimbolo() + "(" + token.getLexema() + ")" + ">");
			//	lastToken = token;
			//	token = analisadorLexico.getToken();
			//	}
			//} catch (IndexOutOfBoundsException indexError) {
			//	System.out.print(indexError.getMessage());
			//}
			
			rotulo = 1;
			token = analisadorLexico.getToken();

			if (token.simbolo == "sprograma") {
				token = analisadorLexico.getToken();
				if (token.simbolo == "sidentificador") {
					// insere_table(token.lexema, "nomedeprograma","","");
					tabelaSimbolos.insereTabela(token.lexema, "nomedeprograma");
					token = analisadorLexico.getToken(); 
					if (token.simbolo == "sponto_virgula") {
						//comeca analisa_bloco
						analisaBloco();
						if (token.simbolo == "sponto") {
							if(analisadorLexico.getToken().getSimbolo() == "error") {
								System.out.print("Compilado com sucesso");
							} else {
								throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
							}
						} else {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
					if (token.simbolo == "sponto") {
//						Verifica se acabou arquivo e comentarios(Nao deve precisar)
					}
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
			
		} catch (IOException ioException) {
			System.out.print("Erro ao abrir o arquivo: " + ioException.getMessage());
		} catch (LexicoException lexicoException) {
			System.out.print(lexicoException.getMessage());
		}
	}
	
	public static void analisaBloco() throws IOException, SintaticoException, LexicoException {
		token = analisadorLexico.getToken();
		analisaEtVariaveis();
		analisaSubrotina();
		analisaComandos();
	}
	
	public static void analisaComandos() throws IOException, SintaticoException, LexicoException {
		if (token.simbolo == "sinicio") {
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			while (token.simbolo != "sfim") {
				if (token.simbolo == "sponto_virgula") {
					token = analisadorLexico.getToken();
					if (token.simbolo != "sfim") {
						analisaComandoSimples();
					}
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			}
			token = analisadorLexico.getToken();
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}

	public static void analisaComandoSimples() throws IOException, SintaticoException, LexicoException {
		switch (token.simbolo) {
			
			case "sidentificador":
			analisaAtribChProcedimento();
			break;
			
			case "sse":
			analisaSe();
			break;
			
			case "senquanto":
			analisaEnquanto();
			break;
			
			case "sleia":
			analisaLeia();
			break;
			
			case "sescreva":
			analisaEscreva();
			break;
			
			default:
			analisaComandos();
		}
	}
	
	public static void analisaAtribChProcedimento() throws SintaticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "satribuicao") {
			token = analisadorLexico.getToken();
			analisaExpressao();
		} else {
			chamadaProcedimento();
		}
	}
	
	public static void analisaSe() throws SintaticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		analisaExpressao();
		if (token.simbolo == "sentao") {
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			if (token.simbolo == "ssenao") {
				token = analisadorLexico.getToken();
				analisaComandoSimples();
			}
		}else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}
	public static void analisaEnquanto() throws SintaticoException, IOException, LexicoException {
		// Def auxrot1, auxrot2 inteiro
		
		// auxrot1 := rotulo
		// Gera(rotulo, NULL,'','') {inicio do while}
		// rotulo := rotulo + 1
		token = analisadorLexico.getToken();
		analisaExpressao();
		if (token.simbolo == "sfaca") {
			// auxtor2 := rotulo
			// Gera('', JMPF,rotulo,'') {salta se falso}
			// rotulo := rotulo + 1
			token = analisadorLexico.getToken();
			analisaExpressaoSimples();
			// Gera('', JMPF,auxrot1,'') {retorna ao inicio loop}
			// Gera(auxrot2, NULL,'','') {fim do while}
		}else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}
	public static void analisaLeia() throws SintaticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
				if (tabelaSimbolos.pesquisaDuplicaVar(token.lexema)) {
//					Pesquisa em toda tabela ?
					token = analisadorLexico.getToken();
					if (token.simbolo == "sfecha_parenteses") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				}else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}

	}
	public static void analisaEscreva() throws SintaticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
				if (tabelaSimbolos.pesquisaDuplicaVar(token.lexema)) {
					token = analisadorLexico.getToken();
					if (token.simbolo == "sfecha_parenteses") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				}else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else{
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
		} else{
				throw new SintaticoException("Erro Sintatico na linha:"+token.linha);
		}
	}

	
	public static void analisaFator() throws SintaticoException, IOException, LexicoException {
		if (token.simbolo == "sidentificador") {
			// se pesquisaTabela(token.lexema, nivel, ind) {
				// se (TabSimb[ind].tipo == "funcao inteiro") ou (TabSimb[ind].tipo == "funcao  booleano")
					// analisa_chamada_funcao
				// senao
					token = analisadorLexico.getToken();
			// senao ERRO
		} else {
			if (token.simbolo == "snumero") {
				token = analisadorLexico.getToken();
			} else {
				if (token.simbolo == "snao") {
					token = analisadorLexico.getToken();
					analisaFator();
				} else {
					if (token.simbolo == "sabre_parenteses") {
						token = analisadorLexico.getToken();
						analisaExpressao();
						if (token.simbolo == "sfecha_parenteses") {
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					} else {
						if (token.lexema == "verdadeiro" || token.lexema == "falso") {
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					}
				}
			}
		}
	}
	
	public static void analisaExpressao() throws IOException, LexicoException, SintaticoException {
		analisaExpressaoSimples();
		if (token.simbolo == "smaior" || token.simbolo == "smaiorig" || token.simbolo == "sig" || token.simbolo == "smenor" || token.simbolo == "smenorig" || token.simbolo == "sdif" ){
			token = analisadorLexico.getToken();
			analisaExpressaoSimples();
		}
	}
	
	public static void analisaExpressaoSimples() throws IOException, LexicoException, SintaticoException {
		if (token.simbolo == "smais" || token.simbolo == "smenos") {
			token = analisadorLexico.getToken();
		}
		analisaTermo();
		while(token.simbolo == "smais" || token.simbolo == "smenos" || token.simbolo == "sou") {
			token = analisadorLexico.getToken();
			analisaTermo();
		}
	}

	public static void analisaTermo() throws SintaticoException, IOException, LexicoException {
		analisaFator();
		while(token.simbolo == "smult" || token.simbolo == "sdiv" || token.simbolo == "se") {
			token = analisadorLexico.getToken();
			analisaFator();
		}
	}

	public static void analisaEtVariaveis() throws SintaticoException, IOException, LexicoException {
		if(token.simbolo == "svar") {
			token = analisadorLexico.getToken();
			if(token.simbolo == "sidentificador") {
				while (token.simbolo == "sidentificador") {
					analisaVariaveis();
					if (token.simbolo == "sponto_virgula") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		}	
	}

public static void analisaVariaveis() throws SintaticoException, IOException, LexicoException {
	do {
		if (token.simbolo == "sidentificador") {
			if (tabelaSimbolos.pesquisaDuplicaVar(token.lexema)) {
				tabelaSimbolos.insereTabela(token.lexema, "variavel" );
				token = analisadorLexico.getToken();
				if (token.simbolo == "svirgula" || token.simbolo == "sdoispontos") {
					if (token.simbolo == "svirgula") {
						token = analisadorLexico.getToken();
						if (token.simbolo == "sdoispontos") {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					}
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			}else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	} while (token.simbolo != "sdoispontos");
	token = analisadorLexico.getToken();
	analisaTipo();
}

public static void analisaTipo() throws SintaticoException, IOException, LexicoException {
	if (token.simbolo != "sinteiro" && token.simbolo != "sbooleano") {
		throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
	} else {
		// coloca_tipo_tabela(token.lexema)
		tabelaSimbolos.colocaTipo(token.lexema);
	}
	token = analisadorLexico.getToken();
}
	public static void analisaSubrotina() throws SintaticoException, IOException, LexicoException {
		// Def auxrot, flag inteiro
		// flag = 0
		if (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			// auxrot := rotulo
			// Gera('', JMP, rotulo, '') {Salta sub-rotinas}
			// rotulo := rotulo + 1
			// flag = 1
		}
		while (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			if(token.simbolo == "sprocedimento") {
				 analisaDeclaracaoProcedimento();
			} else {
				analisaDeclaracaoFuncao();
			}
			if(token.simbolo == "sponto_virgula") {
				token = analisadorLexico.getToken();
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		}
		// if flag = 1
		// entao Gera(auxrot,NULL'','') {inicio do principal}
	}
	
	public static void analisaDeclaracaoProcedimento() throws SintaticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		// nivel := "L" (marca ou novo galho)
		nivel = "L";
		if(token.simbolo =="sidentificador") {
			// pesquisa_declproc_tabela(token.lexema)
			// se nao encontrou {
				// insere_tabela(token.lexema, "procedimento", nivel, rotulo) {guarda na TabSimb}
				// Gera(rotulo,NULL,'','') {CALL ira buscar esse rotulo na TabSimb}
				// rotulo := rotulo + 1
				token = analisadorLexico.getToken();
				if(token.simbolo == "sponto_virgula") {
					analisaBloco();
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			// senao ERRO
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
		// DESEMPILHA OU VOLTA NIVEL
	}
	
	public static void analisaDeclaracaoFuncao() throws SintaticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		// nivel := "L" (marca ou novo galho)
		nivel = "L";
		if(token.simbolo =="sidentificador") {
			// pesquisa_declfunc_tabela(token.lexema)
			// se nao encontrou
				// insere_tabela
				token = analisadorLexico.getToken();
				if(token.simbolo == "sdoispontos") {
					token = analisadorLexico.getToken();
					if(token.simbolo == "sinteiro" || token.simbolo == "sbooleano") {
						// se (token.simbolo = sinteiro)
							// TABSIMG[pc].tipo := "funcao inteiro"
						// senao
							// TABSIMG[pc].tipo := "funcao booleano"
						token = analisadorLexico.getToken();
						if(token.simbolo == "sponto_virgula") {
							analisaBloco();
						}
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			// Senao ERRO
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
		// DESEMPILHA OU VOLTA NIVEL
	}
	
	public static void chamadaProcedimento() throws SintaticoException, IOException, LexicoException {
		if(token.simbolo == "sponto_virgula") {
			
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}
}