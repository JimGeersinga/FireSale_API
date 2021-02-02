package com.firesale.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Getter
@Setter
@Entity
public class Category extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    private Boolean archived;

    @ManyToMany(mappedBy = "categories")
    private Collection<Auction> auctions;
}
