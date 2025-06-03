package com.ayu.realty.member.model.entity;

import com.ayu.realty.global.entity.BaseTimeEntity;
import com.ayu.realty.member.model.response.MemberPrivateRes;
import com.ayu.realty.member.model.response.MemberPublicRes;
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

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false, length = 10)
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

    public static MemberPublicRes toPublicDTO(Member member) {
      return MemberPublicRes.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public static MemberPrivateRes toAdminDTO(Member member) {
        return MemberPrivateRes.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
