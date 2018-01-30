import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
			OutputStream out= socket.getOutputStream();
			InputStream in= socket.getInputStream();
			
			/*envoi au serveur du choix du mode*/
			System.out.println("Veuillez entrer le mode:");
			BufferedReader consoleIn= new BufferedReader(new InputStreamReader(System.in));
			String mode= consoleIn.readLine();
			
			out.write(mode.getBytes());
			out.flush();
			
			boolean enCour = true;
			
			int taille;
			String message;
			
			String nom;
			String type;
			String derniereModif;
			
			Long lm;
			
			File f;
			byte[] data = new byte[1024];
			
			System.out.println("avant le switch");
			
			while(in.available()<=0);
			taille=in.read(data);
			message = "";
			for (int i = 0;i<taille;i++)
			{
				message += (char)data[i];
			}
			
			mode = message;
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
						
						while(in.available()<=0);
						taille=in.read(data);
						message = "";
						for (int i = 0;i<taille;i++)
						{
							message += (char)data[i];
						}
						System.out.println(message);
						
						if(message.equals("finRacine1"))
							enCour = false;
						
						else if (!message.equals("null"))
						{	
							nom=message.split("  ")[0];
							type=message.split("  ")[1];
							derniereModif = message.split("  ")[2];
							lm=Long.valueOf(derniereModif);
							
							if(type.equals("dir"))
							{
								f = new File(nom);
								f.mkdirs();
							}
							else
							{
								f = new File(nom);
								f.createNewFile();
								if(f.lastModified()==lm)
								{
									System.out.println("OK");
									out.write("OK".getBytes());
									out.flush();
								}
								else
								{
									System.out.println("PASOK");
									out.write("PASOK".getBytes());
									out.flush();
									
									FileOutputStream fos= new FileOutputStream(f);
									do
									{
										System.out.println("ici");
										while(in.available()<=0);
										taille=in.read(data);
										message = "";
										for (int i = 0;i<taille;i++)
										{
											message += (char)data[i];
										}
										System.out.println(message);
										
										if(!message.equals("null"))
										{
												fos.write(data,0,taille);
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
						
						while(in.available()<=0);
						taille=in.read(data);
						message = "";
						for (int i = 0;i<taille;i++)
						{
							message += (char)data[i];
						}
						System.out.println(message);
						
						if(message.equals("finRacine1"))
							enCour = false;
						
						else if (!message.equals("null"))
						{	
							nom=message.split("  ")[0];
							type=message.split("  ")[1];
							derniereModif = message.split("  ")[2];
							lm=Long.valueOf(derniereModif);
							
							if(type.equals("dir"))
							{
								f = new File(nom);
								f.mkdirs();
							}
							else
							{
								f = new File(nom);
								f.createNewFile();
								if(f.lastModified()==lm)
								{
									System.out.println("OK");
									out.write("OK".getBytes());
									out.flush();
								}
								else
								{
									System.out.println("PASOK");
									out.write("PASOK".getBytes());
									out.flush();
									
									FileOutputStream fos= new FileOutputStream(f);
									do
									{
										System.out.println("ici");
										while(in.available()<=0);
										taille=in.read(data);
										message = "";
										for (int i = 0;i<taille;i++)
										{
											message += (char)data[i];
										}
										System.out.println(message);
										
										if(!message.equals("null"))
										{
												fos.write(data,0,taille);
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
					/*------------------FIN DU PUSH ECRASEMENT----------------------*/
					
					
					
				case "push -w":
					
					System.out.println("push -e");
					
					racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
					racine.mkdirs();
					
					do
					{
						System.out.println("la");
						
						while(in.available()<=0);
						taille=in.read(data);
						message = "";
						for (int i = 0;i<taille;i++)
						{
							message += (char)data[i];
						}
						System.out.println(message);
						
						if(message.equals("finRacine1"))
							enCour = false;
						
						else if (!message.equals("null"))
						{	
							nom=message.split("  ")[0];
							type=message.split("  ")[1];
							derniereModif = message.split("  ")[2];
							lm=Long.valueOf(derniereModif);
							
							if(type.equals("dir"))
							{
								f = new File(nom);
								f.mkdirs();
							}
							else
							{
								f = new File(nom);
								f.createNewFile();
								if(f.lastModified()>=lm)
								{
									System.out.println("OK");
									out.write("OK".getBytes());
									out.flush();
								}
								else
								{
									System.out.println("PASOK");
									out.write("PASOK".getBytes());
									out.flush();
									
									FileOutputStream fos= new FileOutputStream(f);
									do
									{
										System.out.println("ici");
										while(in.available()<=0);
										taille=in.read(data);
										message = "";
										for (int i = 0;i<taille;i++)
										{
											message += (char)data[i];
										}
										System.out.println(message);
										
										if(!message.equals("null"))
										{
												fos.write(data,0,taille);
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
					
				
				default:
					System.out.println("default");
					break;
			}
			
		} 
		catch (IOException e) 
		{
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
