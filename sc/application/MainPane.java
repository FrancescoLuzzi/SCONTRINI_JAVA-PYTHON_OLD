package sc.application;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sc.controller.Controller;
import sc.model.*;



public class MainPane extends BorderPane{
	private static int AnnoStart=2018;
	private Controller contr;
	private ComboBox<Integer> anni;
	private ComboBox<Mesi> mesi;
	private TextField luogo;
	private TextField value;
	private TextArea text;
	private Button stmpAnn;
	private Button stmpMens;
	private ComboBox<Tipo> tipi;
	private Button add;
	private Button remove;
	private Button totaleAnn;
	private Button totaleMens;
	private Button meteo;
	private Button markets;
	private List<Tipo> Tipi;
	
	public MainPane(Controller contr) {
		this.contr = contr;
		init();
	}

	private void init() {
		VBox LEFT=new VBox();
		VBox LEFTT=new VBox();
		VBox LEFTB=new VBox();
		VBox top=new VBox();
		HBox bot=new HBox();
		HBox top2=new HBox();
		top2.setSpacing(10);
		this.setTop(top);
		this.setBottom(bot);
		top.setPadding(new Insets(0, 15, 10, 15));
		top.setAlignment(Pos.BASELINE_RIGHT);
		top2.setAlignment(Pos.BASELINE_RIGHT);
		bot.setAlignment(Pos.BASELINE_RIGHT);
		bot.setPadding(new Insets(10,15,10,15));
		bot.setAlignment(Pos.BASELINE_RIGHT);
		bot.setSpacing(20);
		LEFT.setPadding(new Insets(0,30,0,0));
		LEFT.getChildren().addAll(LEFTT,LEFTB);
		LEFTT.setAlignment(Pos.BASELINE_LEFT);
		LEFTB.setAlignment(Pos.CENTER_LEFT);
		LEFTT.setSpacing(15);
		LEFTB.setSpacing(15);
		VBox.setMargin(LEFTB, new Insets(100,0,0,0));
		this.setPadding(new Insets(10,30,10,30));
		top.setSpacing(10);
		this.Tipi=Arrays.asList(Tipo.values())
				.stream()
				.sorted((x,y)->{
					String x1=x.toString();
					String y1=y.toString();
					return x1.compareTo(y1);
				} ).collect(Collectors.toList());
		
		Label l1= new Label("Mesi e anno:");
		{//mesi e anni definiti
		mesi=new ComboBox<Mesi>();
		mesi.setItems(FXCollections.observableArrayList(Mesi.values()));
		mesi.setEditable(false);
		mesi.setValue(Mesi.values()[LocalDate.now().getMonthValue()-1]);
		anni=new ComboBox<Integer>();
		int annoNow=LocalDate.now().getYear();
		List<Integer> annis=new ArrayList<Integer>();
		for(int i=AnnoStart;i<=annoNow;i++) {
			annis.add(i);
		}
		anni.setItems(FXCollections.observableArrayList(annis));
		anni.setValue(annoNow);
		}
		
		
		top2.getChildren().addAll(mesi,anni);
		top.getChildren().addAll(l1,top2);
		text=new TextArea("Scontrini");
		text.setEditable(false);
		text.setPrefColumnCount(20);

		totaleAnn=new Button("Totale Annuale");
		totaleMens=new Button("Totale mensile");
		totaleAnn.setOnAction(this::EventHandlerTotale);
		totaleMens.setOnAction(this::EventHandlerTotale);
		bot.getChildren().addAll(totaleAnn,totaleMens);
		
		stmpAnn=new Button("Stampa annuale");
		stmpAnn.setOnAction(this::EventHandlerDisplay);
		stmpMens=new Button("Stampa Mensile");
		stmpMens.setOnAction(this::EventHandlerDisplay);
		bot.getChildren().add(stmpAnn);
		bot.getChildren().add(stmpMens);
		
		
		this.setCenter(text);
		
		
		Label l3=new Label("Tipo:");
		tipi=new ComboBox<Tipo>();
		tipiSetter(this.Tipi);
		//tipi.setValue(Tipi.get(0));
		tipi.setOnKeyPressed(this::EventHandlerSorter);
		
		Label l4=new Label("Importo:");
		Label l5=new Label("Cerca città:");
		luogo=new TextField("");
		luogo.setOnKeyPressed(this::EventHandlerKey);
		
		markets=new Button("Supermercati");
		
		markets.setOnAction((x)->{try {
			Desktop.getDesktop().browse(new URI("https://www.google.com/maps/search/supermarket/la+mia+posizione"));
		} catch (IOException | URISyntaxException e) {
			JOptionPane.showMessageDialog(null,
				    "Link non trovato.",
				    "Errore apertura pagina web.",
				    JOptionPane.PLAIN_MESSAGE);
		}
		});
		
		value=new TextField("");
		value.setOnKeyPressed(this::EventHandlerKey);
		tipi.getSelectionModel().selectedItemProperty().addListener(
				(c,o,n)->{
						  //this.value.clear();
						  tipiSetter(this.Tipi);
						  this.tipi.autosize();
						  this.tipi.requestFocus();
						  });

		meteo=new Button("Meteo");
		meteo.setOnAction(this::EventHandlerMeteo);
		add=new Button("aggiungi");
		remove=new Button("rimuovi");
		add.setOnAction(this::EventHandlerAdd);
		remove.setOnAction(this::EventHandlerRemove);
		
		LEFTT.getChildren().addAll(l3,tipi,l4,value,add,remove);
		LEFTB.getChildren().addAll(l5,luogo,meteo,markets);
		this.setLeft(LEFT);
		

		
	}
	
