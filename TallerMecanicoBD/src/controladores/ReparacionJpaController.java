/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Automovil;
import modelo.Reparacion;

/**
 *
 * @author dany
 */
public class ReparacionJpaController implements Serializable {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoBDPU");
    
    public ReparacionJpaController(){}
    
    public ReparacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crear(Reparacion r) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
        } catch (Exception e){
            System.out.println("Error al crear la reparacion");
        }
    }
    
    public void create(Reparacion reparacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Automovil idAutomovil = reparacion.getIdAutomovil();
            if (idAutomovil != null) {
                idAutomovil = em.getReference(idAutomovil.getClass(), idAutomovil.getId());
                reparacion.setIdAutomovil(idAutomovil);
            }
            em.persist(reparacion);
            if (idAutomovil != null) {
                idAutomovil.getReparacionList().add(reparacion);
                idAutomovil = em.merge(idAutomovil);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void actualizar(Reparacion r) throws Exception{
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNamedQuery("Reparacion.update")
                    .setParameter("fecha", r.getFecha())
                    .setParameter("hora", r.getHora())
                    .setParameter("costo", r.getCosto())
                    .setParameter("tipo", r.getTipo())
                    .setParameter("kilometraje", r.getKilometraje())
                    .setParameter("descripcionFalla", r.getDescripcionFalla())
                    .setParameter("descripcionMantenimiento", r.getDescripcionMantenimiento())
                    .setParameter("id", r.getId())
                    .executeUpdate();
            
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error al actualizar la reparacion");
        }
    }

    public void edit(Reparacion reparacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reparacion persistentReparacion = em.find(Reparacion.class, reparacion.getId());
            Automovil idAutomovilOld = persistentReparacion.getIdAutomovil();
            Automovil idAutomovilNew = reparacion.getIdAutomovil();
            if (idAutomovilNew != null) {
                idAutomovilNew = em.getReference(idAutomovilNew.getClass(), idAutomovilNew.getId());
                reparacion.setIdAutomovil(idAutomovilNew);
            }
            reparacion = em.merge(reparacion);
            if (idAutomovilOld != null && !idAutomovilOld.equals(idAutomovilNew)) {
                idAutomovilOld.getReparacionList().remove(reparacion);
                idAutomovilOld = em.merge(idAutomovilOld);
            }
            if (idAutomovilNew != null && !idAutomovilNew.equals(idAutomovilOld)) {
                idAutomovilNew.getReparacionList().add(reparacion);
                idAutomovilNew = em.merge(idAutomovilNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reparacion.getId();
                if (findReparacion(id) == null) {
                    throw new NonexistentEntityException("The reparacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reparacion reparacion;
            try {
                reparacion = em.getReference(Reparacion.class, id);
                reparacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reparacion with id " + id + " no longer exists.", enfe);
            }
            Automovil idAutomovil = reparacion.getIdAutomovil();
            if (idAutomovil != null) {
                idAutomovil.getReparacionList().remove(reparacion);
                idAutomovil = em.merge(idAutomovil);
            }
            em.remove(reparacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reparacion> findReparacionEntities() {
        return findReparacionEntities(true, -1, -1);
    }

    public List<Reparacion> findReparacionEntities(int maxResults, int firstResult) {
        return findReparacionEntities(false, maxResults, firstResult);
    }

    private List<Reparacion> findReparacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reparacion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Reparacion findReparacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reparacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getReparacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reparacion> rt = cq.from(Reparacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Reparacion> findReparaciones(String tipo, Automovil idAutomovil) {
        EntityManager em = getEntityManager();
        try {
            List<Reparacion> reparaciones = new ArrayList<Reparacion>();
            reparaciones = em.createNamedQuery("Reparacion.findByTipoLike")
                    .setParameter("idAutomovil", idAutomovil)
                    .setParameter("tipo", tipo).getResultList();
            return reparaciones;
        } finally {
            em.close();
        }
    }
    
    public List<Reparacion> getReparacionesAutomovil(Automovil idAutomovil){
        EntityManager em = getEntityManager();
        try {
            List<Reparacion> reparaciones = new ArrayList<Reparacion>();
            reparaciones = em.createNamedQuery("Reparacion.findAll").setParameter("idAutomovil", idAutomovil).getResultList();
            return reparaciones;
        } finally {
            em.close();
        }
    }
    
    public int getLastId(){
        EntityManager em = getEntityManager();
        try {
            int id = 0;
            List<Reparacion> reparaciones = new ArrayList<Reparacion>();
            reparaciones = em.createNamedQuery("Reparacion.findAllOrderById").getResultList();
            for (Reparacion r: reparaciones){
                id = r.getId();
            }
            return id;
        } finally {
            em.close();
        }
    }

}
