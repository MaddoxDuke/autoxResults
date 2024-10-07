import javax.swing.*; // includes functions for JButton, JTextField, JTextArea, JRadioButton, JCheckbox, JMenu, JColorChooser etc.
import java.awt.*; // add Abstract Window Toolkit, allows the popup window
import java.awt.event.*; //record and respond to user interactions
import java.util.Calendar;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.io.IOException;


public class Interface implements ActionListener {
	
	JFrame frame;
	JTextField textfield, textfield1, errorTextfield;
	JButton[] operatorButtons = new JButton[2]; //array to hold operators
	JButton enterButton, resetButton;
	JPanel panel, resultsPanel;
	JLabel[] resultsText = new JLabel[10];
	JLabel errorMsg = new JLabel("Error: Name or Year is Invalid.");
	JLabel nameMsg, yearMsg;
	Font myFont = new Font("Arial", Font.PLAIN,20); // sets the font style

	
	Interface() {
		
		frame = new JFrame("Autocross Results");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allows exit by closing window
		frame.setSize(550,600); //frame size
		frame.setLayout(null);
		
		textfield = new JTextField(); //text field for name
		textfield.setBounds(50, 25, 200, 40); // bounds of the text field added above
		textfield.setFont(myFont); // font within the text field
		textfield.setEditable(true);
		// nameMsg.setText("Enter name here: ");		

		
		textfield1 = new JTextField(); // text field for year
		textfield1.setBounds(275, 25, 200, 40); // bounds of the text field added above
		textfield1.setFont(myFont); // font within the text field
		textfield1.setEditable(true);
		//yearMsg.setText("Enter year here: ");		
		
		enterButton = new JButton("Search");
		resetButton = new JButton("Reset");
		
		errorMsg.setBounds(135, 150, 250,30);
		

		
		operatorButtons[0] = enterButton;
		operatorButtons[1] = resetButton;
		
		
		panel = new JPanel();
		panel.setBounds(135, 75, 250, 80);
		
		resultsPanel = new JPanel(new BorderLayout());
		resultsPanel.setBounds(49, 20, 450, 550);
		resultsPanel.setLayout(new GridLayout(1, 9, 5, 5));

		
		for (int i = 0; i < operatorButtons.length; i++) {
			operatorButtons[i].addActionListener(this); //allows and records an input
			operatorButtons[i].setFont(myFont);
			operatorButtons[i].setFocusable(false); // disables weird outline when selecting button
		}
		
		panel.add(enterButton);
		
		frame.add(textfield);
		frame.add(panel);
		frame.add(resultsPanel);
		frame.add(textfield1);
		frame.setVisible(true);
		
		
		panel.add(errorMsg);
		errorMsg.setVisible(false);
		
		resultsPanel.setVisible(false);

	}
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == enterButton) { 
			
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			
			String name = findName(textfield);
			int year = findYear(textfield1.getText());
						
			if (name == "" || year > currentYear || year < (currentYear -10)) {
				errorMsg.setVisible(true);
			}
			else { 
				panel.setVisible(false);
				textfield.setVisible(false);
				textfield1.setVisible(false);
				errorMsg.setVisible(false);
				displayResults();

					try {
						JsoupMain.misc(year, name);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		}
	}
	public int findYear(String textfield1) {
		
		int year = Integer.parseInt(textfield1);
		
		return year;
	}
	public String findName(JTextField textfield) {
		
		String name = textfield.getText();
		
		return name;
	}
	public void displayResults() { 
		
		GridLayout layout = new GridLayout(8,1);
		
		 Box.Filler glue = (Box.Filler)Box.createVerticalGlue();
		    glue.changeShape(glue.getMinimumSize(), 
		                    new Dimension(0, Integer.MAX_VALUE), // make glue greedy
		                    glue.getMaximumSize());
		    		
		for (int j = 1; j <= 9; j++) {
			JPanel tempPanel = new JPanel();
			tempPanel.setLayout(layout);
			
			JLabel tempResultsText = new JLabel("Event #" + j); // For event Header
			tempResultsText.setFont(new Font("ARIAL", Font.BOLD, 8));
			
			tempPanel.add(tempResultsText);
			
			for (int i = 0; i < 6; i++) {
				tempResultsText = new JLabel("Run " + (i+1) + ":");
				tempResultsText.setFont(new Font("ARIAL", Font.PLAIN, 7));
				
				tempPanel.add(tempResultsText);
			}
			
			tempResultsText = new JLabel("Placement: ");
			tempResultsText.setFont(new Font("ARIAL", Font.PLAIN, 7));
			tempPanel.add(tempResultsText);
			resultsPanel.add(tempPanel);
		}
			
		resultsPanel.setVisible(true);
	}
}

