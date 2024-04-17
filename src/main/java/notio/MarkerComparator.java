package notio;

    /** 
     * Interface for marker comparators.  During graph matching, and in other situations,
     * the Notio package may need to establish whether two markers are equivalent (refer
     * to the same entity).  
  	 * This decision is likely to be application-specific since markers may refer to 
  	 * entities outside of the scope of the Notio layer, or the application may wish to 
  	 * form relationships when comparisons are made.  In order to allow applications to
  	 * specify the exact rules, they may provide an implementation of this interface as
  	 * part of a MatchingScheme.  The comparator will be called whenever two markers are
  	 * compared.  Possible uses include:
  	 * <UL>
  	 *   <LI>comparing complex data objects such as images referred to by markers
  	 *   <LI>testing and creating equivalencies between markers due to assertions about them
  	 *   <LI>references to external database for which the markers are keys
  	 * </UL>
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.2 $, $Date: 1999/05/04 01:35:54 $
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
     * @see notio.MatchingScheme
     */

public interface MarkerComparator
  {
  	/**
  	 * Called by matching routines when they need to determine whether two individual
  	 * markers should be considered equivalent.  Should return true if the two markers
  	 * are considered to be equivalent, and false otherwise.
  	 *
  	 * @param firstMarker  the first of the two markers being compared.
  	 * @param secondMarker  the second of the two markers being compared.
  	 * @return true if the two markers should be treated as equivalent for matching purpose,
  	 * false otherwise.
  	 */  	 
  public boolean compareMarkers(Marker firstMarker, Marker secondMarker);
  }
