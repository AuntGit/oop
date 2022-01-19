package group;

import student.CaffeinetedStudent;
import student.EducatedStudent;
import student.Student;
import services.StudentService;
import student.Studentable;

import java.util.*;

public class Group extends Observable implements Iterable<Student> {

    private List<Studentable> groepsleden;
    private final int groupNumber;
    private final StudentService service;
    private GroupState state;

    public Group(StudentService service, int groupNumber, Long... leden) {
        this.service = service;
        this.groupNumber = groupNumber;
        this.state = new SpecificationState();
        this.groepsleden = new ArrayList<>();
        for (Long id: leden) {
            groepsleden.add(this.service.getStudentById(id));
        }
    }

    // STATE PATTERN
    public void getToWork() {
        System.out.println(String.format("Group %d is going to work.", this.groupNumber));
        this.state.work(this);
    }

    public void revert() {
        System.out.println(String.format("O nos! Group %d has to go back", this.groupNumber));
        this.state.revert(this);
    }

    public void commit() {
        System.out.println(String.format("Group %d is committing its work.", this.groupNumber));
        this.state.commit(this);
    }

    protected void setState(GroupState state) {
        this.state = state;
        this.hasChanged();
        this.notifyObservers();
    }

    //DECORATOR PATTERN
    public void rondjeKoffie() {
        for (int i = 0; i < this.groepsleden.size(); i++) {
            this.groepsleden.set(i, new CaffeinetedStudent(this.groepsleden.get(i)));
        }
        this.hasChanged();
        this.notifyObservers();
    }

    public void getEducated(String subject) {
        for (int i = 0; i < this.groepsleden.size(); i++) {
           this.groepsleden.set(i, new EducatedStudent(this.groepsleden.get(i), subject));
        }
        this.hasChanged();
        this.notifyObservers();
    }

    //ITERATOR PATTERN
    @Override
    public Iterator<Student> iterator() {
        return new GroupIterator(this.groepsleden);
    }


    public void addStudent(Student student) {
        this.groepsleden.add(student);
    }

    public boolean removeStudent(Long id) {
        Student toRemove = this.service.getStudentById(id);
        return this.groepsleden.remove(toRemove);
    }

    @Override
    public String toString() {
        String rv = "GROEP NUMMER " +this.groupNumber;
        rv += "------\nGROEPSLEDEN";
        for (Studentable st: groepsleden) {
            rv += st.toString();
        }
        rv += "Groepsstatus: " + this.state.toString();
        return rv;
    }
}