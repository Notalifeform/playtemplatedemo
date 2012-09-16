package support.theme;


public class PathResult {

    private final String       fullPath;
    private final String       externalFilePath;
    private final String       code;
 
    private final String       fileName;

    public PathResult(final String fullPath, final String externalFilePath,  final String code,
             final String fileName) {
        this.fullPath = fullPath;
        this.externalFilePath = externalFilePath;
        this.code = code;
      
        this.fileName = fileName;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getExternalFilePath() {
        return externalFilePath;
    }

 
    public String getCode() {
        return code;
    }

  
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (code == null ? 0 : code.hashCode());
        result = prime * result + (externalFilePath == null ? 0 : externalFilePath.hashCode());
        result = prime * result + (fileName == null ? 0 : fileName.hashCode());
        result = prime * result + (fullPath == null ? 0 : fullPath.hashCode());
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
        final PathResult other = (PathResult) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (externalFilePath == null) {
            if (other.externalFilePath != null) {
                return false;
            }
        } else if (!externalFilePath.equals(other.externalFilePath)) {
            return false;
        }
        if (fileName == null) {
            if (other.fileName != null) {
                return false;
            }
        } else if (!fileName.equals(other.fileName)) {
            return false;
        }
        if (fullPath == null) {
            if (other.fullPath != null) {
                return false;
            }
        } else if (!fullPath.equals(other.fullPath)) {
            return false;
        }
         return true;
    }

    @Override
    public String toString() {
        return "PathResult [fullPath=" + fullPath + ", externalFilePath=" + externalFilePath + 
                ", code=" + code + ", fileName=" + fileName + "]";
    }

}
