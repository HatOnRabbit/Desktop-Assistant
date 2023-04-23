import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class El_Stinko implements ActionListener, KeyListener{

	El_Stinky_Vocab elStinkyVocab = new El_Stinky_Vocab();
	
	El_Stinko_Lib elStinkoLib = new El_Stinko_Lib();
	
	public int moodValue = 50;
	public boolean inputNew = false;//Tells the bot a new input is gonna happen
	public String lastInput = "";//Gives last response
	public boolean computerQuestion = false;
	
	Random r = new Random();
	
	ArrayList<String> recentMem = new ArrayList<String>();
	ArrayList<Integer> recentMemWho = new ArrayList<Integer>();
	//String recentMem[];
	

	public void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	JFrame mainFrame = new JFrame("Little El Stinko");
	
	public void ShowRecentMem(JTextArea x) {
		x.setText("");
		for(int i = 0; i < recentMem.size(); i++) {
			/*
			if(i % 2 == 0) {
				x.append("User: " + recentMem.get(i));
			}else {
				x.append("Amelia: " + recentMem.get(i));
			}
			*/
			if(recentMemWho.get(i) == 0) {
				x.append("User: " + recentMem.get(i));
			}else {
				x.append("Shodan: " + recentMem.get(i));
			}
		}
	}
	
	public void AddMem(String x) {
		if(recentMem.size() > 8) {
			recentMem.remove(0);
		}
		recentMem.add(x);
	}
	
	public void AddMemWho(int x) {
		if(recentMemWho.size() > 8) {
			recentMem.remove(0);
		}
		recentMemWho.add(x);
	}
	
	public String ComputerQuestion() {
		return elStinkyVocab.AskingQuestion();
	}
	
	public void UserResponse(String x) {
		//System.out.println("Read: " + x);
		elStinkyVocab.AddUserInput(elStinkyVocab.HoldQuestionLoc(), x);
	}
	
	public void StartUpSequence() {
		//mainFrame.setUndecorated(true); //This is the borderless mode
		//System.out.println(computerQuestion);
		//ComputerQuestion();
		Color backGroundColour = new Color(130, 83, 223);
		Color boxesColour = new Color(150, 128, 239);
		mainFrame.getContentPane().setBackground(backGroundColour);
		mainFrame.setResizable(false);
		String path = "/El_Stinky/src/files/HatOnRabbit_Dana_PFP.png"; 
		///El_Stinky/src/files/Jab.png /El_Stinky/src/files/Jab.png
		///El_Stinky/src/files/HatOnRabbit_Dana_PFP.png
		///El_Stinky/src/files/Female_Rabbit.png
        File file = new File(path);
        BufferedImage image = null;
        Image resultingImage = null;
		try {
			image = ImageIO.read(file);
			resultingImage = image.getScaledInstance(200, 250, Image.SCALE_SMOOTH); //250 250 for not scp
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mainFrame.setIconImage(resultingImage);//This changes left corner icon
		
        JLabel label = new JLabel(new ImageIcon(resultingImage));

        mainFrame.add(label);
        
		JTextArea textArea = new JTextArea(10, 35);
		JTextField textField = new JTextField(35);
		JButton exitButton = new JButton("Exit");
		//JButton enterButton = new JButton("Enter");
		
		textField.setMaximumSize(new Dimension(35, 20));
		textArea.setMaximumSize(new Dimension(10, 35));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		
		Font font = new Font("Osaka", Font.BOLD, 12);
		Font font_User = new Font("Osaka", Font.BOLD, 12);
		textField.setFont(font);
		textArea.setFont(font);
		textField.setForeground(new Color(38, 70, 83));
		/*
		JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        */
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setSize(5, 35);
		//this.add(textArea); // get rid of this
		
		
		textArea.setBackground(boxesColour);
		textField.setBackground(boxesColour);
		
		elStinkyVocab.ReadFromFile();
		elStinkyVocab.Test();
		textArea.setEditable(false);
		
		mainFrame.setLayout(new FlowLayout());
		//mainFrame.add(text); 
		mainFrame.add(scroll);
		//mainFrame.add(textArea);
		mainFrame.add(textField);
		//mainFrame.add(exitButton); //Outdated
		//mainFrame.add(enterButton); //Outdated
		
		textField.addKeyListener(new KeyAdapter()
	    {
		      public void keyPressed(KeyEvent e)
		      {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER)
		        {
		        	int x;
		        	//Runs on the Enter Key
		        	elStinkoLib.RunOnce();
					AddMem(textField.getText() + "\n"); //Adds user to recent memory
					AddMemWho(0);
					//add an if statement here to switch to computer questions
					if(!computerQuestion) {
						if(!inputNew) {
							if(GetResponse(textField.getText()).equals("What should I respond to that with?")) {
								//This is where you can insert new response
								//lastInput = textField.getText();
								inputNew = true;
							}else{
								AddMem(GetResponse(textField.getText()) + "\n"); //Adds computer's response to recent memory
								AddMemWho(1);
							}
						}else{
							//System.out.println("New response");
							AddMem("Adding [" + textField.getText() + "] to memory\n");
							AddMemWho(1);
							inputNew = false;
							elStinkyVocab.AddNewResponse(lastInput, textField.getText(), 1, 0);
							//System.out.println("Adding '" + textField.getText() + "' to memory\n");
						}
						lastInput = textField.getText();
					}else {
						//Get user response to question here
						UserResponse(textField.getText());
						computerQuestion = false;
					}
						textField.setText("");
						x = r.nextInt(5);
						//System.out.println();
						if(!inputNew && x == 0) {
							computerQuestion = true;
							//System.out.println("Question: " + ComputerQuestion());
							AddMem(ComputerQuestion() + "\n");
							AddMemWho(1);
						}
						ShowRecentMem(textArea); //Puts the recent memory into the text area
		        }
		      }
		    });
		
		mainFrame.addWindowListener(new WindowAdapter() {//This just closes the application faster

	        public void windowClosing(WindowEvent evt) {
	        	elStinkyVocab.DecreaseAllWeights();
	        	elStinkyVocab.WriteToFile();
	        	elStinkoLib.SaveToFiles();
	            System.exit(0);
	        }
	}); 
		
		exitButton.addActionListener(new ActionListener() {//Outdated
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
					mainFrame.dispose();
				
			}
		});
		
		
		mainFrame.setSize(420, 520);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	
	public String GetResponse(String x) {
		Random r = new Random();
		//Runs it through my computer
		//System.out.println(elStinkoLib.CommandPrompt(x));
		//if computer asks a question
		//elStinkyVocab.AskingQuestion();
		
		if(elStinkoLib.CommandPrompt(x) != "No Commands") {
			return elStinkoLib.CommandPrompt(x);
		}
		
		//Run chat Bot
		//System.out.println(elStinkyVocab.SelectResponse(x));
		if(elStinkyVocab.SelectResponse(x).equals("What did you say?")){//After this is should switch into new input mode
			inputNew = true;
			return "What would you expect me to respond to that?";
		}else {
			return elStinkyVocab.SelectResponse(x); //This is just a known response
		}
	}

	public El_Stinko() {
		//Read file
		elStinkoLib.AddURLToPlayList();
		elStinkoLib.AddTextToPlayList();
		elStinkoLib.TestLists();
		elStinkoLib.AddToDoListArray();
		StartUpSequence();
		//pause(5000);
		//mainFrame.dispose();
	}
	
	public static void main(String[] args) {
		new El_Stinko();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Default action
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}