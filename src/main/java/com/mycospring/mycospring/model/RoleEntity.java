package com.mycospring.mycospring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Roles", schema = "MyCoSpring")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idRoles;

	@Enumerated(EnumType.STRING)
	private RoleName role;

	public RoleEntity(RoleName roleName) {
		this.role = roleName;
	}

	public String getRole() {
		return role.toString();
	}

}
