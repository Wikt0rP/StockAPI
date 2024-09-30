package org.example.stockapi.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickerRequest {
    private String ticker;
}
