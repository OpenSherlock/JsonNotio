package notio;

    /** 
     * The base Notio operation exception class.
     * This exception class allows other throwables to
     * be embedded in it.  This provides a clean means
     * for implementation-specific throwables to be
     * thrown from Notio routines.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.9 $, $Date: 1999/05/04 01:35:57 $
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

public class OperationException extends Exception
  {
  /** An throwable enclosed within this one. **/
  private Throwable subThrowable;

    /**
     * Constructs an exception with the specified message.
     *
     * @param message  the details of the exception.
     */
  public OperationException(String message)
    {
    super(message);
    }

    /**
     * Constructs an exception with the specified message and sub-throwable.
     *
     * @param message  the details of the exception.
     * @param newSubThrowable  the sub-throwable to be embedded in this
     * OperationException.
     */
  public OperationException(String message, Throwable newSubThrowable)
    {
    super(message);
    subThrowable = newSubThrowable;
    }

    /**
     * This method allows arbitrary throwables to be embedded inside
     * OperationExceptions and thrown along with them.  The embedded
     * throwables can give further details of what happened or be used to pass
     * implementation-specific throwables through the standard Notio API.
     * This method will replace any existing sub-throwable.
     *
     * @param newSubThrowable  the sub-throwable to be embedded in this
     * OperationException.
     */
  public void setSubThrowable(Throwable newSubThrowable)
    {
    subThrowable = newSubThrowable;
    }

    /**
     * This method retrieves arbitrary throwables embedded inside
     * OperationExceptions and thrown along with them.  The embedded
     * throwables can give further details of what happened or be used to pass
     * implementation-specific throwables through the standard Notio API.
     *
     * @return the sub-throwable or null if none is present.
     */
  public Throwable getSubThrowable()
    {
    return subThrowable;
    }
  }
