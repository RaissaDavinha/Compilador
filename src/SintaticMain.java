import java.io.IOException;
import java.util.ArrayList;

public class SintaticMain {
	static int rotulo;
	static int variaveisDeclaradas = 0;
	static Token token = new Token();
	static Token auxToken = new Token();
	static AnalisadorLexico analisadorLexico;
	static GeradorCodigo geradorCodigo;
	static TabelaSimbolos tabelaSimbolos;
	static ArrayList<Integer> nivelList;
	static ArrayList<Token> infixList;
	static ArrayList<Token> postfixList;
	static int nivelMax = 0;
	static int allocIndex = 0;
	static int rotuloVariavel = 0;
	static ArrayList<Integer> procFuncRotuloStack;
	static ArrayList<Integer> allocStack;
	static ArrayList<Integer> allocPerFuncProcStack;

	static ArrayList<String> procFunDeclaPath;
	static boolean procurandoRetorno = false;
	static ArrayList<String> seEntaoStack;

	public static void sintaticMain(String file)
			throws IOException, LexicoException, SintaticoException, SemanticoException {

		try {
			seEntaoStack = new ArrayList<String>();
			variaveisDeclaradas = 0;
			nivelList = new ArrayList<Integer>();
			tabelaSimbolos = new TabelaSimbolos();
			token = new Token();
			infixList = new ArrayList<Token>();
			postfixList = new ArrayList<Token>();
			nivelMax = 0;
			allocIndex = 0;
			rotuloVariavel = 0;
			procurandoRetorno = false;
			analisadorLexico = new AnalisadorLexico(file);
			geradorCodigo = new GeradorCodigo();
			procFuncRotuloStack = new ArrayList<Integer>();
			allocStack = new ArrayList<Integer>();
			allocPerFuncProcStack = new ArrayList<Integer>();
			allocPerFuncProcStack.add(0);
			procFunDeclaPath = new ArrayList<String>();

			rotulo = 0;
			token = analisadorLexico.getToken();
			nivelList.add(0);

			if (token.simbolo == "sprograma") {
				procFunDeclaPath.add("sprograma");
				token = analisadorLexico.getToken();
				if (token.simbolo == "sidentificador") {
					// insere_table(token.lexema, "nomedeprograma","","");
					tabelaSimbolos.insereTabela(token.lexema, "nomedeprograma", nivelList.get(nivelList.size() - 1),
							rotulo++);
					token = analisadorLexico.getToken();
					if (token.simbolo == "sponto_virgula") {

						geradorCodigo.geraStart();

						procurandoRetorno = false;
						// comeca analisa_bloco
						analisaBloco();
						if (token.simbolo == "sponto") {
							if (analisadorLexico.getToken().getSimbolo() == "error") {
								for (int dallocIndex = allocPerFuncProcStack
										.get(allocPerFuncProcStack.size() - 1); dallocIndex > 0; dallocIndex--) {
									geradorCodigo.geraDalloc(allocStack.get(allocStack.size() - 2),
											allocStack.get(allocStack.size() - 1));
									allocStack.remove(allocStack.size() - 1);
									allocStack.remove(allocStack.size() - 1);
								}
								allocPerFuncProcStack.remove(allocPerFuncProcStack.size() - 1);
								geradorCodigo.geraHlt();
								System.out.print("Compilado com sucesso");
								IDE.sendToConsole("Compilado com sucesso");
								geradorCodigo.geraArquivo();
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
		if (procFunDeclaPath.get(procFunDeclaPath.size() - 1) == "sfuncao") {
			procurandoRetorno = true;
			seEntaoStack = new ArrayList<String>();
			seEntaoStack.add("smainN");
		} else {
			procurandoRetorno = false;
		}
		analisaComandos();
		if (procurandoRetorno == true) {
			if (seEntaoStack.get(seEntaoStack.size() - 1) == "smainN") {
				throw new SemanticoException("Erro Semantico do token <" + auxToken.simbolo + "(" + auxToken.lexema
						+ ")>" + " na linha:" + auxToken.linha + ", coluna:" + auxToken.coluna);
			}
		}
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

	public static void analisaComandoSimples()
			throws IOException, SintaticoException, LexicoException, SemanticoException {
		System.out.println(token);
		switch (token.simbolo) {

		case "sidentificador":
			analisaAtribChProcedimento();
			break;

		case "sse":
			if (procurandoRetorno == true) {
				seEntaoStack.add("sseN");
			}
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

	public static void analisaAtribChProcedimento()
			throws SintaticoException, IOException, LexicoException, SemanticoException {
		auxToken = token;
		token = analisadorLexico.getToken();
		if (token.simbolo == "satribuicao") {

			if (!tabelaSimbolos.verificaDeclaradoTudo(auxToken.lexema, nivelList)) {
				throw new SemanticoException("Erro Semantico do token <" + auxToken.simbolo + "(" + auxToken.lexema
						+ ")>" + " na linha:" + auxToken.linha + ", coluna:" + auxToken.coluna);
			}

			infixList = new ArrayList<Token>();
			token = analisadorLexico.getToken();
			analisaExpressao();

			// gera posfix para comando se
			postfixList = geradorCodigo.geraPostFix(infixList);

			for (Token postfix : postfixList) {
				// System.out.print("<" + postfix.lexema + "(" + postfix.simbolo + ")>");
				System.out.print(postfix.lexema);
			}

			System.out.println("");

			switch (tabelaSimbolos.verificaTipoIndentificador(auxToken.lexema, nivelList)) {
			case 1:
				auxToken.simbolo = "variavel inteiro";
				break;
			case 2:
				auxToken.simbolo = "variavel booleano";
				break;
			case 3:
				auxToken.simbolo = "funcao inteiro";
				break;
			case 4:
				auxToken.simbolo = "funcao booleano";
				break;
			case 0:
				throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}

			ArrayList<Token> auxPostfix = new ArrayList<Token>();
			for (Token item : postfixList)
				auxPostfix.add(new Token(item));

			if (auxToken.simbolo == "variavel inteiro") {
				if (!geradorCodigo.validaPostFixInteiro(auxPostfix)) {
					throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
							+ " na linha:" + token.linha + ", coluna:" + token.coluna);
				}
				geradorCodigo.geraCodigoDaPosfix(postfixList, tabelaSimbolos, nivelList);
				// dar store
				geradorCodigo.geraStr(tabelaSimbolos.returnVarRotulo(auxToken.lexema, nivelList));
			} else {
				if (auxToken.simbolo == "variavel booleano") {
					if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
						throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema
								+ ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
					}
					geradorCodigo.geraCodigoDaPosfix(postfixList, tabelaSimbolos, nivelList);
					// dar store
					geradorCodigo.geraStr(tabelaSimbolos.returnVarRotulo(auxToken.lexema, nivelList));
				} else {
					if (auxToken.simbolo == "funcao inteiro") {
						if (procurandoRetorno == true) {
							// substitui ultimo caracter por S caso achar retorno
							seEntaoStack.set(seEntaoStack.size() - 1, seEntaoStack.get(seEntaoStack.size() - 1)
									.substring(0, seEntaoStack.get(seEntaoStack.size() - 1).length() - 1) + "S");
						}
						if (!geradorCodigo.validaPostFixInteiro(auxPostfix)) {
							throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "("
									+ token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
						geradorCodigo.geraCodigoDaPosfix(postfixList, tabelaSimbolos, nivelList);

						// valida se esta na funcao para dar retorno

						// dar return de funcao
						int allocQtd = 0;
						int allocStart = 0;
						if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
							for (int dallocIndex = 0; dallocIndex < allocPerFuncProcStack
									.get(allocPerFuncProcStack.size() - 1); dallocIndex++) {
								allocQtd += allocStack.get(allocStack.size() - 1 - dallocIndex * 2);
								allocStart = allocStack.get(allocStack.size() - 2 - dallocIndex * 2);
							}
						}
						geradorCodigo.geraReturnF(allocStart, allocQtd);
					} else {
						if (auxToken.simbolo == "funcao booleano") {
							if (procurandoRetorno == true) {
								// substitui ultimo caracter por S caso achar retorno
								seEntaoStack.set(seEntaoStack.size() - 1, seEntaoStack.get(seEntaoStack.size() - 1)
										.substring(0, seEntaoStack.get(seEntaoStack.size() - 1).length() - 1) + "S");
							}
							if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
								throw new SemanticoException(
										"Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
												+ " na linha:" + token.linha + ", coluna:" + token.coluna);
							}
							geradorCodigo.geraCodigoDaPosfix(postfixList, tabelaSimbolos, nivelList);

							// valida se esta na funcao para dar retorno

							// dar return de funcao
							int allocQtd = 0;
							int allocStart = 0;
							if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
								for (int dallocIndex = 0; dallocIndex < allocPerFuncProcStack
										.get(allocPerFuncProcStack.size() - 1); dallocIndex++) {
									allocQtd += allocStack.get(allocStack.size() - 1 - dallocIndex * 2);
									allocStart = allocStack.get(allocStack.size() - 2 - dallocIndex * 2);
								}
							}
							geradorCodigo.geraReturnF(allocStart, allocQtd);
						} else {
							throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "("
									+ token.lexema + ")>" + " na linha:" + token.linha + ", coluna:" + token.coluna);
						}
					}
				}

			}

		} else {
			chamadaProcedimento();

		}
	}

	public static void analisaSe() throws SintaticoException, IOException, LexicoException, SemanticoException {
		infixList = new ArrayList<Token>();
		token = analisadorLexico.getToken();
		int auxRot, auxRot2;
		auxRot2 = 0;
		analisaExpressao();

		// gera posfix para comando se
		postfixList = geradorCodigo.geraPostFix(infixList);

		for (Token postfix : postfixList) {
			System.out.print(postfix.lexema);
		}
		System.out.println("");

		ArrayList<Token> auxPostfix = new ArrayList<Token>();
		for (Token item : postfixList)
			auxPostfix.add(new Token(item));

		if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
			throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}

		geradorCodigo.geraCodigoDaPosfix(postfixList, tabelaSimbolos, nivelList);

		if (token.simbolo == "sentao") {
			geradorCodigo.geraJmpF(rotulo);
			auxRot = rotulo;
			rotulo++;

			token = analisadorLexico.getToken();
			analisaComandoSimples();
			System.out.println("1 aaaaaaaaaaaaaa");
			if (token.simbolo.equals("ssenao")) {
				System.out.println("2");
				token = analisadorLexico.getToken();
				analisaComandoSimples();

				geradorCodigo.geraJmp(rotulo);
				auxRot2 = rotulo;
				rotulo++;
			}

			geradorCodigo.geraNull(auxRot);

			if (token.simbolo == "ssenao") {
				if (procurandoRetorno == true) {
					seEntaoStack.add("ssenaoN");
				}
				token = analisadorLexico.getToken();
				analisaComandoSimples();
				geradorCodigo.geraNull(auxRot2);
				if (procurandoRetorno == true) {
					// verificar se o se e o senao possuem retorno
					if (seEntaoStack.get(seEntaoStack.size() - 1).equals("ssenaoS")
							&& seEntaoStack.get(seEntaoStack.size() - 2).equals("sseS")) {
						seEntaoStack.remove(seEntaoStack.size() - 1);
						seEntaoStack.remove(seEntaoStack.size() - 1);
						// substitui ultimo caracter por S caso achar retorno
						seEntaoStack.set(seEntaoStack.size() - 1, seEntaoStack.get(seEntaoStack.size() - 1).substring(0,
								seEntaoStack.get(seEntaoStack.size() - 1).length() - 1) + "S");
					} else {
						seEntaoStack.remove(seEntaoStack.size() - 1);
						seEntaoStack.remove(seEntaoStack.size() - 1);
					}
				}
			} else {
				if (procurandoRetorno == true) {
					// caso n tiver retorno desempilha ultimo por n garantir retorno
					seEntaoStack.remove(seEntaoStack.size() - 1);
				}
			}
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}

	public static void analisaEnquanto() throws SintaticoException, IOException, LexicoException, SemanticoException {
		infixList = new ArrayList<Token>();
		// Def auxrot1, auxrot2 inteiro
		int auxrot1, auxrot2;

		// auxrot1 := rotulo
		auxrot1 = rotulo;

		// Gera(rotulo, NULL,'','') {inicio do while}
		geradorCodigo.geraNull(rotulo);

		// rotulo := rotulo + 1
		rotulo++;

		token = analisadorLexico.getToken();
		analisaExpressao();

		// gera posfix para comando se
		postfixList = geradorCodigo.geraPostFix(infixList);

		for (Token postfix : postfixList) {
			// System.out.print("<" + postfix.lexema + "(" + postfix.simbolo + ")>");
			System.out.print(postfix.lexema);
		}
		System.out.println("");

		ArrayList<Token> auxPostfix = (ArrayList<Token>) postfixList.clone();

		if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
			throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}

		geradorCodigo.geraCodigoDaPosfix(postfixList, tabelaSimbolos, nivelList);

		if (token.simbolo == "sfaca") {
			// auxrot2 := rotulo
			auxrot2 = rotulo;

			// Gera('', JMPF,rotulo,'') {salta se falso}
			geradorCodigo.geraJmpF(rotulo);

			// rotulo := rotulo + 1
			rotulo++;

			token = analisadorLexico.getToken();
			analisaComandoSimples();

			// Gera('', JMPF,auxrot1,'') {retorna ao inicio loop}
			geradorCodigo.geraJmp(auxrot1);

			// Gera(auxrot2, NULL,'','') {fim do while}
			geradorCodigo.geraNull(auxrot2);

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
					geradorCodigo.geraRd();
					geradorCodigo.geraStr(tabelaSimbolos.returnVarRotulo(token.lexema, nivelList));
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
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}

	}

	public static void analisaEscreva() throws SintaticoException, SemanticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {

			infixList = new ArrayList<Token>();
			token = analisadorLexico.getToken();
			analisaExpressao();

			// gera posfix para comando se
			postfixList = geradorCodigo.geraPostFix(infixList);

			ArrayList<Token> auxPostfix = new ArrayList<Token>();
			for (Token item : postfixList)
				auxPostfix.add(new Token(item));

			if (!geradorCodigo.validaPostFixInteiro(auxPostfix)) {
				throw new SemanticoException("Erro Semantico do token <" + token.simbolo + "(" + token.lexema + ")>"
						+ " na linha:" + token.linha + ", coluna:" + token.coluna);
			}
			geradorCodigo.geraCodigoDaPosfix(postfixList, tabelaSimbolos, nivelList);

			geradorCodigo.geraPrn();
			if (token.simbolo == "sfecha_parenteses") {
				token = analisadorLexico.getToken();
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
				// se (TabSimb[ind].tipo == "funcao inteiro") ou (TabSimb[ind].tipo == "funcao
				// booleano")
				if (tabelaSimbolos.verificaIndentificadorFuncao(token.getLexema(), nivelList)) {
					switch (tabelaSimbolos.verificaTipoIndentificador(token.lexema, nivelList)) {
					case 3:
						token.simbolo = "funcao inteiro";
						break;
					case 4:
						token.simbolo = "funcao booleano";
						break;
					}
					infixList.add(token);
					// analisa_chamada_funcao

					chamadaFuncao();

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
						if (token.lexema.equals("verdadeiro") || token.lexema.equals("falso")) {
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

	public static void analisaExpressaoSimples()
			throws IOException, LexicoException, SintaticoException, SemanticoException {
		if (token.simbolo == "smais" || token.simbolo == "smenos") { // caso der erro colocar token.simbolo == "smais"
																		// || de volta
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

	public static void analisaEtVariaveis()
			throws SintaticoException, SemanticoException, IOException, LexicoException {
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
					tabelaSimbolos.insereTabela(token.lexema, "variavel", nivelList.get(nivelList.size() - 1),
							rotuloVariavel++);
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

			geradorCodigo.geraAlloc(allocIndex, variaveisDeclaradas);
			allocStack.add(allocIndex);
			allocStack.add(variaveisDeclaradas);
			allocPerFuncProcStack.set(allocPerFuncProcStack.size() - 1,
					allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) + 1);
			allocIndex += variaveisDeclaradas;

			variaveisDeclaradas = 0;
		}
		token = analisadorLexico.getToken();
	}

	public static void analisaSubrotina() throws SintaticoException, IOException, LexicoException, SemanticoException {
		// Def auxrot, flag inteiro
		int auxrot = 0;
		int flag = 0;

		// flag = 0
		if (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			// auxrot := rotulo
			auxrot = rotulo;

			// Gera('', JMP, rotulo, '') {Salta sub-rotinas}
			geradorCodigo.geraJmp(rotulo);

			// rotulo := rotulo + 1
			rotulo++;

			// flag = 1
			flag = 1;
		}
		while (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			allocPerFuncProcStack.add(0);
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
		if (flag == 1) {
			// entao Gera(auxrot,NULL'','') {inicio do principal}
			geradorCodigo.geraNull(auxrot);
		}
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

				procFunDeclaPath.add("sprocedimento");

				// insere_tabela(token.lexema, "procedimento", nivel, rotulo) {guarda na
				// TabSimb}
				tabelaSimbolos.insereTabela(token.getLexema(), "procedimento", nivelList.get(nivelList.size() - 2),
						rotulo);
				System.out.println(rotulo);
				// Gera(rotulo,NULL,'','') {CALL ira buscar esse rotulo na TabSimb}
				geradorCodigo.geraNull(rotulo);

				// rotulo := rotulo + 1
				rotulo++;

				token = analisadorLexico.getToken();

				if (token.simbolo == "sponto_virgula") {
					analisaBloco();
					procFunDeclaPath.remove(procFunDeclaPath.size() - 1);
					if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
						for (int dallocIndex = allocPerFuncProcStack
								.get(allocPerFuncProcStack.size() - 1); dallocIndex > 0; dallocIndex--) {
							geradorCodigo.geraDalloc(allocStack.get(allocStack.size() - 2),
									allocStack.get(allocStack.size() - 1));
							allocStack.remove(allocStack.size() - 1);
							allocStack.remove(allocStack.size() - 1);
						}
					}
					allocPerFuncProcStack.remove(allocPerFuncProcStack.size() - 1);
					geradorCodigo.geraReturn();
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

	public static void analisaDeclaracaoFuncao()
			throws SintaticoException, SemanticoException, IOException, LexicoException, SemanticoException {
		token = analisadorLexico.getToken();
		// nivel := "L" (marca ou novo galho)
		nivelMax++;
		nivelList.add(nivelMax);
		if (token.simbolo == "sidentificador") {
			// pesquisa_declfunc_tabela(token.lexema) se tem identificador duplicado
			if (!tabelaSimbolos.verificaDeclaDuplicProc(token.simbolo, nivelList)) {
				// se nao encontrou
				procFunDeclaPath.add("sfuncao");
				// insere_tabela
				auxToken = token;
				token = analisadorLexico.getToken();

				if (token.simbolo == "sdoispontos") {
					token = analisadorLexico.getToken();

					if (token.simbolo == "sinteiro" || token.simbolo == "sbooleano") {
						// se (token.simbolo = sinteiro)
						if (token.simbolo == "sinteiro") {
							// TABSIMG[pc].tipo := "funcao inteiro"
							tabelaSimbolos.insereTabela(auxToken.getLexema(), "funcao inteiro",
									nivelList.get(nivelList.size() - 2), rotulo);

						}
						// senao
						if (token.simbolo == "sbooleano") {
							// TABSIMG[pc].tipo := "funcao booleano"
							tabelaSimbolos.insereTabela(auxToken.getLexema(), "funcao booleano",
									nivelList.get(nivelList.size() - 2), rotulo);

						}

						geradorCodigo.geraNull(rotulo);

						rotulo++;

						token = analisadorLexico.getToken();

						if (token.simbolo == "sponto_virgula") {
							analisaBloco();

							procFunDeclaPath.remove(procFunDeclaPath.size() - 1);

							if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
								for (int dallocIndex = allocPerFuncProcStack
										.get(allocPerFuncProcStack.size() - 1); dallocIndex > 0; dallocIndex--) {
									allocStack.remove(allocStack.size() - 1);
									allocStack.remove(allocStack.size() - 1);
								}
							}
							allocPerFuncProcStack.remove(allocPerFuncProcStack.size() - 1);
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
		if (!tabelaSimbolos.verificaProcFuncDeclarada(token.getLexema(), nivelList)) {
			// gera codigo call para label do procedimento
			geradorCodigo.geraCall(tabelaSimbolos.returnProcFuncRotulo(auxToken.lexema, nivelList));
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}

	public static void chamadaFuncao() throws SintaticoException, IOException, LexicoException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sponto_virgula" || token.simbolo == "smult" || token.simbolo == "sdiv"
				|| token.simbolo == "smais" || token.simbolo == "smenos" || token.simbolo == "smaior"
				|| token.simbolo == "smaiorig" || token.simbolo == "smenor" || token.simbolo == "smenorig"
				|| token.simbolo == "sig" || token.simbolo == "sdif" || token.simbolo == "se" || token.simbolo == "sou"
				|| token.simbolo == "sentao" || token.simbolo == "sfaca" || token.simbolo == "sfecha_parenteses"
				|| token.simbolo == "sfim" || token.getSimbolo() == "ssenao") {
		} else {
			throw new SintaticoException("Erro Sintatico do token <" + token.simbolo + "(" + token.lexema + ")>"
					+ " na linha:" + token.linha + ", coluna:" + token.coluna);
		}
	}
}
