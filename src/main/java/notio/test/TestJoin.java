package notio.test;

import notio.*;

    /** 
     * Class used to test the join() methods in Graph.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.1.2.1 $, $Date: 1999/10/11 01:16:45 $
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
public class TestJoin extends TesterBase
  { 	
  	/** Flag indicating whether or not this test has passed. **/
  private boolean passed;
  
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest()
  	{
  	TestFixtures.ConceptTypeHierarchyFixtureOne cHierFixture;
  	TestFixtures.RelationTypeHierarchyFixtureOne rHierFixture;
  	TestFixtures.SimpleGraphFixtureOne simpleGraphs;
  	MatchingScheme matchScheme;
  	CopyingScheme copyScheme;
  	Graph joinedGraph;
  	
		passed = true;
		
		// Construct fixtures
  	cHierFixture = new TestFixtures.ConceptTypeHierarchyFixtureOne();
  	rHierFixture = new TestFixtures.RelationTypeHierarchyFixtureOne();
		simpleGraphs = new TestFixtures.SimpleGraphFixtureOne(cHierFixture, rHierFixture);
		
		// Build matching scheme
		matchScheme = new MatchingScheme(
			MatchingScheme.GR_MATCH_ANYTHING,
			MatchingScheme.CN_MATCH_ALL,
			MatchingScheme.RN_MATCH_ALL,
			MatchingScheme.CT_MATCH_SUBTYPE,
			MatchingScheme.RT_MATCH_SUBTYPE,
			MatchingScheme.QF_MATCH_ANYTHING,
			MatchingScheme.DG_MATCH_RESTRICTION,
			MatchingScheme.MARKER_MATCH_ID,
			MatchingScheme.ARC_MATCH_CONCEPT,
			MatchingScheme.COREF_AUTOMATCH_ON,
			MatchingScheme.COREF_AGREE_OFF,
			MatchingScheme.FOLD_MATCH_OFF,
			MatchingScheme.CONN_MATCH_ON,
			1,
			null,
			null);
		
		// Build CopyingScheme
		copyScheme = new CopyingScheme(
			CopyingScheme.GR_COPY_DUPLICATE,
			CopyingScheme.CN_COPY_DUPLICATE,
			CopyingScheme.RN_COPY_DUPLICATE,
			CopyingScheme.DG_COPY_DUPLICATE,
			CopyingScheme.COMM_COPY_ON,
			null);
		
		// Join two graphs using Graph.join(Graph, Concept, Graph, Concept, MatchingScheme, 
		// CopyingScheme)
		try
			{
			joinedGraph = Graph.join(simpleGraphs.fourthSimpleGraph, simpleGraphs.sg4C2,
				simpleGraphs.fifthSimpleGraph, simpleGraphs.sg5C1, matchScheme, copyScheme);
			}
		catch (JoinException e)
			{
			logMessage("Exception occured during join: " + e.getMessage());
			return false;
			}

		// Checked join result for correct number of concepts.
		if (joinedGraph.getNumberOfConcepts() != 3)
			{
			passed = false;
			logMessage("Joined graph does not have the correct number of concepts: " + joinedGraph.getNumberOfConcepts());
			}
		
		// Checked join result for correct number of relations.
		if (joinedGraph.getNumberOfRelations() != 2)
			{
			passed = false;
			logMessage("Joined graph does not have the correct number of relations: " + joinedGraph.getNumberOfRelations());
			}
			
		// Check for correct number of each type of concept
		Concept concepts[];
		int numATypeConcepts = 0;
		int numBTypeConcepts = 0;
		int numCTypeConcepts = 0;
		
		concepts = joinedGraph.getConcepts();
		
		for (int con = 0; con < concepts.length; con++)
			{
			if (concepts[con].getType().equals(cHierFixture.ctA))
				numATypeConcepts++;

			if (concepts[con].getType().equals(cHierFixture.ctB))
				numBTypeConcepts++;

			if (concepts[con].getType().equals(cHierFixture.ctC))
				numCTypeConcepts++;
			}

		if (numATypeConcepts != 1)
			{
			logMessage("Joined graph has wrong number of type A concepts: "+ numATypeConcepts);
			passed = false;
			}
				
		if (numBTypeConcepts != 1)
			{
			logMessage("Joined graph has wrong number of type B concepts: "+ numBTypeConcepts);
			passed = false;
			}
				
		if (numCTypeConcepts != 1)
			{
			logMessage("Joined graph has wrong number of type C concepts: "+ numCTypeConcepts);
			passed = false;
			}
							
		// Check for correct number of each type of relation		
		Relation relations[];
		int numCTypeRelations = 0;
		int numDTypeRelations = 0;
		
		relations = joinedGraph.getRelations();
		
		for (int rel = 0; rel < relations.length; rel++)
			{
			if (relations[rel].getType().equals(rHierFixture.rtC))
				numCTypeRelations++;

			if (relations[rel].getType().equals(rHierFixture.rtD))
				numDTypeRelations++;
			}

		if (numCTypeRelations != 1)
			{
			logMessage("Joined graph has wrong number of type C relations: "+ numCTypeRelations);
			passed = false;
			}
			
		if (numDTypeRelations != 1)
			{
			logMessage("Joined graph has wrong number of type D relations: "+ numDTypeRelations);
			passed = false;
			}
			
		// Check for correct arcs
		int numABCArcs = 0;
		int numBCDArcs = 0;
		
		for (int rel = 0; rel < relations.length; rel++)
			{
			Concept args[];
			
			args = relations[rel].getArguments();
			
			if (relations[rel].getType().equals(rHierFixture.rtC))
				if ((args[0].getType().equals(cHierFixture.ctA) && args[1].getType().equals(cHierFixture.ctB)) ||
					(args[0].getType().equals(cHierFixture.ctB) && args[1].getType().equals(cHierFixture.ctA)))
					{
					numABCArcs++;
					}

			if (relations[rel].getType().equals(rHierFixture.rtD))
				if ((args[0].getType().equals(cHierFixture.ctB) && args[1].getType().equals(cHierFixture.ctC)) ||
					(args[0].getType().equals(cHierFixture.ctC) && args[1].getType().equals(cHierFixture.ctB)))
					{
					numBCDArcs++;
					}
			}

		if (numABCArcs != 1)
			{
			logMessage("Joined graph has wrong number of A-C-B clauses: "+ numABCArcs);
			passed = false;
			}
			
		if (numBCDArcs != 1)
			{
			logMessage("Joined graph has wrong number of B-D-C clauses: "+ numBCDArcs);
			passed = false;
			}		
			
		return passed;
  	}

  public String getTestName()
  	{
  	return "Join Test";
  	}
  	
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestJoin test;
  	
  	test = new TestJoin();
  	test.runAndReport();
  	}  	
  }
