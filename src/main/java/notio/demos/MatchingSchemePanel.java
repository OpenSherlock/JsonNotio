package notio.demos;

/*
	A basic extension of the java.awt.Panel class
 */

import java.awt.*;
import java.util.*;
import notio.*;

public class MatchingSchemePanel extends Panel
{
Hashtable graphTable, conceptTable, relationTable, 
	conceptTypeTable, relationTypeTable,
  quantifierTable, designatorTable, markerTable,
  arcTable, corefAutoTable,
  corefAgreeTable, foldTable, connectedTable;
  
	public MatchingSchemePanel()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(288,341);
		add(graphMatchChoice);
		graphMatchChoice.setBounds(156,0,120,24);
		label2.setText("Concept Match");
		add(label2);
		label2.setBounds(12,24,84,24);
		label3.setText("Relation Match");
		add(label3);
		label3.setBounds(12,48,84,24);
		label4.setText("Concept Type Match");
		add(label4);
		label4.setBounds(12,72,120,24);
		add(conceptMatchChoice);
		conceptMatchChoice.setBounds(156,24,120,24);
		add(relationMatchChoice);
		relationMatchChoice.setBounds(156,48,120,24);
		add(conceptTypeMatchChoice);
		conceptTypeMatchChoice.setBounds(156,72,120,24);
		label5.setText("Relation Type Match");
		add(label5);
		label5.setBounds(12,96,120,24);
		add(relationTypeMatchChoice);
		relationTypeMatchChoice.setBounds(156,96,120,24);
		label6.setText("Quantifier Match");
		add(label6);
		label6.setBounds(12,120,120,24);
		add(quantifierMatchChoice);
		quantifierMatchChoice.setBounds(156,120,120,24);
		label1.setText("Graph Match");
		add(label1);
		label1.setBounds(12,0,84,24);
		label7.setText("Designator Match");
		add(label7);
		label7.setBounds(12,144,120,24);
		label8.setText("Arc Match");
		add(label8);
		label8.setBounds(12,192,84,24);
		add(designatorMatchChoice);
		designatorMatchChoice.setBounds(156,144,120,24);
		add(arcMatchChoice);
		arcMatchChoice.setBounds(156,192,120,24);
		label9.setText("Fold Match");
		add(label9);
		label9.setBounds(12,264,84,24);
		add(foldMatchChoice);
		foldMatchChoice.setBounds(156,264,120,24);
		label10.setText("Connected Match");
		add(label10);
		label10.setBounds(12,288,96,24);
		add(connectedMatchChoice);
		connectedMatchChoice.setBounds(156,288,120,24);
		label11.setText("Coreference Automatch");
		add(label11);
		label11.setBounds(12,216,132,24);
		add(corefAutoMatchChoice);
		corefAutoMatchChoice.setBounds(156,216,120,24);
		label12.setText("Coreference Agreement");
		add(label12);
		label12.setBounds(12,240,132,24);
		add(corefAgreementChoice);
		corefAgreementChoice.setBounds(156,240,120,24);
		label14.setText("Maximum # of Matches");
		add(label14);
		label14.setBounds(12,312,132,24);
		maxMatchTextField.setText("0");
		add(maxMatchTextField);
		maxMatchTextField.setBounds(156,312,120,24);
		label13.setText("Marker Match");
		add(label13);
		label13.setBounds(12,168,84,24);
		add(markerMatchChoice);
		markerMatchChoice.setBounds(156,168,120,24);
		//}}

		//{{REGISTER_LISTENERS
		//}}

		graphTable = new Hashtable();
		graphTable.put("Complete", new Integer(MatchingScheme.GR_MATCH_COMPLETE));
		graphTable.put("Subgraph", new Integer(MatchingScheme.GR_MATCH_SUBGRAPH));
		graphTable.put("Proper Subgraph", new Integer(MatchingScheme.GR_MATCH_PROPER_SUBGRAPH));
		graphTable.put("Either Subgraph", new Integer(MatchingScheme.GR_MATCH_EITHER_SUBGRAPH));
		graphTable.put("Either Proper Subgraph", new Integer(MatchingScheme.GR_MATCH_EITHER_PROPER_SUBGRAPH));
		graphTable.put("Instance", new Integer(MatchingScheme.GR_MATCH_INSTANCE));
		graphTable.put("Anything", new Integer(MatchingScheme.GR_MATCH_ANYTHING));
		
