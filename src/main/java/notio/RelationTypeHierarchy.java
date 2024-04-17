package notio;

import java.util.*;
import java.io.Serializable;

    /** 
     * The relation type hierarchy class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.25 $, $Date: 1999/08/04 04:15:12 $
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

public class RelationTypeHierarchy extends TypeHierarchy
                                   implements Serializable
  {
   /** The predefined type label for the universal type. **/
  public static final String UNIVERSAL_TYPE_LABEL = "Universal";

  /** The predefined type label for the absurd type. **/
  public static final String ABSURD_TYPE_LABEL = "Absurd";

    /**
     * Constructs a new RelationTypeHierarchy.
     */
  public RelationTypeHierarchy()
    {
    super(new RelationType(UNIVERSAL_TYPE_LABEL), new RelationType(ABSURD_TYPE_LABEL));
    }

    /**
     * Adds a new type to the hierarchy with the specified supertypes and
     * subtypes.
     *
     * @param newType  the type being added.
     * @param supertypes  the array of supertypes to be used, or null (assumes
     *                 universal type as only supertype).
     * @param subtypes  the array of subtypes to be used, or null (assumes
     *                  absurd type as only subtype).
     * @exception notio.TypeAddError
     *            if type label is already in use by another type.
     * @exception notio.TypeChangeError
     *            if specified supertypes or subtypes are not in hierarchy, or
     *            if addition of type creates a type order conflict.
     */
  public void addTypeToHierarchy(RelationType newType, RelationType supertypes[], RelationType subtypes[]) throws TypeAddError, TypeChangeError
    {
    super.addTypeToHierarchy(newType, supertypes, subtypes);
    }
 
    /**
     * Adds a new type to this hierarchy with the specified supertype and
     * subtype.
     *
     * @param newType  the type being added.
     * @param supertype  the single supertype to be used, or null (assumes
     *                 universal type as subtype).
     * @param subtype  the single subtype to be used, or null (assumes
     *                  absurd type as subtype).
     * @exception notio.TypeAddError
     *            if type label is already in use by another type.
     * @exception notio.TypeChangeError
     *            if specified supertypes or subtypes are not in hierarchy, or
     *            if addition of a type creates a type order conflict.
     */
  public void addTypeToHierarchy(RelationType newType, RelationType supertype, RelationType subtype) throws TypeAddError, TypeChangeError
    {
    super.addTypeToHierarchy(newType, supertype, subtype);
    }

    /**
     * Adds a new type to this hierarchy with the Universal type
     * as its only supertype, and the Absurd type as its only subtype.
     * Note that unlike the other versions of this method, this does
     * not throw notio.TypeChangeError since the Universal and Absurd
     * types are always present in the hierarchy.
     *
     * @param newType  the type being added.
     * @exception notio.TypeAddError
     *            if type label is already in use by another type.
     */
  public void addTypeToHierarchy(RelationType newType) throws TypeAddError
    {
    super.addTypeToHierarchy(newType);
    }

    /**
     * Adds the specified list of supertypes as super types to the subject type.
     *
     * @param subjectType  the type having supertypes added.
     * @param newSuperTypes  the array of types to be added as supertypes.
     * @exception notio.TypeChangeError
     *            if specified supertypes are not in hierarchy, or
     *            if addition of supertypes creates a type order conflict.
     */
  public void addSuperTypesToType(RelationType subjectType, RelationType newSuperTypes[]) throws TypeChangeError
    {
    super.addSuperTypesToType(subjectType, newSuperTypes);
    }
 
    /**
     * Adds the specified supertype to the subject type.
     *
     * @param subjectType  the type having a supertype added.
     * @param newSuperType  the supertype to be added.
     * @exception notio.TypeChangeError
     *            if specified supertype is not in hierarchy or,
     *            if addition of the supertype creates a type order conflict.
     */
  public void addSuperTypeToType(RelationType subjectType, RelationType newSuperType) throws TypeChangeError
  	{
  	super.addSuperTypeToType(subjectType, newSuperType);
  	}
  	
    /**
     * Adds the specified list of subtypes as sub types to the subject type.
     *
     * @param subjectType  the type having subtypes added.
     * @param newSubTypes  the array of types to be added as subtypes.
     * @exception notio.TypeChangeError
     *            if specified subtypes are not in hierarchy, or
     *            if addition of subtypes creates a type order conflict.
     */
  public void addSubTypesToType(RelationType subjectType, RelationType newSubTypes[]) throws TypeChangeError
    {
    super.addSubTypesToType(subjectType, newSubTypes);
    }
 
    /**
     * Adds the specified subtype to the subject type.
     *
     * @param subjectType  the type having a subtype added.
     * @param newSubType  the subtype to be added.
     * @exception notio.TypeChangeError
     *            if specified subtype is not in hierarchy or,
     *            if addition of the subtype creates a type order conflict.
     */
  public void addSubTypeToType(RelationType subjectType, RelationType newSubType) throws TypeChangeError
  	{
  	super.addSubTypeToType(subjectType, newSubType);
  	}

    /**
     * Removes a type from this hierarchy.
     * Removing a type from its hierarchy does not remove it from any of the nodes in
     * which it may be used.  This must be done manually if it is necessary.  In most cases
     * it is expected that types are being replaced rather than actually removed entirely.
     * In this case, it is much easier to simply the change the characteristics of the
     * existing type.  If a type is being replaced by more than one type, it will naturally
     * be necessary to perform at least part of the replacement manually.  If replacements
     * must be performed, the utility method replaceTypeInGraph() may be used.
     *
     * @param deadType  the type being removed.
     * @exception notio.TypeRemoveError
     *            if specified type is not in hierarchy
		 * 
     * @see notio.RelationTypeHierarchy#replaceTypeInGraph
     */
  public void removeTypeFromHierarchy(RelationType deadType) throws TypeRemoveError
    {
    super.removeTypeFromHierarchy(deadType);
    }
    
    /**
     * Looks up and returns the type object associated with the specified
     * label.
     *
     * @param label  the label used to lookup.
     * 
     * @return the type associated with the label or null if non-existant.
     */
  public RelationType getTypeByLabel(String label)
    {
    return (RelationType)super.returnTypeByLabel(label);
    }

    /**
     * Returns all unlabelled types in this hierarchy in no particular order.
     *
     * @return an array of the unlabelled types in this hierarchy, possibly empty.
     */
  public RelationType[] getUnlabelledTypes()
  	{
  	Vector unVec;
  	RelationType rArr[];
  	
  	unVec = super.returnUnlabelledTypes();
  	rArr = new RelationType[unVec.size()];
  	unVec.copyInto(rArr);
  	
  	return rArr;
  	}
  
    /**
     * Returns all the supertypes of the subject type, not just the immediate
     * supertypes, in no particular order.
     *
     * @param subjectType  the type whose supertypes will be returned.
     * @return an array of the supertypes of the parent type, possibly empty.
     */
  public RelationType[] getProperSuperTypesOf(RelationType subjectType)
    {
    Set supertypeSet;
    RelationType rTypeArr[];

    supertypeSet = super.returnProperSuperTypesOf(subjectType);

    rTypeArr = new RelationType[supertypeSet.size()];
    supertypeSet.copyInto(rTypeArr);
    
    return rTypeArr;
    }

    /**
     * Returns all the subtypes of the subject type, not just the immediate
     * subtypes, in no particular order.
     *
     * @param subjectType  the type whose subtypes will be returned.
     * @return an array of the subtypes of the subject type, possibly empty.
     */
  public RelationType[] getProperSubTypesOf(RelationType subjectType)
    {
    Set subtypeSet;
    RelationType rTypeArr[];

    subtypeSet = super.returnProperSubTypesOf(subjectType);

    rTypeArr = new RelationType[subtypeSet.size()];
    subtypeSet.copyInto(rTypeArr);
    
    return rTypeArr;
    }
 
    /**
     * Returns the immediate subtypes of the subject type in no particular order.
     *
     * @param subjectType  the type whose immediate subtypes will be returned.
     * @return an array of the immediate subtypes of the subject type, possibly empty.
     */
  public RelationType[] getImmediateSuperTypesOf(RelationType subjectType)
    {
    Object arr[];
    RelationType rTypeArr[];

    arr = super.returnImmediateSuperTypesOf(subjectType);

    rTypeArr = new RelationType[arr.length];
    System.arraycopy(arr, 0, rTypeArr, 0, arr.length);
    
    return rTypeArr;
    }
    
    /**
     * Returns the immediate subtypes of the subject type in no particular order.
     *
     * @param subjectType  the type whose immediate subtypes will be returned.
     * @return an array of the immediate subtypes of the subject type, possibly empty.
     */
  public RelationType[] getImmediateSubTypesOf(RelationType subjectType)
    {
    Object arr[];
    RelationType rTypeArr[];

    arr = super.returnImmediateSubTypesOf(subjectType);

    rTypeArr = new RelationType[arr.length];
    System.arraycopy(arr, 0, rTypeArr, 0, arr.length);
    
    return rTypeArr;
    }
    
    /**
     * Determines whether subject is a subtype of object.
     *
     * @param subject  the potential subtype being tested.
     * @param object  the potential supertype being tested.
     * @return true if subject is a subtype of object.
     */
  public boolean isSubTypeOf(RelationType subject, RelationType object)
    {
    return super.isSubTypeOf(subject, object);
    }

    /**
     * Determines whether subject is a supertype of object.
     *
     * @param subject  the potential supertype being tested.
     * @param object  the potential subtype being tested.
     * @return true if subject is a supertype of object.
     */
  public boolean isSuperTypeOf(RelationType subject, RelationType object)
    {
    return super.isSuperTypeOf(subject, object);
    }
   
    /**
     * Determines whether subject is a subtype of object.
     *
     * @param subject  the potential subtype being tested.
     * @param object  the potential supertype being tested.
     * @return true if subject is a subtype of object.
     */ 
  public boolean isProperSubTypeOf(RelationType subject, RelationType object)
    {
    return super.isProperSubTypeOf(subject, object);
    }
 
   /**
     * Determines whether subject is a proper supertype of object.
     *
     * @param subject  the potential supertype being tested.
     * @param object  the potential subtype being tested.
     * @return true if subject is a supertype of object.
     */
   public boolean isProperSuperTypeOf(RelationType subject, RelationType object)
     {
     return super.isProperSuperTypeOf(subject, object);
     }
   
    /**
     * Sets a flag indicating whether or not the processing of type labels within this
     * hierarchy is case-sensitive.
     *
     * @param flag  the flag setting for case-sensitivity.
     */
  public void setCaseSensitiveLabels(boolean flag)
  	{
  	super.setCaseSensitiveLabels(flag);
  	}

    /**
     * Returns true if the processing of type labels in this hierarchy is case-sensitive.
     *
     * @return true if the processing of type labels in this hierarchy is case-sensitive.
     */
  public boolean getCaseSensitiveLabels()
  	{
  	return super.getCaseSensitiveLabels();
  	}
  	
  	/**
  	 * Traverses the specified graph, replacing all occurrences of the specified type
  	 * with a replacement type.  Either of these types may be null.  If the old type
  	 * is null, untyped relations will be set to the new type.  If the new type is null,
  	 * relations using the old type will become untyped.
  	 * In most cases where a non-null type is replacing a non-null type, it is probably 
  	 * more efficient to modify the old type directly and avoid the use of this method.
  	 *
  	 * @bug This method does nothing at present since it would need a setType() method in
  	 * Relation and that has not yet been decided upon.
  	 */
  public static void replaceTypeInGraph(Graph graph, RelationType oldType, RelationType newType)
  	{
  	Concept concepts[];
  	Relation relations[];
  	
  	concepts = graph.getConcepts();
  	relations = graph.getRelations();

  	for (int rel = 0; rel < relations.length; rel++)
  		{
//  		if (relations[rel].getType().equals(oldType))
//  			relations[rel].setType(newType);
  		}
  	
  	for (int con = 0; con < concepts.length; con++)
  		{
  		if (concepts[con].isContext())
  			replaceTypeInGraph(concepts[con].getReferent().getDescriptor(), oldType, newType);
  		}
  	}
  }
