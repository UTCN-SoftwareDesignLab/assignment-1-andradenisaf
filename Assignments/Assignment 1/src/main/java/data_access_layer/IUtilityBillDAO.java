package data_access_layer;

import model.UtilityBill;

import java.util.List;

public interface IUtilityBillDAO {

    public boolean save(UtilityBill utilityBill);

    public boolean delete(int id);

    public boolean update(UtilityBill utilityBill);

    public UtilityBill getUtilityBillById(int id);

    public List<UtilityBill> getUtilityBills();
}
