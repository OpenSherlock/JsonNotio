package notio;

    /** 
     * Exception thrown when the removal of a concept gives rise to an
     * error. 
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8 $, $Date: 1999/05/04 01:35:48 $
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

public class ConceptRemoveException extends OperationException
  {
    /**
     * Constructs an exception with the specified message.
     * 
     * @param message  The details of the exception.
     */
  public ConceptRemoveException(String message)
    {
    super(message);
    }
    
    /**
     * Constructs an exception with the specified message and sub-throwable.
     *
     * @param message  the details of the exception.
     * @param newSubThrowable  the sub-throwable to be embedded in this exception.
     */
  public ConceptRemoveException(String message, Throwable newSubThrowable)
    {
    super(message, newSubThrowable);
    }    
  }
