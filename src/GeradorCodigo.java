import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;


public class GeradorCodigo {
	ArrayList<Token> postFix;
	ArrayList<Token> postfixStack;
	String codigoGerado = "";
	
	public ArrayList<Token> geraPostFix(ArrayList<Token> infix) throws SemanticoException {
		postFix = new ArrayList<Token>();
		postfixStack = new ArrayList<Token>();
		int infixIndex = 0;

		while (infixIndex < infix.size()) {
			if (infix.get(infixIndex).simbolo == "snao") {
				while (postfixStack.size() > 0) {
					if (infixIndex >= infix.size()) {
						
					}
					if (postfixStack.get(postfixStack.size() - 1).simbolo == "sabre_parenteses") {
						postfixStack.add(infix.get(infixIndex));
						break;
					}
					if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
						postFix.add(postfixStack.get(postfixStack.size() - 1));
						postfixStack.remove(postfixStack.size() - 1);
					} else {
						postfixStack.add(infix.get(infixIndex));
						break;
					}
				}
			} else {
				if (infix.get(infixIndex).simbolo == "smenos") {
					if (infixIndex == 0) {
						infix.get(infixIndex).simbolo += "unitario";
						postfixStack.add(infix.get(infixIndex));
					} else {
						if (!(infix.get(infixIndex - 1).simbolo == "variavel inteiro" || infix.get(infixIndex - 1).simbolo == "variavel booleano" || infix.get(infixIndex - 1).simbolo == "snumero"
							|| infix.get(infixIndex - 1).simbolo == "funcao booleano" || infix.get(infixIndex - 1).simbolo == "funcao inteiro" || infix.get(infixIndex - 1).simbolo == "sverdadeiro"
							|| infix.get(infixIndex - 1).simbolo == "sfalso")) { // se o token antes do operador menos/nao for um operador
							// os operadores sao unitarios
							infix.get(infixIndex).simbolo += "unitario";
						}
						while (postfixStack.size() > 0) {
							if (postfixStack.get(postfixStack.size() - 1).simbolo == "sabre_parenteses") {
								postfixStack.add(infix.get(infixIndex));
								break;
							}
							if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
								postFix.add(postfixStack.get(postfixStack.size() - 1));
								postfixStack.remove(postfixStack.size() - 1);
							} else {
								postfixStack.add(infix.get(infixIndex));
								break;
							}
						}
					}
				} else {
					if (infix.get(infixIndex).simbolo == "variavel inteiro" || infix.get(infixIndex).simbolo == "variavel booleano" || infix.get(infixIndex).simbolo == "snumero"
							|| infix.get(infixIndex).simbolo == "funcao booleano" || infix.get(infixIndex).simbolo == "funcao inteiro" || infix.get(infixIndex).simbolo == "sverdadeiro"
							|| infix.get(infixIndex).simbolo == "sfalso") {
							postFix.add(infix.get(infixIndex));
					} else {
						if (infix.get(infixIndex).simbolo == "sabre_parenteses") {
							postfixStack.add(infix.get(infixIndex));
						} else {
							if (infix.get(infixIndex).simbolo == "sfecha_parenteses") {
								while (postfixStack.get(postfixStack.size() - 1).simbolo != "sabre_parenteses") {
									postFix.add(postfixStack.get(postfixStack.size() - 1));
									postfixStack.remove(postfixStack.size() - 1);	
								}
								postfixStack.remove(postfixStack.size() - 1);
							} else {
								if (postfixStack.size() <= 0) {
									postfixStack.add(infix.get(infixIndex));
								} else {
									if (postfixStack.get(postfixStack.size() - 1).simbolo == "sabre_parenteses") {
										postfixStack.add(infix.get(infixIndex));
									} else {
										if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
											postFix.add(postfixStack.get(postfixStack.size() - 1));
											postfixStack.remove(postfixStack.size() - 1);
											while (postfixStack.size() > 0) {
												if (postfixStack.get(postfixStack.size() - 1).simbolo == "sabre_parenteses") {
													break;
												}
												if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
													postFix.add(postfixStack.get(postfixStack.size() - 1));
													postfixStack.remove(postfixStack.size() - 1);
												} else {
													break;
												}
											}
											postfixStack.add(infix.get(infixIndex));
										} else {
											postfixStack.add(infix.get(infixIndex));
										}
									}
								}
							}
						}
					}
				}
			}
			infixIndex++;
		}
		while (postfixStack.size() > 0) {
			postFix.add(postfixStack.get(postfixStack.size() - 1));
			postfixStack.remove(postfixStack.size() - 1);
		}
		return postFix;
	}
	
	private int postOperand(Token token) {
		if (token.getSimbolo() == "smenosunitario" || token.getSimbolo() == "snao") {
			return 6;
		} else {
			if (token.getSimbolo() == "smult" || token.getSimbolo() == "sdiv") {
				return 5;
			} else {
				if (token.getSimbolo() == "smais" || token.getSimbolo() == "smenos") {
					return 4;
				}
				if (token.getSimbolo() == "smaior" || token.getSimbolo() == "smenor" || token.getSimbolo() == "smaiorig" || token.getSimbolo() == "smenorig" || token.getSimbolo() == "sig" || token.getSimbolo() == "sdif") {
					return 3;
				} else {
					if (token.getSimbolo() == "se") {
						return 2;
					} else {
						if (token.getSimbolo() == "sou") {
							return 1;
						}
					}
				}
			}
		}
		return -1;
	}

	public boolean validaPostFixBooleano(ArrayList<Token> postFix) {
		int postFixIndex = 0;
		Token auxToken;
		while (postFix.size() > 1) {
			postFixIndex = 0;
			while ((postFix.get(postFixIndex).simbolo == "variavel inteiro" || postFix.get(postFixIndex).simbolo == "variavel booleano" || postFix.get(postFixIndex).simbolo == "snumero"
					|| postFix.get(postFixIndex).simbolo == "funcao booleano" || postFix.get(postFixIndex).simbolo == "funcao inteiro" || postFix.get(postFixIndex).simbolo == "sverdadeiro"
					|| postFix.get(postFixIndex).simbolo == "sfalso")) {
				postFixIndex++;
			}
			if (postFix.get(postFixIndex).simbolo == "sou" || postFix.get(postFixIndex).simbolo == "se") {
				if (postFix.get(postFixIndex - 1).simbolo == "variavel booleano" || postFix.get(postFixIndex - 1).simbolo == "funcao booleano" || postFix.get(postFixIndex - 1).simbolo == "sverdadeiro"
						|| postFix.get(postFixIndex - 1).simbolo == "sfalso") {
					if (postFix.get(postFixIndex - 2).simbolo == "variavel booleano" || postFix.get(postFixIndex - 2).simbolo == "funcao booleano" || postFix.get(postFixIndex - 2).simbolo == "sverdadeiro"
							|| postFix.get(postFixIndex - 2).simbolo == "sfalso") {
						postFix.remove(postFixIndex);
						postFix.remove(postFixIndex - 1);
						auxToken = postFix.get(postFixIndex - 2);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 2, auxToken);
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (postFix.get(postFixIndex).simbolo == "snao") {
					if (postFix.get(postFixIndex - 1).simbolo == "variavel booleano" || postFix.get(postFixIndex - 1).simbolo == "funcao booleano" || postFix.get(postFixIndex - 1).simbolo == "sverdadeiro"
							|| postFix.get(postFixIndex - 1).simbolo == "sfalso") {
						postFix.remove(postFixIndex);
						auxToken = postFix.get(postFixIndex - 1);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 1, auxToken);
					} else {
						return false;
					}
				} else {
					if (postFix.get(postFixIndex).simbolo == "sig" || postFix.get(postFixIndex).simbolo == "sdif") {
						if (postFix.get(postFixIndex - 1).simbolo == "variavel booleano" || postFix.get(postFixIndex - 1).simbolo == "funcao booleano" || postFix.get(postFixIndex - 1).simbolo == "sverdadeiro"
								|| postFix.get(postFixIndex - 1).simbolo == "sfalso") {
							if (postFix.get(postFixIndex - 2).simbolo == "variavel booleano" || postFix.get(postFixIndex - 2).simbolo == "funcao booleano" || postFix.get(postFixIndex - 2).simbolo == "sverdadeiro"
									|| postFix.get(postFixIndex - 2).simbolo == "sfalso") {
								postFix.remove(postFixIndex);
								postFix.remove(postFixIndex - 1);
								auxToken = postFix.get(postFixIndex - 2);
								auxToken.simbolo = "variavel booleano";
								postFix.set(postFixIndex - 2, auxToken);
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
								if (postFix.get(postFixIndex - 2).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 2).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 2).simbolo == "snumero") {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel booleano";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					} else {
						if (postFix.get(postFixIndex).simbolo == "smaior" || postFix.get(postFixIndex).simbolo == "smenor" || postFix.get(postFixIndex).simbolo == "smaiorig"
						|| postFix.get(postFixIndex).simbolo == "smenorig") {
							if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
								if (postFix.get(postFixIndex - 2).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 2).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 2).simbolo == "snumero") {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel booleano";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex).simbolo == "smenosunitario") {
									if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
										postFix.remove(postFixIndex);
										auxToken = postFix.get(postFixIndex - 1);
										auxToken.simbolo = "variavel inteiro";
										postFix.set(postFixIndex - 1, auxToken);
									} else {
										return false;
									}
							} else {
								if (postFix.get(postFixIndex).simbolo == "smais" || postFix.get(postFixIndex).simbolo == "smenos"
								|| postFix.get(postFixIndex).simbolo == "smult" || postFix.get(postFixIndex).simbolo == "sdiv") {
									if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
										if (postFix.get(postFixIndex - 2).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 2).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 2).simbolo == "snumero") {
											postFix.remove(postFixIndex);
											postFix.remove(postFixIndex - 1);
											auxToken = postFix.get(postFixIndex - 2);
											auxToken.simbolo = "variavel inteiro";
											postFix.set(postFixIndex - 2, auxToken);
										} else {
											return false;
										}
									} else {
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		postFixIndex = 0;
		if (postFix.get(postFixIndex).simbolo == "variavel booleano" || postFix.get(postFixIndex).simbolo == "funcao booleano" || postFix.get(postFixIndex).simbolo == "sverdadeiro"
				|| postFix.get(postFixIndex).simbolo == "sfalso") {
			return true;	
		}
		return false;
	}
	
	public boolean validaPostFixInteiro(ArrayList<Token> postFix) {
		int postFixIndex = 0;
		Token auxToken;
		while (postFix.size() > 1) {
			postFixIndex = 0;
			while ((postFix.get(postFixIndex).simbolo == "variavel inteiro" || postFix.get(postFixIndex).simbolo == "variavel booleano" || postFix.get(postFixIndex).simbolo == "snumero"
					|| postFix.get(postFixIndex).simbolo == "funcao booleano" || postFix.get(postFixIndex).simbolo == "funcao inteiro" || postFix.get(postFixIndex).simbolo == "sverdadeiro"
					|| postFix.get(postFixIndex).simbolo == "sfalso")) {
				postFixIndex++;
			}
			if (postFix.get(postFixIndex).simbolo == "sou" || postFix.get(postFixIndex).simbolo == "se") {
				if (postFix.get(postFixIndex - 1).simbolo == "variavel booleano" || postFix.get(postFixIndex - 1).simbolo == "funcao booleano" || postFix.get(postFixIndex - 1).simbolo == "sverdadeiro"
						|| postFix.get(postFixIndex - 1).simbolo == "sfalso") {
					if (postFix.get(postFixIndex - 2).simbolo == "variavel booleano" || postFix.get(postFixIndex - 2).simbolo == "funcao booleano" || postFix.get(postFixIndex - 2).simbolo == "sverdadeiro"
							|| postFix.get(postFixIndex - 2).simbolo == "sfalso") {
						postFix.remove(postFixIndex);
						postFix.remove(postFixIndex - 1);
						auxToken = postFix.get(postFixIndex - 2);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 2, auxToken);
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (postFix.get(postFixIndex).simbolo == "snao") {
					if (postFix.get(postFixIndex - 1).simbolo == "variavel booleano" || postFix.get(postFixIndex - 1).simbolo == "funcao booleano" || postFix.get(postFixIndex - 1).simbolo == "sverdadeiro"
							|| postFix.get(postFixIndex - 1).simbolo == "sfalso") {
						postFix.remove(postFixIndex);
						auxToken = postFix.get(postFixIndex - 1);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 1, auxToken);
					} else {
						return false;
					}
				} else {
					if (postFix.get(postFixIndex).simbolo == "sig" || postFix.get(postFixIndex).simbolo == "sdif") {
						if (postFix.get(postFixIndex - 1).simbolo == "variavel booleano" || postFix.get(postFixIndex - 1).simbolo == "funcao booleano" || postFix.get(postFixIndex - 1).simbolo == "sverdadeiro"
								|| postFix.get(postFixIndex - 1).simbolo == "sfalso") {
							if (postFix.get(postFixIndex - 2).simbolo == "variavel booleano" || postFix.get(postFixIndex - 2).simbolo == "funcao booleano" || postFix.get(postFixIndex - 2).simbolo == "sverdadeiro"
									|| postFix.get(postFixIndex - 2).simbolo == "sfalso") {
								postFix.remove(postFixIndex);
								postFix.remove(postFixIndex - 1);
								auxToken = postFix.get(postFixIndex - 2);
								auxToken.simbolo = "variavel booleano";
								postFix.set(postFixIndex - 2, auxToken);
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
								if (postFix.get(postFixIndex - 2).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 2).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 2).simbolo == "snumero") {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel inteiro";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					} else {
						if (postFix.get(postFixIndex).simbolo == "smaior" || postFix.get(postFixIndex).simbolo == "smenor" || postFix.get(postFixIndex).simbolo == "smaiorig"
						|| postFix.get(postFixIndex).simbolo == "smenorig") {
							if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
								if (postFix.get(postFixIndex - 2).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 2).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 2).simbolo == "snumero") {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel booleano";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex).simbolo == "smenosunitario") {
									if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
										postFix.remove(postFixIndex);
										auxToken = postFix.get(postFixIndex - 1);
										auxToken.simbolo = "variavel inteiro";
										postFix.set(postFixIndex - 1, auxToken);
									} else {
										return false;
									}
							} else {
								if (postFix.get(postFixIndex).simbolo == "smais" || postFix.get(postFixIndex).simbolo == "smenos"
								|| postFix.get(postFixIndex).simbolo == "smult" || postFix.get(postFixIndex).simbolo == "sdiv") {
									if (postFix.get(postFixIndex - 1).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 1).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 1).simbolo == "snumero") {
										if (postFix.get(postFixIndex - 2).simbolo == "variavel inteiro" || postFix.get(postFixIndex - 2).simbolo == "funcao inteiro" || postFix.get(postFixIndex - 2).simbolo == "snumero") {
											postFix.remove(postFixIndex);
											postFix.remove(postFixIndex - 1);
											auxToken = postFix.get(postFixIndex - 2);
											auxToken.simbolo = "variavel inteiro";
											postFix.set(postFixIndex - 2, auxToken);
										} else {
											return false;
										}
									} else {
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		postFixIndex = 0;
		if (postFix.get(postFixIndex).simbolo == "variavel inteiro" || postFix.get(postFixIndex).simbolo == "funcao inteiro"
				|| postFix.get(postFixIndex).simbolo == "snumero") {
			return true;	
		}
		return false;
	}

	
	public void geraCodigoDaPosfix(ArrayList<Token> postFixList, TabelaSimbolos tabelaSimbolos, ArrayList<Integer> nivelList) {
		int postFixIndex = 0;
		while (postFixIndex < postFix.size()) {
			switch (postFix.get(postFixIndex).simbolo) {
				case "funcao booleano":
				case "funcao inteiro":
					this.geraCall(tabelaSimbolos.returnProcFuncRotulo(postFix.get(postFixIndex).lexema, nivelList));
					break;
					
				case "variavel inteiro":
				case "variavel booleano":
					this.geraLdv(tabelaSimbolos.returnVarRotulo(postFix.get(postFixIndex).lexema, nivelList));
					break;
					
				case "snumero":
					this.geraLdc(postFix.get(postFixIndex).lexema);
					break;
				
				case "sverdadeiro":
					this.geraLdc("1");
					break;
					
				case "sfalso":
					this.geraLdc("0");
					break;
				
				case "smenosunitario":
					this.geraInv();
						break;
						
				case "snao":
					this.geraNeg();
					break;
					
				case "smult":
					this.geraMult();
					break;
					
				case "sdiv":
					this.geraDiv();
					break;
					
				case "smais":
					this.geraAdd();;
					break;
					
				case "smenos":
					this.geraSub();
					break;
					
				case "smaior":
					this.geraCma();
					break;
					
				case "smenor":
					this.geraCme();
					break;
					
				case "smaiorig":
					this.geraCmaq();
					break;
					
				case "smenorig":
					this.geraCmeq();
					break;
					
				case "sig":
					this.geraCeq();
					break;
					
				case "sdif":
					this.geraCdif();
					break;
					
				case "se":
					this.geraAnd();
					break;
					
				case "sou":
					this.geraOr();
					break;
			}
			postFixIndex++;
		}
	}
	
	public void geraArquivo() throws IOException {
		FileWriter fileWriter = new FileWriter("object.txt");
		String fileContent = "This is a sample text.";
	    fileWriter.write(codigoGerado);
	    fileWriter.close();
	}
	
	public void geraLdc(String k) {
		codigoGerado += "LDC " + k +"\n"; 
	}

	public void geraLdv(int k) {
		codigoGerado += "LDV " + k +"\n"; 
	}

	public void geraAdd() {
		codigoGerado += "ADD" + "\n"; 
	}

	public void geraSub() {
		codigoGerado += "SUB" + "\n"; 
	}

	public void geraMult() {
		codigoGerado += "MULT" + "\n"; 
	}

	public void geraDiv() {
		codigoGerado += "DIV" + "\n"; 
	}

	public void geraInv() {
		codigoGerado += "INV" + "\n"; 
	}

	public void geraAnd() {
		codigoGerado += "AND" + "\n"; 
	}

	public void geraOr() {
		codigoGerado += "OR" + "\n"; 
	}

	public void geraNeg() {
		codigoGerado += "NEG" + "\n"; 
	}

	public void geraCme() {
		codigoGerado += "CME" + "\n"; 
	}

	public void geraCma() {
		codigoGerado += "CMA" + "\n"; 
	}

	public void geraCeq() {
		codigoGerado += "CEQ" + "\n"; 
	}

	public void geraCdif() {
		codigoGerado += "CDIF" + "\n"; 
	}

	public void geraCmeq() {
		codigoGerado += "CMEQ" + "\n"; 
	}

	public void geraCmaq() {
		codigoGerado += "CMAQ" + "\n"; 
	}

	public void geraStart() {
		codigoGerado += "START" + "\n";
	}

	public void geraHlt() {
		codigoGerado += "HLT" + "\n";
	}

	public void geraStr(int n) {
		codigoGerado += "STR " + n + "\n"; 
	}

	public void geraJmp(int t) {
		codigoGerado += "JMP " +"L" + t + "\n"; 
	}

	public void geraJmpF(int t) {
		codigoGerado += "JMPF " + "L" + t + "\n"; 
	}

	public void geraNull(int t) {
		codigoGerado += "L" + t + " NULL" + "\n"; 
	}

	public void geraRd() {
		codigoGerado += "RD" + "\n"; 
	}

	public void geraPrn() {
		codigoGerado += "PRN" + "\n"; 
	}

	public void geraAlloc(int m, int n) {
		codigoGerado += "ALLOC " + m + " " + n + "\n"; 
	}

	public void geraDalloc(int m, int n) {
		codigoGerado += "DALLOC " + m + " " + n + "\n"; 
	}

	public void geraCall(int t) {
		codigoGerado += "CALL " + "L" + t + "\n";
	}

	public void geraReturn() {
		codigoGerado += "RETURN" + "\n";
	}
	public void geraReturnF(int m, int n) {
		codigoGerado += "RETURNF " + m + " " + n + "\n"; 
	}
}
