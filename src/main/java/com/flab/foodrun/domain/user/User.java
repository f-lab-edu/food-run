package com.flab.foodrun.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter // getter 메서드 자동 생성 애노테이션
@Setter // setter 메서드 자동 생성 애노테이션
@ToString // toString() 메서드 자동 생성 애노테이션
@Builder //Builder 패턴을 쉽게 사용할 수 있게 도와주는 애노테이션
@NoArgsConstructor //인자가 없는 생성자를 만들어줌
@AllArgsConstructor //모든 인자가 존재하는 생성자를 만들어줌
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
