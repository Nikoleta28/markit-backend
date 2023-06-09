//package springproject.markit;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springproject.markit.models.*;
//import springproject.markit.repositories.*;
//
//
//import java.time.LocalDate;
//import java.util.Arrays;
//
//@Configuration
//public class StudentConfig {
//
//    @Bean
//    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
//                                        SecretariatRepository secretariatRepository,
//                                        CourseRepository courseRepository,
//                                        ProfessorRepository professorRepository,
//                                        AssignmentRepository assignmentRepository,
//                                        AssignmentMarkRepository assignmentMarkRepository,
//                                        StudyHoursRepository studyHoursRepository,
//                                        FinalMarkRepository finalMarkRepository,
//                                        SyllabusRepository syllabusRepository
//    ) {
//
//        return args -> {
//            Student student = new Student(
//                    "ΓΚΟΥΛΗ ΝΙΚΟΛΕΤΑ",
//                    "ΠΑΝΕΠΙΣΤΗΜΙΟ ΜΑΚΕΔΟΝΙΑΣ",
//                    "ΕΦΑΡΜΟΣΜΕΝΗ ΠΛΗΡΟΦΟΡΙΚΗ",
//                    "dai19252",
//                    "dai19252@uom.edu.gr",
//                    "gkouli_UOM23"
//
//            );
//
//
//            Secretariat secretariat = new Secretariat(
//                    "ΠΑΝΕΠΙΣΤΗΜΙΟ ΜΑΚΕΔΟΝΙΑΣ",
//                    "ΕΦΑΡΜΟΡΜΕΝΗ ΠΛΗΡΟΦΟΡΙΚΗ",
//                    "daisecr@uom.edu.gr",
//                    "dai_SecrUOM23"
//            );
//
//
//
//
//
//            Professor professor = new Professor(
//                    "ΑΜΠΑΤΖΟΓΛΟΥ ΑΠΟΣΤΟΛΟΣ",
//                    "ΠΑΝΕΠΙΣΤΗΜΙΟ ΜΑΚΕΔΟΝΙΑΣ",
//                    "ΕΦΑΡΜΟΡΜΕΝΗ ΠΛΗΡΟΦΟΡΙΚΗ",
//                    "a.ampatzoglou@uom.edu.gr",
//                    "ampa_UOM23"
//            );
//
//            Professor professor1 = new Professor(
//                    "ΣΑΤΡΑΤΖΕΜΗ ΜΑΡΙΑ",
//                    "ΠΑΝΕΠΙΣΤΗΜΙΟ ΜΑΚΕΔΟΝΙΑΣ",
//                    "ΕΦΑΡΜΟΡΜΕΝΗ ΠΛΗΡΟΦΟΡΙΚΗ",
//                    "maya@uom.edu.gr",
//                    "maya_UOM23"
//            );
//
//            Professor professor2 = new Professor(
//                    "ΣΑΜΑΡΑΣ ΝΙΚΟΛΑΟΣ",
//                    "ΠΑΝΕΠΙΣΤΗΜΙΟ ΜΑΚΕΔΟΝΙΑΣ",
//                    "ΕΦΑΡΜΟΡΜΕΝΗ ΠΛΗΡΟΦΟΡΙΚΗ",
//                    "samaras@uom.edu.gr",
//                    "sam_UOM23"
//            );
//
//            Professor professor3 = new Professor(
//                    "ΕΜΜΑΝΟΥΗΛ ΣΤΕΙΑΚΑΚΗΣ",
//                    "ΠΑΝΕΠΙΣΤΗΜΙΟ ΜΑΚΕΔΟΝΙΑΣ",
//                    "ΕΦΑΡΜΟΡΜΕΝΗ ΠΛΗΡΟΦΟΡΙΚΗ",
//                    "stiakakis@uom.edu.gr",
//                    "stiak_UOM23"
//            );
//
//
//
//            Course course = new Course("ΠΟΙΟΤΗΤΑ ΛΟΓΙΣΜΙΚΟΥ",3,5);
//            Course course1 = new Course("ΔΙΑΔΙΚΑΣΤΙΚΟΣ ΠΡΟΓΡΑΜΜΑΤΙΣΜΟΣ",1,1);
//            Course course2 = new Course("ΑΛΓΟΡΙΘΜΟΙ",1,1);
//            Course course3 = new Course("ΕΠΙΧΕΙΡΗΣΙΑΚΗ ΕΡΕΥΝΑ",3,6);
//            Course course4 = new Course("ΑΝΑΠΤΥΞΗ ΕΦΑΡΜΟΓΩΝ ΓΙΑ ΚΙΝΗΤΕΣ ΣΥΣΚΕΥΕΣ",4,7);
//            Course course5 = new Course("ΨΗΦΙΑΚΗ ΟΙΚΟΝΟΜΙΚΗ",2,4);
//            Course course6 = new Course("ΨΗΦΙΑΚΑ ΝΟΜΙΣΜΑΤΑ",4,7);
//            Course course7 = new Course("ΠΛΗΡΟΦΟΡΙΚΗ ΚΑΙ ΒΙΩΣΙΜΗ ΑΝΑΠΤΥΞΗ",4,7);
//
//
//            course.setProfessor(professor);
//            professor.addCourse(course);
//
//            course1.setProfessor(professor1);
//            professor1.addCourse(course1);
//
//            course2.setProfessor(professor2);
//            professor2.addCourse(course2);
//
//            course3.setProfessor(professor2);
//            professor2.addCourse(course3);
//
//            course4.setProfessor(professor);
//            professor.addCourse(course4);
//
//            course5.setProfessor(professor3);
//            professor3.addCourse(course5);
//
//            course6.setProfessor(professor3);
//            professor3.addCourse(course6);
//
//            course7.setProfessor(professor3);
//            professor3.addCourse(course7);
//
//            secretariat.setCourseList(Arrays.asList(course,course1,course2,course3,course4,course5,course6,course7));
//
//            student.setCourseList(Arrays.asList(course,course1,course4,course7));
//
//            Assignment assignment = new Assignment(
//                    "1.Εφαρμογή Μετρικών",
//                    15,
//                    LocalDate.of(2023,3,10)
//            );
//            Assignment assignment1 = new Assignment(
//                    "2.Αξιολόγηση Ποιότητας",
//                    25,
//                    LocalDate.of(2023,4,28)
//            );
//            Assignment assignment3 = new Assignment(
//                    "Πρόοδος",
//                    50,
//                    LocalDate.of(2023,4,3)
//            );
//
//
//
//            course.addAssignment(assignment);
//            assignment.setCourse(course);
//            course.addAssignment(assignment1);
//            assignment1.setCourse(course);
//
//            course1.addAssignment(assignment3);
//            assignment3.setCourse(course1);
//
//
//            Syllabus syllabus = new Syllabus(
//                    "1.Εισαγωγή",
//                    1
//            );
//
//            Syllabus syllabus1 = new Syllabus(
//                    "2.Είδη Μετρικών",
//                    3
//            );
//
//            course.addSyllabus(syllabus);
//            syllabus.setCourse(course);
//
//            course.addSyllabus(syllabus1);
//            syllabus1.setCourse(course);
//
//            AssignmentMark assignmentMark = new AssignmentMark(5F);
//            AssignmentMark assignmentMark1 = new AssignmentMark(7F);
//
//
//            student.addAssignmentMark(assignmentMark);
//            assignmentMark.setStudent(student);
//            assignment.addAssignmentMark(assignmentMark);
//            assignmentMark.setAssignment(assignment);
//
//            student.addAssignmentMark(assignmentMark1);
//            assignmentMark1.setStudent(student);
//            assignment1.addAssignmentMark(assignmentMark1);
//            assignmentMark1.setAssignment(assignment1);
//
//
//
////            StudyHours studyHours = new StudyHours(LocalDate.of(2023,5,22), 2);
////
////            student.addStudyHours(studyHours);
////            studyHours.setStudent(student);
////            course.addStudyHours(studyHours);
////            studyHours.setCourse(course);
//
//
//
//
//
//
//
//
//            assignmentRepository.saveAll(Arrays.asList(assignment, assignment1));
//
//
//            syllabusRepository.save(syllabus);
//            syllabusRepository.save(syllabus1);
//
//
//
//
////          studentRepository.save(student1);
////          studentRepository.save(student2);
////          studentRepository.save(student3);
//
////            finalMarkRepository.save(finalMark);
//
//            assignmentMarkRepository.save(assignmentMark);
//            assignmentMarkRepository.save(assignmentMark1);
//
//
//            courseRepository.saveAll(Arrays.asList(course,course1,course2,course3,course4,course5,course6,course7));
//
//            professorRepository.save(professor);
//            professorRepository.save(professor1);
//            professorRepository.save(professor2);
//
//            studentRepository.save(student);
//
//
//
//            secretariatRepository.save(secretariat);
//
////          studyHoursRepository.save(studyHours);
//
//
//        };
//    }
//}
