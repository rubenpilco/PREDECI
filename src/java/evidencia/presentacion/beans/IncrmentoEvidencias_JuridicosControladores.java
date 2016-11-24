/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evidencia.presentacion.beans;

import evidencia.logica.clases.Documento;
import evidencia.logica.clases.Evidencia_Juridicos;
import evidencia.logica.clases.IncrementoEvidencias_Juridicos;
import evidencia.logica.funciones.FEvidencia_Juridicos;
import evidencia.logica.funciones.FIncrementoEvidencias_Juridicos;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import master.logica.clases.UsuarioRol;
import master.presentacion.beans.SesionUsuarioDataManager;
import org.apache.taglibs.standard.functions.Functions;
import org.primefaces.context.DefaultRequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import recursos.Util;

/**
 *
 * @author RUBENCHO
 */
@ManagedBean
@ViewScoped
public class IncrmentoEvidencias_JuridicosControladores {

    private IncrementoEvidencias_Juridicos objInEvJu;
    private ArrayList<IncrementoEvidencias_Juridicos> lstInEvJu;
    private ArrayList<IncrementoEvidencias_Juridicos> filtereInvidencia;
    private IncrementoEvidencias_Juridicos selIncrmento;
    private java.util.Date fechaRegistro;
    @ManagedProperty("#{sesionUsuarioDataManager}")
    private SesionUsuarioDataManager dm;

    //Evidencia Juridicos
    private ArrayList<Evidencia_Juridicos> lstEviJu;
    private ArrayList<Evidencia_Juridicos> filterevidencia;
    private Evidencia_Juridicos evidenciaSel;
    private String keywords;
    private String cedula;
    private Documento ArchivoSel;
    private String tipo;

    //manejo de documentos
    private String nombreIncrementoArchivos;
    private UploadedFile archivoIncrementoArchivos;
    ResourceBundle Configuracion = ResourceBundle.getBundle("recursos.rutasMedia");

    public IncrmentoEvidencias_JuridicosControladores() {
        reinit();
    }

