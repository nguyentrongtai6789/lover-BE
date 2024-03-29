package com.example.loverbackend.model;

import lombok.Data;

import javax.persistence.*;
//đây là nhánh của Tài
@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private RoleName roleName;
}
