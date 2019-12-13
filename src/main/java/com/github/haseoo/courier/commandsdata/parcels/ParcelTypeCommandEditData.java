package com.github.haseoo.courier.commandsdata.parcels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Getter
@Validated
public class ParcelTypeCommandEditData {
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;

    @JsonCreator
    public ParcelTypeCommandEditData(@JsonProperty(value = "name") String name,
                                     @JsonProperty(value = "description") String description,
                                     @JsonProperty(value = "price") BigDecimal price,
                                     @JsonProperty(value = "isActive") Boolean active) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
    }
}
