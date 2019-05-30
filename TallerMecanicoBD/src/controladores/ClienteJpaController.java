/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Automovil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Cliente;

/**
 *
 * @author dany
 */
public class ClienteJpaController implements Serializable {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoBDPU");

    
    public ClienteJpaController() {
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void crearCliente (Cliente c) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void editarCliente (Cliente c) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            c = em.merge(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = c.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("El cliente con el id " + id + " no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void create(Cliente cliente) {
        if (cliente.getAutomovilList() == null) {
            cliente.setAutomovilList(new ArrayList<Automovil>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Automovil> attachedAutomovilList = new ArrayList<Automovil>();
            for (Automovil automovilListAutomovilToAttach : cliente.getAutomovilList()) {
                automovilListAutomovilToAttach = em.getReference(automovilListAutomovilToAttach.getClass(), automovilListAutomovilToAttach.getId());
                attachedAutomovilList.add(automovilListAutomovilToAttach);
            }
            cliente.setAutomovilList(attachedAutomovilList);
            em.persist(cliente);
            for (Automovil automovilListAutomovil : cliente.getAutomovilList()) {
                Cliente oldIdClienteOfAutomovilListAutomovil = automovilListAutomovil.getIdCliente();
                automovilListAutomovil.setIdCliente(cliente);
                automovilListAutomovil = em.merge(automovilListAutomovil);
                if (oldIdClienteOfAutomovilListAutomovil != null) {
                    oldIdClienteOfAutomovilListAutomovil.getAutomovilList().remove(automovilListAutomovil);
                    oldIdClienteOfAutomovilListAutomovil = em.merge(oldIdClienteOfAutomovilListAutomovil);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            List<Automovil> automovilListOld = persistentCliente.getAutomovilList();
            List<Automovil> automovilListNew = cliente.getAutomovilList();
            List<String> illegalOrphanMessages = null;
            for (Automovil automovilListOldAutomovil : automovilListOld) {
                if (!automovilListNew.contains(automovilListOldAutomovil)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Automovil " + automovilListOldAutomovil + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Automovil> attachedAutomovilListNew = new ArrayList<Automovil>();
            for (Automovil automovilListNewAutomovilToAttach : automovilListNew) {
                automovilListNewAutomovilToAttach = em.getReference(automovilListNewAutomovilToAttach.getClass(), automovilListNewAutomovilToAttach.getId());
                attachedAutomovilListNew.add(automovilListNewAutomovilToAttach);
            }
            automovilListNew = attachedAutomovilListNew;
            cliente.setAutomovilList(automovilListNew);
            cliente = em.merge(cliente);
            for (Automovil automovilListNewAutomovil : automovilListNew) {
                if (!automovilListOld.contains(automovilListNewAutomovil)) {
                    Cliente oldIdClienteOfAutomovilListNewAutomovil = automovilListNewAutomovil.getIdCliente();
                    automovilListNewAutomovil.setIdCliente(cliente);
                    automovilListNewAutomovil = em.merge(automovilListNewAutomovil);
                    if (oldIdClienteOfAutomovilListNewAutomovil != null && !oldIdClienteOfAutomovilListNewAutomovil.equals(cliente)) {
                        oldIdClienteOfAutomovilListNewAutomovil.getAutomovilList().remove(automovilListNewAutomovil);
                        oldIdClienteOfAutomovilListNewAutomovil = em.merge(oldIdClienteOfAutomovilListNewAutomovil);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String nombre = cliente.getNombre();
                if (findCliente(nombre) == null) {
                    throw new NonexistentEntityException("El cliente con el nombre " + nombre + " no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Automovil> automovilListOrphanCheck = cliente.getAutomovilList();
            for (Automovil automovilListOrphanCheckAutomovil : automovilListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Automovil " + automovilListOrphanCheckAutomovil + " in its automovilList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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
    
    public List<Cliente> findAllClientes(){
        EntityManager em = getEntityManager();
        try {
            List<Cliente> clientes = new ArrayList<Cliente>();
            clientes = em.createNamedQuery("Cliente.findAll").getResultList();
            return clientes;
        } finally {
            em.close();
        }
    }

    public List<Cliente> findClientes(String nombre){
        EntityManager em = getEntityManager();
        try {
            List<Cliente> clientes = new ArrayList<Cliente>();
            clientes = em.createNamedQuery("Cliente.findByNombreLike").setParameter("nombre", nombre).getResultList();
            return clientes;
        } finally {
            em.close();
        }
    }
    
    public Cliente findCliente(String nombre) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, nombre);
        } finally {
            em.close();
        }
    }
    
    public Cliente findCliente(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
