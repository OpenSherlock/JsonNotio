package notio.test;

import notio.*;

    /** 
     * Class used to test various query methods in Graph.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.3 $, $Date: 1999/05/04 01:36:15 $
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
public class TestGraphQueries extends TesterBase
  {
  /** Variables needed for the test. **/
 	boolean passed;
 	KnowledgeBase kBase;
 	Graph g1;
 	RelationType rt1, rt2, rt3;
 	ConceptType ct1, ct2, ct3;
 	Concept c[] = new Concept[9];
 	Relation r[] = new Relation[12];
 	Concept conResult[], conCorrect[];
 	Relation relResult[], relCorrect[];
 	
  	/**
  	 * Initialize graph and concept arrangement.
  	 */
  public void initializeGraphs()
  	{	
  	RelationTypeHierarchy rHier;
  	ConceptTypeHierarchy cHier;
  	Concept args[] = new Concept[2];
  	
  	kBase = new KnowledgeBase();
  	
  	rHier = kBase.getRelationTypeHierarchy();
  	cHier = kBase.getConceptTypeHierarchy();

		// Build graph
  	rt1 = new RelationType("RTypeA");
  	rHier.addTypeToHierarchy(rt1);
  	rt2 = new RelationType("RTypeB");
  	rHier.addTypeToHierarchy(rt2);
  	rt3 = new RelationType("RTypeC");
  	rHier.addTypeToHierarchy(rt3);
  	
  	ct1 = new ConceptType("CTypeA");
  	cHier.addTypeToHierarchy(ct1);
  	ct2 = new ConceptType("CTypeB");
  	cHier.addTypeToHierarchy(ct2);
  	ct3 = new ConceptType("CTypeC");
  	cHier.addTypeToHierarchy(ct3);
  	
  	g1 = new Graph();

  	c[0] = new Concept(ct1);
  	c[1] = new Concept(ct1);
  	c[2] = new Concept(ct1);
  	c[3] = new Concept(ct2);
  	c[4] = new Concept(ct2);
  	c[5] = new Concept(ct2);
  	c[6] = new Concept(ct3);
  	c[7] = new Concept(ct3);
  	c[8] = new Concept(ct3);

		g1.addConcepts(c);

  	r[0] = new Relation(rt1);
  	r[1] = new Relation(rt1);
  	r[2] = new Relation(rt1);
  	r[3] = new Relation(rt2);
  	r[4] = new Relation(rt2);
  	r[5] = new Relation(rt2);
  	r[6] = new Relation(rt3);
  	r[7] = new Relation(rt3);
  	r[8] = new Relation(rt3);

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
		
		initializeGraphs();
		
		// Test getConceptsWithExactType()
		conResult = g1.getConceptsWithExactType(ct1);
		conCorrect = new Concept[3];
		conCorrect[0] = c[0];
		conCorrect[1] = c[1];
		conCorrect[2] = c[2];
		
		if (!compareArrays(conResult, conCorrect))
			{
			passed = false;
			logMessage("Failed to obtain correct results from Graph.getConceptsWithExactType().");
			}
						
		// Test getRelationsWithExactType()
		relResult = g1.getRelationsWithExactType(rt1);
		relCorrect = new Relation[3];
		relCorrect[0] = r[0];
		relCorrect[1] = r[1];
		relCorrect[2] = r[2];
		
		if (!compareArrays(relResult, relCorrect))
			{
			passed = false;
			logMessage("Failed to obtain correct results from Graph.getRelationsWithExactType().");
			}
						
		return passed;
  	}

  public String getTestName()
  	{
  	return "Graph Queries Test";
  	}
  	
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestGraphQueries test;
  	
  	test = new TestGraphQueries();
  	test.runAndReport();
  	}  	
  }
