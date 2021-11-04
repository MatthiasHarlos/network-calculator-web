package forms;

import javax.validation.constraints.NotBlank;

public class NetworkInputForm {
    @NotBlank
    private String ipString;
    private String snmString;
    private Integer shortsnm;

    public Integer getShortsnm() {
        return shortsnm;
    }

    public void setShortsnm(Integer shortsnm) {
        this.shortsnm = shortsnm;
    }

    public String getIpString() {
        return ipString;
    }

    public void setIpString(String ipString) {
        this.ipString = ipString;
    }

    public String getSnmString() {
        return snmString;
    }

    public void setSnmString(String snmString) {
        this.snmString = snmString;
    }

    @Override
    public String toString() {
        return "NetworkInputForm{" +
                "ipString='" + ipString + '\'' +
                ", snmString='" + snmString + '\'' +
                '}';
    }
}
