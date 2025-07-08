package br.tulio.projetospring.data.dto.v1;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long fileSize;

    public UploadFileResponseDTO() {}

    public UploadFileResponseDTO(String fileName, String fileDownloadUri, String fileType, Long fileSize) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UploadFileResponseDTO that)) return false;
        return Objects.equals(getFileName(), that.getFileName()) && Objects.equals(getFileDownloadUri(), that.getFileDownloadUri()) && Objects.equals(getFileType(), that.getFileType()) && Objects.equals(getFileSize(), that.getFileSize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileName(), getFileDownloadUri(), getFileType(), getFileSize());
    }
}
