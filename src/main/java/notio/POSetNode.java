package notio;

import java.util.*;

import com.google.gson.JsonObject;

import java.io.Serializable;

    /** 
     * A general-purpose collection class for implementing partially-ordered
     * sets.  NOTE: This class is most definitely NOT thread-safe.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.14 $, $Date: 1998/02/24 07:28:28 
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
     * @impspec This class is used to implement the type hierarchies.
     */

class POSetNode implements Serializable
  {
  /** Data object stored in this node. **/
  Object data;
  /** Vector of parent nodes. **/
  Vector parents = new Vector(1, 1);
  /** Vector of child nodes. **/
  Vector children = new Vector(1, 1);

  public JsonObject toJSON() {
	  JsonObject result = new JsonObject();
	  
	  
	  return result;
  }

    /**
     * Constructs a new POSetNode.
     */
  public POSetNode()
    {
    }

    /**
     * Constructs a new POSetNode with the specified data object.
     *
     * @param newData  the object to be referenced by this node.
     */
  public POSetNode(Object newData)
    {
    data = newData;
    }

    /**
     * Sets the object stored in the node.
     *
     * @param newData  the object to be stored in the node.
     */
  public void setData(Object newData)
    {
    data = newData;
    }

    /**
     * Returns the object stored in the node.
     *
     * @return the object stored in the node.
     */
  public Object getData()
    {
    return data;
    }

    /**
     * Tests to see if the specified node is a parent of this node.
     *
     * @param queryNode  the node being tested.
     *
     * @return true if queryNode is a parent of this node, false otherwise.
     */
  public boolean hasParent(POSetNode queryNode)
    {
    POSetNode aParent;

    if (parents.contains(queryNode))
      return true;
    
    for (Enumeration e = parents.elements(); e.hasMoreElements();)
      {
      aParent = (POSetNode)e.nextElement();
      if (aParent.hasParent(queryNode))
        return true;
      }

    return false;
    }

    /**
     * Tests to see if the specified node is a child of this node.
     *
     * @param queryNode  the node being tested.
     *
     * @return true if the specified node is a child of this node, false
     *         otherwise.
     */
  public boolean hasChild(POSetNode queryNode)
    {
    POSetNode aChild;

    if (children.contains(queryNode))
      return true;
    
    for (Enumeration e = children.elements(); e.hasMoreElements();)
      {
      aChild = (POSetNode)e.nextElement();
      if (aChild.hasChild(queryNode))
        return true;
      }

    return false;
    }

    /**
     * Tests to see if the specified node is an immediate (non-transitive) parent of this 
     * node.
     *
     * @param queryNode  the node being tested.
     *
     * @return true if queryNode is an immediate parent of this node, false otherwise.
     */
  public boolean hasImmediateParent(POSetNode queryNode)
    {
    if (parents.contains(queryNode))
      return true;

    return false;
    }

    /**
     * Tests to see if the specified node is an immediate (non-transitive) child of this 
     * node.
     *
     * @param queryNode  the node being tested.
     *
     * @return true if queryNode is an immediate child of this node, false otherwise.
     */
  public boolean hasImmediateChild(POSetNode queryNode)
    {
    if (children.contains(queryNode))
      return true;

    return false;
    }


    /**
     * Tests to see if this node is a parent of the specified node.
     *
     * @param queryNode  the node being tested.
     *
     * @return true if this node is a parent of queryNode, false otherwise.
     */
  public boolean isParentOf(POSetNode queryNode)
    {
    return queryNode.hasParent(this);
    }

    /**
     * Tests to see if this node is a child of the specified node.
     *
     * @param queryNode  the node being tested.
     *
     * @return true if this node is a child of queryNode, false otherwise.
     */
  public boolean isChildOf(POSetNode queryNode)
    {
    return queryNode.hasChild(this);
    }

    /**
     * Creates a new parent-child link, adjusting the set to remove redundant links.
     *
     * @param newParent  the parent to be linked.
     * @param newChild  the child to be linked.
     */
  public static void addParentChildLink(POSetNode newParent, POSetNode newChild) throws OrderConflictException
    {
    POSetNode aParent, aChild;

    if (newParent == newChild)
      return;

    if (newChild.hasParent(newParent))
      return;

    if (newChild.hasChild(newParent))
      throw new OrderConflictException("Attempt to add node as a parent when it is already a child.");

		// Remove redundant links from this node to parents of newParent.
    for (Enumeration e = newChild.getParentEnumeration(); e.hasMoreElements();)
      {
      aParent = (POSetNode)e.nextElement();

      if (newParent.hasParent(aParent))
        {
        // Since one of the child's parents is a parent of the new parent, we
        // will remove it.
        newChild.rawRemoveParent(aParent);
        
       	aParent.rawRemoveChild(newChild);        
        }
      }

		// Remove redundant links from newParent to children of this node.
    for (Enumeration e = newParent.getChildEnumeration(); e.hasMoreElements();)
      {
      aChild = (POSetNode)e.nextElement();

      if (newChild.hasChild(aChild))
        {
        // Since one of the new parent's children is the child of this node, we
        // will remove it.
        newParent.rawRemoveChild(aChild);
        
        // If aChild is an immediate child of newParent, we must remove newParent from 
        // its parent list
       	aChild.rawRemoveParent(newParent);        
        }
      }

    newChild.rawAddParent(newParent);
    
    newParent.rawAddChild(newChild);
    }
    
    /**
     * Adds a new parent to this node, adjusting the set to remove redundant links.
     *
     * @param newParent  the parent to be added.
     */
  public void addParent(POSetNode newParent) throws OrderConflictException
    {
    addParentChildLink(newParent, this);
    }

    /**
     * Adds a new child to this node, adjusting the set to remove redundant links.
     *
     * @param newChild  the child to be added.
     */
  public void addChild(POSetNode newChild) throws OrderConflictException
    {
    addParentChildLink(this, newChild);
    }

    /**
     * Removes a parent from this node.
     *
     * @param deadParent  the parent to be removed.
     */
  public void removeParent(POSetNode deadParent)
    {
    POSetNode aParent;

    parents.removeElement(deadParent);

    // To keep things connected we must add the parents of the dead parent
    // to this node.
    for (Enumeration e = deadParent.getParentEnumeration(); e.hasMoreElements();)
      {
      aParent = (POSetNode)e.nextElement();
      try
        {
        addParent(aParent);
        }
      catch (OrderConflictException exception)
        {
        // Should never happen
        exception.printStackTrace();
        }
      }
    }

    /**
     * Removes a child from this node.
     *
     * @param deadChild  the child to be removed.
     */
  public void removeChild(POSetNode deadChild)
    {
    POSetNode aChild;

    children.removeElement(deadChild);

    // To keep things connect we must add the parents of the dead parent
    // to this node.
    for (Enumeration e = deadChild.getChildEnumeration(); e.hasMoreElements();)
      {
      aChild = (POSetNode)e.nextElement();
      try
        {
        addChild(aChild);
        }
      catch (OrderConflictException exception)
        {
        // Should never happen
        exception.printStackTrace();
        System.exit(1);
        }
      }
    }

    /**
     * Removes a parent from this node's list of parents.
     *
     * @param deadParent  the parent to be removed.
     */
  public void rawRemoveParent(POSetNode deadParent)
    {
    parents.removeElement(deadParent);
    }
    
    /**
     * Removes a parent from this node's list of parents.
     *
     * @param deadChild  the parent to be removed.
     */
  public void rawRemoveChild(POSetNode deadChild)
    {
    children.removeElement(deadChild);
    }
    
    /**
     * Adds a new child to this node's list of children.  
     *
     * @param newChild  the child to be added.
     */
  public void rawAddChild(POSetNode newChild)
    {
    children.addElement(newChild);
    }
    
    /**
     * Adds a new parent to this node's list of parents.  
     *
     * @param newParent  the parent to be added.
     */
  public void rawAddParent(POSetNode newParent)
    {
    parents.addElement(newParent);
    }
    
    /** 
     * Returns an array of the parent nodes of this node.
     *
     * @return an array of nodes that are the parent nodes.
     */
  public POSetNode[] getParentArray()
    {
    POSetNode parentArray[] = new POSetNode[parents.size()];

    parents.copyInto(parentArray);

    return parentArray;
    }

    /** 
     * Returns an Enumeration of the parent nodes of this node.
     *
     * @return an Enumeration of nodes that are the parent nodes.
     */
  public Enumeration getParentEnumeration()
    {
    return parents.elements();
    }

    /** 
     * Returns an array of the child nodes of this node.
     *
     * @return an array of nodes that are the child nodes.
     */
  public POSetNode[] getChildArray()
    {
    POSetNode childArray[] = new POSetNode[children.size()];

    children.copyInto(childArray);

    return childArray;
    }

    /** 
     * Returns an Enumeration of the child nodes of this node.
     *
     * @return an Enumeration of nodes that are the child nodes.
     */
  public Enumeration getChildEnumeration()
    {
    return children.elements();
    }

    /**
     * Returns an array of the data objects belonging to the parent nodes.
     *
     * @return array of data objects belonging to the parent nodes.
     */
  public Object[] getParentDataArray()
    {
    int numParents = parents.size();
    Object parentDataArray[] = new Object[numParents];
    
    for (int parent = 0; parent < numParents; parent++)
      parentDataArray[parent] = ((POSetNode)parents.elementAt(parent)).getData();

    return parentDataArray;
    }

    
    /**
     * Returns an array of the data objects belonging to the child nodes.
     *
     * @return array of data objects belonging to the child nodes.
     */
  public Object[] getChildDataArray()
    {
    int numChildren = children.size();
    Object childDataArray[] = new Object[numChildren];
    
    for (int child = 0; child < numChildren; child++)
      childDataArray[child] = ((POSetNode)children.elementAt(child)).getData();

    return childDataArray;
    }
  }
