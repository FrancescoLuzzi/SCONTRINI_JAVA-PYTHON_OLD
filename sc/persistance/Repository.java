package sc.persistance;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sc.model.*;

public interface Repository {
public Set<Tipo> getTipoScontrini();
public List<Scontrino> getScontrini(Tipo tipo);
public void add(Scontrino Scontr) throws IOException;
public Map<Tipo,List<Scontrino>> getMappa();
public void remove(Scontrino sk) throws IOException;
public void save() throws IOException;
}
