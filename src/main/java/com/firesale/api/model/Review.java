package com.firesale.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Review extends BaseEntity {
    @Column(length = 1, nullable = false)
    private Integer rating;

    @Column(nullable = true)
    private String message;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_user_id")
    private User receiver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewer_user_id")
    private User reviewer;
}