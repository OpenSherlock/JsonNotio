package notio;

import java.util.*;
import java.io.Serializable;

    /** 
     * Class that stores a set of marker objects.  Markers are added to the
     * set by specifying the set in the constructor for the marker.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.16 $, $Date: 1999/06/02 05:25:05 $
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
     * @see notio.Marker
     */

public class MarkerSet implements Serializable
  {
  /** Next marker id. **/
  private int nextMarkerID = 1;
  /** Hashtable for marker lookups by ID. **/
  private Hashtable idTable = new Hashtable();
  /** Hashtable for marker lookups by individual. **/
  private Hashtable individualTable = new Hashtable();

    /**
     * Adds a marker to this marker set and returns the name automatically
     * assigned to it.  This name will always have the form '#nnnn' where
     * 'nnnn' is a unique integer.  Other names may be added later. 
     *
     * @param marker  the marker being added and named.
     * @return the unique marker ID assigned to this marker.
     * @bug Do we need to check if the marker is already in the table?
     * It should never happen, but perhaps it would best.
     */
  String addMarker(Marker marker)
    {
    String markerName;

    markerName = getNextAvailableMarkerID();
    idTable.put(markerName, marker);
    if (marker.getIndividual() != null)
      setMarkerIndividual(marker.getIndividual(), marker);
      
    return markerName;
    }
  
    /**
     * Determines the first unused marker ID.
     * This method theoretically allows IDs to be assigned out of order or manually without 
     * messing things up.
     *
     * @return the first available marker ID for this marker set.
     * @bug Is this no longer necessary since we no longer allow addition of IDs to the table?
     */
  private String getNextAvailableMarkerID()
    {
    String markerID;

    do
      {
      markerID = "" + nextMarkerID;
      nextMarkerID++;
      }
    while (idTable.get(markerID) != null);

    return markerID;
    }
    
    /**
     * Returns the marker associated with the specified name or null.
     *
     * @param markerID  the marker ID being used to lookup.
     * @return the marker associated with this name or null.
     */
  public Marker getMarkerByMarkerID(String markerID)
    {
    return (Marker)idTable.get(markerID);
    }

    /**
     * Returns the marker associated with the specified individual or null.
     *
     * @param individual  the individual being used to lookup.
     * @return the marker associated with this individual or null.
     */
  public Marker getMarkerByIndividual(Object individual)
    {
    return (Marker)individualTable.get(individual);
    }

    /**
     * Associates the specified marker with the specified individual.  Note this
     * method will overwrite any existing association with the individual without
     * checking.  Callers should check for existing individual associations if
     * duplications are possible.  
     *
     * @param individual  the marker's associated individual.
     * @param marker  the marker being associated with the individual.
     *
     * @bug Should this throw an exception?
     */
  void setMarkerIndividual(Object individual, Marker marker)
    {
    individualTable.put(individual, marker);
    }

    /**
     * Removes the individual table entry associated with the specified individual.
     *
     * @param deadIndividual  the individual being removed.
     */
  void removeMarkerIndividual(Object deadIndividual)
    {
    individualTable.remove(deadIndividual);
    }
  }
