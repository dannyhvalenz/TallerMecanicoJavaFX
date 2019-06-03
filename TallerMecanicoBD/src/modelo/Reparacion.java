/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dany
 */
@Entity
@Table(name = "Reparacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reparacion.findAll", query = "SELECT r FROM Reparacion r WHERE r.idAutomovil = :idAutomovil")
    , @NamedQuery(name = "Reparacion.findAllOrderById", query = "SELECT r FROM Reparacion r ORDER BY r.id")
    , @NamedQuery(name = "Reparacion.findByTipoLike", query = "SELECT r FROM Reparacion r WHERE r.idAutomovil = :idAutomovil AND r.tipo LIKE CONCAT(:tipo,'%')")
    , @NamedQuery(name = "Reparacion.findById", query = "SELECT r FROM Reparacion r WHERE r.id = :id")
    , @NamedQuery(name = "Reparacion.findByTipo", query = "SELECT r FROM Reparacion r WHERE r.tipo = :tipo")
    , @NamedQuery(name = "Reparacion.findByKilometraje", query = "SELECT r FROM Reparacion r WHERE r.kilometraje = :kilometraje")
    , @NamedQuery(name = "Reparacion.findByFecha", query = "SELECT r FROM Reparacion r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "Reparacion.findByHora", query = "SELECT r FROM Reparacion r WHERE r.hora = :hora")
    , @NamedQuery(name = "Reparacion.findByDescripcionFalla", query = "SELECT r FROM Reparacion r WHERE r.descripcionFalla = :descripcionFalla")
    , @NamedQuery(name = "Reparacion.findByDescripcionMantenimiento", query = "SELECT r FROM Reparacion r WHERE r.descripcionMantenimiento = :descripcionMantenimiento")
    , @NamedQuery(name = "Reparacion.update" , query = "UPDATE Reparacion SET fecha = :fecha, hora = :hora , costo = :costo, tipo = :tipo, kilometraje = :kilometraje, descripcionFalla = :descripcionFalla, descripcionMantenimiento = :descripcionMantenimiento WHERE id = :id")})
public class Reparacion implements Serializable {

    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private java.util.Date fecha;

    @Basic(optional = false)
    @Column(name = "hora")
    @Temporal(TemporalType.DATE)
    private java.util.Date hora;

    @Basic(optional = false)
    @Column(name = "costo")
    private int costo;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "kilometraje")
    private String kilometraje;
    @Basic(optional = false)
    @Column(name = "descripcionFalla")
    private String descripcionFalla;
    @Basic(optional = false)
    @Column(name = "descripcionMantenimiento")
    private String descripcionMantenimiento;
    @JoinColumn(name = "idAutomovil", referencedColumnName = "matricula")
    @ManyToOne(optional = false)
    private Automovil idAutomovil;

    public Reparacion() {
    }

    public Reparacion(Integer id) {
        this.id = id;
    }

    
    
    public Reparacion(Integer id, Date fecha, Date hora, int costo, String tipo, 
            String kilometraje, String descripcionFalla, String descripcionMantenimiento, 
            Automovil idAutomovil) {
        this.fecha = fecha;
        this.hora = hora;
        this.costo = costo;
        this.id = id;
        this.tipo = tipo;
        this.kilometraje = kilometraje;
        this.descripcionFalla = descripcionFalla;
        this.descripcionMantenimiento = descripcionMantenimiento;
        this.idAutomovil = idAutomovil;
    }

   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }


    public String getDescripcionFalla() {
        return descripcionFalla;
    }

    public void setDescripcionFalla(String descripcionFalla) {
        this.descripcionFalla = descripcionFalla;
    }

    public String getDescripcionMantenimiento() {
        return descripcionMantenimiento;
    }

    public void setDescripcionMantenimiento(String descripcionMantenimiento) {
        this.descripcionMantenimiento = descripcionMantenimiento;
    }

    public Automovil getIdAutomovil() {
        return idAutomovil;
    }

    public void setIdAutomovil(Automovil idAutomovil) {
        this.idAutomovil = idAutomovil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reparacion)) {
            return false;
        }
        Reparacion other = (Reparacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "controladores.Reparacion[ id=" + id + " ]";
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public java.util.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.util.Date fecha) {
        this.fecha = fecha;
    }

    public java.util.Date getHora() {
        return hora;
    }

    public void setHora(java.util.Date hora) {
        this.hora = hora;
    }
    
}
