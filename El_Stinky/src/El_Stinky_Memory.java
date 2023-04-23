import java.util.ArrayList;

public class El_Stinky_Memory {
	//This on is specifically for the memory storage of the responses
	ArrayList<Responses> responsesHolder = new ArrayList<Responses>();
	
	public int GetSize() {
		return responsesHolder.size();
	}
	
	public void AddToMem(Responses x) {
		responsesHolder.add(x);
	}
	
	public Responses GetResponsesString(int x) {
		//System.out.println("This is it: " + responsesHolder.get(x).CheckResponses(x));
		return responsesHolder.get(x);
	}
	
	public void TestResponses() {
		for(int i = 0; i < responsesHolder.size(); i++) {
			//System.out.println("User: " + responsesHolder.get(i).GetUser());
			//System.out.println("MoodChange: " + responsesHolder.get(i).GetMoodChange());
			//System.out.println("Responses:");
			for(int x = 0; x < responsesHolder.get(i).GetSize(); x++) {
				//System.out.println("\t" + responsesHolder.get(i).GetSepecificResponse(x));
			}
			//System.out.println("");
		}
	}
}
