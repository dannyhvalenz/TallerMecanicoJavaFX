/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Cliente;
import modelo.Reparacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Automovil;

/**
 *
 * @author dany
 */
public class AutomovilJpaController implements Serializable {

    public AutomovilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoBDPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Automovil automovil) {
        if (automovil.getReparacionList() == null) {
            automovil.setReparacionList(new ArrayList<Reparacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = automovil.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                automovil.setIdCliente(idCliente);
            }
            List<Reparacion> attachedReparacionList = new ArrayList<Reparacion>();
            for (Reparacion reparacionListReparacionToAttach : automovil.getReparacionList()) {
                reparacionListReparacionToAttach = em.getReference(reparacionListReparacionToAttach.getClass(), reparacionListReparacionToAttach.getId());
                attachedReparacionList.add(reparacionListReparacionToAttach);
            }
            automovil.setReparacionList(attachedReparacionList);
            em.persist(automovil);
            if (idCliente != null) {
                idCliente.getAutomovilList().add(automovil);
                idCliente = em.merge(idCliente);
            }
            for (Reparacion reparacionListReparacion : automovil.getReparacionList()) {
                Automovil oldIdAutomovilOfReparacionListReparacion = reparacionListReparacion.getIdAutomovil();
                reparacionListReparacion.setIdAutomovil(automovil);
                reparacionListReparacion = em.merge(reparacionListReparacion);
                if (oldIdAutomovilOfReparacionListReparacion != null) {
                    oldIdAutomovilOfReparacionListReparacion.getReparacionList().remove(reparacionListReparacion);
                    oldIdAutomovilOfReparacionListReparacion = em.merge(oldIdAutomovilOfReparacionListReparacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

//    public void edit(Automovil automovil) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            Automovil persistentAutomovil = em.find(Automovil.class, automovil.getId());
//            Cliente idClienteOld = persistentAutomovil.getIdCliente();
//            Cliente idClienteNew = automovil.getIdCliente();
//            List<Reparacion> reparacionListOld = persistentAutomovil.getReparacionList();
//            List<Reparacion> reparacionListNew = automovil.getReparacionList();
//            if (idClienteNew != null) {
//                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
//                automovil.setIdCliente(idClienteNew);
//            }
//            List<Reparacion> attachedReparacionListNew = new ArrayList<Reparacion>();
//            for (Reparacion reparacionListNewReparacionToAttach : reparacionListNew) {
//                reparacionListNewReparacionToAttach = em.getReference(reparacionListNewReparacionToAttach.getClass(), reparacionListNewReparacionToAttach.getId());
//                attachedReparacionListNew.add(reparacionListNewReparacionToAttach);
//            }
//            reparacionListNew = attachedReparacionListNew;
//            automovil.setReparacionList(reparacionListNew);
//            automovil = em.merge(automovil);
//            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
//                idClienteOld.getAutomovilList().remove(automovil);
//                idClienteOld = em.merge(idClienteOld);
//            }
//            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
//                idClienteNew.getAutomovilList().add(automovil);
//                idClienteNew = em.merge(idClienteNew);
//            }
//            for (Reparacion reparacionListOldReparacion : reparacionListOld) {
//                if (!reparacionListNew.contains(reparacionListOldReparacion)) {
//                    reparacionListOldReparacion.setIdAutomovil(null);
//                    reparacionListOldReparacion = em.merge(reparacionListOldReparacion);
//                }
//            }
//            for (Reparacion reparacionListNewReparacion : reparacionListNew) {
//                if (!reparacionListOld.contains(reparacionListNewReparacion)) {
//                    Automovil oldIdAutomovilOfReparacionListNewReparacion = reparacionListNewReparacion.getIdAutomovil();
//                    reparacionListNewReparacion.setIdAutomovil(automovil);
//                    reparacionListNewReparacion = em.merge(reparacionListNewReparacion);
//                    if (oldIdAutomovilOfReparacionListNewReparacion != null && !oldIdAutomovilOfReparacionListNewReparacion.equals(automovil)) {
//                        oldIdAutomovilOfReparacionListNewReparacion.getReparacionList().remove(reparacionListNewReparacion);
//                        oldIdAutomovilOfReparacionListNewReparacion = em.merge(oldIdAutomovilOfReparacionListNewReparacion);
//                    }
//                }
//            }
//            em.getTransaction().commit();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//            if (msg == null || msg.length() == 0) {
//                Integer id = automovil.getId();
//                if (findAutomovil(id) == null) {
//                    throw new NonexistentEntityException("The automovil with id " + id + " no longer exists.");
//                }
//            }
//            throw ex;
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Automovil automovil;
            try {
                automovil = em.getReference(Automovil.class, id);
                automovil.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The automovil with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = automovil.getIdCliente();
            if (idCliente != null) {
                idCliente.getAutomovilList().remove(automovil);
                idCliente = em.merge(idCliente);
            }
            List<Reparacion> reparacionList = automovil.getReparacionList();
            for (Reparacion reparacionListReparacion : reparacionList) {
                reparacionListReparacion.setIdAutomovil(null);
                reparacionListReparacion = em.merge(reparacionListReparacion);
            }
            em.remove(automovil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Automovil> findAutomovilEntities() {
        return findAutomovilEntities(true, -1, -1);
    }

    public List<Automovil> findAutomovilEntities(int maxResults, int firstResult) {
        return findAutomovilEntities(false, maxResults, firstResult);
    }

    private List<Automovil> findAutomovilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Automovil.class));
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

    public Automovil findAutomovil(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Automovil.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutomovilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Automovil> rt = cq.from(Automovil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