    private void reinit() {
        this.objInEvJu = new IncrementoEvidencias_Juridicos();
        this.lstInEvJu = new ArrayList();
        this.fechaRegistro = new java.util.Date();
        this.evidenciaSel = new Evidencia_Juridicos();
        this.lstEviJu = new ArrayList();
        this.ArchivoSel = new Documento();
        cargarInEvidencias();
        cargarEvidencias();
    }
//<editor-fold defaultstate="collapsed" desc="Llenar Listas de Producto, Servicio y Ubicacion">

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Cargar Incremento de Evidencia Juridicos">
    public void cargarInEvidencias() {
        try {
            this.lstInEvJu = FIncrementoEvidencias_Juridicos.obtenerTodosIncrementoEvidencias_Juridicos();
            this.filterevidencia = lstEviJu;
        } catch (Exception e) {
            Util.addErrorMessage(e, "Error");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cargar Evidencias Juridicos">
    public void cargarEvidencias() {
        try {
            this.lstEviJu = FEvidencia_Juridicos.obtenerEvidencia_Juridicos();
            this.filterevidencia = lstEviJu;
        } catch (Exception e) {
            Util.addErrorMessage(e, "Error");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Insertar Incremento Evidencias Juridicos">
    public void insertarIncrementoEvidecniaJ() {
        try {
            UsuarioRol us = new UsuarioRol();
            us.setCodigo(this.dm.getSesionUsuarioRolActual().getCodigo());
            this.objInEvJu.setCodigo_usuario_rol(us);
            this.objInEvJu.setCodigo_evidencias(evidenciaSel);

            if (FIncrementoEvidencias_Juridicos.insertar(this.objInEvJu) == true) {
                FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Datos Insertados");
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
                DefaultRequestContext.getCurrentInstance().execute("wdlgincrementoE.hide()");
                this.objInEvJu = new IncrementoEvidencias_Juridicos();
            } else {
                FacesMessage mensajeError = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenci�n", "Datos no Insertados");
                FacesContext.getCurrentInstance().addMessage(null, mensajeError);
            }
        } catch (Exception e) {
            FacesMessage mensajeErrorIngreso = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, mensajeErrorIngreso);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cargar Archivo">
    public void cargarIncrementoArchivos(FileUploadEvent e) {
        System.out.println("Entra al m�todo cargar archivos");
        UploadedFile file = e.getFile();
        this.archivoIncrementoArchivos = file;
        System.out.println(file.getContentType());
        System.out.println(file.getSize());
        System.out.println(file.getFileName());
        this.nombreIncrementoArchivos = file.getFileName();
        try {
            byte[] contenido = getFileContents(e.getFile().getInputstream());
            if (guardarIncrementoArchivos(file.getFileName(), contenido)) {
                Util.addSuccessMessage("Archivo Cargado con �xito!!");
            } else {
                Util.addErrorMessage("Error al cargar la Archivo");
            }
        } catch (IOException ex) {
            Logger.getLogger(IncrmentoEvidencias_JuridicosControladores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean guardarIncrementoArchivos(String nombre, byte[] contenido) {
        String rutaArchivosOriginales = this.Configuracion.getString("rutaIncremento");
        int longitudRelativa = Integer.valueOf(this.Configuracion.getString("logitudRelativa")).intValue();
        File f = new File(rutaArchivosOriginales + nombre);
        try {
            System.out.println("PATH: " + f.getAbsolutePath());
            this.objInEvJu.setPath(Functions.substring(f.getAbsolutePath(), longitudRelativa, f.getAbsolutePath().length()));

            System.out.println("cargar objeto fos ");
            FileOutputStream fos = new FileOutputStream(f);
            System.out.println("escribir fos ");
            fos.write(contenido);
            return true;
        } catch (Exception e) {
            Util.mostrarMensaje(e.getMessage());
        }
        return false;
    }

    private byte[] getFileContents(InputStream in) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read = 0;
            bytes = new byte['?'];
            while ((read = in.read(bytes)) != -1) {
                bos.write(bytes, 0, read);
            }
            bytes = bos.toByteArray();
            in.close();
            in = null;
            bos.flush();
            bos.close();
            bos = null;
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return bytes;
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="Set y get">

    public ArrayList<IncrementoEvidencias_Juridicos> getFiltereInvidencia() {
        return filtereInvidencia;
    }

    public void setFiltereInvidencia(ArrayList<IncrementoEvidencias_Juridicos> filtereInvidencia) {
        this.filtereInvidencia = filtereInvidencia;
    }
            
    public IncrementoEvidencias_Juridicos getObjInEvJu() {
        return objInEvJu;
    }

    public void setObjInEvJu(IncrementoEvidencias_Juridicos objInEvJu) {
        this.objInEvJu = objInEvJu;
    }

    public IncrementoEvidencias_Juridicos getObjIncrementoArchivos() {
        return this.objInEvJu;
    }

    public void setObjIncrementoArchivos(IncrementoEvidencias_Juridicos objInEvJu) {
        this.objInEvJu = objInEvJu;
    }

    public ArrayList<IncrementoEvidencias_Juridicos> getLstIncrementoArchivos() {
        return this.lstInEvJu;
    }

    public void setLstIncrementoArchivos(ArrayList<IncrementoEvidencias_Juridicos> lstInEvJu) {
        this.lstInEvJu = lstInEvJu;
    }

    public IncrementoEvidencias_Juridicos getSelIncrmento() {
        return this.selIncrmento;
    }

    public void setSelIncrmento(IncrementoEvidencias_Juridicos selIncrmento) {
        this.selIncrmento = selIncrmento;
    }

    public java.util.Date getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(java.util.Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public SesionUsuarioDataManager getDm() {
        return this.dm;
    }

    public void setDm(SesionUsuarioDataManager dm) {
        this.dm = dm;
    }

    public String getNombreIncrementoArchivos() {
        return this.nombreIncrementoArchivos;
    }

    public void setNombreIncrementoArchivos(String nombreIncrementoArchivos) {
        this.nombreIncrementoArchivos = nombreIncrementoArchivos;
    }

    public UploadedFile getArchivoIncrementoArchivos() {
        return this.archivoIncrementoArchivos;
    }

    public void setArchivoIncrementoArchivos(UploadedFile archivoIncrementoArchivos) {
        this.archivoIncrementoArchivos = archivoIncrementoArchivos;
    }

    public ResourceBundle getConfiguracion() {
        return this.Configuracion;
    }

    public void setConfiguracion(ResourceBundle Configuracion) {
        this.Configuracion = Configuracion;
    }

    public ArrayList<Evidencia_Juridicos> getFilterevidencia() {
        return filterevidencia;
    }

    public void setFilterevidencia(ArrayList<Evidencia_Juridicos> filterevidencia) {
        this.filterevidencia = filterevidencia;
    }

    public Evidencia_Juridicos getEvidenciaSel() {
        return evidenciaSel;
    }

    public void setEvidenciaSel(Evidencia_Juridicos evidenciaSel) {
        this.evidenciaSel = evidenciaSel;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public ArrayList<IncrementoEvidencias_Juridicos> getLstInEvJu() {
        return lstInEvJu;
    }

    public void setLstInEvJu(ArrayList<IncrementoEvidencias_Juridicos> lstInEvJu) {
        this.lstInEvJu = lstInEvJu;
    }

    public ArrayList<Evidencia_Juridicos> getLstEviJu() {
        return lstEviJu;
    }

    public void setLstEviJu(ArrayList<Evidencia_Juridicos> lstEviJu) {
        this.lstEviJu = lstEviJu;
    }

    public String getCedula() {
        return this.cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Documento getArchivoSel() {
        return this.ArchivoSel;
    }

    public void setArchivoSel(Documento ArchivoSel) {
        this.ArchivoSel = ArchivoSel;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
//</editor-fold>

}
