package notio;

import java.io.*;

    /** 
     * The Generator interface.
     * All generators should implement this interface
     * to facilitate pluggability.  While a generator must implement
     * the entire interface, it can opt to throw an
     * UnimplementedFeature exception if it does not support generating
     * of a particular nonterminal. Minimally, a generator should support
     * the Unit nonterminal.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.18 $, $Date: 1999/08/02 02:40:22 $
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
     * @bug What methods should generators be forced to implement.
     * @idea Add OutermostContext() or TopLevelGraph()?  This would allow for automatic 
     * clearing of def. label tables, etc.
     */
public interface Generator
  {
    /**
     * Initializes the generator to generate into the specified writer.
     * 
     * @param writer  the writer to be generated into.
     * @param kBase  the knowledge base to be used while parsing.
     * @param tContext  the translation context to be used while parsing.
     * @exception GeneratorException  if an error occurs while initializing
     * the generator.
     */
  public void initializeGenerator(Writer writer, KnowledgeBase kBase,
  	TranslationContext tContext) throws GeneratorException;

    /**
     * Returns a Class object that indicates what class the Unit generate
     * method requires.
     * 
     * @return a Class object that indicates what class the Unit generate
     * method requires.
     * @see notio.Generator#generateUnit
     */
	public Class getUnitClass();

    /**
     * Generates the specified unit object.
     * 
     * @param unit  the unit to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void generateUnit(Object unit) throws GeneratorException;

    /**
     * Generates the specified graph which is treated as the outermost context
     * for purposes for scoping.  This method should be used instead of generateGraph()
     * when no translation information is to be used from previous translation sessions
     * and when the generator can safely assume that the graph is "self-contained".
     * 
     * @param graph  the graph to be generated.
     * @exception notio.GeneratorException  if an error occurs while generating.
     * @exception notio.UnimplementedFeatureException  if this generator does not support this
     * generation method.
     */
  public void generateOutermostContext(Graph graph) throws GeneratorException;

    /**
     * Generates the specified graph.
     * 
     * @param graph  the graph to be generated.
     * @exception notio.GeneratorException  if an error occurs while generating.
     * @exception notio.UnimplementedFeatureException  if this generator does not support this
     * generation method.
     */
  public void generateGraph(Graph graph) throws GeneratorException;

    /**
     * Generates the specified concept.
     * 
     * @param concept  the concept to be generated.
     * @exception notio.GeneratorException  if an error occurs while generating.
     * @exception notio.UnimplementedFeatureException  if this generator does not support this
     * generation method.
     */
  public void generateConcept(Concept concept) throws GeneratorException;

    /**
     * Generates the specified relation.
     * 
     * @param relation  the relation to be generated.
     * @exception notio.GeneratorException  if an error occurs while generating.
     * @exception notio.UnimplementedFeatureException  if this generator does not support this
     * generation method.
     */
  public void generateRelation(Relation relation) throws GeneratorException;
  
    /**
     * Generates the specified actor.
     * 
     * @param actor  the actor to be generated.
     * @exception notio.GeneratorException  if an error occurs while generating.
     * @exception notio.UnimplementedFeatureException  if this generator does not support this
     * generation method.
     */
  public void generateActor(Actor actor) throws GeneratorException;
  }
