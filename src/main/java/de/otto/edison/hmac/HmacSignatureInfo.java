package de.otto.edison.hmac;

public class HmacSignatureInfo {
    private final String signature;

    public String getDate() {
        return date;
    }

    public String getSignature() {
        return signature;
    }

    private final String date;

    public HmacSignatureInfo(String signature, String date) {
        this.signature = signature;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HmacSignatureInfo that = (HmacSignatureInfo) o;

        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;
        return !(date != null ? !date.equals(that.date) : that.date != null);

    }

    @Override
    public int hashCode() {
        int result = signature != null ? signature.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HmacSignatureInfo{" +
                "signature='" + signature + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
