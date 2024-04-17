package notio.examples;

import notio.*;

    /** 
     * An example of how to programmatically construct a 
     * type hierarchy with the Notio package.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.9 $, $Date: 1999/07/04 19:50:51 $
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

public class TypeBuild
	{
		/**
		 * This method takes the specified concept type hierarchy and adds some
		 * types to it.  The process for building relation type hierarchies
		 * is almost identical.
		 */
	public static void buildConceptHierarchy(ConceptTypeHierarchy conceptHierarchy)
		{
		ConceptType dogType, apeType, personType, animalType;

		animalType = new ConceptType("Animal");
		conceptHierarchy.addTypeToHierarchy(animalType);

		dogType = new ConceptType("Dog");
		conceptHierarchy.addTypeToHierarchy(dogType, animalType, null);

		apeType = new ConceptType("Ape");
		conceptHierarchy.addTypeToHierarchy(apeType, animalType, null);
		
		personType = new ConceptType("Person");
		conceptHierarchy.addTypeToHierarchy(personType, animalType, null);
		}

		/**
		 * This method takes the specified relation type hierarchy and adds some
		 * types to it.  
		 */
	public static void buildRelationHierarchy(RelationTypeHierarchy relationHierarchy)
		{
		relationHierarchy.addTypeToHierarchy(new RelationType("Owns"));		
		}
		
		/**
		 * This method takes the specified type hierarchy and adds a type that has
		 * a type definition (ConceptTypeDefinition).  The process for building 
		 * RelationTypeDefinitions is similar.  This method also demonstrates how one
		 * may look up types by their type label.
		 */
	public static void buildConceptTypeDefinition(ConceptTypeHierarchy conceptHierarchy,
		RelationTypeHierarchy relationHierarchy)
		{
		ConceptType dogType, personType, petDogType;
		RelationType ownsType;
		Graph differentia;
		Concept someDog, somePerson;
		Relation ownsRelation;
		ConceptTypeDefinition definition;
		
		// Look up existing types
		personType = conceptHierarchy.getTypeByLabel("Person");
		dogType = conceptHierarchy.getTypeByLabel("Dog");

		ownsType = relationHierarchy.getTypeByLabel("Owns");

		// Build differentia graph
		differentia = new Graph();
		someDog = new Concept(dogType);
		somePerson = new Concept(personType);
		differentia.addConcept(someDog);
		differentia.addConcept(somePerson);
		
		ownsRelation = new Relation(ownsType);
		ownsRelation.setArgument(0, somePerson);
		ownsRelation.setArgument(1, someDog);
		differentia.addRelation(ownsRelation);
		
		// Build concept type definition, specifying someDog as the formal parameter
		definition = new ConceptTypeDefinition(someDog, differentia);
		
		// Build the new "PetDog" type using the concept type definition.
		petDogType = new ConceptType("PetDog", definition);		
		
		// Add the new concept type to the concept type hierarchy with the "Dog" type as
		// a supertype.
		conceptHierarchy.addTypeToHierarchy(petDogType, dogType, null);
		}
		
		/**
		 * This is a main method showing the invocation of the example methods.
		 */
	public static void main(String args[])
		{
		KnowledgeBase kBase;

		kBase = new KnowledgeBase();
		buildConceptHierarchy(kBase.getConceptTypeHierarchy());
		buildRelationHierarchy(kBase.getRelationTypeHierarchy());
		buildConceptTypeDefinition(kBase.getConceptTypeHierarchy(), 
			kBase.getRelationTypeHierarchy());
		}
	}
