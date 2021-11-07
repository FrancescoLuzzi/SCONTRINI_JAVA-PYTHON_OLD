package sc.controller;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sc.model.*;
public interface Controller {
	
public Map<Tipo, List<Scontrino>> getScontriniMese(LocalDate data)throws Error;

public Map<Tipo, List<Scontrino>> getScontriniAnno(LocalDate data)throws Error;

public Map<Tipo,List<Scontrino>> getTotaleAnno(LocalDate data,Tipo tipo)throws Error;

public Map<Tipo,List<Scontrino>> getTotaleMese(LocalDate data,Tipo tipo)throws Error;

public void add(Scontrino Scontr) throws IOException;
public void remove(Scontrino sk) throws IOException;
public void save() throws IOException;

public static void alert(String message) {
	Alert alert = new Alert(AlertType.ERROR);
	alert.setTitle("Errore");
	alert.setHeaderText("Disastro!");
	alert.setContentText(message);
	alert.showAndWait();
}
}
