package com.snort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.snort.entity.EligibilityDetails;
import com.snort.repository.EligibilityDetailsRepo;

public class AppRunner implements ApplicationRunner{

	@Autowired
	private EligibilityDetailsRepo detailsRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		EligibilityDetails entity1 = new EligibilityDetails();
		entity1.setEligId(1);
		entity1.setName("Azam");
		entity1.setEmail("azam@gmail.com");
		entity1.setMobile(5454564L);
		entity1.setGender('M');
		entity1.setSsn(767665655654L);
		entity1.setPlanName("SMAW");
		entity1.setPlanStatus("Approved");
		
		detailsRepo.save(entity1);
		
		EligibilityDetails entity2 = new EligibilityDetails();
		entity2.setEligId(2);
		entity2.setName("Rashid");
		entity2.setEmail("rashid@gmail.com");
		entity2.setMobile(54545564L);
		entity2.setGender('M');
		entity2.setSsn(7666665654L);
		entity2.setPlanName("SCAW");
		entity2.setPlanStatus("Rejected");
		
		detailsRepo.save(entity2);
		
		EligibilityDetails entity3 = new EligibilityDetails();
		entity3.setEligId(3);
		entity3.setName("Madhan");
		entity3.setEmail("madhan@gmail.com");
		entity3.setMobile(545544564L);
		entity3.setGender('M');
		entity3.setSsn(76765643354L);
		entity3.setPlanName("Medicated");
		entity3.setPlanStatus("Denied");
		
		detailsRepo.save(entity3);
	}

}