		graphMatchChoice.addItem("Complete");
		graphMatchChoice.addItem("Subgraph");
		graphMatchChoice.addItem("Proper Subgraph");
		graphMatchChoice.addItem("Either Subgraph");
		graphMatchChoice.addItem("Either Proper Subgraph");
		graphMatchChoice.addItem("Instance");
		graphMatchChoice.addItem("Anything");
		
		conceptTable = new Hashtable();
		conceptTable.put("Types and Referents", new Integer(MatchingScheme.CN_MATCH_ALL));
		conceptTable.put("Types", new Integer(MatchingScheme.CN_MATCH_TYPES));
		conceptTable.put("Referents", new Integer(MatchingScheme.CN_MATCH_REFERENTS));
		conceptTable.put("Coreferents", new Integer(MatchingScheme.CN_MATCH_COREFERENTS));
		conceptTable.put("Instance", new Integer(MatchingScheme.CN_MATCH_INSTANCE));
		conceptTable.put("Anything", new Integer(MatchingScheme.CN_MATCH_ANYTHING));

		conceptMatchChoice.addItem("Types and Referents");
		conceptMatchChoice.addItem("Types");
		conceptMatchChoice.addItem("Referents");
		conceptMatchChoice.addItem("Coreferents");
		conceptMatchChoice.addItem("Instance");
		conceptMatchChoice.addItem("Anything");

		relationTable = new Hashtable();
		relationTable.put("Types and Arcs", new Integer(MatchingScheme.RN_MATCH_ALL));
		relationTable.put("Types", new Integer(MatchingScheme.RN_MATCH_TYPES));
		relationTable.put("Arcs", new Integer(MatchingScheme.RN_MATCH_ARCS));
		relationTable.put("Instance", new Integer(MatchingScheme.RN_MATCH_INSTANCE));
		relationTable.put("Anything", new Integer(MatchingScheme.RN_MATCH_ANYTHING));

		relationMatchChoice.addItem("Types and Arcs");
		relationMatchChoice.addItem("Types");
		relationMatchChoice.addItem("Arcs");
		relationMatchChoice.addItem("Instance");
		relationMatchChoice.addItem("Anything");

		conceptTypeTable = new Hashtable();
		conceptTypeTable.put("Label", new Integer(MatchingScheme.CT_MATCH_LABEL));
		conceptTypeTable.put("Subtype", new Integer(MatchingScheme.CT_MATCH_SUBTYPE));
		conceptTypeTable.put("Supertype", new Integer(MatchingScheme.CT_MATCH_SUPERTYPE));
		conceptTypeTable.put("Equivalent", new Integer(MatchingScheme.CT_MATCH_EQUIVALENT));
		conceptTypeTable.put("Instance", new Integer(MatchingScheme.CT_MATCH_INSTANCE));
		conceptTypeTable.put("Anything", new Integer(MatchingScheme.CT_MATCH_ANYTHING));

		conceptTypeMatchChoice.addItem("Label");
		conceptTypeMatchChoice.addItem("Subtype");
		conceptTypeMatchChoice.addItem("Supertype");
		conceptTypeMatchChoice.addItem("Equivalent");
		conceptTypeMatchChoice.addItem("Instance");
		conceptTypeMatchChoice.addItem("Anything");

		relationTypeTable = new Hashtable();
		relationTypeTable.put("Label", new Integer(MatchingScheme.RT_MATCH_LABEL));
		relationTypeTable.put("Subtype", new Integer(MatchingScheme.RT_MATCH_SUBTYPE));
		relationTypeTable.put("Supertype", new Integer(MatchingScheme.RT_MATCH_SUPERTYPE));
		relationTypeTable.put("Equivalent", new Integer(MatchingScheme.RT_MATCH_EQUIVALENT));
		relationTypeTable.put("Instance", new Integer(MatchingScheme.RT_MATCH_INSTANCE));
		relationTypeTable.put("Anything", new Integer(MatchingScheme.RT_MATCH_ANYTHING));

