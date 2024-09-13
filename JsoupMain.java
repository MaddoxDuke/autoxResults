import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Calendar;

public class JsoupMain  {

	public static void main(String[] args) throws IOException 
	{  
		Document[] selectedDocs = new Document[12]; // array of yearly links to results
		Document doc;
		int docSize = 0;
		
		String name = "Duke, Maddox";
		int year = 2022; // year input
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		
		
		if (year == currentYear) {
			doc = Jsoup.connect("https://www.texasscca.org/solo/results/").timeout(6000).get(); // current year link
			docSize = currentYear(doc, selectedDocs, docSize);
		}
		if (year >= (currentYear-10) && year < currentYear){
			doc = Jsoup.connect("https://www.texasscca.org/solo/results/past-results/").timeout(6000).get();
			docSize = pastYear(doc, selectedDocs, docSize, year);
		}
		if (year < (currentYear-10) || year > currentYear) {
			System.out.println("Year entered is invalid.");
			return;
		}
				
		int[] trNthChild = new int[docSize];
		int searchNum = 350; //cannot have more entries than 350.
		//21.7
		for (int j = 0; j < docSize; j++) { // loop to locate row that contains name
			for (int i = 0; i < searchNum; i++) { // information is unsorted so must brute force the data
					String temp = selectedDocs[j].selectXpath("/html/body/a/table[2]/tbody/tr["+i+"]/td[4]").text(); // name locations
		            if(temp.contains(name)){
		                trNthChild[j] = i; // array for name addresses
		                searchNum = i + 100; // the name will not be on a row greater than this. Can narrow search to increase speed
		                break;
				}
		            else trNthChild[j] = -1; // assigns a -1 to the events not participated in
		    }
		}
		int eventCount = 0;
		for (int i = 0; i < docSize; i++) { //loop to find if name exists on any of the result links.
			if (trNthChild[i] == -1) {
				eventCount++;
				System.out.println("Did not participate in Event #" + (i+1));
			}
		}
		if (eventCount == docSize) {
			System.out.println(name + " has not participated in any events in " + year); //skips finalOutput if not participated
			return;
		}
		finalOutput(docSize, trNthChild, name, year, selectedDocs);
	}
	
	public static int currentYear(Document doc, Document[] selectedDocs, int docSize) throws IOException {
		for (int i = 1; i <= 10; i++) {
			if (i%2 == 0) {
				Element link1 = doc.select("tr.row-"+(i)+".even > td.column-4 > a").first(); //even rows
				if (link1 != null) {
				String url = link1.attr("href"); 
				selectedDocs[docSize] = Jsoup.connect(url).timeout(6000).get();
				docSize++;
				}
			}
			if (i%2 != 0) {
				Element link = doc.select("tr.row-"+(i)+".odd > td.column-4 > a").first(); // odd rows
				if (link != null) {
				String url = link.attr("href");
				selectedDocs[docSize] = Jsoup.connect(url).timeout(6000).get();
				docSize++;
				}
			}
		}
		return docSize;
	}
	public static int pastYear(Document doc, Document[] selectedDocs, int docSize, int year) throws IOException {
		
		for (int i = 1; i <= 10; i++) { // for loop to find the links from the year chosen
			if (i%2 == 0) {
				Element link1 = doc.select("#tablepress-300-"+(year-2000)+"R > tbody > tr.row-"+(i)+".even > td.column-4 > a").first(); //even rows
				if (link1 != null) {
				String url = link1.attr("href"); 
				selectedDocs[docSize] = Jsoup.connect(url).timeout(6000).get();
				docSize++;
				}
			}
			if (i%2 != 0) {
				Element link = doc.select("#tablepress-300-"+(year-2000)+"R > tbody > tr.row-"+(i)+".odd > td.column-4 > a").first(); // odd rows
				if (link != null) {
				String url = link.attr("href");
				selectedDocs[docSize] = Jsoup.connect(url).timeout(6000).get();
				docSize++;
				}
			}
		}
		
		return docSize;
	}
		
	public static void finalOutput(int docSize, int[] trNthChild, String name, int year, Document[] selectedDocs) {
		System.out.println(name + "\nYear: " + year);
		
		for (int j = 0; j < docSize; j++) {
			int counter = 1;
			while (trNthChild[j] == -1) { // while loop to skip the events that the individual did not participate in
				if (j == docSize-1) break;
				else j++;
			}
			if (j == docSize-1 && trNthChild[j] == -1) break; // breaks from for loop if not participated on last event
			
			System.out.println("Event #" + (j+1)); 
			
			for (int i = 7; i <= 9; i++) { // for loop to output the run times
				System.out.println("Run " + (counter)+ ": " + selectedDocs[j].select("table:nth-child(2) > tbody > tr:nth-child("+trNthChild[j]+") > td:nth-child("+i+")").text()); // time results
				counter++;
				System.out.println("Run " + (counter)+ ": " + selectedDocs[j].select("table:nth-child(2) > tbody > tr:nth-child("+(trNthChild[j]+1)+") > td:nth-child("+i+")").text());// time results, second row.
				counter++;
			}
			System.out.println("Placement: " + selectedDocs[j].select("table:nth-child(2) > tbody > tr:nth-child("+trNthChild[j]+") > td:nth-child(1)").text()); // position
		}		
		
	}
}
