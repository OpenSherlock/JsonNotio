package notio;

    /** 
     * The base class for graph nodes.  Provides the admittedly tiny
     * amount of common functionality and serves chiefly to make
     * collections of nodes simpler to manage.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8 $, $Date: 1999/06/02 01:35:45 $
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
     * @bug Should this be in the API or not?
     */

abstract public class Node implements java.io.Serializable
  {
  /** The type for this node. **/
  private Type type;
  /** The enclosing graph for this node. **/
  private Graph enclosingGraph;
  /** The comment associated with this node. **/
  private String comment;

  	/**
  	 * Returns the type for this node.
  	 *
  	 * @return the type for this node.
  	 * @impspec This mechanism relies on a common Type class which is non-standard.
  	 */
  Type getNodeType()
  	{
  	return type;
  	}
  	
  	/**
  	 * Sets the type for this node.
  	 *
  	 * @param newType  the type for this node.
  	 * @impspec This mechanism relies on a common Type class which is
  	 * non-standard.
  	 */
  void setNodeType(Type newType)
  	{
  	type = newType;
  	}
  
    /**
     * Sets the enclosing graph for this node.
     *
     * @param newGraph  the enclosing graph.
     *
     * @impspec this method is present to support the getEnclosingGraph() method.
     */
  void setEnclosingGraph(Graph newGraph)
  	{
  	enclosingGraph = newGraph;
  	}

    /**
     * Returns the graph that encloses this node or null if the node does not currently 
     * belong to a graph.
     *
     * @return the node's enclosing graph.
     */
  public Graph getEnclosingGraph()
    {
    return enclosingGraph;
    }  
  
    /**
     * Sets the comment string for this node.
     *
     * @param newComment  the new comment string for this node.
     */
  public void setComment(String newComment)
    {
    comment = newComment;
    }

    /**
     * Returns the comment string for this node.
     *
     * @return the comment string associated with this node or null.
     */
  public String getComment()
    {
    return comment;
    }
  }
