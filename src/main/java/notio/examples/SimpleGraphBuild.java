package notio.examples;

import notio.*;

    /** 
     * An example of how to programmatically construct a 
     * graph using the Notio package.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.9 $, $Date: 1999/05/04 01:36:12 $
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
public class SimpleGraphBuild
  {
  	/**
  	 * An application main() method for this example.
  	 * This method creates the objects necessary to represent a
  	 * simple graph.
  	 *
  	 * @param args  an array of String containing the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	KnowledgeBase kBase;
  	ConceptType animalType, foodType;
  	RelationType eatsType;
  	Concept animalExistsConcept, foodExistsConcept;
  	Relation animalEatsFoodRelation;
 		Concept arguments[];
  	Graph graph;
  	
  	// CREATING A KNOWLEDGE BASE
  	// A knowledge base is not strictly necessary but creating one
  	// is quick way to create and keep track of other useful structures
  	// like a concept type hierarchy and a relation type hierarchy.
  	kBase = new KnowledgeBase();

  	// CREATING A CONCEPT TYPE
  	// Before we create any concepts, we need to create some concept
  	// types and add them to our concept type hierarchy.
  	// These next lines create an "Animal" concept type and add it
  	// to the hierarchy.  Since we don't specify any sub- or supertypes
  	// these will be assumed to be the "Absurd" and "Universal" types
  	// respectively.  When adding the type we must catch any exceptions.
  	// After creating the "Animal" type, we create a "Food" concept type.
  	animalType = new ConceptType("Animal");
  	foodType = new ConceptType("Food");
  	kBase.getConceptTypeHierarchy().addTypeToHierarchy(animalType);
  	kBase.getConceptTypeHierarchy().addTypeToHierarchy(foodType);

	  // CREATING A RELATION TYPE
	  // This procedure is very similar to creating a concept type.
	  // Here we will create an "Eats" relationship.
	  eatsType = new RelationType("Eats");
  	kBase.getRelationTypeHierarchy().addTypeToHierarchy(eatsType);
	  	
	  // CREATING A GRAPH
	  // It is often easiest to create a blank graph and then add
	  // concepts and relations to it when we create them.  The 
	  // following code creates a blank graph.
	  graph = new Graph();
	  
	  // ADDING CONCEPTS TO A GRAPH
	  // Here we create a new concept of type "Animal" with a null
	  // referent (which means it is a generic concept).
	  // The concept means "there exists
	  // some animal".  We then add it to the graph.
	  // After this we repeat the operation to create a concept with
	  // the meaning "there exists some food" and add that to the
	  // graph.
	  animalExistsConcept = new Concept(animalType, null);
	  graph.addConcept(animalExistsConcept);

	  foodExistsConcept = new Concept(foodType, null);
	  graph.addConcept(foodExistsConcept);
	  
	  // ADDING RELATIONS TO A GRAPH
	  // We create an relation that links the two concepts together
	  // and then we add it to the graph.  In the end we have a graph
	  // that states "there exists some animal and there exists some
	  // food such that the animal eats the food".
	  arguments = new Concept[2];
	  arguments[0] = animalExistsConcept;
	  arguments[1] = foodExistsConcept;
	  
	  animalEatsFoodRelation = new Relation(eatsType, arguments);

	  graph.addRelation(animalEatsFoodRelation);
	  System.out.println("DID "+graph.toString());
  	}
  }
