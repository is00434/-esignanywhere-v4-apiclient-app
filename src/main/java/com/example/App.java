package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import osplus.esignanywhere.v4.api.EnvelopeApi;
import osplus.esignanywhere.v4.api.SspFileApi;
import osplus.esignanywhere.v4.api.VersionApi;
import osplus.esignanywhere.v4.invoker.ApiClient;
import osplus.esignanywhere.v4.model.AuditingToolsConfiguration;
import osplus.esignanywhere.v4.model.ClientAction;
import osplus.esignanywhere.v4.model.EnvelopeSendModel;
import osplus.esignanywhere.v4.model.EnvelopeStatus;
import osplus.esignanywhere.v4.model.EnvelopeStatus.StatusEnum;
import osplus.esignanywhere.v4.model.FinishAction;
import osplus.esignanywhere.v4.model.FinishedDocument;
import osplus.esignanywhere.v4.model.FlowApiResult;
import osplus.esignanywhere.v4.model.GeneralPolicies;
import osplus.esignanywhere.v4.model.KeyValuePair;
import osplus.esignanywhere.v4.model.Policy;
import osplus.esignanywhere.v4.model.Position;
import osplus.esignanywhere.v4.model.ReceiverInformation;
import osplus.esignanywhere.v4.model.ResourceUris;
import osplus.esignanywhere.v4.model.SendEnvelopeDescription;
import osplus.esignanywhere.v4.model.SendEnvelopeDocumentOption;
import osplus.esignanywhere.v4.model.SendEnvelopeRecipient;
import osplus.esignanywhere.v4.model.SendEnvelopeResult;
import osplus.esignanywhere.v4.model.SendEnvelopeStep;
import osplus.esignanywhere.v4.model.SendEnvelopeStep.RecipientTypeEnum;
import osplus.esignanywhere.v4.model.SenderInformation;
import osplus.esignanywhere.v4.model.SigTypeClick2Sign;
import osplus.esignanywhere.v4.model.Signature;
import osplus.esignanywhere.v4.model.Size;
import osplus.esignanywhere.v4.model.StampImprintConfiguration;
import osplus.esignanywhere.v4.model.UploadSspFileResult;
import osplus.esignanywhere.v4.model.UserInformation;
import osplus.esignanywhere.v4.model.ViewerPreferences;
import osplus.esignanywhere.v4.model.VisibleAreaOptions;
import osplus.esignanywhere.v4.model.WorkstepConfiguration;
import osplus.esignanywhere.v4.model.WorkstepTasks;
import osplus.esignanywhere.v4.model.WorkstepTasks.PictureAnnotationColorDepthEnum;
import osplus.esignanywhere.v4.model.WorkstepTasks.PositionUnitsEnum;
import osplus.esignanywhere.v4.model.WorkstepTasks.ReferenceCornerEnum;
import osplus.esignanywhere.v4.model.WorkstepTasks.SequenceModeEnum;

/**
 * Hello world!
 *
 */
public class App implements Runnable {
    private final String[] args;

    private App(final String[] args) {
        this.args = args;
    }

    private ApiClient apiClient = null;

    private ApiClient getApiClient() {
        if (apiClient == null) {
            apiClient = new ApiClient();
            apiClient.setDebugging(false);
            apiClient.setBasePath("https://saas.esignanywhere.net/Api");
            apiClient.addDefaultHeader("ApiToken", "mh24gps8u1xkqaxp4l34sgmm6qnxn438b1acq510fjk5vjqtdqilzgm7utgvrq7a");
        }
        return apiClient;
    }

    private SspFileApi sspFileApi = null;

    private SspFileApi getSspFileApi() {
        if (sspFileApi == null) {
            sspFileApi = new SspFileApi(getApiClient());
        }
        return sspFileApi;
    }

    private VersionApi versionApi = null;

    private VersionApi getVersionApi() {
        if (versionApi == null) {
            versionApi = new VersionApi(getApiClient());
        }
        return versionApi;
    }

    private EnvelopeApi envelopeApi = null;

    private EnvelopeApi getEnvelopeApi() {
        if (envelopeApi == null) {
            envelopeApi = new EnvelopeApi(getApiClient());
        }
        return envelopeApi;
    }

    private File getTestPdf() throws URISyntaxException {
        final URL resource = getClass().getClassLoader().getResource("Test.pdf");
        return new File(resource.toURI());
    }

    private byte[] getTestPdfAsByteArray() throws URISyntaxException, IOException {
        final URL resource = getClass().getClassLoader().getResource("Test.pdf");
        return Files.readAllBytes(Paths.get(resource.toURI()));
    }

