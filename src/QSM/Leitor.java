package QSM;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.GherkinDocument;
import gherkin.pickles.Pickle;
import gherkin.pickles.PickleStep;
import gherkin.pickles.Compiler;

public class Leitor {

	public static List<List<String>> parser(String path)
	{
		List<List<String>> sequencias = new ArrayList<List<String>>();
		File arq = new File(path);
		Scanner in = null;
		
		try
		{
			in = new Scanner(new FileInputStream(arq), "UTF-8");
		}
		catch(Exception e)
		{
			System.err.println("Erro na leitura do arquivo: " + path);
		}
		StringBuilder sb = new StringBuilder();
		while(in.hasNextLine())
		{
			sb.append(in.nextLine() + "\n");
		}
		
		
		Parser<GherkinDocument> parser = new Parser<GherkinDocument>(new AstBuilder());
		GherkinDocument gd = parser.parse(sb.toString());
		List<Pickle> pickles = new Compiler().compile(gd);
		//System.err.println("Total de Cen�rios: " + pickles.size());		
		
		int nCenarios = 0;
		int estados = 0;
		for(Pickle p : pickles)
		{
			//System.err.println("Cen�rio: " + p.getName());
			nCenarios++;
			//System.err.println("Cen�rios: " + nCenarios);
			List<String> lt = new ArrayList<String>();
			
			for(PickleStep ps :p.getSteps())
			{
				estados++;
				//System.err.println("estados: " + estados);
				//System.err.println("Step: " + ps.getText());
				lt.add(ps.getText());
			}
			sequencias.add(lt);
		}

		return sequencias;
	}
}
