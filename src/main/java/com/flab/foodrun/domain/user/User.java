package com.flab.foodrun.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Getter, @Setter, @RequiredArgsConstructor,@ToString, @EqualsAndHashCode, @Value를 합친 애노테이션
@Data
//Builder 패턴을 쉽게 사용할 수 있게 도와주는 애노테이션
@Builder
//인자가 없는 생성자를 만들어줌
@NoArgsConstructor
//모든 인자가 존재하는 생성자를 만들어줌
@AllArgsConstructor
public class User {

	private Long id;
	private String loginId;
	private String password;
	private String name;
	private Role role;
	private UserStatus status;
	private String phoneNumber;
	private String email;
	private String streetAddress;
	private String detailAddress;
	private String createdAt;
	private String modifiedAt;
	private String createdBy;
	private String modifiedBy;
}
