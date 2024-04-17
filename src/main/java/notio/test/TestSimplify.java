package notio.test;

import notio.*;

    /** 
     * Class used to test the simplify() methods in Graph.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.3 $, $Date: 1999/05/04 01:36:16 $
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
     * @bug This test is implementation specific since it relies on knowing which of the
     * redundant relations will be left after simplification and on the same instance being
     * returned by getRelations() every time it is called.
     */
public class TestSimplify extends TesterBase
  {
  /** Variables needed for the test. **/
 	boolean passed;
 	KnowledgeBase kBase;
 	Graph g1;
 	Concept c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
 	RelationType rt1, rt2, rt3;
 	Relation r[] = new Relation[12];
 	Relation result[], correct[];
 	
  	/**
  	 * Initialize graph and concept arrangement.
  	 */
  public void initializeGraphs()
  	{	
  	RelationTypeHierarchy rHier;
  	Concept args[] = new Concept[2];
  	
  	kBase = new KnowledgeBase();
  	
  	rHier = kBase.getRelationTypeHierarchy();

		// Build graph
  	rt1 = new RelationType("TypeA");
  	rHier.addTypeToHierarchy(rt1);
  	rt2 = new RelationType("TypeB");
  	rHier.addTypeToHierarchy(rt2);
  	rt3 = new RelationType("TypeC");
  	rHier.addTypeToHierarchy(rt3);
  	
  	g1 = new Graph();



  	c1 = new Concept();
  	c2 = new Concept();
  	c3 = new Concept();
  	c4 = new Concept();
  	c5 = new Concept();
  	c6 = new Concept();
  	c7 = new Concept();
  	c8 = new Concept();
  	c9 = new Concept();
  	c10 = new Concept();

		g1.addConcept(c1);
		g1.addConcept(c2);
		g1.addConcept(c3);
		g1.addConcept(c4);
		g1.addConcept(c5);
		g1.addConcept(c6);
		g1.addConcept(c7);
		g1.addConcept(c8);
		g1.addConcept(c9);
		g1.addConcept(c10);
	
		// Of type rt1
		// c1 - r0 - c2
		// c1 - r1 - c2
		// c1 - r2 - c2
		// c1 - r3 - c3
		// c1 - r4 - c3

		args[0] = c1; args[1] = c2;
		r[0] = new Relation(rt1, args);
		r[1] = new Relation(rt1, args);
		r[2] = new Relation(rt1, args);

		args[0] = c1; args[1] = c3;
		r[3] = new Relation(rt1, args);
		r[4] = new Relation(rt1, args);

		// Of type rt2
		// c3 - r5 - c4
		// c3 - r6 - c4
		// c3 - r7 - c4

		args[0] = c3; args[1] = c4;
		r[5] = new Relation(rt2, args);
		r[6] = new Relation(rt2, args);
		r[7] = new Relation(rt2, args);


		// Of type rt3
		// c5 - r8 - c6
		// c5 - r9 - c6
		// c7 - r10 - c8
		// c7 - r11 - c8
		args[0] = c5; args[1] = c6;
		r[8] = new Relation(rt3, args);
		r[9] = new Relation(rt3, args);
  	
		args[0] = c7; args[1] = c8;
		r[10] = new Relation(rt3, args);
		r[11] = new Relation(rt3, args);
		
		g1.addRelations(r);
  	}
  	
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest()
  	{	
		passed = true;
		
		// Test simplify(Relation)
		initializeGraphs();
		
		g1.simplify(r[0]);
		
		result = g1.getRelations();
		correct = new Relation[10];
		correct[0] = r[0]; 
		for (int rel = 1; rel < correct.length; rel++)
			correct[rel] = r[rel + 2];
			
		if (compareArrays(result, correct))
			logMessage("Correct result from simplify(Relation).");
		else
			{
			passed = false;
			logMessage("Incorrect result from simplify(Relation).");
			}
		
		// Test simplify(RelationType)
		initializeGraphs();
		
		g1.simplify(rt1);
		
		result = g1.getRelations();
		correct = new Relation[9];
		correct[0] = r[0]; 
		correct[1] = r[3]; 
		for (int rel = 2; rel < correct.length; rel++)
			correct[rel] = r[rel + 3];
			
		if (compareArrays(result, correct))
			logMessage("Correct result from simplify(RelationType).");
		else
			{
			passed = false;
			logMessage("Incorrect result from simplify(RelationType).");
			}
				
		// Test simplify()
		initializeGraphs();
		
		g1.simplify();
		
		result = g1.getRelations();
		correct = new Relation[5];
		correct[0] = r[0]; 
		correct[1] = r[3]; 
		correct[2] = r[5]; 
		correct[3] = r[8]; 
		correct[4] = r[10]; 
			
		if (compareArrays(result, correct))
			logMessage("Correct result from simplify().");
		else
			{
			passed = false;
			logMessage("Incorrect result from simplify().");
			}
				
		return passed;
  	}

  public String getTestName()
  	{
  	return "Simplify Test";
  	}
  	
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestSimplify test;
  	
  	test = new TestSimplify();
  	test.runAndReport();
  	}  	
  }
