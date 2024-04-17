package notio;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;

    /**
     * The basic conceptual graph class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.123.2.6 $, $Date: 1999/10/11 01:11:28 $
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
     * @bug Must add default class-wide setting for allowIncompleteRelations.
     * @idea Add isolateGraph() method to remove coref links to other graphs but
     * not internal links?
     * @idea How about maintaining a 'connected subgraph' ID for each node in the graph,
     * which is set when it is added.  For concepts, check relators for IDs.  For relations,
     * check arguments for IDs.  When two IDs are found the same, somehow identify them
     * with each other.  Perhaps bitfields: bit for each new connected subgraph found.  
     * Also use a set of masks to represent the real groups. 
     * If two are later found to be connected, the masks are combined but the bitfield on 
     * each node can remain the same.  Drag if we have to recompute the whole thing after
     * disconnecting something.  Better to change everyone you are connected to.  Or, could
     * create an int and point to it whenever a new subset is found.  If two different ints
     * are found to be connected, one of them simply changes value to that of the other.
     * @idea What about "graph references".  Could implement the same interface as Graph, but
     * actual be a proxy class for most methods.  The reference would keep track of the
     * context information.  This would allow sharing of graphs while maintaining context
     * traversability.
     * @idea Add Node[] findPaths(Node from, Node to, int maxLength, int maxPaths);
     * @idea What about simplify(RelationType, Concept[]) and simplify(Concept[])?
     */

