import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class test {

	public static void main(String[] args) {
		File f = new File("H:\\Mes documents\\ProgReseauProjet\\test");
		f.delete();
		
		
		f.mkdirs();

		System.out.println(f.getAbsolutePath() + "\n" + f.lastModified());
		f.setLastModified(f.lastModified()-1);
		File f1=new File(f.getAbsolutePath()+"\\"+"test3.txt");
		try {
			f1.createNewFile();
			
			PrintWriter fos = new PrintWriter(new FileOutputStream(f1));
		
			fos.print("hallo");
		
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File[] list = f.listFiles();
		for (File test:list)
			System.out.println(test.getName());
		Long lm;
		String message = "test cetruc " +f.lastModified();
		String testt = message.split(" ")[1];
		String derniereModif = message.split(" ")[2];
		
		lm = Long.valueOf(derniereModif);
		
		System.out.println(testt+"\n"+lm);
		
	}

}
