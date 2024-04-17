package notio;

import java.util.*;

import com.google.gson.JsonObject;

import java.io.Serializable;

    /**
     * The concept node class.
     * A concept consists primarily of a type and a referent, either or both of which
     * can be null.  Concepts can also be members of coreference sets, linking them to 
     * other concepts.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.79.2.1 $, $Date: 1999/09/21 02:01:37 $
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
     * @idea Add relatedBy() method.
     */
public class Concept extends Node implements Serializable
  {
  	/** The referent for this concept. **/
  private Referent referent;
  
	  /** The coreference sets associated with this concept. **/
  private Vector corefSets = new Vector(1,1);

    /**
     * Constructs a concept with the specified type and referent.
     * A null value may be used for either or both.  
     * A null referent indicates a generic concept.
     *
     * @param newType  the concept type for this concept.
     * @param newReferent  the referent for this concept.
     */
  public Concept(ConceptType newType, Referent newReferent)
    {
    super.setNodeType(newType);
    referent = newReferent;
    
    if (referent != null)
	    referent.setEnclosingConcept(this);
    }

    /**
     * Constructs a concept with the specified type and no referent.
     * The referent in this case is set to null, indicating a generic concept.
     *
     * @param newType  the concept type for this concept.
     */
  public Concept(ConceptType newType)
    {
    this(newType, null);
    }

    /**
     * Constructs a concept with the specified referent and no type.
     * A null referent indicates a generic concept.
     *
     * @param newReferent  the referent for this concept.
     */
  public Concept(Referent newReferent)
    {
    this(null, newReferent);
    }
    
    /**
     * Constructs a concept neither type nor referent.
     * Both type and referent are initialized to null.
     * A null referent indicates a generic concept.
     */
  public Concept()
    {
    this(null, null);
    }
    
  public JsonObject toJSON() {
	  JsonObject result = new JsonObject();
	  
	  
	  return result;
  }

    /**
     * Returns this concept's type.
     * Null indicates an untyped concept.
     *
     * @return this concept's type
     */
  public ConceptType getType()
    {
    return (ConceptType)getNodeType();
    }

    /**
     * Returns this concept's referent.  
     * Null indicates a generic concept.
     *
     * @return this concept's referent.
     */
  public Referent getReferent()
    {
    return referent;
    }
    
    /**
     * Sets this concept's type.
     * If a type has already been set, it is replaced.
     *
     * @param newType  the new type for this concept.
     * @bug What about type-checking in relations.
     */
  public void setType(ConceptType newType)
    {
    setNodeType(newType);
    }

    /**
     * Sets this concept's referent.
     * If a referent has already been set, it is replaced.
     * The referent may be set to null, indicating a generic concept.
     *
     * @param newReferent  the new referent for this concept.
     */
  public void setReferent(Referent newReferent)
    {
    // Clear old referent's enclosing concept field (if any)
    if (referent != null)
    	referent.setEnclosingConcept(null);
    	
    // Replace referent.
    referent = newReferent;

	// Set enclosing concept for new referent (if any)
    if (referent != null)
    	referent.setEnclosingConcept(this);
    }

    /**
     * Returns the coreferences sets of which this concept is a member.
     *
     * @return an array containing all of the coreference sets of which this
     * concept is a member, possibly empty.
     */
  public CoreferenceSet[] getCoreferenceSets()
    {
    CoreferenceSet corefSetsArr[];

    corefSetsArr = new CoreferenceSet[corefSets.size()];
    corefSets.copyInto(corefSetsArr);

    return corefSetsArr;
    }

    /**
     * Returns all of the concepts coreferent to this concept.
     * This is essentially the union of all the coreferent sets to
     * which this concept belongs.  Note that every concept is coreferent
     * to itself so this method will always return at least one element.
     *
     * @return an array containing of the concepts coreferent to this concept.
     */
  public Concept[] getCoreferentConcepts()
    {
    CoreferenceSet corefSetsArr[];
    Set corefConceptSet = new Set();
    Concept concepts[], finalArr[];

    corefSetsArr = getCoreferenceSets();
    for (int set = 0; set < corefSetsArr.length; set++)
    	{
    	concepts = corefSetsArr[set].getCoreferentConcepts();
    	for (int con = 0; con < concepts.length; con++)
    		corefConceptSet.addElement(concepts[con]);
    	}

    // Every concept is coreferent with itself at the very least so make sure
    // it's in the set.
    corefConceptSet.addElement(this);

    finalArr = new Concept[corefConceptSet.size()];
    corefConceptSet.copyInto(finalArr);

    return finalArr;
    }

    /**
     * Returns true if this concept is a dominant node of a coreference set.
     *
     * @return true if this concept is a dominant node of a coreference set.
     */
  public boolean isDominantConcept()
    {
    // A dominant concept must be a member of one coref set.
    if (corefSets.size() != 1)
      return false;

    return ((CoreferenceSet)corefSets.elementAt(0)).hasDominantConcept(this);
    }

    /**
     * Returns true if this concept is the defining concept of a coreference
     * set.
     *
     * @return true if this concept is the defining concept of a coreference set.
     */
  public boolean isDefiningConcept()
    {
    // A dominant concept may only be a member of one coref set.
    if (corefSets.size() != 1)
      return false;

    return ((CoreferenceSet)corefSets.elementAt(0)).getDefiningConcept() == this;
    }

    /**
     * Adds a coreference set to this concept.
     *
     * @param newCorefSet  the coref set to be added.
     */
  void addCoreferenceSet(CoreferenceSet newCorefSet)
    {
    if (corefSets.contains(newCorefSet))
      return;
    else
      corefSets.addElement(newCorefSet);
    }

    /**
     * Removes a coreference set from this concept.
     *
     * @param deadCorefSet  the coref set to be removed.
     */
  void removeCoreferenceSet(CoreferenceSet deadCorefSet)
    {
    corefSets.removeElement(deadCorefSet);
    }

		/**
		 * Returns true if this concept is enclosed by the specified concept.
		 *
		 * @param concept  the concept being checked for as enclosing this concept.
		 * @return true if this concept is enclosed by the specified concept.
		 */
	public boolean isEnclosedBy(Concept concept)
		{
  	Concept enclosingConcept;
  	Graph enclosingGraph;

  	enclosingGraph = getEnclosingGraph();
  	
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
		 * Returns true if this concept is enclosed by the specified graph.
		 *
		 * @param graph  the graph being checked for as enclosing this concept.
		 * @return true if this concept is enclosed by the specified graph.
		 */
	public boolean isEnclosedBy(Graph graph)
		{
  	Graph enclosingGraph;

		enclosingGraph = getEnclosingGraph();

		while (enclosingGraph != null)
			{
  		if (enclosingGraph == graph)
  			return true;
  			
  		enclosingGraph = enclosingGraph.getContextGraph();
  		}
  	
  	return false;
		}
		
    /**
     * Isolates this concept by removing it from all coreference sets
     * to which it belongs and by isolating any and all concepts that may
     * be nested within it.
     *
     * @bug This should be done in some cleaner way, either in Graph or some other way here.
     */
  public void isolate() throws CorefRemoveException
    {
    CoreferenceSet corefSets[];

    corefSets = getCoreferenceSets();
    for (int set = 0; set < corefSets.length; set++)
    	corefSets[set].removeCoreferentConcept(this);

    if (isContext())
    	{
    	Concept concepts[];
	    Graph descriptor;

    	descriptor = referent.getDescriptor();
    	
			if (descriptor != null)
				{
	    	concepts = descriptor.getConcepts();

	    	for (int con = 0; con < concepts.length; con++)
   				concepts[con].isolate();
   			}
	    }
		}

    /**
     * Returns an array (possibly empty) of the relations in the enclosing
     * graph that relate this concept.  This method will return null if the
     * concept does not belong to any graph (getEnclosingGraph() returns null).
     *
     * @return an array containing of the relations that relate this concept.
     * @bug Should this method be in Graph instead?
     */
  public Relation[] getRelators()
  	{
  	Relation relArr[], finalArr[];
  	Vector relVec;
  	Graph graph;

  	graph = getEnclosingGraph();

  	if (graph == null)
			return null;

  	relArr = graph.getRelations();
  	relVec = new Vector(relArr.length);

  	for (int rel = 0; rel < relArr.length; rel++)
  		if (relArr[rel].relatesConcept(this))
  			relVec.addElement(relArr[rel]);

  	finalArr = new Relation[relVec.size()];
  	relVec.copyInto(finalArr);

  	return finalArr;
  	}

    /**
     * Performs a copy operation on this concept according to the
     * the specified CopyingScheme.
     * The result may be a new object of exactly the same class as the original or simply 
     * a reference to this concept depending on the copying scheme.
     * Coreference sets will be copied as needed depending on the copying scheme.
     *
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @return the result of the copy operation.
     */
  public Concept copy(CopyingScheme copyScheme)
    {
    return copy(copyScheme, new Hashtable());
    }

    /**
     * Performs a copy operation on this concept according to the
     * the specified CopyingScheme.
     * The result may be a new object of exactly the same class as the original or simply 
     * a reference to this concept depending on the copying scheme.
     * Coreference sets will be copied as needed depending on the copying scheme.
     *
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @param substitutionTable  a hashtable containing copied objects available due to
     * earlier copy operations.
     * @return the result of the copy operation.
     */
  public Concept copy(CopyingScheme copyScheme, Hashtable substitutionTable)
    {
    switch (copyScheme.getConceptFlag())
    	{
    	case CopyingScheme.CN_COPY_DUPLICATE:
    		{
				Concept newConcept;
				Referent newReferent;
				CoreferenceSet corefSets[], substituteSet;

				// If this concept is already already in the substitution table, return its
				// substitute.
				newConcept = (Concept)substitutionTable.get(this);
				if (newConcept != null)
					return newConcept;

				corefSets = getCoreferenceSets();

				// Check for and create subsitutes for the coreference sets.
				for (int set = 0; set < corefSets.length; set++)
					{
					// Check for existing substitute coref set
					substituteSet = (CoreferenceSet)substitutionTable.get(corefSets[set]);

					// If no existing substitution then we must create one.
					// We also check all members of the original sets to see if they have
					// substitutions and copy those in the new set.
					// This is in case any copies in the substitution set did not copy the coreference
					// set for some reason (e.g. not a member at the time or not copied using this
					// method).
					if (substituteSet == null)
						{
						Concept domConcepts[], subConcepts[], corefSub;

						try
							{
							substituteSet = (CoreferenceSet)corefSets[set].getClass().newInstance();
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
						
						substituteSet.setDefiningLabel(corefSets[set].getDefiningLabel());

						substitutionTable.put(corefSets[set], substituteSet);

						// First add dominant concepts to the set

						domConcepts = corefSets[set].getDominantConcepts();
						for (int corefCon = 0; corefCon < domConcepts.length; corefCon++)
							{
							corefSub = (Concept)substitutionTable.get(domConcepts[corefCon]);
							if (corefSub != null)
								try
									{
									substituteSet.addCoreferentConcept(corefSub);
									}
								catch (CorefAddException e)
									{
									// This should never be thrown since we have checked to see how this
									// should be added.
									e.printStackTrace();
									System.exit(1);
									}
							}
							
						// Next add subordinate concepts to the set
						subConcepts = corefSets[set].getSubordinateConcepts();
						for (int corefCon = 0; corefCon < subConcepts.length; corefCon++)
							{
							corefSub = (Concept)substitutionTable.get(subConcepts[corefCon]);
							if (corefSub != null)
								try
									{
									substituteSet.addCoreferentConcept(corefSub);
									}
								catch (CorefAddException e)
									{
									// This should never be thrown since we have checked to see how this
									// should be added.
									e.printStackTrace();
									System.exit(1);
									}
							}							
						}
					}

				// Create the new concept, copying the referent as we do so.
				if (referent != null)
					newReferent = referent.copy(copyScheme, substitutionTable);
				else
					newReferent = null;

				try
					{
					newConcept = (Concept)this.getClass().newInstance();
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
							
				newConcept.setType(getType());
				newConcept.setReferent(newReferent);

				// Add this concept to all of the necessary substitute coreference sets.
				for (int set = 0; set < corefSets.length; set++)
					{
					// Set existing substitute coref set
					substituteSet = (CoreferenceSet)substitutionTable.get(corefSets[set]);

					// We should be ok adding this copy to the coref set since any dominant concepts
					// should have been copied earlier (this may not be true).
					// If it's not true, we should probably try it and if we throw an exception, 
					// ignore it since it will probably be copied later on.
					try
						{
						substituteSet.addCoreferentConcept(newConcept);
						}
					catch (CorefAddException e)
						{
						// Hopefully never happens, see above.
						System.err.println("False assumption regarding copying of coreference sets.  Please contact Notio developer immediately and report this message.");
						e.printStackTrace();
						System.exit(1);
						}
					}

		    // Copy the comment if the scheme requires it
    		if (copyScheme.getCommentFlag() == CopyingScheme.COMM_COPY_ON)
    			{
					newConcept.setComment(getComment());
					}

				// Add pair to substitution table
				substitutionTable.put(this, newConcept);

    		return newConcept;
    		}

    	case CopyingScheme.CN_COPY_REFERENCE:
    		{
    		return this;
    		}

    	default:
        throw new UnimplementedFeatureException("Specified Concept copy control flag is unknown.");
    	}
    }

     /**
      * Returns a new concept identical to this but restricted to the new type.
      *
      * @param subType  the type to be restricted to.
      * @return the new restricted concept.
      * @exception notio.RestrictionException
      *            if subType is not a real subtype of the current type
      * @bug Need to rethink restricts.
      */
  public Concept restrictTo(ConceptType subType) throws RestrictionException
    {
    if (!subType.hasSuperType(getType()))
      throw new RestrictionException("Specified restriction type is not a subtype of the current type.");

		return null;
    }

    /**
      * Returns a new concept identical to this but restricted to the given
      * referent.
      *
      * @param newReferent  the referent to be restricted to.
      * @return the new restricted concept.
      * @exception notio.RestrictionException
      *            if there is already an equally specific referent
      * @bug Need some method to check whether one designator is
      * more specific than another.  Could be tricky with descriptors if
      * we want to get fancy.
      */
  public Concept restrictTo(Referent newReferent) throws RestrictionException
    {
    return null;
    }

    /**
      * Returns a new concept identical to this but restricted to the given
      * referent and subtype.
      *
      * @param subType  the type to be restricted to.
      * @param newReferent  the referent to be restricted to.
      * @return the new restricted concept.
      * @exception notio.RestrictionException
      *            if subType is not a real sub-type of the current type
      *            and/or if there is already an equally specific referent.
      *
      * @bug Need some method to check whether one designator is
      * more specific than another.  Could be tricky with descriptors if
      * we want to get fancy.
      */
  public Concept restrictTo(ConceptType subType, Referent newReferent) throws RestrictionException
    {
    if (!subType.hasSuperType(getType()))
      throw new RestrictionException("Specified restriction type is not a subtype of the current type.");

    return null;
    }

    /**
      * Returns true if this concept is a context.  A context is a concept
      * whose designator is a non-blank conceptual graph.
      * This method will return true if this concept has a referent which returns
      * true when its isContext() method is called.
      *
      * @return true if this concept is a context.
      * @see notio.Referent#isContext()
      */
  public boolean isContext()
    {
    if (referent == null)
    	return false;
    	
		return referent.isContext();
    }

    /**
     * Returns true if this concept is generic; false if the concept is
     * specific.  A generic concept either has a referent of null, or a referent
     * in which both the quantifier and designator are null.
     *
     * @return true if this concept is generic; false if specific.
     */
  public boolean isGeneric()
    {
    if (referent == null)
    	return true;
    else
	    return (referent.getQuantifier() == null) && (referent.getDesignator() == null) && (referent.getDescriptor() == null);
    }

    /**
     * Returns true if the two concepts specified are coreferent.
     *
     * @param first  the first concept.
     * @param second  the second concept.
     * @return true if the two concepts specified are coreferent.
     */
	public static boolean testCoreference(Concept first, Concept second)
		{
		CoreferenceSet firstSets[], secondSets[];

		firstSets = first.getCoreferenceSets();
		if (firstSets.length == 0)
			return false;

		secondSets = second.getCoreferenceSets();
		if (secondSets.length == 0)
			return false;

		for (int fset = 0; fset < firstSets.length; fset++)
			for (int sset = 0; sset < secondSets.length; sset++)
				if (firstSets[fset] == secondSets[sset])
					return true;

		return false;
		}

    /**
     * Compares two concepts to decide if they match.  The exact semantics of matching
     * are determined by the matching scheme.  This method may compare more than two
     * concepts depending on the setting of the coreference matching flags.
     *
     * @param first  the first concept being matched.
     * @param second  the second concept being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two concepts match according to the scheme's criteria.
     * @bug Should add specified quantifier matching too.
     */
  public static MatchResult matchConcepts(Concept first, Concept second,
    MatchingScheme matchingScheme)
    {
    // If coreference agreement is off then we only need to match the two concepts
    // Otherwise we must match all coreferent concepts with each other.
    if (matchingScheme.getCoreferenceAgreementFlag() == MatchingScheme.COREF_AGREE_OFF)
    	return matchSingleConcepts(first, second, matchingScheme);
    else
    	{
    	Concept firstSet[], secondSet[];
    	MatchResult result, singleResult = null;

    	firstSet = first.getCoreferentConcepts();
  	  secondSet = second.getCoreferentConcepts();

  	  for (int fcon = 0; fcon < firstSet.length; fcon++)
	  	 	for (int scon = 0; scon < secondSet.length; scon++)
	  	 		{
					result = matchSingleConcepts(firstSet[fcon], secondSet[scon], matchingScheme);

	  	 		if ((firstSet[fcon] == first) && (secondSet[scon] == second))
	  	 			singleResult = result;

	  	 		if (!result.matchSucceeded())
	  	 			return new MatchResult(false);
					}

  	  return singleResult;
  	  }
    }

    /**
     * Compares two concepts to decide if they match.  The exact semantics of matching
     * are determined by the matching scheme.  This specifically handles the matching
     * between two single concepts.
     *
     * @param first  the first concept being matched.
     * @param second  the second concept being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two concepts match according to the scheme's criteria.
     * @bug Should add specified quantifier matching too.
     * @bug Currently if doing type matching, if either type is 'null' then the match always
     * works.  This might need to be a matching scheme flag instead.
     * @bug Must change comments to reflect use of MatchResult.
     */
  private static MatchResult matchSingleConcepts(Concept first, Concept second,
    MatchingScheme matchingScheme)
    {
    ConceptType firstType, secondType;

    switch (matchingScheme.getConceptFlag())
      {
      case MatchingScheme.CN_MATCH_INSTANCE:
        return new MatchResult(first == second);

      case MatchingScheme.CN_MATCH_TYPES:
      	{
      	firstType = first.getType();
      	secondType = second.getType();

      	if ((firstType == null) || (secondType == null))
      		return new MatchResult(true);

        return new MatchResult(
        	ConceptType.matchConceptTypes(first.getType(), second.getType(), matchingScheme)
        	);
				}

      case MatchingScheme.CN_MATCH_REFERENTS:
      	{
      	if (matchingScheme.getCoreferenceAutoMatchFlag() == MatchingScheme.COREF_AUTOMATCH_ON)
      		{
	      	if (testCoreference(first, second))
  	    		return new MatchResult(true);
  	    	}

				return Referent.matchReferents(first.getReferent(), second.getReferent(),
					matchingScheme);
				}

      case MatchingScheme.CN_MATCH_COREFERENTS:
      	{
				return new MatchResult(testCoreference(first, second));
				}

      case MatchingScheme.CN_MATCH_ALL:
      	{
      	if (matchingScheme.getCoreferenceAutoMatchFlag() == MatchingScheme.COREF_AUTOMATCH_ON)
      		{
	      	if (testCoreference(first, second))
  	    		return new MatchResult(true);
  	    	}

      	firstType = first.getType();
      	secondType = second.getType();

       	if (((firstType == null) || (secondType == null)) ||
       		(ConceptType.matchConceptTypes(first.getType(), second.getType(), matchingScheme)))
       		{
       		return Referent.matchReferents(first.getReferent(), second.getReferent(),
	          matchingScheme);
       		}
       	else
       		return new MatchResult(false);
        }

      case MatchingScheme.CN_MATCH_ANYTHING:
        return new MatchResult(true);

      default:
        throw new UnimplementedFeatureException("Specified Concept match control flag is unknown.");
      }
    }
  }
