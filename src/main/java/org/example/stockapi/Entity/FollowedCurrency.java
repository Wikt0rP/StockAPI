package org.example.stockapi.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowedCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public FollowedCurrency(String symbol, User user) {
        this.symbol = symbol;
        this.user = user;
    }
    public FollowedCurrency(String symbol, String fullName, User user) {
        this.symbol = symbol;
        this.fullName = fullName;
        this.user = user;
    }

}
