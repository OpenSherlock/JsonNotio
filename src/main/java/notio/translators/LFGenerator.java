package notio.translators;

import java.util.*;
import notio.*;

    /** 
     * A LF Generator class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.23 $, $Date: 1999/08/01 22:05:16 $
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
     * @bug Doesn't handle 0-adic relations/actors.
     * @bug Doesn't actually generate Actors.
     */
public class LFGenerator extends SimpleGenerator implements Generator
  {
		/**	Type label for negation	relation type. **/
	private	static final String	NEG_TYPE_LABEL = "Neg";
	
		/** The concept-to-label hashtable. **/
	private Hashtable conceptToLabel = new Hashtable();
	
		/** Current indent. **/
	private String currentIndent = "";	
	
    /**
     * Initializes the generator to write to the specified writer
     * using the specified TranslationContext and KnowledgeBase.
     *
     * @param newWriter  the writer to be generated to.
     * @param newKnowledgeBase the knowledge base to be used while generating.
     * @param newTranslationContext  the translationContext to be used while
     * generating.
     */
  public void initializeGenerator(java.io.Writer newWriter,
    KnowledgeBase newKnowledgeBase, 
    TranslationContext newTranslationContext) throws GeneratorException
    {
    super.initializeGenerator(newWriter, newKnowledgeBase, newTranslationContext);
    }

    
    /**
     * Generates the specified actor.
     * 
     * @param actor  the actor to be generated.
     * @exception notio.GeneratorException  if an error occurs while generating.
     * @exception notio.UnimplementedFeatureException  if this generator does not support this
     * generation method.
     */
  public void generateActor(Actor actor) throws GeneratorException
  	{
  	Actor(actor);
  	}
    

	/* Graph Rules */

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
  public void generateOutermostContext(Graph graph) throws GeneratorException
  	{
  	generateGraph(graph);
  	}
  
    /**
     * Generates a graph to the output stream.
     *
     * @param graph  the graph to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     *
     * @bug Currently divides into primary and secondary.  Secondary could reduced to all
     * unrelated concepts, I think, since all related secondaries should be generated along
     * with a primary.
     */
  public void generateGraph(Graph graph) throws GeneratorException
    {
    Concept concepts[];
    int numConcepts;
    Vector primaryConcepts, secondaryConcepts;
		Enumeration primaryEnum, secondaryEnum;		
    Relation relators[];
    
    concepts = graph.getConcepts();
    numConcepts = concepts.length;

		primaryConcepts = new Vector(numConcepts);
		secondaryConcepts = new Vector(numConcepts);

		// Split concepts into primary and secondary.  Primary concepts are concepts that
		// are the first argument of some relation.  All other concepts are secondary.
		// Might want a third division for unrelated concepts.
	   
    // For each concept, decide if it is primary or secondary
    for (int con = 0; con < numConcepts; con++)
    	{
			relators = concepts[con].getRelators();
			
			if (relators.length > 0)
				{
				int rel = 0;
				while ((rel < relators.length) && (relators[rel].getArguments()[0] != concepts[con]))
					rel++;
					
				if (rel < relators.length)
					primaryConcepts.addElement(concepts[con]);
				else
					secondaryConcepts.addElement(concepts[con]);
				}
			else
				secondaryConcepts.addElement(concepts[con]);
			}				
		
		// Generate primary concepts
		primaryEnum = primaryConcepts.elements();		

		while (primaryEnum.hasMoreElements())
			{
			Concept concept;
			
			concept = (Concept)primaryEnum.nextElement();
			
    	// If concept has not already been generated
      if (conceptToLabel.get(concept) == null)
      	{
    		// Generate concept
        generateConcept(concept);
        generate("\n");
        }
      }

		// Generate secondary concepts
		secondaryEnum = secondaryConcepts.elements();

		while (secondaryEnum.hasMoreElements())
			{
			Concept concept;
			
			concept = (Concept)secondaryEnum.nextElement();
			
    	// If concept has not already been generated
      if (conceptToLabel.get(concept) == null)
      	{
    		// Generate concept
        generateConcept(concept);
        generate("\n");
        }
      }
    }

    /**
     * Generates a graph comment to output stream.
     *
     * @param comment  the graph comment to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void GraphComment(String comment) throws GeneratorException
    {
    if (comment == null)
      return;

    if (comment.length() == 0)
      return;

    generate("/*" + comment + "*/");
    }

	/* Concept Rules */
	
    /**
     * Generates a concept to the output stream.
     *
     * @param concept  the concept to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     * @bug Defining label stuff is truly a gross hack.
     * @bug Should probably only generate linked relation if this
     * concept is the first argument (or last).
     */
  public void generateConcept(Concept concept) throws GeneratorException
    {
    String label;
		ConceptType conType;
		Relation relators[], primaryRelators[];
		Vector relVec;
		int refCount = 0;
		
    // If concept has not already been generated
    label = (String)conceptToLabel.get(concept);
    
    if (label == null)
    	{
			// Generate concept start
  	  generate("[");

	    // Add concept to table of generated concepts
	    
	    // Generate typefield
  	  conType = (ConceptType)concept.getType();
    	if (conType != null)
    		ConceptType(conType);
    		
    	// Defining label (if necessary)
    	// Need to generate and print label only if concept is related by 
    	// two or more relations, or if referenced twice in a single
    	// relation.
    	// Must add something to the table in any event so we add a dummy
    	// label to start.
    	conceptToLabel.put(concept, "__DUMMY");
    	relators = concept.getRelators();
    	if (relators.length > 0)
    		{
    		if (relators.length == 1)
    			{
    			Concept args[] = relators[0].getArguments();
    			
    			for (int arg = 0; arg < args.length; arg++)
    				if (args[arg] == concept)
    					refCount++;
    			}
    			
    		if ((relators.length > 1) || (refCount > 1))
    			{
					label = getDefiningLabelTable(translationContext).getNextAvailableDefiningLabel();
					// Monstrous hack
					getDefiningLabelTable(translationContext).mapDefiningLabelToCoreferenceSet(label, new CoreferenceSet(label));
					conceptToLabel.put(concept, label);
					generate("*" + label);
					}
    		}
			
			// Generate referent
	    Referent(concept.getReferent());

	    // Generate concept comment
			ConceptComment(concept.getComment());
	    
			// Generate concept end
  	  generate("]");
  	  
  	  // Find all relators for which this concept is the first arg (primary relators).
			relVec = new Vector(relators.length);

			for (int rel = 0; rel < relators.length; rel++)
				if (relators[rel].getArguments()[0] == concept)
					relVec.addElement(relators[rel]);

			primaryRelators = new Relation[relVec.size()];
			relVec.copyInto(primaryRelators);
			
			// If there are no relators, we are finished generating the concept.
			if (relators.length == 0)
				return;
				
			// If there are some relators, but no primary relators, we are finished generating.
			if (primaryRelators.length == 0)
				return;
				
	    // If one primary relator is attached to this concept
			if (primaryRelators.length == 1)
				{
	    	// Generate arc followed by relation
	    	if (relators[0].getValence() == 1)
		    	generate("<-1-");
		    else
		    	generate("-1->");

				generateRelation(relators[0]);    
				return;
	    	}

	    // If more than one relation is attached to this concept
    	// Generate multiple link leader and indent
    	increaseIndent();
    	generate("-");
    	
 	  	// For each primary relation attached to this concept
	    for (int rel = 0; rel < primaryRelators.length; rel++)
	    	{
	    	if (primaryRelators[rel].getValence() == 1)
		    	generate("\n" + currentIndent + "<-1-");
		    else
		    	generate("\n" + currentIndent + "-1->");
		    	
   			// Generate relation
	    	generateRelation(primaryRelators[rel]);
    		}
    		
    	// Deindent
    	decreaseIndent();
    	}
    else
    	{
    	// Use bound variable 
  	  generate("[?"+label+"]");
    	}    		
    }

    /**
     * Generates a concept type to the output stream.
     *
     * @param conType  the concept type to be generated.
     * @exception GeneratorException  if an IO error occurs.
     */
  public void ConceptType(ConceptType conType) throws GeneratorException
  	{
    if (conType.getLabel() == null)
    	{
    	// Should generate type definition here instead as a lambda expression
    	}
    else
    	generate(conType.getLabel());
    }

    /**
     * Generates a referent to the output stream.
     *
     * @param referent  the referent to be generated.
     * @exception GeneratorException  if an IO error occurs.
     */
  public void Referent(Referent referent) throws GeneratorException
  	{
  	Graph descriptor;
  	
  	if (referent == null)
  		return;
  	
 		Quantifier(referent.getQuantifier());
		Designator(referent.getDesignator());
		
		descriptor = referent.getDescriptor();
		if (descriptor != null)
			generateGraph(descriptor);
  	}
  	
    /**
     * Generates a quantifier to the output stream.
     *
     * @param quantifier  the quantifier to be generated.
     * @exception GeneratorException  if an IO error occurs.
     */
  public void Quantifier(Macro quantifier) throws GeneratorException
    {
    if (quantifier != null)
      generate(quantifier.getName() + " ");
    }

    /**
     * Generates a designator to the output stream.
     *
     * @param designator  the designator to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void Designator(Designator designator) throws GeneratorException
    {
    if (designator == null)
      return;

    switch (designator.getDesignatorKind())
      {
      case Designator.DESIGNATOR_LITERAL:
      	{
				LiteralDesignator((LiteralDesignator)designator);
        break;
        }

      case Designator.DESIGNATOR_MARKER:
        {
				MarkerDesignator((MarkerDesignator)designator);
        break;
        }

      case Designator.DESIGNATOR_NAME:
        {
				NameDesignator((NameDesignator)designator);
        break;
        }

      case Designator.DESIGNATOR_DEFINED:
       	throw new GeneratorException("Defined designators not handled by this generator.");
        
      default:
       	throw new GeneratorException("Unknown designator kind encountered in concept.");
       	
      }
    }

    /**
     * Generates a literal designator to output stream.
     *
     * @param designator  the designator to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void LiteralDesignator(LiteralDesignator designator) throws GeneratorException
  	{
  	Object literal;
      	
   	literal = designator.getLiteral();
   	if (literal instanceof String)
       generate("\"" + escapeCharactersInString(literal.toString(), "\"", '\\') + "\"");
    else if (literal instanceof Number)
       generate(" " + literal.toString());
    else
      throw new GeneratorException("Unknown type of literal designator found in concept.");
  	}
    
    /**
     * Generates a marker designator to output stream.
     *
     * @param designator  the designator to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void MarkerDesignator(MarkerDesignator designator) throws GeneratorException
    {
    Marker marker;
    String markerID;

    marker = designator.getMarker();
    generate("#" + marker.getMarkerID());
    }
    
    /**
     * Generates a name designator to output stream.
     *
     * @param designator  the designator to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void NameDesignator(NameDesignator designator) throws GeneratorException
    {
    String name;

    name = designator.getName();
    generate("'" + escapeCharactersInString(name, "'", '\\') + "'");
    }
    
    /**
     * Generates a concept comment to output stream.
     *
     * @param comment  the concept comment to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void ConceptComment(String comment) throws GeneratorException
    {
    if (comment == null)
      return;

    if (comment.length() == 0)
      return;

    generate(";"+escapeCharactersInString(comment, "]", '\\'));
    }

	/* Relation Rules */

    /**
     * Generates a relation to the output stream.
     *
     * @param relation  the relation to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     * @bug Assumes that first argument has already been generated.
     */
  public void generateRelation(Relation relation) throws GeneratorException
    {
    RelationType relType;
    Concept arguments[];
    String corefLabel;

		// Generate relation start
    generate("(");
   
    // Generate typefield
    relType = relation.getType();
    if (relType != null)
    	RelationType(relType);
    else
      throw new GeneratorException("No type found while generating relation.");

		// Generate relation comment
    RelationComment(relation.getComment());

		// Generate relation terminator
    generate(")");

    
    // Generate arguments as bound labels
    arguments = relation.getArguments();

		// Assume that first argument has always been generated before
		
		// If only 1 argument then we are finished.
		if (arguments.length == 1)
			return;

		// If only 2 arguments, then generate the second and return
		if (arguments.length == 2)
			{
			generate("-2->");
			generateConcept(arguments[1]);
			return;
			}
			
		// If more than 2 arguments
   	// Generate multiple link leader and indent
   	increaseIndent();
   	generate("-");

   	// First 2 .. N-1 args are outgoing from the relation.
    for (int argNo = 1; argNo < arguments.length - 1; argNo++)
      {
      generate("\n" + currentIndent + "<-" + (argNo+1) + "-");
      generateConcept(arguments[argNo]);
      }
    
    // Last arc is always incoming.
    generate("\n" + currentIndent + "<-" + (arguments.length) + "-");
    generateConcept(arguments[arguments.length - 1]);
      
    // Deindent
    decreaseIndent();
    }

    /**
     * Generates a relation type to the output stream.
     *
     * @param relType  the relation type to be generated.
     * @exception GeneratorException  if an IO error occurs.
     */
  public void RelationType(RelationType relType) throws GeneratorException
  	{
    if (relType.getLabel() == null)
    	{
    	// Should generate type definition here instead as a lambda expression
    	}
    else
    	generate(relType.getLabel());
    }

    /**
     * Generates a relation comment to output stream.
     *
     * @param comment  the relation comment to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void RelationComment(String comment) throws GeneratorException
    {
    if (comment == null)
      return;

    if (comment.length() == 0)
      return;

    generate(";"+escapeCharactersInString(comment, ")", '\\'));
    }

	/* Actor Rules */
	
    /**
     * Generates an actor to the output stream.
     *
     * @param actor  the actor to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     * @bug Literal concept handling is just plain wrong.
     */
  public void Actor(Actor actor) throws GeneratorException
    {
    RelationType relationType;
    Concept inArcs[];
    Concept outArcs[];
    String corefLabel;

		// Generate actor start
    generate("<");
   
    // Generate typefield
    relationType = actor.getType();
    if (relationType != null)
    	RelationType(relationType);
    else
      throw new GeneratorException("No type found while generating actor.");

    // Generate input arcs as bound labels
    inArcs = actor.getInputArguments();

    for (int arcNo = 0; arcNo < inArcs.length; arcNo++)
      {
      CoreferenceSet corefSets[];
      
      // BUG: See bound label problem from Concept()
      corefSets = inArcs[arcNo].getCoreferenceSets();

      if (corefSets == null || corefSets.length == 0)
        // This should never happen.  If it were to be allowed, we might write a literal here.
        {
        throw new GeneratorException("Reference to concept with no coreference set in actor.");
        }
      else
        {
        corefLabel = getDefiningLabelTable(translationContext).getDefiningLabelByCoreferenceSet(corefSets[0]);
        if (corefLabel == null)
          // Again, this should never happen.
          {
          throw new GeneratorException("Reference to unlabelled coreference set in actor.");
          }
        else
          generate("?" + corefLabel);
        }
      }

		// Generate input/output arc separator
    generate("|");

    // Generate ouput arcs as bound labels
    outArcs = actor.getOutputArguments();

    for (int arcNo = 0; arcNo < outArcs.length; arcNo++)
      {
      CoreferenceSet corefSets[];
      
      // BUG: See bound label problem from Concept()
      corefSets = outArcs[arcNo].getCoreferenceSets();

      if (corefSets == null || corefSets.length == 0)
        // This should never happen.  If it were to be allowed, we might write a literal here.
        {
        throw new GeneratorException("Reference to concept with no coreference set in actor.");
        }
      else
        {
        corefLabel = getDefiningLabelTable(translationContext).getDefiningLabelByCoreferenceSet(corefSets[0]);
        if (corefLabel == null)
          {
          // Again, this should never happen.
          throw new GeneratorException("Reference to unlabelled coreference set in actor.");
          }
        else
          generate("?" + corefLabel);
        }
      }

		// Generate actor comment
    ActorComment(actor.getComment());

		// Generate actor termination
    generate(">");
    }

    /**
     * Generates a actor comment to output stream.
     *
     * @param comment  the actor comment to be generated.
     * @exception GeneratorException  if an error occurs while generating.
     */
  public void ActorComment(String comment) throws GeneratorException
    {
    if (comment == null)
      return;

    if (comment.length() == 0)
      return;

    generate(";"+escapeCharactersInString(comment, ">", '\\'));
    }

	/* Support Functions */

    /**
     * Increases the current indent level.
     */
	public void increaseIndent()    
		{
		currentIndent += "\t";
		}
		
    /**
     * Decreases the current indent level.
     */
	public void decreaseIndent()    
		{
		currentIndent = currentIndent.substring(0, currentIndent.length() - 1);
		}
  }
