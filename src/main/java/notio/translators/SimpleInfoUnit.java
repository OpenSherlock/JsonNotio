package notio.translators;

import java.io.Serializable;
import notio.*;

    /** 
     * An abstract base-class for implementors the TranslationInfoUnit interface.
     * This class simply provides support for the name-related methods.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.1 $, $Date: 1999/05/06 03:09:42 $
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
     */
abstract public class SimpleInfoUnit implements TranslationInfoUnit
  {
	  /** The name of this unit. **/
	private String unitName;
	
  	/**
  	 * Sets the name of this translation unit.  Parsers and generators can use a
  	 * name to retrieve the units they need from a TranslationContext.
  	 *
  	 * @param newName  the new name for this unit.
  	 */
  public void setUnitName(String newName)
  	{
  	unitName = newName;
  	}
  
  	/**
  	 * Returns the name of this translation unit.  Parsers and generators can use a
  	 * name to retrieve the units they need from a TranslationContext.
  	 *
  	 * @return the name for this unit.
  	 */
  public String getUnitName()
  	{
  	return unitName;
  	}
  }
