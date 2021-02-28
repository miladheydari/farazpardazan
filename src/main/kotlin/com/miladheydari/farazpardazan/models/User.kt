package com.miladheydari.farazpardazan.models

import com.fasterxml.jackson.annotation.JsonInclude
import javax.persistence.*
import javax.persistence.Entity
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern

@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = "users")
class User(@Id @GeneratedValue val id: Long,

           @Pattern(regexp = "(^$|[0-9]{11})") @Column(unique = true, nullable = false) val phoneNumber: String,
           @Email @Column(unique = true, nullable = true) var email: String?= null,
           @Column(unique = true, nullable = true) var username: String?= null,
           var firstName: String?= null,
           var lastName: String?= null,
           var birthDate: String?= null,
           var genderType: GenderType? =null,
           var hasImage:Boolean? = false
) : Audit()