/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dany
 */
@Entity
@Table(name = "automovil")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Automovil.findAll", query = "SELECT a FROM Automovil a")
    , @NamedQuery(name = "Automovil.findById", query = "SELECT a FROM Automovil a WHERE a.id = :id")
    , @NamedQuery(name = "Automovil.findByMarca", query = "SELECT a FROM Automovil a WHERE a.marca = :marca")
    , @NamedQuery(name = "Automovil.findByModelo", query = "SELECT a FROM Automovil a WHERE a.modelo = :modelo")
    , @NamedQuery(name = "Automovil.findByLinea", query = "SELECT a FROM Automovil a WHERE a.linea = :linea")
    , @NamedQuery(name = "Automovil.findByColor", query = "SELECT a FROM Automovil a WHERE a.color = :color")})
public class Automovil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @Column(name = "modelo")
    private String modelo;
    @Basic(optional = false)
    @Column(name = "linea")
    private String linea;
    @Basic(optional = false)
    @Column(name = "color")
    private String color;
    @JoinColumn(name = "idCliente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @OneToMany(mappedBy = "idAutomovil")
    private List<Reparacion> reparacionList;

    public Automovil() {
    }

    public Automovil(Integer id) {
        this.id = id;
    }

    public Automovil(Integer id, String marca, String modelo, String linea, String color) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.linea = linea;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    @XmlTransient
    public List<Reparacion> getReparacionList() {
        return reparacionList;
    }

    public void setReparacionList(List<Reparacion> reparacionList) {
        this.reparacionList = reparacionList;
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
        if (!(object instanceof Automovil)) {
            return false;
        }
        Automovil other = (Automovil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Automovil[ id=" + id + " ]";
    }
    
}