		relationTypeMatchChoice.addItem("Label");
		relationTypeMatchChoice.addItem("Subtype");
		relationTypeMatchChoice.addItem("Supertype");
		relationTypeMatchChoice.addItem("Equivalent");
		relationTypeMatchChoice.addItem("Instance");
		relationTypeMatchChoice.addItem("Anything");

		quantifierTable = new Hashtable();
		quantifierTable.put("Anything", new Integer(MatchingScheme.QF_MATCH_ANYTHING));
		
		quantifierMatchChoice.addItem("Anything");
		
		designatorTable = new Hashtable();
		designatorTable.put("Equivalent", new Integer(MatchingScheme.DG_MATCH_EQUIVALENT));
		designatorTable.put("Individual", new Integer(MatchingScheme.DG_MATCH_INDIVIDUAL));
		designatorTable.put("Restriction", new Integer(MatchingScheme.DG_MATCH_RESTRICTION));
		designatorTable.put("Instance", new Integer(MatchingScheme.DG_MATCH_INSTANCE));
		designatorTable.put("Anything", new Integer(MatchingScheme.DG_MATCH_ANYTHING));

		designatorMatchChoice.addItem("Equivalent");
		designatorMatchChoice.addItem("Individual");
		designatorMatchChoice.addItem("Restriction");
		designatorMatchChoice.addItem("Instance");
		designatorMatchChoice.addItem("Anything");

		markerTable = new Hashtable();
		markerTable.put("ID", new Integer(MatchingScheme.MARKER_MATCH_ID));
		markerTable.put("Comparator", new Integer(MatchingScheme.MARKER_MATCH_COMPARATOR));
		markerTable.put("Anything", new Integer(MatchingScheme.MARKER_MATCH_ANYTHING));

		markerMatchChoice.addItem("ID");
// Currently disable since we have no way to specify a comparator (run-time class-loading?)
//		markerMatchChoice.addItem("Comparator");
		markerMatchChoice.addItem("Anything");

		arcTable = new Hashtable();
		arcTable.put("Concept", new Integer(MatchingScheme.ARC_MATCH_CONCEPT));
		arcTable.put("Valence", new Integer(MatchingScheme.ARC_MATCH_VALENCE));
		arcTable.put("Instance", new Integer(MatchingScheme.ARC_MATCH_INSTANCE));
		arcTable.put("Anything", new Integer(MatchingScheme.ARC_MATCH_ANYTHING));

		arcMatchChoice.addItem("Concept");
		arcMatchChoice.addItem("Valence");
		arcMatchChoice.addItem("Instance");
		arcMatchChoice.addItem("Anything");

		corefAutoTable = new Hashtable();
		corefAutoTable.put("Off", new Integer(MatchingScheme.COREF_AUTOMATCH_OFF));
		corefAutoTable.put("On", new Integer(MatchingScheme.COREF_AUTOMATCH_ON));

		corefAutoMatchChoice.addItem("Off");
		corefAutoMatchChoice.addItem("On");

		corefAgreeTable = new Hashtable();
		corefAgreeTable.put("Off", new Integer(MatchingScheme.COREF_AGREE_OFF));
		corefAgreeTable.put("On", new Integer(MatchingScheme.COREF_AGREE_ON));

		corefAgreementChoice.addItem("Off");
		corefAgreementChoice.addItem("On");

		foldTable = new Hashtable();
		foldTable.put("Off", new Integer(MatchingScheme.FOLD_MATCH_OFF));
		foldTable.put("On", new Integer(MatchingScheme.FOLD_MATCH_ON));

		foldMatchChoice.addItem("Off");
		foldMatchChoice.addItem("On");

		connectedTable = new Hashtable();
		connectedTable.put("On", new Integer(MatchingScheme.CONN_MATCH_ON));

