package notio.test;

import java.util.Enumeration;
import notio.*;

    /** 
     * Class used to run all other tests for Notio.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8.2.1 $, $Date: 1999/10/11 00:28:18 $
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
public class TestAllNotio extends TesterBase
  {
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest()
  	{
  	boolean passed;
  	Tester tests[] =
  		{
  		new TestGraphTraversal(),
			new TestTypeHierarchies(),  	
			new TestCoreferenceSet(),  	
			new TestGraphQueries(),
			new TestSimplify(),
			new TestJoin(),
			new TestSerialization()
  		};
  	
  	passed = true;
  	
  	for (int test = 0; test < tests.length; test++)
  		{
  		if (tests[test].runTest())
	  		logMessage(tests[test].getTestName() + " : PASSED");
	  	else
	  		{
	  		Enumeration logEnum;
	  		
	  		passed = false;
  			logMessage(tests[test].getTestName() + " : FAILED");
  			
	  		logEnum = tests[test].enumerateMessages();
	  		
	  		while (logEnum.hasMoreElements())
	  			logMessage("\t" + (String)logEnum.nextElement());
	  		}
	  		
	  	// Drop reference to test when complete to facilitate garbage collection.
	  	// This is done because tests could conceivably use a lot of resources.
	  	tests[test] = null;
  		}
  	
  	return passed;
  	}

  public String getTestName()
  	{
  	return "Standard Full Notio Test";
  	}
  
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestAllNotio test;
  	
  	test = new TestAllNotio();
  	test.runAndReport();
  	}
  }
