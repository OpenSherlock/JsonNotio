package notio.test;

import java.util.*;

    /** 
     * Base class used by Tester implementations.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.6 $, $Date: 1999/05/04 01:36:17 $
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
abstract public class TesterBase implements Tester
  {
  /** A vector of message strings generated during the test. **/
  private Vector logVec = new Vector();

  	/**
  	 * Adds a message to the log.
  	 *
  	 * @param message  the String to be added to the log.
  	 */
  public void logMessage(String message)
  	{
  	logVec.addElement(message);
  	}
  
  	/**
  	 * Returns an enumeration of the messages in the log.
  	 *
  	 * @return an Enumeration of the messages in the log.
  	 */
  public Enumeration enumerateMessages()
  	{
  	return logVec.elements();
  	}

  	/**
  	 * Returns all messages in the log in a formatted fashion.
  	 *
  	 * @return all messages in the log in a formatted fashion.
  	 */
  public String getFormattedLog()
  	{
		Enumeration logEnum;
		String logString;
		
		logString = "";
		
		logEnum = enumerateMessages();
		while (logEnum.hasMoreElements())
			{
			String message;
			
			message = (String)logEnum.nextElement();
			logString += message + "\n";
			}
			
		return logString;
  	}

		/**
		 * Runs this test and dumps report to standard out.
		 */		 
  public void runAndReport()
  	{
  	runTest();
  	System.err.println(getFormattedLog());
  	}
  	
  	/**
  	 * Returns true if the arrays contain the same elements, though not necessarily in order.
  	 * This is a generally useful function when testing so it placed in the base.
  	 * @return true if the arrays contain the same elements.
  	 */
  public static boolean compareArrays(Object first[], Object second[])
  	{
  	boolean used[];
  	
  	if ((first == null) || (second == null))
  		return false;
  		
  	if (first.length != second.length)
  		return false;
  		
  	used = new boolean[first.length];
  	
  	for (int felem = 0; felem < first.length; felem++)
  		{
  		int selem;
  		boolean found;
  		
  		selem = 0;
  		found = false;
  		
  		while (selem < first.length && !found)
  			{
	  		if (!used[selem])
	  			if (first[felem].equals(second[selem]))
	  				{
	  				found = true;
	  				used[selem] = true;
	  				}
	  				
	  		selem++;
	  		}
	  				
	  	if (!found)
	  		return false;
  		}
  	return true;
  	}
  }
