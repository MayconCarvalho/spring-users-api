package com.example.demo.entity;

import com.example.demo.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;
}
