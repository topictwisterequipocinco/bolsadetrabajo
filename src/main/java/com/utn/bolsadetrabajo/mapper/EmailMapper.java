package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.response.ResponseEmailDto;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public ResponseEmailDto toModelEmailCreatePerson(Person person, String path, String welcome) {
        String url = ""+path+"/"+person.getSubscriber().getUsername()+"/"+person.getSubscriber().getVerificationCode()+"";

        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(person.getOficialName() + " " + person.getLastName())
                .identification(person.getIdentification())
                .email(person.getSubscriber().getUsername())
                .phone(person.getPhoneNumber())
                .message(welcome)
                .url(url)
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailCreateApplicant(Applicant applicant, String pathBase, String emailWelcome) {
        String url = ""+pathBase+"/"+applicant.getSubscriber().getUsername()+"/"+applicant.getSubscriber().getVerificationCode()+"";

        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(applicant.getOficialName() + " " + applicant.getLastName())
                .identification(applicant.getIdentification())
                .email(applicant.getSubscriber().getUsername())
                .phone(applicant.getPhoneNumber())
                .message(emailWelcome)
                .url(url)
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailCreatePublisher(Publisher publisher, String pathBase, String emailWelcome) {
        String url = ""+pathBase+"/"+publisher.getSubscriber().getUsername()+"/"+publisher.getSubscriber().getVerificationCode()+"";

        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(publisher.getOficialName() + " " + publisher.getLastName())
                .identification(publisher.getIdentification())
                .email(publisher.getSubscriber().getUsername())
                .phone(publisher.getPhoneNumber())
                .message(emailWelcome)
                .url(url)
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailJobOffer(JobOffer jobOffer, Publisher person, String publicated, String day) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(person.getOficialName() + " " + person.getLastName())
                .identification(person.getIdentification())
                .email(person.getSubscriber().getUsername())
                .message(publicated + " " + jobOffer.getTitle() + " " +
                jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailPostulateApplicantJobOffer(JobOffer jobOffer, Applicant applicant, String postulate, String day) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(applicant.getOficialName() + " " + applicant.getLastName())
                .email(applicant.getSubscriber().getUsername())
                .message(postulate + " " + jobOffer.getTitle() + " " +
                jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailDto toModelEmailPostulatedJobOfferByPublisher(JobOffer jobOffer, Applicant applicant, String applicantPostulate, String day) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(applicant.getOficialName() + " " + applicant.getLastName())
                .email(jobOffer.getPublisher().getSubscriber().getUsername())
                .message(applicantPostulate + " " + jobOffer.getTitle() + " " +
                jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailDto toSendEmailJobOfferReview(JobOffer jobOffer, String stateReview) {
        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(jobOffer.getPublisher().getOficialName() + " " + jobOffer.getPublisher().getLastName())
                .email(jobOffer.getPublisher().getSubscriber().getUsername())
                .message(stateReview + " " + " con el tìtulo : " + " " + jobOffer.getTitle())
                .build();
        return res;
    }



}
