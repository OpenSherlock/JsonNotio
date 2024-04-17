package notio;

import java.util.*;
import java.io.Serializable;

    /** 
     * Class to implement coreference sets (also known as lines of identity).
     * Note that it is necessary to add at least one valid dominant concept to this
     * set before adding subordinate concepts that may belong to other coreference sets.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.31 $, $Date: 1999/09/10 03:57:29 $
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
     * @idea Consider adding method for adding concepts 'out of order' with respect to
     * dominant/subordinate issues.
     * @idea Should probably remove defining label stuff from this class in the name of 
     * "purity".  Strictly speaking, defining labels are not part of CG's (the abstract 
     * syntax) and are purely a feature of notations (e.g. CGIF or LF).
     */
public class CoreferenceSet implements Serializable
  {
	  /** Set of coreferent concepts. **/
  private Set corefSet = new Set();
  
  	/** The defining concept of the set. **/
  private Concept definingConcept;
  
  	/** The defining label of the set included as an aid to translators. **/
  private String definingLabel;
  
  	/** The graph containing the dominant nodes. **/
  private Graph dominantGraph;
  
  	/** A flag indicating whether or not coreference scope is checked. **/
  private boolean checkScope = false;

    /**
     * Constructs a new, empty coreference set.
     */
  public CoreferenceSet()
    {
    }

    /**
     * Constructs a new, empty coreference set with the specified defining label.
     * The defining label is not an essential part of the coreference set and
     * is not required (it can be null).  
     * It is included to make parsing and generating easier.
     *
     * @param newDefLabel  the defining label for this coreference set.
     */
  public CoreferenceSet(String newDefLabel)
    {
    definingLabel = newDefLabel;
    }

    /**
     * Sets the defining label associated with this coreference set.  No check
     * is made to see if this label is in use by any other set so incautious use of
     * this method may confuse translators.  There is really little reason to
     * change the coreference label of a set once established but the method
     * is included for the sake of completeness.
     *
     * @param newDefLabel  the defining label to be associated with this coreference set.
     */
  public void setDefiningLabel(String newDefLabel)
    {
    definingLabel = newDefLabel;
    }

    /**
     * Sets the defining concept associated with this coreference set.  This
     * is the concept directly associated with the defining label.  The
     * primary use for this feature is related to parsing and generating,
     * particulary as regards constructing arcs for relations.  The specified
     * concept must already be a dominant member of the coreference set.
     *
     * @param newDefConcept  the defining concept to be associated with this coreference set.
     * @exception InvalidDefiningConceptException  if the specified concept is not a dominant
     * concept in this set.
     */
  public void setDefiningConcept(Concept newDefConcept) throws InvalidDefiningConceptException
    {
    if (checkScope)
	    if (!hasDominantConcept(newDefConcept))
  	  	throw new InvalidDefiningConceptException("The specified concept is not a dominant concept in this coreference set.");
    	
    definingConcept = newDefConcept;
    }

    /**
     * Returns the defining label associated with this coreference set or null
     * if none was specified.
     *
     * @return the defining label associated with this coreference set.
     */
  public String getDefiningLabel()
    {
    return definingLabel;
    }

    /**
     * Returns the defining concept associated with this coreference set or null
     * if none was specified.
     *
     * @return the defining concept associated with this coreference set.
     */
  public Concept getDefiningConcept()
    {
    return definingConcept;
    }

    /**
     * Adds a concept to this coreference set.  
     *
     * @param newConcept  the concept to be added to this coref set.
     * @exception CorefAddException  if the specified concept is dominant in another
     * coreference set,
     * or if the specified concept would be dominant in this set but is a member of other 
     * sets,
     * or if the specified concept is not in the correct scope for this set,
     * or if the specified concept is not enclosed by any graph.
     */
  public void addCoreferentConcept(Concept newConcept) throws CorefAddException
    {
    CoreferenceSet corefSetArr[];
    boolean isDominant = false;

		if (checkScope)
			{
			// If the new concept doesn't have an enclosing graph than it can't form the basis
			// for a proper coreference set (defined to be enclosed by some graph, g).
			if (newConcept.getEnclosingGraph() == null)
				throw new CorefAddException("Concept does not have an enclosing graph.");

	    isDominant = false;

			// Check to see if this concept belongs in this coreference set and if it
			// would be dominant.
			if (dominantGraph != null)
				{
				if (newConcept.isEnclosedBy(dominantGraph))
					{
					// Check if this concept is enclosed directly be the dominant graph, in which case
					// it would be a dominant node of this set.
					if (newConcept.getEnclosingGraph() == dominantGraph)
						isDominant = true;
					}
				else
					{
					// This concept is not enclosed by the dominant graph, so it is either defines
					// a new level of dominance or is not a legal member of this coref set.
					if (dominantGraph.isEnclosedBy(newConcept))
						{
						// This concept encloses the currently dominant graph and thus defines a new
						// level of dominance.
						isDominant = true;
						}
					else
						{
						// This concept does not enclose the existing set members, nor is it enclosed by
						// them.  Therefore it is not a legal member of this set.  Throw an exception.
						throw new CorefAddException("Specified concept is not in the correct scope for this set.");
						}
					}
				}
			else
				isDominant = true;


			// If we have determined that this node would be a dominant member of this set, we
			// must check to see if it belongs to any other coreference sets before we allow
			// it to be added.  If not dominant in this set, we must ensure that is not dominant
			// in another.

			corefSetArr = newConcept.getCoreferenceSets();
			
			if (isDominant)
				{
				// Check to see if the concept belongs to any other coreference sets
				if ((corefSetArr != null) && (corefSetArr.length > 0))
					{
					// Dominant concepts may not belong to more than one coreference set.
					// Throw an exception expressive of our displeasure.
					throw new CorefAddException("Specified concept would be dominant and is already a member of one or more other coreference sets.");
					}
				}
			else
				{
				// Since it is not dominant, we must ensure that it is not a dominant member of another
				// set.  We can determine this by calling isDominantConcept() in it.
				if (newConcept.isDominantConcept())
					{
					// Concept is already dominant in another set.  Throw an exception.
					throw new CorefAddException("Specified concept is the dominant member of another set.");
					}
				}
		  }
		  
		// If we made it here then everything is ok.  Add the concept to this set.
    corefSet.addElement(newConcept);
    newConcept.addCoreferenceSet(this);
    
    // If we determined that the new concept is dominant, update the dominantGraph field
    // so we have the highest level of dominance.
    if (checkScope)
	    if (isDominant)
		    dominantGraph = newConcept.getEnclosingGraph();    		    
    }

    /**
     * Removes the specified concept from this coreference set.  
     * Note that this method will automatically clear the current defining concept
     * if it is removed from the set.
     *
     * @param deadConcept  the concept to be removed from this coref set.
     * @exception CorefRemoveException  if removal of the specified concept would result in
     * an invalid coreference set.
     */
  public void removeCoreferentConcept(Concept deadConcept) throws CorefRemoveException
    {
    corefSet.removeElement(deadConcept);
    
    // Check to see if this removal will invalidate the set.
    if (checkScope)
	    if (!validateScope())
  	  	{
  	  	// This removal invalidates the set.  Put the concept back in the set and throw an
   		 	// exception.
   	 		corefSet.addElement(deadConcept);
    		throw new CorefRemoveException("Removal of the specified concept would invalidate the coreference set.");
    		}
    
    deadConcept.removeCoreferenceSet(this);

    // Clear defining concept if it is the concept we just removed.
    if (definingConcept == deadConcept)
    	definingConcept = null;
    }

    /**
     * Removes the specified concepts from this coreference set.
     * Note that this method will automatically clear the current defining concept
     * if it is removed from the set.
     *
     * @param deadConcepts  the array of concepts to be removed from this coref set.
     * @exception CorefRemoveException  if removal of the specified concepts would result in
     * an invalid coreference set.
     */
  public void removeConcepts(Concept deadConcepts[]) throws CorefRemoveException
    {
    for (int con = 0; con < deadConcepts.length; con++)
    	corefSet.removeElement(deadConcepts[con]);
    	
    // Check to see if this removal will invalidate the set.
    if (checkScope)
	    if (!validateScope())
  	  	{
    		// This removal invalidates the set.  Put the concepts back in the set and throw an
	    	// exception.
		    for (int con = 0; con < deadConcepts.length; con++)
 			   	corefSet.addElement(deadConcepts[con]);
 		  	 	
	    	throw new CorefRemoveException("Removal of the specified concepts would invalidate the coreference set.");
  	  	}
    	
    for (int con = 0; con < deadConcepts.length; con++)
    	{  	  	
 	    deadConcepts[con].removeCoreferenceSet(this);
 	    
    	// Clear defining concept if it is the concept we just removed.
	    if (definingConcept == deadConcepts[con])
  	  	definingConcept = null;
 	    }
    }

		/**
		 * This method will validate this coreference set following deletions
		 * to ensure that the deletions do not result in an illegal scope or illegal dominants.
		 *
		 * @return true if this coreference set is valid, false otherwise.
		 */
	private boolean validateScope()
		{
		Enumeration corefEnum;
		int depth, minDepth;
		Concept concept, newDominantConcept;
		Graph enclosingGraph, newDominantGraph = null;

		// If the set is empty we are valid.
		if (corefSet.size() == 0)
			return true;

		minDepth = Integer.MAX_VALUE;
		
		corefEnum = corefSet.elements();
		while (corefEnum.hasMoreElements())
			{
			concept = (Concept)corefEnum.nextElement();
			enclosingGraph = concept.getEnclosingGraph();
			
			// If we found a concept that is in the current dominant graph then we are valid.
			if (enclosingGraph == dominantGraph)
				return true;
			
			// The lowest depth value is the highest in dominance so it is our candidate for a
			// new level of dominance.
			depth = enclosingGraph.getContextDepth();
			if (depth < minDepth)
				{
				minDepth = depth;
				newDominantConcept = concept;
				newDominantGraph = enclosingGraph;
				}
			}

		// Check to see if our dominant concept(s) belong to any other sets.
		corefEnum = corefSet.elements();
		while (corefEnum.hasMoreElements())
			{
			concept = (Concept)corefEnum.nextElement();
			enclosingGraph = concept.getEnclosingGraph();
			
			// Check to see if this concept is one of the dominants.
			if (enclosingGraph == newDominantGraph)
				{
				CoreferenceSet corefSets[];
		
				corefSets = concept.getCoreferenceSets();
				
				// If a new dominant concept has more than one coreference set, this set is invalid.
				if (corefSets.length != 1)
					return false;
				}				
			}
			
		// If we reach here, we are valid and we should set the new dominantGraph.
		dominantGraph = newDominantGraph;
		
		return true;
		}

		/**
		 * This method will validate this coreference set when there is no established scope.
		 */
	private void establishScope() throws CorefAddException, InvalidDefiningConceptException
		{
		Enumeration corefEnum;
		int depth, minDepth;
		Concept concept, newDominantConcept;
		Graph enclosingGraph, newDominantGraph = null;

		// If the set is empty we are valid.
		if (corefSet.size() == 0)
			return;

		minDepth = Integer.MAX_VALUE;
		
		corefEnum = corefSet.elements();
		while (corefEnum.hasMoreElements())
			{
			concept = (Concept)corefEnum.nextElement();
			enclosingGraph = concept.getEnclosingGraph();

			// If the enclosing graph is null, it is not valid
			if (enclosingGraph == null)
				throw new CorefAddException("One or more concepts in the set do not have an enclosing graph.");
			
			// The lowest depth value is the highest in dominance so it is our candidate for a
			// new level of dominance.
			depth = enclosingGraph.getContextDepth();
			if (depth < minDepth)
				{
				minDepth = depth;
				newDominantConcept = concept;
				newDominantGraph = enclosingGraph;
				}
			}

		// Check to see if dominant concept(s) belong to any other sets.
		corefEnum = corefSet.elements();
		while (corefEnum.hasMoreElements())
			{				
			CoreferenceSet corefSets[];
		

			concept = (Concept)corefEnum.nextElement();
			enclosingGraph = concept.getEnclosingGraph();
			corefSets = concept.getCoreferenceSets();
			
			// Check to see if this concept is one of the dominants.
			if (enclosingGraph == newDominantGraph)
				{
				
				// If a new dominant concept has more than one coreference set, this set is invalid.
				if (corefSets.length != 1)
					throw new CorefAddException("Specified concept would be dominant and is already a member of one or more other coreference sets.");
				}
			else
				{
				// The concept is not dominant in this set, but must not be dominant in another set.
				for (int set = 0; set < corefSets.length; set++)
					if (corefSets[set].getEnableScopeChecking())
						{
						if (corefSets[set].hasDominantConcept(concept))
							throw new CorefAddException("One or more concepts would be dominant and are already the dominant member of one or more other coreference sets.");
						}
				}
			}
			
		// Check to see if all concepts are within scope.
		corefEnum = corefSet.elements();
		while (corefEnum.hasMoreElements())
			{
			concept = (Concept)corefEnum.nextElement();
			
			if (!concept.isEnclosedBy(newDominantGraph))
				throw new CorefAddException("Specified concept is not in the correct scope for this set.");
			}
			
		// Check to see if defining concept is dominant if one is set.
		if (definingConcept != null)
			if (corefSet.contains(definingConcept))
				{
				if (definingConcept.getEnclosingGraph() != newDominantGraph)
	  	  	throw new InvalidDefiningConceptException("The specified defining concept is not a dominant concept in this coreference set.");
				}
			else
  	  	throw new InvalidDefiningConceptException("The specified defining concept is not a dominant concept in this coreference set.");

					
			
		// If we reach here, we are valid and we should set the new dominantGraph.
		dominantGraph = newDominantGraph;
		}

    /**
     * Tests whether the specified concept is a member of this coref
     * set whether dominant or subordinate.
     * 
     * @param concept  the concept being tested.
     * @return true if the specified concept is a member of this coref set.
     */
  public boolean hasConcept(Concept concept)
    {
    return corefSet.contains(concept);
    }
 
    /**
     * Tests whether the specified concept is a dominant concept in this coreference
     * set.  
     * Note that the return value of this method is undefined if scope checking
     * is disabled.
     * 
     * @param concept  the concept being tested to see if it a dominant concept in this set.
     * @return true if the specified concept is a dominant concept of this coreference set.
     */
  public boolean hasDominantConcept(Concept concept)
    {
    if (!hasConcept(concept))
    	return false;
    	
    return (concept.getEnclosingGraph() == dominantGraph);
    }

    /**
     * Tests whether the specified concept is a subordinate concept of this coref
     * set.
     * Note that the return value of this method is undefined if scope checking
     * is disabled.
     * 
     * @param concept  the concept being tested.
     * @return true if the specified concept is a subordinate concept of this
     * coref set.
     */
  public boolean hasSubordinateConcept(Concept concept)
    {
    if (!hasConcept(concept))
    	return false;
    	
    return (concept.getEnclosingGraph() != dominantGraph);
    }
    
    /**
     * Returns all of the concepts in this coreference set.
     *
     * @return an array containing all the concepts in this coreference set.
     */
  public Concept[] getCoreferentConcepts()
  	{
  	Concept arr[];
  	
  	arr = new Concept[corefSet.size()];
  	corefSet.copyInto(arr);
  	
  	return arr;
  	}
  	
    /**
     * Returns the dominant concepts in this coreference set.
     * Note that the return value of this method is undefined if scope checking
     * is disabled.
     *
     * @return an array containing the dominant concepts in this coreference set.
     */
  public Concept[] getDominantConcepts()
  	{
  	Concept concept, arr[], finalArr[];
  	Enumeration conEnum;
  	int numDom;
  	
  	arr = new Concept[corefSet.size()];
  	numDom = 0;
  	conEnum = corefSet.elements();
  	while (conEnum.hasMoreElements())
  		{
  		concept = (Concept)conEnum.nextElement();
  		
  		if (concept.getEnclosingGraph() == dominantGraph)
				{
				arr[numDom] = concept;
				numDom++;
				}
  		}
  	
  	finalArr = new Concept[numDom];
  	System.arraycopy(arr, 0, finalArr, 0, numDom);
  	
  	return finalArr;
  	}

    /**
     * Returns the subordinate concepts in this coreference set.  Note that this array
     * may be empty.
     * Note that the return value of this method is undefined if scope checking
     * is disabled.
     *
     * @return an array containing the subordinate concepts in this coreference set, 
     * possibly empty.
     */
  public Concept[] getSubordinateConcepts()
  	{
  	Concept concept, arr[], finalArr[];
  	Enumeration conEnum;
  	int numSub;
  	
  	arr = new Concept[corefSet.size()];
  	numSub = 0;
  	conEnum = corefSet.elements();
  	while (conEnum.hasMoreElements())
  		{
  		concept = (Concept)conEnum.nextElement();
  		
  		if (concept.getEnclosingGraph() != dominantGraph)
				{
				arr[numSub] = concept;
				numSub++;
				}
  		}
  	
  	finalArr = new Concept[numSub];
  	System.arraycopy(arr, 0, finalArr, 0, numSub);
  	
  	return finalArr;
  	}
  	
  	/**
  	 * Sets a flag which enables or disables scope checking (enabled by default) when
  	 * modifying this coreference set.
  	 *
  	 * @param flag  the new setting for the scope checking flag.
  	 * @exception CorefAddException  if any of the concepts in the set have no enclosing
  	 * graph,
  	 * or if any of the concepts is out of scope,
  	 * or if any of the domintant concepts belongs to one or more other coreference sets.
		 * @exception InvalidDefiningConceptException  if a defining concept has been specified
		 * and is not dominant.
  	 */
  public void setEnableScopeChecking(boolean flag) throws CorefAddException,
  	InvalidDefiningConceptException
  	{
  	// If we are changing the flag from false to true, we must ensure that the scope is
  	// valid.	
  	if (!checkScope && flag)
  		{
			establishScope();				
  		}
  		
  	checkScope = flag;
  	}
  	
  	/**
  	 * Returns the current value of the flag which enables or disables scope checking 
  	 * (disabled by default) when modifying this coreference set.
  	 *
  	 * @return the current setting for the scope checking flag.
  	 */
  public boolean getEnableScopeChecking()
  	{
  	return checkScope;
  	}
  }
