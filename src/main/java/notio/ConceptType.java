package notio;

import java.io.Serializable;

    /** 
     * The concept type class.
     * This class encapsulates all available information about a concept type.
     * The type can be defined by a label and/or a type definition.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.30 $, $Date: 1999/08/04 03:59:41 $
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
     * @idea Include proper sub/supertype matching?
     */
public class ConceptType extends Type implements Serializable
  {
  	/** The type label for this type. **/
  private String label;
  
	  /** The type definition for this type. **/
  private ConceptTypeDefinition typeDefinition;
  
	  /** The comment associated with this type. **/
  private String comment;

    /**
     * Constructs a labelled ConceptType with the specified type label and type definition.
     * Note that this method does not add the type to any hierarchy.
     *
     * @param newLabel  the type label for this type.
     * @param newDefinition  the type definition for this type.
     */
  public ConceptType(String newLabel, ConceptTypeDefinition newDefinition)
    {
    label = newLabel;
    typeDefinition = newDefinition;
    }

    /**
     * Constructs a labelled ConceptType with the specified type label and no type definition.
     * Note that this method does not add the type to any hierarchy.
     *
     * @param newLabel  the type label for this type.
     */
  public ConceptType(String newLabel)
    {
    this(newLabel, null);
    }

    /**
     * Constructs an unlabelled ConceptType with the specified type definition.
     * Note that this method does not add the type to any hierarchy.
     *
     * @param newDefinition  the type definition for this type.
     */
  public ConceptType(ConceptTypeDefinition newDefinition)
    {
    this(null, newDefinition);
    }

    /**
     * Constructs a ConceptType with no type label or type definition.
     * Note that this method does not add the type to any hierarchy.
     *
     * @bug Must check whether undefined/unlabelled types break anything.
     */
  public ConceptType()
    {
    this(null, null);
    }
    
		/**
	   * Sets the type label for this type.
     * If the type already had a label, it is replaced.
  	 * If a null is used, the label is removed from the type.
	   *
     * @param newLabel  the string that is the label for this type.
     * @exception TypeChangeError  
     *   if the type belongs to a hierarchy and the new label is already in use within it.
	   */
	public void setLabel(String newLabel) throws TypeChangeError
   	{
		if (hierarchy != null)
			hierarchy.updateTypeLabel(this, label, newLabel);
       	
    label = newLabel;
    }

    /**
     * Returns the type label for this type.
     *
     * @return the string that is the label for this type.
     */
   public String getLabel()
     {
     return label;
     }

    /**
     * Sets the type definition for this type.
     * If the type already has a definition, it is replaced.
     * If null is used, the type definition will be removed.
     *
     * @param newDefinition  the new type definition for this type.
     */
  public void setTypeDefinition(ConceptTypeDefinition newDefinition)
    {
    typeDefinition = newDefinition;
    }
  
    /**
     * Returns the type definition for this type (if any).
     *
     * @return the type definition for this type or null if there isn't one.
     */
  public ConceptTypeDefinition getTypeDefinition()
    {
    return typeDefinition;
    }
  
    /**
     * Returns all subtypes of this type.
     *
     * @return an array of subtypes of this type.
     */
  public ConceptType[] getProperSubTypes()
    {
    return ((ConceptTypeHierarchy)hierarchy).getProperSubTypesOf(this);
    }

    /**
     * Returns all supertypes of this type.
     *
     * @return an array of supertypes of this type.
     */
  public ConceptType[] getProperSuperTypes()
    {
    return ((ConceptTypeHierarchy)hierarchy).getProperSuperTypesOf(this);
    }

    /**
     * Returns the immediate subtypes of this type.
     *
     * @return an array of immediate subtypes of this type.
     */
  public ConceptType[] getImmediateSubTypes()
    {
    return ((ConceptTypeHierarchy)hierarchy).getImmediateSubTypesOf(this);
    }

    /**
     * Returns the immediate supertypes of this type.
     *
     * @return an array of immediate supertypes of this type.
     */
  public ConceptType[] getImmediateSuperTypes()
    {
    return ((ConceptTypeHierarchy)hierarchy).getImmediateSuperTypesOf(this);
    }

    /**
     * Tests whether the specified type is a subtype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a subtype of this type, false otherwise.
     */
  public boolean hasSubType(ConceptType queryType)
    {
    return ((ConceptTypeHierarchy)hierarchy).isSubTypeOf(queryType, this);
    }

    /**
     * Tests whether the specified type is a supertype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a supertype of this type, false otherwise.
     */
  public boolean hasSuperType(ConceptType queryType)
    {
    return ((ConceptTypeHierarchy)hierarchy).isSuperTypeOf(queryType, this);
    }

    /**
     * Tests whether the specified type is a proper subtype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a proper subtype of this type, false otherwise.
     */
   public boolean hasProperSubType(ConceptType queryType)
    {
    return ((ConceptTypeHierarchy)hierarchy).isProperSubTypeOf(queryType, this);
    }

    /**
     * Tests whether the specified type is a proper supertype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a proper supertype of this type, false otherwise.
     */
  public boolean hasProperSuperType(ConceptType queryType)
    {
    return ((ConceptTypeHierarchy)hierarchy).isProperSuperTypeOf(queryType, this);
    }

    /**
     * Compares two concept types to decide if they match.  The exact semantics of matching
     * are determined by the match control flag.
     *
     * @param first  the first concept type being matched.
     * @param second  the second concept type being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two concept types match according to the scheme's criteria.
     * @bug Should really check to see if the two types belong to the same hierarchy, no?
     */
  public static boolean matchConceptTypes(ConceptType first, ConceptType second, 
    MatchingScheme matchingScheme)
    {
    switch (matchingScheme.getConceptTypeFlag())
      {
      case MatchingScheme.CT_MATCH_INSTANCE:
        return first == second;
        
      case MatchingScheme.CT_MATCH_LABEL:
      	{
        if (first == second)
          return true;
        
        if (((ConceptTypeHierarchy)first.getHierarchy()).getCaseSensitiveLabels())
	        return first.getLabel().equals(second.getLabel());
	      else
	        return (first.getLabel().toLowerCase()).equals(second.getLabel().toLowerCase());
        }

      case MatchingScheme.CT_MATCH_SUBTYPE:
        return second.hasSubType(first);
        
      case MatchingScheme.CT_MATCH_SUPERTYPE:
        return second.hasSuperType(first);
        
      case MatchingScheme.CT_MATCH_EQUIVALENT:
        return second.hasSuperType(first) || second.hasSubType(first);

      case MatchingScheme.CT_MATCH_ANYTHING:
        return true;
      
      default:
        throw new UnimplementedFeatureException("Specified ConceptType match control flag is unknown.");
      }
    }
    
    /**
     * Sets the comment string for this type.
     *
     * @param newComment  the new comment string for this type.
     */
  public void setComment(String newComment)
    {
    comment = newComment;
    }

    /**
     * Returns the comment string for this type.
     *
     * @return the comment string associated with this type or null.
     */
  public String getComment()
    {
    return comment;
    }        
  }
