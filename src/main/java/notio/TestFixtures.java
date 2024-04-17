package notio.test;

import notio.*;

    /** 
     * Abstract class used to contain various static inner classes that construct 
     * various structures (fixtures) used in other tests.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.1.2.4 $, $Date: 1999/10/10 23:59:14 $
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
abstract public class TestFixtures
  {
  	/**
  	 * An inner class that builds concept types and a concept type hierarchy to act as 
  	 * a testing fixture.
  	 */
  public static class ConceptTypeHierarchyFixtureOne
  	{
  		/** The ConceptTypeHierarchy forming this fixture. **/
  	public ConceptTypeHierarchy cHier;

  		/** All of the concept types in the hierarchy. **/
  	public ConceptType ctUni, ctAbs, ctA, ctB, ctC, ctD, ctE, ctF, ctG, ctH, ctI, ctJ,
  		ctK, ctL, ctM, ctN;
  		
  		/**
  		* Constructs a ConceptTypeHierarchyFixtureA object to act as a testing fixture.
  		* The concept hierarchy created is as follows:<BR>
  		* A < UNIVERSAL      <BR>
  		* B < UNIVERSAL      <BR>
  		* C < UNIVERSAL      <BR>
  		* D < UNIVERSAL      <BR>
  		* E < A				      <BR>
  		* F < A				      <BR>
  		* G < A				      <BR>
  		* H < A, B			      <BR>
  		* I < B, C			      <BR>
  		* J < A, B, C	      <BR>
  		* K < E							<BR>
  		* L < F, G						<BR>
  		* M < G, H						<BR>
  		* N < I, J			      <BR>
  		*/
	  public ConceptTypeHierarchyFixtureOne()
  		{
  		ConceptType superTypes[];
  		
  		cHier = new ConceptTypeHierarchy();
  		ctUni = cHier.getTypeByLabel(ConceptTypeHierarchy.UNIVERSAL_TYPE_LABEL);
  		ctAbs = cHier.getTypeByLabel(ConceptTypeHierarchy.ABSURD_TYPE_LABEL);
  		ctA = new ConceptType("A");
  		ctB = new ConceptType("B");
  		ctC = new ConceptType("C");
  		ctD = new ConceptType("D");
  		ctE = new ConceptType("E");
  		ctF = new ConceptType("F");
  		ctG = new ConceptType("G");
  		ctH = new ConceptType("H");
  		ctI = new ConceptType("I");
  		ctJ = new ConceptType("J");
  		ctK = new ConceptType("K");
  		ctL = new ConceptType("L");
  		ctM = new ConceptType("M");
  		ctN = new ConceptType("N");
  		
  		cHier.addTypeToHierarchy(ctA);
  		cHier.addTypeToHierarchy(ctB);
  		cHier.addTypeToHierarchy(ctC);
  		cHier.addTypeToHierarchy(ctD);

  		cHier.addTypeToHierarchy(ctE, ctA, null);
  		cHier.addTypeToHierarchy(ctF, ctA, null);
  		cHier.addTypeToHierarchy(ctG, ctA, null);

			superTypes = new ConceptType[2];
			superTypes[0] = ctA;
			superTypes[1] = ctB;
  		cHier.addTypeToHierarchy(ctH, superTypes, null);
  		
			superTypes = new ConceptType[2];
			superTypes[0] = ctB;
			superTypes[1] = ctC;
  		cHier.addTypeToHierarchy(ctI, superTypes, null);
  		
			superTypes = new ConceptType[3];
			superTypes[0] = ctA;
			superTypes[1] = ctB;
			superTypes[2] = ctC;
  		cHier.addTypeToHierarchy(ctJ, superTypes, null);  		

  		cHier.addTypeToHierarchy(ctK, ctE, null);

			superTypes = new ConceptType[2];
			superTypes[0] = ctF;
			superTypes[1] = ctG;
  		cHier.addTypeToHierarchy(ctL, superTypes, null);

			superTypes = new ConceptType[2];
			superTypes[0] = ctG;
			superTypes[1] = ctH;
  		cHier.addTypeToHierarchy(ctM, superTypes, null);

			superTypes = new ConceptType[2];
			superTypes[0] = ctI;
			superTypes[1] = ctJ;
  		cHier.addTypeToHierarchy(ctN, superTypes, null);
  		}
    }

  	/**
  	 * An inner class that builds relation types and a relation type hierarchy to act as 
  	 * a testing fixture.
  	 */
	public static class RelationTypeHierarchyFixtureOne
  	{
  		/** The RelationTypeHierarchy forming this fixture. **/
  	public RelationTypeHierarchy rHier;

  		/** All of the relation types in the hierarchy. **/
  	public RelationType rtUni, rtAbs, rtA, rtB, rtC, rtD, rtE, rtF, rtG, rtH, rtI, rtJ,
  		rtK, rtL, rtM, rtN;
  		
 		/**
  		* Constructs a RelationTypeHierarchyFixtureA objert to art as a testing fixture.
  		* The Relation hierarchy created is as follows:<BR>
  		* A < UNIVERSAL      <BR>
  		* B < UNIVERSAL      <BR>
  		* C < UNIVERSAL      <BR>
  		* D < UNIVERSAL      <BR>
  		* E < A				      <BR>
  		* F < A				      <BR>
  		* G < A				      <BR>
  		* H < A, B			      <BR>
  		* I < B, C			      <BR>
  		* J < A, B, C	      <BR>
  		* K < E							<BR>
  		* L < F, G						<BR>
  		* M < G, H						<BR>
  		* N < I, J			      <BR>
  		*/
	  public RelationTypeHierarchyFixtureOne()
  		{
  		RelationType superTypes[];
  		
  		rHier = new RelationTypeHierarchy();
  		rtUni = rHier.getTypeByLabel(RelationTypeHierarchy.UNIVERSAL_TYPE_LABEL);
  		rtAbs = rHier.getTypeByLabel(RelationTypeHierarchy.ABSURD_TYPE_LABEL);
  		rtA = new RelationType("A");
  		rtB = new RelationType("B");
  		rtC = new RelationType("C");
  		rtD = new RelationType("D");
  		rtE = new RelationType("E");
  		rtF = new RelationType("F");
  		rtG = new RelationType("G");
  		rtH = new RelationType("H");
  		rtI = new RelationType("I");
  		rtJ = new RelationType("J");
  		rtK = new RelationType("K");
  		rtL = new RelationType("L");
  		rtM = new RelationType("M");
  		rtN = new RelationType("N");
  		
  		rHier.addTypeToHierarchy(rtA);
  		rHier.addTypeToHierarchy(rtB);
  		rHier.addTypeToHierarchy(rtC);
  		rHier.addTypeToHierarchy(rtD);

  		rHier.addTypeToHierarchy(rtE, rtA, null);
  		rHier.addTypeToHierarchy(rtF, rtA, null);
  		rHier.addTypeToHierarchy(rtG, rtA, null);

			superTypes = new RelationType[2];
			superTypes[0] = rtA;
			superTypes[1] = rtB;
  		rHier.addTypeToHierarchy(rtH, superTypes, null);
  		
			superTypes = new RelationType[2];
			superTypes[0] = rtB;
			superTypes[1] = rtC;
  		rHier.addTypeToHierarchy(rtI, superTypes, null);
  		
			superTypes = new RelationType[3];
			superTypes[0] = rtA;
			superTypes[1] = rtB;
			superTypes[2] = rtC;
  		rHier.addTypeToHierarchy(rtJ, superTypes, null);  		

  		rHier.addTypeToHierarchy(rtK, rtE, null);

			superTypes = new RelationType[2];
			superTypes[0] = rtF;
			superTypes[1] = rtG;
  		rHier.addTypeToHierarchy(rtL, superTypes, null);

			superTypes = new RelationType[2];
			superTypes[0] = rtG;
			superTypes[1] = rtH;
  		rHier.addTypeToHierarchy(rtM, superTypes, null);

			superTypes = new RelationType[2];
			superTypes[0] = rtI;
			superTypes[1] = rtJ;
  		rHier.addTypeToHierarchy(rtN, superTypes, null);
  		}
    }

  	/**
  	 * An inner class that builds some simple Graphs to serve as a testing fixtures.
  	 * Simple graphs have no nested graphs.
  	 * This graph uses the concept and relation type hierarchies from an instance of
  	 * TestFixtures.ConceptTypeHierarchyFixureOne and an instance of
  	 * TestFixtures.RelationTypeHierarchyFixureOne.
  	 */
	public static class SimpleGraphFixtureOne
  	{
  		/** The ConceptTypeHierarchyFixtureOne instance that provides types and hierarchy for
  		 * this graph fixture. **/
  	public ConceptTypeHierarchyFixtureOne cHierFixture;

  		/** The RelationTypeHierarchyFixtureOne instance that provides types and hierarchy for
  		 * this graph fixture. **/
  	public RelationTypeHierarchyFixtureOne rHierFixture;

  		/** The first simple Graph offered by this fixture. **/
  	public Graph firstSimpleGraph;
  	
  		/** Concepts used in firstSimpleGraph. **/
  	public Concept sg1C1;

  		/** Array containing concepts used in firstSimpleGraph. **/
  	public Concept sg1Cons[];
  	
  		/** The second simple Graph offered by this fixture. **/
  	public Graph secondSimpleGraph;
  	
  		/** Concepts used in secondSimpleGraph. **/
  	public Concept sg2C1;

  		/** Array containing concepts used in secondSimpleGraph. **/
  	public Concept sg2Cons[];
  	
  		/** The third simple Graph offered by this fixture. **/
  	public Graph thirdSimpleGraph;
  	
  		/** Concepts used in thirdSimpleGraph. **/
  	public Concept sg3C1, sg3C2;
  	
  		/** Array containing concepts used in thirdSimpleGraph. **/
  	public Concept sg3Cons[];
  	
  		/** Relations used in thirdSimpleGraph. **/
  	public Relation sg3R1;
  	
  		/** Array containing relations used in thirdSimpleGraph. **/
  	public Relation sg3Rels[];
  	
  		/** The fourth simple Graph offered by this fixture. **/
  	public Graph fourthSimpleGraph;
  	
  		/** Concepts used in fourthSimpleGraph. **/
  	public Concept sg4C1, sg4C2;
  	
  		/** Array containing concepts used in fourthSimpleGraph. **/
  	public Concept sg4Cons[];
  	
  		/** Relations used in fourthSimpleGraph. **/
  	public Relation sg4R1;
  	
  		/** Array containing relations used in fourthSimpleGraph. **/
  	public Relation sg4Rels[];
  	
  		/** The fifth simple Graph offered by this fixture. **/
  	public Graph fifthSimpleGraph;
  	
  		/** Concepts used in fifthSimpleGraph. **/
  	public Concept sg5C1, sg5C2;
  	
  		/** Array containing concepts used in fifthSimpleGraph. **/
  	public Concept sg5Cons[];
  	
  		/** Relations used in fifthSimpleGraph. **/
  	public Relation sg5R1;
  	
  		/** Array containing relations used in fifthSimpleGraph. **/
  	public Relation sg5Rels[];
  	
			/**
			 * Constructs a new SimpleGraphFixtureOne, using the specified instances of the 
			 * necessary type hierarchy fixtures.
			 *	<BR>
			 * firstSimpleGraph: []											<BR>
			 * secondSimpleGraph: [A]										<BR>
			 * thirdSimpleGraph: [*x][*y](?x?y)					<BR>
			 * fourthSimpleGraph: [A*x][B*y](C?x?y)			<BR>
			 * fifthSimpleGraph: [B*x][C*y](D?x?y)			<BR>
			 *
			 * @param newCHierFixture  the ConceptTypeHierarchyFixtureOne to be used for types
			 * when creating the graphs.
			 * @param newRHierFixture  the RelationTypeHierarchyFixtureOne to be used for types
			 * when creating the graphs.
			 */
  	public SimpleGraphFixtureOne(ConceptTypeHierarchyFixtureOne newCHierFixture,
  		RelationTypeHierarchyFixtureOne newRHierFixture)
  		{
  		cHierFixture = newCHierFixture;
  		rHierFixture = newRHierFixture;

  		buildFirstSimpleGraph();
  		buildSecondSimpleGraph();
  		buildThirdSimpleGraph();
  		buildFourthSimpleGraph();
  		buildFifthSimpleGraph();
  		}

			/**
			 * Constructs a new SimpleGraphFixtureOne, creating new instances of the necessary
			 * type hierarchy fixtures automatically.
			 */
  	public SimpleGraphFixtureOne()
  		{
  		this(new ConceptTypeHierarchyFixtureOne(), new RelationTypeHierarchyFixtureOne());
  		}

  		/**
  		 * Builds the first simple graph for this fixture.
  		 */
  	private void buildFirstSimpleGraph()
  		{
  		firstSimpleGraph = new Graph();
  		sg1C1 = new Concept();
  		sg1Cons = new Concept[1];
  		sg1Cons[0] = sg1C1;
  		}
  		
  		/**
  		 * Builds the second simple graph for this fixture.
  		 */
  	private void buildSecondSimpleGraph()
  		{
  		secondSimpleGraph = new Graph();
  		sg2C1 = new Concept(cHierFixture.ctA);
  		secondSimpleGraph.addConcept(sg2C1);
  		sg2Cons = new Concept[1];
  		sg2Cons[0] = sg2C1;
  		}

  		/**
  		 * Builds the third simple graph for this fixture.
  		 */
  	private void buildThirdSimpleGraph()
  		{
  		thirdSimpleGraph = new Graph();
  		sg3C1 = new Concept();
  		sg3C2 = new Concept();
  		thirdSimpleGraph.addConcept(sg3C1);
  		thirdSimpleGraph.addConcept(sg3C2);
  		sg3Cons = new Concept[2];
  		sg3Cons[0] = sg3C1;
  		sg3Cons[1] = sg3C2;
  		sg3R1 = new Relation(sg3Cons);
  		thirdSimpleGraph.addRelation(sg3R1);
  		sg3Rels = new Relation[1];
  		sg3Rels[0] = sg3R1;
  		}

  		/**
  		 * Builds the fourth simple graph for this fixture.
  		 */
  	private void buildFourthSimpleGraph()
  		{
  		fourthSimpleGraph = new Graph();
  		sg4C1 = new Concept(cHierFixture.ctA);
  		sg4C2 = new Concept(cHierFixture.ctB);
  		fourthSimpleGraph.addConcept(sg4C1);
  		fourthSimpleGraph.addConcept(sg4C2);
  		sg4Cons = new Concept[2];
  		sg4Cons[0] = sg4C1;
  		sg4Cons[1] = sg4C2;
  		sg4R1 = new Relation(rHierFixture.rtC, sg4Cons);
  		fourthSimpleGraph.addRelation(sg4R1);
  		sg4Rels = new Relation[1];
  		sg4Rels[0] = sg4R1;
  		}

  		/**
  		 * Builds the fifth simple graph for this fixture.
  		 */
  	private void buildFifthSimpleGraph()
  		{
  		fifthSimpleGraph = new Graph();
  		sg5C1 = new Concept(cHierFixture.ctB);
  		sg5C2 = new Concept(cHierFixture.ctC);
  		fifthSimpleGraph.addConcept(sg5C1);
  		fifthSimpleGraph.addConcept(sg5C2);
  		sg5Cons = new Concept[2];
  		sg5Cons[0] = sg5C1;
  		sg5Cons[1] = sg5C2;
  		sg5R1 = new Relation(rHierFixture.rtD, sg5Cons);
  		fifthSimpleGraph.addRelation(sg5R1);
  		sg5Rels = new Relation[1];
  		sg5Rels[0] = sg5R1;
  		}
  	}
  }
