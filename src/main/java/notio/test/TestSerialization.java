package notio.test;

import java.io.*;
import notio.*;

    /** 
     * Class used to test the serializability.
     * Note that serialization is implementation specific, therefore we need only
     * test the serialization and deserialization of a KnowledgeBase generated using
     * the current implementation.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.4 $, $Date: 1999/08/01 22:07:00 $
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
     * @bug Should add more structures to ensure adequate coverage.
     */
public class TestSerialization extends TesterBase
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
  	 * Initialize knowledge base.
  	 */
  public void initializeKnowledgeBase()
  	{	
  	RelationTypeHierarchy rHier;
  	Concept args[] = new Concept[2];
  	Concept outermostContext;
  	
  	kBase = new KnowledgeBase();
  	
  	rHier = kBase.getRelationTypeHierarchy();

		outermostContext = kBase.getOutermostContext();
		
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
		
		
		outermostContext.setReferent(new Referent(g1));
  	}
  	
  	/**
  	 * Standard test access method.
  	 *
  	 * @return true if test was passed, false otherwise.
  	 */
  public boolean runTest()
  	{
  	ObjectOutputStream oStream = null;
  	ByteArrayOutputStream oByteStream;
		ObjectInputStream iStream = null;
		KnowledgeBase endBase;
  	
		passed = true;
		
		// Test simplify(Relation)
		initializeKnowledgeBase();

		oByteStream = new ByteArrayOutputStream();
		
		try
			{
			oStream = new ObjectOutputStream(oByteStream);
			}
		catch (IOException e)
			{
			e.printStackTrace();
			System.exit(1);
			}
			
		// Write the knowledge base to the stream.
		try
			{
			oStream.writeObject(kBase);
			}
		catch (InvalidClassException e)
			{
			passed = false;
			logMessage("Failed to serialize class and encountered exception: " + e.getMessage());
			}
		catch (NotSerializableException e)
			{
			passed = false;
			logMessage("Failed to serialize class and encountered exception: " + e.getMessage());
			}
		catch (IOException e)
			{
			// Should only occur if there is a problem with the underlying streams.
			e.printStackTrace();
			System.exit(1);
			}
			
		try
			{
			iStream = new ObjectInputStream(new ByteArrayInputStream(oByteStream.toByteArray()));
			}
		catch (IOException e)
			{
			e.printStackTrace();
			System.exit(1);
			}
		
		// Read the knowledge base from the stream.
		try
			{
			endBase = (KnowledgeBase)iStream.readObject();
			}
		catch (ClassNotFoundException e)
			{
			passed = false;
			logMessage("Failed to deserialize class and encountered exception: " + e.getMessage());
			}
		catch (InvalidClassException e)
			{
			passed = false;
			logMessage("Failed to deserialize class and encountered exception: " + e.getMessage());
			}
		catch (StreamCorruptedException e)
			{
			passed = false;
			logMessage("Failed to deserialize class and encountered exception: " + e.getMessage());
			}
		catch (OptionalDataException e)
			{
			passed = false;
			logMessage("Failed to deserialize class and encountered exception: " + e.getMessage());
			}
		catch (IOException e)
			{
			// Should only occur if there is a problem with the underlying streams.
			e.printStackTrace();
			System.exit(1);
			}
			
		return passed;
  	}

  public String getTestName()
  	{
  	return "Serialization Test";
  	}
  	
  	/**
  	 * A main method so this test can be run independently as an application.
  	 *
  	 * @param args  the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	TestSerialization test;
  	
  	test = new TestSerialization();
  	test.runAndReport();
  	}  	
  }
