package utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class IOUtils {
	public static void main(String[] args) {
		
	}
	
	public static void writeObject(String path, Serializable obj) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));
			objectOutputStream.writeObject(obj);
			objectOutputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Serializable readObject(String path) {
		Serializable obj = null;
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
			obj = (Serializable) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}
	
	public static void writeUnicode(String path, String str){
		try {
			FileWriter fileWriter = new FileWriter(path);
			fileWriter.write(str);
			fileWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String readUnicode(String path){
		StringBuilder result = new StringBuilder(4096);
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			String tmp = null;
			while((tmp = bufferedReader.readLine())!=null){
				result.append(tmp+"\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result.toString();
	}
}
