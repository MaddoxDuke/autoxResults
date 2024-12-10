import java.util.Calendar;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ReadingVar {

	private int year;
	private String name;
	private int docSize;
	private Document[] selectedDocs;
	private int[] trNthChild;
	
	public ReadingVar(int year, String name, int docSize, Document[] selectedDocs,int[] trNthChild) {
		
		this.year = year;
		this.name = name;
		this.docSize = docSize;
		this.selectedDocs = selectedDocs;
		this.trNthChild = trNthChild;
		
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	public int getYear() {
		return year;
	}
	public void setYearDoc(Document doc, Document[] selectedDocs, int docSize) throws IOException {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		
		if (year == currentYear) {
			this.docSize = 0;
			doc = Jsoup.connect("https://www.texasscca.org/solo/results/").timeout(6000).get();
			for (int i = 1; i < 12; i++) { 
				if (doc.selectXpath("//*[@id=\"tablepress-300-24R\"]/tbody/tr["+i+"]/td[1]").hasText()) this.docSize++;
			}			
			this.selectedDocs = new Document[this.docSize];


			for (int i = 1; i <= this.docSize; i++) {
				if (i%2 == 0) {
						this.selectedDocs[i-1] = Jsoup.connect("https://www.texasscca.org/Solo/"+year+"/Results/"+year+"-E"+i+"-Final.htm").timeout(6000).get();
				}
				if (i%2 != 0) {
						this.selectedDocs[i-1] = Jsoup.connect("https://www.texasscca.org/Solo/"+year+"/Results/"+year+"-E"+i+"-Final.htm").timeout(6000).get();
				}
			}
		}
		if (year >= (currentYear-10) && year < currentYear) {
			doc = Jsoup.connect("https://www.texasscca.org/solo/results/past-results/").timeout(6000).get();
			int count = 0;
			this.docSize = 0;
			
			for (int i = 1; i < 12; i++) if (doc.selectXpath("//*[@id=\"tablepress-300-"+(year-2000)+"R\"]/tbody/tr["+i+"]/td[1]").hasText()) this.docSize++;
			
			this.selectedDocs = new Document[this.docSize];

			for (int i = 1; i <= this.docSize; i++) { // for loop to find the links from the year chosen
				if (i%2 == 0) {					
					this.selectedDocs[i-1] = Jsoup.connect("https://www.texasscca.org/Solo/"+year+"/Results/"+year+"-E"+i+"-Final.htm").timeout(6000).get();
				}
				if (i%2 != 0) {
					this.selectedDocs[i-1] = Jsoup.connect("https://www.texasscca.org/Solo/"+year+"/Results/"+year+"-E"+i+"-Final.htm").timeout(6000).get();
				}
			}
		}
		if (year < (currentYear-10) || year > currentYear) {
			System.out.println("Year entered is invalid.");
		}
		System.out.println(docSize);
	}
	public Document[] getSelectedDocs() {
		return selectedDocs;
	}
	public void setName(String name) {
		
		name = Interface.textfield.getText();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public int getDocSize() {
		return docSize;
	}
	public void setDocSize(int docSize) {
		this.docSize = docSize;
	}
	public int[] getTrNthChild() {
		return trNthChild;
	}
	public void setTrNthChild(int[] trNthChild) {
		this.trNthChild = trNthChild;
	}
	public void setFindTrNthChild(int[] trNthChild, int docSize) {
		int searchNum = 350;
		//21.7
		for (int j = 0; j < this.docSize; j++) { // loop to locate row that contains name
			for (int i = 0; i < searchNum; i++) {
					String temp = selectedDocs[j].selectXpath("/html/body/a/table[2]/tbody/tr["+i+"]/td[4]").text(); // name locations
		            if(temp.equalsIgnoreCase(name)){
		                trNthChild[j] = i; // array for name addresses
		                searchNum = i + 100;
		                break;
				}
		            else trNthChild[j] = -1; // assigns a -1 to the events not participated in
		    }
			System.out.println("Loading... (" + (j+1) + "/" + docSize + ")");
		}
		this.trNthChild = trNthChild;
	}
}
