package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.models.Assignment;
import springproject.markit.models.Course;
import springproject.markit.models.Syllabus;
import springproject.markit.repositories.CourseRepository;
import springproject.markit.repositories.SyllabusRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SyllabusService {

    @Autowired
    private SyllabusRepository syllabusRepository;

    @Autowired
    private CourseRepository courseRepository;


    public List<Syllabus> getSyllabuses() {
        return syllabusRepository.findAll();
    }


    public Syllabus updateSyllabus(Long id, String name, Integer difficulty) {

        Syllabus syllabus = syllabusRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Syllabus with id "+ id +" doesn't exist!"
        ));

        if(name !=null && name.length()> 0 && !Objects.equals(syllabus.getName(),name)){
            syllabus.setName(name);
        }

        if( difficulty instanceof Integer){
            syllabus.setDifficulty(difficulty);
        }

        syllabusRepository.save(syllabus);

        return syllabus;
    }


    public void createSyllabus(Syllabus syllabus, Course course) {

        Optional<Syllabus> syllabusOptional =  syllabusRepository.findSyllabusByName(syllabus.getName());

        if(course == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
        }
        else{
            if(syllabusOptional.isPresent()) {
                throw new IllegalStateException("name taken");
            }else{

                course.addSyllabus(syllabus);
                syllabus.setCourse(course);

                syllabusRepository.save(syllabus);
                courseRepository.save(course);


            }
        }
    }
}
