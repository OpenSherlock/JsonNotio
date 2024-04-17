package notio;

import java.util.*;
import java.io.Serializable;

    /** 
     * A class for holding information related to translation so it can be
     * given to different parsers and generators to ensure consistent
     * representations.  A TranslationContext consists of a set of TranslationInfoUnits.
     * Translators can add, retrieve, and remove TranslationInfoUnits from a context.
     * The context has no understanding about the nature of the units it carries and
     * only a particular translator knows which units it needs and understands.  Translators
     * must agree on a TranslationInfoUnit name and sub-interface in order to share 
     * information.  Note that the term 'context' here does not refer to the conceptual
     * graph idea of a context.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.14 $, $Date: 1999/07/01 04:14:19 $
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
		 * @see notio.TranslationInfoUnit
		 * @see notio.Parser
		 * @see notio.Generator
     */

public class TranslationContext implements Serializable
  {
  	/** A hashtable mapping unit names to the TranslationInfoUnits that form this context. **/
  private Hashtable unitTable;

  	/**
  	 * Constructs a new TranslationContext containing no TranslationInfoUnits.
  	 */
	public TranslationContext()
		{
  	unitTable = new Hashtable();
  	}

  	/**
  	 * Adds the specified unit to this context under the unit's name.  The name is 
  	 * determined by a call to getUnitName() in the unit.  If the unit is already part
  	 * of this context, nothing happens.  If another unit has been added to this context
  	 * under the same name, it is replaced by the new unit.
  	 *
  	 * @param newUnit  the unit to be added.
  	 * @exception java.IllegalArgumentException  if the unit's name is null.
  	 *
  	 * @see notio.TranslationInfoUnit#getUnitName
  	 */
	public void addUnit(TranslationInfoUnit newUnit)
  	{
  	if (newUnit.getUnitName() == null)
  		throw new IllegalArgumentException("TranslationInfoUnit names must be non-null before being added to a TranslationContext.");
  		
  	unitTable.put(newUnit.getUnitName(), newUnit);
		}  	

  	/**
  	 * Removes the specified unit from this context.  If the unit is not part of this
  	 * context, nothing happens.
  	 *
  	 * @param deadUnit  the unit to be removed.
  	 */
	public void removeUnit(TranslationInfoUnit deadUnit)
  	{
  	unitTable.remove(deadUnit.getUnitName());
  	}

		/**
		 * Returns the unit corresponding to the specified name, or null if no unit with that
		 * name is currently in this context.
		 *
		 * @param unitName  the name of the unit being retrieved.
		 */
	public TranslationInfoUnit getUnit(String unitName)
  	{
		return (TranslationInfoUnit)unitTable.get(unitName);
  	}

  	/**
  	 * Calls resetUnit() in all units in this context.
  	 * This effectively clears all information relating to earlier translation sessions
  	 * from this context.
  	 *
  	 * @see notio.TranslationInfoUnit#resetUnit
  	 */
  public void resetUnits()
  	{
  	Enumeration unitEnum;
  	
  	unitEnum = unitTable.elements();
  	while (unitEnum.hasMoreElements())
  		((TranslationInfoUnit)unitEnum.nextElement()).resetUnit();
  	}
  }
