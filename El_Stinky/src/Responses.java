import java.util.ArrayList;
import java.util.Random;

public class Responses {
	
	String userInput = "";
	
	int moodChange = 1;
	
	ArrayList<String> responses = new ArrayList<String>();
	ArrayList<Integer> moodWeights = new ArrayList<Integer>();
	ArrayList<Integer> moodChecks = new ArrayList<Integer>();
	
	//|Hello|1|Hey!(0.5)[50].What's up!(0.25)[25]&
	
	public int Question() {
		return moodChange;
	}
	
	public void AddResponses(String _response, int moodWeight, int moodCheck) {
		responses.add(_response);
		moodWeights.add(moodWeight);
		moodChecks.add(moodCheck);
	}
	
	public String CheckResponses(int x) {
		return responses.get(x);
	}
	
	public int GetSize() {
		return responses.size();
	}
	
	public String GetSepecificResponse(int x) {
		return responses.get(x) + "|" + moodWeights.get(x) + "|" + moodChecks.get(x);
	}
	
	public String GetResponses() {
		//List out all responses 
		String holder = "";
		for(int i = 0 ; i < responses.size(); i++) {
			holder = holder + responses.get(i) + "(" + moodWeights.get(i) + "[" + moodChecks.get(i) + "";
			if(i + 1 < responses.size()) {
				holder = holder + ".";
			}
		}
		return holder;
	}
	
	public void UpdateWeight(int x) {
		if(moodWeights.get(x) < 91) {//Checks to make sure it doesnt go over 100
			moodWeights.set(x, moodWeights.get(x) + 2);
		}
	}
	
	public void DecreaseWeight() {
		for(int i = 0; i < moodWeights.size(); i++) {
			moodWeights.set(i, moodWeights.get(i) - 1);
			if(moodWeights.get(i) < 0) {
				moodWeights.set(i, 0);
			}
		}
	}
	
	public void AddMoodChange(int y) {
		moodChange = y;
	}
	
	public void AddUserInput(String x) {
		userInput = x;
	}
	
	public String GetUser() {
		return userInput;
	}
	
	public int GetMoodChange() {
		return moodChange;
	}
	
	public String RandomSelection() {
		//System.out.println(responses.size());
		if(responses.size() > 0) {
		Random r = new Random();
		int compare = r.nextInt(responses.size());
		//System.out.println(responses.get(compare));
		UpdateWeight(compare);
		return responses.get(compare);
		}
		UpdateWeight(0);
		return responses.get(0);
	}
	
	public String FindBest() {
		int compare = 0;
		for(int i = 0; i < moodWeights.size(); i++) {
			if(moodWeights.get(compare) < moodWeights.get(i)) {
				compare = i;
			}
		}
		//System.out.println(responses.get(compare));
		return responses.get(compare);
	}
}
