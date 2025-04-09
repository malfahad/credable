package com.credable.lms.domain;

import com.credable.lms.util.Constants;
import jakarta.persistence.*;

@Entity
@Table(name = Constants.TABLE_CLIENT_CONFIG, uniqueConstraints = {
    @UniqueConstraint(columnNames = Constants.COLUMN_URL)
})
public class ClientConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Constants.COLUMN_URL, nullable = false, unique = true)
    private String url;

    @Column(name = Constants.COLUMN_NAME, nullable = false)
    private String name;

    @Column(name = Constants.COLUMN_TOKEN, nullable = false)
    private String token;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
} 