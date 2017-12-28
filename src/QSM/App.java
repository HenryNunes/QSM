package QSM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;

import com.gct.finiteStateMachine.FiniteStateMachine;
import com.gct.finiteStateMachine.State;
import com.gct.finiteStateMachine.Transition;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import jdk.jfr.events.FileWriteEvent;

public class App {
	static int contador = 0;
	static StringBuilder csv = new StringBuilder(); 
	
	
	public static void main(String[] args) {
		//US001.feature
		//List<List<String>> sequenciasPositivas = Leitor.parser("US036.feature");
		//List<List<String>> sequenciasPositivas = Leitor.parser("US001.feature");
		List<List<String>> sequenciasPositivas = Leitor.parser("US001.feature");
		//List<List<String>> sequenciasPositivas = Leitor.parser("multiplasSequencias.feature");
		List<List<String>> sequenciasNegativas = new ArrayList<List<String>>();
		FiniteStateMachine resultado = QSM(sequenciasPositivas, sequenciasNegativas);
		
		
		System.out.println("\nContador: " +  contador);
		System.out.println("\n\nResultado Final:");
		System.out.println(resultado);
		
		File arq = new File("perguntas.csv");
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try{
			fw = new FileWriter(arq);
			bw = new BufferedWriter(fw);
			
			bw.write(csv.toString());
			bw.close();
			fw.close();
			
		}
		catch(Exception e)
		{
			System.err.println("ERRO ESCRITA ");
			System.err.println(e.toString());
		}
		
		
	}
	private static FiniteStateMachine QSM(List<List<String>> sequenciasPositivas, List<List<String>> sequenciasNegativas)
	{
		FiniteStateMachine PTA = initialize(sequenciasPositivas);		
		
		//Queue<Pair<Integer>> comb = chooseStatePairs2(PTA);
				
		//while(comb.size() > 0)
		for(Pair<State> p : chooseStatePairs(PTA))
		{	
			// System.out.println("\nsize " + comb.size());
			//Pair<Integer> rank = comb.poll();
			//Pair<State> p = new Pair<State>(pegarEstadoFsm(PTA ,rank.primeiro()), pegarEstadoFsm(PTA ,rank.segundo()));
			 
			//System.out.println("\nFSM consolidade");
			//System.out.print(PTA);
			
			if(!PTA.getStates().contains(p.primeiro()) || !PTA.getStates().contains(p.segundo()))
			{
				
				System.out.println("Merge Invalido - FSM não contem esses estados");
				continue;
			}
						
			FiniteStateMachine temp = merge(PTA, p.primeiro(), p.segundo());
			//System.out.println("Merge: " + p.primeiro() + " - " + p.segundo());
			//System.out.println("\nFSM NÃO consolidada");
			//System.out.println(temp);
			
			if(compatibilityNeg(temp, sequenciasNegativas))
			{
				//System.out.println("FSM temporaria não aceita sequencias negativas");
				//System.out.println("Sequencias negativas: " + sequenciasNegativas);
				
				for(List<String> teste : sequenciasTeste(PTA, p.primeiro, p.segundo ))
				{
					//System.out.println("Sequencias teste: " + teste);
					
					
					if(sequenciasNegativas.contains(teste))
					{
						//System.out.println("Já é recusada");
						temp = PTA;
						break;
					}
					if(sequenciasPositivas.contains(teste))
					{
						//System.out.println("Já é aceita");
						continue;						
					}
					
					if(verificarUsuario(teste))
					{
						sequenciasPositivas.add(teste);	
						continue;
					}
					else
					{
						sequenciasNegativas.add(teste);
						//return QSM(sequenciasPositivas, sequenciasNegativas);
						temp = PTA;
						break;
					}	
				}
				// Se foi aprovado em todos os testes, passa a ser uma sequencia consolidada
				PTA = temp;
				System.out.println("Contador: " + contador);
			}
			else
			{
				//System.out.println("FSM temporaria aceita sequencias negativas");
				//System.out.println("Sequencias negativas: " + sequenciasNegativas);
			}
			
			//PTA = temp;
		}
		return PTA;
	}
	
