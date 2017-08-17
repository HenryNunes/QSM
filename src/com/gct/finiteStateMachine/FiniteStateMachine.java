//Observação:
//Esta classe foi convertida e adaptada do projeto em C#, algoritmos-mbt

package com.gct.finiteStateMachine;

import java.util.List;
import java.util.Set;

import com.gct.utilities.Tupla;

import java.util.ArrayList;
import java.util.HashSet;

public class FiniteStateMachine{
    /// EPSILON constant. Denotes empty sets.
    public static final String EPSILON = "ε";
    private ArrayList<State> finals;
    private ArrayList<String> inputAlphabet;
    private String nameUseCase;
    private ArrayList<String> outputAlphabet;
    private ArrayList<State> states;
    private String name;
    private State initialState;
    private ArrayList<Transition> transitions;
    private ArrayList<ArrayList<String>> wiSet;

    private List<State> finalStates;
   
    public FiniteStateMachine(String name){
        this.name = name;
        this.states = new ArrayList<State>();
        this.transitions = new ArrayList<Transition>();
        this.inputAlphabet = new ArrayList<String>();
        this.outputAlphabet = new ArrayList<String>();
        this.finals = new ArrayList<State>();
        this.wiSet = new ArrayList<ArrayList<String>>();
    }
    
    public FiniteStateMachine(){
    	this("");
    }

	public ArrayList<State> getFinals() {
		return finals;
	}

	public void setFinals(ArrayList<State> finals) {
		this.finals = finals;
	}

	public ArrayList<String> getInputAlphabet() {
		return inputAlphabet;
	}

	public void setInputAlphabet(ArrayList<String> inputAlphabet) {
		//Adiciona também o Epslon
		if(!inputAlphabet.contains(EPSILON))inputAlphabet.add(EPSILON);
		this.inputAlphabet = inputAlphabet;
	}

	public String getNameUseCase() {
		return nameUseCase;
	}

	public void setNameUseCase(String nameUseCase) {
		this.nameUseCase = nameUseCase;
	}

	public ArrayList<String> getOutputAlphabet() {
		return outputAlphabet;
	}

	public void setOutputAlphabet(ArrayList<String> outputAlphabet) {
		if(!outputAlphabet.contains(EPSILON))outputAlphabet.add(EPSILON);
		this.outputAlphabet = outputAlphabet;
	}

	public ArrayList<State> getStates() {
		return states;
	}

	public void setStates(ArrayList<State> states) {
		this.states = states;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}

	public ArrayList<ArrayList<String>> getWiSet() {
		return wiSet;
	}

	public void setWiSet(ArrayList<ArrayList<String>> wiSet) {
		this.wiSet = wiSet;
	}

	public List<State> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(List<State> finalStates) {
		this.finalStates = finalStates;
	}

	public void addTransition(Transition transition){
		transitions.add(transition);
	}
	
