import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Env {
	public static Properties propMain= new Properties();
	
	public static String GetEnvironment() throws IOException
	{
		FileInputStream fileStream = new FileInputStream(".\\DataAndConfig\\env.properties");
		propMain.load(fileStream);
		String baseuri = propMain.getProperty("baseuri");
		return baseuri;
	}
	


}
