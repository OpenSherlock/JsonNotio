package notio;

import java.util.*;

    /**
     * A generator class used to extract SimpleMatches from a MatchResult one at a time.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.9 $, $Date: 1999/05/04 01:36:01 $
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
     * @deprecated This class is purely experimental and will be removed shortly.
     */

public class SimpleMatchGenerator
  {
  /** Tracks the node mapping selected from a given MatchResult. **/
  private Hashtable matchResultTable;
  /** Tracks the current choice point (MatchResult) for a given NodeMapping. **/
  private Hashtable nodeMappingTable;
  /** The match result being processed. **/
  private MatchResult rootResult;

  	/**
  	 * Constructs a new generator that will process the specified MatchResult.
  	 *
  	 * @param matchResult  the MatchResult to be processed.
  	 */
  public SimpleMatchGenerator(MatchResult matchResult)
  	{
  	rootResult = matchResult;
  	resetGenerator();
  	}

  	/**
  	 * Resets the generator that will generate from the beginning again.
  	 */
  public void resetGenerator()
  	{
  	matchResultTable = new Hashtable();
  	nodeMappingTable = new Hashtable();
  	}

  	/**
  	 * Processes a MatchResult.
  	 *
  	 * @param matchResult  the MatchResult being processed.
  	 * @param choose  a flag indicating whether this method should advance the search
  	 * or simply return old results.
  	 * @return the MatchResult that is the result of processing.
  	 */
  private MatchResult processMatchResult(MatchResult matchResult, boolean choose)
  	{
  	Integer pickOb;
  	int pickInt, lastPick;
  	NodeMapping mappings[], returnedMapping;
		NodeMapping pickedMapping[] = new NodeMapping[1];


		// Get NodeMappings from MatchResult
		mappings = matchResult.getMappings();

  	// Check for this MatchResult in the table to see if has a picked value
  	pickOb = (Integer)matchResultTable.get(matchResult);

  	// If never picked before, initialize pickInt to 0 and lastPick to -1
  	if (pickOb == null)
  		{
  		lastPick = -1;
  		pickInt = 0;
  		}
  	else
  		{
  		pickInt = pickOb.intValue();
  		lastPick = pickInt;
  		}


		// If we are not advancing our choice, simply process the last mapping we picked.
		// Call it with false so it only returns old results.
		if (!choose)
			{
	 		returnedMapping = processNodeMapping(mappings[pickInt], false);
			pickedMapping[0] = returnedMapping;

			return new MatchResult(pickedMapping);
			}

		// Find next choice
  	while (pickInt < mappings.length)
  		{
  		// Process the currently select pick.
  		// If we have picked it before, and it's a leaf (returns itself), pick the next.
  		// If it is exhausted (returns null), pick the next.
  		// Otherwise, use whatever it returns.
  		returnedMapping = processNodeMapping(mappings[pickInt], true);

			if ((returnedMapping == null) || ((lastPick == pickInt) && (returnedMapping == mappings[pickInt])))
				{
				// We've exhausted this mapping, move to the next one, if any
				pickInt++;
				}
  		else
  			{
  			// This mapping produced a usable result so we will return it in a MatchResult
  			matchResultTable.put(matchResult, new Integer(pickInt));

  			pickedMapping[0] = returnedMapping;

  			return new MatchResult(pickedMapping);
  			}
  		}

  	// If we reach here,
  	// no more mappings are available, reset the entry for this match result
  	// and return null
 		matchResultTable.remove(matchResult);
 		return null;
  	}

  	/**
  	 * Processes a NodeMapping
  	 *
  	 * @param mapping  the NodeMapping being processed.
  	 * @param choose  a flag indicating whether this method should advance the search
  	 * or simply return old results.
  	 * @return the NodeMapping that is the result of processing.
  	 */
  private NodeMapping processNodeMapping(NodeMapping mapping, boolean choose)
  	{
  	MatchResult results[], newResults[];
  	Integer choiceOb;
  	int choiceInt;
  	boolean noSubContexts;
  	int res;

  	// Check nodeMappingTable to see if there is an entry for this NodeMapping
  	choiceOb = (Integer)nodeMappingTable.get(mapping);

		// Get MatchResults from mapping.
	  results = mapping.getMatchResults();

  	// If there is no entry in the table, check for subContexts
  	if (choiceOb == null)
  		{
	  	// Check for subContexts in this mapping
			res = 0;
			noSubContexts = true;

			while (res < results.length && noSubContexts)
				{
				if (results[res] != null)
					noSubContexts = false;
				res++;
				}

	  	// If no subcontexts, then this mapping is a leaf,
	  	// add -1 to table so we don't check it again and return the mapping itself.
			if (noSubContexts)
				{
				nodeMappingTable.put(mapping, new Integer(-1));
				return mapping;
				}

  		// There are subcontexts, so initialize choiceInt to 0
  		choiceInt = 0;
  		}
  	else
  		choiceInt = choiceOb.intValue();

  	// If choiceInt is -1, that means we have already determined that this mapping is a leaf
  	// so we can return the mapping itself right away.
  	if (choiceInt == -1)
  		return mapping;

		// Create new results array
		newResults = new MatchResult[results.length];

  	// If we are not allowed to choose, Fill newResults array with results of earlier
  	// choices
  	// Do this by calling processMatchResult with false for the choose flag.
  	if (!choose)
  		{
	  	for (int old = 0; old < results.length; old++)
  			{
  			if (results[old] != null)
   				newResults[old] = processMatchResult(results[old], false);
	  		}

		  return new NodeMapping(
		  	mapping.getFirstGraph(), mapping.getSecondGraph(),
	  		mapping.getFirstConcepts(), mapping.getSecondConcepts(),
	  		mapping.getFirstRelations(), mapping.getSecondRelations(),
		  	newResults);
	  	}



  	// Fill beginning of newResults array with results of earlier choices
  	// Do this by calling processMatchResult with false for the choose flag
  	// up to the choice point.
  	for (int old = 0; old < choiceInt; old++)
  		{
  		if (results[old] != null)
   			newResults[old] = processMatchResult(results[old], false);
  		}

		while (choiceInt < results.length)
			{
  		// If the result has a corresponding MatchResult, then process it.
  		if (results[choiceInt] != null)
  			{
	  		// Process its MatchResult, pass a true to the choose flag so that it's free to
	  		// choose
		  	// If true is returned, process next MatchResult (if any)
   			// If false is returned, backtrack and process previous subcontext

   			newResults[choiceInt] = processMatchResult(results[choiceInt], true);
   			if (newResults[choiceInt] == null)
					{
					// If it returns null, backtrack until we find another valid choicepoint
					choiceInt--;
					while ((choiceInt >= 0) && (results[choiceInt] == null))
						choiceInt--;

					// If we have backtracked completely, then we are exhausted and must return null
  				// after clearing this NodeMapping's entry from the table.
					if (choiceInt == -1)
						{
  					nodeMappingTable.remove(mapping);
						return null;
  					}
					}
				else
					{
					// Processing didn't return null, so increment the choice point
					choiceInt++;
					}
 				}
 			else
 				{
 				// No match result at this index, so advance
 				choiceInt++;
 				}
	  	}

	  // If we reach here, we have successfully processed all MatchResults so we store
	  // the choice point as being the last result index and return true
	  // Strictly speaking, we are not storing a choice point, but a leaf/unexplored/explored
	  // flag but we'll leave it this way for now.

		// The choicepoint to store is the last real choice, so 
		// backtrack until we find a valid choicepoint
		choiceInt--;
		while ((choiceInt >= 0) && (results[choiceInt] == null))
			choiceInt--;

	  nodeMappingTable.put(mapping, new Integer(choiceInt));

	  return new NodeMapping(
	  	mapping.getFirstGraph(), mapping.getSecondGraph(),
	  	mapping.getFirstConcepts(), mapping.getSecondConcepts(),
	  	mapping.getFirstRelations(), mapping.getSecondRelations(),
	  	newResults);
  	}

  	/**
  	 * Gets the next simple match from this generator.
  	 *
  	 * @return the next simple match from this generator or null if there are no more.
  	 */
  public MatchResult getNextSimpleMatch()
  	{
  	return processMatchResult(rootResult, true);
  	}

  	/**
  	 * Gets all remaining simple matches from this generator.
  	 *
  	 * @return an array of all simple matches remaining in this generator, possibly empty.
  	 */
  public MatchResult[] getRemainingMatches()
  	{
  	Vector matchVec = new Vector();
  	MatchResult result, resultArr[];

  	result = getNextSimpleMatch();
  	while (result != null)
  		{
  		matchVec.addElement(result);
	  	result = getNextSimpleMatch();
  		}

  	resultArr = new MatchResult[matchVec.size()];
  	matchVec.copyInto(resultArr);

  	return resultArr;
  	}
  }
