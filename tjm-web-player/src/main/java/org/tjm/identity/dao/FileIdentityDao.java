/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.identity.dao;

import java.util.List;
import javax.ejb.Local;
import org.tjm.identity.entity.FileIdentity;

/**
 *
 * @author larry
 */
@Local
public interface FileIdentityDao {

    public List<FileIdentity> selectAll();

    public void save(FileIdentity fi);

    public void remove(String fileIdentityId);

    public FileIdentity findById(String fileIdentityId);

    public FileIdentity findByFilePath(String filepath);

    public void update(FileIdentity fileIdentity);
}
