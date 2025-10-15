package com.statusreserv.reservations.model.user;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_user_profile")
public class UserProfile {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String phone;
}