	private void EventHandlerMeteo(ActionEvent e) {
		try {
			String place=luogo.getText().trim();
			if(place.equalsIgnoreCase("cento")) {
			Desktop.getDesktop().browse(new URI("https://www.ilmeteo.it/meteo/Cento"));
			
			}
			else if(place.contains("lido") && place.contains("scacchi")) {
				Desktop.getDesktop().browse(new URI("https://www.ilmeteo.it/meteo/Lido+Degli+Scacchi"));
			}
			else if(place.isBlank()) {
				Desktop.getDesktop().browse(new URI("https://www.ilmeteo.it"));
			}
			else {
			Desktop.getDesktop().browse(new  URI("https://www.ilmeteo.it/meteo/cerca?citta="+place));
			}
			} 
		catch (IOException | URISyntaxException e1) {
			JOptionPane.showMessageDialog(null,
				    "Link non trovato.",
				    "Errore apertura pagina web.",
				    JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void EventHandlerRemove(ActionEvent e) {
		try{
		DelayAction obj = new DelayAction();
		contr.remove(this.create());
		this.EventHandlerDisplay(obj);
		}
		catch(IOException e1) {Controller.alert("errore durante rimozione");} 
		catch (ParseException e2) {Controller.alert("errore durante copiatura");}
		catch(NumberFormatException e3) {Controller.alert("errore durante copiatura");}
	}
	private void EventHandlerAdd(ActionEvent e) {
		try{
		DelayAction obj = new DelayAction();
		contr.add(this.create());
		this.EventHandlerDisplay(obj);
		}
		catch(IOException e1) {Controller.alert("aggiunta non avvenuta");}
		catch (ParseException e2) {Controller.alert("errore durante copiatura");}
		catch(NumberFormatException e3) {Controller.alert("errore durante copiatura");}

		value.setText("");
		
		
	}
	
	private void EventHandlerDisplay(ActionEvent e) {
		LocalDate data=  LocalDate.of(this.anni.getValue(),this.mesi.getValue().ordinal()+1,LocalDate.now().getDayOfMonth());
		Map<Tipo,List<Scontrino>> scontr=new HashMap<Tipo, List<Scontrino>>();
		int giusto=1;
		if(e.getSource()==stmpAnn) {
			try {
			scontr=contr.getScontriniAnno(data);
			}
			catch(Error e1) {
				JOptionPane.showMessageDialog(null,
					    "Nessun scontrino salvato.",
					    "Nessun scontrino",
					    JOptionPane.PLAIN_MESSAGE);
				text.setText("Scontrini");
				giusto=0;
			}
		}
		else {
			try {
			scontr=contr.getScontriniMese(data);
			}
			catch(Error e2) {
				JOptionPane.showMessageDialog(null,
					    "Nessun scontrino salvato.",
					    "Nessun scontrino",
					    JOptionPane.PLAIN_MESSAGE);
				text.setText("Scontrini");
				giusto=0;
			}
		}
		if(giusto==1) {
		float totale=0,parz=0;
		Map<Tipo,Float> temp=new HashMap<Tipo,Float>();
		StringBuilder out=new StringBuilder("");
		for(Tipo t: scontr.keySet()) {
			parz=0;
			out.append(t.toString().toUpperCase()+": "+System.lineSeparator());
			for(Scontrino s: scontr.get(t)) {
				parz+=s.getPrezzo();
				totale+=s.getPrezzo();
				out.append(s.toString()+System.lineSeparator());
			}
			temp.put(t, parz);
		}
		if(e.getClass().equals(new ActionEvent().getClass())) {
		ChartPane pane= new ChartPane(temp);
		pane.getStylesheets().add(getClass().getResource("chartPane.css").toExternalForm());
		this.show(pane);
		}
		out.append("\n--------------------------------------\nTotale pagato: "+String.format("%.2f", totale));
		text.setText(out.toString());
		}
	}
	
	private void EventHandlerTotale(ActionEvent e) {
		LocalDate data=  LocalDate.of(this.anni.getValue(),this.mesi.getValue().ordinal()+1,LocalDate.now().withMonth(this.mesi.getValue().ordinal()+1).getDayOfMonth());
		Map<Tipo,List<Scontrino>> scontr=new HashMap<Tipo, List<Scontrino>>();
		int giusto=1;
		if(this.tipi.getValue()!=null) {
		if(e.getSource()==totaleAnn && tipi.getValue().toString()!="") {
			try {
			scontr=contr.getTotaleAnno(data,tipi.getValue());
			}
			catch(Error e1) {
				JOptionPane.showMessageDialog(null,
					    "Nessun scontrino salvato.",
					    "Nessun scontrino",
					    JOptionPane.PLAIN_MESSAGE);
				text.setText("Scontrini");
				giusto=0;
			}
		}
		else {
			try {
			scontr=contr.getTotaleMese(data,tipi.getValue());
			}
			catch(Error e2) {
				JOptionPane.showMessageDialog(null,
					    "Nessun scontrino salvato.",
					    "Nessun scontrino",
					    JOptionPane.PLAIN_MESSAGE);
				text.setText("Scontrini");
				giusto=0;
			}
			}
		}
		else {
			JOptionPane.showMessageDialog(null,
				    "Nessun Tipo selezionato.",
				    "Nessun Tipo",
				    JOptionPane.PLAIN_MESSAGE);
			text.setText("Scontrini");
			giusto=0;
		}
		if(giusto==1) {
		StringBuilder out=new StringBuilder("");
		float totale=0;
		for(Tipo t: scontr.keySet()) {
			out.append(t.toString().toUpperCase()+": "+System.lineSeparator());
			for(Scontrino s: scontr.get(t)) {
				out.append(s.toString()+System.lineSeparator());
				totale+=s.getPrezzo();
			}
		}
		text.setText(out.toString());
		text.appendText("\n\n--------------------------------------\nTotale pagato per la tipologia "+tipi.getValue().toString()+" e': "+String.format("%.2f", totale));
		}
	}
	
	private void EventHandlerKey(KeyEvent k) {
		KeyCode cod=k.getCode();
		//if(k.getSource()==value && k.getCode().isLetterKey()) {value.selectBackward();value.deleteNextChar();}
		if(cod==KeyCode.ENTER) {
			if(k.getSource()==value) {
			this.EventHandlerAdd(new DelayAction());
			}
			else if(k.getSource()==luogo) {
			this.EventHandlerMeteo(new ActionEvent());
			}
		}
	}
	
	private void tipiSetter(List<Tipo> changer) {
		tipi.setItems(FXCollections.observableArrayList(changer));
	}
	
	private void EventHandlerSorter(KeyEvent k) {
		String find=k.getText();
		List<Tipo> out=this.Tipi.stream().filter((x)->x.toString().toLowerCase().startsWith(find)).collect(Collectors.toList());
		if(out.size()>0) {
			tipiSetter(out);
		}
		
	}
	
	
	private Scontrino create() throws ParseException,NumberFormatException{
		LocalDate data;
		if(	LocalDate.now().getMonth().ordinal()!=this.mesi.getValue().ordinal() 
				|| LocalDate.now().getYear()!=this.anni.getValue()) 
		{
		data=LocalDate.of(this.anni.getValue(),	this.mesi.getValue().ordinal()+1,	1);
		}
		else 
		{
		data=LocalDate.now();
		}
		Tipo tipo=this.tipi.getValue();
		String v="";
		v=value.getText().trim().replace("\n", "");
		if(v==null||v.isBlank())throw new ParseException("errore copiatura", 0);
		float f=0;
		DecimalFormat form;
		if(v.contains(".") && v.lastIndexOf(".")>0) {
			String start=v.substring(0, v.lastIndexOf("."));
			String end=v.substring(v.lastIndexOf(".")+1);
			v=start+","+end;}
		if(v.contains(",")) {form=new DecimalFormat("#,##0.##");f= form.parse(v).floatValue();}
		else {f=(float)Integer.parseInt(v);}
		//if(f<=0)throw new ParseException("errore copiatura", 0); if you want only positive values
		return new Scontrino(tipo,f,data);
	}
	
	public void save() {
			try {
				contr.save();
			} catch (IOException e) {
				Controller.alert("salvataggio errato");
			}

	}
	
	//private<T extends Pane> void show(T scene) {
	private void show(Group group) {
		Stage stage=new Stage();
		stage.setTitle("Grafico delle uscite");
		Scene scene=new Scene(group,500,400, Color.LIGHTCYAN);
		stage.setResizable(false);;
		stage.setScene(scene);
		stage.show();
	}
	
	
}