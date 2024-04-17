package notio.test;

import java.util.Enumeration;

    /** 
     * Interface used by all Notio testing classes.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.4 $, $Date: 1999/05/04 01:36:16 $
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
public interface Tester
  {
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest();

  	/**
  	 * Returns the name for this test.
  	 *
  	 * @return the name for this test.
  	 */
  public String getTestName();
  
  	/**
  	 * Returns an enumeration of the messages in the log.
  	 *
  	 * @return an Enumeration of the messages in the log.
  	 */
  public Enumeration enumerateMessages();

  	/**
  	 * Returns all messages in the log in a formatted fashion.
  	 *
  	 * @return all messages in the log in a formatted fashion.
  	 */
  public String getFormattedLog();
  }
