package com.ipomoea.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Marie-Luise Lux
 */

@Entity
@Table(name = "user")
public class Authenticator implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstName;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public Authenticator() {
		this.createdAt = new Date();
	}

	public Authenticator(final String username) {
		this.username = username;
		this.createdAt = new Date();
	}

	public Authenticator(final String firstName, final String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.createdAt = new Date();
	}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(final Date createdAt) {
		this.createdAt = createdAt;
	}
}