	public FiniteStateMachine minimization() 
	{
		FiniteStateMachine resposta = new FiniteStateMachine();
		resposta.setName(this.name);
		resposta.setInputAlphabet(this.inputAlphabet);
		resposta.setOutputAlphabet(this.outputAlphabet);
		
		List<Tupla<State, State>> tuplas = listaTuplas(this.states);
		List<Tupla<State, State>> tuplasRemover = new ArrayList<Tupla<State, State>>();
		//System.err.println("Inicio da remoção de Tuplas");
		//System.err.println("Tuplas Iniciais" + tuplas.toString());
		// Elimina tuplas 
		for(Tupla<State, State> t : tuplas)
		{
			// Que não sejam finais com finais
			if((finalStates.contains(t.getPrimeiro()) && !finalStates.contains(t.getSegundo())) ||
					(!finalStates.contains(t.getPrimeiro()) && finalStates.contains(t.getSegundo())))
			{
				//System.err.println("removida por estados finais: " + t.toString());
				tuplasRemover.add(t);
				continue;
			}
			// Com output diefernte na transição
			for(String in : this.inputAlphabet)
			{
				String sPri = outputResultante(t.getPrimeiro(), in);
				String sSeg = outputResultante(t.getSegundo(), in);		
				if((sPri == null && sSeg != null) || (sPri != null && sSeg == null) )
				{
					tuplasRemover.add(t);
					break;
				}
				else if(sPri == null && sSeg== null)
				{
					continue;
				}
				else if(!sPri.equals(sSeg))
				{
					tuplasRemover.add(t);
					break;
				}
			}
		}
		//System.err.println("Todas tuplas para remover: " + tuplasRemover.toString());
		tuplas.removeAll(tuplasRemover);
		//System.err.println(valor);
		
		// Recursão que remove tuplas que apontam para tuplas previamente removidas
		//System.err.println("Tuplas pré-filtradas" + tuplas.toString());
		removerEstadosNãofundiveis(tuplas);
		System.err.println("Tuplas Finais" + tuplas.toString());
		
		//Hora de fundir tudo
		Set<State> estadosFundiveis = new HashSet<State>();
		for(Tupla<State, State> t : tuplas)
		{
			estadosFundiveis.add(t.getPrimeiro());
			estadosFundiveis.add(t.getSegundo());
		}
		
		ArrayList<State> estadosNovos = new ArrayList<State>();
		ArrayList<State> estadosFinaisNovos = new ArrayList<State>();
		ArrayList<Transition> transicoesNovas = new ArrayList<Transition>();
		
		//Adiciona os estados que não vao ser fundidos
		estadosNovos.addAll(this.getStates());
		estadosNovos.removeAll(estadosFundiveis);
		estadosFinaisNovos.addAll(this.getFinals());
		estadosFinaisNovos.removeAll(estadosFundiveis);
		transicoesNovas.addAll(this.transitions);
		
		//Criação dos novos estados
		while(!(estadosFundiveis.isEmpty()))
		{
			Set<State> estados = new HashSet<State>();
			for(Tupla<State, State> t : tuplas)
			{
				estados.add(t.getPrimeiro());
				estados.add(t.getSegundo());
			}
			
			Set<State> novoEstadoConjunto = new HashSet<State>();
			estadosCompativeis(tuplas, (State) estadosFundiveis.toArray()[0], estados, novoEstadoConjunto);
			estadosFundiveis.removeAll(novoEstadoConjunto);
			//System.err.println(novoEstadoConjunto);
			
			//Tenho um conjunto com estados fundiveis, preciso criar o estado
			State estadoNovo = new State();
			estadoNovo.setId("");
			Boolean flagFinal = false;
			for(State s : novoEstadoConjunto)
			{
				if(this.finalStates.contains(s))flagFinal = true;
				estadoNovo = fundirEstados(new Tupla<State, State>(estadoNovo, s));				
			}
			estadosNovos.add(estadoNovo);
			if(flagFinal)estadosFinaisNovos.add(estadoNovo); 
			
			//System.err.println("Finais: "+ estadosNovos.toString());
			
			// Alterar as trancições
			for(State s : novoEstadoConjunto)
			{
				for(Transition t : transicoesNovas)
				{
					if(t.getSourceState().equals(s))t.setSourceState(estadoNovo);
					if(t.getTargetState().equals(s))t.setTargetState(estadoNovo);
				}
			}
			//Remove duplicatas
			Set<Transition> temp = new HashSet<Transition>();
			temp.addAll(transicoesNovas);
			//System.err.println("Temp" + temp);
			transicoesNovas.clear();
			transicoesNovas.addAll(temp);
		}
		
		//Adiciona na maquina de estados
		resposta.setFinalStates(estadosFinaisNovos);
		resposta.setStates(estadosNovos);
		resposta.setTransitions(transicoesNovas);
		//System.err.println(resposta);
		return resposta;
	}
	
	private void estadosCompativeis(List<Tupla<State, State>>  tuplas, State head, Set<State> estados, Set<State> resposta)
	{
		resposta.add(head);
		estados.remove(head);
		State estado = null;
		loop:
		for(State s : estados)
		{
			for(Tupla<State, State> t : tuplas)
			{
				if(t.Contains(s) && t.Contains(head)) 
					{
						estado = s;
						break loop;
					}
			}
		}
		if(! (estado == null))estadosCompativeis(tuplas, estado, estados, resposta);
	}

