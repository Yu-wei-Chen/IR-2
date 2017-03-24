import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import Classes.Path;

public class test_preprocessed {
	int i;
    Map<String, String> result;
	@Test
	public void test() throws IOException {

		FileInputStream fis = null;
	    BufferedReader reader = null; 
		
		
		fis = new FileInputStream("data//indexweb//index");
        reader = new BufferedReader(new InputStreamReader(fis));
        String line;
        while ((line = reader.readLine()) != null){
        	System.out.println(line);
        	//i++;
        	//if(i==1000) break;
        	//System.out.println("---");
        }
        
        
		
	}
}