package sc.model;

import java.io.IOException;

public class SaveLauncher extends Thread {
	private String path;
	public SaveLauncher(String path) {
		super();
		this.path=path;
	}
	
	public void run() {
		String pre="cmd /c python ";
		String post=" SaveLauncher";
		Process saver;
		try {
			saver= Runtime.getRuntime().exec(String.format("%s%s%s", pre,this.path,post));
			int val=saver.waitFor();
			if(val==0) {
				System.out.print("Bella");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
