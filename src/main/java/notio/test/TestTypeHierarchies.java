package notio.test;

import notio.*;

    /** 
     * Class used to test the type hierarchy classes.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.15 $, $Date: 1999/08/28 07:03:27 $
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
		 * @bug Need to add test cases for different constructors/uses of RelationTypeDefinition.
     */
public class TestTypeHierarchies extends TesterBase
  {
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest()
  	{
  	boolean passed;
		ConceptTypeHierarchy cHier;
		ConceptType cUni, cAbs;
		ConceptType cA, cB, cC, cD, cE, cF, cG, cH;
		ConceptType cArr1[], cArr2[];
		
		cHier = new ConceptTypeHierarchy();
		cA = new ConceptType("A");
		cB = new ConceptType("B");
		cC = new ConceptType("C");
		cD = new ConceptType("D");
		cE = new ConceptType("E");
		cF = new ConceptType("F");
		cG = new ConceptType("G");
		cH = new ConceptType("H");
		
		passed = true;
		
		cUni = cHier.getTypeByLabel(ConceptTypeHierarchy.UNIVERSAL_TYPE_LABEL);
		
		if (cUni == null)
			{
			logMessage("Failed to retrieve universal concept type via its defined label.");
			passed = false;
			}
		else
			logMessage("Retrieved universal concept type via its defined label.");
		
		cAbs = cHier.getTypeByLabel(ConceptTypeHierarchy.ABSURD_TYPE_LABEL);
		
		if (cAbs == null)
			{
			logMessage("Failed to retrieve absurd concept type via its defined label.");
			passed = false;
			}
		else
			logMessage("Retrieved absurd concept type via its defined label.");
				
		try
			{
	  	cHier.addTypeToHierarchy(cA);
	  	logMessage("Successfully added a concept type with no sub/supertypes.");
	  	}
	  catch (TypeAddError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeAddError whilst adding a concept type with no sub/supertypes: " + e.getMessage());
	  	}
  	
		try
			{
	  	cHier.addTypeToHierarchy(cB);
	  	logMessage("Successfully added a concept type with no sub/supertypes.");
	  	}
	  catch (TypeAddError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeAddError whilst adding a concept type with no sub/supertypes: " + e.getMessage());
	  	}

		//         A     B
  	
		try
			{
	  	cHier.addTypeToHierarchy(cC);
	  	logMessage("Successfully added a concept type with no sub/supertypes.");
	  	}
	  catch (TypeAddError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeAddError whilst adding a concept type with no sub/supertypes: " + e.getMessage());
	  	}

		try
			{
	  	cHier.addTypeToHierarchy(cD, cA, cC);
	  	logMessage("Successfully added a concept type with single subtype and single supertype.");
	  	}
	  catch (TypeAddError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeAddError whilst adding a concept type with single subtype and single supertype: " + e.getMessage());
	  	}
	  catch (TypeChangeError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeChangeError whilst adding a concept type with single subtype and single supertype: " + e.getMessage());
	  	}

		cArr1 = cHier.getProperSubTypesOf(cA);
		cArr2 = new ConceptType[3];
		cArr2[0] = cAbs;
		cArr2[1] = cC;
		cArr2[2] = cD;
		
		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct subtype array using getProperSubTypesOf() on A.");
		else
			{
			passed = false;
			logMessage("Found incorrect subtype array (length " + cArr1.length + ") using getProperSubTypesOf() on A.");
			}
				
		cArr1 = cHier.getProperSubTypesOf(cD);
		cArr2 = new ConceptType[2];
		cArr2[0] = cAbs;
		cArr2[1] = cC;

		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct subtype array using getProperSubTypesOf() on D.");
		else
			{
			passed = false;
			logMessage("Found incorrect subtype array (length " + cArr1.length + ") using getProperSubTypesOf() on D.");
			}
				
		cArr1 = cHier.getProperSuperTypesOf(cA);
		cArr2 = new ConceptType[1];
		cArr2[0] = cUni;
		
		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct supertype array using getProperSuperTypesOf() on A.");
		else
			{
			passed = false;
			logMessage("Found incorrect supertype array (length " + cArr1.length + ") using getProperSuperTypesOf() on A.");
			}
				
		cArr1 = cHier.getProperSuperTypesOf(cD);
		cArr2 = new ConceptType[2];
		cArr2[0] = cUni;
		cArr2[1] = cA;

		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct supertype array using getProperSuperTypesOf() on D.");
		else
			{
			passed = false;
			logMessage("Found incorrect supertype array (length " + cArr1.length + ") using getProperSuperTypesOf() on D.");
			}
				

		cArr1 = cHier.getImmediateSubTypesOf(cA);
		cArr2 = new ConceptType[1];
		cArr2[0] = cD;

		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct immediate subtype array using getImmediateSubTypesOf() on A.");
		else
			{
			passed = false;
			logMessage("Found incorrect subtype array (length " + cArr1.length + ") using getImmediateSubTypesOf() on A.");
			}

				
		cArr1 = cHier.getImmediateSubTypesOf(cD);
		cArr2 = new ConceptType[1];
		cArr2[0] = cC;

		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct immediate subtype array using getImmediateSubTypesOf() on D.");
		else
			{
			passed = false;
			logMessage("Found incorrect subtype array (length " + cArr1.length + ") using getImmediateSubTypesOf() on D.");
			}
				
				
		cArr1 = cHier.getImmediateSuperTypesOf(cA);
		cArr2 = new ConceptType[1];
		cArr2[0] = cUni;

		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct immediate supertype array using getImmediateSuperTypesOf() on A.");
		else
			{
			passed = false;
			logMessage("Found incorrect supertype array (length " + cArr1.length + ") using getImmediateSuperTypesOf() on A.");
			}

				
		cArr1 = cHier.getImmediateSuperTypesOf(cD);
		cArr2 = new ConceptType[1];
		cArr2[0] = cA;

		if (compareArrays(cArr1, cArr2))
			logMessage("Found correct immediate supertype array using getImmediateSubTypesOf() on D.");
		else
			{
			passed = false;
			logMessage("Found incorrect supertype array (length " + cArr1.length + ") using getImmediateSuperTypesOf() on D.");
			}


		//         A     B
		//				/ 
		//       D  
		//       | 
		//       C 

		try
			{
	  	cHier.addTypeToHierarchy(cE);
	  	logMessage("Successfully added a concept type with no sub/supertypes.");
	  	}
	  catch (TypeAddError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeAddError whilst adding a concept type with no sub/supertypes: " + e.getMessage());
	  	}


		try
			{
	  	cHier.addSuperTypeToType(cE, cA);
	  	logMessage("Successfully added a supertype to a concept type.");
	  	}
	  catch (TypeChangeError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeChangeError whilst adding a supertype to a concept type: " + e.getMessage());
	  	}
	  	
		//         A     B
		//				/ \    
		//       D   E
		//       | 
		//       C 
		
		try
			{
	  	cHier.addTypeToHierarchy(cF);
	  	logMessage("Successfully added a concept type with no sub/supertypes.");
	  	}
	  catch (TypeAddError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeAddError whilst adding a concept type with no sub/supertypes: " + e.getMessage());
	  	}

		try
			{
	  	cHier.addSubTypeToType(cB, cF);
	  	logMessage("Successfully added a subtype to a concept type.");
	  	}
	  catch (TypeChangeError e)
	  	{
	  	passed = false;
	  	logMessage("Inappropriately threw TypeChangeError whilst adding a subtype to a concept type: " + e.getMessage());
	  	}

		//         A     B
		//				/ \    |
		//       D   E   F
		//       |
		//       C

		//         A     B
		//				/ \    |
		//       D   E   F
		//       |    \ /
		//       C     H
		//        \   /
		//         \ /
		//          G
		

		return passed;
  	}

  public String getTestName()
  	{
  	return "Type Hierarchy Test";
  	}
  	
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestTypeHierarchies test;
  	
  	test = new TestTypeHierarchies();
  	test.runAndReport();
  	}
  }