public class Graph implements Serializable
  {
	  /** The concepts in this graph. **/
  private Vector concepts = new Vector(1,2);
  
	  /** The relations in this graph. **/
  private Vector relations = new Vector(1,2);
  
	  /** The comments on this graph. **/
  private Vector comments = new Vector(1,1);
  
	  /** Flag indicating whether incomplete Relation nodes may be added. **/
  private boolean allowIncompleteRelations = false;
  
	  /** An integer that specifies the current context depth of this graph. **/
  private int contextDepth = 0;

	  /** The Referent that encloses this graph, if any. **/
  private Referent enclosingReferent = null;

  /* Constructors */
    /**
     * Constructs an empty graph.
     */
  public Graph()
    {
    }

  public JsonObject toJSON() {
	  JsonObject result = new JsonObject();
	  Iterator<Concept> citr = concepts.iterator();
	  Iterator<Relation> ritr = relations.iterator();
	  JsonArray cx = new JsonArray();
	  result.add("concepts", cx);
	  while(citr.hasNext())
		  cx.add(citr.next().toJSON());
	  JsonArray rx = new JsonArray();
	  result.add("relations", rx);
	  while(ritr.hasNext())
		  rx.add(ritr.next().toJSON());
	  if (enclosingReferent  != null)
		  result.add("enclosingReferent", enclosingReferent.toJSON());
	  return result;
  }
  /* Graph building operations */

    /**
     * Adds a concept to this graph.  If the concept is already
     * part of the graph, nothing is changed and no exception
     * is thrown.
     *
     * @param newConcept  the concept to be added.
     * @exception ConceptAddError  if the specified concept is already
     * part of another graph.
     */
  public void addConcept(Concept newConcept) throws ConceptAddError
    {
    Graph gr;

    gr = newConcept.getEnclosingGraph();
    if ((gr != null) && (gr != this))
    	throw new ConceptAddError("Specified concept belongs to another graph already.");

    if (concepts.contains(newConcept))
      return;

    concepts.addElement(newConcept);
    newConcept.setEnclosingGraph(this);
    }

    /**
     * Adds one or more concepts to this graph.  Any nulls in the array of concepts
     * will be ignored.
     *
     * @param newConcepts  the array of concepts to be added.
     * @exception ConceptAddError  if one of the concepts is already
     * part of another graph.  All concepts before the offending concept are
     * still added.
     */
  public void addConcepts(Concept newConcepts[]) throws ConceptAddError
    {
    int numConcepts;

    numConcepts = newConcepts.length;
    for (int con = 0; con < numConcepts; con++)
    	if (newConcepts[con] != null)
	      addConcept(newConcepts[con]);
    }

    /**
     * Adds a relation to this graph and adds any related concepts that
     * are not already in this graph at the same time.
     *
     * @param newRelation  the relation to be added.
     * @exception notio.RelationAddError
     *            if newRelation already belongs to another graph, or
     *            if newRelation is incomplete and this graph does not allow
     *              incomplete relations.
     * @exception notio.ConceptAddError
     *            if an error is thrown whilst adding one of arguments.
     *
     * @see Graph#getAllowIncompleteRelations()
     * @bug Should check that valence is correct.  Better done in Relation.
     */
  public void addRelation(Relation newRelation) throws RelationAddError, ConceptAddError
    {
    Concept newArguments[];
		Graph gr;

		gr = newRelation.getEnclosingGraph();
		if ((gr != null) && (gr != this))
    	throw new RelationAddError("Relation already belongs to another graph.");

    if (!allowIncompleteRelations)
    	if (!newRelation.isComplete())
    		throw new RelationAddError("Addition of incomplete relations is not allowed in this graph.");

    if (relations.contains(newRelation))
      return;

    newArguments = newRelation.getArguments();

    addConcepts(newArguments);

    relations.addElement(newRelation);

    newRelation.setEnclosingGraph(this);
    }

    /**
     * Adds one or more relations to this graph and adds any
     * related concepts that are not already in this graph at
     * the same time.  Any nulls in the array of relations will be ignored.
     *
     * @param newRelations  the array of relations to be added.
     * @exception notio.RelationAddError
     *            if one of the relations already belongs to another graph.
     *						All additions up to the one that causes the error
     *						are still performed.
     * @exception notio.ConceptAddError
     *            if an error is thrown whilst adding one of arguments.
     */
  public void addRelations(Relation newRelations[]) throws RelationAddError, ConceptAddError
    {
    int numRelations;

    numRelations = newRelations.length;
    for (int rel = 0; rel < numRelations; rel++)
    	if (newRelations[rel] != null)
        addRelation(newRelations[rel]);
    }

    /**
     * Replaces one concept with another in a graph.  The new concept fills
     * exactly the same role as the old concept with respect to relations 
     * and coreference sets.
     *
     * @param oldConcept  the old concept being replaced.
     * @param newConcept  the new concept that replaces the old.
     *
     * @exception notio.ConceptReplaceException
     *            if oldConcept is not present in the graph.
     *
     * @bug Should review the coreference stuff for this.  Does removal need to be deeper?
     */
  public void replaceConcept(Concept oldConcept, Concept newConcept) throws ConceptReplaceException
    {
    int indexOfConcept, numRelations;
    Relation relArray[];
    CoreferenceSet corefSets[];

    indexOfConcept = concepts.indexOf(oldConcept);

    if (indexOfConcept == -1)
      throw new ConceptReplaceException("Concept is not present in graph.");

    concepts.removeElement(oldConcept);
    oldConcept.setEnclosingGraph(null);

    concepts.addElement(newConcept);
    newConcept.setEnclosingGraph(this);
    
		// Replace in all coreferences sets
		corefSets = oldConcept.getCoreferenceSets();
		for (int set = 0; set < corefSets.length; set++)
			{
			// Add new concept to sets
			try
				{
				corefSets[set].addCoreferentConcept(newConcept);
				}
			catch (CorefAddException addException)
				{
				// If we threw an exception, we must add the concept back into any sets it has been
				// removed from so far and remove the new concept
				for (int reset = 0; reset < set; reset++)
					{
					try
						{
						corefSets[reset].addCoreferentConcept(oldConcept);
						corefSets[reset].removeCoreferentConcept(newConcept);
						}
					catch (CorefAddException secondAddException)
						{
						// This should never happen since the concept was part of the set before.
						secondAddException.printStackTrace();
						System.exit(1);
						}
					catch (CorefRemoveException removeException)
						{
						// This should never happen since the concept was not part of the set before.
						removeException.printStackTrace();
						System.exit(1);
						}
					}
				}
				
			// Remove old concept from sets				
			try
				{
				corefSets[set].removeCoreferentConcept(oldConcept);
				}
			catch (CorefRemoveException removeException)
				{
				// If we threw an exception, we must add the concept back into any sets it has been
				// removed from so far and remove the new concept
				for (int reset = 0; reset < set; reset++)
					{
					try
						{
						corefSets[reset].addCoreferentConcept(oldConcept);
						corefSets[reset].removeCoreferentConcept(newConcept);
						}
					catch (CorefAddException addException)
						{
						// This should never happen since the concept was part of the set before.
						addException.printStackTrace();
						System.exit(1);
						}
					catch (CorefRemoveException secondRemoveException)
						{
						// This should never happen since the concept was not part of the set before.
						secondRemoveException.printStackTrace();
						System.exit(1);
						}
					}
						
				throw new ConceptReplaceException("Replacement of this concept results in an invalid coreference set.");
				}
    	}
    	  
    relArray = getRelations();
    numRelations = relArray.length;

    for (int rel = 0; rel < numRelations; rel++)
      relArray[rel].replaceArgument(oldConcept, newConcept);
    }

    /**
     * Removes the specified concept from this graph.  Any relations
     * involving the concept must be removed before the concept
     * may be removed.  Any corefence links to this concept are also removed.
     *
     * @param deadConcept  the concept to be removed.
     *
     * @exception notio.ConceptRemoveException
     *            if deadConcept is involved in a relation in this graph,
     *            or if deadConcept is not present in this graph,
     *						or if removal of deadConcept will result in an invalid coreference set.
     */
  public void removeConcept(Concept deadConcept) throws ConceptRemoveException
    {
    int indexOfConcept, numRelations, numArguments;
    Relation relArray[];
    Concept arguments[];
    CoreferenceSet corefSets[];

    indexOfConcept = concepts.indexOf(deadConcept);

    if (indexOfConcept == -1)
      throw new ConceptRemoveException("Concept is not present in graph.");

		// Remove all coreferences links from this concept
		corefSets = deadConcept.getCoreferenceSets();
		for (int set = 0; set < corefSets.length; set++)
			try
				{
				corefSets[set].removeCoreferentConcept(deadConcept);
				}
			catch (CorefRemoveException e)
				{
				// If we threw an exception, we must add the concept back into any sets it has been
				// removed from so far
				for (int reset = 0; reset < set; reset++)
					try
						{
						corefSets[reset].addCoreferentConcept(deadConcept);
						}
					catch (CorefAddException e2)
						{
						// This should never happen since the concept was part of the set before.
						e.printStackTrace();
						System.exit(1);
						}
						
				throw new ConceptRemoveException("Removal of this concept results in an invalid coreference set.");
				}

		// Remove this concept from any relators it may have.
    relArray = getRelations();
    numRelations = relArray.length;

    for (int rel = 0; rel < numRelations; rel++)
      {
      arguments = relArray[rel].getArguments();
      numArguments = arguments.length;

      for (int arg = 0; arg < numArguments; arg++)
        if (arguments[arg] == deadConcept)
          throw new ConceptRemoveException("Concept is part of a relation in graph.");
      }

    deadConcept.setEnclosingGraph(null);
    concepts.removeElementAt(indexOfConcept);
    }

    /**
     * Removes the specified concepts from this graph.
     * Any nulls in the array will be ignored.
     *
     * @param deadConcepts  the array of concepts to be removed.
     *
     * @exception notio.ConceptRemoveException
     *            if one of the concepts is part of a relation in this graph,
     *            or if one of the concepts is not present in this graph,
     *						or if one of the removals will result in an invalid coreference set.
     *            All removals up to the one that causes the exception are
     *            still performed.
     */
  public void removeConcepts(Concept deadConcepts[]) throws ConceptRemoveException
    {
    int numConcepts;

    numConcepts = deadConcepts.length;

    for (int con = 0; con < numConcepts; con++)
    	if (deadConcepts[con] != null)
	      removeConcept(deadConcepts[con]);
    }

    /**
     * Removes the specified relation from this graph.  If the
     * relation is not part of this graph, nothing happens.
     * Note: This does not remove any of the related concepts.
     *
     * @param deadRelation  the relation to be removed.
     */
  public void removeRelation(Relation deadRelation)
    {
    int indexOfRelation;

    indexOfRelation = relations.indexOf(deadRelation);

    if (indexOfRelation == -1)
      return;

    deadRelation.setEnclosingGraph(null);
    relations.removeElementAt(indexOfRelation);
    }

    /**
     * Removes the specified relations from this graph.  If a
     * relation is not part of the graph, nothing happens and
     * all other relations are removed as usual.  Any nulls in the
     * array will be ignored.
     * Note: This does not remove any of the related concepts.
     *
     * @param deadRelations  the array of relations to be removed.
     */
  public void removeRelations(Relation deadRelations[])
    {
    int numRelations;

    numRelations = deadRelations.length;
    for (int rel = 0; rel < numRelations; rel++)
      removeRelation(deadRelations[rel]);
    }


  /* Query Operations */

    /**
     * Returns the context of this graph or null if this graph is in the outermost context.
     * This may also be thought of as the enclosing concept of this graph, accessible via 
     * the enclosing referent.  This method is provided for convenience since the 
     * enclosing concept of a graph is often of interest.
     *
     * @return the concept that is the context of the graph or null if it is
     *          not enclosed by a concept.
     */
  public Concept getContext()
    {
    if (enclosingReferent == null)
    	return null;
    
    return enclosingReferent.getEnclosingConcept();
    }

    /**
     * Returns the graph containing the context of this graph or null if this graph is 
     * not enclosed by a graph.
     * This is equivalent to calling getEnclosingGraph() in this graph's context (if it has
     * one).
		 * This method is provided for convenience since the enclosing graph 
     * of a graph is often of interest.
     *
     * @return the concept that is the context of the graph or null if it is
     *         not enclosed by a graph.
     *
     * @bug Should this be called getEnclosingGraph() instead?
     */
  public Graph getContextGraph()
    {
    Concept enclosingConcept;
    
    enclosingConcept = getContext();

    if (enclosingConcept == null)
    	return null;
    	
    return enclosingConcept.getEnclosingGraph();
    }    
    
   /**
    * Sets the enclosing referent for this graph.
    *
    * @param newReferent  the referent enclosing this graph.
    *
    * @impspec this method is present to support the getEnclosingReferent() method.
		*/
	void setEnclosingReferent(Referent newReferent)
		{
		enclosingReferent = newReferent;
		}

   /**
    * Return the enclosing referent for this graph if it is nested or null otherwise.
    *
    * @return returns the referent enclosing this graph or null if it is not enclosed by
    * by a referent.
		*/
	public Referent getEnclosingReferent()
		{
		return enclosingReferent;
		}

    /**
     * Returns the context depth (level of nesting) of this graph.  A depth of zero
     * indicates that this graph is at the outermost level.
     * 
     * @return the context depth of this graph.
     */
  public int getContextDepth()
  	{
  	int depth;
  	Graph enclosingGraph = this;
  	
  	depth = -1;
		
		do  	
  		{
			enclosingGraph = enclosingGraph.getContextGraph();
  		depth++;
  		}
  	while (enclosingGraph != null);
  		
  	return depth;
  	}

		/**
		 * Returns true if this graph is enclosed by the specified concept.
		 *
		 * @param concept  the concept being checked for as enclosing this graph.
		 * @return true if this graph is enclosed by the specified concept.
		 */
	public boolean isEnclosedBy(Concept concept)
		{
  	Concept enclosingConcept;
  	Graph enclosingGraph;

  	enclosingGraph = this;
  	
		while (true)
  		{
  		if (enclosingGraph == null)
  			return false;
  		else
				enclosingConcept = enclosingGraph.getContext();
				
			if (enclosingConcept == concept)
				return true;

			if (enclosingConcept == null)
				return false;
			else
				enclosingGraph = enclosingConcept.getEnclosingGraph();
  		}
		}
		
		/**
		 * Returns true if this graph is enclosed by the specified graph.
		 *
		 * @param graph  the graph being checked for as enclosing this graph.
		 * @return true if this graph is enclosed by the specified graph.
		 */
	public boolean isEnclosedBy(Graph graph)
		{
  	Graph enclosingGraph;

		enclosingGraph = this;

		do
			{
	  	enclosingGraph = enclosingGraph.getContextGraph();
  	
  		if (enclosingGraph == graph)
  			return true;
  		}
  	while (enclosingGraph != null);
  	
  	return false;
		}

    /**
     * Returns a list of the concepts in this graph.
     * No guarantee is made as to the ordering of the concepts in the array.
     * Subsequent calls to this method may produce different orderings.
     *
     * @return an array containing all concepts in this graph.
     */
  public Concept[] getConcepts()
    {
    Concept conceptArr[];

    conceptArr = new Concept[concepts.size()];
    concepts.copyInto(conceptArr);

    return conceptArr;
    }

    /**
     * Returns the number of concepts in this graph.
     *
     * @return the number of concepts in this graph.
     */
  public int getNumberOfConcepts()
    {
    return concepts.size();
    }

    /**
     * Returns a list of the relations in this graph.
     * This includes all relations, including Actors.
     * No guarantee is made as to the ordering of the relations in the array.
     * Subsequent calls to this method may produce different orderings.
     *
     * @return an array containing all relations in this graph.
     */
  public Relation[] getRelations()
    {
    Relation relationArr[];

    relationArr = new Relation[relations.size()];
    relations.copyInto(relationArr);

    return relationArr;
    }

    /**
     * Returns the number of relations in this graph.
     * This includes all relations including Actors rather than just Relations.
     *
     * @return the number of relations in this graph.
     */
  public int getNumberOfRelations()
    {
    return relations.size();
    }

    /**
     * Returns a list of the those relations in this graph that are actors.
     * No guarantee is made as to the ordering of the actors in the array.
     * Subsequent calls to this method may produce different orderings.
     *
     * @return an array containing all actors in this graph.
     * @idea Possibly cache results of this operation?
     */
  public Actor[] getActorRelations()
    {
    Actor actorArr[], outArr[];
    Relation relArr[];
    int numActors;
    
    relArr = new Relation[relations.size()];
    relations.copyInto(relArr);

    actorArr = new Actor[relations.size()];
    
    numActors = 0;
    
    for (int rel = 0; rel < relArr.length; rel++)
    	if (relArr[rel] instanceof Actor)
    		{
    		actorArr[numActors] = (Actor)relArr[rel];
    		numActors++;
    		}

		outArr = new Actor[numActors];
    System.arraycopy(actorArr, 0, outArr, 0, numActors);
    		
    return outArr;
    }

    /**
     * Returns a list of the those relations in this graph that are NOT actors.
     * No guarantee is made as to the ordering of the relations in the array.
     * Subsequent calls to this method may produce different orderings.
     *
     * @return an array containing all non-actor relations in this graph.
     * @idea Possibly cache results of this operation?
     */
  public Relation[] getNormalRelations()
    {
    Relation tempArr[], outArr[];
    Relation relArr[];
    int numNormal;
    
    relArr = new Relation[relations.size()];
    relations.copyInto(relArr);

    tempArr = new Relation[relations.size()];
    
    numNormal = 0;
    
    for (int rel = 0; rel < relArr.length; rel++)
    	if (!(relArr[rel] instanceof Actor))
    		{
    		tempArr[numNormal] = relArr[rel];
    		numNormal++;
    		}

		outArr = new Relation[numNormal];
    System.arraycopy(tempArr, 0, outArr, 0, numNormal);
    		
    return outArr;
    }

    /**
     * Returns true if the graph contains the specified concept.
     *
     * @param concept  the concept being checked for.
     * @return true if the specified concept is part of this graph.
     */
  public boolean hasConcept(Concept concept)
    {
    return concepts.contains(concept);
    }

    /**
     * Returns true if the graph contains the specified concepts.
     *
     * @param conceptArr  the array of concepts being checked for.
     * @return true if the specified concepts are part of this graph.
     */
  public boolean hasConcepts(Concept conceptArr[])
    {
    for (int con = 0; con < conceptArr.length; con++)
      if (!concepts.contains(conceptArr[con]))
        return false;

    return true;
    }

    /**
     * Returns true if the graph contains the specified relation.
     *
     * @param relation  the relation being checked for.
     * @return true if the specified relation is part of this graph.
     */
  public boolean hasRelation(Relation relation)
    {
    return relations.contains(relation);
    }

    /**
     * Returns true if the graph reltains the specified relations.
     *
     * @param relationArr  the array of relations being checked for.
     * @return true if the specified relations are part of this graph.
     */
  public boolean hasRelations(Relation relationArr[])
    {
    for (int rel = 0; rel < relationArr.length; rel++)
      if (!relations.contains(relationArr[rel]))
        return false;

    return true;
    }

    /**
     * Returns all nodes in the graph with exactly the type specified.
     * Subtypes will not be included.
     *
     * @param nodeType  the node type to be matched exactly.
     * @param nodeArr  the array of nodes to selected from.
     * @param matchType 0 for exact match, 1 for supertype, -1 for subtype.
     * @return an array of all nodes of the exactly the specified type
     *         or null if no nodes of the specified type are present in
     *         the graph.
     *
     * @impspec An easy way of centralizing this activity.
     */
  Node[] getNodesWithType(Type nodeType, Node nodeArr[],
  	int matchType)
    {
    int numNodes;
    Node finalArr[];
    Vector nodeVec;
    Type tType;
    TypeHierarchy hier;

    numNodes = nodeArr.length;

    nodeVec = new Vector(numNodes);

		switch (matchType)
			{
			case -1:
				{
				hier = nodeType.getHierarchy();

		    for (int node = 0; node < numNodes; node++)
		    	{
		    	tType = nodeArr[node].getNodeType();
    		  if (hier.isSuperTypeOf(tType, nodeType))
        		nodeVec.addElement(nodeArr[node]);
        	}
        break;
        }

			case 0:
				{
		    for (int node = 0; node < numNodes; node++)
    		  if (nodeArr[node].getNodeType() == nodeType)
        		nodeVec.addElement(nodeArr[node]);
        break;
        }

			case 1:
				{
				hier = nodeType.getHierarchy();

		    for (int node = 0; node < numNodes; node++)
		    	{
		    	tType = nodeArr[node].getNodeType();
    		  if (hier.isSubTypeOf(tType, nodeType))
        		nodeVec.addElement(nodeArr[node]);
        	}
        break;
        }

      default:
      	throw new Error("Bad matchFlag in Graph.getNodesWithType().");
      }

		// Gruesome little hack to ensure that the array returned is of the correct type.
		if (nodeArr instanceof Concept[])
	    finalArr = new Concept[nodeVec.size()];
		else
	    finalArr = new Relation[nodeVec.size()];
			
    nodeVec.copyInto(finalArr);

    return finalArr;
    }

    /**
     * Returns all concepts in the graph with exactly the type specified.
     * Subtypes will not be included.
     *
     * @param conType  the concept type to be matched exactly.
     * @return an array of all concepts of the exactly the specified type
     *         or null if no concepts of the specified type are present in
     *         the graph.
     */
  public Concept[] getConceptsWithExactType(ConceptType conType)
    {
    return (Concept[])getNodesWithType(conType, getConcepts(), 0);
    }

    /**
     * Returns all concepts in the graph that are subtypes of the specified
     * type (which includes the type itself).
     *
     * @param conType  the concept type whose subtypes will be matched.
     * @return an array of all concepts that are subtypes of the specified
     *         type.
     */
  public Concept[] getConceptsWithSuperType(ConceptType conType)
    {
    return (Concept[])getNodesWithType(conType, getConcepts(), 1);
    }

    /**
     * Returns all concepts in the graph that are supertypes of the specified
     * type (which includes the type itself).
     *
     * @param conType  the concept type whose supertypes will be matched.
     * @return an array of all concepts that are supertypes of the specified
     *         type.
     */
  public Concept[] getConceptsWithSubType(ConceptType conType)
    {
    return (Concept[])getNodesWithType(conType, getConcepts(), -1);
    }

    /**
     * Returns all relations in the graph with exactly the type specified.
     * Subtypes will not be included.
     *
     * @param relType  the relation type to be matched exactly.
     * @return an array of all relations of the exactly the specified type
     *         or null if no relations of the specified type are present in
     *         the graph.
     */
  public Relation[] getRelationsWithExactType(RelationType relType)
    {
    return (Relation[])getNodesWithType(relType, getRelations(), 0);
    }

    /**
     * Returns all relations in the graph that are subtypes of the specified
     * type (which includes the type itself).
     *
     * @param relType  the relation type whose subtypes will be matched.
     * @return an array of all relations that are subtypes of the specified
     *         type.
     */
  public Relation[] getRelationsWithSuperType(RelationType relType)
    {
    return (Relation[])getNodesWithType(relType, getRelations(), 1);
    }

    /**
     * Returns all relations in the graph that are supertypes of the specified
     * type (which includes the type itself).
     *
     * @param relType  the relation type whose supertypes will be matched.
     * @return an array of all relations that are supertypes of the specified
     *         type.
     */
  public Relation[] getRelationsWithSubType(RelationType relType)
    {
    return (Relation[])getNodesWithType(relType, getRelations(), -1);
    }

  /* Expansion/Contraction Operations */

    /**
     * Performs minimal type expansion on the specified concept if a type definition
     * is available for that type.
     *
     * @param concept  the concept which is to undergo type expansion.
     *
     * @bug Should we throw an exception if there is no type definition?
     */
  public void expandConceptType(Concept concept) throws TypeExpansionException
    {
    ConceptTypeDefinition typeDef;
    Graph defCopy;

    typeDef = concept.getType().getTypeDefinition();

    if (typeDef == null)
      throw new TypeExpansionException("No type definition for specified type.");

//    defCopy = new Graph(typeDef.getDifferentia());

    }

    /**
     * Performs minimal type expansion on the specified concepts if type definitions
     * is available for their types.
     *
     * @param concepts  the array of concepts to undergo type expansion.
     *
     * @bug Should we throw an exception if there is no type definition?
     */
  public void expandConceptType(Concept concepts[]) throws TypeExpansionException
    {
    int numConcepts;

    numConcepts = concepts.length;
    for (int con = 0; con < numConcepts; con++)
      expandConceptType(concepts[con]);
    }

    /**
     * Performs minimal type expansion on all concepts in this graph of the specified
     * type, if a type definition is available for that type.
     *
     * @param conceptType  the concept type to expand.
     *
     * @bug Should we throw an exception if there is no type definition?
     */
  public void expandConceptType(ConceptType conceptType) throws TypeExpansionException
    {
    Concept concepts[];

    if (conceptType.getTypeDefinition() == null)
      throw new TypeExpansionException("No type definition for specified type.");

    concepts = getConceptsWithExactType(conceptType);
    expandConceptType(concepts);
    }

    /**
     * Performs minimal type expansion on the specified relation if a type definition
     * is available for that type.
     *
     * @param relation  the relation which is to undergo type expansion.
     *
     * @bug Should we throw an exception if there is no type definition?
     */
  public void expandRelationType(Relation relation) throws TypeExpansionException
    {
    RelationTypeDefinition typeDef;
    Graph defCopy;

    typeDef = relation.getType().getTypeDefinition();

    if (typeDef == null)
      throw new TypeExpansionException("No type definition for specified type.");


    }

    /**
     * Performs minimal type expansion on the specified relations if type definitions
     * is available for their types.
     *
     * @param relations  the array of relations to undergo type expansion.
     *
     * @bug Should we throw an exception if there is no type definition?
     */
  public void expandRelationType(Relation relations[]) throws TypeExpansionException
    {
    int numRelations;

    numRelations = relations.length;
    for (int rel = 0; rel < numRelations; rel++)
      expandRelationType(relations[rel]);
    }

    /**
     * Performs minimal type expansion on all relations in this graph of the specified
     * type, if a type definition is available for that type.
     *
     * @param relationType  the relation type to expand.
     *
     * @bug Should we throw an exception if there is no type definition?
     */
  public void expandRelationType(RelationType relationType) throws TypeExpansionException
    {
    Relation relations[];

    if (relationType.getTypeDefinition() == null)
      throw new TypeExpansionException("No type definition for specified type.");

    relations = getRelationsWithExactType(relationType);
    expandRelationType(relations);
    }


		/**
		 * Returns true if this graph is blank.  Blank means that there
		 * are no concepts, relations, or actors in this graph.
		 *
		 * @return true if this graph is blank.
		 */
  public boolean isBlank()
  	{
  	if (concepts.size() > 0)
  		return false;

  	if (relations.size() > 0)
  		return false;

  	return true;
  	}

		/**
		 * Returns true if this graph is complete.  A complete
		 * graph is one in which all relations are complete.
		 *
		 * @return true if this graph is complete.
		 *
		 * @see notio.Relation#isComplete()
		 */
  public boolean isComplete()
  	{
  	Relation relArr[];

  	relArr = getRelations();
  	for (int rel = 0; rel < relArr.length; rel++)
  		if (!relArr[rel].isComplete())
  			return false;

  	return true;
  	}

  /* Canonical Operations */
  
		/**
     * Performs a copy operation on this graph according to the
     * the specified CopyingScheme.
     * This method will always produce a new Graph object and only nested graphs
     * will be affect by the value of MatchingScheme.getGraphFlag().
  	 *
  	 * @param copyScheme  the copying scheme used to control the copy operation.
  	 * @return the result of the copy operation.
		 */
	public Graph copy(CopyingScheme copyScheme)
		{
		return copy(copyScheme, new Hashtable());
		}

		/**
     * Performs a copy operation on this graph according to the
     * the specified CopyingScheme.
     * This method will always produce a new object of the exact same type
     * as the original and only nested graphs
     * will be affected by the value of MatchingScheme.getGraphFlag().
     * Note that the flag returned by Graph.getAllowIncompleteRelation() will be
     * copied to the new graph.
  	 *
  	 * @param copyScheme  the copying scheme used to control the copy operation.
  	 * @param substitutionTable  a hashtable containing copied objects available due to 
     * earlier copy operations.
  	 * @return the result of the copy operation.
  	 *
  	 * @see notio.Graph#getAllowIncompleteRelations
		 */
	public Graph copy(CopyingScheme copyScheme, Hashtable substitutionTable)
		{
		Graph newGraph;
    Concept originalConcepts[], newConcepts[];
    Relation originalRelations[], newRelations[];
    int numConcepts, numRelations;

		// Check for graph in substitution table and return its substitute if it exists.
		newGraph = (Graph)substitutionTable.get(this);
		if (newGraph != null)
			return newGraph;

		// Since a substitute was not found, create an empty graph and add copies of all 
		// nodes to it.
		
		try
			{
			newGraph = (Graph)this.getClass().newInstance();
			}
		catch (java.lang.InstantiationException e)
			{
			// This will only occur in a poorly designed subclass
			e.printStackTrace();
			System.exit(1);
			}
		catch (java.lang.IllegalAccessException e)
			{
			// This will only occur in a poorly designed subclass
			e.printStackTrace();
			System.exit(1);
			}

		// Copy the allowIncompleteRelations flag
		newGraph.setAllowIncompleteRelations(getAllowIncompleteRelations());

    originalConcepts = getConcepts();
    numConcepts = originalConcepts.length;
    originalRelations = getRelations();
    numRelations = originalRelations.length;

    // Copy and add concepts

    newConcepts = new Concept[numConcepts];

    for (int con = 0; con < numConcepts; con++)
      {
      newConcepts[con] = originalConcepts[con].copy(copyScheme, substitutionTable);
      }

		newGraph.addConcepts(newConcepts);

		// Copy and add relations.

		newRelations = new Relation[numRelations];

    for (int rel = 0; rel < numRelations; rel++)
      {
      newRelations[rel] = originalRelations[rel].copy(copyScheme, substitutionTable);
      }

    newGraph.addRelations(newRelations);

    // Copy comments if the scheme requires it
    if (copyScheme.getCommentFlag() == CopyingScheme.COMM_COPY_ON)
    	{
    	String comments[];
    	
    	comments = getComments();
    	for (int comm = 0; comm < comments.length; comm++)
    		newGraph.addComment(comments[comm]);
    	}

    // Add pair to substitution table
    substitutionTable.put(this, newGraph);
    
    return newGraph;
		}

   /**
    * Joins two graphs on the specified concept nodes using the specified matching scheme
    * to determine if the nodes form a valid join point.
    *
    * @param firstGraph  the first graph to be joined.
    * @param firstConcept  the joining concept in the first graph.
    * @param secondGraph  the second graph to be joined.
    * @param secondConcept  the joining concept in the second graph.
    * @param matchingScheme  the matching scheme used to determine if the specified concepts
    * are a valid join point.
    * @param copyScheme  the copying scheme used to create the new graph from the two being
    * joined.
    * @return the graph that results from the join.
    * @exception notio.JoinException
    *            if specified concepts are not part of the specified graphs
    *            and/or if the concepts do not form a valid join point.
    */
  public static Graph join(Graph firstGraph, Concept firstConcept, Graph secondGraph,
    Concept secondConcept, MatchingScheme matchingScheme, CopyingScheme copyScheme
    ) throws JoinException
    {
    Concept firstConcepts[] = new Concept[1];
    Concept secondConcepts[] = new Concept[1];

    firstConcepts[0] = firstConcept;
    secondConcepts[0] = secondConcept;
    
    return join(firstGraph, firstConcepts, secondGraph, secondConcepts, matchingScheme, copyScheme);
    }

   /**
    * Joins two graphs on the specified concept nodes using the specified matching scheme
    * to determine if the nodes form a valid join point.
    *
    * @param firstGraph  the first graph to be joined.
    * @param firstConcepts  the joining concepts in the first graph.
    * @param secondGraph  the second graph to be joined.
    * @param secondConcepts  the joining concepts in the second graph.
    * @param matchingScheme  the matching scheme used to determine if the specified concepts
    * form valid join points.
    * @param copyScheme  the copying scheme used to create the new graph from the two being
    * joined.
    * @return the graph that results from the join.
    * @exception notio.JoinException
    *            if specified concepts are not part of the specified graphs
    *            and/or if the concepts do not form valid join points.
    * @bug Need to examine the copy operations especially with regard to coreference.
    * @bug Need to use newInstance() to construct graph.
    */
  public static Graph join(Graph firstGraph, Concept firstConcepts[], Graph secondGraph,
    Concept secondConcepts[], MatchingScheme matchingScheme, CopyingScheme copyScheme)
    throws JoinException
    {
    Graph newGraph;
    Concept orgConcepts[], newConcepts[], copyConcepts[], joinConcepts[];
    Relation orgRelations[], copyRelation;
    int numJoins;
    Hashtable substitutionTable;

    // Check that same number of joining nodes specified for each graph
    numJoins = firstConcepts.length;
    if (numJoins != secondConcepts.length)
      throw new JoinException("Different number of joining points specified for each graph.");

    // Check that join nodes are part of their respective graphs
    if (!firstGraph.hasConcepts(firstConcepts))
      throw new JoinException("Specified joining concept not part of first graph.");

    if (!secondGraph.hasConcepts(secondConcepts))
      throw new JoinException("Specified joining concept not part of second graph.");

    // Check if the concepts form valid join points
    for (int join = 0; join < numJoins; join++)
      if (!Concept.matchConcepts(firstConcepts[join], secondConcepts[join], matchingScheme).matchSucceeded())
        throw new JoinException("Specified concepts do not form a valid join point.");

    // Create a new empty graph
    newGraph = new Graph();

		// Create substitution table for use in this copy operation.
		substitutionTable = new Hashtable();

    // Add copies of all concepts in the first graph including the joining concepts
    // Keep track of the copies of the joining concepts
    orgConcepts = firstGraph.getConcepts();
    newConcepts = new Concept[orgConcepts.length];
    joinConcepts = new Concept[numJoins];
    for (int con = 0; con < orgConcepts.length; con++)
      {
      int join = 0;
      
      newConcepts[con] = orgConcepts[con].copy(copyScheme, substitutionTable);
      
      // Loop through searching for joining concept corresponding to graph concept so
      // we can keep track of copied join points.
      while ((join < numJoins) && (orgConcepts[con] != firstConcepts[join]))
      	join++;
      
      if (join < numJoins)
        joinConcepts[join] = newConcepts[con];
      }
      
    newGraph.addConcepts(newConcepts);

    // Add copies of all relations in the first graph, replace arguments as we copy
    orgRelations = firstGraph.getRelations();
    for (int rel = 0; rel < orgRelations.length; rel++)
      {
      copyRelation = orgRelations[rel].copy(copyScheme, substitutionTable);
      for (int con = 0; con < orgConcepts.length; con++)
        copyRelation.replaceArgument(orgConcepts[con], newConcepts[con]);

      newGraph.addRelation(copyRelation);
      }

    // Add copies of all concepts in the second graph excluding the joining concepts
    // Cunningly replace the joining concepts with the copy of the corresponding
    // joining concept
    orgConcepts = secondGraph.getConcepts();
    newConcepts = new Concept[orgConcepts.length];
    for (int con = 0; con < orgConcepts.length; con++)
      {
      int join = -1;
      do
        {
        join++;
        }
      while ((join < numJoins) && (orgConcepts[con] != secondConcepts[join]));

      if (join == numJoins)
        newConcepts[con] = orgConcepts[con].copy(copyScheme, substitutionTable);
      else
      	{
        newConcepts[con] = joinConcepts[join];
        // Add entry to substitution table so that all future references to the joining
        // concepts from the second graph will be replaced by the copies of the joining
        // concepts from the first graph.
        substitutionTable.put(orgConcepts[con], joinConcepts[join]);
        }
      }
      
    newGraph.addConcepts(newConcepts);

    // Add copies of all relations in the second graph, replace arguments as we copy
    orgRelations = secondGraph.getRelations();
    for (int rel = 0; rel < orgRelations.length; rel++)
      {
      copyRelation = orgRelations[rel].copy(copyScheme, substitutionTable);
      for (int con = 0; con < orgConcepts.length; con++)
        copyRelation.replaceArgument(orgConcepts[con], newConcepts[con]);

      newGraph.addRelation(copyRelation);
      }

    return newGraph;
    }

   /**
    * Maximally joins two graphs using all possible mappings.
    *
    * @param firstGraph  the first graph to be joined.
    * @param secondGraph  the second graph to be joined.
    * @param matchingScheme  the matching scheme used to determine if the specified concepts
    * form valid join points.
    * @param copyScheme  the copying scheme used to create the new graph from the two being
    * joined.
    * @return the graphs that form the set of all possible maximal joins
    *         or null if no join exists.
    * @exception notio.JoinException
    *            if specified concepts are not part of the specified graphs
    *            and/or if the concepts do not form valid join points.
    */
  public static Graph[] maximalJoin(Graph firstGraph, Graph secondGraph,
  	MatchingScheme matchingScheme, CopyingScheme copyScheme) throws JoinException
    {
    return maximalJoin(firstGraph, secondGraph, matchingScheme, copyScheme, 0);
    }

   /**
    * Maximally joins two graphs, using N or fewer node mappings, depending
    * on what is possible.  If the specified N less than 1, all possible
    * mappings will be used.
    *
    * @param firstGraph  the first graph to be joined.
    * @param secondGraph  the second graph to be joined.
    * @param matchingScheme  the matching scheme used to determine if the specified concepts
    * form valid join points.
    * @param copyScheme  the copying scheme used to create the new graph from the two being
    * joined.
    * @param N  the maximum number of graphs that will be produced.
    * @return up to N graphs that are maximal joins
    *         or null if no join exists.
    * @exception notio.JoinException
    *            if specified concepts are not part of the specified graphs
    *            and/or if the concepts do not form valid join points.
    */
  public static Graph[] maximalJoin(Graph firstGraph, Graph secondGraph,
    MatchingScheme matchingScheme, CopyingScheme copyScheme, int N) throws JoinException
    {
    NodeMappingGenerator gen;
    NodeMapping mapping;
    Vector vec = new Vector();
    Graph results[];

    // Create mapping generator that maps concepts but not relations.
    gen = new NodeMappingGenerator(firstGraph, secondGraph, matchingScheme,
      true, false);

    // If N is less than 1, set N so that we get all mappings
    if (N < 1)
      N = -1;

    while ((N != 0) && ((mapping = gen.getNextMapping()) != null))
      {
      vec.addElement(join(firstGraph, mapping.getFirstConcepts(),
        secondGraph, mapping.getSecondConcepts(), matchingScheme, copyScheme));
      }

    if (vec.size() == 0)
      return null;
    else
      {
      results = new Graph[vec.size()];
      vec.copyInto(results);
      return results;
      }
    }

   /**
     * Simplifies the specified relation node by removing any duplicates of
     * that relation in this graph.
     *
     * @param relation  the relation node to simplify.
     * @exception IllegalArgumentException  if the specified relation is not part of this
     * graph.
     */
	public void simplify(Relation relation)
		{
		Relation relArr[], otherRel;
		MatchingScheme simplifyScheme;

		if (!hasRelation(relation))
			throw new IllegalArgumentException("Specified relation is not part of the graph.");

		simplifyScheme = new MatchingScheme(
			MatchingScheme.GR_MATCH_ANYTHING,
			MatchingScheme.CN_MATCH_INSTANCE,
			MatchingScheme.RN_MATCH_ALL,
			MatchingScheme.CT_MATCH_ANYTHING,
			MatchingScheme.RT_MATCH_LABEL,
			MatchingScheme.QF_MATCH_ANYTHING,
			MatchingScheme.DG_MATCH_ANYTHING,
			MatchingScheme.MARKER_MATCH_ANYTHING,
			MatchingScheme.ARC_MATCH_INSTANCE,
			MatchingScheme.COREF_AUTOMATCH_ON,
			MatchingScheme.COREF_AGREE_OFF,
			MatchingScheme.FOLD_MATCH_OFF,
			MatchingScheme.CONN_MATCH_ON,
			1,
			null,
			null);

		relArr = getRelations();

		for (int rel = 0; rel < relArr.length; rel++)
			{
			otherRel = relArr[rel];
			if (otherRel != relation)
				if (Relation.matchRelations(relation, otherRel, simplifyScheme))
					removeRelation(otherRel);
			}
		}

   /**
     * Simplifies all instances of the specified relation type in this graph.
     *
     * @param relType  the relation type whose instances are to be simplified.
     */
	public void simplify(RelationType relType)
		{
		Relation relArr[], rightArr[];
		boolean handled[];
		int rightCount;
		MatchingScheme simplifyScheme;

		simplifyScheme = new MatchingScheme(
			MatchingScheme.GR_MATCH_ANYTHING,
			MatchingScheme.CN_MATCH_INSTANCE,
			MatchingScheme.RN_MATCH_ALL,
			MatchingScheme.CT_MATCH_ANYTHING,
			MatchingScheme.RT_MATCH_LABEL,
			MatchingScheme.QF_MATCH_ANYTHING,
			MatchingScheme.DG_MATCH_ANYTHING,
			MatchingScheme.MARKER_MATCH_ANYTHING,
			MatchingScheme.ARC_MATCH_INSTANCE,
			MatchingScheme.COREF_AUTOMATCH_ON,
			MatchingScheme.COREF_AGREE_OFF,
			MatchingScheme.FOLD_MATCH_OFF,
			MatchingScheme.CONN_MATCH_ON,
			1,
			null,
			null);

		relArr = getRelations();
		rightArr = new Relation[relArr.length];

		rightCount = 0;

		for (int rel = 0; rel < relArr.length; rel++)
			if (relArr[rel].getType() == relType)
				{
				rightArr[rightCount] = relArr[rel];
				rightCount++;
				}

		handled = new boolean[rightCount];

		for (int rel = 0; rel < rightCount; rel++)
			{
			if (!handled[rel])
				{
				handled[rel] = true;
			
				for (int other = rel + 1; other < rightCount; other++)
					{
					if (!handled[other])
						if (Relation.matchRelations(rightArr[rel], rightArr[other], simplifyScheme))
							{
							removeRelation(rightArr[other]);
							handled[other] = true;
							}
				  }
				}
			}
		}

   /**
     * Simplifies all relations in this graph.
     */
	public void simplify()
		{
		Relation relArr[];
		boolean handled[];
		MatchingScheme simplifyScheme;

		simplifyScheme = new MatchingScheme(
			MatchingScheme.GR_MATCH_ANYTHING,
			MatchingScheme.CN_MATCH_INSTANCE,
			MatchingScheme.RN_MATCH_ALL,
			MatchingScheme.CT_MATCH_ANYTHING,
			MatchingScheme.RT_MATCH_LABEL,
			MatchingScheme.QF_MATCH_ANYTHING,
			MatchingScheme.DG_MATCH_ANYTHING,
			MatchingScheme.MARKER_MATCH_ANYTHING,
			MatchingScheme.ARC_MATCH_INSTANCE,
			MatchingScheme.COREF_AUTOMATCH_ON,
			MatchingScheme.COREF_AGREE_OFF,
			MatchingScheme.FOLD_MATCH_OFF,
			MatchingScheme.CONN_MATCH_ON,
			1,
			null,
			null);

		relArr = getRelations();

		handled = new boolean[relArr.length];

		for (int rel = 0; rel < relArr.length; rel++)
			{
			if (!handled[rel])
				{
				handled[rel] = true;
			
				for (int other = rel + 1; other < relArr.length; other++)
					{
					if (!handled[other])
						if (Relation.matchRelations(relArr[rel], relArr[other], simplifyScheme))
							{
							removeRelation(relArr[other]);
							handled[other] = true;
							}
				  }
				}
			}
		}

  /* Graph Comparisons */
    /**
     * Compares two graphs to decide if they match.  The method will
     * return true if at least match can be made between the two graphs.
     * This method should be used when a simple yes or no answer is
     * required since it can avoid the overhead of constructing
     * MatchResult objects.  The exact semantics of matching
     * are determined by the matching scheme.
     *
     * @param first  the first graph being matched.
     * @param second  the second graph being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two graphs match according to the scheme's criteria.
     * @see notio.Graph#getGraphMatches
     */
/*  public static boolean isMatchingGraph(Graph first, Graph second,
    MatchingScheme matchingScheme)
    {
    switch (matchingScheme.getGraphFlag())
      {
      case MatchingScheme.GR_MATCH_INSTANCE:
      	return first == second;

      case MatchingScheme.GR_MATCH_COMPLETE:
      case MatchingScheme.GR_MATCH_SUBGRAPH:
      	{
        if (first == second)
        	return true;

        return getGraphMatches(first, second, 1, matchingScheme).matchFound();
				}

      case MatchingScheme.GR_MATCH_PROPER_SUBGRAPH:
        return getGraphMatches(first, second, 1, matchingScheme).matchFound();

      case MatchingScheme.GR_MATCH_ANYTHING:
      	return true;

      default:
        throw new UnimplementedFeatureException("Specified Graph match control flag is unknown.");
      }
    }
*/

    /**
     * Attempts to matches between the two specified graphs and returns the results in a 
     * MatchResult object.  Matching is governed by the specified MatchingScheme.
     *
     * @param first  the first graph being matched.
     * @param second  the second graph being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return the MatchResult object describing the details of the match.
     *
     * @bug What about MATCH_INSTANCE and null graphs?  Should use equals() instead of ==.
     */
  public static MatchResult matchGraphs(Graph first, Graph second, 
  	MatchingScheme matchingScheme)
    {
    switch (matchingScheme.getGraphFlag())
      {
      case MatchingScheme.GR_MATCH_ANYTHING:
      	{
     		return new MatchResult(true);
      	}
      	
      case MatchingScheme.GR_MATCH_INSTANCE:
      	{
     		return new MatchResult(first == second);
      	}

      case MatchingScheme.GR_MATCH_COMPLETE:
      	{
      	if (first == null || second == null)
      		return new MatchResult(first == second); // True if both are null

      	// Note, this is supposed to fall through to the next flag.
      	}
      	
      case MatchingScheme.GR_MATCH_SUBGRAPH:
      	{
      	if (first == null)
      		return new MatchResult(true);
      	
      	if (second == null)
      		return new MatchResult(false);

      	// Note, this is supposed to fall through to the next flag.
      	}
      	
      case MatchingScheme.GR_MATCH_PROPER_SUBGRAPH:
        {
        int maxMatches;
        NodeMappingGenerator gen;
        NodeMapping mapping, results[];
		    Vector matches;
        
        if (first == null)
        	if (second == null)
        		return new MatchResult(false);  // Not a proper subgraph
        	else
        		return new MatchResult(true);
        
        maxMatches = matchingScheme.getMaxMatches();
        
		    matches = new Vector(maxMatches);

        gen = new NodeMappingGenerator(first, second, matchingScheme);

        mapping = gen.getNextMapping();
				while ((mapping != null) && ((maxMatches == 0) || (matches.size() < maxMatches)))
        	{
          if (matchingScheme.getConnectedFlag() == MatchingScheme.CONN_MATCH_ON)
          	{
	          if (matchStructures(mapping))
  	        	matches.addElement(mapping);
  	        }
					else
						matches.addElement(mapping);

          mapping = gen.getNextMapping();
          }

        results = new NodeMapping[matches.size()];
        matches.copyInto(results);

        return new MatchResult(results);
        }

      default:
        throw new UnimplementedFeatureException("Specified Graph match control flag is unknown.");
      }
    }

    /**
     * Examines two graphs to determine if their structures match given the specified node
     * mapping.
     *
     * @param mapping  the node mapping used to compare the graphs.
     * @return true  if the graph structures match given the specifide node mapping.
     *
     * @bug Currently forces arcs to be in order.
     */
  private static boolean matchStructures(NodeMapping mapping)
    {
    Graph firstGraph, secondGraph;
    Concept firstConcepts[], secondConcepts[];
    Relation firstRelations[], secondRelations[];
    Actor firstActors[], secondActors[];
    Hashtable conceptHash = new Hashtable();
    Concept firstArgs[], secondArgs[];

    firstGraph = mapping.getFirstGraph();
    secondGraph = mapping.getSecondGraph();
    firstRelations = mapping.getFirstRelations();
    secondRelations = mapping.getSecondRelations();
    firstConcepts = mapping.getFirstConcepts();
    secondConcepts = mapping.getSecondConcepts();

    if ((firstRelations == null) && (secondRelations == null))
    	return true;

    for (int con = 0; con < firstConcepts.length; con++)
      conceptHash.put(firstConcepts[con], secondConcepts[con]);

    for (int rel = 0; rel < firstRelations.length; rel++)
      {
      firstArgs = firstRelations[rel].getArguments();
      secondArgs = secondRelations[rel].getArguments();
      for (int arg  = 0; arg < firstArgs.length; arg++)
        if (!conceptHash.get(firstArgs[arg]).equals(secondArgs[arg]))
          return false;
      }

    return true;
    }

    /**
     * For each concept the first graph, this method finds what concepts match it from the
     * second graph.  Returns an array of concepts from the second graph that match the
     * first graph's concepts.
     * @param first  the first graph from which concepts are to be matched.
     * @param second  the second graph from which concepts are to be matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return an array of matching concepts from the second graph.
     */
  public static Concept[] getMatchingConcepts(Graph first, Graph second, MatchingScheme matchingScheme)
    {
    Concept aConcepts[], bConcepts[];
    Concept aConcept, bConcept;

    int aNum, bNum;
    Set matchSet;
    Concept matchArr[];

    aConcepts = first.getConcepts();
    bConcepts = second.getConcepts();
		aNum = aConcepts.length;
		bNum = bConcepts.length;

    matchSet = new Set();

    for (int aCon = 0; aCon < aNum; aCon++)
    	{
    	aConcept = aConcepts[aCon];

    	for (int bCon = 0; bCon < bNum; bCon++)
    		{
    		bConcept = bConcepts[bCon];

    		if (Concept.matchConcepts(aConcept, bConcept, matchingScheme).matchSucceeded())
    			matchSet.addElement(bConcept);
    		}
    	}

    matchArr = new Concept[matchSet.size()];
    matchSet.copyInto(matchArr);

    return matchArr;
    }


    /**
     * For each relation the first graph, this method finds what relations match it from the
     * second graph.  Returns an array of relations from the second graph that match the
     * first graph's relations.
     * @param first  the first graph from which relations are to be matched.
     * @param second  the second graph from which relations are to be matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return an array of matching relations from the second graph.
     */
  public static Relation[] getMatchingRelations(Graph first, Graph second, MatchingScheme matchingScheme)
    {
    Relation aRelations[], bRelations[];
    Relation aRelation, bRelation;

    int aNum, bNum;
    Set matchSet;
    Relation matchArr[];

    aRelations = first.getRelations();
    bRelations = second.getRelations();
		aNum = aRelations.length;
		bNum = bRelations.length;

    matchSet = new Set();

    for (int aRel = 0; aRel < aNum; aRel++)
    	{
    	aRelation = aRelations[aRel];

    	for (int bRel = 0; bRel < bNum; bRel++)
    		{
    		bRelation = bRelations[bRel];

    		if (Relation.matchRelations(aRelation, bRelation, matchingScheme))
    			matchSet.addElement(bRelation);
    		}
    	}

    matchArr = new Relation[matchSet.size()];
    matchSet.copyInto(matchArr);

    return matchArr;
    }

  /* Comment Methods */

    /**
     * Adds a comment to this graph.  The comment has no strict
     * interpretation but may be used be applications or humans
     * to discover extra information about this graph.
     *
     * @param newComment  the comment to be added to this graph.
     */
  public void addComment(String newComment)
    {
    comments.addElement(newComment);
    }

    /**
     * Removes a comment from this graph.
     * Removes the first occurrence of the specified comment from
     * this graph.  If the comment is not part of this graph,
     * nothing happens.
     *
     * @param deadComment  the comment to be removed from this graph.
     */
  public void removeComment(String deadComment)
    {
    comments.removeElement(deadComment);
    }

    /**
     * Returns an array of all comments associated with this graph.
     *
     * @return  an array of Strings (possibly empty) that are all
     * the comments associated with this graph.
     */
  public String[] getComments()
    {
    String arr[] = new String[comments.size()];

    comments.copyInto(arr);
    return arr;
    }


    /*  Flag control methods */

		/**
		 * Sets a flag for this graph indicating whether or not incomplete
		 * relations and actors may be added to this graph.  An incomplete
		 * relation (or actor) is one which returns false when its
		 * isComplete() method is called.  Note that changing this flag
		 * from true to false does not guarantee that all relations/actors
		 * in the graph are complete.  The check is only performed when
		 * the node is added.  Use the Graph.isComplete() method to check
		 * whether a graph is complete.
		 *
		 * @param flag  the new setting for this flag.
		 *
		 * @see notio.Relation#isComplete
		 * @see notio.Actor#isComplete
		 * @see notio.Graph#isComplete
		 */
	public void setAllowIncompleteRelations(boolean flag)
		{
		allowIncompleteRelations = flag;
		}

		/**
		 * Returns the flag setting for this graph indicating whether or
		 * not incomplete relations and actors may be added to this graph.
		 *
		 * @return  the flag setting for this graph.
		 */
	public boolean getAllowIncompleteRelations()
		{
		return allowIncompleteRelations;
		}
  }
