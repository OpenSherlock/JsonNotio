package notio;

    /** 
     * The base Notio operation error class.
     * This error class allows other throwables to
     * be embedded in it.  This provides a clean means
     * for implementation-specific throwables to be
     * thrown from Notio routines.
     * Note that since OperationError is a subclass of Error,
     * applications are not required to explicitly throw or 
     * catch it.  As such it is reserved for Notio errors that
     * are most probably due to a programming error in the application
     * rather than a reasonable misuse of the API.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.4 $, $Date: 1999/05/04 01:35:57 $
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
     * @see java.lang.Error
     */

public class OperationError extends Error
  {
  /** An throwable enclosed within this one. **/
  private Throwable subThrowable;

    /**
     * Constructs an error with the specified message.
     *
     * @param message  the details of the error.
     */
  public OperationError(String message)
    {
    super(message);
    }

    /**
     * Constructs an error with the specified message and sub-throwable.
     *
     * @param message  the details of the error.
     * @param newSubThrowable  the sub-throwable to be embedded in this
     * OperationError.
     */
  public OperationError(String message, Throwable newSubThrowable)
    {
    super(message);
    subThrowable = newSubThrowable;
    }

    /**
     * This method allows arbitrary throwables to be embedded inside
     * OperationErrors and thrown along with them.  The embedded
     * throwables can give further details of what happened or be used to pass
     * implementation-specific throwables through the standard Notio API.
     * This method will replace any existing sub-throwable.
     *
     * @param newSubThrowable  the sub-throwable to be embedded in this
     * OperationError.
     */
  public void setSubThrowable(Throwable newSubThrowable)
    {
    subThrowable = newSubThrowable;
    }

    /**
     * This method retrieves arbitrary throwables embedded inside
     * OperationErrors and thrown along with them.  The embedded
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
