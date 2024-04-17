package notio;

import java.io.Serializable;

    /** 
     * The type class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.14 $, $Date: 1999/05/04 01:36:02 $
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
     * @impspec The Notio API does not formally require a Type class.
     */

abstract class Type implements Serializable
  {
  /** The hierarchy to which this type belongs. **/
  TypeHierarchy hierarchy;

  /**
   * Returns the type label for this type.
   *
   * @return the type label for this type.
   */
  abstract public String getLabel();

    /**
     * An implementation specific method for ensuring that every type knows
     * which type heirarchy is belongs to.
     *
     * @param newHierarchy  the hierarchy to which this type belongs.
     */
  void setHierarchy(TypeHierarchy newHierarchy)
    {
    hierarchy = newHierarchy;
    }

    /**
     * An implementation specific method for which hierachy
     * this type belongs to.
     *
     * @return the hierarchy to which this type belongs.
     */
  TypeHierarchy getHierarchy()
    {
    return hierarchy;
    }
  }
