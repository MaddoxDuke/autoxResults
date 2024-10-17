import javax.swing.*; // includes functions for JButton, JTextField, JTextArea, JRadioButton, JCheckbox, JMenu, JColorChooser etc.
import java.awt.*; // add Abstract Window Toolkit, allows the popup window
import java.awt.event.*; //record and respond to user interactions
import java.util.Calendar;
import javax.swing.JPanel;
import org.jsoup.nodes.Document;
import javax.swing.JFrame;
import java.io.IOException;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


/* TO DO:
 * 
 * Add Autox Classes
 * 
 */

public class Interface implements ActionListener {
	
	JFrame frame;
	static JTextField textfield, textfield1, errorTextfield;
	JButton[] operatorButtons = new JButton[3]; //array to hold operators
	JButton enterButton, backButton, resetButton;
	JPanel panel, resultsPanel, partPanel;
	JLabel[] resultsText = new JLabel[10];
	JLabel errorMsg, nameFormat, yearFormat = new JLabel("");
	Font myFont = new Font("Arial", Font.PLAIN,20); // sets the font style
	Font eventFont = new Font("Arial", Font.BOLD, 12);
	Font runFont = new Font("Arial", Font.PLAIN, 9);
	ReadingVar in = new ReadingVar(0, null, 0, null, null);

	Document doc = null;
	Document[] selectedDocs = new Document[12];
	
	String yearInvalid = "  Year is invalid.";
	String nameNotFound = "Name not found.";
	String nameInvalid = " Name entered is invalid.";
	String name = "";
	int year = 0;
	int docSize = 0;
	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

	
	Interface() {
		
		frame = new JFrame("Autocross Results");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allows exit by closing window
		frame.setSize(600,600); //frame size
		frame.setLayout(null);
		
		nameFormat = new JLabel("       Name Input: \"Last, First\"");
		yearFormat = new JLabel("          Year Input: \"20XX\"");
		
		nameFormat.setFont(eventFont);
		yearFormat.setFont(eventFont);
		
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
		resetButton = new JButton("Reset");
		
		backButton.setBounds(8, 8, 70, 25);
		
		
		operatorButtons[0] = enterButton;
		operatorButtons[1] = backButton;
		operatorButtons[2] = resetButton;
		
		panel = new JPanel();
		panel.setBounds(90, 75, 400, 120);
		panel.setLayout(new GridLayout(3, 2, 6, 5));
		
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
		
		
		panel.add(nameFormat);
		panel.add(yearFormat);
		panel.add(textfield);
		panel.add(textfield1);
		panel.add(enterButton);
		panel.add(resetButton);
		
		frame.add(backButton);
		backButton.setVisible(false);
		backButton.setFont(new Font("Arial", Font.PLAIN, 14));
		
		frame.add(panel);
		frame.add(resultsPanel);
		frame.add(partPanel);
		frame.setVisible(true);	
				
		resultsPanel.setVisible(false);
		partPanel.setVisible(false);
		

	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetButton) {
			textfield.setText("");
			textfield1.setText("");
		}
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
		
			partPanel.removeAll();
			partPanel.setVisible(false);
			docSize = 0;
			selectedDocs = new Document[12];
			in.setName(textfield.getText());
			if (textfield1 != null) in.setYear(Integer.parseInt(textfield1.getText()));
			name = in.getName();
			year = in.getYear();
			
