package support;



public class TemplateCode {

    final String       code;

 
    public TemplateCode(final String templateCode) {
        this.code = templateCode;
     }


    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (code == null ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TemplateCode other = (TemplateCode) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }

  
    @Override
    public String toString() {
        return "TemplateCode [code=" + code +  "]";
    }

}
