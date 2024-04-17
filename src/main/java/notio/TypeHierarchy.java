package notio;

import java.util.*;

import com.google.gson.JsonObject;

import java.io.Serializable;

    /** 
     * The type hierarchy class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.36 $, $Date: 1999/07/01 20:39:44 $
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

class TypeHierarchy implements Serializable
  {
  /** The node for the universal type. **/
  private POSetNode universalNode;
  /** The node for the absurd type. **/
  private POSetNode absurdNode;
  /** The universal type. **/
  private Type universalType;
  /** The absurd type. **/
  private Type absurdType;
  /** The hashtable used for looking up a type's POSetNode by type label. **/
  private Hashtable labelTable = new Hashtable();
  /** The hashtable used for looking up a type's POSetNode by type . **/
  private Hashtable typeTable = new Hashtable();
  /** Flag indicating whether type labels are case sensitive or not. **/
  private boolean caseSensitiveLabels = false;
  /** A vector containing the all unlabelled types. **/
	private Vector unlabelledTypeVec = new Vector();
 
	  public JsonObject toJSON() {
		  JsonObject result = new JsonObject();
		  
		  
		  return result;
	  }

    /**
     * Constructs a type hierarchy with the specified universal and absurd
     * types.
     *
     * @param newUniversal  the universal type for this hierarchy.
     * @param newAbsurd  the absurd type for this hierarchy.
     */
  TypeHierarchy(Type newUniversal, Type newAbsurd)
    {
    universalType = newUniversal;
    absurdType = newAbsurd;
    universalNode = new POSetNode(universalType);
    absurdNode = new POSetNode(absurdType);
    try
      {
      universalNode.addChild(absurdNode);
      }
    catch (OrderConflictException e)
      {
      // This should never happen
      e.printStackTrace();
      System.exit(1);
      }
    
    newUniversal.setHierarchy(this);
    newAbsurd.setHierarchy(this);
      
    labelTable.put(universalType.getLabel(), universalNode);
    labelTable.put(absurdType.getLabel(), absurdNode);
    typeTable.put(universalType, universalNode);
    typeTable.put(absurdType, absurdNode);
    }
 
    /**
     * Adds a new type to this hierarchy with the specified supertypes and
     * subtypes.
     *
     * @param newType  the type being added.
     * @param supertypes  the array of supertype types to be used, or null (assumes
     *                 universal type as only supertype).
     * @param subtypes  the array of subtypes to be used, or null (assumes
     *                  absurd type as only subtype).
     * @exception notio.TypeAddError
     *            if type label is already in use by another type.
     * @exception notio.TypeChangeError
     *            if specified supertypes or subtypes are not in hierarchy, or
     *            if addition of a type creates a type order conflict.
     */
  final void addTypeToHierarchy(Type newType, Type supertypes[], Type subtypes[]) throws TypeAddError, TypeChangeError
    {
    String label;
    Type oldType = null;
    POSetNode oldNode, newNode;

		// Check to see if type is labelled
    label = newType.getLabel();
    
    if (label != null)
    	{
			// Check to see if label is used by different type
 	   	oldNode = (POSetNode)labelTable.get(label);
 	   	if (oldNode != null)
 	    	oldType = (Type)oldNode.getData();

    	if (oldType != null)
      	if (oldType == newType)
        	return;
      	else
        	throw new TypeAddError("Specified type label already in use.");
      }

    newNode = new POSetNode(newType);

		// Add node to type table
		typeTable.put(newType, newNode);

    // Add node to label table if it has a label and to the unlabelled type vector otherwise.
    if (label == null)
    	unlabelledTypeVec.addElement(newType);
    else
	    labelTable.put(label, newNode);
    

		// Now, add the type as though it had no super or subtypes by giving it the absurd and
		// universal types.  All additional types will adjust the hierarchy accordingly.
    try
      {
      newNode.addParent(universalNode);
      }
    catch (OrderConflictException e)
      {
      throw new TypeChangeError("Addition of parent creates an order conflict.", e);
      }
      
    try
      {
      newNode.addChild(absurdNode);
      }
    catch (OrderConflictException e)
      {
      throw new TypeChangeError("Addition of child creates an order conflict.", e);
      }

		// Now add any supertypes specified
		if ((supertypes != null) && (supertypes.length > 0))
    	addSuperTypesToType(newType, supertypes);

		// Now add any subtypes specified
    if ((subtypes != null) && (subtypes.length > 0))
	    addSubTypesToType(newType, subtypes);   

    newType.setHierarchy(this);
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
  final void addTypeToHierarchy(Type newType, Type supertype, Type subtype) throws TypeAddError, TypeChangeError
    {
    Type supers[] = null;
    Type subs[] = null;
    
    if (supertype != null)
    	{
    	supers = new Type[1];
	    supers[0] = supertype;
    	}

    if (subtype != null)
    	{
    	subs = new Type[1];
	    subs[0] = subtype;
    	}
    
    addTypeToHierarchy(newType, supers, subs);
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
  final void addTypeToHierarchy(Type newType) throws TypeAddError
    {
    Type supers[] = null;
    Type subs[] = null;
    
    addTypeToHierarchy(newType, supers, subs);
		}
		
    /**
     * Adds the specified list of supertypes as super types to the subject type.
     *
     * @param subjectType  the type having supertypes added.
     * @param newSuperTypes  the array of types to be added as supertypes.
     * @exception notio.TypeChangeError
     *            if specified supertypes are not in hierarchy or,
     *            if addition of supertypes creates a type order conflict.
     */
  final void addSuperTypesToType(Type subjectType, Type newSuperTypes[]) throws TypeChangeError 
    {
    int numSuperTypes;
    POSetNode supertypeNode, subjectNode;

    subjectNode = (POSetNode)typeTable.get(subjectType);

    if (subjectNode == null)
      throw new TypeChangeError("Subject type is not in hierarchy.");

    numSuperTypes = newSuperTypes.length;
    for (int par = 0; par < numSuperTypes; par++)
      {
      supertypeNode = (POSetNode)typeTable.get(newSuperTypes[par]);
      if (supertypeNode == null)
        throw new TypeChangeError("Specified supertype is not in hierarchy.");
      try
        {
        subjectNode.addParent(supertypeNode);
        }
      catch (OrderConflictException e)
        {
        throw new TypeChangeError("Addition of parent creates an order conflict.", e);
        }
      }
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
  final void addSuperTypeToType(Type subjectType, Type newSuperType) throws TypeChangeError
  	{
  	Type supers[] = new Type[1];
  	
  	supers[0] = newSuperType;
  	addSuperTypesToType(subjectType, supers);
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
  final void addSubTypesToType(Type subjectType, Type newSubTypes[]) throws TypeChangeError
    {
    int numSubTypes;
    POSetNode subtypeNode, subjectNode;

    subjectNode = (POSetNode)typeTable.get(subjectType);

    if (subjectNode == null)
      throw new TypeChangeError("Subject type is not in hierarchy.");

    numSubTypes = newSubTypes.length;
    for (int par = 0; par < numSubTypes; par++)
      {
      subtypeNode = (POSetNode)typeTable.get(newSubTypes[par]);
      if (subtypeNode == null)
        throw new TypeChangeError("Specified child is not in hierarchy.");
      try
        {
        subjectNode.addChild(subtypeNode);
        }
      catch (OrderConflictException e)
        {
        throw new TypeChangeError("Addition of child creates an order conflict.", e);
        }
      }
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
  final void addSubTypeToType(Type subjectType, Type newSubType) throws TypeChangeError
  	{
  	Type subs[] = new Type[1];
  	
  	subs[0] = newSubType;
  	addSubTypesToType(subjectType, subs);
  	}

    /**
     * Updates the type label in the hierarchy to reflect a change.
     *
     * @param type  the type being updated.
     * @param oldLabel  the old label for the type.
     * @param newLabel  the new label for the type.
     * @exception notio.TypeChangeError
     *            if newLabel is already in use
     */
  final void updateTypeLabel(Type type, String oldLabel, String newLabel) throws TypeChangeError
    {
    POSetNode oldNode;
    
    oldNode = (POSetNode)typeTable.get(type);
    
    if ((newLabel != null) && (labelTable.get(newLabel) != null))
    	throw new TypeChangeError("Can't relabel type.  New type label is already in use.");

    // If there was an old label for this type, remove it from the label table.  Otherwise,
    // remove the type from the unlabelled type vector.
    if (oldLabel == null)
    	unlabelledTypeVec.removeElement(type);
    else
    	labelTable.remove(oldLabel);
    
    // If there is a new label for this type, add it to the label table.  Otherwise,
    // add the type to the unlabelled type vector.
    if (newLabel == null)
    	unlabelledTypeVec.addElement(type);
    else
	    labelTable.put(newLabel, oldNode);
    }

    /**
     * Removes a type from this hierarchy.
     *
     * @param deadType  the type being removed.
     * @exception notio.TypeRemoveError
     *            if specified type is not in hierarchy
     * @bug Should TypeRemoveError be TypeRemoveException instead?
     */
  final void removeTypeFromHierarchy(Type deadType) throws TypeRemoveError
    {
    POSetNode subjectNode;
    Enumeration e;
    String label;

    subjectNode = (POSetNode)typeTable.get(deadType);
    if (subjectNode == null)
    	throw new TypeRemoveError("Type '"+deadType.getLabel()+"' not found in hierarchy.");

    e = subjectNode.getParentEnumeration();
    while (e.hasMoreElements())
      {      
      ((POSetNode)e.nextElement()).removeChild(subjectNode);
      }

    e = subjectNode.getChildEnumeration();
    while (e.hasMoreElements())
      {      
      ((POSetNode)e.nextElement()).removeParent(subjectNode);
      }
      
    label = deadType.getLabel();
    if (label == null)
	    unlabelledTypeVec.removeElement(deadType);
	  else
	    labelTable.remove(label);
	    
	  typeTable.remove(deadType);
    deadType.setHierarchy(null);
    }
    
    /**
     * Looks up and returns the type object associated with the specified
     * label.
     *
     * @param label  the label used to lookup.
     * 
     * @return the type associated with the label or null if non-existant.
     */     
  final Type returnTypeByLabel(String label)
    {
    POSetNode node;

    node = (POSetNode)labelTable.get(label);
    if (node != null)
      return (Type)node.getData();
    else
      return null;
    }
  
    /**
     * Returns the vector of unlabelled types.
     *
     * @return the real vector that contains the unlabelled types.
     */
	final Vector returnUnlabelledTypes()
		{
		return unlabelledTypeVec;
		}

    /**
     * Returns all the supertypes of the subject type, not just the immediate
     * supertypes, in no particular order.  
     * It does not return the subject type itself.
     *
     * @param subjectType  the type whose supertypes will be returned.
     * @return a set containing the supertypes of the subject type, possibly empty.
     */
  final Set returnProperSuperTypesOf(Type subjectType)
    {
    POSetNode currNode = null, subjectNode, parentNode;
    Type supertype;
    Vector searchVec;
    Set supertypeSet = new Set();
    int currNodeIdx;
    Enumeration typeEnum;

    subjectNode = (POSetNode)typeTable.get(subjectType);
    
    searchVec = new Vector();

	  for (Enumeration e = subjectNode.getParentEnumeration(); e.hasMoreElements(); )
 	    searchVec.addElement(e.nextElement());

		currNodeIdx = 0;

		while (currNodeIdx < searchVec.size())
			{
			currNode = (POSetNode) searchVec.elementAt(currNodeIdx);
      supertype = (Type)currNode.getData();
			supertypeSet.addElement(supertype);
			
		  for (Enumeration e = currNode.getParentEnumeration(); e.hasMoreElements(); )
		  	{
		  	parentNode = (POSetNode) e.nextElement();
		  	if (!searchVec.contains(parentNode))
	 		    searchVec.addElement(parentNode);
 		    }
  	    
	  	currNodeIdx++;
			}    

		return supertypeSet;
    }

    /**
     * Returns all the subtypes of the subject type, not just the immediate
     * subtypes, in no particular order.
     * It does not return the subject type itself.
     *
     * @param subjectType  the type whose subtypes will be returned.
     * @return a set containing the subtypes of the subject type, possibly empty.
     */
  final Set returnProperSubTypesOf(Type subjectType)
    {
    POSetNode currNode = null, subjectNode, childNode;
    Type subtype, typeList[];
    Vector searchVec;
    Set subtypeSet = new Set();
    int currNodeIdx;

    subjectNode = (POSetNode)typeTable.get(subjectType);
    
    searchVec = new Vector();

	  for (Enumeration e = subjectNode.getChildEnumeration(); e.hasMoreElements(); )
 	    searchVec.addElement(e.nextElement());

		currNodeIdx = 0;

		while (currNodeIdx < searchVec.size())
			{
			currNode = (POSetNode) searchVec.elementAt(currNodeIdx);
      subtype = (Type)currNode.getData();
			subtypeSet.addElement(subtype);
			
		  for (Enumeration e = currNode.getChildEnumeration(); e.hasMoreElements(); )
		  	{
		  	childNode = (POSetNode) e.nextElement();
		  	if (!searchVec.contains(childNode))
	 		    searchVec.addElement(childNode);
 		    }
  	    
	  	currNodeIdx++;
			}    
    
    return subtypeSet;
    }

    /**
     * Returns the immediate supertypes of the subject type in no particular order.  
     * It does not return the subject type itself.
     *
     * @param subjectType  the type whose immediate supertypes will be returned.
     * @return an array containing the immediate supertypes of the subject type, possibly empty.
     */
  final Object[] returnImmediateSuperTypesOf(Type subjectType)
    {
    POSetNode subjectNode;
    Object supertypesArr[];

    subjectNode = (POSetNode)typeTable.get(subjectType);
    supertypesArr = subjectNode.getParentDataArray();
    
    return supertypesArr;
    }

    /**
     * Returns the immediate subtypes of the subject type in no particular order.  
     * It does not return the subject type itself.
     *
     * @param subjectType  the type whose immediate subtypes will be returned.
     * @return an array containing the immediate subtypes of the subject type, possibly empty.
     */
  final Object[] returnImmediateSubTypesOf(Type subjectType)
    {
    POSetNode subjectNode;
    Object subtypesArr[];

    subjectNode = (POSetNode)typeTable.get(subjectType);
    subtypesArr = subjectNode.getChildDataArray();
    
    return subtypesArr;
    }

    /**
     * Determines whether subject is a subtype of object.
     *
     * @param subject  the potential subtype being tested.
     * @param object  the potential supertype being tested.
     * @return true if subject is a subtype of object.
     */
  final boolean isSubTypeOf(Type subject, Type object)
    {
    POSetNode subtypeNode, supertypeNode;

    if (subject == object)
      return true;
      
    subtypeNode = (POSetNode)typeTable.get(subject);
    supertypeNode = (POSetNode)typeTable.get(object);

    return subtypeNode.hasParent(supertypeNode);
    }

    /**
     * Determines whether subject is a supertype of object.
     *
     * @param subject  the potential supertype being tested.
     * @param object  the potential subtype being tested.
     * @return true if subject is a supertype of object.
     */
  final boolean isSuperTypeOf(Type subject, Type object)
    {
    POSetNode subtypeNode, supertypeNode;

    if (subject == object)
      return true;
      
    subtypeNode = (POSetNode)typeTable.get(object);
    supertypeNode = (POSetNode)typeTable.get(subject);

    return subtypeNode.hasParent(supertypeNode);
    }
    
    /**
     * Determines whether subject is a subtype of object.
     *
     * @param subject  the potential subtype being tested.
     * @param object  the potential supertype being tested.
     * @return true if subject is a subtype of object.
     */
  final boolean isProperSubTypeOf(Type subject, Type object)
    {
    if (subject == object)
      return false;
    else
      return isSubTypeOf(subject, object);
    }

    /**
     * Determines whether subject is a proper supertype of object.
     *
     * @param subject  the potential supertype being tested.
     * @param object  the potential subtype being tested.
     * @return true if subject is a supertype of object.
     */
  final boolean isProperSuperTypeOf(Type subject, Type object)
    {
    if (subject == object)
      return false;
    else
      return isSuperTypeOf(subject, object);
    }

    /**
     * Sets a flag indicating whether or not the processing of type labels within this
     * hierarchy is case-sensitive.
     *
     * @param flag  the flag setting for case-sensitivity.
     */
  void setCaseSensitiveLabels(boolean flag)
  	{
  	caseSensitiveLabels = flag;
  	}

    /**
     * Returns true if the processing of type labels in this hierarchy is case-sensitive.
     *
     * @return true if the processing of type labels in this hierarchy is case-sensitive.
     */
  boolean getCaseSensitiveLabels()
  	{
  	return caseSensitiveLabels;
  	}

    /**
     * Returns a hashtable mapping type object into Integer objects
     * indicating their level in the heirarchy.
     *
     * @return a hashtable mapping type objects into Integer objects
     * indicating their level in the hierarchy.
     * @bug Just here as an experiment.
     */
  final Hashtable computeTypeLevels()
    {
    Hashtable levelTable = new Hashtable();
    Vector currentLevel, previousLevel;
    java.util.Enumeration<Object> _enum;
    POSetNode childArray[];
    int arrLen, level;

    level = 0;

    levelTable.put(universalType, new Integer(level));
    previousLevel = new Vector(1);
    previousLevel.addElement(universalNode);


    do
      {
      level++;
      _enum = previousLevel.elements();
      currentLevel = new Vector(1);
      while (_enum.hasMoreElements())
        {
        childArray = ((POSetNode)_enum.nextElement()).getChildArray();
        arrLen = childArray.length;
        for (int child = 0; child < arrLen; child++)
          {
          levelTable.put(childArray[child].getData(), new Integer(level));
          currentLevel.addElement(childArray[child]);
          }
        }

      previousLevel = currentLevel;

      }
    while (previousLevel.size() > 0);

    return levelTable;
    }
  }
