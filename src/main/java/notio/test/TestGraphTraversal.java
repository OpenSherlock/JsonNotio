package notio.test;

import notio.*;

    /** 
     * Class used to test nested graph traversals.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.6 $, $Date: 1999/08/01 22:09:44 $
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
public class TestGraphTraversal extends TesterBase
  {
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest()
  	{
  	boolean passed;
  	Graph outer, inner;
  	KnowledgeBase kBase;
  	Referent referent;
  	ConceptType conType;
  	Concept concept;


		passed = true;

  	kBase = new KnowledgeBase();
  	outer = new Graph();
  	inner = new Graph();
  	conType = new ConceptType("X");
  	referent = new Referent(inner);
  	concept = new Concept(conType, referent);
  	
  	outer.addConcept(concept);

  	if (inner.getEnclosingReferent() == referent)
  		logMessage("Inner's enclosing referent is correct.");
  	else
  		{
  		logMessage("Inner's enclosing referent is NOT correct.");
  		passed = false;
  		}
  		
  	if (referent.getEnclosingConcept() == concept)
  		logMessage("Referent's enclosing concept is correct.");
  	else
  		{
  		logMessage("Referent's enclosing concept is NOT correct.");
  		passed = false;
  		}
  	
  	if (outer == concept.getEnclosingGraph())
  		logMessage("Concept's enclosing graph is correct.");
  	else
  		{
  		logMessage("Concept's enclosing graph is NOT correct.");
  		passed = false;
  		}

  	if (outer.getEnclosingReferent() == null)
  		logMessage("Outer's enclosing referent is correct.");  		
  	else
  		{
  		logMessage("Outer's enclosing referent is NOT correct.");  		
  		passed = false;
  		}

  	if (outer.getContext() == null)
  		logMessage("Outer's context is correct.");  		
  	else
  		{
  		logMessage("Outer's context is NOT correct.");  		
  		passed = false;
  		}

  	if (concept == inner.getContext())
  		logMessage("Inner's context is correct.");  		
  	else
  		{
  		logMessage("Inner's context is NOT correct.");  		
  		passed = false;
  		}
		
		return passed;
  	}

  public String getTestName()
  	{
  	return "Graph Traversal Test";
  	}
  	
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestGraphTraversal test;
  	
  	test = new TestGraphTraversal();
  	test.runAndReport();
  	}
  }
