package notio;

    /** 
     * An exception for POSetNode class that indicates when an action
     * would create an order conflict (both parent and child).
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8 $, $Date: 1999/05/24 04:01:18 $
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
     * @impspec This exception is thrown by POSetNode, but is caught by Notio API
     * classes and thrown as an appropriate Notio exception (e.g. notio.TypeChangeException).
     */

class OrderConflictException extends Exception
  {
    /**
     * Constructs an exception with the specified message.
     *
     * @param message  the details of the exception.
     */
  public OrderConflictException(String message)
    {
    super(message);
    }
  }
