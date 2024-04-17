package notio.translators;

import java.util.*;
import java.io.Serializable;
import notio.*;

    /** 
     * A TranslationInfoUnit that serves as a symbol table relating defining labels to
     * defining concepts and coreference sets.  This table allows stack-like operations
     * for maintaining label scope.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8 $, $Date: 1999/07/09 22:21:19 $
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
		 * @idea Consider extending "pop" operations to the label generator.
     */
public class DefiningLabelTable extends SimpleInfoUnit
  {
  	/** The default initial size for the stack used to track changes to the table. **/
  private static final int DEFAULT_STACK_SIZE = 100;

	  /** A lookup table that maps coreference labels to coreference sets. **/
  private Hashtable defLabelToCorefSetTable;

	  /** A lookup table that maps coreference sets to coreference labels. **/
  private Hashtable corefSetToDefLabelTable;
  
	  /** A lookup table that maps coreference labels to defining concepts. **/
  private Hashtable defLabelToDefConceptTable;

	  /** A lookup table that maps defining concepts to coreference labels. **/
  private Hashtable defConceptToDefLabelTable;
  
	  /** A factory for producing defining labels. **/
  private LabelFactory defLabelFactory;
  
  	/** The stack of labels used to track changes to the table. **/
  private String labelStack[];

  	/** The stack pointer that indicates the current top of the stack. **/
  private int stackPointer;

    /**
     * Constructs a new DefiningLabelTable.
     */
  public DefiningLabelTable()
    {
    defLabelToCorefSetTable = new Hashtable();
    corefSetToDefLabelTable = new Hashtable();
    defLabelToDefConceptTable = new Hashtable();
    defConceptToDefLabelTable = new Hashtable();
    defLabelFactory = new LabelFactory();
    labelStack = new String[DEFAULT_STACK_SIZE];
    stackPointer = -1;
    }

    /**
     * Constructs a new DefiningLabelTable that is a copy of the specified original.
     * Changes to the new table will not affect the old one or vice versa.
     *
     * @param originalContext  the translation context to be copied.
     */
  public DefiningLabelTable(DefiningLabelTable originalTable)
    {
    String lastLabel;
    String orgStack[];
    
    defLabelToCorefSetTable = copyHashtable(originalTable.getDefLabelToCorefSetTable());
    corefSetToDefLabelTable = copyHashtable(originalTable.getCorefSetToDefLabelTable());
    defLabelToDefConceptTable = copyHashtable(originalTable.getDefLabelToDefConceptTable());
    defConceptToDefLabelTable = copyHashtable(originalTable.getDefConceptToDefLabelTable());
    lastLabel = originalTable.getLastDefiningLabel();
    
    if (lastLabel == null)
    	defLabelFactory = new LabelFactory();
    else
    	defLabelFactory = new LabelFactory(lastLabel);

    labelStack = new String[DEFAULT_STACK_SIZE];
    
    stackPointer = originalTable.getStackPointer();
    orgStack = originalTable.getStack();
    labelStack = new String[orgStack.length];
    System.arraycopy(orgStack, 0, labelStack, 0, orgStack.length);
		}

		/**
		 * Pushes a new context onto the stack.
		 * Any entries added to this table after the push will be removed when a corresponding
		 * pop is called.
		 */
	public final void pushContext()
		{
		// Add a null to the stack to indicate another context
		addToStack(null);
		}

		/**
		 * Pops a context off the stack.
		 * Any entries added to this table since the corresponding the push will be removed.
		 */
	public final void popContext()
		{
		// Remove entries on stack until we encounter a null indicating the context boundary
		while (labelStack[stackPointer] != null)
			{
			removeEntry(labelStack[stackPointer]);
			stackPointer--;
			}
			
		// Remove a null from the stack remove the context
		stackPointer--;
		}

		/**
		 * Adds the entry to the stack, doubling the stack size whenever it runs out of space.
		 * 
		 * @param label  the entry to be added to the stack.
		 */
	private final void addToStack(String label)
		{
		// Increase stack size if necessary (double it)
		if (stackPointer == labelStack.length - 1)
			{
			String newStack[];
			
			newStack = new String[labelStack.length * 2];
			System.arraycopy(labelStack, 0, newStack, 0, labelStack.length);
			labelStack = newStack;
			}
		
		stackPointer++;
		labelStack[stackPointer] = label;
		}
			
		/**
		 * Removes all entries corresponding to the specified label.
		 *
		 * @param label  the label to be removed.
		 */
	private final void removeEntry(String label)
		{
		CoreferenceSet deadSet;
		Concept deadConcept;

    deadSet = (CoreferenceSet)defLabelToCorefSetTable.get(label);
   	defLabelToCorefSetTable.remove(label);
   	if (deadSet != null)
   		corefSetToDefLabelTable.remove(deadSet);
   	
    deadConcept = (Concept)defLabelToDefConceptTable.get(label);
    defLabelToDefConceptTable.remove(label);
    if (deadConcept != null)
	    defConceptToDefLabelTable.remove(deadConcept);
		}

    /**
     * Creates a two-way mapping between a defining label and a coreference set instance.
     * This table may be used by
     * parsers to associate coreference sets with defining labels.  This is
     * particularly necessary for parsing or generating nested graphs with 
     * lines of identity.  It is difficult for a parser or generator to
     * intelligently decide when this mapping is no longer valid (e.g. when
     * parsing a new graph that uses the same defining labels) so it may be
     * cleared by the user or controlling application using the
     * clearDefiningLabelToCoreferenceSetMapping() method.
     *
     * @param definingLabel  the defining label being mapped.
     * @param corefSet  the coreference set being mapped.
     */
  public void mapDefiningLabelToCoreferenceSet(String definingLabel, CoreferenceSet corefSet)
    {
    // Add the label to the stack if it isn't already in this table.
    if (defLabelToDefConceptTable.get(definingLabel) == null)
    	addToStack(definingLabel);
    	
    defLabelToCorefSetTable.put(definingLabel, corefSet);
    corefSetToDefLabelTable.put(corefSet, definingLabel);
    }
    
    /**
     * Returns the coreference set associated with the specified defining label in this 
     * translation context.
     *
     * @param definingLabel  the defining label used for lookup.
     * @return the coreference set associated with definingLabel, or null if no mapping exists.
     */
  public CoreferenceSet getCoreferenceSetByDefiningLabel(String definingLabel)
    {
    return (CoreferenceSet)defLabelToCorefSetTable.get(definingLabel);
    }

    /**
     * Returns the defining label associated with the specified coreference set in this 
     * translation context.
     *
     * @param corefSet  the coreference set used for lookup.
     * @return the defining label associated with the set, or null if no mapping exists.
     */
  public String getDefiningLabelByCoreferenceSet(CoreferenceSet corefSet)
    {
    return (String)corefSetToDefLabelTable.get(corefSet);
    }
    
    /**
     * Clears all existing mappings between coreference labels and coreference sets.
     */
  public void clearDefiningLabelToCoreferenceSetMapping()
    {
    defLabelToCorefSetTable.clear();
    corefSetToDefLabelTable.clear();
    }

    /**
     * Creates a two-way mapping between a defining label and a defining concept.
     * This table may be used by
     * parsers to associate defining labels with defining concepts.  This is
     * particularly necessary for parsing or generating nested graphs with 
     * lines of identity.  It is difficult for a parser or generator to
     * intelligently decide when this mapping is no longer valid (e.g. when
     * parsing a new graph that uses the same defining labels) so it may be
     * cleared by the user or controlling application using the
     * clearDefiningLabelToDefiningConceptMapping() method.
     *
     * @param definingLabel  the defining label being mapped.
     * @param defConcept  the defining concept being mapped.
     */
  public void mapDefiningLabelToDefiningConcept(String definingLabel, Concept defConcept)
    {
    // Add the label to the stack if it isn't already in this table.
    if (defLabelToCorefSetTable.get(definingLabel) == null)
	    addToStack(definingLabel);
    defLabelToDefConceptTable.put(definingLabel, defConcept);
    defConceptToDefLabelTable.put(defConcept, definingLabel);
    }
    
    /**
     * Returns the defining concept associated with the specified defining label in this 
     * translation context.
     *
     * @param definingLabel  the defining label used for lookup.
     * @return the defining concept associated with definingLabel, or null if no mapping exists.
     */
  public Concept getDefiningConceptByDefiningLabel(String definingLabel)
    {
    return (Concept)defLabelToDefConceptTable.get(definingLabel);
    }

    /**
     * Returns the defining label associated with the specified defining concept in this 
     * translation context.
     *
     * @param defConcept  the defining concept used for lookup.
     * @return the defining label associated with the set, or null if no mapping exists.
     */
  public String getDefiningLabelByDefiningConcept(Concept defConcept)
    {
    return (String)defConceptToDefLabelTable.get(defConcept);
    }
    
    /**
     * Clears all existing mappings between defining concepts and defining labels.
     */
  public void clearDefiningLabelToDefiningConceptMapping()
    {
    defLabelToDefConceptTable.clear();
    defConceptToDefLabelTable.clear();
    }
    
    /**
     * Returns a new defining label that is not already present in the defining 
     * label/coreference set mapping.  There are no constraints on the form of the
     * label other than that they conform to a valid CGIF identifier.  Otherwise,
     * labels' forms are determined by the underlying implementation.
     *
     * @return a new, unique, and unused defining label.
     */
  public String getNextAvailableDefiningLabel()
    {
    String newLabel;
    
    do
    	{
    	newLabel = defLabelFactory.getNextLabel();
    	}
    while (defLabelToCorefSetTable.get(newLabel) != null);
      
    return newLabel;
    }
    
    /**
     * Resets the generator for defining labels so that it starts again.
     * This method would typically be called after clearing the defining label - 
     * coreference set mapping so that labels may be reused in a new translation, 
     * if possible.
     * Implementations are not required to honour this although they should silently
     * do nothing rather than throwing an exception.  If possible, implementations
     * should reset their label generators so that they produce a sequence of
     * labels as if this were a newly constructed context.
     */
  public void resetAvailableDefiningLabel()
    {
    defLabelFactory = new LabelFactory();
    }
    
    /**
     * Returns the defining label to coreference set table as a hashtable.
     *
     * @return the defining label to coreference set table as a hashtable.
     */
  Hashtable getDefLabelToCorefSetTable()
  	{
  	return defLabelToCorefSetTable;
  	}

    /**
     * Returns the coreference set to defining label table as a hashtable.
     *
     * @return the coreference set to defining label table as a hashtable.
     */
  Hashtable getCorefSetToDefLabelTable()
  	{
  	return corefSetToDefLabelTable;
  	}

    /**
     * Returns the defining label to defining concept table as a hashtable.
     *
     * @return the defining label to defining concept table as a hashtable.
     */
  Hashtable getDefLabelToDefConceptTable()
  	{
  	return defLabelToDefConceptTable;
  	}

    /**
     * Returns the defining concept to defining label table as a hashtable.
     *
     * @return the defining concept to defining label table as a hashtable.
     */
  Hashtable getDefConceptToDefLabelTable()
  	{
  	return defConceptToDefLabelTable;
  	}

    /**
     * Returns the last defining label generated by this context or null if no labels
     * have generated.
     * Note that this method does not generated any new labels.
     *
     * @return the last defining label generated by this context or null.
     */
  public String getLastDefiningLabel()
  	{
  	return defLabelFactory.getLastLabel();
  	}
  	
  	/**
  	 * Returns the stack pointer.
  	 *
  	 * @return the stack pointer.
  	 */
  public int getStackPointer()
  	{
  	return stackPointer;
  	}
  	
  	/**
  	 * Returns the stack.
  	 *
  	 * @return the stack.
  	 */
  public String[] getStack()
  	{
  	return labelStack;
  	}
  	
		/**
		 * Creates a deep copy of the specified hashtable.
		 *
		 * @param inTable  the hashtable to be copied.
		 * @return a copy of the hashtable.
		 */
	private static Hashtable copyHashtable(Hashtable inTable)
		{
		Enumeration keys, elements;
		Hashtable newTable = new Hashtable();
		
		keys = inTable.keys();
		elements = inTable.elements();
		
		while (keys.hasMoreElements())
			newTable.put(keys.nextElement(), elements.nextElement());
			
		return newTable;
		}

	  /**
  	 * Resets this information unit to its initial state.  This method can be called
  	 * by translators in order to ensure that a unit contains no information relating
  	 * to earlier translation sessions.
  	 */
  public void resetUnit()
  	{
		resetAvailableDefiningLabel();
		clearDefiningLabelToCoreferenceSetMapping();
		clearDefiningLabelToDefiningConceptMapping();
		stackPointer = 0;
		labelStack[0] = null;
		}

  	/**
  	 * Returns a duplicate of this information unit that is distinct from the original.
  	 * This means that changes to the original will not affect the duplicate and vice
  	 * versa.
  	 *
  	 * @return a duplicate of this unit.
  	 */
  public TranslationInfoUnit copyUnit()
  	{
  	return new DefiningLabelTable(this);
  	}
  }
