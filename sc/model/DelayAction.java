package sc.model;

import javafx.event.ActionEvent;

public class DelayAction extends ActionEvent{
/**
 * gang gang arrivo dopo e non chiedo scusa sksk
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tipo;
public DelayAction() {
	super();
	this.setTipo("");
}

public DelayAction(String stringa) {
	super();
	this.setTipo(stringa);
}

public String getTipo() {
	return tipo;
}

public void setTipo(String tipo) {
	this.tipo = tipo;
}

}
