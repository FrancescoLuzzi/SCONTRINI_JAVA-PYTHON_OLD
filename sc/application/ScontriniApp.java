package sc.application;


import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sc.controller.MyController;
import sc.persistance.MyRepository;
import javafx.scene.Scene;


public class ScontriniApp extends Application {
	@Override
	public void start(Stage stage) {
		try {
			stage.setTitle("Scontrini");
			MyRepository repo=new MyRepository();
			MyController contr=new MyController(repo);
			MainPane pane=new MainPane(contr);
			Scene scene = new Scene(pane,600,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					
					int c = JOptionPane.showConfirmDialog(null,"Vuoi davvero uscire?","Uscita",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);    
					
					if(c==0){ pane.save();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						System.out.print("Lasciami dormire");
					}
					}
					
					else {
						e.consume();
						}
					
				}
			});
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.setProperty("java.locale.providers", "JRE");
		launch(args);
	}
}
