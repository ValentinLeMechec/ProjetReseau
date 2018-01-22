import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Serveur 
{
	static Semaphore s;
	static File racine;
	
	public static void main(String[] args) 
	{
		try
		{
			ServerSocket socket;
			if(args.length>=1)  
			{
				socket = new ServerSocket(Integer.valueOf(args[0]));
				if(args.length==2) 
				{
					racine = new File(args[1] + "\\racine");
					racine.mkdirs();
				}
				else 
				{
					racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
					racine.mkdirs();
				}
			}
			else
			{
				socket = new ServerSocket(56565);
				System.out.println(socket.getLocalPort());
				racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				racine.mkdirs();
			}			
			
			while(true)
			{
				Socket client = socket.accept();
				System.out.println("Nouveau Client");
				Thread t = new Thread(new ServeurService(client,s,racine));
				t.start();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}

}
