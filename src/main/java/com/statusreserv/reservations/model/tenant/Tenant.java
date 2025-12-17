package com.statusreserv.reservations.model.tenant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_tenant")
public class Tenant {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String phone;
    private String address;
    private String email;
    private Instant createdAt;
    private Instant openYear;
    private boolean enabled;

    @ElementCollection
    @CollectionTable(
            name = "tenant_config",
            joinColumns = @JoinColumn(name = "tenant_id")
    )
    @AttributeOverrides({
            @AttributeOverride(name = "property", column = @Column(name = "config_key")),
            @AttributeOverride(name = "value", column = @Column(name = "config_value"))
    })
    private Set<TenantConfig> configSet;
}
