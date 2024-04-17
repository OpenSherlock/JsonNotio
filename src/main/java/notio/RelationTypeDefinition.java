package notio;

import java.io.Serializable;

    /** 
     * The relation type definition class.
     * This class provides the functionality of a lambda expression used
     * for describing a relation type.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.13 $, $Date: 1999/08/28 06:59:19 $
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
public class RelationTypeDefinition implements Serializable
  {
	  /** The abstraction used in this definition. **/
  private Abstraction abstraction;
  
  	/** The signature used in this definition. **/
  private ConceptType signature[];

    /**
     * Constructs a new relation type definition with the specified relator
     * graph and the array of formal parameter concepts.  The formal parameter
     * concepts must be part of the graph and have a null referent.
     * The signature of this definition is derived from the formal parameters.
     *
     * @param newParameters  the array of formal parameter concepts used in this
     * definition.
     * @param newRelator  the relator graph for this definition.
     */
  public RelationTypeDefinition(Concept newParameters[], Graph newRelator)
    {
    abstraction = new Abstraction(newParameters, newRelator);
    signature = new ConceptType[newParameters.length];
    for (int par = 0; par < newParameters.length; par++)
    	signature[par] = newParameters[par].getType();
    }

    /**
     * Constructs a new relation type definition with the specified signature.
     *
     * @param newSignature  the array of concept types that form the signature of this
     * definition.
     */
  public RelationTypeDefinition(ConceptType newSignature[])
    {
    if (newSignature == null)
    	throw new IllegalArgumentException("Signature can not be null.");

    signature = new ConceptType[newSignature.length];
    	
    System.arraycopy(newSignature, 0, signature, 0, newSignature.length);
    }

    /**
     * Returns the relator graph for this definition or null if no relator has been
     * specified.
     *
     * @return the relator graph for this definition.
     */
  public Graph getRelator()
    {
    if (abstraction == null)
    	return null;
    else
	    return abstraction.getBody();
    }

    /**
     * Returns the formal parameter concepts for this definition or null if no
     * formal parameters have been specified.
     *
     * @return the formal parameter concepts for this definition.
     */
  public Concept[] getFormalParameters()
    {
    if (abstraction == null)
    	return null;
    else
	    return abstraction.getFormalParameters();
    }
    
    /**
     * Returns the signature for this definition.
     * The signature consists of the concept types of the formal parameters. 
     * No relator graph need have been specified in order to have a signature.
     *
     * @return the signature for this definition.
     */
  public ConceptType[] getSignature()
    {
    ConceptType signatureCopy[];
    
    signatureCopy = new ConceptType[signature.length];
    System.arraycopy(signature, 0, signatureCopy, 0, signature.length);
    
    return signatureCopy;
    }
    
    /**
     * Returns the valence (number of formal parameters) for this definition.
     *
     * @return the valence for this definition.
     */
  public int getValence()
    {
    return signature.length;
    }
  }
