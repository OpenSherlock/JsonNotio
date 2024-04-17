package notio;

import java.util.Hashtable;
import java.io.Serializable;

    /** 
     * The actor node class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.27 $, $Date: 1999/08/02 08:09:52 $
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

public class Actor extends Relation implements Serializable
  {
    /**
     * Constructs an actor with the given type and arguments.
     * The last argument is assumed to be the sole output argument.
     * The number of arguments must conform to the valence of the
     * relation type unless it is unspecified in which case the valence
     * is set to the number of arguments in the array.
     *
     * @param newType  the type for this actor.
     * @param newArguments  the concepts related by this actor.
     */
  public Actor(RelationType newType, Concept newArguments[])
    {
    super(newType, newArguments);
    }

    /**
     * Constructs an actor with the given type and arguments and output argument start 
     * index.
     * The number of arguments must conform to the valence of the
     * relation type unless it is unspecified in which case the valence
     * is set to the number of arguments in the array.
     *
     * @param newType  the type for this actor.
     * @param newArguments  the concepts related by this actor.
     * @param newOutputStartIndex  the index into the newArguments array at which the
     * output arcs start.
     */
  public Actor(RelationType newType, Concept newArguments[], int newOutputStartIndex)
    {
    super(newType, newArguments, newOutputStartIndex);
    }

    /**
     * Constructs an actor with the given type and the specified input and output arguments.
     * The number of arguments must conform to the valence of the
     * relation type unless it is unspecified in which case the valence
     * is set to the number of arguments in the array.
     *
     * @param newType  the type for this actor.
     * @param newInputArguments  the input concepts related by this actor.
     * @param newOutputArguments  the output concepts related by this actor.
     */
  public Actor(RelationType newType, Concept newInputArguments[], Concept newOutputArguments[])
    {
    super(newType, newInputArguments, newOutputArguments);
    }

    /**
     * Constructs an actor with the given type.
     * The valence is taken from the specified type and the arguments 
     * are initialized to null.  If the type's valence is unspecified, 
     * a zero-length argument array is used for construction.
     * For a specified non-zero valence, the last argument is assumed to be the
     * sole output argument.
     *
     * @param newType  the type for this actor.
     */
  public Actor(RelationType newType)
    {
    super(newType);
    }

    /**
     * Constructs an actor with no type and the specified arguments with output arguments
     * starting at the specified index.
     *
     * @param newArguments  the concepts related by this actor.
     * @param newOutputStartIndex  the index into the newArguments array at which the
     * output arcs start.
     */
  public Actor(Concept newArguments[], int newOutputStartIndex)
    {
    super(newArguments, newOutputStartIndex);
    }

    /**
     * Constructs an actor with no type and the specified arguments.
     * For a non-zero valence, the last argument is assumed to be the
     * sole output argument.
     *
     * @param newArguments  the concepts related by this actor.
     */
  public Actor(Concept newArguments[])
    {
    super(newArguments);
    }

    /**
     * Constructs an actor that has no type and no arguments.
     * The number of arguments is automatically set to zero.
     *
     * @bug Should the default be monadic rather than 0-adic?
     */
  public Actor()
    {
    super();
    }

    /**
     * Performs a copy operation on this actor according to the
     * the specified CopyingScheme.
     * The result may be a new node or simply a reference to this actor
     * depending on the scheme.
     * Note that the returned object is truly an Actor object even though the return
     * type is Relation.  This is because Java does not allow overriding of return types.
     * 
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @return the result of the copy operation.
     */
  public Relation copy(CopyingScheme copyScheme)
    {
    return copy(copyScheme, new Hashtable());
    }
    
    /**
     * Performs a copy operation on this actor according to the
     * the specified CopyingScheme.
     * The result may be a new node or simply a reference to this actor
     * depending on the scheme.
     * Note that the returned object is truly an Actor object even though the return
     * type is Relation.  This is because Java does not allow overriding of return types.
     * 
     * @param copyScheme  the copying scheme used to control the copy operation.
     * @param substitutionTable  a hashtable containing copied objects available due to 
     * earlier copy operations.
     * @return the result of the copy operation.
     */
  public Relation copy(CopyingScheme copyScheme, Hashtable substitutionTable)
    {
		return super.copy(copyScheme, substitutionTable);
    }
  }
