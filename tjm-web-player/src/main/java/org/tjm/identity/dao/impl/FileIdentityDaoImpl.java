/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.identity.dao.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.tjm.identity.dao.FileIdentityDao;
import org.tjm.identity.entity.FileIdentity;

/**
 *
 * @author larry
 */
@Stateless
public class FileIdentityDaoImpl implements FileIdentityDao {

    @PersistenceContext(unitName = "tjm")
    EntityManager em;

    @Override
    public List<FileIdentity> selectAll() {
        List<FileIdentity> fiList = em.createQuery("SELECT f FROM FileIdentity f").getResultList();
        return fiList;
    }

    @Override
    public void save(FileIdentity fi) {
        em.persist(fi);
    }

    @Override
    public void remove(String fileIdentityId) {
        em.remove(em.find(FileIdentity.class, fileIdentityId));
    }

    @Override
    public FileIdentity findById(String fileIdentityId) {
        return em.find(FileIdentity.class, fileIdentityId);
    }

    @Override
    public FileIdentity findByFilePath(String filepath) {
        Query query = em.createQuery("SELECT f FROM FileIdentity f WHERE f.filepath = :filepath").setParameter("filepath", filepath);
        List<FileIdentity> fiList = query.getResultList();
        if (fiList.isEmpty()) {
            return null;
        }
        return fiList.get(0);
    }
    @Override
    public void update(FileIdentity fileIdentity) {
        em.merge(fileIdentity);
    }

}
