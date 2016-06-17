package order.controller;

import order.model.OrderProduct;
import order.model.OrderProductDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author Hoang
 */
public class OrderProductComboBoxModel extends CustomizedComboBoxModel<OrderProduct> {

    public OrderProductComboBoxModel() {
        super(new OrderProductDAOImpl());
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
            if (list.get(i).getProName()==proID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
