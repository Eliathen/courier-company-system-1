package com.github.haseoo.courier.views.parcels.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class ParcelTypeView extends ParcelTypeOfferView {
    private Long id;
}