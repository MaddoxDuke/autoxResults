import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Calendar;

/* 
 * To Do:
 * Embed in website
 * 
 * figure out how to search using classes
 * 
 * Display Classes
 * Display Overall Pax
 * Display Raw Standing
 * 
 * Create database using a class system (Cache)
 * 
 */
public class JsoupMain {

	public static void main(String[] args) throws IOException {  
		Interface ui = new Interface();
	}
	/*
	public static void run(int year, String name) throws IOException {
		ReadingVar in = new ReadingVar(year, name, 0, null, null);
		
		Document[] selectedDocs = new Document[12]; // array of yearly links to results
		Document doc = null;
		int docSize = 0;
		
		System.out.println("Searching...");
		
		in.setYearDoc(doc, selectedDocs, docSize);
		docSize = in.getDocSize();
		selectedDocs = in.getSelectedDocs();
				
		int[] trNthChild = new int[docSize];
		int searchNum = 350;
		//21.7
		for (int j = 0; j < docSize; j++) { // loop to locate row that contains name
			for (int i = 0; i < searchNum; i++) {
					String temp = selectedDocs[j].selectXpath("/html/body/a/table[2]/tbody/tr["+i+"]/td[4]").text(); // name locations
		            if(temp.contains(name)){
		                trNthChild[j] = i; // array for name addresses
		                searchNum = i + 100;
		                break;
				}
		            else trNthChild[j] = -1; // assigns a -1 to the events not participated in
		    }
			System.out.println("Loading... (" + j + "/" + docSize + ")");
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
	
	public static int findYearDoc(int year, Document[] selectedDocs, int docSize) throws IOException {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		Document doc;
		
		if (year == currentYear) {
			doc = Jsoup.connect("https://www.texasscca.org/solo/results/").timeout(6000).get(); // current year link
			docSize = currentYear(doc, selectedDocs, docSize);
			return docSize;
		}
		if (year >= (currentYear-10) && year < currentYear){
			doc = Jsoup.connect("https://www.texasscca.org/solo/results/past-results/").timeout(6000).get();
			docSize = pastYear(doc, selectedDocs, docSize, year);
			return docSize;
		}
		if (year < (currentYear-10) || year > currentYear) {
			System.out.println("Year entered is invalid.");
			return -1;
		}
		else return -1;
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
	*/
}
