package sc.persistance;

import java.io.*;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import sc.model.*;

public class MyRepository implements Repository {
private Map<Tipo,List<Scontrino>> mappa;
private Reader reader;
private final String nomeDoc="Scontrini.txt";
private DateTimeFormatter formatter= DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.ITALY);
private DecimalFormat nFormatter=new DecimalFormat("€ #,##0.##;€ -#,##0.##");
private String JsonSaver="/path/python/backupper";
	@Override
	public Set<Tipo> getTipoScontrini() {
		return this.mappa.keySet();
	}

	@Override
	public List<Scontrino> getScontrini(Tipo tipo) {
		return this.mappa.get(tipo);
	}
	
	@Override
	public Map<Tipo,List<Scontrino>> getMappa(){
		return this.mappa;
	}

	@Override
	public void add(Scontrino scontr) throws IOException {
		this.addToMap(scontr);
	}
	
	@Override
	public void remove(Scontrino scontr) throws IOException {
		List<Scontrino>cambia =this.mappa.get(scontr.getTipo());
		if(cambia==null || cambia.size()==0)throw new IOException();
		removeLast(scontr,cambia);
		this.mappa.replace(scontr.getTipo(), cambia);
	}
	
	
	public void addToMap(Scontrino scontr) {
		List<Scontrino>stm;
		if((stm=this.mappa.get(scontr.getTipo()))!=null) {
			stm=this.mappa.get(scontr.getTipo());
			stm.add(scontr);
			this.mappa.replace(scontr.getTipo(),stm);
		}
		else {
			stm= new ArrayList<Scontrino>();
			stm.add(scontr);
			this.mappa.put(scontr.getTipo(),stm);
		}
	}
	
	private void leggiScontrini() throws IOException,Error {
	this.mappa=new HashMap<Tipo,List<Scontrino>>();
	BufferedReader rd= new BufferedReader(this.reader);
	Optional<Scontrino> scontr=readScontrino(rd);
	while(scontr.isPresent()) {
		addToMap(scontr.get());
		scontr=readScontrino(rd);
	}
	this.reader.close();
}

private Optional<Scontrino> readScontrino(BufferedReader rd) throws IOException,Error {
	String line=rd.readLine();
	if(line==null) return Optional.empty();
	String[] parti=line.split("\\|");
	if(parti.length!=3)throw new Error("formato scontrino errato");
	String tipo=parti[0].trim();
	List<String> tipi=Arrays.asList(Tipo.values()).stream().map((x)->x.toString()).collect(Collectors.toList());
	if(!tipi.contains(tipo))throw new Error("Tipo non presente");
	Tipo type=Tipo.valueOf(tipo);
	String sordi=parti[1].trim();
	float numero;
	try {
	numero= this.nFormatter.parse(sordi).floatValue();
	}catch(ParseException e) {throw new Error("Parse error soldi");}
	LocalDate data;
	try {
	data=this.formatter.parse(parti[2].trim(), LocalDate::from);
	}catch(DateTimeParseException e1) {
		throw new Error("Data Error");
	}
	Scontrino out=new Scontrino(type,numero,data);
	return Optional.of(out);
}

public MyRepository() throws IOException,Error {
	Reader reader = new FileReader(nomeDoc);
	this.reader = reader;
	this.leggiScontrini();
}

public void save()throws IOException {
	if(!this.mappa.isEmpty()) {
	PrintWriter wr=new PrintWriter(new FileWriter(nomeDoc));
		for(Tipo t: this.mappa.keySet()) {
			for(Scontrino s: this.mappa.get(t)) {
				this.saveOne(s,wr);
			}
		}
	wr.close();
	}
		if(LocalDate.now().getDayOfWeek().ordinal()%2==0 && !this.mappa.isEmpty()) {
		SaveLauncher xd=new SaveLauncher(this.JsonSaver);
		try {
			xd.join();
			xd.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
}
	
	private void saveOne(Scontrino scontr,PrintWriter wr) throws IOException {
		wr.append(scontr.toString());
		wr.flush();
	}
	
	private static void removeLast(Scontrino s,List<Scontrino> lista) throws IOException {
		int index= lista.lastIndexOf(s);
		if(index<0)throw new IOException();
		lista.remove(index);
	}

}
