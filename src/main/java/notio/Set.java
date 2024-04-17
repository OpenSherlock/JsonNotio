package notio;

import java.util.*;
import java.io.Serializable;

    /** 
     * A general-purpose collection class for implementing sets.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.9 $, $Date: 1999/05/04 01:36:01 $
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

class Set implements Serializable
  {
  /** Members of this set. **/
  Vector members = new Vector(1, 1);

    /**
     * Adds a new object to this set.
     *
     * @param newObject  the object to be added.
     */
  public void addElement(Object newObject)
    {
    if (members.contains(newObject))
      return;
    members.addElement(newObject);
    }

    /**
     * Removes an object from this set.
     *
     * @param deadObject  the object to be removed.
     */
  public void removeElement(Object deadObject)
    {
    members.removeElement(deadObject);
    }

    /**
     * Tests if an object is a member of this set.
     *
     * @param queryObject  the object that is being checked for.
     *
     * @return true if queryObject is a member of this set, false otherwise.
     */
   public boolean contains(Object queryObject)
     {
     return members.contains(queryObject);
     }

    /** 
     * Returns an Enumeration of the members of this set.
     *
     * @return an Enumeration of the members of this set.
     */
  public Enumeration elements()
    {
    return members.elements();
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements in this set.
     */
  public int size()
    {
    return members.size();
    }

    /**
     * Copies the elements of this set into the specified array.
     *
     * @param elements  the target array.
     */
  public void copyInto(Object elements[])
    {
    members.copyInto(elements);
    }
  }
