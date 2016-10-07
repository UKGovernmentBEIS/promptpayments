package controllers;

import com.google.inject.Inject;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;
import play.twirl.api.Html;
import play.twirl.api.HtmlFormat;
import scala.collection.JavaConversions;
import utils.MockUtcTimeProvider;
import components.PagedList;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by daniel.rothig on 27/09/2016.
 *
 * Main controller
 */
public class VisualTest extends PageController {

    private Form<ReportFilingModel> reportForm;

    @Inject
    public VisualTest(FormFactory formFactory) {
        reportForm = formFactory.form(ReportFilingModel.class);
    }

    public Result index() {

        Calendar time = new MockUtcTimeProvider(2016,9,1).Now();
        ReportSummary healthyReportSummary = new ReportSummary(1, time);

        ReportModel healthyReportModel = new ReportModel(
                healthyReportSummary,
                new BigDecimal("31.00"),
                new BigDecimal("10.00"),
                new BigDecimal("80.00"),
                new BigDecimal("15.00"),
                new BigDecimal("5.00"),

                new MockUtcTimeProvider(2016,0,0).Now(),
                new MockUtcTimeProvider(2016,5,0).Now(),

                "User-supplied description of payment terms",
                "User-supplied description of dispute resolution",

                true,
                true,
                false,
                false,
                "Prompt Payment Code"
        );

        ReportModel emptyReportModel = new ReportModel(healthyReportSummary, null, null, null, null, null, new MockUtcTimeProvider(2016,0,1).Now(), new MockUtcTimeProvider(2016,5,30).Now(), null, null, false, false, false, false, null);
        CompanySummary healthyCompanySummary = new CompanySummary("Eigencode Ltd.", "123");

        CompanyModel healthyCompanyModel = new CompanyModel(healthyCompanySummary, new PagedList<>(Arrays.asList(healthyReportSummary, healthyReportSummary, healthyReportSummary), 6, 0, 3));

        ReportFilingModel newReportFilingModel = new ReportFilingModel();
        newReportFilingModel.setTargetCompanyCompaniesHouseIdentifier("123");

        ReportFilingModel completeReportFilingModel = getCompleteFilingModel();
        Html html = HtmlFormat.fill(JavaConversions.asScalaBuffer(Arrays.asList(

                views.html.Home.index.render(),
                views.html.Home.about.render(),
                views.html.Home.accessData.render(),

                views.html.Reports.report.render(healthyReportModel, healthyCompanySummary),
                views.html.Reports.report.render(emptyReportModel, healthyCompanySummary),

                views.html.Reports.searchstart.render(),
                views.html.Reports.search.render(),
                views.html.Reports.results.render("cod", new PagedList<>(Arrays.asList(healthyCompanySummary, healthyCompanySummary, healthyCompanySummary), 100, 0, 3)),
                views.html.Reports.company.render(healthyCompanyModel),

                views.html.FileReport.index.render(),
                views.html.FileReport.login.render(),
                views.html.FileReport.companies.render(new PagedList<>(Arrays.asList(healthyCompanySummary, healthyCompanySummary, healthyCompanySummary), 100, 3, 3)),
                views.html.FileReport.file.render(reportForm.fill(newReportFilingModel), healthyCompanySummary, new UiDate(time)),
                views.html.FileReport.file.render(reportForm.fill(completeReportFilingModel), healthyCompanySummary, new UiDate(time)),
                views.html.FileReport.review.render(reportForm.fill(completeReportFilingModel), healthyCompanySummary, new UiDate(time))

        )).toList());

        return ok(page(html));
    }

    private ReportFilingModel getCompleteFilingModel() {
        ReportFilingModel rfm = new ReportFilingModel();
        rfm.setTargetCompanyCompaniesHouseIdentifier("122");
        rfm.setAverageTimeToPay(31.0);
        rfm.setPercentInvoicesPaidBeyondAgreedTerms(10.0);
        rfm.setPercentInvoicesWithin30Days(80.0);
        rfm.setPercentInvoicesWithin60Days(15.0);
        rfm.setPercentInvoicesBeyond60Days( 5.0);

        rfm.setStartDate_year(2016);
        rfm.setStartDate_month(0);
        rfm.setStartDate_day(1);

        rfm.setEndDate_year(2016);
        rfm.setEndDate_month(5);
        rfm.setEndDate_day(29);

        rfm.setPaymentTerms("Payment terms");
        rfm.setDisputeResolution("Dispute resolution");
        rfm.setPaymentCodes("Payment codes");

        rfm.setOfferEInvoicing(true);
        rfm.setOfferSupplyChainFinance(true);
        rfm.setRetentionChargesInPolicy(false);
        rfm.setRetentionChargesInPast(false);

        return rfm;
    }
}

