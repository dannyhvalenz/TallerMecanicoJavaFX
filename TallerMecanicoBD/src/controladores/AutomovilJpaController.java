/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
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
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoBDPU");

    public AutomovilJpaController() {}
    
    public AutomovilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crear(Automovil a){
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error al crear un automovil");
        }
    }
    
    public void create(Automovil automovil) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = automovil.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                automovil.setIdCliente(idCliente);
            }
            em.persist(automovil);
            if (idCliente != null) {
                idCliente.getAutomovilList().add(automovil);
                idCliente = em.merge(idCliente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAutomovil(automovil.getId()) != null) {
                throw new PreexistingEntityException("Automovil " + automovil + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void actualizar (Automovil automovil) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNamedQuery("Automovil.update")
                    .setParameter("matricula", automovil.getId())
                    .setParameter("marca", automovil.getMarca())
                    .setParameter("modelo", automovil.getModelo())
                    .setParameter("linea", automovil.getLinea())
                    .setParameter("color", automovil.getColor())
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error al actualizar el automovil");
        }
    }

    public void edit(Automovil automovil) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Automovil persistentAutomovil = em.find(Automovil.class, automovil.getId());
            Cliente idClienteOld = persistentAutomovil.getIdCliente();
            Cliente idClienteNew = automovil.getIdCliente();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
                automovil.setIdCliente(idClienteNew);
            }
            automovil = em.merge(automovil);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getAutomovilList().remove(automovil);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getAutomovilList().add(automovil);
                idClienteNew = em.merge(idClienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = automovil.getId();
                if (findAutomovil(id) == null) {
                    throw new NonexistentEntityException("The automovil with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String matricula) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            System.out.println(matricula + " a encontrar");
            em = getEntityManager();
            em.getTransaction().begin();
            Automovil automovil;
            try {
                automovil = findAutomovil(matricula);
                //automovil = em.getReference(Automovil.class, matricula);
                automovil.getId();
                System.out.println("Destroy id auto: " + automovil.getId());
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The automovil with id " + matricula + " no longer exists.", enfe);
            }
            Cliente idCliente = automovil.getIdCliente();
            if (idCliente != null) {
                System.out.println("Destroy id auto 1: " + automovil.getId());
                idCliente.getAutomovilList().remove(automovil);
                System.out.println("Destroy id auto 2: " + automovil.getId());
                idCliente = em.merge(idCliente);
                System.out.println("Destroy id auto 3: " + automovil.getId());
            }
            System.out.println("Destroy id auto 4: " + automovil.getId());
            Automovil a = em.merge(automovil);
            em.remove(a);
            System.out.println("Destroy id auto 5: " + automovil.getId());
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
    
    public Automovil findAutomovil(String matricula) {
        EntityManager em = getEntityManager();
        try {
            System.out.println(matricula + " a encontrar");
            return em.createNamedQuery("Automovil.findByMatricula", Automovil.class)
              .setParameter("matricula", matricula).getSingleResult();
            //return em.find(Automovil.class, matricula);
        } finally {
            em.close();
        }
    }
    
    public List<Automovil> findAutomoviles(String matricula, Cliente idCliente) {
        EntityManager em = getEntityManager();
        try {
            List<Automovil> autos = new ArrayList<Automovil>();
            autos = em.createNamedQuery("Automovil.findByMatriculaLike")
                    .setParameter("idCliente", idCliente)
                    .setParameter("matricula", matricula).getResultList();
            return autos;
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
    
    public List<Automovil> getAutomovilesCliente(Cliente idCliente){
        EntityManager em = getEntityManager();
        try {
            List<Automovil> autos = new ArrayList<Automovil>();
            autos = em.createNamedQuery("Automovil.findAll").setParameter("idCliente", idCliente).getResultList();
            return autos;
        } finally {
            em.close();
        }
    }

    public int getAutomovilCount(Cliente idCliente) {
        EntityManager em = getEntityManager();
        try {
            List<Automovil> autos = getAutomovilesCliente(idCliente);
            int contador = 0;
            for (Automovil a : autos){
                contador++;
            }
            return contador;
        } finally {
            em.close();
        }
    }
    
}

