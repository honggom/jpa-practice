package com.example.jpapractice.dto.listener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class EntityListener {

    @PrePersist
    public void prePersist(Object object) {
        if (object instanceof Auditable){
            ((Auditable) object).setCreatedAt(LocalDateTime.now());
            ((Auditable) object).setUpdatedAt(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void preUpdate(Object object) {
        if (object instanceof Auditable){
            ((Auditable) object).setUpdatedAt(LocalDateTime.now());
        }
    }

}
