package com.statusreserv.reservations.model.tenant;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class TenantConfig {
    private TenantConfigType property;
    private String value;
}
