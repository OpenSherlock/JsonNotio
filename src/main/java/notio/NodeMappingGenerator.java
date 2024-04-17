package notio;

import java.util.*;

    /**
     * A mapping between the nodes in two graphs.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.38 $, $Date: 1999/05/04 01:35:56 $
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
     * @bug Needs to be upated to support actors and nested matches.
     * @bug Currently assumes that mappings of different node types can not affect each
     * other.  This may not be true if coreferent matching is used on compound graphs.
     * @bug Folding probably requires that folded nodes match.  The current method is
     * that they just have to match the same node in another graph but they should match
     * each other too, I think.
     * @bug Need to test folding.
     * @bug Need to add common subgraph stuff.
     * @bug Need to fix proper subgraph implementation and restrictions.
     * @bug Some types of relation matching might preclude the need for mapping related
     * concepts.  Unrelated concepts (unconnected) will have to be mapped, of course.
     */
class NodeMappingGenerator
  {
  /* ** Class Constants ** */

  /** Less than but no less than 1 comparator **/
  private static final int COMPARATOR_LT1 = -2;
  /** Less than comparator **/
  private static final int COMPARATOR_LT = -1;
  /** Equals comparator **/
  private static final int COMPARATOR_EQUAL = 0;
  /** Greater than comparator **/
  private static final int COMPARATOR_GT = 1;


	/* ** Constant values for this generator. ** */

  /** Flag indicating that concepts should be mapped. **/
  private boolean mapConcepts = true;
  /** Flag indicating that relations should be mapped. **/
  private boolean mapRelations = true;
  /** Flag indicating that we are performing subgraph matching. **/
  private boolean subgraph;
  /** Flag indicating that we are performing proper subgraph matching. **/
  private boolean properSubgraph;
  /** Flag indicating the generator is exhausted. **/
  private boolean noMoreMappings;
  /** Flag indicating that this generator will never generate any mappings. **/
  private boolean noPossibleMappings;
  /** First graph. **/
  private Graph firstGraph;
  /** Second graph. **/
  private Graph secondGraph;
  /** The matching scheme used for mapping. **/
  private MatchingScheme matchScheme;
  /** The concepts from the first graph. **/
  private Concept firstConcepts[];
  /** The concepts from the second graph. **/
  private Concept secondConcepts[];
  /** The relations from the first graph. **/
  private Relation firstRelations[];
  /** The relations from the second graph. **/
  private Relation secondRelations[];
  /** The number of concept in the first graph. **/
  private int numFirstConcepts;
  /** The number of concept in the second graph. **/
  private int numSecondConcepts;
  /** The number of relation in the first graph. **/
  private int numFirstRelations;
  /** The number of relation in the second graph. **/
  private int numSecondRelations;
  /** The matching concept pairs. **/
  private int conceptCandidates[][];
  /** The matching relation pairs. **/
  private int relationCandidates[][];
	/** The MatchResult associated with each of the concept pairs. **/
	private MatchResult conceptMatchResults[][];

  /** Flags indicating whether or not the first and second graphs may be folded. **/
  private boolean foldFirst, foldSecond;

	/** Flag indicating that we are do a fixed size mapping. **/
	boolean fixedMatching;

  /** Number of mapped first and second concepts to be compared in a fixed size mapping. **/
  private int fixedFirstConceptsRequired, fixedSecondConceptsRequired;
  /** Number of mapped first and second relations to be compared in a fixed size mapping. **/
  private int fixedFirstRelationsRequired, fixedSecondRelationsRequired;

	/** Comparison operation to be performed for the first mapping requirements. **/
	private int firstComparator;
	/** Comparison operation to be performed for the second mapping requirements. **/
	private int secondComparator;




  /* *** Current mapping state information *** */

	/** Number of concept mappings that have been generated since initialization or reset. */
	private int numConceptMappings;
	/** Number of relation mappings that have been generated since initialization or reset. */
	private int numRelationMappings;

	/** Current number of choices made in the concept mapping. **/
	private int numConceptChoices;
	/** Current number of choices made in the relation mapping. **/
	private int numRelationChoices;

	/** Current number of concept nodes mapped from first and second graphs. **/
	private int mappedFirstConcepts, mappedSecondConcepts;
	/** Current number of relation nodes mapped from first and second graphs. **/
	private int mappedFirstRelations, mappedSecondRelations;

	/** Usage counters for concept nodes. **/
	private int usedFirstConcepts[], usedSecondConcepts[];
	/** Usage counters for relation nodes. **/
	private int usedFirstRelations[], usedSecondRelations[];

	/** Pair index stacks for concepts. **/
	private int firstConceptPairIndices[], secondConceptPairIndices[];
	/** Pair index stacks for relations. **/
	private int firstRelationPairIndices[], secondRelationPairIndices[];

	/** Currently selected concept nodes for mapping from first to second. **/
	private Concept chosenFirstConcepts[], chosenSecondConcepts[];
	/** Currently selected relation nodes for mapping from first to second. **/
	private Relation chosenFirstRelations[], chosenSecondRelations[];

	/** MatchResults corresponding to selected concept pairs. **/
	private MatchResult chosenMatchResults[];
	
    /**
     * Constructs a generator of node mappings from the first graph to the second.
     *
     * @param newFirst  graph to be mapped from.
     * @param newSecond  graph to be mapped into.
     * @param matchingScheme  the matching scheme to be used in deciding mappings.
     * @bug Actor mapping flag is currently hardcoded out.
     */
	public NodeMappingGenerator(Graph newFirst, Graph newSecond, MatchingScheme matchingScheme)
		{
    this (newFirst, newSecond, matchingScheme, true, true);
    }

    /**
     * Constructs a generator of node mappings from the first graph to the second.
     *
     * @param newFirst  graph to be mapped from.
     * @param newSecond  graph to be mapped into.
     * @param matchingScheme  the matching scheme to be used in deciding mappings.
     * @param mapConceptNodes  flag to indicate whether the generator should map concepts.
     * @param mapRelationNodes  flag to indicate whether the generator should map relations.
     */
  public NodeMappingGenerator(Graph newFirst, Graph newSecond, MatchingScheme matchingScheme,
    boolean mapConceptNodes, boolean mapRelationNodes)
    {
    boolean foldingAllowed = false;
    boolean matchAllFirst = false;
    boolean matchAllSecond = false;

    mapConcepts = mapConceptNodes;
    mapRelations = mapRelationNodes;
    firstGraph = newFirst;
    secondGraph = newSecond;
    matchScheme = matchingScheme;
    firstConcepts = firstGraph.getConcepts();
    secondConcepts = secondGraph.getConcepts();
    firstRelations = firstGraph.getRelations();
    secondRelations = secondGraph.getRelations();

		numFirstConcepts = firstConcepts.length;
		numSecondConcepts = secondConcepts.length;
		numFirstRelations = firstRelations.length;
		numSecondRelations = secondRelations.length;

    foldingAllowed = matchScheme.getFoldingFlag() == MatchingScheme.FOLD_MATCH_ON;

    if (foldingAllowed)
    	{
    	foldFirst = true;
    	foldSecond = true;
    	}

    switch (matchingScheme.getGraphFlag())
      {
      case MatchingScheme.GR_MATCH_COMPLETE:
      	fixedMatching = true;
        subgraph = false;
        properSubgraph = false;
        matchAllFirst = true;
        matchAllSecond = true;

				firstComparator = COMPARATOR_EQUAL;
				secondComparator = COMPARATOR_EQUAL;
       	fixedFirstConceptsRequired = numFirstConcepts;
       	fixedSecondConceptsRequired = numSecondConcepts;
       	fixedFirstRelationsRequired = numFirstRelations;
       	fixedSecondRelationsRequired = numSecondRelations;
        break;

      case MatchingScheme.GR_MATCH_SUBGRAPH:
      	fixedMatching = true;
        subgraph = true;
        properSubgraph = false;
        matchAllFirst = true;
        matchAllSecond = false;
				firstComparator = COMPARATOR_EQUAL;
				secondComparator = COMPARATOR_GT;
       	fixedFirstConceptsRequired = numFirstConcepts;
       	fixedSecondConceptsRequired = 0;
       	fixedFirstRelationsRequired = numFirstRelations;
       	fixedSecondRelationsRequired = 0;
        break;

      case MatchingScheme.GR_MATCH_PROPER_SUBGRAPH:
      	fixedMatching = true;
        subgraph = true;
        properSubgraph = true;
        matchAllFirst = true;
        matchAllSecond = false;
				firstComparator = COMPARATOR_EQUAL;
				secondComparator = COMPARATOR_LT1;
       	fixedFirstConceptsRequired = numFirstConcepts;
       	fixedSecondConceptsRequired = numSecondConcepts;
       	fixedFirstRelationsRequired = numFirstRelations;
       	fixedSecondRelationsRequired = numSecondRelations;
        break;

      case MatchingScheme.GR_MATCH_INSTANCE:
        System.err.println("Graph instance matching should never need node mapping.");
        System.exit(1);

      default:
        throw new UnimplementedFeatureException("Specified Graph match control flag is unknown or not currently supported.");
      }

    // If we are doing fixed matching,
    // check to see whether the number of nodes in either graph precludes the possibility
    // of a match
    if (violatesSizeConstraints(subgraph, properSubgraph))
    	{
    	noMappings();
    	return;
    	}

		// If either graph lacks any of a given type of node, we should not try to map it.
		// We assume that if the type of matching requested is precluded by such a lack
		// that it has been caught earlier.
		if ((numFirstConcepts == 0) || (numSecondConcepts == 0))
			mapConcepts = false;

		if ((numFirstRelations == 0) || (numSecondRelations == 0))
			mapRelations = false;

    // If no node types are to be mapped, we will produce no mappings.
    if (!mapConcepts && !mapRelations)
    	{
    	noMappings();
    	return;
    	};

    // Find all candidate concept mappings from first to second
    if (mapConcepts)
      {
      // If we're not allowing subgraph matches, we set the matchAllFirst flag to true.
      conceptCandidates = findNodeMatches(firstConcepts, secondConcepts, matchScheme, 
      	matchAllFirst, matchAllSecond);

      if (conceptCandidates == null)
      	{
				noMappings();
				return;
				}

			initializeConceptMappings();
      }

    // Find all candidate relation mappings from first to second
    if (mapRelations)
      {
      // If we're not allowing subgraph matches, we set the matchAllFirst flag to true.
      relationCandidates = findNodeMatches(firstRelations, secondRelations, matchScheme,
      	matchAllFirst, matchAllSecond);

      if (relationCandidates == null)
      	{
				noMappings();
				return;
				}

			initializeRelationMappings();
      }

  // Complete match (no folding)
  	// One pair for each of first and second, no reusing nodes.
  // Complete match (folding)
  	// One pair for each of first and second, nodes may be reused.
  // Subgraph match (no folding)
  	// One pair for each of first, no reusing nodes.
  // Subgraph match (folding)
  	// One pair for each of first, reusing nodes.

  // Common subgraph (no folding)
  	// At least one pair of first, no reusing nodes.
  // Common subgraph (folding)
  	// At least one pair of first, reusing nodes.
	// Number of pairs = 1 to numFirst
		// Enumerate all combinations of N pairs

  // Maximal common subgraph (no folding)
  	// Maximum possible pairs of first, no reusing nodes.
  // Maximal common subgraph (folding)
  	// Maximum possible pairs of first, reusing nodes.

  // Connectedness?
    // Should probably clear secondConcepts and secondRelations here.

    }

		/**
		 * Returns false if the number of nodes in the graphs makes a mapping impossible.
		 *
		 * @param subgraph  a flag indicating whether subgraph matching is being performed.
		 * @param properSubgraph  a flag indicating whether proper subgraph matching is being performed.
		 * @return true if a mapping is possible, false if impossible.
		 * @bug Are the size constraints on subgraphs realistic?  The answer is no!
		 */
	private boolean violatesSizeConstraints(boolean subgraph, boolean properSubgraph)
		{
    // Subgraph and proper subgraph matches have size restrictions if the first graph
    // is not allowed to fold
    // (subgraph: numFirst <= numSecond  proper: numFirst < numSecond).
    if (subgraph && !foldFirst)
      {
      // Check that the second graph has at least one node of each type that the first has
      if ( ((numFirstConcepts > 0) && (numSecondConcepts == 0)) ||
	      ((numFirstRelations > 0) && (numSecondRelations == 0)))
      	return true;
	      	
      if (properSubgraph)
      	{
	    	// Check that the first graph is not equal to or larger than the second
	      if ((numFirstConcepts + numFirstRelations) >=
	      	(numSecondConcepts + numSecondRelations))
	        {
	        return true;
	        }
	      }
	    else
	    	{
	    	// Check that the first graph is not larger than the second
		    if ((numFirstConcepts + numFirstRelations) >
	      	(numSecondConcepts + numSecondRelations))
	  	    {
	        return true;
	        }
	      }
	    }
		else
			{
			// Complete graph matches have restrictions if neither fold or if the larger
			// graph is not allowed to fold
	  	if ((numFirstConcepts != numSecondConcepts) ||
      	(numFirstRelations != numSecondRelations))
      	if (!foldFirst && !foldSecond)
	        return true;
	     	else
	     		{
	     		if ((numFirstConcepts < numSecondConcepts) && !foldSecond)
	     			return true;

	     		if ((numFirstConcepts > numSecondConcepts) && !foldFirst)
	     			return true;

	     		if ((numFirstRelations < numSecondRelations) && !foldSecond)
	     			return true;

	     		if ((numFirstRelations > numSecondRelations) && !foldFirst)
	     			return true;
	     		}
			}

		return false;
		}

    /**
     * Returns a table in which, for each of the first nodes, the matching second nodes
     * indices are listed.  If the matchAllFirst flag is true, a match must be found for
     * all of the first nodes or this method will return null.
     *
     * @param firstNodes the array of nodes being mapped from.
     * @param secondNodes  the array of Nodes being mapped into.
     * @param matchScheme  the matching scheme to be used in deciding mappings.
     * @param matchAllFirst  requires that all concepts in the first array be matched or
     * this method returns null.
     * @param matchAllSecond  requires that all concepts in the second array be matched or
     * this method returns null.
     * @return the desired table or null if the matchAllFirst flag is true and matches could
     * not be found for all of the concepts in the first array.
     */
	private int[][] findNodeMatches(Node firstNodes[], Node secondNodes[],
		MatchingScheme matchScheme, boolean matchAllFirst, boolean matchAllSecond)
		{
		int numFirst, numSecond;
		Node firstNode, secondNode;
		int candidates[];
		int candidateTable[][];
		int numCand;
		boolean usedSecond[] = null;
		// These are used only for concept matches
		MatchResult matchResult;
		MatchResult matchResults[] = null, matchResultTable[][] = null;

		numFirst = firstNodes.length;
		numSecond = secondNodes.length;

		candidateTable = new int[numFirst][];
    candidates = new int[numSecond];

		// Initialize these if we are dealing with concepts
		if (firstNodes instanceof Concept[])
			{
	    matchResultTable = new MatchResult[numFirst][];
  	  matchResults = new MatchResult[numSecond];
  	  }

		if (matchAllSecond)
			usedSecond = new boolean[numSecond];

		for (int fnode = 0; fnode < numFirst; fnode++)
			{
			firstNode = firstNodes[fnode];
			numCand = 0;

			for (int snode = 0; snode < numSecond; snode++)
				{
				secondNode = secondNodes[snode];

				// Comparison for Concepts
				if (firstNode instanceof Concept)
					{
					matchResult = Concept.matchConcepts((Concept)firstNode, (Concept)secondNode, 
						matchScheme);
						
					if (matchResult.matchSucceeded())
						{
						candidates[numCand] = snode;
						
						// Must associate this MatchResult with the pair if it contains node mappings.
						if (matchResult.getMappings() == null)
							matchResults[numCand] = null;
						else
							matchResults[numCand] = matchResult;
						
						numCand++;

						if (matchAllSecond)
							usedSecond[snode] = true;													
						}
					}
					
				// Comparison for Relations
				if (firstNode instanceof Relation)
					if (Relation.matchRelations((Relation)firstNode, (Relation)secondNode, matchScheme))
						{
						candidates[numCand] = snode;
						numCand++;

						if (matchAllSecond)
							usedSecond[snode] = true;
						}
				}

			if (matchAllFirst && (numCand == 0))
				return null;

			candidateTable[fnode] = new int[numCand];
			System.arraycopy(candidates, 0, candidateTable[fnode], 0, numCand);
			
			// Add matchResultTable entry, if appropriate
			if (matchResults != null)
				{
				matchResultTable[fnode] = new MatchResult[numCand];
				System.arraycopy(matchResults, 0, matchResultTable[fnode], 0, numCand);
				}
			}

		// Check to see if we found a match all nodes in the second list (if required)
		if (matchAllSecond)
			for (int used = 0; used < numSecond; used++)
				if (!usedSecond[used])
					return null;

		// Since we can't return the matchResultTable (if there is one) easily,
		// we'll just hackishly assign it to a global var.  Eech.
		if (firstNodes instanceof Concept[])
			conceptMatchResults = matchResultTable;

		return candidateTable;
		}

    /**
     * Returns the next mapping from this generator or null if no more mappings exist.
     * This method is used for generating fixed size matches (complete or subgraph matches).
     *
     * @return true if a mapping was found, false if the mappings are exhausted.
     * @bug This method could be rewritten to store mappings so they are not regenerated
     * while creating permutations of different node type mappings.
     */
	public boolean getNextFixedMapping()
		{
    NodeMapping newMapping;

		// Case where we map both concepts and relations
		if (mapConcepts && mapRelations)
			{
			// Relation primer.  If no relation mappings have been generated, generate the first
			if (numRelationMappings == 0)
				{
	      if (!nextRelationMapping(fixedFirstRelationsRequired, fixedSecondRelationsRequired))
	      	{
	      	// If we fail to find a relation mapping at this point, then no mapping is
	      	// possible
	      	noMappings();
					return false;
					}
				}

			// Since a relation mapping is already in place, try to generate a concept mapping.
			while (!nextConceptMapping(fixedFirstConceptsRequired, fixedSecondConceptsRequired))
				{
				// If we failed to find a concept mapping and no previous mappings were found,
				// then no mapping is possible
				if (numConceptMappings == 0)
					{
	      	noMappings();
					return false;
					}

				// We have exhausted the concept mappings so we'll try to find a new relation
				// mapping.
	      if (!nextRelationMapping(fixedFirstRelationsRequired, fixedSecondRelationsRequired))
	      	{
	      	// If we fail to find a relation mapping at this point, then we have exhausted
	      	// all possible mappings.  The noMoreMappings flag will be set by the calling
	      	// method
					return false;
					}
				else
					{
					// We have found a new relation mapping so we must reset the concept mappings
					// so they start from the beginning
					resetConceptMappings();
					}
				}

			return true;
			}

		// Map concepts only
    if (mapConcepts && !mapRelations)
    	{
			if (!nextConceptMapping(fixedFirstConceptsRequired, fixedSecondConceptsRequired))
				{
				// If we failed to find a mapping and no previous mappings were found
				// then no mappings are possible.
				if (numConceptMappings == 0)
					{
					noMappings();
					}
				return false;
        }
			return true;
      }

		// Map relations only
    if (!mapConcepts && mapRelations)
    	{
			if (!nextRelationMapping(fixedFirstRelationsRequired, fixedSecondRelationsRequired))
				{
				// If we failed to find a mapping and no previous mappings were found
				// then no mappings are possible.
				if (numRelationMappings == 0)
					{
					noMappings();
					}
				return false;
        }
			return true;
      }

    throw new UnimplementedFeatureException("Requested node mappings are not currently supported.");
		}

    /**
     * Returns the next mapping from this generator or null if no more mappings exist.
     *
     * @return the next mapping from this generator or null if no more mappings exist.
     */
  public NodeMapping getNextMapping()
    {
    NodeMapping newMapping;

		// Check to see if at some point we have determined that no mappings are available.
    if (noMoreMappings)
      return null;

		if (fixedMatching)
			{
			if (!getNextFixedMapping())
				{
				noMoreMappings = true;
				return null;
				}
			}
		else
			{
			}

    // If we reached here, we found a mapping so we create the NodeMapping instance
    newMapping = new NodeMapping(firstGraph, secondGraph,
    	chosenFirstConcepts, chosenSecondConcepts,
    	chosenFirstRelations, chosenSecondRelations,
			chosenMatchResults);

    return newMapping;
    }

    /**
     * Returns an array containing all remaining ungenerated mappings.  If none remain, the
     * array will be empty.
     *
     * @return a (possibly empty) array containing the remaining ungenerated mappings.
     */
  public NodeMapping[] getRemainingMappings()
    {
    Vector mappings = new Vector();
    NodeMapping map, arr[];

    map = getNextMapping();
    while (map != null)
      {
      mappings.addElement(map);
      map = getNextMapping();
      }

    arr = new NodeMapping[mappings.size()];
    mappings.copyInto(arr);

    return arr;
    }

			/**
	  	 * Attempts to generate the next concept mapping.
  		 *
  		 * @param requiredFirstConcepts  the number of first concepts that must be mapped.
	  	 * @param requiredSecondConcepts  the number of second concepts that must be mapped.
  		 * @return true if a valid mapping was generated, false if all mappings have been exhausted.
  		 */
  	public boolean nextConceptMapping(int requiredFirstConcepts, int requiredSecondConcepts)
  		{
  		// An array used to pass numConceptChoices, mappedFirstConcepts, and mappedSecondConcepts.
  		int counters[] = new int[3];
  		// Return value of nextNodeMapping().
  		boolean returnValue;

  		// Copy values into counters array
  		counters[0] = numConceptChoices;
  		counters[1] = mappedFirstConcepts;
  		counters[2] = mappedSecondConcepts;

  		returnValue = nextNodeMapping(firstConcepts, secondConcepts,
  			conceptCandidates,
  			firstConceptPairIndices, secondConceptPairIndices,
  			counters,
  			numConceptMappings,
  			usedFirstConcepts, usedSecondConcepts,
  			chosenFirstConcepts, chosenSecondConcepts,
  			requiredFirstConcepts, requiredSecondConcepts,
  			foldFirst, foldSecond);

	 		// Copy values from counters array
  		numConceptChoices = counters[0];
  		mappedFirstConcepts = counters[1];
  		mappedSecondConcepts = counters[2];

			if (returnValue)
				numConceptMappings++;

			return returnValue;
  		}


			/**
	  	 * Attempts to generate the next relation mapping.
  		 *
  		 * @param requiredFirstRelations  the number of first relations that must be mapped.
	  	 * @param requiredSecondRelations  the number of second relations that must be mapped.
  		 * @return true if a valid mapping was generated, false if all mappings have been exhausted.
  		 */
  	public boolean nextRelationMapping(int requiredFirstRelations, int requiredSecondRelations)
  		{
  		// An array used to pass numRelationChoices, mappedFirstRelations, and mappedSecondRelations.
  		int counters[] = new int[3];
  		// Return value of nextNodeMapping().
  		boolean returnValue;

  		// Copy values into counters array
  		counters[0] = numRelationChoices;
  		counters[1] = mappedFirstRelations;
  		counters[2] = mappedSecondRelations;

  		returnValue = nextNodeMapping(firstRelations, secondRelations,
  			relationCandidates,
  			firstRelationPairIndices, secondRelationPairIndices,
  			counters,
  			numRelationMappings,
  			usedFirstRelations, usedSecondRelations,
  			chosenFirstRelations, chosenSecondRelations,
  			requiredFirstRelations, requiredSecondRelations,
  			foldFirst, foldSecond);

	 		// Copy values from counters array
  		numRelationChoices = counters[0];
  		mappedFirstRelations = counters[1];
  		mappedSecondRelations = counters[2];

			if (returnValue)
				numRelationMappings++;

			return returnValue;
  		}

		/**
		 * Attempts to generate the next node mapping.
		 *
		 * @param nodesA  an array containing the nodes from A.
		 * @param nodesB  an array containing the nodes from B.
		 * @param pairs  a table with a list of indices into B for each A that form valid mappings.
		 * @param pairIndicesA  a stack of A indices into the pairs array for each choice point.
		 * @param pairIndicesB  a stack of B indices into the pairs array for each choice point.
		 * @param counters  an array of three integers: the choice point counter, the number of
		 * nodes mapped from A, the number of nodes mapped from B.
		 * @param numMappings  the number of mappings that have been generated thus far.
		 * @param usedA  an array of counters indicating the number of times the corresponding
		 * element of nodesA has been mapped.
		 * @param usedB  an array of counters indicating the number of times the corresponding
		 * element of nodesB has been mapped.
		 * @param chosenNodesA  an array containing the currently mapped nodes from A.
		 * @param chosenNodesB  an array containing the currently mapped nodes from B.
		 * @param requiredA  the number of nodes from A that must be mapped into B.
		 * @param requiredB  the number of nodes from B that must be mapped into A.
		 * @param foldA  a flag indicating whether nodes from A may be folded.
		 * @param foldB  a flag indicating whether nodes from B may be folded.
		 * @return true if another mapping was generated, false if no more remain.
		 * @bug Requirement method doesn't work for proper subgraphs since we are allowed to
		 * completely use 2 out of the 3 types of nodes.  Probably have to use some higher
		 * level control.
		 */
  private boolean nextNodeMapping(Node nodesA[], Node nodesB[],
  	int pairs[][],
  	int pairIndicesA[], int pairIndicesB[],
  	int counters[],
  	int numMappings,
  	int usedA[], int usedB[],
		Node chosenNodesA[], Node chosenNodesB[],
  	int requiredA, int requiredB,
  	boolean foldA, boolean foldB)
    {
    // Number of nodes in A and B
    int numA = nodesA.length, numB = nodesB.length;
    // Flag indicating whether we have found a node for the current choicepoint.
    boolean choiceSatisfied;
    // Flag indicating whether we have exhausted the current choicepoint.
    boolean choicesExhausted;
    // Flag indicating we have not mapped the required number of A and B nodes.
    boolean mappingSatisfied;
    // Flag indicating that we have exhausted all possible mappings, given the requirements.
    boolean mappingsExhausted;
    // Temporary variables for quick access to the pair indices.
    int pairIndexA, pairIndexB;
    // The current number of choices that have been made (the choicepoint counter)
    // (taken from counters array)
		int numChoices;
    // Number of nodes currently mapped from A into B and from B into A (taken from counters).
    int mappedA, mappedB;

		// Set local variables from counters array.
		numChoices = counters[0];
		mappedA = counters[1];
		mappedB = counters[2];

    // Initialize pairIndexA and pairIndexB
    // If we haven't made any choices, then we start with the first pair.
    // Otherwise, we grab the last choice point that gave us a successful mapping
    // which has been popped already but is still in the array.
    if (numMappings == 0)
    	{
    	pairIndexA = 0;
    	pairIndexB = -1;
    	}
    else
    	{
	    pairIndexA = pairIndicesA[numChoices];
  	  pairIndexB = pairIndicesB[numChoices];
  	  }

		// FOR REFERENCE: GENERAL PRINCIPLE OF FOLDING
		// IF A is allowed to fold, then nodes in B may be reused.

		// ** Point Loop **
		// Loop through, popping and pushing choice points until the mapping is complete
		// or no more choice points exist

		// Initialize point loop termination flags
		mappingSatisfied = false;
		mappingsExhausted = false;

		do
			{
			// Temporary variables used to make the choice loop easier to read
	   	int choiceA = -1;
	   	int choiceB = -1;

			// ** Choice Loop **
			// Loop through choices at current choice point until we have found a valid choice
			// or run out of choices.

			// Initialize choice loop flags
			choiceSatisfied = false;
			choicesExhausted = false;

			do
	    	{

  	  	// Increment B pair index
				pairIndexB++;

  	  	// Determine if we have run out of B selections at the current A pair index
  	  	if (pairIndexB == pairs[pairIndexA].length)
  	  		{
					// If we are required to map every node of A, and we haven't mapped A to anything
					// yet, then we must backtrack
					if (requiredA == numA)
						{
						// We must map every node of A to one in B
						// Check to see if we have already mapped A by looking in usedA
						if (usedA[pairIndexA] == 0)
							choicesExhausted = true;
						}
					else
						{
	  	  		// Since we are not required to map every node of A, we can simply advance the
	  	  		// A pair index and set the B pair index to -1.
  		  		pairIndexA++;
  		  		pairIndexB = -1;

  	  			// If we have reached the end of all pairs, we have run out of choices for this
	  	  		// choice point and must backtrack.
  		  		if (pairIndexA == numA)
  		  			choicesExhausted = true;
  	 	  		}
  	  		}

				if (!choicesExhausted)
  	  		{
  	  		// Store indices from A and B for easy access
					choiceA = pairIndexA;
					choiceB = pairs[pairIndexA][pairIndexB];

  	  		// Choices were not exhausted so now we check to see if this choice is valid
  	  		// Start by assuming it is valid and then contradict if necessary
  	  		choiceSatisfied = true;

  	  		// If folding of A is not allowed then choiceB cannot have been used before this
  	  		if (!foldA)
  	  			if (usedB[choiceB] > 0)
  	  				choiceSatisfied = false;

  	  		// If folding of B is not allowed then choiceA cannot have been used before this
  	  		// WE CAN PROBABLY SKIP THIS SINCE WE CAN ENSURE THAT an A pair index is never reused trivially
  	  		if (!foldB)
  	  			if (usedA[choiceA] > 0)
  	  				choiceSatisfied = false;
  	  		}

  	  	// For some cases, one of the choices is nothing but if
  	  	// at some point we can stop since there won't be enough choices
  	  	// left to satisfy the requirements.
    		}
    	while (!choiceSatisfied && !choicesExhausted);

			if (choiceSatisfied)
				{
				// Push a choice point (advance)

				// Push the current pair indices on to a stack
				pairIndicesA[numChoices] = pairIndexA;
				pairIndicesB[numChoices] = pairIndexB;

				// Add chosen nodes to arrays (optional but means that when we are done we have
				// the node arrays ready)
				chosenNodesA[numChoices] = nodesA[choiceA];
				chosenNodesB[numChoices] = nodesB[choiceB];

				// Nasty hack to make sure that the MatchResult corresponding to a concept pair
				// is available.  Could build these out of the pairIndices elsewhere and probably
				// should.
				if (nodesA instanceof Concept[])
					{
					chosenMatchResults[numChoices] = conceptMatchResults[pairIndexA][pairIndexB];
					}

				// Increment choice point counter
				numChoices++;

				// If we are not folding B, we can automatically advance the pairIndexA
				// (means we don't need to check if A nodes are being reused - whoopee!)
				if (!foldB)
					{
					pairIndexA++;
					pairIndexB = -1;
					}

				// Increment A's usage count and mappedA (if this is the first time we've mapped A)
				usedA[choiceA]++;
				if (usedA[choiceA] == 1)
					mappedA++;

				// Increment B's usage count and mappedB (if this is the first time we've mapped B)
				usedB[choiceB]++;
				if (usedB[choiceB] == 1)
					mappedB++;

				// Check whether we have reached the requirements for this mapping and
				// set mappingSatisfied correspondingly

				// Assume that the mapping is satisfied and contradict if necessary.
				mappingSatisfied = true;

				// Test requirements on A
				if (!doComparison(firstComparator, mappedA, requiredA))
					mappingSatisfied = false;

				// Test requirements on B
				if (!doComparison(secondComparator, mappedB, requiredB))
					mappingSatisfied = false;
				}

			// We have either exhausted all choices, or advanced and possibly generated a valid
			// mapping.
			// If we have been satisfied the mapping, we backtrack to prepares for the next mapping.
			// If choices are exhausted, we backtrack to prepares for the next iteration of the loop (if any).
			// Otherwise, we simply start working on the new choice point.

			// If no choices have been made at this point and we are trying to backtrack
			// then we have exhausted all possible mappings, so we don't perform the backtracking
			// steps and we set the mappingsExhausted.
			if (numChoices == 0)
				{
				mappingsExhausted = true;
				}
			else
				{
				if (!choiceSatisfied || mappingSatisfied)
					{
					// BACKTRACKING START
					// Pop a choice point (backtrack)

					// Decrement the choicepoint counter
					numChoices--;

					// Pop pair indices off stack
					pairIndexA = pairIndicesA[numChoices];
					pairIndexB = pairIndicesB[numChoices];

	 	  		// Store indices from A and B for easy access (not needed later but makes the next
 		  		// couple of steps easier to read)
					choiceA = pairIndexA;
					choiceB = pairs[pairIndexA][pairIndexB];

					// Decrement A's usage counter and mappedA (if A is no longer mapped)
					usedA[choiceA]--;
					if (usedA[choiceA] == 0)
						mappedA--;

					// Decrement B's usage counter and mappedB (if B is no longer mapped)
					usedB[choiceB]--;
					if (usedB[choiceB] == 0)
						mappedB--;

					// BACKTRACKING COMPLETE
					}
				}
    	}
		while (!mappingSatisfied && !mappingsExhausted);


		// Set counters array from local variables so the values are returned when we exit.
		counters[0] = numChoices;
		counters[1] = mappedA;
		counters[2] = mappedB;

		// No more mappings are left so we return false
		if (mappingsExhausted)
	  	return false;

	  // The mapping is now complete
	  // We have already backtracked to prepare for the next mapping
	  // The calling function can grab the result from the chosenNodesX arrays.

	  return true;
    }


		/**
		 * Performs a comparison between two integers according to the specified operator.
		 *
		 * @param comparator  the type of comparison.
		 * @param value  the value being compared.
		 * @param target  the target value.
		 * @return true if the comparison holds.
		 */
	private boolean doComparison(int comparator, int value, int target)
 		{
		switch (comparator)
			{
			case COMPARATOR_LT1:
				{
				if ((value < 1) || (value >= target))
					return false;
				break;
				}

			case COMPARATOR_LT:
				{
				if (value >= target)
					return false;
				break;
				}

			case COMPARATOR_EQUAL:
				{
				if (value != target)
					return false;
				break;
				}

			case COMPARATOR_GT:
				{
				if (value <= target)
					return false;
				break;
				}
			}

		return true;
	  }


		/**
		 * Initializes all state variables for the mapping.
		 */
	public void initializeConceptMappings()
  	{
  	// Set the number of generated mappings to zero
  	numConceptMappings = 0;

  	// Number of A and B nodes.
  	int numA, numB;
  	// The maximum number of choice points that may be required.
		int maxChoices = 0;

  	numA = numFirstConcepts;
  	numB = numSecondConcepts;

  	// Initialize node usage counters.
  	usedFirstConcepts = new int[numA];
  	usedSecondConcepts = new int[numB];

		// Determine the maximum number of possible choices that need to be made
		// Should probably be done in the calling function.
		if (!foldFirst && !foldSecond)
			// With no folding, we can't map any node to more than one other node
			maxChoices = Math.min(numA, numB);
		if (foldFirst && foldSecond)
			// With A and B folding, we can conceivably map every node to every node
			maxChoices = numA * numB;
		if (foldFirst && !foldSecond)
			// With only A folding, the most choices we can make are one for each node in A
			maxChoices = numA;
		if (!foldFirst && foldSecond)
			// With only B folding, the most choices we can make are one for each node in B
			maxChoices = numB;

  	// Initialize pair indices stacks.
  	firstConceptPairIndices = new int[maxChoices];
  	secondConceptPairIndices = new int[maxChoices];

  	// Initialize chosen node arrays
  	chosenFirstConcepts = new Concept[maxChoices];
  	chosenSecondConcepts = new Concept[maxChoices];

		// Initialize MatchResult corresponding to chosen pairs array
		chosenMatchResults = new MatchResult[maxChoices];

  	// Initialize choice point counter
  	numConceptChoices = 0;

  	// Initialize mapped node counters
  	mappedFirstConcepts = 0;
  	mappedSecondConcepts = 0;
  	}

		/**
		 * Resets state variables so the mappings will resume at the beginning.
		 */
  public void resetConceptMappings()
  	{
  	// Set the number of generated mappings to zero
  	numConceptMappings = 0;

  	// Reset used node counts
  	for (int node = 0; node < numFirstConcepts; node++)
  		usedFirstConcepts[node] = 0;

  	for (int node = 0; node < numSecondConcepts; node++)
  		usedSecondConcepts[node] = 0;

  	// Reset mapped node counters
  	mappedFirstConcepts = 0;
  	mappedSecondConcepts = 0;

  	// Reset choice point counter
  	numConceptChoices = 0;
  	}

 		/**
		 * Initializes all state variables for the mapping.
		 */
	public void initializeRelationMappings()
  	{
  	// Set the number of generated mappings to zero
  	numRelationMappings = 0;

  	// Number of A and B nodes.
  	int numA, numB;
  	// The maximum number of choice points that may be required.
		int maxChoices = 0;

  	numA = numFirstRelations;
  	numB = numSecondRelations;

  	// Initialize node usage counters.
  	usedFirstRelations = new int[numA];
  	usedSecondRelations = new int[numB];

		// Determine the maximum number of possible choices that need to be made
		// Should probably be done in the calling function.
		if (!foldFirst && !foldSecond)
			// With no folding, we can't map any node to more than one other node
			maxChoices = Math.min(numA, numB);
		if (foldFirst && foldSecond)
			// With A and B folding, we can conceivably map every node to every node
			maxChoices = numA * numB;
		if (foldFirst && !foldSecond)
			// With only A folding, the most choices we can make are one for each node in A
			maxChoices = numA;
		if (!foldFirst && foldSecond)
			// With only B folding, the most choices we can make are one for each node in B
			maxChoices = numB;

  	// Initialize pair indices stacks.
  	firstRelationPairIndices = new int[maxChoices];
  	secondRelationPairIndices = new int[maxChoices];

  	// Initialize chosen node arrays
  	chosenFirstRelations = new Relation[maxChoices];
  	chosenSecondRelations = new Relation[maxChoices];

  	// Initialize choice point counter
  	numRelationChoices = 0;

  	// Initialize mapped node counters
  	mappedFirstRelations = 0;
  	mappedSecondRelations = 0;
  	}

		/**
		 * Resets state variables so the mappings will resume at the beginning.
		 */
  public void resetRelationMappings()
  	{
  	// Set the number of generated mappings to zero
  	numRelationMappings = 0;

  	// Reset used node counts
  	for (int node = 0; node < numFirstRelations; node++)
  		usedFirstRelations[node] = 0;

  	for (int node = 0; node < numSecondRelations; node++)
  		usedSecondRelations[node] = 0;

  	// Reset mapped node counters
  	mappedFirstRelations = 0;
  	mappedSecondRelations = 0;

  	// Reset choice point counter
  	numRelationChoices = 0;
  	}

    /**
     * Resets the generator so that it will produce mappings from the beginning.
     */
  public void resetGenerator()
    {
  	// Can't reset the generator if there were never any possible mappings

  	if (noPossibleMappings)
  		return;

    if (mapConcepts)
			resetConceptMappings();

    if (mapRelations)
			resetRelationMappings();
    }

    /**
     * Called whenever it is determined that there are no valid mappings so we can free
     * up resources and set flags.
     */
  private void noMappings()
    {
    // Set flags to indicate that there are no mappings
    noPossibleMappings = true;
    noMoreMappings = true;

    // Allow resources to be garbage collected since we will never use them.
    firstGraph = null;
    secondGraph = null;

    firstConcepts = null;
    secondConcepts = null;

    firstRelations = null;
    secondRelations = null;

    conceptCandidates = null;
    relationCandidates = null;

    usedFirstConcepts = null;
    usedSecondConcepts = null;
    usedFirstRelations = null;
    usedSecondRelations = null;

    firstConceptPairIndices = null;
    secondConceptPairIndices = null;
    firstRelationPairIndices = null;
    secondRelationPairIndices = null;

    chosenFirstConcepts = null;
    chosenSecondConcepts = null;
    chosenMatchResults = null;
    chosenFirstRelations = null;
    chosenSecondRelations = null;
    }
  }
