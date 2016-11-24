/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package master.presentacion.beans;


import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import master.logica.clases.Menu_Sitio;
import master.logica.funciones.FMenu_Sitio;
import org.apache.taglibs.standard.functions.Functions;
import recursos.Util;

/**
 *
 * @author icits
 */
@ManagedBean
@ViewScoped
public class IndexControlador implements Serializable {


    private final static String[] iconos;
    private ArrayList<Menu_Sitio> lstMenuSitio;
    private int codigo_articulo;

    //sesion del sitio
    @ManagedProperty(value = "#{sesionSitioDataManager}")
    private SesionSitioDataManager dmSitio;

    static {
        iconos = new String[3];
        iconos[0] = "*.pdf";
        iconos[1] = "*.word";
        iconos[2] = "*.odt";
    }

    public IndexControlador() {

        this.cargarMenuSitio();

    }

   

    public SesionSitioDataManager getDmSitio() {
        return dmSitio;
    }

    public void setDmSitio(SesionSitioDataManager dmSitio) {
        this.dmSitio = dmSitio;
    }

    public ArrayList<Menu_Sitio> getLstMenuSitio() {
        return lstMenuSitio;
    }

    public void setLstMenuSitio(ArrayList<Menu_Sitio> lstMenuSitio) {
        this.lstMenuSitio = lstMenuSitio;
    }

  

    public void cargarMenuSitio() {
        try {
            lstMenuSitio = FMenu_Sitio.ObtenerMenuSitio();
        } catch (Exception e) {
            Util.addErrorMessage(e, "Error con la conexion");
        }
    }

    public String cambiarPagina() {

        return "/faces" + (Functions.substring(this.dmSitio.getPaginaActual().getUrl(), 2,
                this.dmSitio.getPaginaActual().getUrl().length() - 6));
    }

    /*public int CargarIconos() {
     int tamaño = lstDocumento.size();
     int valor = 0;
     if (lstDocumento.get(valor).equals(iconos[0])) {
     valor = 1;

     }
     if (lstDocumento.get(i).equals(iconos[1])) {
     valor = 2;

     }
     if (lstDocumento.get(i).equals(iconos[2])) {
     valor = 3;
     }
     return valor;
     }*/
}