    @Override
    public void run() {
        try {
            final FlowApiResult version = getVersionApi().versionGet();
            System.out.println("Version: " + version);

            final UploadSspFileResult sspUploadFileResult1 = getSspFileApi().sspFileUploadTemporary(getTestPdf());
            System.out.println("UploadSspFileResult1: " + sspUploadFileResult1);

            final UploadSspFileResult sspUploadFileResult2 = getSspFileApi().sspFileUploadTemporaryFromByteArray("Test2.pdf", getTestPdfAsByteArray());
            System.out.println("UploadSspFileResult2: " + sspUploadFileResult2);

            final UploadSspFileResult sspUploadFileResult3 = getSspFileApi().sspFileUploadTemporaryFromByteArray("Test3.pdf", getTestPdfAsByteArray());
            System.out.println("UploadSspFileResult3: " + sspUploadFileResult3);

            final SendEnvelopeRecipient sendEnvelopeRecipient1 = new SendEnvelopeRecipient();
            sendEnvelopeRecipient1.email("gerhard.ziesler@gmail.com").firstName("Gerhard").lastName("Ziesler")
                    .languageCode("de").emailBodyExtra("").disableEmail(false).addAndroidAppLink(false)
                    .addIosAppLink(false).addWindowsAppLink(false).allowDelegation(true)
                    .skipExternalDataValidation(false);

            final SendEnvelopeRecipient sendEnvelopeRecipient2 = new SendEnvelopeRecipient();
            sendEnvelopeRecipient2.email("gerhard.ziesler@gmail.com").firstName("Gerhard").lastName("Ziesler")
                    .languageCode("de").emailBodyExtra("").disableEmail(false).addAndroidAppLink(false)
                    .addIosAppLink(false).addWindowsAppLink(false).allowDelegation(false)
                    .skipExternalDataValidation(false);

            final ClientAction clientAction = new ClientAction();
            clientAction.removeDocumentFromRecentDocumentList(false).callClientActionOnlyAfterSuccessfulSync(false)
                    .clientName("SIGNificant SignAnywhere").closeApp(true).action("https://www.esignanywhere.net");

            final FinishAction finishAction = new FinishAction();
            finishAction.addClientActionsItem(clientAction);

            final UserInformation senderUserInformation = new UserInformation();
            senderUserInformation.firstName("Gerhard").lastName("Ziesler").email("gerhard.ziesler@gmail.com");

            final UserInformation receiveUserInformation = new UserInformation();
            receiveUserInformation.firstName("Gerhard").lastName("Ziesler").email("gerhard.ziesler@extern.f-i.de");

            final ReceiverInformation receiverInformation = new ReceiverInformation();
            receiverInformation.userInformation(receiveUserInformation);

            final SenderInformation senderInformation = new SenderInformation();
            senderInformation.userInformation(senderUserInformation);

            final VisibleAreaOptions visibleAreaOptions = new VisibleAreaOptions();
            visibleAreaOptions.allowedDomain("*").enabled(true);

            final ViewerPreferences viewerPreferences = new ViewerPreferences();
            viewerPreferences.finishWorkstepOnOpen(false).visibleAreaOptions(visibleAreaOptions);

            final ResourceUris resourceUris = new ResourceUris();
            resourceUris.delegationUri("https://demo.xyzmo.com/Resource/Delegate");

            final AuditingToolsConfiguration auditingToolsConfiguration = new AuditingToolsConfiguration();
            auditingToolsConfiguration.writeAuditTrail(false);

            final GeneralPolicies generalPolicies = new GeneralPolicies();
            generalPolicies.allowSaveDocument(true).allowSaveAuditTrail(true).allowRotatingPages(false)
                    .allowEmailDocument(true).allowPrintDocument(true).allowFinishWorkstep(true)
                    .allowRejectWorkstep(true).allowRejectWorkstepDelegation(true).allowUndoLastAction(true)
                    .allowAdhocPdfAttachments(false).allowAdhocSignatures(false).allowAdhocStampings(false)
                    .allowAdhocFreeHandAnnotations(false).allowAdhocTypewriterAnnotations(false)
                    .allowAdhocPictureAnnotations(false).allowAdhocPdfPageAppending(false);

            final StampImprintConfiguration stampImprintConfiguration = new StampImprintConfiguration();
            stampImprintConfiguration.displayExtraInformation(true).displayEmail(true).displayIp(true).displayName(true)
                    .displaySignatureDate(true).fontFamily("Times New Roman").fontSize(11.0);

            final SigTypeClick2Sign allowedSignatureType = new SigTypeClick2Sign();
            allowedSignatureType.stampImprintConfiguration(stampImprintConfiguration)
                    .id("89a057d6-8e35-410f-84d3-e26cf93da175").discriminatorType("SigTypeClick2Sign").preferred(true);

            final Signature signature = new Signature();
            signature.positionPage(1).position(new Position().positionX(68.0).positionY(68.0))
                    .size(new Size().height(79.0).width(190.0))
                    .addAdditionalParametersItem(new KeyValuePair().key("enabled").value("1"))
                    .addAdditionalParametersItem(new KeyValuePair().key("positioning").value("onPage"))
                    .addAdditionalParametersItem(new KeyValuePair().key("reg").value("1"))
                    .addAdditionalParametersItem(new KeyValuePair().key("fd").value(""))
                    .addAdditionalParametersItem(new KeyValuePair().key("fd_dateformat").value("dd-MM-yyyy HH:mm:ss"))
                    .addAdditionalParametersItem(new KeyValuePair().key("fd_timezone").value("datetimeutc"))
                    .addAllowedSignatureTypesItem(allowedSignatureType).useTimestamp(false).isRequired(true)
                    .id("1#XyzmoDuplicateIdSeperator#Signature_5430c3b6-cc0d-26b1-86ea-3ba909701349").displayName("")
                    .docRefNumber(2).discriminatorType("Signature");

            final WorkstepTasks workstepTasks = new WorkstepTasks();
            workstepTasks.pictureAnnotationMinResolution(0).pictureAnnotationMaxResolution(0)
                    .pictureAnnotationColorDepth(PictureAnnotationColorDepthEnum.COLOR16M)
                    .sequenceMode(SequenceModeEnum.NOSEQUENCEENFORCED).positionUnits(PositionUnitsEnum.PDFUNITS)
                    .referenceCorner(ReferenceCornerEnum.LOWER_LEFT).addTasksItem(signature);

            final Policy policy = new Policy();
            policy.generalPolicies(generalPolicies).workstepTasks(workstepTasks);

            final WorkstepConfiguration workstepConfiguration = new WorkstepConfiguration();
            workstepConfiguration.workstepLabel("Test").smallTextZoomFactorPercent(100).finishAction(finishAction)
                    .receiverInformation(receiverInformation).senderInformation(senderInformation)
                    .viewerPreferences(viewerPreferences).resourceUris(resourceUris)
                    .auditingToolsConfiguration(auditingToolsConfiguration).policy(policy);

            final SendEnvelopeDocumentOption documentOptionsItem = new SendEnvelopeDocumentOption();
            documentOptionsItem.documentReference("1").isHidden(false);

            final SendEnvelopeStep sendEnvelopeStep1 = new SendEnvelopeStep();
            sendEnvelopeStep1.orderIndex(1).addRecipientsItem(sendEnvelopeRecipient1).emailBodyExtra("")
                    .recipientType(RecipientTypeEnum.SIGNER).workstepConfiguration(workstepConfiguration)
                    .addDocumentOptionsItem(documentOptionsItem).useDefaultAgreements(true);

            final SendEnvelopeStep sendEnvelopeStep2 = new SendEnvelopeStep();
            sendEnvelopeStep2.orderIndex(2).addRecipientsItem(sendEnvelopeRecipient2).emailBodyExtra("")
                    .recipientType(RecipientTypeEnum.CC).useDefaultAgreements(false);

            final SendEnvelopeDescription sendEnvelopeDescription = new SendEnvelopeDescription();
            sendEnvelopeDescription.name("'Super wichtig'")
                    .emailSubject("Bitte unterzeichnen Sie das beiliegende Dokument")
                    .emailBody(
                            "Herr #RecipientFirstName# #RecipientLastName#\n\n#PersonalMessage#\n\nBitte unterzeichnen Sie das beiliegende Dokument #EnvelopeName#\n\nDie Frist zur Unterzeichnung des Dokuments läuft am #ExpirationDate# ab")
                    .displayedEmailSender("").enableReminders(true).firstReminderDayAmount(5)
                    .recurrentReminderDayAmount(3).beforeExpirationDayAmount(3).daysUntilExpire(28).callbackUrl("")
                    .statusUpdateCallbackUrl("").addStepsItem(sendEnvelopeStep1).addStepsItem(sendEnvelopeStep2)
                    .attachSignedDocumentsToEnvelopeLog(false);

            final EnvelopeSendModel envelopeSendModel = new EnvelopeSendModel();
            envelopeSendModel.addSspFileIdsItem(sspUploadFileResult1.getSspFileId())
                    .addSspFileIdsItem(sspUploadFileResult2.getSspFileId())
                    .addSspFileIdsItem(sspUploadFileResult3.getSspFileId())
                    .sendEnvelopeDescription(sendEnvelopeDescription);

            final SendEnvelopeResult sendEnvelopeResult = getEnvelopeApi().envelopeSend(envelopeSendModel);
            System.out.println("SendEnvelopeResult: " + sendEnvelopeResult);
            
            EnvelopeStatus envelopeStatus = null;

            for (;;) {
                envelopeStatus = getEnvelopeApi().envelopeById(sendEnvelopeResult.getEnvelopeId());
                System.out.println("EnvelopeStatus: " + envelopeStatus.getStatus());
                if (envelopeStatus.getStatus() == StatusEnum.COMPLETED) {
                    break;
                }
                Thread.sleep(2000);
            }

            for (final FinishedDocument finishedDocument : envelopeStatus.getBulks().get(0).getFinishedDocuments()) {
                System.out.println("FinishedDocument: " + finishedDocument);
                final byte[] finishedDocumentContent = getEnvelopeApi().envelopeDownloadCompletedDocument(finishedDocument.getFlowDocumentId());
                Files.write(Paths.get(finishedDocument.getFileName()), finishedDocumentContent, StandardOpenOption.CREATE);
            }
        }  catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        new App(args).run();
    }
}
