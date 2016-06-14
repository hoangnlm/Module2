package customer.controller;

import customer.model.CustomerLevel;
import customer.model.CustomerLevelDAOImpl;
import utility.MyComboBoxModel;

/**
 *
 * @author Hoang
 */
public class CustomerLevelComboBoxModel extends MyComboBoxModel<CustomerLevel> {

    public CustomerLevelComboBoxModel() {
        super(new CustomerLevelDAOImpl());
    }

    /**
     * Tim customer level object tu customer level
     *
     * @param cusLevel
     * @return
     */
    public CustomerLevel getCustomerLevelFromValue(int cusLevel) {
        CustomerLevel result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCusLevel() == cusLevel) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
