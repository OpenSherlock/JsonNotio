package notio;

    /** 
     * Interface for Macros.  This interface specifies the methods that all
     * macros must implement.  Essentially, a macro is some unspecified
     * operator that can be executed.  It is up to specific applications to
     * decide when macros should be executed, what the appropriate arguments
     * are, and what should be done with the results.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.7 $, $Date: 1999/05/04 01:35:53 $
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

public interface Macro
  {
    /**
     * Returns the name of the macro (e.g. &forall)
     *
     * @return the name of the macro.
     */
  public String getName();

    /**
     * Executes the macro with the specified array of Objects for arguments
     * and returns whatever results as an array of Objects.
     *
     * @param args  an array of Objects that are the argument to the macro.
     * @return the results of executing the macro as an array of Objects.
     */
  public Object[] executeMacro(Object args[]);
  }
