import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServeurService implements Runnable
{
	Socket client;
	Semaphore mutex;
	File racine;
	
	static int compteur = 1;
	
	public ServeurService(Socket s, Semaphore sem, File r)
	{
		client = s;
		mutex = sem;
		racine = r;
	}
	
	public void viderDossier(File D)
	{
		for (File f: D.listFiles())
		{
			if(f.isDirectory()) viderDossier(f);
			f.delete();
		}
	}
	
	@Override
	public void run() 
	{
		try 
		{
			InputStream in = client.getInputStream();
			OutputStream out = client.getOutputStream();
			
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
			
			String mode = message;
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
					
					
				/*case "push -e":
					
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
					
					
					
				/*case "push -w":
					
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
					
					
					
				/*case "pull -s":case "pull -e":case "pull -w":
					if(mode.equals("pull -s"))
					{
						System.out.println("pull -s");
						buffOut.writeObject("push -s");
						buffOut.flush();
					}
					else if(mode.equals("pull -e"))
					{
						System.out.println("pull -e");
						buffOut.writeObject("push -e");
						buffOut.flush();
					}
					else
					{
						System.out.println("pull -w");
						buffOut.writeObject("push -w");
						buffOut.flush();
					}
					
					racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
					racine.mkdirs();
					
					try {
						envoi(racine, buffOut, buffIn);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;*/
					
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
	
	public static void envoi(File f, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException 
	{
		int compteurcourrant = compteur++;
		
		String message;
		String data = new String();
	
		File[] list = f.listFiles();
		
		if(list.length>0) 
		{
			for(int i=0; i<list.length; i++) 
			{
				if(list[i].isDirectory())
				{
					out.writeObject(list[i].getAbsolutePath() + "  dir  " + list[i].lastModified());
					out.flush();
					
					envoi(list[i], out,in);
				}
				else 
				{
					
					out.writeObject(list[i].getAbsolutePath() + "  file  " + list[i].lastModified());
					out.flush();
					
					message = in.readObject().toString();
					
					if(message.equals("PASOK"))
					{	
						BufferedReader fis = new BufferedReader(new FileReader(list[i]));
						
						while((data=fis.readLine())!=null)
						{
							out.writeObject(data);
							out.flush();
						}
						
						data = "null";
						out.writeObject(data);
						out.flush();
						fis.close();
					}
				}			
			}
		}
		
		System.out.println("fin de l'envoi " + compteurcourrant);
		if(compteurcourrant!=1) 
		{
			out.writeObject("null");
			out.flush();
			
		}
		else 
		{
			System.out.println("\n\n\nfinRacine "+compteurcourrant);
			out.writeObject("finRacine"+compteurcourrant);
			out.flush();
			compteur=1;
		}
	}

}
