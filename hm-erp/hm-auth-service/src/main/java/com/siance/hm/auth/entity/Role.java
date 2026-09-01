package com.siance.hm.auth.entity;

import com.siance.hm.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Port of the {@code Role} Prisma model - a named, reusable permission label. */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_system", nullable = false)
    private boolean system = true;

    public Role(String code, String name, boolean system) {
        this.code = code;
        this.name = name;
        this.system = system;
    }
}
