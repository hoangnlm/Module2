package order.controller;

import order.model.OrderProduct;
import order.model.OrderProductDAOImplDialog;
import utility.CustomizedComboBoxModel;

/**
 * Dung cho combo box column product trong table product list
 * cua OrderDialog.
 * 
 * @author Hoang
 */
public class OrderProductComboBoxModel extends CustomizedComboBoxModel<OrderProduct> {

    public OrderProductComboBoxModel() {
        super(new OrderProductDAOImplDialog());
    }
        
    public void load(int proID){
        list = daoImpl.getList();
        fireContentsChanged(this, 0, list.size()-1);
    }

    /**
     * Tim status object tu status name
     *
     * @param proID
     * @return
     */
    public OrderProduct getProductFromID(int proID) {
        OrderProduct result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProID()==proID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
    
        public OrderProduct getProductFromName(String proName) {
        OrderProduct result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProName().equals(proName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
