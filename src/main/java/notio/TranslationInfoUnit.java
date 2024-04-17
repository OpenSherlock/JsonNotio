package notio;

import java.io.Serializable;

    /** 
     * An interface for information units relating to translation so they can be
     * given to different parsers and generators to ensure consistent
     * representations.  Common examples of these units include symbol tables.
     * TranslationInfoUnits are contained within and passed around using TranslationContexts.
     * Translators that are to exchange information via these units must agree on both a 
     * name for particular unit and a sub-interface of this interface which defines the
     * methods usable on that unit.  This allows for multiple instances of the same
     * type of unit (e.g. a generic symbol table) but with different names for instances
     * being used for different purposes.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.2 $, $Date: 1999/05/06 02:48:15 $
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
		 * @see notio.TranslationContext
     */
public interface TranslationInfoUnit extends Serializable
  {
  	/**
  	 * Sets the name of this translation unit.  Parsers and generators can use a
  	 * name to retrieve the units they need from a TranslationContext.
  	 *
  	 * @param newName  the new name for this unit.
  	 */
  public void setUnitName(String newName);
  
  	/**
  	 * Returns the name of this translation unit.  Parsers and generators can use a
  	 * name to retrieve the units they need from a TranslationContext.
  	 *
  	 * @return the name for this unit.
  	 */
  public String getUnitName();

  	/**
  	 * Resets this information unit to its initial state.  This method can be called
  	 * by translators in order to ensure that a unit contains no information relating
  	 * to earlier translation sessions.
  	 */
  public void resetUnit();

  	/**
  	 * Returns a duplicate of this information unit that is distinct from the original.
  	 * This means that changes to the original will not affect the duplicate and vice
  	 * versa.
  	 *
  	 * @return a duplicate of this unit.
  	 */
  public TranslationInfoUnit copyUnit();
  }
