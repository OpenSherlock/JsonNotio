package notio;

import java.io.Serializable;

    /** 
     * The concept type definition class.  
     * This class provides the functionality of a monadic lambda expression used
     * for describing a concept type.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.13 $, $Date: 1999/08/28 06:59:18 $
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
public class ConceptTypeDefinition implements Serializable
  {
  	/** The abstraction used in this definition. **/
  private Abstraction abstraction;
  
		/** The signature used in this definition. **/
	private ConceptType signature;
	
    /**
     * Constructs a new concept type definition using the differentia graph
     * and the concept in that graph that acts as a formal parameter for
     * this definition.  The parameter concept must have a null referent.
     * The signature of this definition is derived from the formal parameter.
     *
     * @param newParameter  the single concept that is the formal parameter 
     * used in this definition.
     * @param newDifferentia  the differentia graph for this definition.
     */
  public ConceptTypeDefinition(Concept newParameter, Graph newDifferentia)
    {
    Concept formals[] = new Concept[1];

    formals[0] = newParameter;
    abstraction = new Abstraction(formals, newDifferentia);
    signature = formals[0].getType();
    }

    /**
     * Constructs a new concept type definition using the specified signature.
     *
     * @param newSignature  the single concept type that forms the signature
     * used in this definition.
     */
  public ConceptTypeDefinition(ConceptType newSignature)
    {
    signature = newSignature;
    }

    /**
     * Returns the differentia graph for this definition or null if no differentia graph has 
     * been specified.
     *
     * @return the differentia graph for this definition.
     */
  public Graph getDifferentia()
    {
    if (abstraction == null)
    	return null;
    else
	    return abstraction.getBody();
    }

    /**
     * Returns the formal parameter concept or null if no differentia graph has been
     * specified.
     *
     * @return the formal parameter concept.
     */
  public Concept getFormalParameter()
    {
    if (abstraction == null)
    	return null;
    else
	    return abstraction.getFormalParameters()[0];
    }

    /**
     * Returns the signature of this definition.
     *
     * @return the signature of this definition.
     */
  public ConceptType getSignature()
    {
    return signature;
    }
  }
