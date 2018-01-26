import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Esclave {

	

	
	public static void main(String[] args) {
		
		
		
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
			
			
			
			
			boolean enCour = true;
			
			String message;
			
			String nom;
			String type;
			String derniereModif;
			
			Long lm;
			
			File f;
			
			System.out.println("avant le switch");
			
			mode = (String) buffIn.readObject();
			System.out.println(mode);
			
			switch (mode)
			{
				case "push -s": 
					
					System.out.println("push -s");
					
					racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
					viderDossier(racine);
					
					racine.mkdirs();
					
					do
					{
						System.out.println("la");
						
						message=(String) buffIn.readObject();
						System.out.println(message);
						
						if(message!=null && message.equals("finRacine1"))
							enCour = false;
						
						else if (message!=null && !message.equals("null"))
						{	
							nom=message.split("  ")[0];
							type=message.split("  ")[1];
							derniereModif = message.split("  ")[2];
							lm=Long.valueOf(derniereModif);
							
							if(type.equals("dir"))
							{
								f = new File(nom);
								f.mkdir();
							}
							else
							{
								f = new File(nom);
								f.createNewFile();
								if(f.lastModified()==lm)
								{
									System.out.println("OK");
									buffOut.writeObject("OK");
									buffOut.flush();
								}
								else
								{
									System.out.println("PASOK");
									buffOut.writeObject("PASOK");
									buffOut.flush();
									
									PrintWriter fos = new PrintWriter(new FileOutputStream(f));
									do
									{
										System.out.println("ici");
										message=(String) buffIn.readObject();
										if(!message.equals("null"))
										{
												fos.println(message);
												fos.flush();
										}
									}while(!message.equals("null"));
									fos.close();
									
									f.setLastModified(lm);
								}
							}
						}
						System.out.println(enCour);
						
					}while(enCour);
					
					break;
					/*------------------FIN DU PUSH SUPPRESSION----------------------*/
					
					
				case "push -e":
					
					System.out.println("push -e");
					
					racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
					racine.mkdirs();
					
					do
					{
						System.out.println("la");
						message=(String) buffIn.readObject();
						System.out.println(message);
						if(message!=null && message.equals("finRacine1"))
							enCour = false;
						else if (message!=null && !message.equals("null"))
						{	
							nom=message.split("  ")[0];
							type=message.split("  ")[1];
							derniereModif = message.split("  ")[2];
							lm=Long.valueOf(derniereModif);
							
							if(type.equals("dir"))
							{
								f = new File(nom);
								f.mkdir();
							}
							else
							{
								f = new File(nom);
								f.createNewFile();
								
								System.out.println("PASOK");
								buffOut.writeObject("PASOK");
								buffOut.flush();

								PrintWriter fos = new PrintWriter(new FileOutputStream(f));
								do
								{
									System.out.println("ici");
									message=(String) buffIn.readObject();
									if(!message.equals("null"))
									{
											fos.println(message);
											fos.flush();
									}
								}while(!message.equals("null"));
								fos.close();
								
								f.setLastModified(lm);
							}
						}
						System.out.println(enCour);
						
					}while(enCour);
					
					break;
					/*------------------FIN DU PUSH ECRASEMENT----------------------*/
					
					
					
				case "push -w":
					
					System.out.println("push -w");
					
					racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
					racine.mkdirs();
					
					do
					{
						System.out.println("la");
						message=(String) buffIn.readObject();
						System.out.println(message);
						if(message!=null && message.equals("finRacine1"))
							enCour = false;
						else if (message!=null && !message.equals("null"))
						{	
							nom=message.split("  ")[0];
							type=message.split("  ")[1];
							derniereModif = message.split("  ")[2];
							lm=Long.valueOf(derniereModif);
							
							if(type.equals("dir"))
							{
								f = new File(nom);
								f.mkdir();
							}
							else
							{
								f = new File(nom);
								f.createNewFile();
								if(f.lastModified()>lm)
								{
									System.out.println("OK");
									buffOut.writeObject("OK");
									buffOut.flush();
								}
								else
								{
									System.out.println("PASOK");
									buffOut.writeObject("PASOK");
									buffOut.flush();

									PrintWriter fos = new PrintWriter(new FileOutputStream(f));
									do
									{
										System.out.println("ici");
										message=(String) buffIn.readObject();
										if(!message.equals("null"))
										{
												fos.println(message);
												fos.flush();
										}
									}while(!message.equals("null"));
									fos.close();
									
									f.setLastModified(lm);
								}
							}
						}
						System.out.println(enCour);
						
					}while(enCour);
					
					break;
					/*------------------FIN DU PUSH WATCHDOG----------------------*/
					
					
					
				case "pull":
					break;
					
				default:
					System.out.println("default");
					break;
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	
	
	public static void viderDossier(File D)
	{
		for (File f: D.listFiles())
		{
			if(f.isDirectory()) viderDossier(f);
			f.delete();
		}
	}
	

}