		connectedMatchChoice.addItem("On");
 	}
	
	public void addNotify()
	{
  	    // Record the size of the window prior to calling parents addNotify.
	    Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(getInsets().left, getInsets().top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

    // Used for addNotify check.
	boolean fComponentsAdjusted = false;


	public synchronized void setVisible(boolean visible)
	{
		if (visible)
			{
			Rectangle bounds = getParent().getBounds();
			Rectangle abounds = getBounds();

			setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
				 bounds.y + (bounds.height - abounds.height)/2);
			}

		super.setVisible(visible);
	}

	//{{DECLARE_CONTROLS
	java.awt.Choice graphMatchChoice = new java.awt.Choice();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Label label4 = new java.awt.Label();
	java.awt.Choice conceptMatchChoice = new java.awt.Choice();
	java.awt.Choice relationMatchChoice = new java.awt.Choice();
	java.awt.Choice conceptTypeMatchChoice = new java.awt.Choice();
	java.awt.Label label5 = new java.awt.Label();
	java.awt.Choice relationTypeMatchChoice = new java.awt.Choice();
	java.awt.Label label6 = new java.awt.Label();
	java.awt.Choice quantifierMatchChoice = new java.awt.Choice();
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Label label7 = new java.awt.Label();
	java.awt.Label label8 = new java.awt.Label();
	java.awt.Choice designatorMatchChoice = new java.awt.Choice();
	java.awt.Choice arcMatchChoice = new java.awt.Choice();
	java.awt.Label label9 = new java.awt.Label();
	java.awt.Choice foldMatchChoice = new java.awt.Choice();
	java.awt.Label label10 = new java.awt.Label();
	java.awt.Choice connectedMatchChoice = new java.awt.Choice();
	java.awt.Label label11 = new java.awt.Label();
	java.awt.Choice corefAutoMatchChoice = new java.awt.Choice();
	java.awt.Label label12 = new java.awt.Label();
	java.awt.Choice corefAgreementChoice = new java.awt.Choice();
	java.awt.Label label14 = new java.awt.Label();
	java.awt.TextField maxMatchTextField = new java.awt.TextField();
	java.awt.Label label13 = new java.awt.Label();
	java.awt.Choice markerMatchChoice = new java.awt.Choice();
	//}}

public MatchingScheme getMatchingScheme()
  { 
  MatchingScheme scheme;
  int maxMatches;
  
  try
  	{
  	maxMatches = Integer.parseInt(maxMatchTextField.getText());
  	}
  catch (NumberFormatException e)
  	{
  	return null;
  	}
  
   scheme = new MatchingScheme(
     ((Integer)graphTable.get(graphMatchChoice.getSelectedItem())).intValue(),
     ((Integer)conceptTable.get(conceptMatchChoice.getSelectedItem())).intValue(),
     ((Integer)relationTable.get(relationMatchChoice.getSelectedItem())).intValue(),
     ((Integer)conceptTypeTable.get(conceptTypeMatchChoice.getSelectedItem())).intValue(),
     ((Integer)relationTypeTable.get(relationTypeMatchChoice.getSelectedItem())).intValue(),
     ((Integer)quantifierTable.get(quantifierMatchChoice.getSelectedItem())).intValue(),
     ((Integer)designatorTable.get(designatorMatchChoice.getSelectedItem())).intValue(),
     ((Integer)markerTable.get(markerMatchChoice.getSelectedItem())).intValue(),
     ((Integer)arcTable.get(arcMatchChoice.getSelectedItem())).intValue(),
     ((Integer)corefAutoTable.get(corefAutoMatchChoice.getSelectedItem())).intValue(),
     ((Integer)corefAgreeTable.get(corefAgreementChoice.getSelectedItem())).intValue(),
     ((Integer)foldTable.get(foldMatchChoice.getSelectedItem())).intValue(),
     ((Integer)connectedTable.get(connectedMatchChoice.getSelectedItem())).intValue(),
     maxMatches,
     null,
     null);
     
  return scheme;
	}
}
