package notio.demos;

import java.io.*;
import java.awt.*;
import notio.*;
import notio.translators.*;

    /**
     * An applet for testing and demonstrating the Parser and Generator systems.
     * Applet parameters "input" and "output" can be used to set the default parser and 
     * generator formats to either "CGIF" or "LF".
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.7 $, $Date: 1999/07/10 03:20:03 $
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
public class TranslatorApplet extends java.applet.Applet implements java.awt.event.ActionListener
  {
  /** Misc interface vars **/
  private TextArea inputTextArea, outputTextArea;
  
  /** Misc interface vars **/
  private Button convertButton, clearInputButton, clearOutputButton;
  
  /** Misc interface vars **/
  private Panel choicePanel, buttonPanel, textPanel;
  
  /** Misc interface vars **/
	private Choice parserChoice, generatorChoice;
	
    /**
     * Applet init.
     */
  public void init()
    {
    String selectedInput = null;
    String selectedOutput = null;
    
    selectedInput = getParameter("input");
    selectedOutput = getParameter("output");
    
    if (selectedInput == null)
    	selectedInput = "CGIF";
    
    if (selectedOutput == null)
    	selectedOutput = "CGIF";
    
    setLayout(new BorderLayout());

		choicePanel = new Panel();
    buttonPanel = new Panel();
    textPanel = new Panel();

    buttonPanel.setLayout(new GridLayout(1, 3));
    textPanel.setLayout(new GridLayout(2, 1));

    inputTextArea = new TextArea();
    outputTextArea = new TextArea();
    outputTextArea.setEditable(false);
    textPanel.add(inputTextArea);
    textPanel.add(outputTextArea);

    convertButton = new Button("Convert");
    convertButton.addActionListener(this);
    clearInputButton = new Button("Clear Input");
    clearInputButton.addActionListener(this);
    clearOutputButton = new Button("Clear Output");
    clearOutputButton.addActionListener(this);
    parserChoice = new Choice();
    parserChoice.add("LF");
    parserChoice.add("CGIF");
    parserChoice.select(selectedInput);

    generatorChoice = new Choice();
    generatorChoice.add("CGIF");
    generatorChoice.add("LF");
    generatorChoice.select(selectedOutput);

    textPanel.add(inputTextArea);
    textPanel.add(outputTextArea);

    buttonPanel.add(convertButton);
    buttonPanel.add(clearInputButton);
    buttonPanel.add(clearOutputButton);

    choicePanel.add(new Label("Input:"));
    choicePanel.add(parserChoice);
    choicePanel.add(new Label("Output:"));
    choicePanel.add(generatorChoice);

    add("Center", textPanel);
    add("South", choicePanel);
    add("North", buttonPanel);
}

    /**
     * Event handling.
     *
     * @param e  the event being handled.
     */
  public void actionPerformed(java.awt.event.ActionEvent e)
    {
    if (e.getSource() == convertButton)
      {
      parseGenerate();
      return;
      }

    if (e.getSource() == clearInputButton)
      {
      inputTextArea.setText("");
      return;
      }

    if (e.getSource() == clearOutputButton)
      {
      outputTextArea.setText("");
      return;
      }
    }

    /**
     * Parse and then generate.
     */
  public void parseGenerate()
    {
    Parser parser;
    Generator generator;
    KnowledgeBase kBase;
    TranslationContext tContext;
    Graph graph;
    StringWriter writer;

    kBase = new KnowledgeBase();
    tContext = new TranslationContext();
    writer = new StringWriter();

		if (parserChoice.getSelectedItem().equals("CGIF"))
	    parser = new CGIFParser();
	  else
	  	parser = new LFParser();
    
    
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

		if (generatorChoice.getSelectedItem().equals("CGIF"))
	    generator = new CGIFGenerator();
	  else
	    generator = new LFGenerator();

    try
	    {
    	generator.initializeGenerator(writer, kBase, tContext);
	    }
    catch (GeneratorException e)
	  	{
	  	e.printStackTrace();
	  	System.exit(1);
	  	}

    outputTextArea.append(inputTextArea.getText() + "\n");
    try
      {
      graph = parser.parseGraph();
      outputTextArea.append(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

      tContext.resetUnits();

      generator.generateGraph(graph);
      outputTextArea.append(writer.toString());
      }
    catch (GeneratorException e)
      {
      outputTextArea.append("Exception thrown while generating: " + e.getMessage() + "\n");
      if (e.getSubThrowable() != null)
        outputTextArea.append("sub-throwable: " + e.getSubThrowable().getMessage() + "\n");
      }
    catch (ParserException e)
      {
      outputTextArea.append("Exception thrown while parsing: " + e.getMessage() + "\n");
      
      if (e.getOccurranceMessage() != null)
        outputTextArea.append(e.getOccurranceMessage() + "\n");
      	
      if (e.getSubThrowable() != null)
        outputTextArea.append("sub-throwable: " + e.getSubThrowable().getMessage() + "\n");
      }

    outputTextArea.append("\n --------------------------------------\n\n");
    }
}
