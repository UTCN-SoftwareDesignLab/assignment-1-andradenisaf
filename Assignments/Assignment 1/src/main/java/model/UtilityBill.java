package model;

import java.io.Serializable;

public class UtilityBill implements IModel, Serializable {

    private int id;
    private UtilityBillType type;
    private int clientId;
    private float amount;
    private boolean paid;

    public UtilityBill(UtilityBillType type, int clientId, float amount, boolean paid) {
        this.type = type;
        this.clientId = clientId;
        this.amount = amount;
        this.paid = paid;
    }
    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public UtilityBillType getType() {
        return type;
    }

    public void setType(UtilityBillType type) {
        this.type = type;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "UtilityBill{" +
                "type=" + type +
                ", clientId=" + clientId +
                ", amount=" + amount +
                ", paid=" + paid +
                '}';
    }
}
