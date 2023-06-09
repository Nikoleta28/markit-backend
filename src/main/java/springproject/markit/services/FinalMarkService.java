package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springproject.markit.models.FinalMark;
import springproject.markit.repositories.FinalMarkRepository;

import java.util.List;

@Service
public class FinalMarkService {

    @Autowired
    private FinalMarkRepository finalMarkRepository;


    public List<FinalMark> getFinalMarksList() { return finalMarkRepository.findAll();}

//    public void createFinalMark(Long studentId, Long courseId, FinalMark finalMark) {
//
////        //find st. and course by id
////        student.addFinalMark(finalMark);
////        finalMark.setStudent(student);
////        course.addFinalMark(finalMark);
////        finalMark.setCourse(course);
//
//        finalMarkRepository.save(finalMark);
//    }

    public void deleteFinalMark(Long id) { finalMarkRepository.deleteById(id);}

    @Transactional
    public FinalMark updateFinalMark(Long id, Float mark) {

        FinalMark finalMark = finalMarkRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Final mark with id "+ id +" doesn't exist!"
        ));

        finalMark.setMark(mark);

        finalMarkRepository.save(finalMark);

        return finalMark;

    }

}
