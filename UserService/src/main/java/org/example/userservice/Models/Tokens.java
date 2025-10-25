package org.example.userservice.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Tokens extends BaseModel{
    private String tokenValue;
    private Date expiryAt;
    @ManyToOne
    private User user;
}
/*
*   1         1
* Token --- user
*   M         1
* */

