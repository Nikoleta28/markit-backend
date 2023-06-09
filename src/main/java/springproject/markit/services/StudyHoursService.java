package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springproject.markit.models.StudyHours;
import springproject.markit.repositories.StudyHoursRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudyHoursService {
    @Autowired
    private StudyHoursRepository studyHoursRepository;

    public List<StudyHours> getStudyHours(){
        return studyHoursRepository.findAll();
    }

    public Optional<StudyHours> getStudyHoursById(Long id) {
        return studyHoursRepository.findById(id);
    }

    public void createStudyHours(StudyHours studyHours){

        studyHoursRepository.save(studyHours);
    }

    public void deleteStudyHours(Long id) {
        studyHoursRepository.deleteById(id);
    }

    @Transactional
    public StudyHours updateStudyHours(Long id,
                                       LocalDate start,
                                       Integer hours  ) {


        StudyHours studyHours = studyHoursRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "StudyHours with id "+ id +" doesn't exist!"
        ));

        if(start !=null ){
            studyHours.setStart(start);
        }

        if(hours !=null && hours>=0 ){
            studyHours.setHours(hours);
        }


        return studyHours;
    }

}


