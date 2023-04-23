import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class El_Stinko_Lib {
	
	//El_Stinko main = new El_Stinko();
	
	ArrayList<String> playListText = new ArrayList<String>();//Use to connect playlist to a tag
	ArrayList<String> playListURL = new ArrayList<String>();
	
	ArrayList<String> toDoList = new ArrayList<String>();
	
	public boolean runOnce = false;
//Commands
	public String CommandPrompt(String x) {
		if(x.equals("Current.Time")) {
			return TimeCommand();
		}else if(x.equals("Total.Space")) {
			return MemoryUsage();
		}else if(x.contains(".Encrypt.")) {
			return Encrypt(x.substring((x.indexOf(".") + 8)), Integer.parseInt(x.substring(0, x.indexOf("."))));
		}else if(x.contains(".Decrypt.")) {
			return Decrypt(x.substring((x.indexOf(".") + 9)), Integer.parseInt(x.substring(0, x.indexOf("."))));
		}else if(x.equals("Help")) {
			return Help();
		}else if(x.equals("System.Info") && runOnce) {
			return SystemInfo();
		}else if(x.equals("System.IP") && runOnce) {
			return GetIpAddress();
		}else if(x.contains("Todo.Add.") && runOnce) {
			runOnce = false;
			return ToDoListAddTo(x.substring(9));
		}else if(x.equals("Todo.List") && runOnce) {
			runOnce = false;
			return ToDoListAddRead();
		}else if(x.equals("Todo.Clear") && runOnce) {
			runOnce = false;
			return ClearToDoList();
		}else if(x.contains("Todo.Remove.") && runOnce) {
			runOnce = false;
			return RemoveFromToDoList(x.substring(12));
		}else if(x.contains("Hide.") && runOnce) {
			runOnce = false;
			try {
				return LockFolder(x.substring(5));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(x.contains("Show.") && runOnce) {
			runOnce = false;
			try {
				return UnlockFolder(x.substring(5));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(x.equals("Playlist.Clear")) {
			return ClearPlaylists();
		}else if(x.equals("Playlist.Get")) {
			return PlayListsList();
		}else if(x.contains("PlaylistAdd.") && runOnce) {
			runOnce = false;
			//System.out.println("Adding\n");
			return AddToPlayListArray(x.substring(12));
		}else if(x.contains("Playlist.") && runOnce) {
			runOnce = false;
			return PlayYTPlayList(x.substring(9));
		}else if(x.contains("Define.") && runOnce) {
			runOnce = false;
			return WordDef(x.substring(7));
		}//Maybe add a translate function
		if(x.equals("Todo.List") && !runOnce) {
			return ToDoListAddRead();
		}
		if(x.length() > 5) {
			if(x.substring(0, 5).equals("Open.") && runOnce) {
				try {
					OpenPage(x.substring(5));
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				runOnce = false;
				return "Opening";
	    	}else if(x.substring(0, 5).equals("Open.") && !runOnce){
	    		return "Opening";
			}
		}
		
		 if(x.contains("Todo.Remove.") && !runOnce) {
				return "Removing";
		}
		if(x.contains("Hide.") && !runOnce) {
				return "Hidden";
		} 
		if(x.contains("Show.") && !runOnce) {
			return "Released";
		} 
		if(x.contains("Todo.Clear") && !runOnce) {
				return "Clearing list";
		}
		if(x.contains("Todo.Add.") && !runOnce) {
			return "Adding";
		}
		if(x.equals("System.Info") && !runOnce) {
			return "";
		}
		if(x.contains("Playlist.") && !runOnce) {
			return "Playing";
		}
		if(x.contains("Define.") && !runOnce) {
			return "Playing";
		}
		 if(x.contains("PlaylistAdd.") && !runOnce) {
			 return "Adding";
		 }
		//System.out.println(x.substring(0, 5));
		return "No Commands";
	}
	
	private String ToDoListAddRead() {
		String holder = "\n\t[To-do List]\n";
	for(int i = 0; i < toDoList.size(); i++) {
		holder += "- " + toDoList.get(i) + "\n";
	}
	return holder;
}
	
	public void SaveToFiles() {
		RunOnce();
		AddToDoListFile();
		RunOnce();
		SaveToPlayListURLFile();
		RunOnce();
		SaveToPlayListTextFile();
	}
	
	private String RemoveFromToDoList(String x) {
		for(int i = 0; i < toDoList.size(); i++) {
			if(x.equals(toDoList.get(i))) {
				toDoList.remove(i);
				return "Cleared from list";
			}
		}
		return "Error, Element not found";
	}
	
	private String LockFolder(String x) throws InterruptedException, IOException {
		    // win32 command line variant
		File src = new File(x);
		    Process p = Runtime.getRuntime().exec("attrib +h " + src.getPath());
		    p.waitFor(); // p.waitFor() important, so that the file really appears as hidden immediately after function exit.
		return "Locked";
	}
	
	private String UnlockFolder(String x) throws InterruptedException, IOException {
	    // win32 command line variant
	File src = new File(x);
	    Process p = Runtime.getRuntime().exec("attrib -h " + src.getPath());
	    p.waitFor(); // p.waitFor() important, so that the file really appears as hidden immediately after function exit.
	return "Unlocked";
}
	
	private String ClearToDoList() {
		toDoList.clear();
		return "Cleared";
	}

	private String ToDoListAddTo(String x) {
		toDoList.add(x);
		//System.out.println("Size: " + toDoList.size());
		return "Add to list!";
	}
	
	public void AddToDoListArray() {
		try  
		{  
			File file = null;
			FileReader fr = null;
				file=new File("/El_Stinky/src/files/ToDoList.txt");    //creates a new file instance  
				fr=new FileReader(file);   //reads the file  
				BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
				StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
				String line, holder = "";
				while((line=br.readLine())!=null)  
				{  
					sb.append(line);      //appends line to string buffer  
					sb.append("\n");     //line feed  
					holder = line;
						//System.out.println("Adding: " + holder + " to text");
						toDoList.add(line);
				} 
			fr.close();    //closes the stream and release the resources  
		
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}  
	}
	
	public void AddToDoListFile() {
				try {
					FileWriter myWriter = null;
						myWriter = new FileWriter("/El_Stinky/src/files/ToDoList.txt");
						//System.out.println("Size of the array is: " + toDoList.size());
						for(int x = 0; x < toDoList.size(); x++) {//This runs through all of the responses
							myWriter.write(toDoList.get(x) + "\n");
							//System.out.println("Adding to list: " + toDoList.get(x) + "\n");
						}
					//myWriter.append((CharSequence) _response);
					myWriter.close();
					//System.out.println("Successfully wrote to the file.");
		    	} catch (IOException e) {
		    	//System.out.println("An error occurred.");
		      	e.printStackTrace();
		    	}
	}
	
	private String WordDef(String x) {
		//https://www.dictionary.com/browse/
		URI theURI = null;
		try {
				theURI = new URI("https://www.dictionary.com/browse/" + x);
			try {
				java.awt.Desktop.getDesktop().browse(theURI);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Googling";
	}
	
	public String PlayListsList() {
		String holder = "";
		if(playListText.size() < 1) {
			
		}else {
			holder = playListText.get(0);
			for(int i = 1; i < playListText.size(); i++) {
				holder += ", " + playListText.get(i);
			}
		}
		return "Music playlists: " + holder;
	}
	
	public void AddURLToPlayList() {
			try  
			{  
				File file = null;
				FileReader fr = null;
					file=new File("/El_Stinky/src/files/Playlists.txt");    //creates a new file instance  
					fr=new FileReader(file);   //reads the file  
					BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
					StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
					String line, holder = "";
					while((line=br.readLine())!=null)  
					{  
						sb.append(line);      //appends line to string buffer  
						sb.append("\n");     //line feed  
						holder = line;
							//System.out.println("Adding: " + holder + " to text");
							playListURL.add(line);
					} 
				fr.close();    //closes the stream and release the resources  
			
			}  
			catch(IOException e)  
			{  
				e.printStackTrace();  
			}  
	}
	
	public void AddTextToPlayList() {
		try  
		{  
			File file = null;
			FileReader fr = null;
				file=new File("/El_Stinky/src/files/Playlist_Text.txt");    //creates a new file instance  
				fr=new FileReader(file);   //reads the file  
				BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
				StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
				String line, holder = "";
				while((line=br.readLine())!=null)  
				{  
					sb.append(line);      //appends line to string buffer  
					sb.append("\n");     //line feed  
					holder = line;
						//System.out.println("Adding: " + holder + " to text");
						playListText.add(line);
				} 
			fr.close();    //closes the stream and release the resources  
		
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}  
}
	
	public String AddToPlayListArray(String x) {
		playListURL.add(x.substring(x.indexOf("|") + 1));
		playListText.add(x.substring(0, x.indexOf("|")));
		return "Added Text: " + x.substring(0, x.indexOf("|") + 1) + " URL: " + x.substring(x.indexOf("|") + 1);
	}
	
	public void TestLists() {
		for(int i = 0; i < playListText.size(); i++) {
			//.out.println("Text: " + playListText.get(i) + "\n");
		}
		//System.out.println("URL: " + playListURL.size() + "\n");
		for(int i = 0; i < playListURL.size(); i++) {
			//System.out.println("URL: " + playListURL.get(i) + "\n");
		}
	}
	
	public void SaveToPlayListURLFile() {
		if(runOnce) {
			runOnce = false;
				try {
					FileWriter myWriter = null;
						myWriter = new FileWriter("/El_Stinky/src/files/Playlists.txt");
						for(int x = 0; x < playListURL.size(); x++) {//This runs through all of the responses
							myWriter.write(playListURL.get(x) + "\n");
							//System.out.println(playListURL.get(x) + "\n");
						}
					//myWriter.append((CharSequence) _response);
					myWriter.close();
					//System.out.println("Successfully wrote to the file.");
		    	} catch (IOException e) {
		    	//System.out.println("An error occurred.");
		      	e.printStackTrace();
		    	}
			}
	}
	
	public void SaveToPlayListTextFile() {
		if(runOnce) {
			runOnce = false;
				try {
					FileWriter myWriter = null;
						myWriter = new FileWriter("/El_Stinky/src/files/Playlist_Text.txt");
						//System.out.println("Text: " + playListText.size() + "\n");
						for(int x = 0; x < playListText.size(); x++) {//This runs through all of the responses
							myWriter.write(playListText.get(x) + "\n");
							//System.out.println("Adding: " + playListText.get(x) + "\n");
						}
					//myWriter.append((CharSequence) _response);
					myWriter.close();
					//System.out.println("Successfully wrote to the file.");
		    	} catch (IOException e) {
		    	//System.out.println("An error occurred.");
		      	e.printStackTrace();
		    	}
			}
	}
	
	private String ClearPlaylists() {
		playListText.clear();
		playListURL.clear();
		return "Cleared playlist";
	}
	
	private String PlayYTPlayList(String x) {
		//https://www.youtube.com/watch?v=SgPB5fvMR_E&list=PLS0qctopcIfdEiemhn2XJ7GSEXQjG6B3Q (Random all)
		URI theURI = null;
		try {
			for(int i = 0; i < playListText.size(); i++) {
				if(playListText.get(i).equals(x)) {
					theURI = new URI(playListURL.get(i));
					break;
				}
			}
			try {
				java.awt.Desktop.getDesktop().browse(theURI);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Playing " + x;
	}
	
	private String CreateList(String x) {
		return "Work in progress";
	}
	
	int testNum = 0; //Kinda weird but it makes the system info run faster
	
	private String GetIpAddress() {
		// Returns the instance of InetAddress containing
        // local host name and address
        InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("System IP Address : " +
                      (localhost.getHostAddress()).trim());
 
        // Find public IP address
        String systemipaddress = "";
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");
 
            BufferedReader sc =
            new BufferedReader(new InputStreamReader(url_name.openStream()));
 
            // reads system IPAddress
            systemipaddress = sc.readLine().trim();
        }
        catch (Exception e)
        {
            systemipaddress = "Unknown";
        }
        System.out.println("Public IP Address: " + systemipaddress + "\n");
		return "Public address: " + systemipaddress + " | Local: " + (localhost.getHostAddress()).trim();
	}
	
	private String SystemInfo() {
		String graphicsInfo = "";
		//runOnce = false;
		testNum++;
		System.out.println(testNum);
		if(testNum % 6 == 0)
		try {
			System.out.println("Running");
	        String filePath = "./foo.txt";
	        // Use "dxdiag /t" variant to redirect output to a given file
	        ProcessBuilder pb = new ProcessBuilder("cmd.exe","/c","dxdiag","/t",filePath);
	        //System.out.println("-- Executing dxdiag command --");
	        Process p = pb.start();
	        try {
				p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        BufferedReader br = new BufferedReader(new FileReader(filePath));
	        String line;
	        //System.out.println(String.format("-- Printing %1$1s info --",filePath));
	        while((line = br.readLine()) != null){
	            if(line.trim().startsWith("Card name:")){
	                //System.out.println(line.trim());
	                graphicsInfo += "\t" + line.trim() + "\n";
	            }
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
		System.out.println("System: " + graphicsInfo);
		return "Your specs are:\n    Graphics: \n" + graphicsInfo;
	}
	
	private String Help() {
		return "\n\t[General Commands]\nHelp, Open.'Site name', Current.Time, #.Encrypt/Decrypt.#, Total.Space, "
				+ "Define.'Word', Hide.'Path', Show.'Path'\n\n\t[Playlist Commands]\nPlaylist.'Name', Playlist.Get, PlaylistAdd.'Name',   Playlist.Clear"
				+ "\n\n\t[System Commands]\nSystem.Info, System.IP\n\n\t[To Do List Commands]\nTodo.Add.'What you want to add', "
				+ "Todo.Remove.'What you want to remove', Todo.List, Todo.Clear";
	}
	
	public String TimeCommand() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		Date currentTime = new Date();
		return "The current time is: " + formatter.format(currentTime);
	}
	
	public String MemoryUsage() {
		File cDrive = new File("/");
		return "Space: " + (cDrive.getTotalSpace()) + " Bytes";
	}
	
	public String Encrypt(String x, int key) {
		String y = "";
		for(int i = 1; i < x.length(); i++) {
			 y = y + ((int)Character.toLowerCase(x.charAt(i)) + key) + "."; //lowercase
		}
		return y;
	}
	
	public String Decrypt(String x, int key) {
		String holder = "";
		//System.out.println(x);
		while(x.contains(".")) {
			//System.out.println(x.substring(0, x.indexOf(".")) + " | " + ((char)Integer.parseInt(x.substring(0, x.indexOf(".")))));
			//System.out.println(x.substring(0, );
			//8.Decrypt.107.105.124.
			holder = holder + ((char)(Integer.parseInt(x.substring(0, x.indexOf("."))) - key));
			//System.out.println(x.substring(x.indexOf(".")));
			x = x.substring(x.indexOf(".") + 1);
		}
		return holder;
	}
	
	public void OpenPage(String x) throws IOException, URISyntaxException {
		URI theURI = new URI(x);
		java.awt.Desktop.getDesktop().browse(theURI);
	}
	
	public void RunOnce() {
		runOnce = true;
	}
}