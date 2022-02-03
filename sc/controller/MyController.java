package sc.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import sc.model.Scontrino;
import sc.model.Tipo;
import sc.persistance.Repository;

public class MyController implements Controller {
private Repository repo;


@Override
public Map<Tipo, List<Scontrino>> getScontriniAnno(LocalDate data)throws Error {
	Map<Tipo,List<Scontrino>>mappa=repo.getMappa();
	Set<Tipo> tipi=mappa.keySet();
	List<Scontrino>lista,controlled;
	Map<Tipo,List<Scontrino>>out=new HashMap<Tipo,List<Scontrino>>();
	for(Tipo t:tipi) {
		lista=mappa.get(t);
		controlled=filter(lista, (x)-> x.getData().getYear()==data.getYear());
		if(controlled.size()!=0) {
		out.put(t, controlled);
		}
	}
	if(out.isEmpty())throw new Error("dati non trovati");
	
	return out;
}

@Override
public Map<Tipo, List<Scontrino>> getScontriniMese(LocalDate data) throws Error{
	Map<Tipo,List<Scontrino>>mappa=repo.getMappa();
	Set<Tipo> tipi=mappa.keySet();
	List<Scontrino>lista,controlled;
	Map<Tipo,List<Scontrino>>out=new HashMap<Tipo,List<Scontrino>>();
	for(Tipo t:tipi) {
		lista=mappa.get(t);
		controlled=filter(lista, (x)->x.getData().getMonth()==data.getMonth() &&
				  	      x.getData().getYear()==data.getYear());
		if(controlled.size()!=0) {
		out.put(t, controlled);
		}
	}
	if(out.isEmpty())throw new Error("dati non trovati");
	return out;
}

private static List<Scontrino> filter(List<Scontrino> lista,Predicate<Scontrino> filterCondition) {
	List<Scontrino> out=lista.stream().filter(filterCondition).collect(Collectors.toList());
	return out;
}

public MyController(Repository repo) {
	this.repo = repo;
}

@Override
public void add(Scontrino Scontr) throws IOException {
	this.repo.add(Scontr);
	
}

@Override
public void remove(Scontrino sk) throws IOException {
this.repo.remove(sk);
	
}

@Override
public void save() throws IOException {
	repo.save();
	
}

@Override
public Map<Tipo, List<Scontrino>> getTotaleAnno(LocalDate data,Tipo tipo) throws Error {
	Map<Tipo,List<Scontrino>>mappa=repo.getMappa();
	Set<Tipo> tipi=mappa.keySet();
	List<Scontrino>lista,controlled;
	Map<Tipo,List<Scontrino>>out=new HashMap<Tipo,List<Scontrino>>();
	for(Tipo t:tipi) {
		lista=mappa.get(t);
		controlled=filter(lista, (x)->x.getData().getYear()==data.getYear() &&
				  	      x.getTipo().toString().equals(tipo.toString()));
		if(controlled.size()!=0) {
		out.put(t, controlled);
		}
	}
	if(out.isEmpty())throw new Error("dati non trovati");
	return out;
}

@Override
public Map<Tipo, List<Scontrino>> getTotaleMese(LocalDate data,Tipo tipo) throws Error {
	Map<Tipo,List<Scontrino>>mappa=repo.getMappa();
	Set<Tipo> tipi=mappa.keySet();
	List<Scontrino>lista,controlled;
	Map<Tipo,List<Scontrino>>out=new HashMap<Tipo,List<Scontrino>>();
	for(Tipo t:tipi) {
		lista=mappa.get(t);
		controlled=filter(lista, (x)->x.getData().getMonth()==data.getMonth() &&
				  	      x.getData().getYear()==data.getYear() &&
				  	      x.getTipo().toString().equals(tipo.toString()));
		if(controlled.size()!=0) {
		out.put(t, controlled);
		}
	}
	if(out.isEmpty())throw new Error("dati non trovati");
	return out;
}
}
