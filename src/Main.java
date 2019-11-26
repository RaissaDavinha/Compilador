import java.io.IOException;
import java.util.ArrayList;

public class Main {
	static int rotulo;
	static int variaveisDeclaradas = 0;
	static Token token = new Token();
	static Token atribToken = new Token();
	static AnalisadorLexico analisadorLexico;
	static GeradorCodigo geradorCodigo;
	static TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();
	static ArrayList<Integer> nivelList = new ArrayList<Integer>();
	static ArrayList<Token> infixList;
	static ArrayList<Token> postfixList;
	static int nivelMax = 0;

	public static void main(String[] args) throws IOException, LexicoException, SintaticoException, SemanticoException {

		try {
			analisadorLexico = new AnalisadorLexico("teste1.txt");
			geradorCodigo = new GeradorCodigo();
//			 teste lexico
//			Token lastToken = null;
//			try {
//			lastToken = token = analisadorLexico.getToken();
//			while (token != null) {
//				if (lastToken.getLinha() < token.getLinha()) {
//					System.out.print('\n');
//				}
//				System.out.print("<" + token.getSimbolo() + "(" + token.getLexema() + ")" + ">");
//				lastToken = token;
//				token = analisadorLexico.getToken();
//				}
//			} catch (IndexOutOfBoundsException indexError) {
//				System.out.print(indexError.getMessage());
//			}
			
			rotulo = 1;
			token = analisadorLexico.getToken();
			nivelList.add(0);

			if (token.simbolo == "sprograma") {
				token = analisadorLexico.getToken();
				if (token.simbolo == "sidentificador") {
					// insere_table(token.lexema, "nomedeprograma","","");
					tabelaSimbolos.insereTabela(token.lexema, "nomedeprograma", nivelList.get(nivelList.size() - 1));
					token = analisadorLexico.getToken();
					if (token.simbolo == "sponto_virgula") {
						// comeca analisa_bloco
						analisaBloco();
						if (token.simbolo == "sponto") {
							if (analisadorLexico.getToken().getSimbolo() == "error") {
								System.out.print("Compilado com sucesso");
							} else {
								throw new SintaticoException(
										"Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
												+ " na linha:" + token.linha + ", coluna:" + token.coluna);
							}
						} else {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "("
									+ token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema
								+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
					if (token.simbolo == "sponto") {
//						Verifica se acabou arquivo e comentarios(Nao deve precisar)
					}
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
							+ " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}

		} catch (IOException ioException) {
			System.out.print("Erro ao abrir o arquivo: " + ioException.getMessage());
		} catch (LexicoException lexicoException) {
			System.out.print(lexicoException.getMessage());
		}
	}

	public static void analisaBloco() throws IOException, SintaticoException, LexicoException, SemanticoException {
		token = analisadorLexico.getToken();
		analisaEtVariaveis();
		analisaSubrotina();
		analisaComandos();
	}

	public static void analisaComandos() throws IOException, SintaticoException, LexicoException, SemanticoException {
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
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
							+ " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			}
			token = analisadorLexico.getToken();
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}

	public static void analisaComandoSimples() throws IOException, SintaticoException, LexicoException, SemanticoException {
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

	public static void analisaAtribChProcedimento() throws SintaticoException, IOException, LexicoException, SemanticoException {
		atribToken = token;
		token = analisadorLexico.getToken();
		if (token.simbolo == "satribuicao") {
			
			infixList = new ArrayList<Token>();
			token = analisadorLexico.getToken();
			analisaExpressao();
			
			// gera posfix para comando se
			postfixList = geradorCodigo.geraPostFix(infixList);
			
			
			
			for (Token postfix : postfixList) { 		      
		           //System.out.print("<" + postfix.lexema + "(" + postfix.simbolo + ")>"); 		
				System.out.print(postfix.lexema);
			}
			System.out.println("");
			
			if (tabelaSimbolos.verificaVariavelInteiro(atribToken.lexema, nivelList)) {
				atribToken.simbolo = "variavel inteiro";
			} else {
				atribToken.simbolo = "variavel booleano";
			}
			
			if (atribToken.simbolo == "variavel inteiro") {
				if (!geradorCodigo.validaPostFixInteiro(postfixList)) {
					throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema
							+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				if (!geradorCodigo.validaPostFixBooleano(postfixList)) {
					throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema
							+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			}
			
		} else {
			chamadaProcedimento();
		}
	}

	public static void analisaSe() throws SintaticoException, IOException, LexicoException, SemanticoException {
		infixList = new ArrayList<Token>();
		token = analisadorLexico.getToken();
		analisaExpressao();
		
		// gera posfix para comando se
		postfixList = geradorCodigo.geraPostFix(infixList);
		
		for (Token postfix : postfixList) { 		      
	           //System.out.print("<" + postfix.lexema + "(" + postfix.simbolo + ")>"); 		
			System.out.print(postfix.lexema);
		}
		System.out.println("");
		
		
		if (!geradorCodigo.validaPostFixBooleano(postfixList)) {
			throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema
					+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
		
		if (token.simbolo == "sentao") {
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			if (token.simbolo == "ssenao") {
				token = analisadorLexico.getToken();
				analisaComandoSimples();
			}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}

	public static void analisaEnquanto() throws SintaticoException, IOException, LexicoException, SemanticoException {
		infixList = new ArrayList<Token>();
		// Def auxrot1, auxrot2 inteiro

		// auxrot1 := rotulo
		// Gera(rotulo, NULL,'','') {inicio do while}
		// rotulo := rotulo + 1
		token = analisadorLexico.getToken();
		analisaExpressao();
		
		// gera posfix para comando se
		postfixList = geradorCodigo.geraPostFix(infixList);
		
		for (Token postfix : postfixList) { 		      
	           //System.out.print("<" + postfix.lexema + "(" + postfix.simbolo + ")>"); 		
			System.out.print(postfix.lexema);
		}
		System.out.println("");
		
		
		if (!geradorCodigo.validaPostFixBooleano(postfixList)) {
			throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema
					+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
		
		
		
		if (token.simbolo == "sfaca") {
			// auxtor2 := rotulo
			// Gera('', JMPF,rotulo,'') {salta se falso}
			// rotulo := rotulo + 1
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			// Gera('', JMPF,auxrot1,'') {retorna ao inicio loop}
			// Gera(auxrot2, NULL,'','') {fim do while}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}

	public static void analisaLeia() throws SintaticoException, SemanticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
//					Pesquisa em toda tabela ?
					if (tabelaSimbolos.verificaVarDeclarada(token.lexema, nivelList)) {
						token = analisadorLexico.getToken();

						if (token.simbolo == "sfecha_parenteses") {
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "("
									+ token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					} else {
						throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema
								+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}

	}

	public static void analisaEscreva() throws SintaticoException, SemanticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
				if (tabelaSimbolos.verificaVarDeclarada(token.lexema, nivelList)) {
					token = analisadorLexico.getToken();
					if (token.simbolo == "sfecha_parenteses") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema
								+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				} else {
					throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
							+ " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} else {
			throw new SintaticoException("Erro Sintatico na linha:" + token.linha);
		}
	}

	public static void analisaFator() throws SintaticoException, IOException, LexicoException, SemanticoException {
		if (token.simbolo == "sidentificador") {
			// se pesquisaTabela(token.lexema, nivel, ind)
			if (tabelaSimbolos.verificaDeclaradoTudo(token.getLexema(), nivelList)) {
				// se (TabSimb[ind].tipo == "funcao inteiro") ou (TabSimb[ind].tipo == "funcao booleano")
				if (tabelaSimbolos.verificaIndentificadorFuncao(token.getLexema(), nivelList)) {
					infixList.add(token);
				// analisa_chamada_funcao
					
				} else {
					// senao ler token
					if (tabelaSimbolos.verificaVariavelInteiro(token.getLexema(), nivelList)) {
						token.simbolo = "variavel inteiro";
					} else {
						token.simbolo = "variavel booleano";
					}
					infixList.add(token);
					token = analisadorLexico.getToken();	
				}
			} else {
				throw new SemanticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} else {
			if (token.simbolo == "snumero") {
				infixList.add(token);
				token = analisadorLexico.getToken();
			} else {
				if (token.simbolo == "snao") {
					infixList.add(token);
					token = analisadorLexico.getToken();
					analisaFator();
				} else {
					if (token.simbolo == "sabre_parenteses") {
						infixList.add(token);
						token = analisadorLexico.getToken();
						analisaExpressao();
						if (token.simbolo == "sfecha_parenteses") {
							infixList.add(token);
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "("
									+ token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					} else {
						if (token.lexema == "verdadeiro" || token.lexema == "falso") {
							infixList.add(token);
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "("
									+ token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					}
				}
			}
		}
	}

	public static void analisaExpressao() throws IOException, LexicoException, SintaticoException, SemanticoException {
		analisaExpressaoSimples();
		if (token.simbolo == "smaior" || token.simbolo == "smaiorig" || token.simbolo == "sig"
				|| token.simbolo == "smenor" || token.simbolo == "smenorig" || token.simbolo == "sdif") {
			infixList.add(token);
			token = analisadorLexico.getToken();
			analisaExpressaoSimples();
		}
	}

	public static void analisaExpressaoSimples() throws IOException, LexicoException, SintaticoException, SemanticoException {
		if (token.simbolo == "smenos") { // caso der erro colocar token.simbolo == "smais" || de volta
			infixList.add(token);
			token = analisadorLexico.getToken();
		}
		analisaTermo();
		while (token.simbolo == "smais" || token.simbolo == "smenos" || token.simbolo == "sou") {
			infixList.add(token);
			token = analisadorLexico.getToken();
			analisaTermo();
		}
	}

	public static void analisaTermo() throws SintaticoException, IOException, LexicoException, SemanticoException {
		analisaFator();
		while (token.simbolo == "smult" || token.simbolo == "sdiv" || token.simbolo == "se") {
			infixList.add(token);
			token = analisadorLexico.getToken();
			analisaFator();
		}
	}

	public static void analisaEtVariaveis() throws SintaticoException, SemanticoException, IOException, LexicoException {
		if (token.simbolo == "svar") {
			variaveisDeclaradas = 0;
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
				while (token.simbolo == "sidentificador") {
					analisaVariaveis();
					if (token.simbolo == "sponto_virgula") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema
								+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		}
	}

	public static void analisaVariaveis() throws SintaticoException, SemanticoException, IOException, LexicoException {
		do {
			if (token.simbolo == "sidentificador") {
				if (!tabelaSimbolos.verificaDeclaDuplicVar(token.lexema, nivelList)) {
					tabelaSimbolos.insereTabela(token.lexema, "variavel", nivelList.get(nivelList.size() - 1));
					variaveisDeclaradas++;
					token = analisadorLexico.getToken();
					if (token.simbolo == "svirgula" || token.simbolo == "sdoispontos") {
						if (token.simbolo == "svirgula") {
							token = analisadorLexico.getToken();
							if (token.simbolo == "sdoispontos") {
								throw new SintaticoException(
										"Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
												+ " na linha:" + token.linha + ", coluna:" + token.coluna);
							}
						}
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema
								+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				} else {
					throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
							+ " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} while (token.simbolo != "sdoispontos");
		token = analisadorLexico.getToken();
		analisaTipo();
	}

	public static void analisaTipo() throws SintaticoException, IOException, LexicoException {
		if (token.simbolo != "sinteiro" && token.simbolo != "sbooleano") {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		} else {
			// coloca_tipo_tabela(token.lexema)
			tabelaSimbolos.colocaTipo("variavel " + token.lexema, variaveisDeclaradas);
			
			variaveisDeclaradas = 0;
		}
		token = analisadorLexico.getToken();
	}

	public static void analisaSubrotina() throws SintaticoException, IOException, LexicoException, SemanticoException {
		// Def auxrot, flag inteiro
		// flag = 0
		if (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			// auxrot := rotulo
			// Gera('', JMP, rotulo, '') {Salta sub-rotinas}
			// rotulo := rotulo + 1
			// flag = 1
		}
		while (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			if (token.simbolo == "sprocedimento") {
				analisaDeclaracaoProcedimento();
			} else {
				analisaDeclaracaoFuncao();
			}
			if (token.simbolo == "sponto_virgula") {
				token = analisadorLexico.getToken();
			} else {
				throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		}
		// if flag = 1
		// entao Gera(auxrot,NULL'','') {inicio do principal}
	}

	public static void analisaDeclaracaoProcedimento()
			throws SintaticoException, IOException, LexicoException, SemanticoException {
		token = analisadorLexico.getToken();
		nivelMax++;
		nivelList.add(nivelMax);

		if (token.simbolo == "sidentificador") {
			// pesquisa_declproc_tabela(token.lexema)
			if (!tabelaSimbolos.verificaDeclaDuplicProc(token.lexema, nivelList)) {
				// se nao encontrou
				// insere_tabela(token.lexema, "procedimento", nivel, rotulo) {guarda na
				// TabSimb}
				tabelaSimbolos.insereTabela(token.getLexema(), "procedimento", nivelList.get(nivelList.size() - 1));

				// Gera(rotulo,NULL,'','') {CALL ira buscar esse rotulo na TabSimb}
				// rotulo := rotulo + 1

				token = analisadorLexico.getToken();

				if (token.simbolo == "sponto_virgula") {
					analisaBloco();
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
							+ " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				throw new SemanticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
		// DESEMPILHA OU VOLTA NIVEL
		nivelList.remove(nivelList.size() - 1);
	}

	public static void analisaDeclaracaoFuncao() throws SintaticoException, SemanticoException, IOException, LexicoException, SemanticoException {
		token = analisadorLexico.getToken();
		// nivel := "L" (marca ou novo galho)
		nivelMax++;
		nivelList.add(nivelMax);
		if (token.simbolo == "sidentificador") {
			// pesquisa_declfunc_tabela(token.lexema) se tem identificador duplicado
			if (!tabelaSimbolos.verificaDeclaDuplicProc(token.simbolo, nivelList)) {
				// se nao encontrou
				// insere_tabela

				token = analisadorLexico.getToken();

				if (token.simbolo == "sdoispontos") {
					token = analisadorLexico.getToken();

					if (token.simbolo == "sinteiro" || token.simbolo == "sbooleano") {
						// se (token.simbolo = sinteiro)
						if (token.simbolo == "sinteiro") {
							tabelaSimbolos.insereTabela(token.getLexema(), "funcao inteiro", nivelList.get(nivelList.size() - 1));
							// TABSIMG[pc].tipo := "funcao inteiro"
						}
						// senao
						if (token.simbolo == "sbooleano") {
							tabelaSimbolos.insereTabela(token.getLexema(), "funcao booleano", nivelList.get(nivelList.size() - 1));
							// TABSIMG[pc].tipo := "funcao booleano"
						}

						token = analisadorLexico.getToken();

						if (token.simbolo == "sponto_virgula") {
							analisaBloco();
						}
					} else {
						throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema
								+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
				} else {
					throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
							+ " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
			} else {
				throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
		// DESEMPILHA OU VOLTA NIVEL
		nivelList.remove(nivelList.size() - 1);
	}

	public static void chamadaProcedimento() throws SintaticoException, IOException, LexicoException {
		if (token.simbolo == "sponto_virgula") {

		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}
}