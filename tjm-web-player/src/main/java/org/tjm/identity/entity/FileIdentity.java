/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.identity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author larry
 */
@Entity
@Table(name = "file_identity")
public class FileIdentity {

    @Id
    private String id;
    @Column(name = "filepath")
    private String filepath;
    @Column(name = "is_public")
    private Boolean isPublic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37). // 應放入兩個質數
                append(filepath). // 利用append()加入參與的物件變數
                toHashCode(); // 最後取得hash code數值
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof FileIdentity) {
            FileIdentity f = (FileIdentity) that;
            return filepath.equals(f.getFilepath());
        }
        return false;
    }

}
