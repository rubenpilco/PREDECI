/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package master.presentacion.beans;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import master.logica.clases.*;
import master.logica.funciones.*;
import org.primefaces.context.DefaultRequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author DiegoPalacios
 */
@ManagedBean
@ViewScoped
public class CuentaUsuarioControlador {

    private ArrayList<UsuarioPersona> usuariosPersonas;
    private UsuarioPersona seleccionarUsuarioPersona;
    private ArrayList<Usuario> usuarios;
    private String criterioBusquedaTitular;
    private ArrayList<Persona> personas;
    private LazyDataModel<Persona> lazyModel;
    private Persona selectedPersona;
    private UsuarioPersona usuarioPersona;
    private UsuarioPersona usuarioPersonaEdit;

    public UsuarioPersona getUsuarioPersonaEdit() {
        return usuarioPersonaEdit;
    }

    public void setUsuarioPersonaEdit(UsuarioPersona usuarioPersonaEdit) {
        this.usuarioPersonaEdit = usuarioPersonaEdit;
    }

    public UsuarioPersona getUsuarioPersona() {
        return usuarioPersona;
    }

    public void setUsuarioPersona(UsuarioPersona usuarioPersona) {
        this.usuarioPersona = usuarioPersona;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    private Persona persona;

    public String getDatoBusqueda() {
        return datoBusqueda;
    }

    public Persona getSelectedPersona() {
        return selectedPersona;
    }

    public void setSelectedPersona(Persona selectedPersona) {
        this.selectedPersona = selectedPersona;
    }

    public LazyDataModel<Persona> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Persona> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public ArrayList<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(ArrayList<Persona> personas) {
        this.personas = personas;
    }

    public void setDatoBusqueda(String datoBusqueda) {
        this.datoBusqueda = datoBusqueda;
    }
    private String datoBusqueda;

    public String getCriterioBusquedaTitular() {
        return criterioBusquedaTitular;
    }

    public void setCriterioBusquedaTitular(String criterioBusquedaTitular) {
        this.criterioBusquedaTitular = criterioBusquedaTitular;
    }

    public long getValorUsuarioSeleccionado() {
        return valorUsuarioSeleccionado;
    }

    public void setValorUsuarioSeleccionado(long valorUsuarioSeleccionado) {
        this.valorUsuarioSeleccionado = valorUsuarioSeleccionado;
    }
    private long valorUsuarioSeleccionado;

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public UsuarioPersona getSeleccionarUsuarioPersona() {
        return seleccionarUsuarioPersona;
    }

    public void setSeleccionarUsuarioPersona(UsuarioPersona seleccionarUsuarioPersona) {
        this.seleccionarUsuarioPersona = seleccionarUsuarioPersona;
    }

    public CuentaUsuarioControlador() {
        usuariosPersonas = new ArrayList<UsuarioPersona>();
        usuarios = new ArrayList<Usuario>();
        seleccionarUsuarioPersona = new UsuarioPersona();
        personas = new ArrayList<Persona>();
        selectedPersona = new Persona();
        persona = new Persona();
        usuarioPersona = new UsuarioPersona();
        usuarioPersonaEdit = new UsuarioPersona();
        cargarUsuariosPersonas();
        cargarUsuarios();
    }

    public ArrayList<UsuarioPersona> getUsuariosPersonas() {
        return usuariosPersonas;
    }

    public void setUsuariosPersonas(ArrayList<UsuarioPersona> usuariosPersonas) {
        this.usuariosPersonas = usuariosPersonas;
    }

    public void cargarUsuariosPersonas() {
        try {
            usuariosPersonas = FUsuarioPersona.obtenerUsuarioPersonas();
            usuarioPersonaEdit = usuariosPersonas.get(0);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void cargarUsuarios() {
        try {
            usuarios = FUsuario.ObtenerUsuarios();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void obtenerPersonas() {
        try {
            if (criterioBusquedaTitular.equals("cedula")) {
                personas = FPersona.ObtenerPersonaDadoCedula(datoBusqueda);
            } else {
                personas = FPersona.ObtenerPersonaDadoApellido(
                        datoBusqueda.toUpperCase());
            }
 //           lazyModel = new FLazyPersonasDataModel(personas);
        } catch (Exception e) {
        }
    }

    public void asignarSelectedPersona() {
        try {
            persona = FPersona.ObtenerPersonaDadoCodigo(selectedPersona.getCodigo_persona());
            System.out.print(persona.getCedula());
        } catch (java.lang.Exception e) {

            System.out.print(persona.getApellido() + " " + e.getMessage());
        }
        DefaultRequestContext.getCurrentInstance().execute("wdialogoBusquedaPersonas.hide()");
        DefaultRequestContext.getCurrentInstance().execute("wdlgrolAgregar.show()");
    }

    public void asignarSelectedPersonaEditar() {
        try {
            persona = FPersona.ObtenerPersonaDadoCodigo(selectedPersona.getCodigo_persona());
            System.out.print(persona.getCedula());
        } catch (java.lang.Exception e) {

            System.out.print(persona.getApellido() + " " + e.getMessage());
        }
        DefaultRequestContext.getCurrentInstance().execute("wdialogoBusquedaPersonasEditar.hide()");
        DefaultRequestContext.getCurrentInstance().execute("wdlgrolActualizar.show()");
    }

    public void insertarCuentaUsuario() {
        try {
            //UsuarioPersona up = new UsuarioPersona();
            usuarioPersona.setCodigo_titular(persona);
            Usuario usuario = new Usuario();
            usuario.setCodigo(valorUsuarioSeleccionado);
            usuarioPersona.setCodigo_usuario(usuario);
            boolean bandera = FUsuarioPersona.insertar(usuarioPersona);

            if (bandera == true) {
                cargarUsuariosPersonas();
                usuarioPersona = new UsuarioPersona();
                DefaultRequestContext.getCurrentInstance().execute("wdlgrolAgregar.hide()");
            } else {
            }

        } catch (Exception e) {
        }
    }

    public void actualizarCuentaUsuario() {
        try {
            //UsuarioPersona up = new UsuarioPersona();
            if (persona.getCedula() == null) {
                usuarioPersona.setCodigo_titular(usuarioPersonaEdit.getCodigo_titular());
            } else {
                usuarioPersona.setCodigo_titular(persona);
            }


            Usuario usuario = new Usuario();
            usuario.setCodigo(usuarioPersonaEdit.getCodigo_usuario().getCodigo());
            usuarioPersona.setCodigo_usuario(usuario);
            usuarioPersona.setCodigo(usuarioPersonaEdit.getCodigo());
            usuarioPersona.setEstado(usuarioPersonaEdit.getEstado());
            boolean bandera = FUsuarioPersona.actualizar(usuarioPersona);
            if (bandera == true) {
                cargarUsuariosPersonas();
                usuarioPersona = new UsuarioPersona();
                DefaultRequestContext.getCurrentInstance().execute("wdlgrolActualizar.hide()");
            } else {
            }

        } catch (Exception e) {
        }
    }

    public void cargarUsuarioPersona() {
        try {
            usuarioPersonaEdit = FUsuarioPersona.obtenerUsuarioPersonaDadoCodigo(seleccionarUsuarioPersona.getCodigo());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
