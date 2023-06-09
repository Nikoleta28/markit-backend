package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springproject.markit.models.AssignmentMark;
import springproject.markit.repositories.AssignmentMarkRepository;


import javax.transaction.Transactional;
import java.util.List;

@Service
public class AssignmentMarkService {

    @Autowired
    private AssignmentMarkRepository assignmentMarkRepository;


    public List<AssignmentMark> getAssignmentMarksList() { return assignmentMarkRepository.findAll();}

    public void createAssignmentMark(AssignmentMark assignmentMark) {
        assignmentMarkRepository.save(assignmentMark);
    }

    public void deleteAssignmentMark(Long id) { assignmentMarkRepository.deleteById(id);}
    @Transactional
    public void updateAssignmentMark(Long id, Float mark) {

        AssignmentMark assignmentMark = assignmentMarkRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Assignment mark with id "+ id +" doesn't exist!"
        ));

        assignmentMark.setMark(mark);

        assignmentMarkRepository.save(assignmentMark);

//        return assignmentMark;
    }
}