			if (name == "" || !(name.contains(","))) {
				errorMsg = new JLabel(nameInvalid);
				partPanel.add(errorMsg);
				partPanel.setVisible(true);
			}
			if (year > currentYear || year < (currentYear -10)) {
				errorMsg = new JLabel(yearInvalid);
				partPanel.add(errorMsg);
				partPanel.setVisible(true);
			}
			else { 
				try {
					in.setYearDoc(doc, selectedDocs, docSize);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				docSize = in.getDocSize();
				selectedDocs = in.getSelectedDocs();
				backButton.setVisible(true);
				panel.setVisible(false);
				partPanel.setVisible(false);
				textfield.setVisible(false);
				textfield1.setVisible(false);
				displayResults(docSize);
			}
		}
	}
	
	public void displayResults(int docSize) { 
		GridLayout layout = new GridLayout(8,1);
		boolean newCacheFile = false;
		boolean chck = false;
		
		 Box.Filler glue = (Box.Filler)Box.createVerticalGlue();
		    glue.changeShape(glue.getMinimumSize(), 
		                    new Dimension(0, Integer.MAX_VALUE), // make glue greedy
		                    glue.getMaximumSize());
		    
		    		
		    try {
				chck = checkCache(name, year);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    if (chck == false) {
			    int[] trNthChild = new int[docSize];
			    in.setFindTrNthChild(trNthChild, docSize);
			    try {
					newCacheFile = newCache();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
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
		for (int i = 0; i < in.getTrNthChild().length; i++) System.out.println(in.getTrNthChild()[i]);
		try {
			if (chck == false && newCacheFile == false) addToCache();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		partPanel.setVisible(true);
		return;
	}
	public void addToCache() throws IOException{
		String line = "";
		System.out.println("Adding to cache...");		
		String fileName = "Cache.csv";
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		
		String tempYear = br.readLine();
		System.out.println(tempYear);
		br.readLine();
						
		try (FileWriter Writer = new FileWriter(fileName, true)) {
			
			while (br.readLine() != null) {
				line = br.readLine();
			}
	
				if (br.readLine() == null) {
					Writer.append(Integer.toString(in.getYear()))
						  .append(',')
						  .append(in.getName().replaceAll("\\s+",""))
						  .append(',')
						  .append(Integer.toString(in.getDocSize()))
						  .append(',');
					for (int i = 0; i < in.getDocSize(); i++) {
						Writer.append(Integer.toString(in.getTrNthChild()[i]))
							  .append(',');
					}
					Writer.append("\n");
					Writer.close();
					System.out.println("Added to Cache.");
					
					Writer.close();
					return;
				}
			
		}
		
		return;
	}
	public boolean checkCache(String name, int year) throws IOException{

		String line = "";
		String splitBy = ",";
		String fileName = "Cache.csv";
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		LinkedList<String> list = new LinkedList<String>();
		
		System.out.println("Checking Cache...");
		
		String tempMonth = br.readLine();
		System.out.println(tempMonth);
		
		if (!tempMonth.contentEquals(Integer.toString(currentMonth))) {
			return false;
		}
		
		while ((line = br.readLine()) != null) {
			String[] data = line.split(splitBy);
			int year1 = Integer.parseInt(data[0]);
			String lName = data[1];
			String fName = data[2];
			if (name.equalsIgnoreCase(lName + ", " + fName) && year == year1) {
				int docSize1 = Integer.parseInt(data[3]);
				int[] arrayAddress = new int[docSize1];
				for (int i = 0; i < docSize1; i++) {
					arrayAddress[i] = Integer.parseInt(data[4+i]);
				}
				in.setDocSize(docSize1);
				in.setTrNthChild(arrayAddress);
				return true;
			}
		}
		System.out.println("Name not in cache.");
		return false;
	}
	public boolean newCache() throws IOException {
		String tempCache = "tempCache.csv";
		String cache = "Cache.csv";
		
		FileReader fr = new FileReader(cache);
		BufferedReader br = new BufferedReader(fr);
		
		if (br.readLine().contentEquals(Integer.toString(currentMonth))) return false;
				
		try (FileWriter tempWriter = new FileWriter(cache, false)) {
				
					tempWriter.append(Integer.toString(currentMonth))
						  .append("\n")
						  .append(Integer.toString(in.getYear()))
						  .append(',')
						  .append(in.getName().replaceAll("\\s+",""))
						  .append(',')
						  .append(Integer.toString(in.getDocSize()))
						  .append(',');
					for (int i = 0; i < in.getDocSize(); i++) {
						tempWriter.append(Integer.toString(in.getTrNthChild()[i]))
							  .append(',');
					}
					tempWriter.append("\n");
					tempWriter.close();
					System.out.println("Added to Cache.");
					
					tempWriter.close();
					return true;
		}
		catch (IOException e) {
			return false;
		}	
	}
}
