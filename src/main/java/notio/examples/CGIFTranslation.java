package notio.examples;

import java.io.*;
import notio.*;
import notio.translators.*;

    /** 
     * An example of how to use the CGIFParser and CGIFTranslator
     * for parsing and generating CGIF files.  The application reads
     * a graph from from a file and then immediately outputs the graph
     * it read to a different file.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.11 $, $Date: 1999/07/09 02:43:52 $
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
public class CGIFTranslation
  {
  	/**
  	 * An application main() method for this example.  This method creates
  	 * the basic structures required for parsing and generating and then calls
  	 * two methods, one to parse a graph from a file, the other to write the
  	 * parsed graph back out to a new file.
  	 *
  	 * @param args  an array of String containing the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	KnowledgeBase kBase;
  	TranslationContext tContext;
  	Graph graph;
  	
  	// CREATING A KNOWLEDGE BASE
  	// The current CGIF Parser and CGIF Translator both require a
  	// KnowledgeBase instance when they are constructed.  The base is
  	// simply a means for passing in the type hierarchies and marker set.
  	kBase = new KnowledgeBase();

		// CREATING A TRANSLATION CONTEXT
		// A translation context is used for the storage of temporary tables
		// during parsing and generating.  Using the same translation context
		// for parsing and generating means that user provided identifiers
		// can be preserved.  For example, the same coreference labels will
		// be used in generating as were found during parsing.  A translation
		// context can be partially or completely cleared if desired.
		tContext = new TranslationContext();

		// We now call the method, shown below, which will parse a graph from 
		// the specified file.  In this case, the file is hard-coded to be 
		// "myInputFile.cgif".  We could have easily made it a command line 
		// argument by specifying an element of the args array.
		graph = parseGraphFromFile(kBase, tContext, "myInputFile.cgif");

		if (graph == null)
			System.exit(1);
		
		// This method will output our newly parsed graph into a different 
		// file hard-coded to be "myOutputFile.cgif".
		generateGraphIntoFile(kBase, tContext, "myOutputFile.cgif", graph);
  	}

  	/**
  	 * Opens a file for reading and then attempts to parse a graph from it.
  	 *
  	 * @param kBase  the knowledge base that will be used during parsing.
  	 * @param tContext  the translation context that will be used during parsing.
  	 * @param filename  the name of the file to be opened.
  	 * @return the parsed graph or null if the parsing fails for any reason.
  	 */
  public static Graph parseGraphFromFile(KnowledgeBase kBase, TranslationContext tContext,
  	String filename)
  	{
  	FileReader reader = null;
  	CGIFParser parser;
  	Graph graph = null;
  	
		// INITIALIZING A READER
		// Any Reader can be used with the CGIFParser.  In this case we 
		// use a FileReader to read from the file specified by filename.
		try
			{
			reader = new FileReader(filename);
			}
		catch (FileNotFoundException e)
			{
			// Report error to user.
			return null;
			}
			
		// CREATING A PARSER
		// We now have all we need to create a CGIFParser instance.
		parser = new CGIFParser();
		try
			{
			parser.initializeParser(reader, kBase, tContext);
			}
	  catch (ParserException e)
	  	{
	  	e.printStackTrace();
	  	System.exit(1);
	  	}

		// PARSING A GRAPH FROM THE STREAM
		try
			{
			// Here, we ask the parser to try to parse a graph.  The CGIF parser
			// actually allows any non-terminal of the grammar to be parsed so
			// we could have asked for a Concept, Relation, or any other structure
			// instead.
			graph = parser.parseGraph();
			}
		catch (notio.ParserException e)
			{
			// This is the proper Notio exception that all parsers should throw
			// when they encounter problems.
			
			// Report error to user.
			return null;
			}

		try
			{
			reader.close();
			}
		catch (IOException e)
			{
			// Report error to user.
			return null;
			}
			
		return graph;
  	}

  	/**
  	 * Opens a file for writing and then attempts to generate a graph into it.
  	 *
  	 * @param kBase  the knowledge base that will be used during generating.
  	 * @param tContext  the translation context that will be used during generating.
  	 * @param filename  the name of the file to be written to.
  	 * @param graph  the graph to be generated into the file.
  	 */
  public static void generateGraphIntoFile(KnowledgeBase kBase, TranslationContext tContext,
  	String filename, Graph graph)
  	{
  	FileWriter writer = null;
  	CGIFGenerator generator;
  	
		// INITIALIZING A WRITER
		// Any Writer can be used with the CGIFGenerator.  In this case we 
		// use a FileWriter to write to the file specified by filename.
		try
			{
			writer = new FileWriter(filename);
			}
		catch (IOException e)
			{
			// Report error to user.
			return;
			}
			
		// CREATING A GENERATOR
		// We now have all we need to create a CGIFGenerator instance.
		generator = new CGIFGenerator();
		try
			{
			generator.initializeGenerator(writer, kBase, tContext);
			}
		catch (GeneratorException e)
			{
			// Report error to user.
			return;
			}

		// GENERATING A GRAPH INTO THE STREAM
		try
			{
			// Here, we ask the generator to try and generate the specified graph
			// into the writer.  The generator actually allows any non-terminal of 
			// the grammar to be generated so we could have generated a Concept, Relation, 
			// or any other structure instead.
			generator.generateGraph(graph);
			}
		catch (notio.GeneratorException e)
			{
			// This is the proper Notio exception that all generators should throw
			// when they encounter problems.
			
			// Report error to user.
			return;
			}

		try
			{
			writer.close();
			}
		catch (IOException e)
			{
			// Report error to user.
			return;
			}
			
  	}
  }
