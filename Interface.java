import javax.swing.*; // includes functions for JButton, JTextField, JTextArea, JRadioButton, JCheckbox, JMenu, JColorChooser etc.
import java.awt.*; // add Abstract Window Toolkit, allows the popup window
import java.awt.event.*; //record and respond to user interactions
import java.util.Calendar;
import javax.swing.JPanel;
import org.jsoup.nodes.Document;
import javax.swing.JFrame;
import java.io.IOException;


public class Interface implements ActionListener {
	
	JFrame frame;
	static JTextField textfield, textfield1, errorTextfield;
	JButton[] operatorButtons = new JButton[2]; //array to hold operators
	JButton enterButton, backButton;
	JPanel panel, resultsPanel, partPanel;
	JLabel[] resultsText = new JLabel[10];
	JLabel errorMsg = new JLabel("Error: Name or Year is Invalid.");
	JLabel nameMsg, yearMsg, notPartcipated;
	Font myFont = new Font("Arial", Font.PLAIN,20); // sets the font style
	Font eventFont = new Font("Arial", Font.BOLD, 12);
	Font runFont = new Font("Arial", Font.PLAIN, 9);
	ReadingVar in = new ReadingVar(0, null, 0, null, null);


	
	Interface() {
		
		frame = new JFrame("Autocross Results");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allows exit by closing window
		frame.setSize(600,600); //frame size
		frame.setLayout(null);
		
		textfield = new JTextField(); //text field for name
		textfield.setBounds(75, 25, 200, 40); // bounds of the text field added above
		textfield.setFont(myFont); // font within the text field
		textfield.setEditable(true);
		// nameMsg.setText("Enter name here: ");		

		
		textfield1 = new JTextField(); // text field for year
		textfield1.setBounds(295, 25, 200, 40); // bounds of the text field added above
		textfield1.setFont(myFont); // font within the text field
		textfield1.setEditable(true);
		//yearMsg.setText("Enter year here: ");		
		
		enterButton = new JButton("Search");
		backButton = new JButton("Back");
		
		errorMsg.setBounds(160, 150, 250, 30);
		backButton.setBounds(8, 8, 70, 25);

		
		operatorButtons[0] = enterButton;
		operatorButtons[1] = backButton;
		
		panel = new JPanel();
		panel.setBounds(135, 75, 290, 80);
		
		partPanel = new JPanel();
		partPanel.setBounds(200, 460, 300, 50);
		partPanel.setLayout(new GridLayout(1,1));

		
		resultsPanel = new JPanel(new BorderLayout());
		resultsPanel.setBounds(38, 22, 537, 450);
		resultsPanel.setLayout(new GridLayout(1, 9, 5, 0));
		
		
		for (int i = 0; i < operatorButtons.length; i++) {
			operatorButtons[i].addActionListener(this); //allows and records an input
			operatorButtons[i].setFont(myFont);
			operatorButtons[i].setFocusable(false); // disables weird outline when selecting button
		}
		
		panel.add(enterButton);
		frame.add(backButton);
		backButton.setVisible(false);
		backButton.setFont(new Font("Arial", Font.PLAIN, 14));
		
		frame.add(textfield);
		frame.add(panel);
		frame.add(resultsPanel);
		frame.add(partPanel);
		frame.add(textfield1);
		frame.setVisible(true);	
		
		panel.add(errorMsg);
		
		errorMsg.setVisible(false);
		resultsPanel.setVisible(false);
		partPanel.setVisible(false);

	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			
			resultsPanel.removeAll();
			resultsPanel.setVisible(false);
			textfield.setVisible(true);
			textfield1.setVisible(true);
			panel.setVisible(true);
			backButton.setVisible(false);
			partPanel.removeAll();
			partPanel.setVisible(false);
			
			
		}
		
		if (e.getSource() == enterButton) { 
			
			
			Document doc = null;
			Document[] selectedDocs = new Document[12];
			String name = "";
			int year = 0;
			int tempYear = Integer.parseInt(textfield1.getText());
			int docSize = 0;
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			in.setName(textfield.getText());
			in.setYear(tempYear);
			name = in.getName();
			year = in.getYear();
			backButton.setVisible(true);
			
//			try {
//				JsoupMain.run(year, name);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
			try {
				in.setYearDoc(doc, selectedDocs, docSize);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			docSize = in.getDocSize();
			selectedDocs = in.getSelectedDocs();
			
			if (name == "" || year > currentYear || year < (currentYear -10)) {
				errorMsg.setVisible(true);
			}
			else { 
				panel.setVisible(false);
				textfield.setVisible(false);
				textfield1.setVisible(false);
				errorMsg.setVisible(false);
				displayResults(docSize);
			}
		}
	}
	
	public void displayResults(int docSize) { 
		GridLayout layout = new GridLayout(8,1);
		
		 Box.Filler glue = (Box.Filler)Box.createVerticalGlue();
		    glue.changeShape(glue.getMinimumSize(), 
		                    new Dimension(0, Integer.MAX_VALUE), // make glue greedy
		                    glue.getMaximumSize());
		    
		    		
		    int[] trNthChild = new int[docSize];
		    in.setTrNthChild(trNthChild);
		    
		int eventCount = 0;
		String event = "Did not participate in event(s)# ";
		for (int j = 0; j < docSize; j++) {
			
			JLabel notParticipated = new JLabel();

			
			int counter = 1;
			
			while(in.getTrNthChild()[j] == -1) {
				eventCount++;
				event +=  (Integer.toString(j+1) + ", ");
				
				if (j == docSize-1) break;
				else j++;
			}
			if (eventCount == docSize) {
				notParticipated = new JLabel(in.getName() + " did not participate in any events for the year " + in.getYear());
			}
			if ((j == (in.getDocSize()-1)) && (in.getTrNthChild()[j] == -1)) break;
			
			JPanel tempPanel = new JPanel();
			JLabel eventText = new JLabel();
			JLabel tempResultsText = new JLabel();
			
			tempPanel.setLayout(layout);
			
			// resultsPanel.add(notParticipated);
			
			eventText = new JLabel("Event #" + (j+1)); // For event Header
			eventText.setFont(eventFont);

			tempPanel.add(eventText);
			tempPanel.add(glue);
			
			for (int i = 7; i <= 9; i++) {
				
				tempResultsText = new JLabel("Run " + (counter) + ":" + in.getSelectedDocs()[j].select("table:nth-child(2) > tbody > tr:nth-child("+in.getTrNthChild()[j]+") > td:nth-child("+i+")").text());
				counter++;
				tempResultsText.setFont(runFont);
				tempPanel.add(tempResultsText);
				
				tempResultsText = new JLabel("Run " + (counter)+ ": " + in.getSelectedDocs()[j].select("table:nth-child(2) > tbody > tr:nth-child("+(in.getTrNthChild()[j]+1)+") > td:nth-child("+i+")").text());// time results, second row.
				counter++;
				tempResultsText.setFont(runFont);
				tempPanel.add(tempResultsText);
			}
			
			tempResultsText = new JLabel("Placement: " + in.getSelectedDocs()[j].select("table:nth-child(2) > tbody > tr:nth-child("+in.getTrNthChild()[j]+") > td:nth-child(1)").text());
			tempResultsText.setFont(runFont);
			tempPanel.add(tempResultsText);
			resultsPanel.add(tempPanel);
			
			if (j == docSize-1) {
				event = event.substring(0, event.length() - 2);
				notParticipated = new JLabel(event);
				partPanel.add(notParticipated);
			}
		}
		resultsPanel.add(glue);
		resultsPanel.setVisible(true);
		
		partPanel.setVisible(true);
	}
}
