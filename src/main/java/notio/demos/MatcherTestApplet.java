package notio.demos;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import notio.*;
import notio.translators.*;

    /** 
     * Tests the graph matching facilities.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.13 $, $Date: 1999/07/09 02:43:49 $
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
public class MatcherTestApplet extends java.applet.Applet implements ActionListener
  {
  /** Misc interface vars **/
  private TextArea inputTextArea1, inputTextArea2, outputTextArea;
  /** Misc interface vars **/
  private Button matchButton, clearInputButton1, clearInputButton2, clearOutputButton;
  /** Misc interface vars **/
  private Panel buttonPanel, textPanel;
  /** Matching scheme dialog **/
	private MatchingSchemePanel schemePanel;

    /**
     * Applet init.
     */
  public void init()
    {
    setLayout(new BorderLayout());

    buttonPanel = new Panel();
    textPanel = new Panel();

    buttonPanel.setLayout(new GridLayout(1, 4));
    textPanel.setLayout(new GridLayout(3, 1));

    inputTextArea1 = new TextArea();
    inputTextArea2 = new TextArea();
    outputTextArea = new TextArea();
    outputTextArea.setEditable(false);
    textPanel.add(inputTextArea1);
    textPanel.add(inputTextArea2);
    textPanel.add(outputTextArea);

    matchButton = new Button("Match");
    clearInputButton1 = new Button("Clear Input 1");
    clearInputButton2 = new Button("Clear Input 2");
    clearOutputButton = new Button("Clear Output");

		matchButton.addActionListener(this);
		clearInputButton1.addActionListener(this);
		clearInputButton2.addActionListener(this);
		clearOutputButton.addActionListener(this);

    buttonPanel.add(matchButton);
    buttonPanel.add(clearInputButton1);
    buttonPanel.add(clearInputButton2);
    buttonPanel.add(clearOutputButton);

    schemePanel = new MatchingSchemePanel();

    add("South", buttonPanel);
    add("East", schemePanel);
    add("Center", textPanel);
}

    /**
     * Event handling for buttons.
     *
     * @param e  the event being handled.
     */
  public void actionPerformed(ActionEvent e)
    {
    if (e.getSource() == matchButton)
      {
      tryMatching();
      return;
      }
    
    if (e.getSource() == clearInputButton1)
    	{
      inputTextArea1.setText("");
      return;
      }

    if (e.getSource() == clearInputButton2)
      {
      inputTextArea2.setText("");
      return;
      }

    if (e.getSource() == clearOutputButton)
      {
      outputTextArea.setText("");
      return;
      }
    }

    /**
     * Parse a graph.
     */
  public Graph parse(TextArea inputTextArea)
    {
    CGIFParser parser;
    KnowledgeBase kBase;
    TranslationContext tContext;
    Graph graph = null;

    kBase = new KnowledgeBase();
    tContext = new TranslationContext();
    parser = new CGIFParser();
    
  	try
  		{
	    parser.initializeParser(new StringReader(inputTextArea.getText()),
	        kBase, tContext);
	    }
	  catch (ParserException e)
	  	{
	  	e.printStackTrace();
	  	System.exit(1);
	  	}
	  	
    try
      {
      graph = parser.parseGraph();
      }
    catch (ParserException e)
      {
      outputTextArea.append("Exception thrown while parsing: " + e.getMessage());
      if (e.getSubThrowable() != null)
        outputTextArea.append("sub-throwable: " + e.getSubThrowable().getMessage());
	    outputTextArea.append("\n --------------------------------------\n\n");
      }
      
    return graph;
    }

  public void generate(Graph graph)
    {
    CGIFGenerator generator;
    KnowledgeBase kBase;
    TranslationContext tContext;
    StringWriter writer;

    kBase = new KnowledgeBase();
    tContext = new TranslationContext();
    writer = new StringWriter();

    generator = new CGIFGenerator();

		try
			{
    	generator.initializeGenerator(writer, kBase, tContext);
	    }
	  catch (GeneratorException e)
	  	{
	  	e.printStackTrace();
	  	System.exit(1);
	  	}

    try
      {
      generator.generateGraph(graph);
      outputTextArea.append(writer.toString());
      }
    catch (GeneratorException e)
      {
      outputTextArea.append("Exception thrown while generating: " + e.getMessage());
      if (e.getSubThrowable() != null)
        outputTextArea.append("sub-throwable: " + e.getSubThrowable().getMessage());
      }
    }

	public void tryMatching()
		{
		Graph graphA, graphB;
		MatchResult result;

		graphA = parse(inputTextArea1);
		if (graphA == null)
			return;
			
		graphB = parse(inputTextArea2);
		if (graphB == null)
			return;

		result = Graph.matchGraphs(graphA, graphB, schemePanel.getMatchingScheme());
		
		if (result.matchSucceeded())
			outputTextArea.append("Matched: " + result.getNumberOfMatches() + "\n");
		else
			outputTextArea.append("Not Matched.\n");
		}
	}
