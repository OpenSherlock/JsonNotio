package notio;

import java.util.Hashtable;
import java.io.Serializable;

    /** 
     * The conceptual relation node class.
     * This class encapsulates all available information about a conceptual relation within
     * a graph.  It consists of a type and/or arguments (concepts connected to the arcs of
     * the relation).  The arguments can be divided into input and output arguments as
     * desired, though the default is that the last (and possibly only) argument is the sole
     * output arc.  Relations with no arguments (zero valence) are allowed.  
     * Note that the  terms 'arc' and 'argument' are used interchangably 
     * throughout the documentation.<P>
     * Conceptually, the <I>N</I> arguments of a relation are all stored in one array, with
     * zero being the index of the first argument.  The first <I>k</I> elements of this 
     * array are considered to be inputs and the remaining <I>N - k</I> are outputs.  
     * This allows them to be both ordered and directed at the same time.
     * The boundary between inputs and outputs is defined using the "output start index".
     * This is the index into the array of arguments at which the first output argument
     * occurs.  If there are no outputs, the output start index is 1 greater than the 
     * number of inputs.
     * Using setArguments() sets the entire array.  
     * Using setInputArgument() and setOutputArgument() sets the same array 
     * plus the index that defines where the outputs begin.  
     * That index can be set and found explicitly by calling setOutputStartIndex() and
     * getOutputStartIndex().  In the end, it's all just one array and an index that 
     * shows where the outputs start.  
     * The default output start index for relations is <I>N - 1</I>, as specified by 
     * the standard.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.61.2.1 $, $Date: 1999/09/21 05:32:08 $
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
     * @bug Need to rework some actor-related comments.
     * @bug restrictTo() and copy() can only return a Relation, not an Actor.  Perhaps
     * it can check the class to make sure that the correct class is used for the copy.
     * @bug Do we really want package visibility for member vars?
     * @bug Really need to rethink this whole input/output argument approach.
     */
