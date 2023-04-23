import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class El_Stinky_Vocab {
	
	Responses testResponse = new Responses();
	Responses testResponse2 = new Responses();
	El_Stinky_Memory responsesMem = new El_Stinky_Memory();
	
	int totalResponsesCount = 0;
	int randomHolder = 0;
	
	public void Test() {
		responsesMem.TestResponses();
	}
	
	public int HoldQuestionLoc() {
		return randomHolder;
	}
	
	public String AskingQuestion() {
		Random r = new Random();
		//System.out.println("Counted: " + responsesMem.GetSize());
		randomHolder = r.nextInt(responsesMem.GetSize());
		while(responsesMem.GetResponsesString(randomHolder).Question() != 1) {
		randomHolder = r.nextInt(responsesMem.GetSize()); //Make it so the original mood variable is a okay question variable
		}
		return responsesMem.GetResponsesString(randomHolder).GetUser();
	}
	
	public void AddUserInput(int location, String userInput) {
		responsesMem.GetResponsesString(location).AddResponses(userInput, 0, 0);
	}
	
	public String SelectResponse(String userInput) {
		//Test to see if the user input is in list
		//System.out.println("Number of inputs: " + responsesMem.GetSize());
		for(int i = 0; i < responsesMem.GetSize(); i++) {
			//System.out.println("Comparing: " + responsesMem.GetResponsesString(i).GetUser() + "|" + userInput + "|" + i);
			if(responsesMem.GetResponsesString(i).GetUser().equalsIgnoreCase(userInput)) {
				//System.out.println("True");
				return responsesMem.GetResponsesString(i).RandomSelection();
			}
		}
		//This is where I need to add more ml style
		return "What did you say?";
	}
	
	public void CreateFile() {
		try {
		      File myObj = new File("/El_Stinky/src/files/ChatBot.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public void ReadFromFile() {
		try  
		{  
			File file=new File("/El_Stinky/src/files/ChatBot.txt");    //creates a new file instance  
			FileReader fr=new FileReader(file);   //reads the file  
			BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
			StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
			String line, holder = "";
		while((line=br.readLine())!=null)  
		{  
			sb.append(line);      //appends line to string buffer  
			sb.append("\n");     //line feed  
			holder = line;
			totalResponsesCount++;
			Responses readResponse = new Responses();
			//System.out.println("Entire: " + holder);
			//System.out.println("User: " + holder.substring(0, holder.indexOf("|"))); //Gets user input
			readResponse.AddUserInput(holder.substring(0, holder.indexOf("|")));
			
			holder = holder.substring(holder.indexOf("|") + 1);
			//System.out.println("Mood Change: " + holder.substring(0, holder.indexOf("|"))); //Gets Mood change
			readResponse.AddMoodChange(Integer.parseInt(holder.substring(0, holder.indexOf("|"))));
			
			//System.out.println("Current: " + holder.substring(holder.indexOf("|") + 1) + "\n");
			holder = holder.substring(holder.indexOf("|") + 1);
			ArrayList<String> fullStopTest = new ArrayList<String>(); //Gets the responses & breaks them down
			while(holder.contains(".")) {
				fullStopTest.add(holder.substring(0, holder.indexOf(".")));
				holder = holder.substring(holder.indexOf(".") + 1);
				//System.out.println(holder);
			}
			//System.out.println(holder);
			fullStopTest.add(holder.toString());
			
			//System.out.println("Responses: ");
			for(int i = 0; i < fullStopTest.size(); i++) {
				//System.out.println("\t\tResponse: " + fullStopTest.get(i).substring(0, fullStopTest.get(i).indexOf("(")));
				//System.out.println("\t\tWeight: " + fullStopTest.get(i).substring(fullStopTest.get(i).indexOf("(") + 1, fullStopTest.get(i).indexOf("[")));
				//System.out.println("\t\tMood Check: " + fullStopTest.get(i).substring(fullStopTest.get(i).indexOf("[") + 1));
				readResponse.AddResponses(fullStopTest.get(i).substring(0, fullStopTest.get(i).indexOf("(")), Integer.parseInt(fullStopTest.get(i).substring(fullStopTest.get(i).indexOf("(") + 1, fullStopTest.get(i).indexOf("["))), Integer.parseInt(fullStopTest.get(i).substring(fullStopTest.get(i).indexOf("[") + 1)));
				//System.out.println("This: " + fullStopTest.get(i).substring(0, fullStopTest.get(i).indexOf("(")) + "|" + Integer.parseInt(fullStopTest.get(i).substring(fullStopTest.get(i).indexOf("(") + 1, fullStopTest.get(i).indexOf("["))) + "|" + Integer.parseInt(fullStopTest.get(i).substring(fullStopTest.get(i).indexOf("[") + 1)));
				//System.out.println("\tCheck: " + fullStopTest.get(i));
				//System.out.println("");
				
			}
			responsesMem.AddToMem(readResponse);
			//System.out.println(responsesMem.GetResponsesString(responsesMem.GetSize() - 1).GetResponses());
			//Break down the responses
		}  
			fr.close();    //closes the stream and release the resources  
			//System.out.println("Contents of File: ");  
			//System.out.println(sb.toString());   //returns a string that textually represents the object  
			
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}  
		//System.out.println(responsesMem.GetResponsesString(0).GetUser());
		//System.out.println(responsesMem.GetResponsesString(1).GetUser());
		//System.out.println(responsesMem.GetResponsesString(2).GetUser());
	}
	
	public void AddNewResponse(String userInput, String newResponse, int moodWeight, int moodCheck) {
		Responses addNew = new Responses();
		addNew.AddUserInput(userInput);
		addNew.AddResponses(newResponse, moodWeight, moodCheck);
		responsesMem.AddToMem(addNew);
	}
	
	public void DecreaseAllWeights() {
		for(int i = 0; i < responsesMem.GetSize(); i++) {
			//for(int x = 0; x < responsesMem.GetResponsesString(i).GetSize(); x++) {
			responsesMem.GetResponsesString(i).DecreaseWeight();
			//}
		}
	}
	
	public void WriteToFile() {
		try {
		      FileWriter myWriter = new FileWriter("/El_Stinky/src/files/ChatBot.txt");
		      for(int i = 0; i < responsesMem.GetSize(); i++) {//This runs through all of the responses
		    	  myWriter.write(SetSyntax(responsesMem.GetResponsesString(i)) + "\n");
		    	  //System.out.println(SetSyntax(responsesMem.GetResponsesString(i)) + "\n");
		      }
		      //myWriter.append((CharSequence) _response);
		      myWriter.close();
		      //System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public String SetSyntax(Responses _response) {
		//Hello|1|Hey!(0.5)[50].What's up!(0.25)[25]&
		//System.out.println("Response: " + _response.GetResponses());
		return _response.GetUser() + "|" + _response.GetMoodChange() + "|" + _response.GetResponses();
	}
}
