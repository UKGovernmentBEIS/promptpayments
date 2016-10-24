package models;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.TimeZone;



@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class ReportFilingModelValidationImpl implements ReportFilingModelValidation {
    private final ReportFilingModel model;
    private final Calendar utcNow;

    private final String message_required = "This field is required";
    private final String message_nonnegative = "This should be a non-negative number";
    private final String message_percentagebounds = "This should be a number between 0 and 100";
    private final String message_upperpercentagebounds = "This cannot exceed 100%";
    private final String message_sumto100 = "These fields should sum to 100%";
    private final String message_invaliddate = "This date is invalid";
    private final String message_future = "Reporting period cannot cover the future";
    private final String message_startbeforeend = "End date should be after start date";

    public ReportFilingModelValidationImpl(ReportFilingModel model, Calendar utcNow) {
        this.model = model;
        if (!utcNow.getTimeZone().equals(TimeZone.getTimeZone("UTC"))) {
            throw new InvalidParameterException("utcNow is not UTC");
        }
        this.utcNow = utcNow;
    }

    @Override
    public boolean isValid() {
        return
                validateAverageTimeToPay().isOk() &&
                        validatePercentInvoicesPaidBeyondAgreedTerms().isOk() &&
                        validatePercentInvoicesWithin30Days().isOk() &&
                        validatePercentInvoicesWithin60Days().isOk() &&
                        validateTimePercentages().isOk() &&
                        validateStartDate().isOk() &&
                        validateEndDate().isOk() &&
                        validatePaymentTerms().isOk() &&
                        validateDisputeResolution().isOk() &&
                        validatePaymentCodes().isOk() &&
                        validateOfferEInvoicing().isOk() &&
                        validateOfferSupplyChainFinance().isOk() &&
                        validateRetentionChargesInPast().isOk() &&
                        validateRetentionChargesInPolicy().isOk();
    }

    @Override
    public FieldValidation validateAverageTimeToPay() {
        if (model.getAverageTimeToPay() == null || model.getAverageTimeToPay().equals("")) return FieldValidation.fail(message_required);
        if (model.getAverageTimeToPayAsDecimal() == null) return FieldValidation.fail(message_nonnegative);
        if (model.getAverageTimeToPayAsDecimal().doubleValue() < 0) return FieldValidation.fail(message_nonnegative);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validatePercentInvoicesPaidBeyondAgreedTerms() {
        if (model.getPercentInvoicesPaidBeyondAgreedTerms() == null || model.getPercentInvoicesPaidBeyondAgreedTerms().equals("")) return FieldValidation.fail(message_required);
        if (model.getPercentInvoicesPaidBeyondAgreedTermsAsDecimal() == null) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesPaidBeyondAgreedTermsAsDecimal().doubleValue() < 0) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesPaidBeyondAgreedTermsAsDecimal().doubleValue() > 100) return FieldValidation.fail(message_upperpercentagebounds);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validatePercentInvoicesWithin30Days() {
        if (model.getPercentInvoicesWithin30Days() == null || model.getPercentInvoicesWithin30Days().equals("")) return FieldValidation.fail(message_required);
        if (model.getPercentInvoicesWithin30DaysAsDecimal() == null) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesWithin30DaysAsDecimal().doubleValue() < 0) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesWithin30DaysAsDecimal().doubleValue() > 100) return FieldValidation.fail(message_upperpercentagebounds);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validatePercentInvoicesWithin60Days() {
        if (model.getPercentInvoicesWithin60Days() == null || model.getPercentInvoicesWithin60Days().equals("")) return FieldValidation.fail(message_required);
        if (model.getPercentInvoicesWithin60DaysAsDecimal() == null) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesWithin60DaysAsDecimal().doubleValue() < 0) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesWithin60DaysAsDecimal().doubleValue() > 100) return FieldValidation.fail(message_upperpercentagebounds);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validatePercentInvoicesBeyond60Days() {
        if (model.getPercentInvoicesBeyond60Days() == null || model.getPercentInvoicesBeyond60Days().equals("")) return FieldValidation.fail(message_required);
        if (model.getPercentInvoicesBeyond60DaysAsDecimal() == null) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesBeyond60DaysAsDecimal().doubleValue() < 0) return FieldValidation.fail(message_percentagebounds);
        if (model.getPercentInvoicesBeyond60DaysAsDecimal().doubleValue() > 100) return FieldValidation.fail(message_upperpercentagebounds);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validateTimePercentages() {
        // only apply if the inner fields are valid
        if (!validatePercentInvoicesWithin30Days().isOk() || !validatePercentInvoicesWithin60Days().isOk() || !validatePercentInvoicesBeyond60Days().isOk()) {
            return FieldValidation.ok();
        }
        Double totalAbs = Math.abs(100.0
                - model.getPercentInvoicesWithin30DaysAsDecimal().doubleValue()
                - model.getPercentInvoicesWithin60DaysAsDecimal().doubleValue()
                - model.getPercentInvoicesBeyond60DaysAsDecimal().doubleValue());
        if (totalAbs > 0.001) return FieldValidation.fail(message_sumto100);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validateStartDate() {
        if (model.getStartDate() == null) return FieldValidation.fail(message_invaliddate);
        if (model.getStartDate().getTime().getTime() > utcNow.getTime().getTime())
            return FieldValidation.fail(message_future);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validateEndDate() {
        if (model.getEndDate() == null) return FieldValidation.fail(message_invaliddate);
        if (model.getEndDate().getTime().getTime() > utcNow.getTime().getTime())
            return FieldValidation.fail(message_future);
        if (validateStartDate().isOk() && model.getEndDate().getTime().getTime() <= model.getStartDate().getTime().getTime())
            return FieldValidation.fail(message_startbeforeend);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validatePaymentTerms() {
        if (model.getPaymentTerms() == null || model.getPaymentTerms().equals("")) return FieldValidation.fail(message_required);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validateDisputeResolution() {
        if (model.getDisputeResolution() == null || model.getDisputeResolution().equals(""))
            return FieldValidation.fail(message_required);
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validatePaymentCodes() {
        return FieldValidation.ok();
    }

    @Override
    public FieldValidation validateOfferEInvoicing() {
        return model.isOfferEInvoicing() == null ? FieldValidation.fail(message_required) : FieldValidation.ok();
    }

    @Override
    public FieldValidation validateOfferSupplyChainFinance() {
        return model.isOfferSupplyChainFinance() == null ? FieldValidation.fail(message_required) : FieldValidation.ok();
    }

    @Override
    public FieldValidation validateRetentionChargesInPolicy() {
        return model.isRetentionChargesInPolicy() == null ? FieldValidation.fail(message_required) : FieldValidation.ok();
    }

    @Override
    public FieldValidation validateRetentionChargesInPast() {
        return model.isRetentionChargesInPast() == null ? FieldValidation.fail(message_required) : FieldValidation.ok();
    }
}