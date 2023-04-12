package it.uniroma2.isw2.retriever;

import it.uniroma2.isw2.builder.URLBuilder;
import it.uniroma2.isw2.model.TicketInfo;
import it.uniroma2.isw2.model.VersionInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketRetriever {

    String projectName ;

    public TicketRetriever(String projectName) {
        this.projectName = projectName ;
    }

    public List<TicketInfo> retrieveBugTicket(List<VersionInfo> versionInfoList) throws IOException, URISyntaxException {
        Logger.getGlobal().log(Level.INFO, "Ticket Info Retrieve From Jira");

        URLBuilder urlBuilder = new URLBuilder() ;
        String urlFirstPart = urlBuilder.buildUrl(projectName) ;
        int startPoint = 0 ;
        int maxAmount = 500 ;
        int issuesNumber ;

        List<TicketInfo> ticketInfoList = new ArrayList<>() ;

        JSONRetriever jsonRetriever = new JSONRetriever() ;
        do {
            String urlString = urlBuilder.completeUrl(startPoint, maxAmount, urlFirstPart) ;
            URI uri = new URI(urlString) ;
            URL url = uri.toURL() ;
            Logger.getGlobal().log(Level.INFO, "\nRetrieve From: {0}", urlString) ;

            String jsonString = jsonRetriever.getJsonString(url) ;
            JSONObject jsonObject = new JSONObject(jsonString) ;
            JSONArray jsonIssueArray = jsonObject.getJSONArray("issues") ;

            ticketInfoList.addAll(parseIssuesArray(jsonIssueArray, versionInfoList)) ;

            issuesNumber = jsonIssueArray.length() ;
            startPoint = startPoint + maxAmount ;
        } while (issuesNumber != 0) ;

        return ticketInfoList ;
    }

    private List<TicketInfo> parseIssuesArray(JSONArray jsonArray, List<VersionInfo> versionInfoList) {

        List<TicketInfo> ticketInfoList = new ArrayList<>() ;
        for (int i = 0 ; i < jsonArray.length() ; i++) {
            JSONObject jsonIssue = jsonArray.getJSONObject(i) ;
            TicketInfo ticketInfo = parseIssue(jsonIssue, versionInfoList) ;
            ticketInfoList.add(ticketInfo);
        }

        return ticketInfoList ;
    }

    private TicketInfo parseIssue(JSONObject jsonIssue, List<VersionInfo> versionInfoList) {
        String ticketId = jsonIssue.get("key").toString() ;

        JSONObject jsonIssueFieldsObject = jsonIssue.getJSONObject("fields") ;

        TicketInfo ticketInfo = new TicketInfo(ticketId) ;
        if (jsonIssueFieldsObject.has("created")) {
            String stringDate = jsonIssueFieldsObject.getString("created") ;
            LocalDate createdDate = LocalDate.parse(stringDate.substring(0,10)) ;

            ticketInfo.setCreateDate(createdDate);
            VersionInfo openingVersion = computeVersionAfterDate(createdDate, versionInfoList) ;
            ticketInfo.setOpeningVersion(openingVersion);
        }

        if (jsonIssueFieldsObject.has("versions")) {
            JSONArray affectedVersionJsonArray = jsonIssueFieldsObject.getJSONArray("versions") ;
            List<VersionInfo> affectedVersionsList = parseAffectedVersions(affectedVersionJsonArray, versionInfoList) ;

            if (!affectedVersionsList.isEmpty()) {
                ticketInfo.setAffectedVersionList(affectedVersionsList);
                ticketInfo.setInjectedVersion(affectedVersionsList.get(0));
            }
        }

        if (jsonIssueFieldsObject.has("resolutiondate")) {
            String stringDate = jsonIssueFieldsObject.getString("resolutiondate") ;
            LocalDate resolutionDate = LocalDate.parse(stringDate.substring(0,10)) ;

            ticketInfo.setResolutionDate(resolutionDate);
            VersionInfo fixVersion = computeVersionAfterDate(resolutionDate, versionInfoList) ;
            ticketInfo.setFixVersion(fixVersion);
        }

        return ticketInfo ;

    }

    private VersionInfo computeVersionAfterDate(LocalDate date, List<VersionInfo> versionInfoList) {
        for (VersionInfo versionInfo : versionInfoList) {
            if (versionInfo.getVersionDate().isAfter(date)) {
                return versionInfo ;
            }
        }
        return null ;
    }

    private List<VersionInfo> parseAffectedVersions(JSONArray affectedVersionJsonArray, List<VersionInfo> versionInfoList) {
        List<VersionInfo> affectedVersionList = new ArrayList<>() ;
        for (int i = 0 ; i < affectedVersionJsonArray.length() ; i++) {
            JSONObject versionJsonObject = affectedVersionJsonArray.getJSONObject(i) ;
            for (VersionInfo versionInfo : versionInfoList) {
                if (versionInfo.getVersionId().compareTo(versionJsonObject.getString("id")) == 0) {
                    affectedVersionList.add(versionInfo) ;
                    break ;
                }
            }
        }
        affectedVersionList.sort(Comparator.comparing(VersionInfo::getVersionDate));

        return affectedVersionList ;
    }
}