	//Retorna um prefix tree acceptor
	private static FiniteStateMachine initialize(List<List<String>> sequenciasPositivas) 
	{
		FiniteStateMachine resultado = new FiniteStateMachine();
		ArrayList<String> alfabeto = new ArrayList<String>();
		ArrayList<State> estadosFinais = new ArrayList<State>();
		ArrayList<State> estados = new ArrayList<State>();
		ArrayList<Transition> trans = new ArrayList<Transition>(); 
		
		
		State start = new State("0");
		resultado.setInitialState(start);
		
		estadosFinais.add(start);
		estados.add(start);
		
		int contadorTemporario = 1;
		for(List<String> lis : sequenciasPositivas)
		{
			State ultimo = 	start;		
			for(String s : lis)
			{
				alfabeto.add(s);
				
				
				State temp = new State("A"+Integer.toString(contadorTemporario));
				contadorTemporario++;
				estadosFinais.add(temp);
				estados.add(temp);
				
				Transition tt = new Transition();
				tt.setInput(s);
				tt.setOutput(s);
				tt.setSourceState(ultimo);
				tt.setTargetState(temp);
							
				trans.add(tt);
				
				ultimo = temp;
			}
		}
		
		//Renomeando de acordo com a hierarquia		
		resultado.setInputAlphabet(alfabeto);
		resultado.setFinalStates(estadosFinais);
		resultado.setTransitions(trans);
		resultado.setStates(estados);
		resultado.setFinalStates(estados);
		//Remover não-determinismo
	
		removerNaoDeterminismoAux(resultado);
		
		//Ajusta os nomes de forma padrão
		Queue<State> queue = new LinkedList<State>();
		queue.add(start);
		renomearHierarquiaAux(resultado, 0, queue);
		
		return resultado;
	}
	private static void renomearHierarquiaAux(FiniteStateMachine fsm,int contador, Queue<State> states)
	{
		State source = states.poll();
		if(source == null)return;
		
		source.setId(Integer.toString(contador));
		source.setName(Integer.toString(contador));
		contador++;
		
		for(Transition t : fsm.getTransitions())
		{
			if(t.getSourceState().equals(source))
			{
				states.add(t.getTargetState());
			}
		}
		renomearHierarquiaAux(fsm, contador, states);
	}
	private static void removerNaoDeterminismoAux(FiniteStateMachine fsm)
	{
		for(Transition t1 : fsm.getTransitions())
		{
			for(Transition t2 : fsm.getTransitions())
			{
				if(t1.getSourceState().equals(t2.getSourceState()) && t1.getInput().equals(t2.getInput()) && !t1.getTargetState().equals(t2.getTargetState()))
				{
					//System.err.println("Removendo não-determinismo: " + t1 +" - " + t2 ); 
					//Verifica qual o ranking, destroi o maior. Importante para o algoritmo QSM
					State sobrevivente;
					State removido;
					
					if(t1.getTargetState().getName().substring(0, 1).equals("A"))
					{
						if(Integer.parseInt(t1.getTargetState().getName().substring(1)) < Integer.parseInt(t2.getTargetState().getName().substring(1)))
						{
							sobrevivente = t1.getTargetState();
							removido = t2.getTargetState();
						}
						else
						{
							sobrevivente = t2.getTargetState();
							removido = t1.getTargetState();
						}
					}
					else
					{
						if(Integer.parseInt(t1.getTargetState().getName()) < Integer.parseInt(t2.getTargetState().getName()))
						{
							sobrevivente = t1.getTargetState();
							removido = t2.getTargetState();
						}
						else
						{
							sobrevivente = t2.getTargetState();
							removido = t1.getTargetState();
						}
						
						
					}
					
					for(Transition t : fsm.getTransitions())
					{
						if(t.getSourceState().equals(removido))
						{
							t.setSourceState(sobrevivente);
						}
						if(t.getTargetState().equals(removido))
						{
							t.setTargetState(sobrevivente);
						}
					}

					//Remove estado
					ArrayList<State> states = fsm.getStates();
					states.remove(removido);
					fsm.setStates(states);
					
					removerNaoDeterminismoAux(fsm);
					break;
				}
			}
		}
		
		//Na ultima recursão limpa todos os repetidos antes de proseguir
		Set<Transition> tranSet = new HashSet<Transition>();
		tranSet.addAll(fsm.getTransitions());
		ArrayList<Transition> trans = new ArrayList<Transition>();
		trans.addAll(tranSet);
		fsm.setTransitions(trans);
	}
	private static Queue<Pair<State>> chooseStatePairs(FiniteStateMachine fsm)
	{
		//Ordenar
		Comparator<State> com = new Comparator<State>()
				{
					@Override
					public int compare(State o1, State o2) {
						int a1 = Integer.parseInt(o1.getName());
						int a2 = Integer.parseInt(o2.getName());
						return a1 - a2;
					}
				};	
		Collections.sort(fsm.getStates(), com);
		
		
		Queue<Pair<State>> result = new LinkedTransferQueue<Pair<State>>();
		for(State s1 : fsm.getStates())
		{
			//System.err.println(s1.getName());
			for(State s2 : fsm.getStates())
			{
				if(Integer.parseInt(s1.getName()) < Integer.parseInt(s2.getName()))
				{
					//System.err.println("Size: " + result.size());
					result.add(new Pair<State>(s1, s2));
				}
			}
		}
		return result;
	}
	private static class Pair<T>
	{
		private T primeiro;
		private T segundo;
		
