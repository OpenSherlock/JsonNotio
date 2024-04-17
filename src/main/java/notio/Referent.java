package notio;

import java.util.*;

    /** 
     * A class for storing the referent of a concept.
     * A referent consists of a quantifier, a designator, and a descriptor (nested graph).
     * A null quantifier indicates that the enclosing concept is existentially quantified.
     * A null designator indicates that the enclosing concept is unspecified.
     * A null descriptor indicates that the enclosing concept is undescribed.
     * A combination of a null quantifier, null designator, and null descriptor indicates
     * that the enclosing concept is a generic concept.  A concept that has no
     * referent at all (null) is also considered to be a generic concept.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.34.2.3 $, $Date: 1999/10/08 05:46:24 $
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
public class Referent implements java.io.Serializable
  {
	  /** The designator for this referent. **/
  private Designator designator;
  
	  /** The quantifier for this referent. **/
  private Macro quantifier;
  
	  /** The descriptor for this referent. **/
  private Graph descriptor;
  
	  /** The coreference sets to which this referent belongs. **/
  private CoreferenceSet corefSets[];
  
  	/** The concept that encloses this referent. **/
  private Concept enclosingConcept;

		/**
		 * Constructs a new referent with the specified quantifier, designator, and descriptor.
		 * The quantifier may be null, indicating existential quantification.
		 * The designator may be null, indicating an unspecified referent.
		 * The descriptor may be null, indicating an undescribed referent.
		 *
		 * @param newQuantifier  the quantifier for this referent.
		 * @param newDesignator  the designator for this referent.
		 * @param newDescriptor  the descriptor for this referent.
		 */
  public Referent(Macro newQuantifier, Designator newDesignator, Graph newDescriptor)
  	{
  	quantifier = newQuantifier;
 		designator = newDesignator;
 		descriptor = newDescriptor;
  	
  	if (designator != null)
  		{
	  	designator.setEnclosingReferent(this);
  		}  		
  		
  	if (descriptor != null)
  		{
	  	descriptor.setEnclosingReferent(this);
  		}  		
  	}
  	
		/**
		 * Constructs a new referent with the specified quantifier and designator.
		 * The quantifier may be null, indicating existential quantification.
		 * The designator may be null, indicating an unspecified referent.
		 * The decriptor is initialized to null, indicating an undescribed referent.
		 *
		 * @param newQuantifier  the quantifier for this referent.
		 * @param newDesignator  the designator for this referent.
		 */
  public Referent(Macro newQuantifier, Designator newDesignator)
  	{
  	quantifier = newQuantifier;
 		designator = newDesignator;
  	
  	if (designator != null)
  		{
	  	designator.setEnclosingReferent(this);
  		}  		
  	}
   	
		/**
		 * Constructs a new referent with the specified quantifier and descriptor.
		 * The quantifier may be null, indicating existential quantification.
		 * The descriptor may be null, indicating an undescribed referent.
		 * The designator is initialized to null, indicating an unspecified referent.
		 *
		 * @param newQuantifier  the quantifier for this referent.
		 * @param newDescriptor  the descriptor for this referent.
		 */
  public Referent(Macro newQuantifier, Graph newDescriptor)
  	{
  	quantifier = newQuantifier;
 		designator = null;
 		descriptor = newDescriptor;
  	
  	if (descriptor != null)
  		{
	  	descriptor.setEnclosingReferent(this);
  		}  		
  	}
  	
		/**
		 * Constructs a new referent with the specified designator and descriptor.
		 * The designator may be null, indicating an unspecified referent.
		 * The descriptor may be null, indicating an undescribed referent.
		 * The quantifier is initialized to null, indicating existential quantification.
		 *
		 * @param newDesignator  the designator for this referent.
		 * @param newDescriptor  the descriptor for this referent.
		 */
  public Referent(Designator newDesignator, Graph newDescriptor)
  	{
  	quantifier = null;
 		designator = newDesignator;
 		descriptor = newDescriptor;
  	
  	if (designator != null)
  		{
	  	designator.setEnclosingReferent(this);
  		}  		
  		
  	if (descriptor != null)
  		{
	  	descriptor.setEnclosingReferent(this);
  		}  		
  	}
  	
		/**
		 * Constructs a new referent with the specified quantifier.
		 * The quantifier may be null, indicating existential quantification.
		 * The designator is initialized to null, indicating an unspecified referent.
		 * The decriptor is initialized to null, indicating an undescribed referent.
		 *
		 * @param newQuantifier  the quantifier for this referent.
		 */
  public Referent(Macro newQuantifier)
  	{
  	designator = null;
  	quantifier = newQuantifier;
  	descriptor = null;
  	}
  	
		/**
		 * Constructs a new referent with the specified designator.
		 * The designator may be null, indicating an unspecified referent.
		 * The quantifier is initialized to null, indicating existential quantification.
		 * The decriptor is initialized to null, indicating an undescribed referent.
		 *
		 * @param newDesignator  a single designator for this referent.
		 */
  public Referent(Designator newDesignator)
  	{
  	quantifier = null;
 		designator = newDesignator;
 		descriptor = null;
  	
  	if (designator != null)
  		{
	  	designator.setEnclosingReferent(this);
  		}  		
  	}
  
		/**
		 * Constructs a new referent with the specified descriptor.
		 * The descriptor may be null, indicating an undescribed referent.
		 * The quantifier is initialized to null, indicating existential quantification.
		 * The designator is initialized to null, indicating an unspecified referent.
		 *
		 * @param newDescriptor  the descriptor for this referent.
		 */
  public Referent(Graph newDescriptor)
  	{
  	quantifier = null;
 		designator = null;
 		descriptor = newDescriptor;
  	
  	if (descriptor != null)
  		{
	  	descriptor.setEnclosingReferent(this);
  		}  		
  	}
  	
		/**
		 * Constructs a new referent with no quantifier, designator or descriptor.
		 * This is automatically a 'generic' referent.
		 * The quantifier is initialized to null, indicating existential quantification.
		 * The designator is initialized to null, indicating an unspecified referent.
		 * The decriptor is initialized to null, indicating an undescribed referent.
		 */
  public Referent()
  	{
  	quantifier = null;
 		designator = null;
 		descriptor = null;
  	}
  
    /**
     * Sets this referent's quantifier.
     * Null indicates existential quantification.
     *
     * @param newQuantifier  this referent's new quantifier.
     */
  public void setQuantifier(Macro newQuantifier)
    {
    quantifier = newQuantifier;
    }

    /**
     * Sets this concept's designator.
     * Null indicates an unspecified referent.
     *
     * @param newDesignator this referent's new designator or null.
     */
  public void setDesignator(Designator newDesignator)
    {
    if (designator == newDesignator)
    	return;
    	
    if (designator != null)
    	{
    	// Detach old designator
	  	designator.setEnclosingReferent(null);
	  	}
	  	
	  designator = newDesignator;

    if (designator != null)
    	{
    	// Attach new designator
	  	designator.setEnclosingReferent(this);
	  	}
    }
    
    /**
     * Sets this referent's descriptor.
     * Null indicates undescribed.
     *
     * @param newDescriptor  this referent's new descriptor.
     */
  public void setDescriptor(Graph newDescriptor)
    {
    // Detach old descriptor, if any
    if (descriptor != null)
    	descriptor.setEnclosingReferent(null);
    
    descriptor = newDescriptor;
    
    // Attach new descriptor, if any
    if (descriptor != null)
	    descriptor.setEnclosingReferent(this);    
    }

    /**
     * Returns this referent's quantifier.
     * Null indicates existential quantification.
     *
     * @return this concept's quantifier.
     */
  public Macro getQuantifier()
    {
    return quantifier;
    }

    /**
     * Returns this referent's designator.
     * Null indicates an unspecified referent.
     *
     * @return this referent's designator or null.
     */
  public Designator getDesignator()
    {
    return designator;
    }
    
    /**
     * Returns this referent's descriptor.
     * Null indicates an undescribed referent.
     *
     * @return this referent's descriptor or null.
     */
  public Graph getDescriptor()
    {
    return descriptor;
    }
    
		/**
		 * Returns true if this referent forms a context.
		 * A referent forms a context if it has a descriptor.
		 *
		 * @return true if this referent forms a context.
		 *
		 * @bug Should probably include the non-blank condition.
		 */
	public boolean isContext()
		{
		return descriptor != null;
		}

  	/**
  	 * Returns the concept that encloses this referent.
  	 *
  	 * @return the concept that encloses this referent.
  	 */
	public Concept getEnclosingConcept()
		{
		return enclosingConcept;
		}
		
		/**
		 * Sets the enclosing concept for this referent.
		 *
		 * @param newEnclosingConcept  the new enclosing concept for this referent.
		 *
     * @impspec this method is present to support the getEnclosingConcept() method.
		 */
	void setEnclosingConcept(Concept newEnclosingConcept)
		{
		enclosingConcept = newEnclosingConcept;
		}
		
    /**
     * Performs a copy operation on this referent according to the
     * the specified CopyingScheme.
     * The result may be a new referent or simply a reference to this referent
     * depending on the scheme.
     * Coreference sets will be copied as required by the CopyingScheme.
     *
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @return the result of the copy operation.
     */
  public Referent copy(CopyingScheme copyScheme)
    {
    return copy(copyScheme, new Hashtable());
    }

    /**
     * Performs a copy operation on this referent according to the
     * the specified CopyingScheme.
     * The result may be a new referent or simply a reference to this referent
     * depending on the scheme.
     * Coreference sets will be copied as required by the CopyingScheme.
     *
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @param substitutionTable  a hashtable containing copied objects available due to
     * earlier copy operations.
     * @return the result of the copy operation.
     * @bug Currently uses CN_ copy flags.  Should probably add referent flags.
     */
  public Referent copy(CopyingScheme copyScheme, Hashtable substitutionTable)
    {
    switch (copyScheme.getConceptFlag())
    	{
    	case CopyingScheme.CN_COPY_DUPLICATE:
    		{
    		Referent newReferent;
    		Designator orgDesignator;
    		Graph orgDescriptor;

				// If this referent is already already in the substitution table, return its
				// substitute.
				newReferent = (Referent)substitutionTable.get(this);
				if (newReferent != null)
					return newReferent;

				// Create the new referent, copying the designator as we do so.
				try
					{
					newReferent = (Referent)this.getClass().newInstance();
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
				
		    newReferent.setQuantifier(getQuantifier());
		    
		    orgDesignator = getDesignator();
		    if (orgDesignator != null)
		    	newReferent.setDesignator(orgDesignator.copy(copyScheme, substitutionTable));
		    	
		    orgDescriptor = getDescriptor();
		    if (orgDescriptor != null)
			    newReferent.setDescriptor(orgDescriptor.copy(copyScheme, substitutionTable));

				// Add pair to substitution table
				substitutionTable.put(this, newReferent);

    		return newReferent;
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
     * Compares two referents to decide if they match.  The exact semantics of matching
     * are determined by the matching scheme.  
     *
     * @param first  the first referent being matched.
     * @param second  the second referent being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two referents match according to the scheme's criteria.
     *
     * @bug Should add specified quantifier matching too.
     * @bug Should review descriptor matching to see if this is doing the right thing.
     */
  public static MatchResult matchReferents(Referent first, Referent second,
    MatchingScheme matchingScheme)
    {
  	Designator firstDesignator, secondDesignator;
  	Graph firstDescriptor, secondDescriptor;
  	MatchResult designatorResult;
    MatchResult descriptorResult;
    MatchingScheme nestedScheme;        

		// If a referent is null, then pass it on as a single null designator,
		
    if (first == null)
    	firstDesignator = null;
    else
	    firstDesignator = first.getDesignator();
    	
    if (second == null)
    	secondDesignator = null;
    else    
	    secondDesignator = second.getDesignator();
	    
		designatorResult = Designator.matchDesignators(firstDesignator, secondDesignator,
			matchingScheme);
			
		if (!designatorResult.matchSucceeded())
			return designatorResult;

		// Designators matched so now check for descriptors
		// Use nested matching scheme, if any
    nestedScheme = matchingScheme.getNestedMatchingScheme();
    if (nestedScheme == null)
      nestedScheme = matchingScheme;

    if (first == null)
    	firstDescriptor = null;
    else
	    firstDescriptor = first.getDescriptor();
    	
    if (second == null)
    	secondDescriptor = null;
    else    
	    secondDescriptor = second.getDescriptor();        
        
    descriptorResult = Graph.matchGraphs(firstDescriptor, secondDescriptor, nestedScheme);
          
    return descriptorResult;
    }
  }
