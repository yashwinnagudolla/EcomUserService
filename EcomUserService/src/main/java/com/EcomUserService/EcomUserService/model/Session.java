package com.EcomUserService.EcomUserService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Session extends BaseClass{
    private String token;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus status;
    @ManyToOne
    private User user;
    private Date loginAt;
    private Date expitingAt;

}
