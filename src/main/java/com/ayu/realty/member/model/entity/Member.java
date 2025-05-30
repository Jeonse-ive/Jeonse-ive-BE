package com.ayu.realty.member.model.entity;

import com.ayu.realty.global.entity.BaseTimeEntity;
import com.ayu.realty.member.model.request.MemberReq;
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

    public static MemberReq toDTO(Member member) {
      return MemberReq.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }

}
