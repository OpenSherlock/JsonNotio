package notio;

    /** 
     * A mapping between the nodes in two graphs.  The primary
     * purpose for this class is to provide the details of a 
     * match between two graphs.  It consists of a list of pairs of
     * the two types of nodes.  Each pair is a mapping.
     * Since there are some conditions that allow one-to-many mappings,
     * there may be duplicates amongst the first or second elements of 
     * the pairs, but no two pairings will be identical.
     *
     * For example: { a->B, a->C, b->B } is possible but { a->B, a->B } is not.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.13 $, $Date: 1999/05/04 01:35:56 $
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

public class NodeMapping
  {
  /** Graph being mapped from. **/
  private Graph firstGraph;
  /** Graph being mapped to. **/
  private Graph secondGraph;
  /** Concepts being mapped from. **/
  private Concept firstConcepts[];
  /** Concepts being mapped to. **/
  private Concept secondConcepts[];
  /** Relations being mapped from. **/
  private Relation firstRelations[];
  /** Relations being mapped to. **/
  private Relation secondRelations[];
  /** Match results for graphs nested in concepts. **/
  private MatchResult matchResults[];
  
    /**
     * Constructs a node mapping between the two graphs.
     *
     * @param newFirstGraph  the graph being mapped from.
     * @param newSecondGraph  the graph being mapped into.
     * @param newFirstConcepts  the array of concepts that form the first elements of the
     * concept pairs.
     * @param newSecondConcepts  the array of concepts that form the second elements of the
     * concept pairs.
     * @param newFirstRelations  the array of relations that form the first elements of the
     * relation pairs.
     * @param newSecondRelations  the array of relations that form the second elements of the
     * relation pairs.
     * @param newMatchResults  an array of match results from the matching
     * of concepts with nested graphs corresponding to the order of the 
     * concept pairs.
     */
  public NodeMapping(Graph newFirstGraph, Graph newSecondGraph, 
    Concept newFirstConcepts[], Concept newSecondConcepts[],
    Relation newFirstRelations[], Relation newSecondRelations[],
    MatchResult newMatchResults[])
    {
    
    firstGraph = newFirstGraph;
    secondGraph = newSecondGraph;
    
    if (newFirstConcepts != null)
    	{
	    firstConcepts = new Concept[newFirstConcepts.length];
  	  System.arraycopy(newFirstConcepts, 0, firstConcepts, 0, newFirstConcepts.length);
  	  }
  	  
    if (newSecondConcepts != null)
    	{
	    secondConcepts = new Concept[newSecondConcepts.length];
  	  System.arraycopy(newSecondConcepts, 0, secondConcepts, 0, newSecondConcepts.length);
  	  }

    if (newFirstRelations != null)
    	{  
	    firstRelations = new Relation[newFirstRelations.length];
  	  System.arraycopy(newFirstRelations, 0, firstRelations, 0, newFirstRelations.length);
  	  }
  	  
    if (newSecondRelations != null)
    	{  
	    secondRelations = new Relation[newSecondRelations.length];
	    System.arraycopy(newSecondRelations, 0, secondRelations, 0, newSecondRelations.length);
	    }
	    
	  if (newMatchResults != null)
	  	{
	    matchResults = new MatchResult[newMatchResults.length];
  	  System.arraycopy(newMatchResults, 0, matchResults, 0, newMatchResults.length);
  	  }
    }    
  
    /**
     * Returns the first graph involved in the mapping.
     *
     * @return the first graph involved in the mapping.
     */
  public Graph getFirstGraph()
    {
    return firstGraph;
    }
    
    /**
     * Returns the second graph involved in the mapping.
     *
     * @return the second graph involved in the mapping.
     */
  public Graph getSecondGraph()
    {
    return secondGraph;
    }

    /**
     * Returns an array that forms the first elements of the Concept pairs.
     *
     * @return an array of Concepts that map into the second array of Concepts.
     */
  public Concept[] getFirstConcepts()
    {
    if (firstConcepts == null)
    	return null;
    	
    Concept newArr[] = new Concept[firstConcepts.length];
    
    System.arraycopy(firstConcepts, 0, newArr, 0, firstConcepts.length);
    
    return newArr;
    }

    /**
     * Returns an array that forms the second elements of the Concept pairs.
     *
     * @return an array of Concepts that map into the first array of Concepts.
     */
  public Concept[] getSecondConcepts()
    {
    if (secondConcepts == null)
    	return null;
    	
    Concept newArr[] = new Concept[secondConcepts.length];
    
    System.arraycopy(secondConcepts, 0, newArr, 0, secondConcepts.length);
    
    return newArr;
    }

    /**
     * Returns an array that forms the first elements of the Relation pairs.
     *
     * @return an array of Relations that map into the second array of Relations.
     */
  public Relation[] getFirstRelations()
    {
    if (firstRelations == null)
    	return null;
    	
    Relation newArr[] = new Relation[firstRelations.length];
    
    System.arraycopy(firstRelations, 0, newArr, 0, firstRelations.length);
    
    return newArr;
    }

    /**
     * Returns an array that forms the second elements of the Relation pairs.
     *
     * @return an array of Relations that map into the first array of Relations.
     */
  public Relation[] getSecondRelations()
    {
    if (secondRelations == null)
    	return null;
    	
    Relation newArr[] = new Relation[secondRelations.length];
    
    System.arraycopy(secondRelations, 0, newArr, 0, secondRelations.length);
    
    return newArr;
    }

    /**
     * Returns an array of match results that come from matching the
     * graphs nested in concepts.  The order of the results corresponds
     * to the order of the concept pairs returned by getFirstConcepts()
     * and getSecondConcepts().  
     * If the a concept did not have a nested graph, or if the 
     * matching scheme did not require that the graphs be matched, the
     * corresponding match result will be null.
     * If no match results wree specified when this mapping was constructed,
     * this method will return null.
     *
     * @return an array of match results that correspond to the pairs of concepts.
     */
  public MatchResult[] getMatchResults()
    {
    if (matchResults == null)
    	return null;
    
    MatchResult newArr[] = new MatchResult[matchResults.length];
    
    System.arraycopy(matchResults, 0, newArr, 0, matchResults.length);
    
    return newArr;
    }
  }