	private List<Tupla<State, State>> listaTuplas(List<State> lis)
	{
		List<Tupla<State, State>> resposta = new ArrayList<Tupla<State, State>>();
		for(int i = 0; i < lis.size();i++)
		{
			for(int b = i + 1; b < lis.size();b++)
			{
				Tupla<State,State> temp = new Tupla<State,State>(lis.get(i),lis.get(b));
				resposta.add(temp);				
			}
		}
		return resposta;
	}
	private State fundirEstados(Tupla<State, State> t)
	{
		State resposta = new State();
		resposta.setId(t.getPrimeiro().getId() + t.getSegundo().getId());
		resposta.setName(t.getPrimeiro().getName() + t.getSegundo().getName());
		
		return resposta;
	}
	private State estadoResultante(State org, String input)
	{
		State resposta = null;
		for(Transition t : transitions)
		{
			if(org.equals(t.getSourceState()) && input.equals(t.getInput()))
			{
				resposta = t.getTargetState();
			}
		}
		return resposta;
	}
	private String outputResultante(State org, String input)
	{
		String resposta = null;
		for(Transition t : transitions)
		{
			if(org.equals(t.getSourceState()) && input.equals(t.getInput()))
			{
				resposta = t.getOutput();
			}
		}
		return resposta;
	}
	private void removerEstadosNãofundiveis(List<Tupla<State, State>> lis)
	{
		Tupla<State,State> remover = null;
		Loop:
		for(Tupla<State,State> t : lis )
		{
			for(String in : inputAlphabet)
			{
				
				State ePri = estadoResultante(t.getPrimeiro(), in);
				State eSeg = estadoResultante(t.getSegundo(), in);
				System.err.println("\nTuplas presentes: " + lis);
				System.err.println("Input: " + in);
				System.err.println("tupla original " + t);
				if(!(ePri == null || eSeg == null))
				{
				System.err.println("tupla destino " + new Tupla<State, State>(ePri, eSeg));
				System.err.println("Contem Essa tupla na lista? " + lis.contains(new Tupla<State, State>(ePri, eSeg)));
				}
				
//				Tupla<State, State> temp = new Tupla<State, State>(t.getPrimeiro(),t.getSegundo());
//				Tupla<State, State> temp2 = new Tupla<State, State>(eSeg,ePri);
//				ArrayList<Tupla<State,State>> temp3 = new ArrayList<Tupla<State,State>>();
//				temp3.add(temp);
//				System.err.println("Teste de tuplas: " + temp3.contains(temp2) + "\n");

				if(ePri == null && eSeg == null)continue;
				else if(ePri == null || eSeg == null )
				{
					
					remover = t;
					break Loop;
				}
				else if(!lis.contains(new Tupla<State, State>(ePri, eSeg)) && !(ePri.equals(eSeg)) )
				{
					System.err.println("tupla para remover" + t + "\n");
					remover = t;
					break Loop;
				}
			}
		}
		if(remover != null)
		{
			lis.remove(remover);
			this.removerEstadosNãofundiveis(lis);
		}		
	}
	
	
	
//	public FiniteStateMachine minimizationHopcraft()
//	{
//		Set<Set<State>> P = new HashSet<Set<State>>();
//		Set<Set<State>> W = new HashSet<Set<State>>();
//		
//		if(!(finalStates == null)) 
//			{
//				P.add(new HashSet<State>(this.finalStates));
//				Set<State> temp = new HashSet<State>(this.states);
//				temp.removeAll(this.finalStates);
//				P.add(temp);
//			}
//		else
//		{
//			P.add(new HashSet<State>(this.states));
//		}
//		//caso o set esteja vazio não vai funcionar
//		W.add(new HashSet<State>(this.finalStates));
//		while(!W.isEmpty())
//		{
//			Set<State> A = pegaElementoSet(W);
//			W.remove(A);
//			for(String in : inputAlphabet)
//			{
//
//				Set<State> X = listarEstadosOrigem(A ,in);
//				for()
//				
//				
//			}
//			
//			
//		}
//		return null;
//	}
	private Set<State> pegaElementoSet(Set<Set<State>> set)
	{
		Set<State> resposta = null;
		for(Set<State> s : set)
		{
			resposta = s;
			break;
		}
		return resposta;
	}
	
	private Set<State> listarEstadosOrigem(Set<State> set, String input)
	{
		Set<State> resultado = new HashSet<State>();
		for(State s : set)
		{
			if(!(estadoOrigem(s,input) == null))resultado.add(estadoOrigem(s,input));
		}	
		return resultado;
	}
	
	private State estadoOrigem(State state, String input)
	{
		State resultado = null;
		for(Transition t: transitions)
		{
			if(t.getTargetState().equals(state) && t.getInput().equals(input))
			{
				resultado = t.getSourceState();
				break;
			}	
		}
		return resultado;
	}
	

	
	
	
    @Override
    public String toString()
    {
        String msg = "";
        if (!name.equals(""))
            msg += "\n" + name + ":\n";

        for(Transition t : transitions)
        {
            msg += "(" + t.getSourceState().getName() + ":" + t.getTargetState().getName() +
                "[" + t.getInput().toString() + ":" + t.getOutput().toString() + "])\n";
        }

        return msg;
    }
}