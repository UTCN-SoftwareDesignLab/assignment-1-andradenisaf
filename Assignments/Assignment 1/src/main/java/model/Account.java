package model;

import java.io.Serializable;

import java.sql.Date;

public class Account implements IModel, Serializable {

    private int id;
    private int clientId;
    private AccountType type;
    private float amount;
    private Date creationDate;

    public Account(int clientId, AccountType type, float amount, Date creationDate) {
        this.clientId = clientId;
        this.type = type;
        this.amount = amount;
        this.creationDate = creationDate;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "clientId=" + clientId +
                ", type=" + type +
                ", amount=" + amount +
                ", creationDate=" + creationDate +
                '}';
    }
}
