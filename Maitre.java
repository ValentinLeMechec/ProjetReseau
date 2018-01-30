import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Maitre extends Esclave
{
	static int compteur = 1;
	
	public static void main(String[] args) 
	{
		File racine;
		
		try
		{
			Socket socket = new Socket(InetAddress.getByName("172.18.50.20"), 56565);
			OutputStream buffOut= socket.getOutputStream();
			InputStream buffIn= socket.getInputStream();
			
			/*envoi au serveur du choix du mode*/
			System.out.println("Veuillez entrer le mode:");
			BufferedReader consoleIn= new BufferedReader(new InputStreamReader(System.in));
			String mode= consoleIn.readLine();
			
			buffOut.write(mode.getBytes());
			buffOut.flush();
			
			/*if(args.length>=1)  
			{
				if(args.length==2) 
				{
					file = new File(args[1] + "\\racine");
					file.mkdirs();
				}
				else 
				{
					file = new File("H:\\Mes documents\\ProgReseauProjet\\racine"); 
					file.mkdirs();

				}
			}
			else
			{
				file = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				file.mkdirs();

			}*/
			
			racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
			racine.mkdirs();
			
			envoi(racine, buffOut, buffIn);
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	
	
	public static void envoi(File f, OutputStream out, InputStream in) throws IOException 
	{
		int compteurcourrant = compteur++;
		
		String message;
		byte[] data = new byte[1024];
		BufferedReader br;

		File[] list = f.listFiles();
		
		if(list.length>0) 
		{
			for(int i=0; i<list.length; i++) 
			{
				if(list[i].isDirectory())
				{
					message = list[i].getAbsolutePath() + "  dir  " + list[i].lastModified();
					out.write(message.getBytes());
					out.flush();
					
					envoi(list[i], out,in);
				}
				else 
				{
					br= new BufferedReader(new FileReader(list[i]));
					
					message = list[i].getAbsolutePath() + "  file  " + list[i].lastModified();
					out.write(message.getBytes());
					out.flush();
					
					if(in.available()!=0)in.read(data);
					message = "";
					for (byte b : data)
					{
						message += (char)b;
					}
					System.out.println(message);
					
					if(message.equals("PASOK"))
					{	
						while((message=br.readLine())!=null) 
						{
							out.write(message.getBytes());
							out.flush();
						}
						
						message = "null";
						out.write(message.getBytes());
						out.flush();
					}
					br.close();
				}			
			}
		}
		System.out.println("fin de l'envoi " + compteurcourrant);
		if(compteurcourrant!=1) 
		{
			message = "null";
			out.write(message.getBytes());
			out.flush();
			
		}
		else 
		{
			System.out.println("finRacine"+compteurcourrant);
			message = "finRacine"+compteurcourrant;
			out.write(message.getBytes());
			out.flush();
			in.close();
			out.close();
		}
	}
}