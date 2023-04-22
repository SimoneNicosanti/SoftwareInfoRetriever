package it.uniroma2.isw2.model;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VersionInfo {

    private String versionName ;
    private LocalDate versionDate ;
    private String versionId ;

    private Integer releaseNumber ;

    private List<RevCommit> versionCommitList ;

    private List<ClassInfo> classInfoList ;


    public VersionInfo(String versionName, LocalDate versionDate, String versionId) {
        this.versionName = versionName ;
        this.versionDate = versionDate ;
        this.versionId = versionId ;
    }

    public LocalDate getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(LocalDate versionDate) {
        this.versionDate = versionDate;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(Integer releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder() ;
        stringBuilder.append("Number ").append(releaseNumber).append(" // ").append("Name ").append(versionName).append(" // ").append("Date ").append(versionDate.toString()) ;
        return stringBuilder.toString() ;
    }

    public List<ClassInfo> getClassInfoList() {
        return classInfoList;
    }

    public void setClassInfoList(List<ClassInfo> classInfoList) {
        this.classInfoList = classInfoList;
    }

    public List<RevCommit> getVersionCommitList() {
        return versionCommitList;
    }

    public void setVersionCommitList(List<RevCommit> versionCommitList) {
        this.versionCommitList = versionCommitList;
    }
}
