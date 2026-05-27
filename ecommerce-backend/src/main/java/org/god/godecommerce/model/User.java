package org.god.godecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(min = 3, max = 20, message = "username must not exceed 20 characters.")
    @Column(unique = true)
    private String userName;

    @NotBlank
    @Size(max = 50, message = "email address must not exceed 50 characters.")
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 3, max = 120)
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.userName = name;
        this.email = email;
        this.password = password;
    }

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "seller", orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Cart cart;

    public Long getId() {
        return userId;
    }
}
