package it.uniroma2.isw2.model.rerieve;

import org.eclipse.jgit.revwalk.RevCommit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketInfo {

    private String ticketId ;
    private VersionInfo fixVersion ;
    private List<RevCommit> fixCommitList;
    private LocalDate createDate ;
    private LocalDate resolutionDate ;
    private List<VersionInfo> affectedVersionList ;
    private VersionInfo openingVersion ;
    private VersionInfo injectedVersion ;


    public TicketInfo(String ticketId) {
        this.ticketId = ticketId ;
        this.fixCommitList = new ArrayList<>() ;
        this.affectedVersionList = new ArrayList<>() ;
    }


    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }


    public VersionInfo getOpeningVersion() {
        return openingVersion;
    }

    public void setOpeningVersion(VersionInfo openingVersion) {
        this.openingVersion = openingVersion;
    }

    public List<RevCommit> getFixCommitList() {
        return fixCommitList;
    }

    public void setFixCommitList(List<RevCommit> fixCommitList) {
        this.fixCommitList = fixCommitList;
    }

    public VersionInfo getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(VersionInfo fixVersion) {
        this.fixVersion = fixVersion;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public List<VersionInfo> getAffectedVersionList() {
        return affectedVersionList;
    }

    public void setAffectedVersionList(List<VersionInfo> affectedVersionList) {
        this.affectedVersionList = affectedVersionList;
    }

    public VersionInfo getInjectedVersion() {
        return injectedVersion;
    }

    public void setInjectedVersion(VersionInfo injectedVersion) {
        this.injectedVersion = injectedVersion;
    }


    public LocalDate getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(LocalDate resolutionDate) {
        this.resolutionDate = resolutionDate;
    }


}
