package order.controller;

import order.model.Status;
import order.model.StatusDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author Hoang
 */
public class OrderStatusComboBoxModel extends CustomizedComboBoxModel<Status> {

    public OrderStatusComboBoxModel() {
        super(new StatusDAOImpl());
    }

    /**
     * Tim status object tu status name
     *
     * @param sttName
     * @return
     */
    public Status getStatusFromValue(String sttName) {
        Status result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSttName().equals(sttName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
