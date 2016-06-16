package order.model;

/**
 *
 * @author Hoang
 */
public class Status {
    private int sttID;
    private String sttName;
    private String sttType;
    
    public static final String COL_ID = "SttID";
    public static final String COL_NAME = "SttName";
    public static final String COL_TYPE = "SttType";

    public Status() {
    }

    public Status(int sttID, String sttName, String sttType) {
        this.sttID = sttID;
        this.sttName = sttName;
        this.sttType = sttType;
    }

    public int getSttID() {
        return sttID;
    }

    public void setSttID(int sttID) {
        this.sttID = sttID;
    }

    public String getSttName() {
        return sttName;
    }

    public void setSttName(String sttName) {
        this.sttName = sttName;
    }

    public String getSttType() {
        return sttType;
    }

    public void setSttType(String sttType) {
        this.sttType = sttType;
    }
}
