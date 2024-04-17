package notio;

import java.io.Serializable;

    /** 
     * The relation type class.
     * This class encapsulates all available information about a relation type.
     * The type can be defined by a label and/or a type definition or valence.
     * Relation types provide the type information for both the relations and actors.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.37 $, $Date: 1999/08/04 04:04:12 $
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
public class RelationType extends Type implements Serializable
  {
	  /** The type label for this type. **/
  private String label;
  
  	/** The definition of this relation type. **/
  private RelationTypeDefinition typeDefinition;
  
  	/** The valence of this relation. **/
  private int valence = -1;
  
  	/** The comment associated with this type. **/
  private String comment;

    /**
     * Constructs a labelled RelationType with the specified type label and type definition.
     * The valence of the type is automatically determined from the type definition.
     * Note that this method does not add the type to any hierarchy.
     *
     * @param newLabel  the type label for this type.
     * @param newDefinition  the type definition for this type.
     */
  public RelationType(String newLabel, RelationTypeDefinition newDefinition)
    {
    label = newLabel;
    typeDefinition = newDefinition;
    if (typeDefinition != null)
	    valence = typeDefinition.getValence();
	  else
	  	valence = -1;
    }

    /**
     * Constructs a labelled RelationType with the specified type label and valence.
     * Note that this method does not add the type to any hierarchy.
     *
     * @param newLabel  the type label for this type.
     * @param newValence  the valence for this type.
     */
  public RelationType(String newLabel, int newValence)
    {
    label = newLabel;
    valence = newValence;
    }

    /**
     * Constructs a labelled RelationType with the specified type label and no type definition.
     * The valence of the relation is automatically set to be unspecified.
     * Note that this method does not add the type to any hierarchy.
     *
     * @param newLabel  the type label for this type.
     */
  public RelationType(String newLabel)
    {
    label = newLabel;
    valence = -1;
    }

    /**
     * Constructs an unlabelled RelationType with the specified type definition.
     * The valence of the type is automatically determined from the type definition.
     * Note that this method does not add the type to any hierarchy.
     *
     * @param newDefinition  the type definition for this type.
     */
  public RelationType(RelationTypeDefinition newDefinition)
    {
    this(null, newDefinition);
    }

    /**
     * Constructs a RelationType with no label or type definition.
     * The valence of the relation is automatically set to be unspecified.
     * Note that this method does not add the type to any hierarchy.
     *
     * @bug Must check whether undefined/unlabelled types break anything.
     */
  public RelationType()
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
     * Specifying a type definition will override any existing valence 
     * information with the valence of the type definition.
     *
     * @param newDefinition  the new type definition for this type.
     */
  public void setTypeDefinition(RelationTypeDefinition newDefinition)
    {
    typeDefinition = newDefinition;
    valence = typeDefinition.getValence();
    }
  
    /**
     * Returns the relation type definition for this type (if any).
     *
     * @return the type definition for this type or null if there isn't one.
     */
  public RelationTypeDefinition getTypeDefinition()
    {
    return typeDefinition;
    }

    /**
     * Sets the valence for this type.  A value of -1 indicates that the valence is undefined.
     * The valence is the number of arcs (or arguments) that this relation type possesses.
     * Changing the valence has no effect on existing relations using this type except that
     * they many return a different value when their isComplete() method is called.
     * It is up to the application to ensure that all such relations are corrected by the
     * additional or removal of arguments.  In most cases, it is expected that this method
     * will be used to change a valence from unspecified to a specific value, rather than
     * from one specific value to another.
     *
     * @param newValence  the new valence for this type.
     * @exception notio.TypeChangeError
     *   if a type definition has been specified for this type.
     * @see notio.Relation#isComplete()
     */
  public void setValence(int newValence)
    {
    if (typeDefinition != null)
    	throw new TypeChangeError("Cannot specify a valence for a type that has a type definition.");
    valence = newValence;
    }

    /**
     * Returns the valence for this type, or -1 if the valence is undefined.
     *
     * @return the valence for this type, or -1 if the valence is undefined.
     */
  public int getValence()
    {
    return valence;
    }

    /**
     * Returns all subtypes of this type.
     *
     * @return an array of subtypes of this type.
     */
  public RelationType[] getProperSubTypes()
    {
    return ((RelationTypeHierarchy)hierarchy).getProperSubTypesOf(this);
    }

    /**
     * Returns all supertypes of this type.
     *
     * @return an array of supertypes of this type.
     */
  public RelationType[] getSuperProperTypes()
    {
    return ((RelationTypeHierarchy)hierarchy).getProperSuperTypesOf(this);
    }

    /**
     * Returns the immediate subtypes of this type.
     *
     * @return an array of immediate subtypes of this type.
     */
  public RelationType[] getImmediateSubTypes()
    {
    return ((RelationTypeHierarchy)hierarchy).getImmediateSubTypesOf(this);
    }

    /**
     * Returns the immediate supertypes of this type.
     *
     * @return an array of immediate supertypes of this type.
     */
  public RelationType[] getImmediateSuperTypes()
    {
    return ((RelationTypeHierarchy)hierarchy).getImmediateSuperTypesOf(this);
    }

    /**
     * Tests whether the specified type is a subtype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a subtype of this type, false otherwise.
     */
  public boolean hasSubType(RelationType queryType)
    {
    return ((RelationTypeHierarchy)hierarchy).isSubTypeOf(queryType, this);
    }

    /**
     * Tests whether the specified type is a supertype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a supertype of this type, false otherwise.
     */
  public boolean hasSuperType(RelationType queryType)
    {
    return ((RelationTypeHierarchy)hierarchy).isSuperTypeOf(queryType, this);
    }

    /**
     * Tests whether the specified type is a proper subtype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a proper subtype of this type, false otherwise.
     */
   public boolean hasProperSubType(RelationType queryType)
    {
    return ((RelationTypeHierarchy)hierarchy).isProperSubTypeOf(queryType, this);
    }

    /**
     * Tests whether the specified type is a proper supertype of this type.
     *
     * @param queryType  the type being tested.
     * @return true if queryType is a proper supertype of this type, false otherwise.
     */
  public boolean hasProperSuperType(RelationType queryType)
    {
    return ((RelationTypeHierarchy)hierarchy).isProperSuperTypeOf(queryType, this);
    }
    
    /**
     * Compares two relation types to decide if they match.  The exact semantics of matching
     * are determined by the match control flag.
     *
     * @param first  the first relation type being matched.
     * @param second  the second relation type being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two relation types match according to the scheme's criteria.
     * @bug Should really check to see if the two types belong to the same hierarchy, no?
     */
  public static boolean matchRelationTypes(RelationType first, RelationType second, 
    MatchingScheme matchingScheme)
    {
    switch (matchingScheme.getRelationTypeFlag())
      {
      case MatchingScheme.RT_MATCH_INSTANCE:
        return first == second;
        
      case MatchingScheme.RT_MATCH_LABEL:
      	{
        if (first == second)
          return true;
          
 	      if (((RelationTypeHierarchy)first.getHierarchy()).getCaseSensitiveLabels())
	        return first.getLabel().equals(second.getLabel());
	      else
	        return (first.getLabel().toLowerCase()).equals(second.getLabel().toLowerCase());
        }

      case MatchingScheme.RT_MATCH_SUBTYPE:
        return second.hasSubType(first);
        
      case MatchingScheme.RT_MATCH_SUPERTYPE:
        return second.hasSuperType(first);
        
      case MatchingScheme.RT_MATCH_EQUIVALENT:
        return second.hasSuperType(first) || second.hasSubType(first);

      case MatchingScheme.RT_MATCH_ANYTHING:
        return true;
      
      default:
        throw new UnimplementedFeatureException("Specified RelationType match control flag is unknown.");
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
