package notio.examples;

import notio.*;

    /** 
     * An example of how to use a matching scheme to test if two graphs match.

     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8 $, $Date: 1999/05/04 01:36:12 $
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
public class MatchTwoGraphs
  {
  	/**
  	 * An application main() method for this example.
  	 *
  	 * @param args  an array of String containing the command line arguments.
  	 */
  public static void main(String args[])
  	{
  	Graph firstGraph, secondGraph;

		// One would initialize the two graphs by loading them from a file or database
		// or constructing them programmatically (see notio.examples.CGIFTranslation and
		// notio.examples.SimpleGraphBuild).
		// We will set them to empty graphs here to stop the compiler complaining so we can
		// show the syntax of the actual matching without worrying about getting real graphs.
		firstGraph = new Graph();
		secondGraph = new Graph();		

		// First graph is query graph.

		// Now we construct a matching scheme with all of the flags set the way we want.
		// Note that we have set the newMaxMatches argument to 0 so we will get all possible
		// matches.  We have also set newNestedScheme to null so that the same matching scheme
		// will be used at all levels of a compound (nested) graph.
		// Note, in general terms, this matching scheme attempts to determine if the meaning
		// one graph is entirely expressed (though possibly more specifically) in another,
		// possibly larger graph.  It is also set to find all possible node mappings with these
		// characteristics.
		
		MatchingScheme scheme;

		scheme = new MatchingScheme(
			MatchingScheme.GR_MATCH_SUBGRAPH,
			MatchingScheme.CN_MATCH_ALL,
			MatchingScheme.RN_MATCH_ALL,
			MatchingScheme.CT_MATCH_SUBTYPE,
			MatchingScheme.RT_MATCH_SUBTYPE,
			MatchingScheme.QF_MATCH_ANYTHING,
			MatchingScheme.DG_MATCH_RESTRICTION,
			MatchingScheme.MARKER_MATCH_ID,
			MatchingScheme.ARC_MATCH_CONCEPT,
			MatchingScheme.COREF_AUTOMATCH_ON,
			MatchingScheme.COREF_AGREE_OFF,
			MatchingScheme.FOLD_MATCH_OFF,
			MatchingScheme.CONN_MATCH_ON,
			0,
			null,
			null);

		// Now we perform the actual match which returns a MatchResult instance.
		MatchResult result;

		result = Graph.matchGraphs(firstGraph, secondGraph, scheme);
		
		// Do a simple test to see if any matches were found.
		if (result.matchSucceeded())
			System.out.println("One or more graph matches were found.");
		else
			System.out.println("No graph matches found.");
			
		// We can also check the number of matches found.
		System.out.println(result.getNumberOfMatches() + " match(s) were found.");
		
		// Finally, we can get an array of NodeMappings.  There is one NodeMapping for each
		// match found.  These node mappings can be used to determine the matching pairs of
		// nodes from the two graphs.

		NodeMapping mappings[];
		
		mappings = result.getMappings();
  	}
  }
