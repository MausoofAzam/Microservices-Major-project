package com.dc.service.impl;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dc.binding.CoResponse;
import com.dc.entity.CitizenAppEntity;
import com.dc.entity.CoTriggerEntity;
import com.dc.entity.DcCaseEntity;
import com.dc.entity.EligDetailsEntity;
import com.dc.repository.CitizenAppRepository;
import com.dc.repository.CoTriggerRepository;
import com.dc.repository.DcCaseRepo;
import com.dc.repository.EligDtlsRepository;
import com.dc.service.CoService;
import com.dc.utils.EmailUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class CoServiceImpl implements CoService {

	@Autowired
	private CoTriggerRepository triggerRepository;

	@Autowired
	private EligDtlsRepository dtlsRepository;

	@Autowired
	private CitizenAppRepository appRepository;

	@Autowired
	private DcCaseRepo caseRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public CoResponse processPendingTriggers() {

		Long failed = 0l;
		Long success = 0l;

		CoResponse response = new CoResponse();

		CitizenAppEntity appEntity = null;
		// fetch all pending trigger

		List<CoTriggerEntity> pendingTrgs = triggerRepository.findByTrgStatus("Pending");

		response.setTatalTriggers(Long.valueOf(pendingTrgs.size()));
		// to perform the execution quickly we use multithreading
		// here multithreading concept we are using with Executor, earlier we were using
		// single thread so
		// exection time was large like it takes around 10 minutes to send 5000 reposrt,
		// but when we use
		// executor then it sent report within 2 minutes thats why we use
		// multithreading.
		ExecutorService executorService = Executors.newFixedThreadPool(10); // threadpool: collection of threads

		ExecutorCompletionService<Object> pool = new ExecutorCompletionService<>(executorService);

		// process each pending trigger

		for (CoTriggerEntity entity : pendingTrgs) {
			pool.submit(new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					try {
						processTrigger(response, entity);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
			});
		}
		response.setSuccTruggers(success);
		response.setFailedTriggers(failed);
		return response;
	}

	private CitizenAppEntity processTrigger(CoResponse response, CoTriggerEntity entity) throws IOException {
		CitizenAppEntity appEntity = null;
		// gget eligibility data based on casenumber
		EligDetailsEntity elig = dtlsRepository.findByCaseNum(entity.getCaseNum());

		// get citizen data based on case number
		Optional<DcCaseEntity> findById = caseRepo.findById(entity.getCaseNum());
		if (findById.isPresent()) {
			DcCaseEntity dcCaseEntity = findById.get();
			Integer appId = dcCaseEntity.getAppId();
			Optional<CitizenAppEntity> appEntityOptional = appRepository.findById(appId);

			if (appEntityOptional.isPresent()) {
				appEntity = appEntityOptional.get();
			}
		}

		// generate pdf with elig details

		generateAndSendPdf(elig, appEntity);

		// send pdf to citizen mail
		// store the pdf & update trigger as complete

		// return summary
		return appEntity;
	}

	private void generateAndSendPdf(EligDetailsEntity eligData, CitizenAppEntity appEntity) throws IOException {

		// creating document pdf
		Document document = new Document(PageSize.A4);

		File file = new File(eligData.getCaseNum() + ".pdf");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfWriter.getInstance(document, fos);

		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

		font.setSize(18);
		font.setColor(Color.darkGray);

		Paragraph p = new Paragraph("Eligibility Report", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);
		// five cells in the pdf row
		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 1.5f, 3.0f, 1.5f, 2.0f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Citizen Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Status", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Start Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan End Date", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase(" Benfit Amount", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Denial Reason", font));
		table.addCell(cell);

		// when the loop is executed next row will generate
		table.addCell(appEntity.getFullName());
		table.addCell(eligData.getPlanName());
		table.addCell(eligData.getPlanStatus());
		table.addCell(eligData.getPlanStartDt() + "");
		table.addCell(eligData.getPlanEndDt() + "");
		table.addCell(eligData.getBenefitAmt() + "");
		table.addCell(eligData.getDenialReason() + "");

		document.add(table);
		document.close();

		String subject = "HIS Eligibility Info";
		String body = "HIS Eligibility Info";

		emailUtils.sendEmail(appEntity.getEmail(), subject, body, file);

		updateTrigger(eligData.getCaseNum(), file);
	}

	private void updateTrigger(Long caseNum, File file) throws IOException {
		CoTriggerEntity coEntity = triggerRepository.findByCaseNum(caseNum);

		byte[] arr = new byte[(byte) file.length()];

		FileInputStream fis = new FileInputStream(file);
		fis.read(arr);

		coEntity.setCoPdf(arr);

		coEntity.setTrgStatus("Completed");
		triggerRepository.save(coEntity);

		fis.close();
	}
}
