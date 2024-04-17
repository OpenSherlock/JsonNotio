package notio;

import java.util.Vector;
import java.io.Serializable;

    /** 
     * The marker class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.18 $, $Date: 1999/06/02 05:26:30 $
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
		 * @idea If we switch to JDK 1.2, consider allowing weak references to individuals. 
     */

public class Marker implements Serializable
  {
  /** MarkerSet to which this marker belongs. **/
  MarkerSet markerSet;
  /** Individual associated with this marker. **/
  private Object individual;
  /** The string indentifier for this marker. **/
  String markerID;
  /** Set of types to which this marker conforms. **/
  private Set types = new Set();

    /**
     * Construct a new marker belonging to the specified marker set and
     * with the specified Object as its individual.
     *
     * @param newMarkerSet  the marker set to which this marker will belong.
     * @param newIndividual  the Object that is the individual corresponding
     *                       to this marker.
     */
  public Marker(MarkerSet newMarkerSet, Object newIndividual)
    {
    markerSet = newMarkerSet;
    individual = newIndividual;
    markerID = markerSet.addMarker(this);
    }

    /**
     * Construct a new marker belonging to the specified marker set.
     *
     * @param newMarkerSet  the marker set to which this marker will belong.
     */
  public Marker(MarkerSet newMarkerSet)
    {
    this(newMarkerSet, null);
    }

    /**
     * Returns the marker ID for this marker.  This is a string that unique represents the
     * marker within its current marker set.  Note, the ID is automatically assigned when the
     * marker is added to its marker set.
     *
     * @return the ID for this marker.
     */
  public String getMarkerID()
    {
    return markerID;
    }

    /**
     * Frees the individual associated with this marker.
     * This means that the reference to the individual will be set to null, allowing
     * it to be garbage-collected if no other references exist.  The marker remains
     * as an entry in its MarkerSet but is no longer associated with an individual.
     * If no individual is associated with this Marker when freeIndividual() is called,
     * nothing happens.
     */
  public void freeIndividual()
     {
     if (individual == null)
     	return;
     	
     markerSet.removeMarkerIndividual(individual);
     individual = null;
     }

    /**
     * Returns the individual corresponding to this marker.
     *
     * @return the Object that is the individual corresponding to this marker.
     */
  public Object getIndividual()
    {
    return individual;
    }

    /**
     * Returns the marker set to which this marker belongs.
     *
     * @return the MarkerSet for this marker.
     */
  public MarkerSet getMarkerSet()
    {
    return markerSet;
    }

    /**
     * Test whether the marker conforms to the specified concept type.
     *
     * @param queryType  the concept type we are testing for conformance.
     *
     * @return true if this marker conforms to the specified concept type.
     * @bug Should probably test for subtype as well.
     */
  public boolean conformsToType(ConceptType queryType)
    {
    return types.contains(queryType);
    }

    /**
     * Adds a new concept type to the set of those to which this marker conforms.
     *
     * @param newType  the concept type being added.
     */
  public void addTypeConformance(ConceptType newType)
    {
    types.addElement(newType);
    }

    /**
     * Removes a concept type from the set of those to which this marker conforms.
     *
     * @param deadType  the concept type being removed.
     */
  public void removeTypeConformance(ConceptType deadType)
    {
    types.removeElement(deadType);
    }
  }
