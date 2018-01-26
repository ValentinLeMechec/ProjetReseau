import java.io.File;
import java.io.FileOutputStream;
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
			
			ObjectInputStream buffIn = new ObjectInputStream(in);
			ObjectOutputStream buffOut = new ObjectOutputStream(out);
			
			boolean enCour = true;
			
			String message;
			
			String nom;
			String type;
			String derniereModif;
			
			Long lm;
			
			File f;
			byte[] data;
			
			System.out.println("avant le switch");
			
			String mode = (String) buffIn.readObject();
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

}
