package com.firesale.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Getter
@Setter
@Entity
public class Image extends BaseEntity {
    @Lob
    @Column(nullable = false)
    private byte[] path;

    @Column(nullable = false)
    private Integer sort = 0;

    @Column(nullable = false)
    private String type;
}
