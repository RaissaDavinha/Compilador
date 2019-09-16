import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		FileReader fileReader = new FileReader("lexico.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String readContent = reader.readLine();
		List<Token> tokens = new ArrayList<Token>();
		int count = 0;
		char letra;
		
		
		
		while (readContent != null) {
			letra = readContent.charAt(count);
			while((letra == '{') || (letra == ' ') || (readContent != null)) {
				if (letra == '{') {
					while(letra != '}' && readContent != null) {
						count++;
						letra = readContent.charAt(count);
					}
					while((letra == ' ') && readContent != null) {
						count++;
						letra = readContent.charAt(count);
					}
				}
				
			}
			
			//Pega Token
			
			
			readContent = reader.readLine();
			count = 0;
		}
		
		fileReader.close();

	}

}
