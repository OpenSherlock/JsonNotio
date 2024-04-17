
package notio;

    /** 
     * This exception is thrown whenever a feature or operation is unavailable
     * in a given Notio implementation.  It is included primarily so that
     * partial implementations may be safely distributed.  
     * Since it is a subclass of java.lang.RuntimeException, this exception
     * need not be declared in the throws clause of any method and need not be
     * explicitly caught.  Use of this exception should be kept to a minimum
     * and it should never be used to report any other type of error.
     *
     * @see java.lang.RuntimeException
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.4 $, $Date: 1999/05/04 01:36:04 $
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
public class UnimplementedFeatureException extends RuntimeException
  {

    /**
     * Constructs an exception with the specified message.
     *
     * @param message  the details of the exception.
     */
  public UnimplementedFeatureException(String message)
    {
    super(message);
    }
  }
