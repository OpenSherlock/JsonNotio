package notio.test;

import java.io.*;
import notio.*;
import notio.translators.*;

    /** 
     * Class used to dump an analysis of a MatchResult structure.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.10 $, $Date: 1999/07/09 05:36:43 $
		 * @legal Copyright (c) Finnegan Southey, 1996-1999
		 *	This program is free software; you can redistribute it and/or modify it 
		 *	under the terms of the GNU Library General Public License as published 
		 *	by the Free Software Foundation; either version 2 of the License, or 
		 *	(at your option) any later version.  This program is distributed in the 
		 *	hope that it will be useful, but WITHOUT ANY WARRANTY; without even the 
		 *	implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
		 *	See the GNU Library General Public License for more details.  You should 
		 *	have received a copy of the GNU Library General Public License along 
		 *	with this program; if not, write to the Free Software Foundation, Inc., 
		 *	675 Mass Ave, Cambridge, MA 02139, USA.
     */

public class MatchAnalyzer
  {  
  private static final String INDENT_STRING = "    ";
  private static StringWriter writer = new StringWriter();
  private static TranslationContext tContext = new TranslationContext();
  private static KnowledgeBase kBase = new KnowledgeBase();
  private static CGIFGenerator generator = new CGIFGenerator();
  
  	/**
  	 * Returns a string with detailed human-readable information about the
  	 * specified MatchResult.
  	 *
  	 * @param matchResult  the MatchResult to be analyzed.
  	 * @return the analysis of the MatchResult.
  	 */
  public static String analyzeMatchResult(MatchResult matchResult)
  	{
  	try
  		{
	  	generator.initializeGenerator(writer, kBase, tContext);
	  	}
	  catch (GeneratorException e)
	  	{
	  	e.printStackTrace();
	  	System.exit(1);
	  	}

  	return processMatchResult(matchResult, "");
  	}

  	/**
  	 * Does the work involved in analyzing a MatchResult.
  	 *
  	 * @param matchResult  the MatchResult to be analyzed.
  	 * @return the analysis of the MatchResult.
  	 */
  private static String processMatchResult(MatchResult matchResult, String indent)
  	{
  	String analysis;
  	NodeMapping mappings[];
  	
  	analysis = indent + "Match Result: " + matchResult.matchSucceeded();
  	
  	mappings = matchResult.getMappings();
  	if (mappings == null)
  		{
  		analysis += "\tMappings: <null>\n";
  		}
  	else
  		{
  		analysis += "\tMappings: " + mappings.length + "\n";
  		indent += INDENT_STRING;
  		
  		for (int map = 0; map < mappings.length; map++)
  			{
	  		analysis += indent + "Mapping #"+ map +"\n";
  			analysis += processNodeMapping(mappings[map], indent);
  			}
  			
  		indent = indent.substring(0, indent.length() - INDENT_STRING.length());
  		}
  		
  	return analysis;
  	}
  	/**
  	 * Does the work involved in analyzing a NodeMapping.
  	 *
  	 * @param mapping  the NodeMapping to be analyzed.
  	 * @return the analysis of the NodeMapping.
  	 */
  private static String processNodeMapping(NodeMapping mapping, String indent)
  	{
  	String analysis;
 		Concept firstConcepts[], secondConcepts[];
 		Relation firstRelations[], secondRelations[];
 		Actor firstActors[], secondActors[];
 		MatchResult matchResults[];
  	
  	analysis = "";
  	
  	// Process concept mappings
  	firstConcepts = mapping.getFirstConcepts();
		secondConcepts = mapping.getSecondConcepts();
		matchResults = mapping.getMatchResults();
		
  	if (firstConcepts != null)
  		{
  		analysis += indent + "Concept Pairs (" + firstConcepts.length + ")\n";
  		
  		for (int con = 0; con < firstConcepts.length; con++)
  			{
  			analysis += indent;
  			
  			analysis += generateConcept(firstConcepts[con]) + " {" +
  				firstConcepts[con].hashCode() + "}\t==>\t";

  			analysis += generateConcept(secondConcepts[con]) + " {" +
  				secondConcepts[con].hashCode() + "}\n";
  			
  			if (matchResults != null)
  				if (matchResults[con] != null)
  					{
			  		indent += INDENT_STRING;
  					analysis += processMatchResult(matchResults[con], indent);
			  		indent = indent.substring(0, indent.length() - INDENT_STRING.length());
  					}
  			}
  			
  		analysis += "\n";
  		}

  	// Process relation mappings
  	firstRelations = mapping.getFirstRelations();
		secondRelations = mapping.getSecondRelations();
		
  	if (firstRelations != null)
  		{
  		analysis += indent + "Relation Pairs (" + firstRelations.length + ")\n";
  		
  		for (int rel = 0; rel < firstRelations.length; rel++)
  			{
  			analysis += indent;
  			
  			analysis += generateRelation(firstRelations[rel]) + " {" +
  				firstRelations[rel].hashCode() + "}\t==>\t";

  			analysis += generateRelation(secondRelations[rel]) + " {" +
  				secondRelations[rel].hashCode() + "}\n";
  			}
  			
  		analysis += "\n";
  		}

  	return analysis;
  	}
  	
		/**
		 * Returns the CGIF expression for the specified node.
		 *
		 * @param concept  the node to be generated.
		 * @return the CGIF expression.
		 */
  private static String generateConcept(Concept concept)
  	{
  	String genString = "<ERROR>";
  	
  	writer = new StringWriter();
		tContext.resetUnits();

  	try
  		{
	  	generator.generateConcept(concept);
	  	genString = writer.toString();
	  	}
	  catch (GeneratorException e)
	  	{
	  	e.printStackTrace();
	  	}
  	
  	return genString;
  	}

		/**
		 * Returns the CGIF expression for the specified node.
		 *
		 * @param relation  the node to be generated.
		 * @return the CGIF expression.
		 */
  private static String generateRelation(Relation relation)
  	{
  	String genString = "<ERROR>";
  	
  	writer = new StringWriter();
		tContext.resetUnits();

  	try
  		{
	  	generator.generateRelation(relation);
	  	genString = writer.toString();
	  	}
	  catch (GeneratorException e)
	  	{
	  	e.printStackTrace();
	  	}
  	
  	return genString;
  	}

		/**
		 * Returns the CGIF expression for the specified node.
		 *
		 * @param actor  the node to be generated.
		 * @return the CGIF expression.
		 */
  private static String generateActor(Actor actor)
  	{
  	String genString = "<ERROR>";
  	
  	writer = new StringWriter();
		tContext.resetUnits();

  	try
  		{
	  	generator.generateActor(actor);
	  	genString = writer.toString();
	  	}
	  catch (GeneratorException e)
	  	{
	  	e.printStackTrace();
	  	}
  	
  	return genString;
  	}
  }
