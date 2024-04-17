package notio;
import java.io.Serializable;

    /** 
     * The abstraction class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.13 $, $Date: 1999/05/04 01:35:46 $
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

class Abstraction implements Serializable
  {
  /** The formal parameters for this abstraction. **/
  private Concept formalParameters[];
  /** The body of this abstraction. **/
  private Graph body;

    /**
     * Constructs a new abstraction with the specified concepts as formal
     * parameters and the specified graph as a body.  The graph must contain
     * all the parameter concepts.  All parameter concepts should be existentially
     * quantifier and have lambda designators.
     *
     * @param newFormalParameters  array of concepts that will form the 
     *                             formal parameters.
     * @param newBody  graph that will form the body of the abstraction.
     */
  public Abstraction(Concept newFormalParameters[], Graph newBody)
    {
    if (newFormalParameters == null)
    	throw new IllegalArgumentException("Formal parameters can not be null.");
    if (newBody == null)
    	throw new IllegalArgumentException("Body can not be null.");
    formalParameters = newFormalParameters;
    body = newBody;
    }

    /**
     * Returns the body of the abstraction.
     *
     * @return the graph that is the body of the abstraction.
     */
  public Graph getBody()
    {
    return body;
    }

    /**
     * Returns the formal paramters of the abstraction.
     *
     * @return the array of concepts that are the formal paramters of
     *         the abstraction.
     */
  public Concept[] getFormalParameters()
    {
    Concept returnArray[];
    int numParameters = formalParameters.length;

    returnArray = new Concept[numParameters];
    System.arraycopy(formalParameters, 0, returnArray, 0, numParameters);

    return returnArray;
    }
  }
