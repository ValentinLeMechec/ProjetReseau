import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Maitre
{
	static int compteur = 1;
	

	
	public static void main(String[] args) 
	{
		File racine;
		
		try
		{
			Socket socket = new Socket(InetAddress.getByName("172.18.50.99"), 56565);
			ObjectOutputStream buffOut= new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream buffIn= new ObjectInputStream(socket.getInputStream());
			
			/*envoi au serveur du choix du mode*/
			System.out.println("Veuillez entrer le mode:");
			BufferedReader consoleIn= new BufferedReader(new InputStreamReader(System.in));
			String mode= consoleIn.readLine();
			
			buffOut.writeObject(mode);
			buffOut.flush();
			
			System.out.println("Veuillez entrer le chemin:");
			String chemin= consoleIn.readLine();
			
			
			
			racine = new File(chemin);
			racine.mkdirs();
			
			try {
				envoi(racine, buffOut, buffIn);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	
	
	public static void envoi(File f, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException 
	{
		int compteurcourrant = compteur++;
		
		String message;
		String data = new String();
		BufferedReader br;

	
		File[] list = f.listFiles();
		
		if(list.length>0) 
		{
			for(int i=0; i<list.length; i++) 
			{
				if(list[i].isDirectory())
				{
					out.writeObject(list[i].getAbsolutePath() + "  dir  " + list[i].lastModified());
					
					
					envoi(list[i], out,in);
				}
				else 
				{
					
					out.writeObject(list[i].getAbsolutePath() + "  file  " + list[i].lastModified());
					
					
					message = in.readObject().toString();
					
					if(message.equals("PASOK"))
					{	
						BufferedReader fis = new BufferedReader(new FileReader(list[i]));
						
						while((data=fis.readLine())!=null)
						{
							out.writeObject(data);
						}
						
						data = "null";
						out.writeObject(data);
						fis.close();
					}
				}			
			}
		}
		
		System.out.println("fin de l'envoi " + compteurcourrant);
		if(compteurcourrant!=1) 
		{
			out.writeObject("null");
			
			
		}
		else 
		{
			System.out.println("\n\n\nfinRacine "+compteurcourrant);
			out.writeObject("finRacine"+compteurcourrant);
			in.close();
			out.close();
		}
	}
}