public class Relation extends Node implements Serializable
  {
  	/** The concepts related by this relation. **/
  Concept arguments[];
  
  	/** The index of the first output arc (this is the last index for ordinary relations). **/
  int outputStartIndex;

    /**
     * Constructs a relation with the given type and arguments.
     * The last argument is assumed to be the sole output argument.
     * The number of arguments must conform to the valence of the
     * relation type unless it is unspecified in which case the valence
     * is set to the number of arguments in the array.
     *
     * @param newType  the type for this relation.
     * @param newArguments  the concepts related by this relation.
     */
  public Relation(RelationType newType, Concept newArguments[])
    {
    this(newType, newArguments, newArguments.length - 1);
    }

    /**
     * Constructs a relation with the given type and arguments and output argument start 
     * index.
     * The number of arguments must conform to the valence of the
     * relation type unless it is unspecified in which case the valence
     * is set to the number of arguments in the array.
     *
     * @param newType  the type for this relation.
     * @param newArguments  the concepts related by this relation.
     * @param newOutputStartIndex  the index into the newArguments array at which the
     * output arcs start.
     */
  public Relation(RelationType newType, Concept newArguments[], int newOutputStartIndex)
    {
    int valence;

    if (newType == null)
    	throw new java.lang.IllegalArgumentException("Null type not allowed.");

    if (newArguments == null)
    	throw new java.lang.IllegalArgumentException("Null argument array not allowed.");

		valence = newType.getValence();

		if (valence != -1)
			if (newArguments.length != valence)
				throw new java.lang.IllegalArgumentException("Argument array length must be the same as the specified type's valence.");

    setNodeType(newType);
    arguments = new Concept[newArguments.length];
    System.arraycopy(newArguments, 0, arguments, 0, newArguments.length);
    
    setOutputStartIndex(newOutputStartIndex);
    }

    /**
     * Constructs a relation with the given type and the specified input and output arguments.
     * The number of arguments must conform to the valence of the
     * relation type unless it is unspecified in which case the valence
     * is set to the number of arguments in the array.
     *
     * @param newType  the type for this relation.
     * @param newInputArguments  the input concepts related by this relation.
     * @param newOutputArguments  the output concepts related by this relation.
     */
  public Relation(RelationType newType, Concept newInputArguments[], Concept newOutputArguments[])
    {
    int valence;

    if (newType == null)
    	throw new java.lang.IllegalArgumentException("Null type not allowed.");

    if (newInputArguments == null)
    	throw new java.lang.IllegalArgumentException("Null input argument array not allowed.");

    if (newOutputArguments == null)
    	throw new java.lang.IllegalArgumentException("Null output argument array not allowed.");

		valence = newType.getValence();

		if (valence != -1)
			if ((newInputArguments.length + newOutputArguments.length) != valence)
				throw new java.lang.IllegalArgumentException("Sum of argument array lengths must be the same as the specified type's valence.");

    setNodeType(newType);
    arguments = new Concept[newInputArguments.length + newOutputArguments.length];
    System.arraycopy(newInputArguments, 0, arguments, 0, newInputArguments.length);
    System.arraycopy(newOutputArguments, 0, arguments, newInputArguments.length, newOutputArguments.length);
    
    setOutputStartIndex(newInputArguments.length);
    }

    /**
     * Constructs a relation with the given type.
     * The valence is taken from the specified type and the arguments 
     * are initialized to null.  If the type's valence is unspecified, 
     * a zero-length argument array is used for construction.
     * For a specified non-zero valence, the last argument is assumed to be the
     * sole output argument.
     *
     * @param newType  the type for this relation.
     */
  public Relation(RelationType newType)
    {
    int valence;
    
    if (newType == null)
    	throw new java.lang.IllegalArgumentException("Null type argument not allowed.");
    
    setNodeType(newType);
    valence = newType.getValence();

    if (valence == -1)
    	{
    	arguments = new Concept[0];
    	outputStartIndex = -1;
    	}
    else
    	{
    	arguments = new Concept[valence];    
    	outputStartIndex = valence - 1;
    	}
    }

    /**
     * Constructs a relation with no type and the specified arguments with output arguments
     * starting at the specified index.
     *
     * @param newArguments  the concepts related by this relation.
     * @param newOutputStartIndex  the index into the newArguments array at which the
     * output arcs start.
     */
  public Relation(Concept newArguments[], int newOutputStartIndex)
    {
    if (newArguments == null)
    	throw new java.lang.IllegalArgumentException("Null argument array not allowed.");

    arguments = new Concept[newArguments.length];
    System.arraycopy(newArguments, 0, arguments, 0, newArguments.length);

   	setOutputStartIndex(newOutputStartIndex);
    }

    /**
     * Constructs a relation with no type and the specified arguments.
     * For a non-zero valence, the last argument is assumed to be the
     * sole output argument.
     *
     * @param newArguments  the concepts related by this relation.
     */
  public Relation(Concept newArguments[])
    {
    if (newArguments == null)
    	throw new java.lang.IllegalArgumentException("Null argument array not allowed.");

    arguments = new Concept[newArguments.length];
    System.arraycopy(newArguments, 0, arguments, 0, newArguments.length);

   	outputStartIndex = arguments.length - 1;
    }

    /**
     * Constructs a new relation that has no type and no arguments.
     * The number of arguments is automatically set to zero.
     *
     * @bug Should the default be monadic rather than 0-adic?
     */
  public Relation()
    {
    setNodeType(null);
    arguments = new Concept[0];
    outputStartIndex = -1;
    }

    /**
     * Returns this relation's type.
     *
     * @return this relation's type.
     */
  public RelationType getType()
    {
    return (RelationType)getNodeType();
    }

    /**
     * Returns all of the concepts related by this relation.
     * Input arguments are followed by output arguments.
     * In a 'classic' relation, the last argument is always the sole output argument.
     * If the relation has a type whose valence has changed since its
     * definition, the arguments will not be adjusted.  A call to 
     * isComplete() should be made to effect this adjustment.
     *
     * @return the arguments to this relation.
     */
  public Concept[] getArguments()
    {
    Concept arr[];
    
    arr = new Concept[arguments.length];
    System.arraycopy(arguments, 0, arr, 0, arguments.length);

    return arr;
    }
    
    /**
     * Returns the input arguments for this relation.
     * If the relation has a type whose valence has changed since its
     * definition, the arguments will not be adjusted.  A call to 
     * isComplete() should be made to effect this adjustment.
     *
     * @return an array, possibly empty, containing input arguments for this relation.
     */
  public Concept[] getInputArguments()
    {
    Concept arr[];

		if (outputStartIndex == -1)
			return new Concept[0];

    arr = new Concept[outputStartIndex];
    System.arraycopy(arguments, 0, arr, 0, outputStartIndex);

    return arr;
    }

    /**
     * Returns the output arguments for this relation.
     * If the relation has a type whose valence has changed since its
     * definition, the arguments will not be adjusted.  A call to 
     * isComplete() should be made to effect this adjustment.
     *
     * @return an array, possibly empty, containing output arguments for this relation.
     */
  public Concept[] getOutputArguments()
    {
    Concept arr[];

		if (outputStartIndex == -1)
			return new Concept[0];

    arr = new Concept[arguments.length - outputStartIndex];
    System.arraycopy(arguments, outputStartIndex, arr, 0, arguments.length - outputStartIndex);

    return arr;
    }

    /**
     * Returns true if the specified concept is an argument of this relation.
     *
     * @param concept  the concept being checked for.
     * @return true if the specified concept is an argument of this relation.
     */
  public boolean relatesConcept(Concept concept)
    {
    for (int arg = 0; arg < arguments.length; arg++)
    	if (arguments[arg] == concept)
    		return true;
    		
    return false;
    }

		/**
		 * Sets the type for this relation.
		 * If a type has already been set, it will be replaced.
		 * If the new type has a valence that is different from the previous type 
		 * and arguments have already been specified
		 * for this relation, the arguments will be updated as if isComplete() had been called.
		 *
		 * @param newType  the new type for this relation.
		 *
		 * @see notio.Relation#isComplete
		 */
	public void setType(RelationType newType)
		{
		setNodeType(newType);
		adjustToMatchTypeValence();
		}

    /**
     * Replaces all occurrences of the specified argument with a new one regardless of 
     * whether it is an input or output argument.
     * Replacing nulls with non-nulls or vice-versa is allowed.
     *
     * @param oldConcept  the concept being replaced.
     * @param newConcept  the replacement concept.
     */
  public void replaceArgument(Concept oldConcept, Concept newConcept)
    {
    int numArgs;

    numArgs = arguments.length;
    for(int arg = 0; arg < numArgs; arg++)
      if (arguments[arg] == oldConcept)
        {
        arguments[arg] = newConcept;
        }
    }

    /**
     * Replaces all occurrences of the specified input argument with a new one.
     * Replacing nulls with non-nulls or vice-versa is allowed.
     * Replacing an argument with null effectively removes that argument.
     *
     * @param oldConcept  the input argument being replaced.
     * @param newConcept  the replacement argument.
     */
  public void replaceInputArgument(Concept oldConcept, Concept newConcept)
    {
		if (outputStartIndex == -1)
			return;

    for(int arg = 0; arg < outputStartIndex; arg++)
      if (arguments[arg] == oldConcept)
        {
        arguments[arg] = newConcept;
        }
    }
    
    /**
     * Replaces all occurrences of the specified output argument with a new one.
     * Replacing nulls with non-nulls or vice-versa is allowed.
     * Replacing an argument with null effectively removes that argument.
     *
     * @param oldConcept  the output argument being replaced.
     * @param newConcept  the replacement argument.
     */
  public void replaceOutputArgument(Concept oldConcept, Concept newConcept)
    {
    int numArgs;

		if (outputStartIndex == -1)
			return;

    numArgs = arguments.length;
    for(int arg = outputStartIndex; arg < numArgs; arg++)
      if (arguments[arg] == oldConcept)
        {
        arguments[arg] = newConcept;
        return;
        }
    }

		/**
		 * Adjusts the number of arguments in this relation to match the
		 * valence of the relation's type (if any).  If they already match
		 * nothing is changed.
		 *
		 * @return true if an adjustment was made to the arguments, false otherwise.
		 */
	private boolean adjustToMatchTypeValence()
		{
    RelationType type;
    int valence;
    int numArgs;
    Concept newArgs[];
    
    type = getType();
    
    if (type == null)
    	return false;
    	
    valence = type.getValence();
    
    if (valence == -1)
    	return false;
        	
    numArgs = arguments.length;
    
    if (numArgs == valence)
    	return false;
    	    	
		// Increase or decrease the length of the arguments array to match the type's valence.
		newArgs = new Concept[valence];
		System.arraycopy(arguments, 0, newArgs, 0, Math.min(valence, arguments.length));
		arguments = newArgs;
		
		return true;
		}

    /**
     * Sets the specified argument to the specified concept.
     * The index of the first argument (often labelled "1" in diagrams) is zero.
     * If the argument has already been set, it will be replaced
     * with the new value.  If the valence of the relation type is
     * defined and the specified index exceeds that valence, a
     * IllegalArgumentException is thrown.  If the valence is undefined and
     * a previously unused argument index is specified, the valence of this
     * particular relation will be increased to accomodate the index.  No other
     * relations nor the relation's type are affected by this increase.
     * All currently specified arguments are preserved and any newly created
     * but unspecified arguments are set to null.
     * If the valence of the relation's type has changed since the arguments 
     * were specified, the arguments will be adjusted as if isComplete() had been
     * called.
     * Setting an argument to null effectively removes that argument.
     *
     * @param index  the index of the argument being set.
     * @param newConcept  the concept to be used as an argument.
     *
     * @bug What about adjusting if there is a null type?
     */
  public void setArgument(int index, Concept newConcept) 
    {
    RelationType type;
    int valence;
    Concept newArgs[];
    
    if (index < 0)
    	throw new java.lang.IllegalArgumentException("Argument index out of range: "+index);
    
    type = getType();
    
    if (type != null)
    	{
    	valence = type.getValence();
    	}
    else
    	valence = -1;
    	
   	if (valence == -1)
   		{
   		// Valence is undefined so we can freely expand the number of arguments
   		if (index >= arguments.length)
   			{
   			// Increase the length of the arguments array to allow for the index.
   			newArgs = new Concept[index + 1];
   			System.arraycopy(arguments, 0, newArgs, 0, arguments.length);
   			arguments = newArgs;
   			}
  		}
   	else
   		{
   		// Valence is defined so we must respect the number of arguments
			if (index >= valence)
   			throw new java.lang.IllegalArgumentException("Argument index out of range: " + index);

   		// This will only do something if the type's valence is changed after this relation has
   		// been constructed.
   		adjustToMatchTypeValence();
   		}
    		
    arguments[index] = newConcept;
    }
    
    /**
     * Sets the arguments according to the specified array of concepts.
     * If an argument has already been set, it will be replaced
     * with the new value.  If the valence of the relation type is
     * defined and the number of arguments in the array exceeds that valence, a
     * IllegalArgumentException is thrown.  Otherwise, this method behaves like a 
     * series of calls to setArgument(), with the array index used as the argument's index.
     *
     * @param newConcepts  the array of concepts to be used as arguments.
     *
     * @see notio.Relation#setArgument
     */
  public void setArguments(Concept newConcepts[]) 
    {
    RelationType type;
    int valence; 
    
    if (newConcepts == null)
    	throw new IllegalArgumentException("Array of concepts can not be null.");
    	
    type = getType();	
    
    if (type != null)
    	{
    	valence = type.getValence();
    	}
    else
    	valence = -1;
    	
    if ((valence != -1) && (newConcepts.length > valence))
    	throw new IllegalArgumentException("Array of concepts can not be null.");
    	
    for (int con = 0; con < newConcepts.length; con++)
    	setArgument(con, newConcepts[con]);
    }
    
    /**
     * Sets the specified argument to the specified concept.
     * If the argument has already been set, it will be replaced
     * with the new value.  If the valence of the relation type is
     * defined and the specified index exceeds that valence, a
     * IllegalArgumentException is thrown.  If the valence is undefined and
     * a previously unused argument index is specified, the valence of this
     * particular relation will be increased to accomodate the index.  No other
     * relations nor the relation's type are affected by this increase.
     * All currently specified arguments are preserved and any newly created
     * but unspecified arguments are set to null.
     * If the valence of the relation's type has changed since the arguments 
     * were specified, the arguments will be adjusted as if isComplete() had been
     * called.
     * Setting an argument to null effectively removes that argument.
     *
     * @param index  the index of the input argument being set (the nth input argument).
     * @param newConcept  the concept to be used as an argument.
     *
     * @bug What about adjusting if there is a null type?
     * @bug What if the index is out of range of the input args?
     */
  public void setInputArgument(int index, Concept newConcept) 
  	{
  	// Since input args come first, we can simply call setArgument().
		setArgument(index, newConcept);  	
  	}
  	
    /**
     * Sets the specified argument to the specified concept.
     * If the argument has already been set, it will be replaced
     * with the new value.  If the valence of the relation type is
     * defined and the specified index exceeds that valence, a
     * IllegalArgumentException is thrown.  If the valence is undefined and
     * a previously unused argument index is specified, the valence of this
     * particular relation will be increased to accomodate the index.  No other
     * relations nor the relation's type are affected by this increase.
     * All currently specified arguments are preserved and any newly created
     * but unspecified arguments are set to null.
     * If the valence of the relation's type has changed since the arguments 
     * were specified, the arguments will be adjusted as if isComplete() had been
     * called.
     * Setting an argument to null effectively removes that argument.
     *
     * @param index  the index of the output argument being set (the nth output argument).
     * @param newConcept  the concept to be used as an argument.
     *
     * @bug What about adjusting if there is a null type?
     * @bug What if the index is out of range of the output args?
     */
  public void setOutputArgument(int index, Concept newConcept) 
  	{
  	// Call setArgument() with appropriate offset.
		setArgument(outputStartIndex + index, newConcept);  	
  	}
  	
  	/**
  	 * Returns the index of the first output argument within the array returned by
  	 * getArguments().  This method will return -1 if the relation has a valence of
  	 * zero.  If there are inputs, but no outputs, the output start index will be 1 
  	 * greater than the number of inputs.  This means that any code using the output 
  	 * start index to index into the array returned by getArguments() should check first
  	 * to see if the array is long enough.
  	 *
  	 * @return the index of the first output argument within the array returned by
  	 * getArguments().
  	 *
  	 * @see notio.Relation#getArguments()
  	 */
  public int getOutputStartIndex()
  	{
  	return outputStartIndex;
  	}

  	/**
  	 * Sets the index of the first output argument within the array returned by
  	 * getArguments().  For relations with a valence of zero, the only valid start
  	 * index is -1.  For all other valences, the start index must be within the array's
  	 * range unless there are no outputs, in which case the index may be 1 greater than
  	 * the length of the array.  Note that this method should rarely be needed since the 
  	 * start index
  	 * can be specified in a constructor and it is unlikely to change.  This method is
  	 * provided chiefly for completeness.
  	 *
  	 * @param newOutputStartIndex  the index of the first output argument within the array 
  	 * returned by getArguments().
  	 *
  	 * @see notio.Relation#getArguments()
  	 */
  public void setOutputStartIndex(int newOutputStartIndex)
  	{
  	adjustToMatchTypeValence();

  	// Allow -1 in the case of 0-adic relations
  	if (newOutputStartIndex < 0)
  		if ((arguments.length != 0) || (newOutputStartIndex != -1))
	 			throw new java.lang.IllegalArgumentException("Argument index out of range: " + newOutputStartIndex);
  		
  	if (newOutputStartIndex > arguments.length)
 			throw new java.lang.IllegalArgumentException("Argument index out of range: " + newOutputStartIndex);
  	
  	outputStartIndex = newOutputStartIndex;
  	}

    /**
     * This method returns the valence (number of arguments) that this relation has defined.
     * This number includes null arguments.  If this relation's type has a defined valence,
     * the number returned by this method will equal the valence of the type.
     * 
     * @return the number of arguments this relation has defined (including nulls).
     */
  public int getValence()
    {
		adjustToMatchTypeValence();
		
		return arguments.length;
		}   

    /**
     * Returns true if this relation's arguments are completely specified
     * (are all non-null).  If the relation has a type, its valence will be
     * checked to ensure that the relation is complete according to the type.
     * This check is made in case the valence of the type has changed.  If it
     * has changed, the arguments will be adjusted accordingly.
     * 
     * @return true if this relation's arguments are all non-null.
     */
  public boolean isComplete()
    {
    int numArgs;
    
		adjustToMatchTypeValence();

    numArgs = arguments.length;
 
    for (int arg = 0; arg < numArgs; arg++)
    	if (arguments[arg] == null)
    		return false;
    		
    return true;
    }
    
    
    /**
     * Performs a copy operation on this relation according to the
     * the specified CopyingScheme.
     * The result may be a new node or simply a reference to this relation
     * depending on the scheme.
     * 
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @return the result of the copy operation.
     */
  public Relation copy(CopyingScheme copyScheme)
    {
    return copy(copyScheme, new Hashtable());
    }
    
    /**
     * Performs a copy operation on this relation according to the
     * the specified CopyingScheme.
     * The result may be a new node or simply a reference to this relation
     * depending on the scheme.
     * 
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @param substitutionTable  a hashtable containing copied objects available due to 
     * earlier copy operations.
     * @return the result of the copy operation.
     */
  public Relation copy(CopyingScheme copyScheme, Hashtable substitutionTable)
    {
    switch (copyScheme.getRelationFlag())
    	{
    	case CopyingScheme.RN_COPY_DUPLICATE:
    		{
    		Relation newRelation = null;

				// Call isComplete() to ensure that valence is matched.
				isComplete();

    		// Create a new relation with the same type and arguments 
    		// (args may be changed below).
    		// Check to see if it's an actor and create an actor instead if appropriate.
    		// This next section is redundant but it's possible we'll want to do some extra 
    		// or different stuff with actors.
    		if (this instanceof Actor)
    			{
    			try
    				{
    				newRelation = (Relation)this.getClass().newInstance();
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

			    newRelation.setType(getType());
			    newRelation.setArguments(getArguments());
			    }
    		else
    			{
    			try
    				{
    				newRelation = (Relation)this.getClass().newInstance();
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

			    newRelation.setType(getType());
			    newRelation.setArguments(getArguments());
			    }

		    // Copy the comment if the scheme requires it
    		if (copyScheme.getCommentFlag() == CopyingScheme.COMM_COPY_ON)
    			{
	     		newRelation.setComment(getComment());
	     		}
    		
		    // Argument copying is based on how concepts are to be copied.
		    switch (copyScheme.getConceptFlag())
		    	{
		    	// If concepts are set to be duplicated
		    	case CopyingScheme.CN_COPY_DUPLICATE:
		    		{
			    	// Call copy in all args in case any haven't been duplicated yet
		    		for (int arg = 0; arg < arguments.length; arg++)
		    			if (arguments[arg] != null)
			    			arguments[arg].copy(copyScheme, substitutionTable);
		    	
		    		// Perform the argument subsititution
				    newRelation.substituteArguments(substitutionTable);
				    
				    newRelation.setOutputStartIndex(getOutputStartIndex());
				    
				    break;
						}

		    	// If concepts are set to be referenced
		    	case CopyingScheme.CN_COPY_REFERENCE:
		    		{
		    		// Don't need to do anything since the argument array already contains
		    		// the required references.
		    		break;
		    		}
		    		
	  	  	default:
  	  	    throw new UnimplementedFeatureException("Specified Concept copy control flag is unknown.");
					}
					
    		return newRelation;
    		}

    	case CopyingScheme.RN_COPY_REFERENCE:
    		{
    		return this;
    		}
    		
    	default:
        throw new UnimplementedFeatureException("Specified Relation copy control flag is unknown.");    		    	
      }
    }

    /**
     * Replaces all arguments of this Relation with their equivalent in the substitution
     * table.  If there is no entry in the table for a given argument, a null will be
     * substituted.
     *
     * @param substitutionTable  the table used to find substitutes.
     *
     * @bug Should we expose this in the API?
     */
  void substituteArguments(Hashtable substitutionTable)
    {
    int numArgs;

    numArgs = arguments.length;
    for(int arg = 0; arg < numArgs; arg++)
      arguments[arg] = (Concept)substitutionTable.get(arguments[arg]);
    }

     /**
      * Returns a new node identical to this but restricted to the new type.
      *
      * @param subType  the type to be restricted to.
      * @return the new restricted relation.
      * @exception notio.RestrictionException
      *            if subType is not a real subtype of the current type
      */
  public Relation restrictTo(RelationType subType) throws RestrictionException
    {
    if (!subType.hasSuperType(getType()))
      throw new RestrictionException("Specified restriction type is not a subtype of the current type.");

    return new Relation(subType, getArguments());
    }

    /**
     * Compares two relations to decide if they match.  The exact semantics of matching
     * are determined by the matching scheme.
     *
     * @param first  the first relation being matched.
     * @param second  the second relation being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two concepts match according to the scheme's criteria.
     */
  public static boolean matchRelations(Relation first, Relation second, 
    MatchingScheme matchingScheme)
    {
    switch (matchingScheme.getRelationFlag())
      {
      case MatchingScheme.RN_MATCH_INSTANCE:
        return first == second;
        
      case MatchingScheme.RN_MATCH_TYPES:
        return RelationType.matchRelationTypes(first.getType(), second.getType(), matchingScheme);

      case MatchingScheme.RN_MATCH_ARCS:
        return matchArcs(first, second, matchingScheme);

      case MatchingScheme.RN_MATCH_ALL:
        if (!RelationType.matchRelationTypes(first.getType(), second.getType(), matchingScheme))
          return false;
        return matchArcs(first, second, matchingScheme);        

      case MatchingScheme.RN_MATCH_ANYTHING:
        return true;
      
      default:
        throw new UnimplementedFeatureException("Specified Relation match control flag is unknown.");
      }
    }

    /**
     * Compares two relations to decide if they match.  The exact semantics of matching
     * are determined by the matching scheme.
     *
     * @param first  the first relation being matched.
     * @param second  the second relation being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two concepts match according to the scheme's criteria.
     *
     * @impspec Should this be part of the API?
     */
  static boolean matchArcs(Relation first, Relation second, MatchingScheme matchingScheme)
    {
    Concept firstArgs[], secondArgs[];
    
    switch (matchingScheme.getArcFlag())
      {
      case MatchingScheme.ARC_MATCH_INSTANCE:
        firstArgs = first.getArguments();
        secondArgs = second.getArguments();
        if (firstArgs.length != secondArgs.length)
          return false;
        for (int arc = 0; arc < firstArgs.length; arc++)
          if (firstArgs[arc] != secondArgs[arc])
            return false;
        return true;    

      case MatchingScheme.ARC_MATCH_CONCEPT:
        firstArgs = first.getArguments();
        secondArgs = second.getArguments();
        if (firstArgs.length != secondArgs.length)
          return false;
        for (int arc = 0; arc < firstArgs.length; arc++)
          if (!Concept.matchConcepts(firstArgs[arc], secondArgs[arc], matchingScheme).matchSucceeded())
            return false;
        return true;    

      case MatchingScheme.ARC_MATCH_VALENCE:
        return first.getArguments().length == second.getArguments().length;
            
      case MatchingScheme.ARC_MATCH_ANYTHING:
        return true;

      default:
        throw new UnimplementedFeatureException("Specified Arc match control flag is unknown.");
      }
    }
  }
