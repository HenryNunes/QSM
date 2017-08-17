//Descrição:
//Esta classe foi criada para escrever um arquivo .txt com as sequências de teste
//geradas pelo programa

package com.gct.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Writer {
	
	//Método para escrever arquivo .txt (o arquivo será sobre-escrito sempre que este método for chamado)
	public static void writeToFile(String data, String fileName) {
		File file = new File(fileName + ".txt");
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))){
			bw.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Método que formata cada linha que será escrita no arquivo .txt
	public static String testSequenceFormatter(ArrayList<ArrayList<String>> data){
		String formattedList = "";
		for(int i = 1; i <= data.size(); i++){
			formattedList += "\tTest Sequence " + i + " :\t" + data.get(i-1) + System.lineSeparator();
		}
		return formattedList;
	}
	
	public static String sequenceFormatter(ArrayList<ArrayList<String>> data){
		String formattedList = "";
		for(int i = 0; i < data.size(); i++){
			formattedList += "\t" + data.get(i) + System.lineSeparator();
		}
		return formattedList;
	}
	
	public static String mapFormatter(Map<String, ArrayList<ArrayList<String>>> data){
		String formattedList = "";
		for(String key : data.keySet()){
			formattedList += "\tW " + key + ":" + System.lineSeparator();
			for(ArrayList<String> line : data.get(key)){
				formattedList += "\t\t" + line + System.lineSeparator();
			}
			formattedList += System.lineSeparator();
		}
		return formattedList;
	}
}