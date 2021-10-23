package fpt.swp391.model.dto;

import lombok.Data;

import javax.management.relation.Role;

@Data
public class AccountDto {
    private String name;
    private Role role;

}