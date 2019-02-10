package net.euphalys.core.api.report;

import net.euphalys.api.report.IReport;

/**
 * @author Dinnerwolph
 */

public class Report implements IReport {

    int reportId, reportedBy, target;
    String message;

    public Report(int reportId, int reportedBy, int target, String message) {
        this.reportId = reportId;
        this.reportedBy = reportedBy;
        this.target = target;
        this.message = message;
    }

    public int getReportId() {
        return reportId;
    }

    public int getReportedBy() {
        return reportedBy;
    }

    @Override
    public int getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }
}
