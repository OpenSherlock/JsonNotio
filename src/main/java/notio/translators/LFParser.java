package notio.translators;

import notio.*;
import java.io.*;
import com.metamata.parse.ParseException;

    /** 
     * A LF Parser class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.5 $, $Date: 1999/07/09 05:37:06 $
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
		 *
     * @bug Currently catching ScannerError since token coverage is incomplete.
     * @bug Need to rework comments.
     */
public class LFParser implements Parser
  {
  /** The LF parser implementation. **/
  private LFParserImpl parser;
  
    /**
     * Initializes the parser to parse the specified reader.
     * 
     * @param reader  the reader whose contents are to be parsed.
     * @param kBase  the knowledge base to be used while parsing.
     * @param tContext  the translation context to be used while parsing.
     * @exception ParserException  if an error occurs while initializing
     * the parser.
     */
  public void initializeParser(Reader reader, KnowledgeBase kBase,
  	TranslationContext tContext) throws ParserException
  	{
  	parser = new LFParserImpl(reader);
  	parser.initializeLFParserImpl(kBase, tContext);
  	}

    /**
     * Returns the unit object parsed from the input stream.
     * 
     * @return the unit object parsed from the input stream.
     */
  public Class getUnitClass()
  	{
  	try
  		{
	  	return Class.forName("notio.Graph");
	  	}
	  catch (ClassNotFoundException e)
	  	{
	  	e.printStackTrace();
	  	System.exit(1);
	  	}
	  	
  	return null;
  	}

    /**
     * Attempts to parse the default unit from the input stream.
     * The default unit is whatever a particular parser is usually
     * intended to parse.
     * 
     * @return the unit object parsed from the input stream.
     * @exception ParserException  if an error occurs while parsing.
     */
  public Object parseUnit() throws ParserException
  	{
  	try
  		{
  		return parser.Graph();
  		}
  	catch (com.metamata.parse.ParseException e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	catch (com.metamata.parse.ScanError e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	}

    /**
     * Attempts to parse a graph from the input stream.
     * 
     * @return the graph parsed from the input stream.
     * @exception ParserException  if an error occurs while parsing.
     */
  public Graph parseOutermostContext() throws ParserException
  	{
  	try
  		{
  		return parser.Graph();
  		}
  	catch (com.metamata.parse.ParseException e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	catch (com.metamata.parse.ScanError e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	}

    /**
     * Attempts to parse a graph from the input stream.
     * 
     * @return the graph parsed from the input stream.
     * @exception ParserException  if an error occurs while parsing.
     */
  public Graph parseGraph() throws ParserException
  	{
  	try
  		{
  		return parser.Graph();
  		}
  	catch (com.metamata.parse.ParseException e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	catch (com.metamata.parse.ScanError e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	}

    /**
     * Attempts to parse a concept from the input stream.
     * 
     * @return the concept parsed from the input stream.
     * @exception ParserException  if an error occurs while parsing.
     */
  public Concept parseConcept() throws ParserException
  	{
  	try
  		{
  		return parser.Concept();
  		}
  	catch (com.metamata.parse.ParseException e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	catch (com.metamata.parse.ScanError e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	}
  

    /**
     * Attempts to parse a relation from the input stream.
     * 
     * @return the relation parsed from the input stream.
     * @exception ParserException  if an error occurs while parsing.
     */
  public Relation parseRelation() throws ParserException
  	{
  	try
  		{
  		return parser.Relation();
  		}
  	catch (com.metamata.parse.ParseException e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	catch (com.metamata.parse.ScanError e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	}
  	
    /**
     * Attempts to parse an actor from the input stream.
     * 
     * @return the actor parsed from the input stream.
     * @exception ParserException  if an error occurs while parsing.
     */
  public Actor parseActor() throws ParserException
  	{
  	try
  		{
  		return parser.Actor();
  		}
  	catch (com.metamata.parse.ParseException e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	catch (com.metamata.parse.ScanError e)
  		{
  		throw new ParserException(e.getMessage());
  		}
  	}
  	
    /**
     * This method may be used to tell the parser whether it should create
     * type objects for any parsed type labels not currently in the database,
     * or throw an exception.  Created types have only the universal and
     * absurd types as parents and children respectively.
     * The flag is true by default.
     *
     * @param flag  true or false to turn automatic creation on or off.
     */
  public void setCreateTypesOnDemand(boolean flag)
    {
    parser.setCreateTypesOnDemand(flag);
		}  	
		
    /**
     * This method may be used to check whether the parser will create
     * type objects for any parsed type labels not currently in the database,
     * or throw an exception.  This flag is set using
     * setCreateTypesOnDemand().
     *
     * @return true if types will created on demand or false if an exception
     * will be thrown.
     */
  public boolean getCreateTypesOnDemand()
    {
    return parser.getCreateTypesOnDemand();
    }
  }
