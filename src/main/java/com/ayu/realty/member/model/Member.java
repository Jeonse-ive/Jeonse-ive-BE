package com.ayu.realty.member.model;

import com.ayu.realty.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member toEntity(String email, String name, String password, Role role) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(name)
                .role(role)
                .build();
    }

}
