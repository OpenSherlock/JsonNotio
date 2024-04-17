package notio;

import java.io.*;

    /** 
     * The required interface for all parsers.  
     * All parsers should implement this interface
     * to facilitate pluggability.  While a parser must implement
     * the entire interface, it can opt to throw an
     * UnimplementedFeature exception if it does not support parsing
     * of a particular nonterminal. Minimally, a parser should support
     * the Unit nonterminal.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.16 $, $Date: 1999/08/02 02:40:22 $
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
     * @see notio.UnimplementedFeatureException
     *
     * @bug What methods should parsers be forced to implement?
     */
public interface Parser
  {
    /**
     * Initializes the parser to parse the specified character reader.
     * 
     * @param reader  the reader whose contents are to be parsed.
     * @param kBase  the knowledge base to be used while parsing.
     * @param tContext  the translation context to be used while parsing.
     * @exception notio.ParserException  if an error occurs while initializing
     * the parser.
     */
  public void initializeParser(Reader reader, KnowledgeBase kBase,
  	TranslationContext tContext) throws ParserException;
  	
    /**
     * Returns a Class object that indicates what class the parseUnit()
     * method will return.
     * 
     * @return a Class object that indicates what class the parseUnit()
     * method will return.
     * @see notio.Parser#parseUnit
     */
	public Class getUnitClass();

    /**
     * Attempts to parse the default unit from the input stream.
     * The default unit is whatever a particular parser is usually
     * intended to parse.
     * 
     * @return the unit object parsed from the input stream.
     * @exception notio.ParserException  if an error occurs while parsing.
     */
  public Object parseUnit() throws ParserException;

    /**
     * Attempts to parse a graph which is treated as the outermost context
     * for purposes for scoping.  This method should be used instead of parseGraph()
     * when no translation information is to be used from previous translation sessions
     * and when the parser can safely assume that the graph is "self-contained".
     * 
     * @return the graph parsed from the input stream.
     * @exception notio.ParserException  if an error occurs while parsing.
     * @exception notio.UnimplementedFeatureException  if this parser does not support this
     * parsing method.
     */
  public Graph parseOutermostContext() throws ParserException;
  
    /**
     * Attempts to parse a graph from the input stream.
     * 
     * @return the graph parsed from the input stream.
     * @exception notio.ParserException  if an error occurs while parsing.
     * @exception notio.UnimplementedFeatureException  if this parser does not support this
     * parsing method.
     */
  public Graph parseGraph() throws ParserException;

    /**
     * Attempts to parse a concept from the input stream.
     * 
     * @return the concept parsed from the input stream.
     * @exception notio.ParserException  if an error occurs while parsing.
     * @exception notio.UnimplementedFeatureException  if this parser does not support this
     * parsing method.
     */
  public Concept parseConcept() throws ParserException;

    /**
     * Attempts to parse a relation from the input stream.
     * 
     * @return the relation parsed from the input stream.
     * @exception notio.ParserException  if an error occurs while parsing.
     * @exception notio.UnimplementedFeatureException  if this parser does not support this
     * parsing method.
     */
  public Relation parseRelation() throws ParserException;
  
    /**
     * Attempts to parse an actor from the input stream.
     * 
     * @return the actor parsed from the input stream.
     * @exception notio.ParserException  if an error occurs while parsing.
     * @exception notio.UnimplementedFeatureException  if this parser does not support this
     * parsing method.
     */
  public Actor parseActor() throws ParserException;
  }
