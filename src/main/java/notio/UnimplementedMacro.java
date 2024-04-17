package notio;

import java.io.Serializable;

    /** 
     * Class used to create a dummy for unimplemented macros.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.9 $, $Date: 1999/05/04 01:36:04 $
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

public class UnimplementedMacro extends Object implements Macro, Serializable
  {
  /** The name by which this macro is known. */
  private String name;

    /**
     * Constructs an dummy macro with the specified name.
     * 
     * @param newName  the name by which this macro is known.
     */
   public UnimplementedMacro(String newName)
     {
     name = newName;
     }

    /**
     * Returns the name of the macro.
     *
     * @return the name of the macro.
     */
  public String getName()
    {
    return name;
    }

    /**
     * UnimplementedMacros are dummy macros that do nothing when implemented.
     * Perhaps they should throw an UnimplementedOperationException exception?
     *
     * @param args  these are completely ignored.
     * @return null since this macro doesn't do anything.
     */
  public Object[] executeMacro(Object args[])
    {
    return null;
    }
  }
