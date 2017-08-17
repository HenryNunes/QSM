//Descrição:
//Esta classe foi criada para ler o documento .xml gerado pelo jflap 
//e transcrever suas informações para uma máquina de estados finitos (classe FiniteStateMachine)

package com.gct.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gct.finiteStateMachine.FiniteStateMachine;
import com.gct.finiteStateMachine.State;
import com.gct.finiteStateMachine.Transition;

public class DomParser {	
	public static void parse(FiniteStateMachine fsm, String filename){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		//listas que serão inseridas na FSM
		ArrayList<State> fsmStates = new ArrayList<>();
		ArrayList<State> fsmFinalStates = new ArrayList<>();
		ArrayList<Transition> fsmTransitions = new ArrayList<>();
		ArrayList<String> fsmInputAlphabet = new ArrayList<>();
		ArrayList<String> fsmOutputAlphabet = new ArrayList<>();
		State fsmInitialState = null;
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//estrutura arquivo como uma árvore na memória
			Document doc = builder.parse(filename);
			
			
			//EXTRAÇÃO DOS ESTADOS DA FSM CONTIDOS NO .XLM
			//cria uma lista com os elementos que tem a tag state
			NodeList stateList = doc.getElementsByTagName("state");
			for(int i = 0; i < stateList.getLength(); i++){
				Node s = stateList.item(i);
				//verifica se o elemento realmente é uma tag
				if(s.getNodeType() == Node.ELEMENT_NODE){
					
					//pega id e nome do estado
					Element state = (Element) s;
					String id = state.getAttribute("id");
					String name = state.getAttribute("name");
					State aState = new State(name);
					aState.setId(id);
					fsmStates.add(aState);
					
					//cria uma lista dos elementos filhos de "state"
					NodeList stateChildren = state.getChildNodes();
					for(int j = 0; j < stateChildren.getLength(); j++){
						Node n = stateChildren.item(j);
						//verifica se o elemento realmente é uma tag
						if(n.getNodeType() == Node.ELEMENT_NODE){
							Element element = (Element) n;
							//descobre o estado inicial
							//obs: o jflap permite que exista apenas 1 estado inicial
							if(element.getTagName().equals("initial")){
								fsmInitialState = aState;
							}
							//descobre os estados finais
							if(element.getTagName().equals("final")){
								fsmFinalStates.add(aState);
							}
							
						}
					}
				}
			}
			
			if(fsmInitialState == null) throw new IllegalArgumentException("FSM não possui estado inicial.");
			
			//EXTRAÇÃO DAS TRANSIÇÕES DA FSM CONTIDAS NO .XML
			//cria uma lista com os elementos que tem a tag transition
			NodeList transitionList = doc.getElementsByTagName("transition");
			for(int i = 0; i < transitionList.getLength(); i++){
				Node t = transitionList.item(i);
				//verifica se o elemento realmente é uma tag
				if(t.getNodeType() == Node.ELEMENT_NODE){
					Element transition = (Element) t;
					NodeList transitionChildren = transition.getChildNodes();
					
					//cria transition
					Transition aTransition = new Transition();
					for(int j = 0; j < transitionChildren.getLength(); j++){
						Node n = transitionChildren.item(j);
						if(n.getNodeType() == Node.ELEMENT_NODE){
							
							//pega nome da tag e conteúdo (from, to, read, transout)
							Element element = (Element) n;
							String tagName = element.getTagName();
							String tagContent = element.getTextContent();
							switch(tagName){
								case "from":		State sourceState = fsmStates.stream().filter(state -> state.getId().equals(tagContent)).collect(Collectors.toList()).get(0);
													aTransition.setSourceState(sourceState);
													break;
													
								case "to":			State targetState = fsmStates.stream().filter(state -> state.getId().equals(tagContent)).collect(Collectors.toList()).get(0);
													aTransition.setTargetState(targetState);
													break;
													
								case "read":		if(tagContent.equals("") || tagContent.equalsIgnoreCase("null")){
														aTransition.setInput(FiniteStateMachine.EPSILON);
													} else aTransition.setInput(tagContent);
													if(!fsmInputAlphabet.contains(tagContent))	fsmInputAlphabet.add(tagContent);
													break;
													
								case "transout":	if(tagContent.equals("") || tagContent.equalsIgnoreCase("null")){
														aTransition.setOutput(FiniteStateMachine.EPSILON);
													} else aTransition.setOutput(tagContent);
													if(!fsmOutputAlphabet.contains(tagContent))	fsmOutputAlphabet.add(tagContent);
													break;
							}
							
						}
					}
				//adiciona a transição na lista de transições da máquina
				fsmTransitions.add(aTransition);
				}
			}
			
			//coloca as listas criadas na fsm
			fsm.setInitialState(fsmInitialState);
			fsm.setFinals(fsmFinalStates);
			fsm.setInputAlphabet(fsmInputAlphabet);
			fsm.setOutputAlphabet(fsmOutputAlphabet);
			fsm.setStates(fsmStates);
			fsm.setTransitions(fsmTransitions);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
