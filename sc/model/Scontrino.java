package sc.model;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Scontrino implements Comparable<Scontrino>{
private Tipo tipo;
private float prezzo;
private LocalDate data;
private DateTimeFormatter formatter= DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.ITALY);
public Scontrino(Tipo tipo, float prezzo, LocalDate data) {
	this.tipo = tipo;
	this.prezzo = prezzo;
	this.data = data;
}
public Tipo getTipo() {
	return tipo;
}
public float getPrezzo() {
	return prezzo;
}
public LocalDate getData() {
	return data;
}
@Override
public int compareTo(Scontrino o) {
	if(this.tipo.toString().compareTo(o.tipo.toString())!=0)return this.tipo.toString().compareTo(o.tipo.toString());
	if(!this.getData().isEqual(o.getData()))return this.getData().compareTo(o.getData());
	if(this.prezzo<o.prezzo)return -1;
	else if(this.prezzo>o.prezzo)return 1;
	return 0;

}

public String toString() {
	return this.tipo.toString()+ "| € "+String.format("%.2f", this.prezzo)+" | "+formatter.format(data)+System.lineSeparator();
}

@Override
public  boolean equals(Object scontro) {
	if(scontro instanceof Scontrino) {
		
		Scontrino scontr =(Scontrino)scontro;
	if(this.getPrezzo()==scontr.getPrezzo() && 
	this.tipo.toString().contentEquals(scontr.getTipo().toString()) &&
	this.data.getYear()==scontr.getData().getYear()&&
	this.data.getMonth()==scontr.getData().getMonth()
	) {return true;}
	else return false;
	}
	else return false;
}
}