		public Pair(T primeiro, T segundo)
		{
			this.primeiro = primeiro;
			this.segundo = segundo;
		}
		public T primeiro()
		{
			return this.primeiro;
		}
		public T segundo()
		{
			return this.segundo;
		}
	}
	//Unifica dois estados e remove não determinismo
	private static FiniteStateMachine merge(FiniteStateMachine fsm, State a, State b)
	{	
		//Ordenar
		Comparator<State> com = new Comparator<State>()
				{
					@Override
					public int compare(State o1, State o2) {
						int a1 = Integer.parseInt(o1.getName());
						int a2 = Integer.parseInt(o2.getName());
						return a1 - a2;
					}
				};	
		Collections.sort(fsm.getStates(), com);
		
		FiniteStateMachine resultado = new FiniteStateMachine();
		ArrayList<State> estados = new ArrayList<State>();
		ArrayList<State> aceitacao = new ArrayList<State>();
		resultado.setInputAlphabet(fsm.getInputAlphabet());
		resultado.setOutputAlphabet(fsm.getOutputAlphabet());
		
		State sobrevivente = null;
		State destruido = null;
		
		if(Integer.parseInt(a.getName()) < Integer.parseInt(b.getName()))
		{
			sobrevivente = a;
			destruido = b;
		}
		else
		{
			sobrevivente = b;
			destruido = a;
		}
		
		//estados = fsm.getStates();
		for(State s : fsm.getStates())
		{
			estados.add(s);
		}
		estados.remove(destruido);
		//aceitacao = fsm.getFinals();
		for(State s : fsm.getFinals())
		{
			aceitacao.add(s);
		}
		aceitacao.remove(destruido);
		
		ArrayList<Transition> trans = new ArrayList<Transition>();
		for(Transition t : fsm.getTransitions())
		{
			Transition temp = new Transition();
			temp.setInput(t.getInput());
			temp.setOutput(t.getOutput());
			temp.setSourceState(t.getSourceState());
			temp.setTargetState(t.getTargetState());
			trans.add(temp);
						
			if(temp.getSourceState().equals(destruido))
			{
				temp.setSourceState(sobrevivente);
			}
			if(temp.getTargetState().equals(destruido))
			{
				temp.setTargetState(sobrevivente);
			}
		}
		resultado.setInitialState(fsm.getInitialState());
		resultado.setFinals(aceitacao);
		resultado.setStates(estados);
		resultado.setTransitions(trans);
		
		removerNaoDeterminismoAux(resultado);
		
		return resultado;
	}
	//Retorna se uma lista de sequencias negativas não são aceitas pela maquina de estados
	private static boolean compatibilityNeg(FiniteStateMachine fsm, List<List<String>> sequenciasNegativas)
	{
		for(List<String> l : sequenciasNegativas)
		{
			if(testSequence(fsm, l)) return false;	
		}
		return true;
	}
	//Retorna se uma sequencia é aceita pela maquina
	private static boolean testSequence(FiniteStateMachine fsm, List<String> sequencia)
	{
		State atual = fsm.getInitialState();
		for(String s : sequencia)
		{
			boolean flag = false;
			for(Transition t : fsm.getTransitions())
			{
				if(t.getSourceState().equals(atual) && t.getInput().equals(s)) 
				{
					flag = true;
					atual = t.getTargetState();					
				}
			}
			if(flag == false)return false;
		}
		return true;
	}
	private static List<String> shortPrefix(FiniteStateMachine fsm, State estado)
	{
			return menorCaminho(fsm, fsm.getInitialState(), estado, new ArrayList<State>());
	}
	private static List<String> menorCaminho(FiniteStateMachine fsm, State estadoAtual, State Alvo, List<State> visitados)
	{
		List<String> resposta = new ArrayList<String>();
		if(estadoAtual.equals(Alvo))
		{
			return resposta;
		}
		visitados.add(estadoAtual);
		
		List<String> concaternaResposta = null;
		for(Transition t : fsm.getTransitions())
		{
			if(t.getSourceState().equals(estadoAtual))
			{
				if(visitados.contains(t.getTargetState()))continue;
				
				List<String> temp =  menorCaminho(fsm, t.getTargetState() ,Alvo , visitados );
				//System.err.println("Interno: " + temp);
				
				if(temp == null)
				{
					continue;					
				}
				else if(concaternaResposta == null)
				{
					concaternaResposta = temp;
					resposta = new ArrayList<String>();
					resposta.add(t.getInput());
				}
				else if(concaternaResposta.size() > temp.size())
				{
					resposta = new ArrayList<String>();
					resposta.add(t.getInput());
					concaternaResposta = temp;
				}
			}
		}
		visitados.remove(estadoAtual);
		if(concaternaResposta == null)return null;
		
		resposta.addAll(concaternaResposta);
		return resposta;
	}
	private static List<List<String>> sufixos(FiniteStateMachine fsm, State estado)
	{
		return sufixosAux(fsm, estado, new ArrayList<Transition>());
	}
	private static List<List<String>> sufixosAux(FiniteStateMachine fsm, State estadoAtual, List<Transition> visitados)
	{
		List<List<String>> resposta = new ArrayList<List<String>>();
		
		
		for(Transition t : fsm.getTransitions())
		{
			if(visitados.contains(t))continue;
			if(t.getSourceState().equals(estadoAtual))
			{
				visitados.add(t);
				List<List<String>> temp = sufixosAux(fsm, t.getTargetState(), visitados);
				if(temp.size() == 0)
				{
					List<String> temp2 = new ArrayList<String>();
					temp2.add(t.getInput());
					resposta.add(temp2);
				}
				for(List<String> ls : temp)
				{
					List<String> temp2 = new ArrayList<String>();
					temp2.add(t.getInput());
					temp2.addAll(ls);
					resposta.add(temp2);
				}
				visitados.remove(t);
			}
		}
		
		return resposta; 
	}
	private static List<List<String>> sequenciasTeste(FiniteStateMachine fsm, State rankMenor, State rankMaior)
	{
		List<List<String>> resposta = new ArrayList<List<String>>();
		
		List<String> sp = shortPrefix(fsm, rankMenor);
		List<List<String>> sufixos = sufixos(fsm, rankMaior);
		
		//System.out.println(sufixos);
		
		for(List<String> sufixo : sufixos)
		{
			List<String> temp = new ArrayList<String>();
			
			if(sp == null)
			{
				System.err.println("ERRO SP");
			}
			else if(sufixos == null)
			{
				System.err.println("ERRO sufixos");
			}
			temp.addAll(sp);
			temp.addAll(sufixo);
			resposta.add(temp);
		}
		
		if(sufixos.isEmpty() || estadoAdjacente(fsm, rankMenor, rankMaior))
		{
			//System.err.print("Sequencia do caso da Borda");
			//System.err.println(problemaDaBorda(fsm, rankMenor, rankMaior));
			List<String> temp = new ArrayList<String>();
			temp.addAll(sp);
			temp.addAll(problemaDaBorda(fsm, rankMenor, rankMaior));
			resposta.add(temp);
		}
		return resposta;
	}
	private static boolean verificarUsuario(List<String> teste)
	{
		contador++;
		System.out.println("Sequencia valida? S-N");
		System.out.println(teste);
		csv.append(teste + ";\n");
		
		
		return true;
//		boolean flag = false;
//		Scanner in = new Scanner(System.in);
//		while(1 == 1)
//		{
//			String resposta = in.next();
//			if(resposta.equals("S"))
//			{
//				flag = true;
//				break;
//			}
//			else if(resposta.equals("N"))
//			{
//				flag = false;
//				break;
//			}
//		}
//		return flag;
	}
	private static State pegarEstadoFsm(FiniteStateMachine fsm, int n)
	{
		String num = Integer.toString(n);
		for(State s : fsm.getStates())
		{
			if(s.getName().equals(num))return s;
		}
		return null;
	}
	private static Queue<Pair<Integer>> chooseStatePairs2(FiniteStateMachine fsm)
	{
		LinkedTransferQueue<Pair<Integer>> resposta = new LinkedTransferQueue<Pair<Integer>>();
		int size = fsm.getStates().size();
		for(int a = 0; a < size; a++)
		{
			for(int b = a + 1; b < size; b++)
			{
				Pair<Integer> temp = new Pair<Integer>(a, b);
				resposta.add(temp);
			}
		}
		return resposta;
	}
	private static List<String> problemaDaBorda(FiniteStateMachine fsm, State rankMenor, State rankMaior)
	{
		List<String> resposta = new ArrayList<String>();
		List<String> temp = menorCaminho(fsm, rankMenor, rankMaior, new ArrayList<State>());
		if(temp != null)
		{
			for(String s : temp)
			{
				resposta.add(s);
			}
			for(String s : temp)
			{
				resposta.add(s);
			}
		}
		return resposta;
	}
	private static boolean estadoAdjacente(FiniteStateMachine fsm, State rankMenor, State rankMaior)
	{
		for(Transition t : fsm.getTransitions())
		{
			if(t.getSourceState().equals(rankMenor) && t.getTargetState().equals(rankMaior) )return true;
		}
		return false;
	}
}
