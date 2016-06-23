package main.model;

import java.io.Serializable;

/**
 *
 * @author Hoang
 */
public class UserFunction implements Serializable {

    public String FunctionGroup;
    public String FunctionName;

    public static final String COL_FGROUP = "FunctionGroup";
    public static final String COL_FNAME = "FunctionName";

    public UserFunction(String FunctionGroup, String FunctionName) {
        this.FunctionGroup = FunctionGroup;
        this.FunctionName = FunctionName;
    }

    @Override
    public String toString() {
        return "UserFunction{" + "FunctionGroup=" + FunctionGroup + ", FunctionName=" + FunctionName + '}';
    }
}
