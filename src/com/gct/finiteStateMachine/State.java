//Observação:
//Esta classe foi convertida e adaptada do projeto em C#, algoritmos-m

package com.gct.finiteStateMachine;

public class State{
	private String id;
	private String name;
	
	
	public State(){
		this("");
	}
	
	public State(String name){
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInitialState(FiniteStateMachine fsm){
		return fsm.getInitialState().equals(this);
	}

	public String toString(){
		return this.name;
	}
}
