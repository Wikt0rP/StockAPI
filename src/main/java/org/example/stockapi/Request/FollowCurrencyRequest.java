package org.example.stockapi.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowCurrencyRequest {
    String currency;
    String fullName;
    char currencyTable;
}
