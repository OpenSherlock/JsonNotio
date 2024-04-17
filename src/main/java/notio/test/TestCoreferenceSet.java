package notio.test;

import notio.*;

    /** 
     * Class used to test the CoreferenceSet class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.9 $, $Date: 1999/09/10 05:12:09 $
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
public class TestCoreferenceSet extends TesterBase
  {
  /** Variables needed for the test. **/
 	boolean passed;
 	KnowledgeBase kBase;
 	Graph g1, g2, g3, g4;
 	Concept c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
 	Concept cArr1[], cArr2[];
 	
  	/**
  	 * Initialize graph and concept arrangement.
  	 */
  public void initializeGraphs()
  	{	
  	kBase = new KnowledgeBase();
  	
  	// g1 has c1, c2, c3
  	// c1 has g2
  	// c2 has g3
  	// g2 has c4, c5, c6
  	// c4 has g4
  	// g3 has c7, c8, c9
  	// c4 has g4
  	// g4 has c10

		// Build compound graph
  	g1 = new Graph();
  	g2 = new Graph();
  	g3 = new Graph();
  	g4 = new Graph();

  	c1 = new Concept();
  	c2 = new Concept(new Referent(g2));
  	c3 = new Concept(new Referent(g3));
  	c4 = new Concept(new Referent(g4));
  	c5 = new Concept();
  	c6 = new Concept();
  	c7 = new Concept();
  	c8 = new Concept();
  	c9 = new Concept();
  	c10 = new Concept();

		g1.addConcept(c1);
		g1.addConcept(c2);
		g1.addConcept(c3);

		g2.addConcept(c4);
		g2.addConcept(c5);
		g2.addConcept(c6);

		g3.addConcept(c7);
		g3.addConcept(c8);
		g3.addConcept(c9);

		g4.addConcept(c10);
  	}
  	
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest()
  	{	
		passed = true;
		
		initializeGraphs();
		
		CoreferenceSet corefSetA, corefSetB;

		corefSetA = new CoreferenceSet();
		
		try
			{
			corefSetA.setEnableScopeChecking(true);
			}
		catch (CorefAddException e)
			{
			// Should not throw exception since the set is empty.
			passed = false;
			logMessage("Incorrectly threw CorefAddException for empty coreference set.");
			}
		catch (InvalidDefiningConceptException e)
			{
			// Should not throw exception since the set is empty.
			passed = false;
			logMessage("Incorrectly threw CorefAddException for empty coreference set.");
			}

		try
			{
			corefSetA.addCoreferentConcept(c1);
			logMessage("Successfully added c1 to set A.");
			}
		catch (CorefAddException e)
			{
			passed = false;
			logMessage("Incorrectly threw CorefAddException whilst adding c1 to set A.");
			}

		try
			{
			corefSetA.addCoreferentConcept(c2);
			logMessage("Successfully added c2 to set A.");
			}
		catch (CorefAddException e)
			{
			passed = false;
			logMessage("Incorrectly threw CorefAddException whilst adding c2 to set A.");
			}

		try
			{
			corefSetA.addCoreferentConcept(c5);
			logMessage("Successfully added c5 to set A.");
			}
		catch (CorefAddException e)
			{
			passed = false;
			logMessage("Incorrectly threw CorefAddException whilst adding c5 to set A.");
			}
			
		try
			{
			corefSetA.addCoreferentConcept(c7);
			logMessage("Successfully added c7 to set A.");
			}
		catch (CorefAddException e)
			{
			passed = false;
			logMessage("Incorrectly threw CorefAddException whilst adding c7 to set A.");
			}

		try
			{
			corefSetA.addCoreferentConcept(c10);
			logMessage("Successfully added c10 to set A.");
			}
		catch (CorefAddException e)
			{
			passed = false;
			logMessage("Incorrectly threw CorefAddException whilst adding c10 to set A.");
			}
			
		if (corefSetA.hasDominantConcept(c1))
			logMessage("Correctly identified c1 as dominant.");
		else
			logMessage("Failed to correctly identify c1 as dominant.");
		
		if (corefSetA.hasDominantConcept(c2))
			logMessage("Correctly identified c2 as dominant.");
		else
			logMessage("Failed to correctly identify c2 as dominant.");
		
		if (corefSetA.hasSubordinateConcept(c5))
			logMessage("Correctly identified c5 as subordinate.");
		else
			logMessage("Failed to correctly identify c5 as subordinate.");
		
		if (corefSetA.hasSubordinateConcept(c7))
			logMessage("Correctly identified c7 as subordinate.");
		else
			logMessage("Failed to correctly identify c7 as subordinate.");
			
		if (corefSetA.hasSubordinateConcept(c10))
			logMessage("Correctly identified c10 as subordinate.");
		else
			logMessage("Failed to correctly identify c10 as subordinate.");
			
		cArr1 = corefSetA.getDominantConcepts();
		
		cArr2 = new Concept[2];
		cArr2[0] = c1;
		cArr2[1] = c2;
		
		if (compareArrays(cArr1, cArr2))
			{
			logMessage("Correct result returned by getDominantConcepts().");
			}
		else
			{
			passed = false;
			logMessage("Incorrect result (array length " + cArr1.length + ") returned by getDominantConcepts().");
			}
		
		cArr1 = corefSetA.getSubordinateConcepts();
		
		cArr2 = new Concept[3];
		cArr2[0] = c5;
		cArr2[1] = c7;
		cArr2[2] = c10;
		
		if (compareArrays(cArr1, cArr2))
			{
			logMessage("Correct result returned by getSubordinateConcepts().");
			}
		else
			{
			passed = false;
			logMessage("Incorrect result (array length " + cArr1.length + ") returned by getSubordinateConcepts().");
			}

		return passed;
  	}

  public String getTestName()
  	{
  	return "Coreference Set Test";
  	}
  	
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestCoreferenceSet test;
  	
  	test = new TestCoreferenceSet();
  	test.runAndReport();
  	}
  }
